package util;

import java.sql.*;

public class DBConnection {
    private static final String url = "jdbc:postgresql://ep-empty-sunset-a1wxu632-pooler.ap-southeast-1.aws.neon.tech:5432/neondb?sslmode=require&channel_binding=require&user=neondb_owner&password=npg_QIHJ7vT8ShLU";

    public static Connection getConnection() throws SQLException{
        return DriverManager.getConnection(url); 
    }
}
