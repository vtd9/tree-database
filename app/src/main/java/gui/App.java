package gui;

import java.sql.SQLException;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.stage.Stage;
import sqljdbc.Connector;
import sqljdbc.Query;

/**
 * Launches the graphical user interface (GUI) to get and set information in 
 * the forestry MySQL database. Assume the view of an field biologist that has
 * authorization to perform certain admin actions.
 */
public class App extends Application {
    
    public static void main(String[] args) throws SQLException {
        connect();
        launch();
    }

    @Override
    public void start(Stage stage) throws SQLException {
        stage.setTitle("Forestry Data");
        //stage.setWidth(700);
        //stage.setHeight(500);
        var scene = new Scene(new Group());
        stage.setScene(scene);
        
        // Make TabPane
        ((Group) scene.getRoot()).getChildren().addAll(new Tabs().getTabs());
        
        stage.show();
    }
    
    public static void connect() {
        try {
            Connector.makeConnection();
            System.out.println(String.format("Connected to database %s "
                    + "successfully.", Connector.conn.getCatalog()));
        } 
        catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }      
    }
    
}