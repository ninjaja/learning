package custom_orm;

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
 * @author Dmitry Matrizaev
 * @since 1.0
 */
public class ConnectionManager {

    public static void main(String[] args) {
        initDataBase();
    }

    private static Connection connection;
    private static String driverName;
    static String url;
    static String username;
    static String password;

    static {
        ResourceBundle resourceBundle = ResourceBundle.getBundle("custom_orm/connection");
        driverName = resourceBundle.getString("driverName");
        url = resourceBundle.getString("url");
        username = resourceBundle.getString("username");
        password = resourceBundle.getString("password");
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
            Reader reader = new BufferedReader(new FileReader("resources/custom_orm/orm_database.sql"));
            runner.runScript(reader);
        } catch (IOException | SQLException e) {
            e.printStackTrace();
        }
    }
}
