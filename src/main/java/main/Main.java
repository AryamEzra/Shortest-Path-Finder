package main;

import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import view.MapView; // Make sure this imports your updated view

public class Main extends Application {
    @Override
    public void start(Stage primaryStage) {
        MapView mapView = new MapView();
        Parent root = mapView.createContent(); // Get the Parent layout
        Scene scene = new Scene(root, 800, 650); // Adjusted size slightly for controls

        primaryStage.setTitle("Addis Ababa Shortest Path Finder (Offline Simulation)");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}