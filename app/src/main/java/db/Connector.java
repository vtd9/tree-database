package db;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection; 
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/**
 * Methods to connect to a SQL database.
 */
public class Connector {
    /**
     * Make connection to a SQL database via the login information contained 
     * in an external file "db.properties"
     * @return Connection object that can then be used to make statements
     * and execute queries with
     */
    public static Connection makeConnection() {
        Connection conn = null;
        try (FileInputStream f = new FileInputStream(DB_FILE)) {
            // Load the properties file
            Properties pros = new Properties();
            pros.load(f);
            
            // Get db parameters
            String url = pros.getProperty("url");
            String user = pros.getProperty("user");
            String password = pros.getProperty("password");
            
            // Make connection to database
            conn = DriverManager.getConnection(url, user, password);
        } 
        catch (IOException | SQLException ex) {
            System.out.println(ex.getMessage());
        }
        return conn;
    }

    private static final String DB_FILE = "db.properties";
}