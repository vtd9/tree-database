package gui;

import java.sql.Connection;
import java.sql.SQLException;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.stage.Stage;
import sqljdbc.Connector;

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
        stage.setTitle("Forestry Data");
        var scene = new Scene(new Group());
        stage.setScene(scene);
        
        // Add tabs
        try {
            Connection conn = Connector.makeConnection();
            ((Group) scene.getRoot()).getChildren().addAll(
                    new Tabs(conn).getPane());
        }
        catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        
        stage.show();
    }
    
}