package custom.orm;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

/**
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
            st.executeUpdate(sql);
        } catch (SQLException e) {
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
        String idRowName = tableInfo.getIdFieldName();
        String sql = "SELECT * FROM " + tableName + " WHERE " + idRowName + " = " + id + ";";
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
}





























