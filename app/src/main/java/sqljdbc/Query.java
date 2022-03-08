package sqljdbc;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
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
     * Get all the species in the database.
     * @return List of all species by both scientific and common name.
     * @throws SQLException 
     */
    public static ObservableList<Species> getSpecies() throws SQLException {
        // Execute the query
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(QUERY_SPECIES);
        List<String> colNames = getSpeciesCols();
        
        // Add the results to a list to populate the GUI later
        ObservableList<Species> species = 
                FXCollections.observableArrayList();
        while (rs.next()) {
            species.add(new Species(
                    rs.getString(colNames.get(0)),
                    rs.getString(colNames.get(1)),
                    rs.getString(colNames.get(2))
            ));
        }
        return species;
    }

    /**
     * Get column names from the species query.
     * @return
     * @throws SQLException 
     */
    public static List<String> getSpeciesCols() throws SQLException {
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(QUERY_SPECIES);
        ResultSetMetaData rsmd = rs.getMetaData();
        return Metadata.getResultCols(rsmd);
    }    

    
    private static final Connection conn = Connector.conn;
    private static final String QUERY_SPECIES = "SELECT genus, species,"
            + " tree_name FROM tree NATURAL JOIN common_name";
}
