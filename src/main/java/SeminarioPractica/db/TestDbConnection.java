package SeminarioPractica.db;

import java.sql.Connection;

public class TestDbConnection {

    public static void main(String[] args) {
        try (Connection conn = DatabaseConfig.getConnection()) {
            System.out.println("Conexión exitosa: " + conn.getMetaData().getURL());
        } catch (Exception e) {
            System.out.println("Error de conexión");
            e.printStackTrace();
        }
    }
}
