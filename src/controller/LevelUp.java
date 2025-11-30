package controller;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import model.User;
import util.DBConnection;

public class LevelUp {
    public void levelup(User user){
        String query =  "update accounts set base_level = base_level + 1 where id = ?";

        try {
            Connection conn = DBConnection.getConnection();
            PreparedStatement pst = conn.prepareStatement(query);


            pst.setInt(1, user.getId());
            pst.execute();
        } catch (SQLException exception) {
            exception.printStackTrace();
        }

    }


    public int getCurrentLevel(User user){
        String query = "select base_level from accounts where id = ?";
        try {
            Connection conn = DBConnection.getConnection();
            PreparedStatement pst = conn.prepareStatement(query);

            pst.setInt(1, user.getId());

            ResultSet rs = pst.executeQuery();

            if(rs.next()){
                return rs.getInt("base_level");

            }
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
        return 0;
    }
}
