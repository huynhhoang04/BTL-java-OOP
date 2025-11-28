package controller;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import model.Tower;
import model.User;
import util.DBConnection;

public class ListTower{
    public List<Tower> listTowers(User user) {
        String sql = """
            SELECT t.id, t.name, t.tier, t.base_dmg, t.tower_img_url
            FROM account_towers  as at
            JOIN tower_catalog t ON t.id = at.tower_id
            WHERE at.account_id = ?
            ORDER BY t.tier, t.name
        """;

        List<Tower> list = new ArrayList<>();

        try (Connection conn = DBConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, user.getId());

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                   int id = rs.getInt("id");
                    String name = rs.getString("name");
                    int tier = rs.getInt("tier");

                    int basedmg = rs.getInt("base_dmg");
                    String imgurl = rs.getString("tower_img_url");

                    list.add(new Tower(id, name, tier, basedmg, imgurl));

                }
            }
        }
        catch(SQLException exception){
            exception.printStackTrace();
        }
        return list;
    }

}
