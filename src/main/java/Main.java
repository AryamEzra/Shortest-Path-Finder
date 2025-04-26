package main.java;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import main.java.view.MapView;

public class Main extends Application {
    @Override
    public void start(Stage primaryStage) {
        MapView mapView = new MapView();
        Scene scene = new Scene(mapView.createContent(), 800, 600);
        
        primaryStage.setTitle("Addis Ababa Shortest Path Finder");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
