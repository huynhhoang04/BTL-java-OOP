package controller;


import java.sql.*;
import util.DBConnection;
import model.User;

public class LoginController {

    public User HandleLogin(String username, String password){
        User ulogin = new User(username, password);
        return login(ulogin);
    }

    public User login(User user){
        //query nè
        String query = "select * from accounts where username = ? and password = ?";

        try{        
            Connection conn = DBConnection.getConnection();
            PreparedStatement prest = conn.prepareStatement(query);
            prest.setString(1, user.getUsername());
            prest.setString(2, user.getPassword());

            ResultSet rs = prest.executeQuery();
            // chạy query nè
            if(rs.next()){
                int id = rs.getInt("id");
                String username = rs.getString("username");
                String role = rs.getString("role");
                long gold = rs.getLong("gold");
                int base_level = rs.getInt("base_level");

                return new User(id, username, role, gold, base_level);
            }

        }
        catch(SQLException exception){
            exception.printStackTrace();
        }
        return null;
    }
    
}

//code của em huynh và hoàng