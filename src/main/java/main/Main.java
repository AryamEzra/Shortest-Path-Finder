package main;

import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import view.MapView; 

public class Main extends Application {
    @Override
    public void start(Stage primaryStage) {
        MapView mapView = new MapView();
        Parent root = mapView.createContent(); // Create the map view content
        
        Scene scene = new Scene(root, 800, 600); // display size of the window

        primaryStage.setTitle("Addis Ababa Shortest Path Finder (Offline Simulation)");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}