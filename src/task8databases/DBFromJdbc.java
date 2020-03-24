package task8databases;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Demo of creating database via JDBC, {@code Statement}, {@code PreparedStatement}, {@code CallableStatement}.
 *
 * @author Dmitry Matrizaev
 * @since 1.0
 */
public class DBFromJdbc {

    private static final String URL =
            "jdbc:mysql://127.0.0.1:3306/mysql?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC";
    private static final String SQL_DROP_DATABASE = "DROP DATABASE IF EXISTS learning";
    private static final String SQL_CREATE_DATABASE = "CREATE DATABASE IF NOT EXISTS learning";
    private static final String SQL_ADD_STUDENT = "INSERT INTO learning.students (surname, name) VALUES (?,?)";
    private static final String SQL_CREATE_STUDENTS_TABLE =
            "CREATE TABLE IF NOT EXISTS learning.students(" +
                    "id TINYINT NOT NULL AUTO_INCREMENT, " +
                    "surname CHAR(30) NOT NULL, " +
                    "name CHAR(15) NOT NULL, " +
                    "PRIMARY KEY(id))";
    private static final String SQL_CREATE_STUDENTS_AUDIT_TABLE =
            "CREATE TABLE IF NOT EXISTS learning.students_audit(" +
                    "id TINYINT NOT NULL AUTO_INCREMENT, " +
                    "studentId TINYINT NOT NULL, " +
                    "surname CHAR(30) NOT NULL, " +
                    "name CHAR(15) NOT NULL, " +
                    "changedate DATETIME DEFAULT NULL, " +
                    "action VARCHAR(50) DEFAULT NULL, " +
                    "PRIMARY KEY(id))";

    public static void main(String[] args) {
        try (Connection connection = getConnection()) {
            dropAndCreateDataBase(connection);
            createStudentsTable(connection);
            createStudentsAuditTable(connection);
            addStudent(connection, "Ivanov", "Peter");
            addStudent(connection, "Petrov", "Fedor");
            addStudent(connection, "Sidorov", "Ivan");
            getStudentDataById(connection, 1);
            addStudentWithStoredProcedure(connection,"Babich", "Stepan");

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

    private static void createStudentsAuditTable(Connection connection) throws SQLException {
        Statement statement = connection.createStatement();
        statement.executeUpdate(SQL_CREATE_STUDENTS_AUDIT_TABLE);
    }

    private static void addStudent(Connection connection, String surname, String name) throws SQLException {
        PreparedStatement ps = connection.prepareStatement(SQL_ADD_STUDENT);
        ps.setString(1, surname);
        ps.setString(2, name);
        ps.executeUpdate();
    }

    private static void getStudentDataById(Connection connection, int id) throws SQLException {
        String query = "{ call learning.get_student_data(?) }";
        ResultSet rs;
        CallableStatement statement = connection.prepareCall(query);
        statement.setInt(1, id);
        rs = statement.executeQuery();
        while (rs.next()) {
            System.out.println(rs.getString("surname") + " " + rs.getString("name"));
        }
    }

    private static void addStudentWithStoredProcedure(Connection connection, String surname, String name) throws SQLException {
        String query = "{ call learning.insertStudent(?,?) }";
        CallableStatement statement = connection.prepareCall(query);
        statement.setString(1, surname);
        statement.setString(2, name);
        statement.executeQuery();
    }
}
