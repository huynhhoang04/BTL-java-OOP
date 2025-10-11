package controller;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import model.User;
import util.testdb;

public class UserDataAccess {
    public User register(User user){
        String query = "INSERT INTO AppUser (username, password, email) VALUES (?, ?, ?)";
        try{
            Connection conn = testdb.getConnection();
            PreparedStatement st = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);

            st.setString(1, user.getUsername());
            st.setString(2, user.getPassword());
            st.setString(3, user.getEmail());

            int change = st.executeUpdate();
            if(change > 0){
                ResultSet rs = st.getGeneratedKeys();
                if(rs.next()){
                    long preid = rs.getLong(1);
                    return new User(preid, user.getUsername(), user.getPassword(), user.getEmail());
                }
            }
        }
        catch(SQLException e){
            e.printStackTrace();
        }
        return null;
    }

    public User login(User user){
        String query = "SELECT * FROM AppUser WHERE username = ? AND password = ?";
        try{
            Connection conn = testdb.getConnection();
            PreparedStatement st = conn.prepareStatement(query);

            st.setString(1, user.getUsername());
            st.setString(2, user.getPassword());

            ResultSet rs = st.executeQuery();
            if(rs.next()){
                long id = rs.getLong("id");
                String username = rs.getString("username");
                String password = rs.getString("password");
                String email = rs.getString("email");

                return new User(id, username, password, email);

            }
        }
        catch(SQLException e){
            e.printStackTrace();
        }
        return null;
    }
}
