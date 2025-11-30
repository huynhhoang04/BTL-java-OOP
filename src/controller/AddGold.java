package controller;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import util.DBConnection;

public class AddGold {
    public void addgold(long add, int id){
        String query = "update accounts set gold = gold + ? where id = ?;";

        try {
            Connection conn = DBConnection.getConnection();
            PreparedStatement pst = conn.prepareStatement(query);

            pst.setLong(1, add);
            pst.setInt(2, id);


            pst.execute();
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }
}
