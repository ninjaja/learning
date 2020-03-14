package task8databases;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class Main {

    public static void main(String[] args) throws ClassNotFoundException {
        String connectionUrl = "jdbc:mysql://127.0.0.1:3306/mysql?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC";
        String userName = "root";
        String password = "admin";
        Class.forName("com.mysql.cj.jdbc.Driver");
        try (Connection connection = DriverManager.getConnection(connectionUrl, userName, password);
             Statement statement = connection.createStatement()) {
            statement.executeUpdate("DROP DATABASE IF EXISTS learning");
            statement.executeUpdate("CREATE DATABASE learning");
            statement.executeUpdate("CREATE TABLE IF NOT EXISTS learning.students(id TINYINT NOT NULL AUTO_INCREMENT, surname CHAR(30) NOT NULL, name CHAR(15) NOT NULL, PRIMARY KEY(id))");
            statement.executeUpdate("INSERT INTO learning.students SET surname = 'Ivanov', name = 'Peter'");
            statement.executeUpdate("INSERT INTO learning.students SET surname = 'Petrov', name = 'Ivan'");
            statement.executeUpdate("INSERT INTO learning.students SET surname = 'Sidorov', name = 'Mikhail'");


        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
