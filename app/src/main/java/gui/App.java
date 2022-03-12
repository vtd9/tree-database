package gui;

import java.sql.Connection;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.stage.Stage;
import db.Connector;

/**
 * Launch the graphical user interface (GUI) to get and set information in 
 * the forestry MySQL database. Assume the view of an field biologist that has
 * the authorization to perform certain getting and setting actions:
 * 
 * (1) View all tree species in the database with their common names
 * (2) Add a new common name to a species existing in the database
 * (3) View all sightings of a particular species
 * (4) Add a new sighting of a particular species
 * (5) View all occupied habitats with their corresponding species
 * (6) View all habitats known to support a particular species
 * 
 */
public class App extends Application {
    /**
     * Launch the GUI.
     * @param args command-line arguments, if any
     */
    public static void main(String[] args){
        launch();
    }

    /**
     * Launch the main JavaFX application, allowing the GUI to be shown.
     * @param stage Stage onto which the main JavaFX application will be set
     */
    @Override
    public void start(Stage stage) {
        stage.setWidth(PREF_WIDTH);
        stage.setTitle("Forestry Data");
        var scene = new Scene(new Group());
        stage.setScene(scene);
        
        // Connect to database and add tabs
        Connection conn = Connector.makeConnection();
        ((Group) scene.getRoot()).getChildren().addAll(
                new Tabs(conn).getPane());
        
        stage.show();
    }
    
    public static final int PREF_WIDTH = 700;
    public static final int BUTTON_WIDTH = 70;
}