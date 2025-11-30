package controller;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import model.TowerOnAccount;
import util.DBConnection;

public class AddTowerToInventory {
    public TowerOnAccount add(int accId, int towerId){
        TowerOnAccount twacc = new TowerOnAccount(accId, towerId);
        return addToInven(twacc);

    }

    public TowerOnAccount addToInven(TowerOnAccount twacc){
        String query = "insert into account_towers (account_id, tower_id) values (?, ?)";

        try {
            Connection conn = DBConnection.getConnection();
            PreparedStatement prest = conn.prepareStatement(query);

            prest.setInt(1, twacc.getAccTowerId());
            prest.setInt(2, twacc.getTowerId());

            int change = prest.executeUpdate();

            if(change >0){
                return new TowerOnAccount(twacc.getAccTowerId(), twacc.getTowerId());
            }
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
        return null;
    }
}
