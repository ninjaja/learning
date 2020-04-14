package custom.orm.service;

import custom.orm.annotations.ManyToOne;
import custom.orm.annotations.OneToOne;
import custom.orm.exception.CustomOrmException;
import custom.orm.annotations.JoinColumn;
import custom.orm.annotations.OneToMany;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
import java.util.Collection;
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

    private static final Logger LOGGER = LoggerFactory.getLogger(OrmManager.class);
    private static final String SCHEMA = ConnectionManager.schema;

    /**
     * Saves an Object to DB.
     * @param object Object to save
     */
    public void create(Object object) {
        TableInfoService tableInfo = new TableInfoService(object);
        String tableName = SCHEMA + "." + tableInfo.defineTableName();
        String rowsString = tableInfo.getColumnsNamesWithoutIdString();
        String fieldValuesString = tableInfo.getValuesWithoutIdString();
        String sql = "INSERT INTO " + tableName + "(" + rowsString + ")" + " VALUES (" + fieldValuesString + ");";
        try (Connection connection = ConnectionManager.getConnection(); Statement st = connection.createStatement()) {
            st.executeUpdate(sql, Statement.RETURN_GENERATED_KEYS);
            try (ResultSet rs = st.getGeneratedKeys()) {
                rs.next();
                Field idField = TableInfoService.getIdField(object.getClass().getDeclaredFields());
                Method setter = new PropertyDescriptor(idField.getName(), object.getClass()).getWriteMethod();
                setter.invoke(object, rs.getInt(1));
            }
        } catch (SQLException | IntrospectionException | IllegalAccessException | InvocationTargetException e) {
            LOGGER.error(e.getMessage());
        }
    }

    /**
     * Retrieves an Object from DB with known Object Class and id.
     * @param type Object's class to cast to
     * @param id id of the Object in DB
     * @param <T> the type to cast this object to
     * @return retrieved Object
     */
    public <T> T getById(Class<T> type, int id) {
        Object object = null;
        try {
            object = type.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            LOGGER.error(e.getMessage());
        }
        TableInfoService tableInfo = new TableInfoService(object);
        String tableName = SCHEMA + "." + tableInfo.defineTableName();
        String idColumnName = tableInfo.getIdColumnName();

        String sql = "SELECT * FROM " + tableName + " WHERE " + idColumnName + " = " + id + ";";
        List<T> results;
        T result = null;
        try (Connection connection = ConnectionManager.getConnection(); Statement st = connection.createStatement()) {
            ResultSet resultSet = st.executeQuery(sql);
            ResultSetToObjectMapper<T> mapper = new ResultSetToObjectMapper<>(resultSet, type);
            results = mapper.getObjects();
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
            LOGGER.error(e.getMessage());
        }
        return result;
    }

    /**
     * Retrieves all Objects of specified type found in DB.
     * @param type Object's class to cast to
     * @param <T> the type to cast this object to
     * @return all found objects as List
     */
    public <T> List<T> getAll(Class<T> type) {
        Object object = null;
        try {
            object = type.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            LOGGER.error(e.getMessage());
        }
        TableInfoService tableInfo = new TableInfoService(object);
        String tableName = SCHEMA + "." + tableInfo.defineTableName();
        String sql = "SELECT * FROM " + tableName + ";";
        List<T> results = new ArrayList<>();

        try (Connection connection = ConnectionManager.getConnection(); Statement st = connection.createStatement()) {
            ResultSet resultSet = st.executeQuery(sql);
            ResultSetToObjectMapper<T> mapper = new ResultSetToObjectMapper<>(resultSet, type);
            results = mapper.getObjects();
            for (T result : results) {
                processInnerEntities(result);
            }
        } catch (SQLException e) {
            LOGGER.error(e.getMessage());
        }
        return results;
    }

    /**
     * Updates object in DB by specified id. If object not found {@code CustomOrmException} is thrown.
     * @param object object to update
     * @param id object's id
     */
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
            LOGGER.error(e.getMessage());
        }
    }

    /**
     * Removes object from DB by id.
     * @param object object to delete
     * @param id id of the object
     */
    public void delete(Object object, int id) {
        TableInfoService tableInfo = new TableInfoService(object);
        String tableName = SCHEMA + "." + tableInfo.defineTableName();
        String idColumnName = tableInfo.getIdColumnName();
        String sql = "DELETE FROM " + tableName + " WHERE " + idColumnName + " = " + id + ";";
        try (Connection connection = ConnectionManager.getConnection(); Statement st = connection.createStatement()) {
            st.executeUpdate(sql);
            LOGGER.info("Deleted from DB element: " + object.getClass().getName() + ", id: " + id);
        } catch (SQLException e) {
            LOGGER.error(e.getMessage());
        }
    }

    private String innerTableName;

    private void processInnerEntities(Object inputObject) {
        try {
            Map<String, Field> fieldsWithInnerEntities = TableInfoService.getFieldsWithInnerEntities(inputObject);
            if (!fieldsWithInnerEntities.isEmpty()) {
                for (Map.Entry<String, Field> entry : fieldsWithInnerEntities.entrySet()) {
                    innerTableName = SCHEMA + "." + entry.getKey();
                    Field field = entry.getValue();
                    Type innerEntityType = field.getGenericType();
                    if (innerEntityType instanceof ParameterizedType && field.isAnnotationPresent(OneToMany.class)) {
                        processOneToMany(inputObject, innerEntityType, field);
                    } else if (field.isAnnotationPresent(ManyToOne.class) || field.isAnnotationPresent(OneToOne.class)) {
                        processManyToOne(inputObject, field);
                    }
                }
            }
        } catch (NoSuchFieldException | NoSuchMethodException | IntrospectionException | InvocationTargetException | IllegalAccessException e) {
            LOGGER.error(e.getMessage());
        }
    }

    private <T> void processOneToMany(Object inputObject, Type innerEntityType, Field field) throws NoSuchFieldException, IntrospectionException, InvocationTargetException, IllegalAccessException, NoSuchMethodException {
        ParameterizedType pType = (ParameterizedType) innerEntityType;
        //get collection's generic parameter:
        Class<T> innerEntityClass = (Class<T>) pType.getActualTypeArguments()[0];
        String mappedFieldName = field.getAnnotation(OneToMany.class).mappedBy();
        String mappedIdColumnName = innerEntityClass.getDeclaredField(mappedFieldName).getAnnotation(JoinColumn.class).name();
        Method idGetter = new PropertyDescriptor(TableInfoService.getIdField(inputObject.getClass().getDeclaredFields()).getName(), inputObject.getClass()).getReadMethod();
        int idValue = (Integer) idGetter.invoke(inputObject);
        String innerSql = "SELECT * FROM " + innerTableName + " WHERE " + mappedIdColumnName + " = " + idValue + ";";
        List<T> innerBeans;
        try (Connection connection = ConnectionManager.getConnection(); Statement st = connection.createStatement();
                ResultSet innerResultSet = st.executeQuery(innerSql)) {
            ResultSetToObjectMapper<T> innerMapper = new ResultSetToObjectMapper<>(innerResultSet, innerEntityClass);
            innerBeans = innerMapper.getObjects();
            if (!innerBeans.isEmpty()) {
                // populate inner instances:
                Method collectionFieldGetter = new PropertyDescriptor(field.getName(), inputObject.getClass()).getReadMethod();
                Object collection = collectionFieldGetter.invoke(inputObject);
                if (Objects.isNull(collection)) {
                    collection = instantiateCollection(field);
                }
                Method add = collection.getClass().getDeclaredMethod("add", Object.class);
                for (T innerBean : innerBeans) {
                    add.invoke(collection, innerBean);
                }
                Method collectionFieldSetter = new PropertyDescriptor(field.getName(), inputObject.getClass()).getWriteMethod();
                collectionFieldSetter.invoke(inputObject, collection);
            }
        } catch (SQLException e) {
            LOGGER.error(e.getMessage());
        }
    }

    private Collection<?> instantiateCollection(Field field) {
        Type collectionType = field.getType();
        if (collectionType.getTypeName().contains("Set")) {
            return new HashSet<>();
        } else if (collectionType.getTypeName().contains("List")) {
            return new LinkedList<>();
        } else {
            throw new CustomOrmException("Unknown collection type in @OneToMany field");
        }
    }

    private<T> void processManyToOne(Object inputObject, Field field) throws IntrospectionException,
            InvocationTargetException, IllegalAccessException {
        Class<T> innerEntityClass = (Class<T>) field.getType();
        Field[] innerEntityFields = innerEntityClass.getDeclaredFields();
        String innerEntityFieldName = field.getName();
        Method innerEntityGetter = new PropertyDescriptor(innerEntityFieldName, inputObject.getClass()).getReadMethod();
        Object innerInstance = innerEntityGetter.invoke(inputObject);
        // getting inner entity id value:
        String innerEntityIdColumn = TableInfoService.getIdColumnName(innerEntityFields);
        String innerEntityIdFieldName = TableInfoService.getIdField(innerEntityFields).getName();
        Method innerEntityIdGetter = new PropertyDescriptor(innerEntityIdFieldName, innerEntityClass).getReadMethod();
        String innerEntityIdValue = innerEntityIdGetter.invoke(innerInstance).toString();
        String innerSql = "SELECT * FROM " + innerTableName + " WHERE " + innerEntityIdColumn + " = " + innerEntityIdValue;
        Object innerBean;
        try (Connection connection = ConnectionManager.getConnection(); Statement st = connection.createStatement();
                ResultSet innerResultSet = st.executeQuery(innerSql)) {
            ResultSetToObjectMapper<T> innerMapper = new ResultSetToObjectMapper<>(innerResultSet, innerEntityClass);
            // populate inner instances:
            innerBean = innerMapper.getObjects().get(0);

        Method innerBeanSetter = new PropertyDescriptor(innerEntityFieldName, inputObject.getClass()).getWriteMethod();
        innerBeanSetter.invoke(inputObject, innerBean);
        } catch (SQLException e) {
            LOGGER.error(e.getMessage());
        }
    }
}
