package controller;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Random;

import model.Tower;
import util.DBConnection;

public class PullContrttoller {
    public Tower HandlePull(int rdtier){
        Tower twpull = new Tower(rdtier);
        return pull(twpull);
    }

    public Tower pull(Tower tw){

        String query = "select * from tower_catalog where tier = ? and is_enabled = TRUE order by random() limit 1";

        try {
            Connection conn = DBConnection.getConnection();

            PreparedStatement prest = conn.prepareStatement(query);

            prest.setInt(1, tw.geTier());

            ResultSet rs = prest.executeQuery();
            if(rs.next()){

                int id = rs.getInt("id");
                String towername = rs.getString("name");
                int tier = rs.getInt("tier");

                int base_dmg = rs.getInt("base_dmg");
                String towerImgUrl = rs.getString("tower_img_url");
                System.out.println(id + " "+ towername + " " + tier);

                System.out.println(towerImgUrl);

                return new Tower(id, towername, tier, base_dmg, towerImgUrl);
            }


        } catch (SQLException exception) {
            exception.printStackTrace();
        }
        return null;
    }
}
