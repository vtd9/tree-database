package gui;

import javafx.geometry.Insets;
import javafx.scene.control.Button;
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
import entities.Sighting;

/**
 * Create the habitat tab for the main TabPane.
 */
public class TabHabitat {
    /**
     * Construct a tab that lets the user see results from joining the habitat,
     * grows_in, and tree tables.
     * @param query instance of Query to execute SQL queries with 
     */
    public TabHabitat(Query query) {
        this.query = query;

        // Create new Tab object
        fillBox();
        tab = new Tab(TITLE);
        tab.setContent(sightingBox);
    }

    /**
     * Fill the main vertical box of the tab with the relevant widgets.
     */
    private void fillBox() {
        // Resize the VBox
        sightingBox.setPrefWidth(App.PREF_WIDTH);
        
        // Make description label
        Label desc = new Label(DESCRIPTION);
        desc.setWrapText(true);
        desc.setPadding(new Insets(5, 10, 5, 5));
        
        // Add the refresh button
        Button refreshButton = new Button(REFRESH);
        refreshButton.setPrefWidth(App.BUTTON_WIDTH);
        refreshButton.setOnMousePressed((MouseEvent event) -> {
            table.getItems().clear();
            fillTableNoFilter();
        }); 
        
        // Put description and refresh button together
        HBox descRefreshBox = new HBox();
        descRefreshBox.getChildren().addAll(desc, refreshButton);

        // Stack widgets in a vertical box
        sightingBox.setPadding(new Insets(5, 5, 5, 5));
        sightingBox.getChildren().addAll(
                descRefreshBox,
                createButtonBox(),
                new StackPane(table));
    }
    
    /**
     * Create a horizontal box containing the filtering widgets.
     * @return HBox of an empty field, a search button, and radio buttons
     */
    private HBox createButtonBox() {
        // Initialize nameField
        nameField = new TextField();
        nameField.setPromptText(NAME);

        // Make search button
        Button searchButton = new Button(SEARCH);
        searchButton.setPrefWidth(App.BUTTON_WIDTH);
        searchButton.setOnMousePressed((MouseEvent event) -> {
            table.getItems().clear();
            fillTableFilter();
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
     * Fill the table with all occupied habitats with their corresponding
     * tree species.
     */
    private void fillTableNoFilter() {
        table.setEditable(true);
        table.setPadding(new Insets(5, 5, 5, 5));
        table.setItems(query.getHabitatTree());
        createColumns();
    }
 
    /**
     * Fill the table as a result of filtering the habitat-tree query by 
     * species.
     */
    private void fillTableFilter() {
        table.setEditable(true);
        table.setPadding(new Insets(5, 5, 5, 5));

        // Determine if input given as scientific or common name
        RadioButton selected = (RadioButton) nameGroup.getSelectedToggle();
        String name = nameField.getText();
            if (!name.isEmpty()) {
            if (selected.getText().equals(RADIO_SCI)) 
                table.setItems(query.getHabitatTreeFilt(name, true));
            else 
                table.setItems(query.getHabitatTreeFilt(name, false));
            createColumns();
        }
    }
    
    /**
     * Create columns for the table
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
     * Get reference to the populated tab object.
     * @return Tab object with the initialized habitat-related widgets.
     */
    public Tab getTab() {
        return tab;
    }

    private final Query query;
    private final Tab tab;
    private final TableView table = new TableView();
    private final VBox sightingBox = new VBox();
    private TextField nameField;
    private ToggleGroup nameGroup;
    
    private final String TITLE = "Habitat";
    private final String REFRESH = "Refresh";
    private final String SEARCH = "Search";
    private final String DESCRIPTION = "See all the habitats and their possible"
            + " tree inhabitants.";
    private final String NAME = "Name";
    private final String RADIO_SCI = "Scientific name";
    private final String RADIO_COM = "Common name";

}