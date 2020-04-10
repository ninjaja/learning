package custom.orm;

import custom.orm.annotations.JoinColumn;
import custom.orm.annotations.OneToMany;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static org.apache.commons.collections4.CollectionUtils.isNotEmpty;

/**
 * Class contains CRUD operations to operate with objects and their persistence in DB.
 *
 * @author Dmitry Matrizaev
 * @since 1.0
 */
public class OrmManager {

    private static final String SCHEMA = ConnectionManager.schema;

    public void create(Object object) {
        TableInfoService tableInfo = new TableInfoService(object);
        String tableName = SCHEMA + "." + tableInfo.defineTableName();
        String rowsString = tableInfo.getColumnsNamesWithoutIdString();
        String fieldValuesString = tableInfo.getValuesWithoutIdString();
        String sql = "INSERT INTO " + tableName + "(" + rowsString + ")" + " VALUES (" + fieldValuesString + ");";
        try (Connection connection = ConnectionManager.getConnection(); Statement st = connection.createStatement()) {
            st.executeUpdate(sql, Statement.RETURN_GENERATED_KEYS);
            //NOTE: further goes a "dirty trick" to set id to the persisted object from DB, thus changing input argument which is no good in real projects
            ResultSet rs = st.getGeneratedKeys();
            rs.next();
            Field idField = tableInfo.getIdField();
            Method setter = new PropertyDescriptor(idField.getName(), object.getClass()).getWriteMethod();
            setter.invoke(object, rs.getInt(1));
        } catch (SQLException | IntrospectionException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    public <T> T getById(Class<T> type, int id) {
        Object object = null;
        try {
            object = type.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }
        TableInfoService tableInfo = new TableInfoService(object);
        ResultSetToObjectMapper<T> mapper = new ResultSetToObjectMapper<>();
        String tableName = SCHEMA + "." + tableInfo.defineTableName();
        String idColumnName = tableInfo.getIdColumnName();

        String sql = "SELECT * FROM " + tableName + " WHERE " + idColumnName + " = " + id + ";";
        List<T> results;
        T result = null;
        try (Connection connection = ConnectionManager.getConnection(); Statement st = connection.createStatement()) {
            ResultSet resultSet = st.executeQuery(sql);
            results = mapper.getObjects(resultSet, type);
            int resultsSize = results.size();
            if (resultsSize == 1) {
                result = results.get(0);
                processInnerEntities(result);

            } else if (resultsSize > 1) {
                throw new CustomOrmException("Bad id: more than one element found");
            } else {
                throw new CustomOrmException("Bad id: no elements found");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    public <T> List<T> getAll(Class<T> type) {
        Object object = null;
        try {
            object = type.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }
        TableInfoService tableInfo = new TableInfoService(object);
        ResultSetToObjectMapper<T> mapper = new ResultSetToObjectMapper<>();
        String tableName = SCHEMA + "." + tableInfo.defineTableName();
        String sql = "SELECT * FROM " + tableName + ";";
        List<T> results = new ArrayList<>();

        try (Connection connection = ConnectionManager.getConnection(); Statement st = connection.createStatement()) {
            ResultSet resultSet = st.executeQuery(sql);
            results = mapper.getObjects(resultSet, type);
            for (T result : results) {
                processInnerEntities(result);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return results;
    }

    public void update(Object object, int id) {
        TableInfoService tableInfo = new TableInfoService(object);
        String tableName = SCHEMA + "." + tableInfo.defineTableName();
        String idColumnName = tableInfo.getIdColumnName();
        List<String> columnsNames = tableInfo.getColumnsNamesWithoutId();
        List<String> fieldsValues = tableInfo.getValuesWithoutId();
        StringBuilder assignments = new StringBuilder();
        if (isNotEmpty(columnsNames) && isNotEmpty(fieldsValues) && columnsNames.size() == fieldsValues.size()) {
            for (int i = 0; i < columnsNames.size(); i++) {
                assignments.append(columnsNames.get(i)).append(" = ").append(fieldsValues.get(i));
                if (i < columnsNames.size() - 1) {
                    assignments.append(",");
                }
            }
        } else {
            throw new CustomOrmException("Fields' names and values number mismatch in: " + object.getClass().getName());
        }
        String sql = "UPDATE " + tableName + " SET " + assignments + " WHERE " + idColumnName + " = " + id;
        try (Connection connection = ConnectionManager.getConnection(); Statement st = connection.createStatement()) {
            if (Objects.nonNull(getById(object.getClass(), id))) {
                st.executeUpdate(sql);
            } else {
                throw new CustomOrmException("No entry to update with id: " + id);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void delete(Object object, int id) {
        TableInfoService tableInfo = new TableInfoService(object);
        String tableName = SCHEMA + "." + tableInfo.defineTableName();
        String idColumnName = tableInfo.getIdColumnName();
        String sql = "DELETE FROM " + tableName + " WHERE " + idColumnName + " = " + id + ";";
        try (Connection connection = ConnectionManager.getConnection(); Statement st = connection.createStatement()) {
            st.executeUpdate(sql);
            System.out.println("Deleted from DB element: " + object.getClass().getName() + ", id: " + id);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private <T> void processInnerEntities(Object inputObject) {
        try(Connection connection = ConnectionManager.getConnection(); Statement st = connection.createStatement()) {
            Map<String, Field> fieldsWithInnerEntities = TableInfoService.getFieldsWithInnerEntities(inputObject);
            if (!fieldsWithInnerEntities.isEmpty()) {
                for (Map.Entry<String, Field> entry : fieldsWithInnerEntities.entrySet()) {
                    String innerTableName = SCHEMA + "." + entry.getKey();
                    Type innerEntityType = entry.getValue().getGenericType();
                    Class<?> innerEntityClass;
                    if (innerEntityType instanceof ParameterizedType && entry.getValue().isAnnotationPresent(OneToMany.class)) {
                        //OneToMany case (inner collection):
                        ParameterizedType pType = (ParameterizedType) innerEntityType;
                        //get collection's generic parameter:
                        innerEntityClass = (Class<?>) pType.getActualTypeArguments()[0];
                        String mappedFieldName = entry.getValue().getAnnotation(OneToMany.class).mappedBy();
                        String mappedIdColumnName = innerEntityClass.getDeclaredField(mappedFieldName).getAnnotation(JoinColumn.class).name();
                        Method idGetter = new PropertyDescriptor(TableInfoService.getIdField(inputObject.getClass().getDeclaredFields()).getName(), inputObject.getClass()).getReadMethod();
                        int idValue = (Integer) idGetter.invoke(inputObject);

                        String innerSql = "SELECT * FROM " + innerTableName + " WHERE " + mappedIdColumnName + " = " + idValue;
                        List<T> innerBeans;
                        try (ResultSet innerResultSet = st.executeQuery(innerSql)) {
                            ResultSetToObjectMapper<T> innerMapper = new ResultSetToObjectMapper<>();
                            innerBeans = innerMapper.getObjects(innerResultSet, innerEntityClass);
                        }
                        if (!innerBeans.isEmpty()) {
                            // populate inner instances:
                            Method collectionFieldGetter = new PropertyDescriptor(entry.getValue().getName(), inputObject.getClass()).getReadMethod();
                            Object collection = collectionFieldGetter.invoke(inputObject);
                            if (Objects.isNull(collection)) {
                                Type collectionType = entry.getValue().getType();
                                if (collectionType.getTypeName().contains("Set")) {
                                    collection = new HashSet<>();
                                } else if (collectionType.getTypeName().contains("List")) {
                                    collection = new LinkedList<>();
                                } else {
                                    throw new CustomOrmException("Unknown collection type in @OneToMany field");
                                }
                            }
                            Method add = collection.getClass().getDeclaredMethod("add", Object.class);
                            for (T innerBean : innerBeans) {
                                add.invoke(collection, innerBean);
                            }
                        }
                    } else {
                        //ManyToOne case (inner entity):
                        innerEntityClass = entry.getValue().getType();
                        Field[] innerEntityFields = innerEntityClass.getDeclaredFields();
                        String innerEntityFieldName = entry.getValue().getName();
                        Method innerEntityGetter = new PropertyDescriptor(innerEntityFieldName, inputObject.getClass()).getReadMethod();
                        Object innerInstance = innerEntityGetter.invoke(inputObject);

                        // getting inner entity id value:
                        String innerEntityIdColumn = TableInfoService.getIdColumnName(innerEntityFields);
                        String innerEntityIdFieldName = TableInfoService.getIdField(innerEntityFields).getName();
                        Method innerEntityIdGetter = new PropertyDescriptor(innerEntityIdFieldName, innerEntityClass).getReadMethod();
                        String innerEntityIdValue = innerEntityIdGetter.invoke(innerInstance).toString();

                        String innerSql = "SELECT * FROM " + innerTableName + " WHERE " + innerEntityIdColumn + " = " + innerEntityIdValue;
                        Object innerBean;
                        try (ResultSet innerResultSet = st.executeQuery(innerSql)) {
                            ResultSetToObjectMapper<T> innerMapper = new ResultSetToObjectMapper<>();
                            // populate inner instances:
                            innerBean = innerMapper.getObjects(innerResultSet, innerEntityClass).get(0);
                        }
                        Method innerBeanSetter = new PropertyDescriptor(innerEntityFieldName, inputObject.getClass()).getWriteMethod();
                        innerBeanSetter.invoke(inputObject, innerBean);
                    }
                }
            }
        } catch (NoSuchFieldException | NoSuchMethodException | IntrospectionException | InvocationTargetException | IllegalAccessException | SQLException e) {
            e.printStackTrace();
        }
    }
}





























