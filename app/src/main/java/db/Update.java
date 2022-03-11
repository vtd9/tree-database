package db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 *
 * Interface with a database to update a subset of tables.
 */
public class Update {
    /**
     * Construct a Update 
     * @param conn 
     */
    public Update(Connection conn) {
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
    
    /**
     * 
     * @param name
     * @param sciGiven
     * @param sightingDate
     * @param latitude
     * @param longitude
     * @param altitude
     * @return 
     */
    public int addSighting(String name, boolean sciGiven, String sightingDate, 
            double latitude, double longitude, int altitude) {
        int rowAffected = -1;
        
        // Handle empty string argument
        if (sightingDate.isEmpty())
            sightingDate = null;
  
        try {
            PreparedStatement pstmt;
            
            // If name given is scientific form, parse
            if (sciGiven) {
                String[] parts;
                if (name.contains(" ")) 
                    parts = name.split(" ");
                else
                    throw new IllegalArgumentException(Query.SCI_NO_SPACE); 
                
                pstmt = conn.prepareStatement(INSERT_NEW_SIGHT_SCI);
                pstmt.setString(6, parts[0]);
                pstmt.setString(7, parts[1]);
            }
            else {
                pstmt = conn.prepareStatement(INSERT_NEW_SIGHT_COM);
                pstmt.setString(6, name);
            }
            
            // Set other parameters in the statement
            pstmt.setInt(1, getMaxSightingId() + 1);
            pstmt.setString(2, sightingDate);
            pstmt.setDouble(3, latitude);
            pstmt.setDouble(4, longitude);
            pstmt.setInt(5, altitude);
            
            System.out.println(pstmt.toString());
            
            // Execute the insert
            rowAffected = pstmt.executeUpdate();
        }
        catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        
        return rowAffected;
    }
    
    /**
     * 
     * @param soilMoisture
     * @param soilType
     * @param habitatType
     * @return 
     */
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
    
    /**
     * Get the value of the maximum sighting_id in the sighting table
     * @return max value of sighting_id from the sighting table
     */
    public int getMaxSightingId() {
        int maxId = -1;
        try {
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(QUERY_SIGHT_ID);
            rs.next();
            maxId = rs.getInt(1);
        }
        catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return maxId;
    }    
 
    /**
     * Get the value of the maximum habitat_id in the habitat table
     * @return max value of habitat_id from the habitat table
     */
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
    
    private final String INSERT_NEW_SIGHT_SCI = 
            "INSERT INTO sighting (sighting_id, tree_id, sighting_date,"
            + " latitude, longitude, altitude)"
            + " SELECT ?, tree_id, ?, ?, ?, ?"
            + " FROM sighting NATURAL JOIN tree"
            + " WHERE genus = ? AND species = ?";

    private final String INSERT_NEW_SIGHT_COM = 
            "INSERT INTO sighting (sighting_id, tree_id, sighting_date,"
            + " latitude, longitude, altitude)"
            + " SELECT ?, tree_id, ?, ?, ?, ?"
            + " FROM sighting NATURAL JOIN tree NATURAL JOIN common_name"
            + " WHERE tree_name = ?";
    
    private final String INSERT_NEW_HABITAT = 
            "INSERT INTO habitat (habitat_id, soil_moisture, soil_type,"
            + " habitat_type) VALUES (?, ?, ?, ?)";
    
    private final String QUERY_HABITAT_ID = "SELECT MAX(habitat_id)"
            + " from habitat";

    private final String QUERY_SIGHT_ID = "SELECT MAX(sighting_id)"
            + " from sighting";
}
