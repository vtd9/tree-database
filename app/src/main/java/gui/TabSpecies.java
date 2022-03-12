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
import db.Update;
import entities.SpeciesName;
import javafx.collections.FXCollections;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;

/**
 * Create the species tab for the main TabPane
 */
public class TabSpecies {
    public TabSpecies(Query query, Update update) {
        this.query = query;
        this.update = update;
        
        // Initialize class variables
        speciesBox = new VBox();
        table = new TableView();

        // Create new Tab object
        fillBox();
        tab = new Tab(TITLE);
        tab.setContent(speciesBox);
    }
    
    /**
     * Fill a vertical box with widgets to set and get species data
     */
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
                desc, createButtonBox(), new StackPane(table),
                createAddBox());
    }
    
    /**
     * Create a horizontal box with a refresh button
     * @return HBox object with a refresh button
     */
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
    
    /**
     * Populate the table view with species
     */
    private void fillTable() {
        table.setEditable(true);
        table.setPadding(new Insets(5, 5, 5, 5));
        
        // Fill the table
        table.setItems(query.getSpeciesName());
        createColumns();
    }
    
    /**
     * Create columns for the table
     */
    private void createColumns() {
        // Flush previous columns, if any
        table.getColumns().clear();
        
        // For each attribute in the table, label column and attach a factory
        List<String> cols = query.getColNames();
        for (int i = 0; i < cols.size(); i++) {
            TableColumn col = new TableColumn(cols.get(i));
            col.setCellValueFactory(
                    new PropertyValueFactory<SpeciesName, String>(cols.get(i))
            );            
            table.getColumns().add(col);
        }
    }
    /**
     * Create a horizontal box with a label, text field, and add button.
     * @return HBox object with widgets used for setting
     */
    private HBox createAddBox() {
            // Make description
            Label desc = new Label(DESC_ADD_COM_NAME);

            // Make ComboBox to select species to update
            ComboBox speciesCombo =
                    new ComboBox(FXCollections.observableList(
                            query.getSpecies()));
            
            // Initialize text field
            newNameField = new TextField();
            
            // Make add button with listener
            Button addButton = new Button(ADD);
            addButton.setPrefWidth(App.BUTTON_WIDTH);
            addButton.setOnMousePressed((MouseEvent event) -> {
                // Get ComboBox result
                String[] sciName = Query.splitSciName(
                        speciesCombo.getValue().toString());
                
                // Get text field input
                String newName = newNameField.getText();
                if (!newName.isEmpty() && sciName.length == 2) {
                    update.addCommonName(newName, sciName[0], sciName[1]);
                }
            });        
            
            // Make the horizontal box and place the widgets in
            HBox addBox = new HBox();
            addBox.setSpacing(5);
            addBox.setPadding(new Insets(5, 5, 5, 5));
            addBox.getChildren().addAll(
                    desc, speciesCombo, newNameField, addButton);
            return addBox;        
        }

    /**
     * Get created Tab object
     * @return Tab object for showing and manipulating the species data
     */
    public Tab getTab() {
        return tab;
    }

    private final Query query;
    private final Update update;
    private final Tab tab;
    private final TableView table;
    private final VBox speciesBox;
    private final String TITLE = "Species";
    private final String REFRESH = "Refresh";
    private final String DESCRIPTION = "View all the species currently"
            + " present in the database by both their scientific"
            + " and common names.";
    
    // Data for setting components
    private final String DESC_ADD_COM_NAME = "Insert a new common name to"
            + " an existing species:";
    private final String ADD = "Add";
    private TextField newNameField;
    
}
