package gui;

import java.sql.Connection;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.stage.Stage;
import db.Connector;

/**
 * Launches the graphical user interface (GUI) to get and set information in 
 * the forestry MySQL database. Assume the view of an field biologist that has
 * authorization to perform certain admin actions.
 */
public class App extends Application {
    
    public static void main(String[] args){
        // Launch GUI
        launch();
    }

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
    
    public static final int PREF_WIDTH = 600;
    public static final int BUTTON_WIDTH = 80;
}