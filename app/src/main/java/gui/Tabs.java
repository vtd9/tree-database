package gui;

import java.sql.Connection;
import javafx.scene.control.TabPane;
import javafx.scene.control.TabPane.TabClosingPolicy;
import db.Query;

/**
 *
 */
public class Tabs {
    public Tabs(Connection conn) {
        // Prevent tabs from being closed by the user
        tabPane.setTabClosingPolicy(TabClosingPolicy.UNAVAILABLE);
        Query query = new Query(conn);

        // Add tabs
        tabPane.getTabs().add(new TabSpecies(query).getTab());
        tabPane.getTabs().add(new TabSighting(query).getTab());
    }

    
    public TabPane getPane() {
        return tabPane;
    }
    
    private final TabPane tabPane = new TabPane();
}
