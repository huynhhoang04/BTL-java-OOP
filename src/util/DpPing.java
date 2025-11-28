
package util;

import java.sql.*;

public class DpPing {
    public static void main(String[] args) {
        try (Connection c = DBConnection.getConnection();
             Statement st = c.createStatement();
             ResultSet rs = st.executeQuery("select version()")) {
            if (rs.next()) {
                System.out.println("OK: " + rs.getString(1));
            }
        } catch (SQLException e) {
            System.err.println("FAIL: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
