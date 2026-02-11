package app;

import java.sql.Connection;
import java.sql.DriverManager;

public class DB {

    private static final String URL =
            "jdbc:sqlite::resource:notas.db";

    public static Connection connect() {
        try {
            return DriverManager.getConnection(URL);
        } catch (Exception e) {
            System.err.println("❌ Error de conexión a la BD");
            e.printStackTrace();
            return null;
        }
    }
}
