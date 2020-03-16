package task8databases;

import java.sql.*;

public class StatementTesting {

    private static final String URL =
            "jdbc:mysql://127.0.0.1:3306/mysql?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC";
    private static final String SQL_DROP_DATABASE = "DROP DATABASE IF EXISTS learning";
    private static final String SQL_CREATE_DATABASE = "CREATE DATABASE IF NOT EXISTS learning";
    private static final String SQL_CREATE_STUDENTS_TABLE =
            "CREATE TABLE IF NOT EXISTS learning.students(" +
                    "id TINYINT NOT NULL AUTO_INCREMENT, " +
                    "surname CHAR(30) NOT NULL, " +
                    "name CHAR(15) NOT NULL, " +
                    "PRIMARY KEY(id))";
    private static final String SQL_ADD_STUDENT = "INSERT INTO learning.students (surname, name) VALUES (?,?)";

    public static void main(String[] args) {
        try (Connection connection = getConnection()) {
            dropAndCreateDataBase(connection);
            createStudentsTable(connection);
            addStudent(connection, "Ivanov", "Peter");
            addStudent(connection, "Petrov", "Fedor");
            addStudent(connection, "Sidorov", "Ivan");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static Connection getConnection() {
        Connection connection = null;
        String connectionUrl = URL;
        String userName = "root";
        String password = "admin";
        try {
            connection = DriverManager.getConnection(connectionUrl, userName, password);
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
        return connection;
    }

    private static void dropAndCreateDataBase(Connection connection) throws SQLException {
        Statement statement = connection.createStatement();
        statement.executeUpdate(SQL_DROP_DATABASE);
        statement.executeUpdate(SQL_CREATE_DATABASE);
    }

    private static void createStudentsTable(Connection connection) throws SQLException {
        Statement statement = connection.createStatement();
        statement.executeUpdate(SQL_CREATE_STUDENTS_TABLE);
    }

    private static void addStudent(Connection connection, String surname, String name) throws SQLException {
        PreparedStatement ps = connection.prepareStatement(SQL_ADD_STUDENT);
        ps.setString(1, surname);
        ps.setString(2, name);
        ps.executeUpdate();
    }
}
