package controller;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import model.User;
import util.DBConnection;

public class RegisterController {
    
    public User HandleRegiser(String username, String passord){
        User uregiister = new User(username, passord);
        return register(uregiister);
    }

    public User register(User user){
        String query = "insert into accounts(username, password, role, gold, base_level) values (?, ?, 'PLAYER', 0, 1)";
        String queryforitem = "insert into inventory(account_id, item_id, qty) values (?, ?, 0)";
        // query nưa nè
        try {
            Connection conn = DBConnection.getConnection();
            // thấy cái return gen key mầu tím ko mấy con chó tải sao phải có nó vì khi mấy thawg ngu cm chạy cái câu insert trên kia kìa nó sẽ chỉ trả ra had chage lồn lồn gì dko mà rs thì nó cần lấy id vậy thì cần trả về cả id để cho thg rs đọc dko nếu ko có thì nó sẽ trả ra null và chung quy là bulon
            PreparedStatement prest = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            PreparedStatement newaccitem = conn.prepareStatement(queryforitem);

            prest.setString(1, user.getUsername());
            prest.setString(2, user.getPassword());

            int cothaydoirownaoko = prest.executeUpdate();
            System.out.println(cothaydoirownaoko);
            // cai này xem là exe cu te xong tttht neu thanh cong ay no hiệu là bao nhieu row đã change ấy may tthg ngu ạ
            if(cothaydoirownaoko > 0){
                // lay id la prime kry nè 
                ResultSet rs = prest.getGeneratedKeys();
                if(rs.next()){
                    int id = rs.getInt(1);
                    for (int i = 1; i <=4; i++) {
                        newaccitem.setInt(1, id);
                        newaccitem.setInt(2, i);

                        newaccitem.executeUpdate();
                    }
                    return new User(id, user.getUsername(), user.getRole(), user.getGold(), user.getBaseLevel());
                }

            }
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
        return null;
    }
}

// code của em huynh