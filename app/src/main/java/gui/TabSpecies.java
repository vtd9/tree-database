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
import db.Query;
import entities.Species;

/**
 * Create the species tab for the main TabPane
 */
public class TabSpecies {
    public TabSpecies(Query query) {
        this.query = query;
        
        // Initialize class variables
        speciesBox = new VBox();
        table = new TableView();

        // Create new Tab object
        fillBox();
        tab = new Tab(TITLE);
        tab.setContent(speciesBox);
    }
    
    private void fillBox() {
        // Resize the VBox
        speciesBox.setPrefWidth(App.PREF_WIDTH);
        
        // Make description label
        Label desc = new Label(DESCRIPTION);
        desc.setWrapText(true);
        desc.setPadding(new Insets(5, 10, 5, 5));

        // Stack together in vertical box
        speciesBox.setPadding(new Insets(5, 10, 5, 10));
        speciesBox.getChildren().addAll(
                desc, createButtonBox(), new StackPane(table));
    }
    
    private HBox createButtonBox() {
        // Make refresh button
        Button refreshButton = new Button(REFRESH);
        refreshButton.setPrefWidth(App.BUTTON_WIDTH);
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
        table.setItems(query.getSpecies());
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
                    new PropertyValueFactory<Species, String>(cols.get(i))
            );            
            table.getColumns().add(col);
        }
    }
    
    public Tab getTab() {
        return tab;
    }

    private final Query query;
    private final Tab tab;
    private final TableView table;
    private final VBox speciesBox;
    private final String TITLE = "Species";
    private final String REFRESH = "Refresh";
    private final String DESCRIPTION = "View all the species currently"
            + " present in the database by both their scientific"
            + " and common names.";
}
