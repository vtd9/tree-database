package db;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Parse metadata and other result sets from SQL queries.
 */
public class MetadataHandler {
    /**
     * Get list of table names in the database.
     * @return Unmodifiable list of table names, which are strings
     * @throws SQLException 
     */
    public static List<String> allTables() throws SQLException {
        DatabaseMetaData md = conn.getMetaData();
        ResultSet rs = md.getTables(null, null, null, new String[]{"TABLE"});
        return resultToList(rs, "TABLE_NAME");
    }
    
    /**
     * Get list of column names for a table in the database.
     * @param table String of column name
     * @return Unmodifiable list of column names, which are strings
     * @throws SQLException 
     */
    public static List<String> tableCols(String table) throws SQLException {
        DatabaseMetaData md = conn.getMetaData();
        ResultSet rs = md.getColumns(null, null, table, null);
        return resultToList(rs, "COLUMN_NAME");
    }
    
    public static List<String> resultToList(ResultSet rs) {
        List<String> rsList = new ArrayList<>();
        
        try {
            while (rs.next()) {
                rsList.add(rs.getString(1));
            }
        }
        catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return Collections.unmodifiableList(rsList);
    }

    public static List<String> resultToList(ResultSet rs, String getter) 
            throws SQLException {

        List<String> rsList = new ArrayList<>();
        
        try {
            while (rs.next()) {
                if (!SYS_CONFIG.equals(rs.getString(getter))) {
                    rsList.add(rs.getString(getter));
                }
            }
        }
        catch (SQLException e) {
            System.out.println(e.getMessage());
        }  
        
        return Collections.unmodifiableList(rsList);
    }
 
    public static List<String> getResultCols(ResultSetMetaData rsmd) 
            throws SQLException {
        List<String> rsColList = new ArrayList<>();
        for (int i = 1; i <= rsmd.getColumnCount(); i++) {
            rsColList.add(rsmd.getColumnName(i));
        }
        return Collections.unmodifiableList(rsColList);
    }
    
    
    private static final Connection conn = Connector.makeConnection();
    private static final String SYS_CONFIG = "sys_config";
}
