package controller;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import model.User;
import util.DBConnection;

public class CheckHaveChest {
    public boolean checkchest(User user, int tier){
        String query = "select qty from inventory where account_id = ? and item_id = ?";

        try {
            Connection conn = DBConnection.getConnection();
            PreparedStatement prest = conn.prepareStatement(query);

            prest.setInt(1, user.getId());
            prest.setInt(2, tier);

            ResultSet rs = prest.executeQuery();
            if(rs.next()){
                int qty = rs.getInt("qty");
                if(qty > 0){
                    return true;
                }
                else{
                    return false;
                }
            }
        } catch (SQLException exception) {
            // TODO: handle exception
            exception.printStackTrace();
        }
        return false;
    }
}
