package custom.orm.service;

import org.apache.ibatis.jdbc.ScriptRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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

    private static final Logger LOGGER = LoggerFactory.getLogger(ConnectionManager.class);
    private static final String DB_INIT_SCRIPT_PATH = "resources/custom/orm/orm_database.sql";

    private static Connection connection;
    private static String driverName;
    private static String url;
    private static String username;
    private static String password;
    static String schema;
    static String mappingStrategy;

    static {
        ResourceBundle resourceBundle = ResourceBundle.getBundle("custom/orm/connection");
        driverName = resourceBundle.getString("driverName");
        url = resourceBundle.getString("url");
        username = resourceBundle.getString("username");
        password = resourceBundle.getString("password");
        schema = resourceBundle.getString("schema");
        mappingStrategy = resourceBundle.getString("defaultMappingStrategy");
    }

    /**
     * Obtains a connection
     *
     * @return obtained connection
     */
    public static Connection getConnection() {
        try {
            Class.forName(driverName);
            connection = DriverManager.getConnection(url, username, password);

        } catch (ClassNotFoundException | SQLException e) {
            LOGGER.error(e.getMessage());
        }
        return connection;
    }

    /**
     * Explicitly closes connection. Preferably use try-with-resources instead of this method
     */
    public static void closeConnection() {
        try {
            connection.close();
        } catch (SQLException e) {
            LOGGER.error(e.getMessage());
        }
    }

    /**
     * Initializes database from resources
     */
    public static void initDataBase() {
        try (Connection connection = getConnection()) {
            ScriptRunner runner = new ScriptRunner(connection);
            Reader reader = new BufferedReader(new FileReader(DB_INIT_SCRIPT_PATH));
            runner.runScript(reader);
        } catch (IOException | SQLException e) {
            LOGGER.error(e.getMessage());
        }
    }
}
