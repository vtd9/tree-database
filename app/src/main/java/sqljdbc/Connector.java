package sqljdbc;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection; 
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/**
 *
 */
public class Connector {
    public static Connection makeConnection() {
        Connection conn = null;
        try (FileInputStream f = new FileInputStream("db.properties")) {
            // Load the properties file
            Properties pros = new Properties();
            pros.load(f);
            

            // Get db parameters
            String url = pros.getProperty("url");
            String user = pros.getProperty("user");
            String password = pros.getProperty("password");
            
            // Make connection to database
            conn = DriverManager.getConnection(url, user, password);
            
            // 
            System.out.println(String.format("Connected to database %s "
                    + "successfully.", conn.getCatalog()));
        } 
        catch (IOException | SQLException ex) {
            System.out.println(ex.getMessage());
        }
        return conn;
    }
}