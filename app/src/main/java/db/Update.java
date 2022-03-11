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
            if (sciGiven) {
                String[] parts = new String[2];
                if (name.contains(" ")) {
                    parts = name.split(" ");
                }
                pstmt = conn.prepareStatement(INSERT_NEW_SIGHT_SCI);
                pstmt.setString(5, parts[0]);
                pstmt.setString(6, parts[1]);
            }
            else {
                pstmt = conn.prepareStatement(INSERT_NEW_SIGHT_COM);
                pstmt.setString(5, name);
            }
            
            // Set other parameters in the statement
            pstmt.setString(1, sightingDate);
            pstmt.setDouble(2, latitude);
            pstmt.setDouble(3, longitude);
            pstmt.setInt(4, altitude);
            
            System.out.println(pstmt.toString());
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
            pstmt.setString(1, soilMoisture);
            pstmt.setString(2, soilType);
            pstmt.setString(3, habitatType);
            rowAffected = pstmt.executeUpdate();
        }
        catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        
        return rowAffected;
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
            "INSERT INTO sighting (tree_id, sighting_date,"
            + " latitude, longitude, altitude)"
            + " SELECT tree_id, ?, ?, ?, ?"
            + " FROM sighting NATURAL JOIN tree NATURAL JOIN common_name"
            + " WHERE tree_name = ?";
    
    private final String INSERT_NEW_HABITAT = 
            "INSERT INTO habitat (soil_moisture, soil_type, habitat_type)"
            + " VALUES (?, ?, ?)";
    
}
