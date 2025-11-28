package controller;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import model.Item;
import util.DBConnection;

public class ShowItemInfo {
    public Item HandleDisplay(int id){
        Item forshow = new Item(id);
        return show(forshow);
    }

    public Item show(Item item){
        String query = "select * from items where id = ?";
        try {
            Connection conn = DBConnection.getConnection();
            PreparedStatement prest = conn.prepareStatement(query);

            prest.setInt(1, item.getId());
            ResultSet rs = prest.executeQuery();

            if(rs.next()){
                int id = rs.getInt("id");
                String itemname = rs.getString("code");
                int itmtier = rs.getInt("tier");
                int  price = rs.getInt("price");
                String imgUrl = rs.getString("item_img_url");

                return new Item(id, itemname, itmtier, price, imgUrl);
            }
        } catch (SQLException exception) {
            // TODO: handle exception
            exception.printStackTrace();
        }
        return null;
    }
}
