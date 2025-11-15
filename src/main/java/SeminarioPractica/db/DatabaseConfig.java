package SeminarioPractica.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConfig {


    private static final String URL = "jdbc:mysql://localhost:3306/andina_solicitudes?useSSL=false&serverTimezone=America/Argentina/Buenos_Aires";
    private static final String USER = "root";
    private static final String PASSWORD = "";


    static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            System.err.println("Error cargando driver MySQL JDBC");
            e.printStackTrace();
        }
    }

    private DatabaseConfig() {
        // evitar instanciación
    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}
