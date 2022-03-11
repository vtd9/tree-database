package db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

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

    public int addSighting(String genus, String species, String sightingDate, 
            String latitude, String longitude, String altitude) {
        int rowAffected = -1;
        PreparedStatement pstmt;
        
        System.out.println(altitude.isEmpty());
        
        // Handle empty optional arguments and convert to numerical types
        // From database constraints, latitude and longitude cannot be null
        if (sightingDate.isEmpty()) sightingDate = null;

        try {
            // Create PreparedStatement and set parameters
            pstmt = conn.prepareStatement(INSERT_NEW_SIGHT);
            pstmt.setString(1, sightingDate);
            pstmt.setDouble(2, Double.parseDouble(latitude));
            pstmt.setDouble(3, Double.parseDouble(longitude));
            if (altitude.isEmpty()) {
                pstmt.setNull(4, java.sql.Types.INTEGER);
            }
            else {
                pstmt.setInt(4, Integer.parseInt(altitude));
            }
            pstmt.setString(5, genus);
            pstmt.setString(6, species);
            
            // Execute update
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
    
    private final String INSERT_NEW_SIGHT = 
            "INSERT INTO sighting (tree_id, sighting_date,"
            + " latitude, longitude, altitude)"
            + " SELECT tree_id, ?, ?, ?, ?"
            + " FROM tree"
            + " WHERE genus = ? AND species = ?";
    
    private final String INSERT_NEW_HABITAT = 
            "INSERT INTO habitat (soil_moisture, soil_type, habitat_type)"
            + " VALUES (?, ?, ?)";
}
