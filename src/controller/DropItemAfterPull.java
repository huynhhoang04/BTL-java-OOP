package controller;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import model.Item;
import model.User;
import util.DBConnection;

public class DropItemAfterPull {
    public void dropitem(User user, int itemId){
        String query = "update inventory set qty = qty - 1 where account_id = ? and item_id = ?";

        try {
            Connection conn = DBConnection.getConnection();
            PreparedStatement prest = conn.prepareStatement(query);

            prest.setInt(1, user.getId());
            prest.setInt(2, itemId);

            prest.execute();
            
        } catch (SQLException exception) {
            // TODO: handle exception
            exception.printStackTrace();
        }
    }
}
