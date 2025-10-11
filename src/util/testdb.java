package util;
import java.sql.*;
import java.sql.Statement;
import java.beans.*;
import java.sql.SQLException;
public class testdb {
    private static final String url = "jdbc:mysql://gateway01.ap-southeast-1.prod.aws.tidbcloud.com:4000/test"
                        +"?user=2Escdy9NGrStVSg.root"
                        +"&password=UhhlyIRQCYAG3WCr"
                        +"&sslMode=VERIFY_IDENTITY"
                        +"&enabledTLSProtocols=TLSv1.2"
                        +"&sslCa=lib/isrgrootx1.pem";

    public static Connection getConnection() throws SQLException{
        return DriverManager.getConnection(url);
    }
        
}
