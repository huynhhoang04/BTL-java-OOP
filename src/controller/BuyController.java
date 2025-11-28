package controller;

import model.User;
import util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import model.Item;

public class BuyController {
    public boolean HandleBuy(User user, Item item){
        String query = "update inventory set qty = qty + 1 where account_id = ? and item_id = ?; update accounts set gold = gold - ? where id = ?;";

        try {

            Connection conn = DBConnection.getConnection();
            PreparedStatement prest = conn.prepareStatement(query);

            prest.setInt(1, user.getId());
            prest.setInt(2, item.getId());
            prest.setInt(3, item.getIntPrice());
            prest.setInt(4, user.getId());

            if(prest.execute()){
                return true;
            }
            
        } catch (SQLException exception) {
            // TODO: handle exception
            exception.printStackTrace();
        }
        return false;
    }
}
