package gui;

import java.sql.SQLException;
import javafx.scene.control.TabPane;
import javafx.scene.control.TabPane.TabClosingPolicy;

/**
 *
 */
public class Tabs {
    public Tabs() throws SQLException {
        tabPane.setTabClosingPolicy(TabClosingPolicy.UNAVAILABLE);
        
        // Add species tab
        tabPane.getTabs().add(new TabSpecies().getTab());
    }
    
    
    
    public TabPane getTabs() {
        return tabPane;
    }
    
    private final TabPane tabPane = new TabPane();
}
