package model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
    private static  String URL = "jdbc:mysql://localhost:3306/control_coches";
    private static  String USER = "root";
    private static String PASSWORD = "PracticaRoot";
    private static Connection connection;

    public static Connection getConnection() throws SQLException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver"); //  fuerza la carga del driver Preguntar antonio
        } catch (ClassNotFoundException e) {
            throw new SQLException(" JDBC Driver no encontrado", e);
        }

        if (connection == null || connection.isClosed()) {
            connection = DriverManager.getConnection(URL, USER, PASSWORD);
        }
        return connection;
    }
}
