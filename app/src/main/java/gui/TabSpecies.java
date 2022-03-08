package gui;

import java.sql.SQLException;
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
import sqljdbc.Species;

/**
 * Add the species tab to the main tab. 
 */
public class TabSpecies {
    public TabSpecies() throws SQLException {
        // Create new Tab object
        tab = new Tab(TITLE);
        createColumns();
        fillBox();
        tab.setContent(speciesContent);
    }
    
    private void fillBox() throws SQLException {
        // Make description label
        Label desc = new Label(DESCRIPTION);
        desc.setPadding(new Insets(5, 5, 5, 5));

        // Stack together in vertical box
        speciesContent.setPadding(new Insets(5, 5, 5, 5));
        speciesContent.getChildren().addAll(
                desc, createButtonBox(), new StackPane(table));
    }
    
    private HBox createButtonBox() {
        // Make refresh button
        Button refreshButton = new Button(REFRESH);
        refreshButton.setPrefWidth(BUTTON_WIDTH);
        refreshButton.setOnMousePressed((MouseEvent event) -> {
            try {
                table.refresh();
                table.getItems().clear();
                fillTable();
            }
            catch (SQLException ex) {
                System.out.println(ex.getMessage());
            }
        });
        
        // Put together in a horizontal box
        HBox buttonBox = new HBox();
        buttonBox.setPadding(new Insets(5, 5, 10, 5));
        buttonBox.getChildren().addAll(refreshButton);
        return buttonBox;
    }
    
    private void fillTable() throws SQLException {
        table.setEditable(true);
        table.setPadding(new Insets(5, 5, 5, 5));
        
        // Fill the table
        table.setItems(Query.getSpecies());
    }
    
    private void createColumns() throws SQLException {
        // For each attribute in the table, label column
        List<String> cols = Query.getSpeciesCols();
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
    
    private final Tab tab;
    private final TableView table = new TableView();
    private final VBox speciesContent = new VBox();
    private final String TITLE = "Species";
    private final String REFRESH = "Refresh";
    private final int BUTTON_WIDTH = 100;
    private final String DESCRIPTION = "View all the species currently"
            + " present in the database by both their scientific"
            + " and common names.";
}
