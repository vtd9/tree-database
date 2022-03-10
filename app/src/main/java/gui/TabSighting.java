package gui;

import java.util.List;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import sqljdbc.Query;
import sqljdbc.Sighting;

/**
 * Create the species tab for the main TabPane
 */
public class TabSighting {
    public TabSighting(Query query) {
        this.query = query;

        // Create new Tab object
        fillBox();
        tab = new Tab(TITLE);
        tab.setContent(sightingBox);
    }
    
    private void fillBox() {
        // Resize the VBox
        sightingBox.setPrefWidth(PREF_WIDTH);
        
        // Make description label
        Label desc = new Label(DESCRIPTION);
        desc.setWrapText(true);
        desc.setPadding(new Insets(5, 5, 5, 5));

        // Stack together in vertical box
        sightingBox.setPadding(new Insets(5, 5, 5, 5));
        sightingBox.getChildren().addAll(
                desc, createButtonBox(), new StackPane(table));
    }
    
    private HBox createButtonBox() {
        // Make refresh button
        Button refreshButton = new Button(REFRESH);
        refreshButton.setPrefWidth(BUTTON_WIDTH);
        refreshButton.setOnMousePressed((MouseEvent event) -> {
            table.getItems().clear();
            fillTable();
        });

        // Put together in a horizontal box
        HBox buttonBox = new HBox();
        buttonBox.setPadding(new Insets(5, 5, 10, 5));
        buttonBox.getChildren().addAll(refreshButton);
        return buttonBox;
    }
    
    private void fillTable() {
        table.setEditable(true);
        table.setPadding(new Insets(5, 5, 5, 5));
        
        // Fill the table
        table.setItems(query.getSightings("", "Tamarack", false));
        createColumns();
    }
    
    private void createColumns() {
        // Flush previous columns, if any
        table.getColumns().clear();
        
        // For each attribute in the table, label column
        List<String> cols = query.getColNames();
        for (int i = 0; i < cols.size(); i++) {
            TableColumn col = new TableColumn(cols.get(i));
            col.setCellValueFactory(
                    new PropertyValueFactory<Sighting, String>(cols.get(i))
            );            
            table.getColumns().add(col);
        }
    }
    
    public Tab getTab() {
        return tab;
    }

    private final Query query;
    private final Tab tab;
    private final TableView table = new TableView();
    private final VBox sightingBox = new VBox();
    private final String TITLE = "Sightings";
    private final String REFRESH = "Refresh";
    private final int BUTTON_WIDTH = 100;
    private final int PREF_WIDTH = 500;
    private final String DESCRIPTION = "View the sightings for a tree currently"
            + " in the database by either a scientific or common name.";
}
