package gui;

import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Tab;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import java.util.List;
import db.Query;
import db.Update;
import entities.Sighting;

/**
 * Create the species tab for the main TabPane
 */
public class TabSighting {
    public TabSighting(Query query, Update update) {
        this.query = query;
        this.update = update;

        // Create new Tab object
        fillBox();
        tab = new Tab(TITLE);
        tab.setContent(sightingBox);
    }

    /**
     * Fill the main vertical box with relevant widgets for getting and setting
     * information in the sighting table.
     */
    private void fillBox() {
        // Resize the VBox
        sightingBox.setPrefWidth(App.PREF_WIDTH);
        
        // Make description label
        Label desc = new Label(DESCRIPTION);
        desc.setWrapText(true);
        desc.setPadding(new Insets(5, 10, 5, 5));

        // Stack together in a vertical box
        sightingBox.setPadding(new Insets(5, 5, 5, 5));
        sightingBox.getChildren().addAll(
                desc, 
                createButtonBox(),
                new StackPane(table),
                createNewSightBox());
    }
    
    /**
     * Create horizontal box with a text field and buttons for adding a new
     * common name.
     * @return HBox object with relevant widgets for the species tab
     */
    private HBox createButtonBox() {
        // Initialize nameField
        nameField = new TextField();
        nameField.setPromptText(NAME);

        // Make execute button
        Button searchButton = new Button(SEARCH);
        searchButton.setPrefWidth(App.BUTTON_WIDTH);
        searchButton.setOnMousePressed((MouseEvent event) -> {
            table.getItems().clear();
            fillTable();
        });
        
        // Make radio buttons
        RadioButton rbSci = new RadioButton(RADIO_SCI);
        RadioButton rbCom = new RadioButton(RADIO_COM);
        rbSci.setPadding(new Insets(5, 5, 5, 5));
        rbCom.setPadding(new Insets(5, 5, 5, 5));
        rbSci.setSelected(true);
        
        // Add radio buttons to ToggleGroup
        nameGroup = new ToggleGroup();
        rbSci.setToggleGroup(nameGroup);
        rbCom.setToggleGroup(nameGroup);

        // Put together in a horizontal box
        HBox buttonBox = new HBox();
        buttonBox.setSpacing(5);
        buttonBox.setPadding(new Insets(5, 5, 10, 5));
        buttonBox.getChildren().addAll(nameField, searchButton, rbSci, rbCom);
        return buttonBox;
    }

    /**
     * Populate the table with the result of a sighting filter.
     */
    private void fillTable() {
        table.setEditable(true);
        table.setPadding(new Insets(5, 5, 5, 5));

        // Determine if input given as scientific or common name
        RadioButton selected = (RadioButton) nameGroup.getSelectedToggle();
        String name = nameField.getText();
        if (!name.isEmpty()) {
            if (selected.getText().equals(RADIO_SCI)) 
                table.setItems(query.getSightings(name, true));
            else 
                table.setItems(query.getSightings(name, false));
            createColumns();
        }
    }
    
    /**
     * Create columns for the table.
     */
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
    
    /**
     * Create a horizontal box with a label, combo box, text fields, and a
     * button to execute an insert into the sightings table.
     * @return HBox object with widgets for the setting operation for sighting
     */
    private HBox createNewSightBox() {
        // Description of what setter is doing
        Label desc = new Label(DESC_ADD_SIGHT);
        
        // Make ComboBox to select species to associate sighting with
        ComboBox speciesCombo =
                new ComboBox(FXCollections.observableList(
                        query.getSpecies()));
        
        // Initialize fields
        initSightFields(App.BUTTON_WIDTH);
        
        // Make add button with listener
        Button addButton = new Button(ADD);
        addButton.setPrefWidth(App.BUTTON_WIDTH);
        addButton.setOnMousePressed((MouseEvent event) -> {
            // Get ComboBox result
            String[] sciName = Query.splitSciName(
                    speciesCombo.getValue().toString());

            // Pass inputs from text fields into method
            update.addSighting(sciName[0], sciName[1], newDate.getText(),
                    newLatitude.getText(), newLongitude.getText(),
                    newAltitude.getText());
        });
         
        // Make the horizontal box and place the widgets in
        HBox newSightBox = new HBox();
        newSightBox.setSpacing(5);
        newSightBox.setPadding(new Insets(5, 5, 5, 5));
        newSightBox.getChildren().addAll(desc, speciesCombo,
                newDate, newLatitude, newLongitude, newAltitude, addButton);
        return newSightBox;
    }
    
    /**
     * Initialize and set prompt text on the main four text fields for
     * inserting a new tree sighting.
     * @param fieldWidth 
     */
    private void initSightFields(int fieldWidth) {
        newDate = new TextField();
        newLatitude = new TextField();
        newLongitude = new TextField();
        newAltitude = new TextField();
        
        newDate.setPrefWidth(fieldWidth*1.3);
        newLatitude.setPrefWidth(fieldWidth);
        newLongitude.setPrefWidth(fieldWidth);
        newAltitude.setPrefWidth(fieldWidth);
        
        newDate.setPromptText(PROMPT_DATE);
        newLatitude.setPromptText(PROMPT_LAT);
        newLongitude.setPromptText(PROMPT_LON);
        newAltitude.setPromptText(PROMPT_ALT);
    }
    
    /**
     * Get the created tab.
     * @return Tab object for the sighting-related views and operations
     */
    public Tab getTab() {
        return tab;
    }

    private final Query query;
    private final Update update;
    private final Tab tab;
    private final TableView table = new TableView();
    private final VBox sightingBox = new VBox();
    private TextField nameField;
    private ToggleGroup nameGroup;
    
    private final String TITLE = "Sightings";
    private final String SEARCH = "Search";
    private final String DESCRIPTION = "Find all the sightings of a species"
            + " currently in the database by either its scientific or"
            + " common name. Altitude is in meters.";
    private final String NAME = "Name";
    private final String RADIO_SCI = "Scientific name";
    private final String RADIO_COM = "Common name";
    
    // Data for inserting new sighting pieces
    private final String DESC_ADD_SIGHT = "Add species sighting:";
    private final String ADD = "Add";
    private TextField newDate, newLatitude, newLongitude, newAltitude;
    private final String PROMPT_DATE = "YYYY-MM-DD";
    private final String PROMPT_LAT = "Latitude";
    private final String PROMPT_LON = "Longitude";
    private final String PROMPT_ALT = "Altitude";
}