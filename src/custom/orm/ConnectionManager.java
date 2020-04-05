package custom.orm;

import org.apache.ibatis.jdbc.ScriptRunner;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ResourceBundle;

/**
 * Database connectivity layer configured by properties bundle.
 *
 * @author Dmitry Matrizaev
 * @since 1.0
 */
public class ConnectionManager {

    private static Connection connection;
    private static String driverName;
    private static String url;
    private static String username;
    private static String password;
    static String schema;

    static {
        ResourceBundle resourceBundle = ResourceBundle.getBundle("custom/orm/connection");
        driverName = resourceBundle.getString("driverName");
        url = resourceBundle.getString("url");
        username = resourceBundle.getString("username");
        password = resourceBundle.getString("password");
        schema = resourceBundle.getString("schema");
    }

    public static Connection getConnection() {
        try {
            Class.forName(driverName);
            connection = DriverManager.getConnection(url, username, password);

        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
        return connection;
    }

    public static void closeConnection() {
        try {
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void initDataBase() {
        try (Connection connection = getConnection()) {
            ScriptRunner runner = new ScriptRunner(connection);
            Reader reader = new BufferedReader(new FileReader("resources/custom/orm/orm_database.sql"));
            runner.runScript(reader);
        } catch (IOException | SQLException e) {
            e.printStackTrace();
        }
    }
}
