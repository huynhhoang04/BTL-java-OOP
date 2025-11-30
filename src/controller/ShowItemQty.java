package controller;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import model.Inventory;
import util.DBConnection;

public class ShowItemQty {
    public Inventory HandleTakeQty(int accId, int itemId){
        Inventory forQty = new Inventory(accId, itemId);
        return showQty(forQty);
    }

    public Inventory showQty(Inventory inventory){
        String query  = "select * from inventory where account_id = ? and item_id = ?";

        try {
            Connection conn = DBConnection.getConnection();
            PreparedStatement prest = conn.prepareStatement(query);

            System.out.println(inventory.getAccId());
            System.out.println(inventory.getItemId());

            prest.setInt(1, inventory.getAccId());
            prest.setInt(2, inventory.getItemId());

            ResultSet rs = prest.executeQuery();

            if(rs.next()){
                int accId = rs.getInt("account_id");
                int itemId = rs.getInt("item_id");
                int qty = rs.getInt("qty");

                System.out.println(accId + itemId + qty);

                return new Inventory(accId, itemId, qty);
            }
            
        } catch (SQLException exception) {
            // TODO: handle exception
            exception.printStackTrace();
        }
        return null;
    }
}
