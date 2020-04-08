package custom.orm;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
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
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        TableInfoService tableInfo = new TableInfoService(object);
        ResultSetToObjectMapper<T> mapper = new ResultSetToObjectMapper<>();
        String tableName = SCHEMA + "." + tableInfo.defineTableName();
        String idColumnName = tableInfo.getIdColumnName();

        /*//inner entities processing
        Map<String, Field> fieldsWithInnerEntities = tableInfo.getFieldsWithInnerEntities();
        if (!fieldsWithInnerEntities.isEmpty()) {
            String joinTableName = SCHEMA +
            Set<?> innerObjects = new HashSet<>();
            String innerEntries = "SELECT * FROM " + joinTableName + " WHERE "
        }*/

        String sql = "SELECT * FROM " + tableName + " WHERE " + idColumnName + " = " + id + ";";
        List<T> results;
        T result = null;
        try (Connection connection = ConnectionManager.getConnection(); Statement st = connection.createStatement()) {
            ResultSet resultSet = st.executeQuery(sql);
            results = mapper.getObject(resultSet, type);
            int resultsSize = results.size();
            if (resultsSize == 1) {
                result = results.get(0);
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
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        TableInfoService tableInfo = new TableInfoService(object);
        ResultSetToObjectMapper<T> mapper = new ResultSetToObjectMapper<>();
        String tableName = SCHEMA + "." + tableInfo.defineTableName();
        String sql = "SELECT * FROM " + tableName + ";";
        List<T> results = new ArrayList<>();
        try (Connection connection = ConnectionManager.getConnection(); Statement st = connection.createStatement()) {
            ResultSet resultSet = st.executeQuery(sql);
            results = mapper.getObject(resultSet, type);
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
}





























