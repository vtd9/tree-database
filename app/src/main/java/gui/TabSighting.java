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
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import db.Query;
import db.Sighting;

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
        sightingBox.setPrefWidth(App.PREF_WIDTH);
        
        // Make description label
        Label desc = new Label(DESCRIPTION);
        desc.setWrapText(true);
        desc.setPadding(new Insets(5, 5, 5, 5));

        // Stack together in a vertical box
        sightingBox.setPadding(new Insets(5, 10, 5, 10));
        sightingBox.getChildren().addAll(
                desc, createButtonBox(), new StackPane(table));
    }
    
    private HBox createButtonBox() {
        // Make text field
        Label nameLabel = new Label(NAME);
        nameField = new TextField();

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
        buttonBox.getChildren().addAll(
                nameLabel, nameField, searchButton, rbSci, rbCom);
        return buttonBox;
    }
    
    private void fillTable() {
        table.setEditable(true);
        table.setPadding(new Insets(5, 5, 5, 5));

        // Determine if input given as scientific or common name
        RadioButton selected = (RadioButton) nameGroup.getSelectedToggle();
        if (selected.getText().equals(RADIO_SCI)) 
            table.setItems(query.getSightings(nameField.getText(), true));
        else 
            table.setItems(query.getSightings(nameField.getText(), false));
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
    private final String SEARCH = "Search";
    private final String DESCRIPTION = "Find all the sightings of a species"
            + " currently in the database by either its scientific or"
            + " common name. Altitude is in meters.";
    private final String NAME = "Name:";
    private TextField nameField;
    private final String RADIO_SCI = "Scientific name";
    private final String RADIO_COM = "Common name";
    private ToggleGroup nameGroup;
}