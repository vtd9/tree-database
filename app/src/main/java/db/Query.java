package db;

import entities.HabitatTree;
import entities.Sighting;
import entities.SpeciesName;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * Interface with a database to view a subset of tables.
 */
public class Query {
    /**
     * Construct a Query object with a connection to the database.
     * @param conn Connection to the database to query.
     */
    public Query(Connection conn) {
        this.conn = conn;
    }
    
    public List<String> getColNames() {
        List<String> colNames = null;
        try {
            colNames = MetadataHandler.getResultCols(prevRs.getMetaData());
        }
        catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return colNames;
    }
    
    /**
     * Get all the species with their common names in the database.
     * @return List of all species by both scientific and common name.
     */
    public ObservableList<SpeciesName> getSpeciesName() {
        ObservableList<SpeciesName> species = 
                FXCollections.observableArrayList();        
        try {
            // Execute the query
            Statement stmt = conn.createStatement();
            prevRs = stmt.executeQuery(QUERY_SPECIES_NAME);
            List<String> colNames = getColNames();

            // Add the results to list to populate the GUI later
            while (prevRs.next()) {
                species.add(new SpeciesName(
                        prevRs.getString(colNames.get(0)),
                        prevRs.getString(colNames.get(1)),
                        prevRs.getString(colNames.get(2))
                ));
            }
        }
        catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        
        return species;
    }

     /**
     * Get all the species in the database.
     * @return List of all species by both scientific and common name.
     */
    public List<String> getSpecies() {
        List<String> species = new ArrayList<>();
        try {
            // Execute the query
            Statement stmt = conn.createStatement();
            prevRs = stmt.executeQuery(QUERY_SPECIES);
            
            while (prevRs.next()) {
                species.add(prevRs.getString(1) + " " + prevRs.getString(2));
            }
        }
        catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return species;
    }
    
    
    public ObservableList<Sighting> getSightings(String name, boolean sciGiven) {
        ObservableList<Sighting> sights = FXCollections.observableArrayList();
        
        try {
            PreparedStatement pstmt = conn.prepareStatement(QUERY_SIGHT_SCI);
            
            if (sciGiven) {
                String[] parts = splitSciName(name);
                pstmt.setString(1, parts[0]);
                pstmt.setString(2, parts[1]);
            }
            else {
                pstmt = conn.prepareStatement(QUERY_SIGHT_COMMON);
                pstmt.setString(1, name);
            }
            prevRs = pstmt.executeQuery();
            List<String> colNames = getColNames();
            
            // Execute query
            while (prevRs.next()) {
                sights.add(new Sighting(
                        prevRs.getString(colNames.get(0)),
                        prevRs.getFloat(colNames.get(1)),
                        prevRs.getFloat(colNames.get(2)),
                        prevRs.getInt(colNames.get(3))
                ));
            }        
        }
        catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return sights;
    }

    public ObservableList<HabitatTree> getHabitatTree() {
        
        // List to store query results in
        ObservableList<HabitatTree> habitatTree = 
                FXCollections.observableArrayList();
        
        try {
            Statement stmt = conn.createStatement();
            prevRs = stmt.executeQuery(QUERY_HABITAT_TREE_ALL);
            List<String> colNames = getColNames();
            
            while (prevRs.next()) {
                habitatTree.add(new HabitatTree(
                        prevRs.getString(colNames.get(0)),
                        prevRs.getString(colNames.get(1)),
                        prevRs.getString(colNames.get(2)),
                        prevRs.getString(colNames.get(3)),
                        prevRs.getString(colNames.get(4))
                ));
            }        
        }
        catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return habitatTree;
    }

    public ObservableList<HabitatTree> getHabitatTreeFilt(
            String name, boolean sciGiven) {
        
        // List to store query results in
        ObservableList<HabitatTree> habitatTree = 
                FXCollections.observableArrayList();
        
        try {
            PreparedStatement pstmt;
            
            if (sciGiven) {
                pstmt = conn.prepareStatement(QUERY_HABITAT_TREE_SCI);
                String[] parts = splitSciName(name);
                pstmt.setString(1, parts[0]);
                pstmt.setString(2, parts[1]);
            }
            else {
                pstmt = conn.prepareStatement(QUERY_HABITAT_TREE_COM);
                pstmt.setString(1, name);
            }
            
            prevRs = pstmt.executeQuery();
            List<String> colNames = getColNames();
            
            while (prevRs.next()) {
                habitatTree.add(new HabitatTree(
                        prevRs.getString(colNames.get(0)),
                        prevRs.getString(colNames.get(1)),
                        prevRs.getString(colNames.get(2)),
                        prevRs.getString(colNames.get(3)),
                        prevRs.getString(colNames.get(4))
                ));
            }        
        }
        catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return habitatTree;
    }
    
    /**
     * Parse scientific name (space separated) if given
     * @param name
     * @return 
     */
    public static String[] splitSciName(String name) {
        if (name.contains(" ")) {
            return name.split(" ");
        }
        return null;
    }
    
    
    private static ResultSet prevRs;
    private final Connection conn;
    
    private final String QUERY_SPECIES_NAME = "SELECT genus, species,"
            + " tree_name FROM tree NATURAL JOIN common_name";
 
    private final String QUERY_SPECIES = "SELECT genus, species FROM tree";   
    
    private final String QUERY_SIGHT_SCI = "SELECT"
            + " sighting_date, latitude, longitude, altitude"
            + " FROM sighting NATURAL JOIN tree"
            + " WHERE genus = ? AND species = ?";
    
    private final String QUERY_SIGHT_COMMON = "SELECT"
            + " sighting_date, latitude, longitude, altitude"
            + " FROM sighting NATURAL JOIN tree NATURAL JOIN common_name"
            + " WHERE tree_name = ?";
   
    private final String QUERY_HABITAT_TREE_ALL = "SELECT"
            + " genus, species, soil_moisture, soil_type, habitat_type"
            + " FROM habitat NATURAL JOIN grows_in NATURAL JOIN tree";

    private final String QUERY_HABITAT_TREE_SCI = "SELECT"
            + " genus, species, soil_moisture, soil_type, habitat_type"
            + " FROM habitat NATURAL JOIN grows_in NATURAL JOIN tree"
            + " WHERE genus = ? AND species = ?";
    
    private final String QUERY_HABITAT_TREE_COM = "SELECT"
            + " genus, species, soil_moisture, soil_type, habitat_type"
            + " FROM habitat NATURAL JOIN grows_in NATURAL JOIN tree"
            + " NATURAL JOIN common_name WHERE common_name = ?";
    }