package app;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class LoginService {

    public static boolean login(String usuario, String password, String rol) {

        String sql = """
            SELECT 1
            FROM usuarios
            WHERE nombre = ?
              AND password = ?
              AND rol = ?
        """;

        try (Connection con = DB.connect();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, usuario.trim());
            ps.setString(2, password.trim());
            ps.setString(3, rol.toLowerCase().trim()); 

            ResultSet rs = ps.executeQuery();
            return rs.next();

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
