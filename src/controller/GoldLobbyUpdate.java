package controller;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import util.DBConnection;

public class GoldLobbyUpdate {
    public long getGoldById(long accountId) {
        String sql = "SELECT gold FROM accounts WHERE id = ?";
        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setLong(1, accountId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return rs.getLong("gold");
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return 0L;
    }
}
