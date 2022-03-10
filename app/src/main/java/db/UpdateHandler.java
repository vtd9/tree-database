package db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

/**
 *
 * Interface with a database to update a subset of tables.
 */
public class UpdateHandler {
    /**
     * Construct a Update 
     * @param conn 
     */
    public UpdateHandler(Connection conn) {
        this.conn = conn;
    }
    
    /**
     * Given a tree's scientific name, add a new colloquial name to it.
     * @param newName new common name to add
     * @param genus genus of tree species to update
     * @param species species of tree species to update
     * @return index of row affected
     */
    public int addCommonName(String newName, String genus, String species) {
        int rowAffected = -1;
        try {
            PreparedStatement pstmt = conn.prepareStatement(INSERT_COMMON_NAME);
            
            // Set parameters
            pstmt.setString(1, newName);
            pstmt.setString(2, genus);
            pstmt.setString(3, species);
            
            // Execute
            rowAffected = pstmt.executeUpdate();
        }
        catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return rowAffected;
    }
    
    public int addSighting() {
        int rowAffected = -1;
        try {
            PreparedStatement pstmt = conn.prepareStatement("");
        }
        catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        
        return rowAffected;
    }
    
    public int addHabitat(String soilMoisture, String soilType, 
            String habitatType) {
        int rowAffected = -1;
        
        // If empty string argument(s) given, set to null
        if (soilMoisture.isEmpty())
            soilMoisture = null;
        if (soilType.isEmpty())
            soilType = null;
        if (habitatType.isEmpty())
            habitatType = null;
        
        try {
            PreparedStatement pstmt = conn.prepareStatement(INSERT_NEW_HABITAT);
            pstmt.setInt(1, getMaxHabitatId() + 1);
            pstmt.setString(2, soilMoisture);
            pstmt.setString(3, soilType);
            pstmt.setString(4, habitatType);
            rowAffected = pstmt.executeUpdate();
        }
        catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        
        return rowAffected;
    }
    
    public int getMaxHabitatId() {
        int maxId = -1;
        try {
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(QUERY_HABITAT_ID);
            rs.next();
            maxId = rs.getInt(1);
        }
        catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return maxId;
    }
    
    private final Connection conn;
    private final String INSERT_COMMON_NAME = 
            "INSERT INTO common_name (tree_id, tree_name)"
            + " SELECT tree_id, ? FROM tree NATURAL JOIN common_name"
            + " WHERE genus = ? AND species = ?";
    private final String INSERT_NEW_HABITAT = 
            "INSERT INTO habitat (habitat_id, soil_moisture, soil_type,"
            + " habitat_type) VALUES (?, ?, ?, ?)";
    private final String QUERY_HABITAT_ID = "SELECT MAX(habitat_id)"
            + " from habitat";
}
