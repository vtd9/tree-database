package db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 *
 */
public class Query {
    /**
     * Construct a query object with a connection to the database.
     * @param conn Connection to the database to query.
     */
    public Query(Connection conn) {
        this.conn = conn;
    }
    
    public List<String> getColNames() {
        List<String> colNames = null;
        try {
            colNames = MetadataQuery.getResultCols(prevRs.getMetaData());
        }
        catch (SQLException e) {
        }
        return colNames;
    }
    
    /**
     * Get all the species in the database.
     * @return List of all species by both scientific and common name.
     */
    public ObservableList<Species> getSpecies() {
        ObservableList<Species> species = 
                FXCollections.observableArrayList();        
        try {
            // Execute the query
            Statement stmt = conn.createStatement();
            prevRs = stmt.executeQuery(QUERY_SPECIES);
            List<String> colNames = getColNames();

            // Add the results to list to populate the GUI later
            while (prevRs.next()) {
                species.add(new Species(
                        prevRs.getString(colNames.get(0)),
                        prevRs.getString(colNames.get(1)),
                        prevRs.getString(colNames.get(2))
                ));
            }
        }
        catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        
        // Return query contents or its attributes, depending on argument
        return species;
    }

    public ObservableList<Sighting> getSightings(
        String name, boolean sciGiven) {
        ObservableList<Sighting> sightings = 
                FXCollections.observableArrayList();
        try {
            // Create prepared statement for DBMS to compile
            PreparedStatement pstmt;
            if (sciGiven) {
                if (name.contains(" ")) {
                    // Split scientific name by space
                    String[] parts = name.split(" ");

                    // Set the parameters in the prepared statement
                    pstmt = conn.prepareStatement(QUERY_SIGHT_SCI);
                    pstmt.setString(1, parts[0]);
                    pstmt.setString(2, parts[1]);
                }
                else {
                    throw new IllegalArgumentException(SCI_NO_SPACE); 
                }
            }
            else {
                pstmt = conn.prepareStatement(QUERY_SIGHT_COMMON);
                pstmt.setString(1, name);
            }
            prevRs = pstmt.executeQuery();
            List<String> colNames = getColNames();
            
            // Execute query
            while (prevRs.next()) {
                sightings.add(new Sighting(
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
        return sightings;
    }

    private static ResultSet prevRs;
    private final Connection conn;
    private static final String QUERY_SPECIES = "SELECT genus, species,"
            + " tree_name FROM tree NATURAL JOIN common_name";
    private static final String QUERY_SIGHT_SCI = "SELECT"
            + " sighting_date, latitude, longitude, altitude"
            + " FROM sighting NATURAL JOIN tree"
            + " WHERE genus = ? AND species = ?";
    private static final String QUERY_SIGHT_COMMON = "SELECT"
            + " sighting_date, latitude, longitude, altitude"
            + " FROM sighting NATURAL JOIN tree NATURAL JOIN common_name"
            + " WHERE tree_name = ?";
    private static final String SCI_NO_SPACE = 
            "Entered name does not contain a space!";
}
