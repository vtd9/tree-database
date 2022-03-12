package db;

import entities.HabitatTree;
import entities.Sighting;
import entities.SpeciesName;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * Handler to view and filter a subset of tables via predefined queries.
 */
public class Query {
    /**
     * Construct a Query object to 
     * @param conn Connection to the database to query.
     */
    public Query(Connection conn) {
        this.conn = conn;
    }
    
    /**
     * Get the column names from the result set saved in the instance.
     * @return list of column names as strings
     */
    public List<String> getColNames() {
        List<String> colNames = null;
        try {
            colNames = getResultCols(prevRs.getMetaData());
        }
        catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return colNames;
    }
    
    /**
     * Get all the species with their common names in the database.
     * @return observable list of scientific and common names
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
     * @return list of all species by scientific name
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
    
    /**
     * Get all the sightings of a particular tree species in the database.
     * @param name
     * @param sciGiven true if name given as a scientific "Genus species" 
     * format; false if a colloquial name given
     * @return 
     */
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

    /**
     * Get all the known habitats and tree species that inhabit each
     * according to the database.
     * @return observable list of habitats and corresponding trees
     */
    public ObservableList<HabitatTree> getHabitatTree() {
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

    /**
     * Get all the known habitats a particular tree species inhabits according 
     * to the database.
     * @param name name of species to filter by
     * @param sciGiven true if name given in scientific format; false if
     * a common name
     * @return observable list of habitats 
     */
    public ObservableList<HabitatTree> getHabitatTreeFilt(
            String name, boolean sciGiven) {
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
     * Get a list of column names from a result set.
     * @param rsmd ResultSet to get column names from
     * @return list of column names
     */
    public static List<String> getResultCols(ResultSetMetaData rsmd) {
        List<String> rsColList = new ArrayList<>();
        
        try {
            for (int i = 1; i <= rsmd.getColumnCount(); i++) {
                rsColList.add(rsmd.getColumnName(i));
            }
        }
        catch (SQLException e) {
            
        }
        return Collections.unmodifiableList(rsColList);
    }
    
    /**
     * Parse scientific name that, by definition, is space separated in the
     * format "Genus species"
     * @param name scientific name to split
     * @return array of [Genus, species]
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
            + " NATURAL JOIN common_name WHERE tree_name = ?";
    }