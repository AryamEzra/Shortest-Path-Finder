package main;

import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import view.MapView;

public class Main extends Application {
    @Override
    public void start(Stage primaryStage) {
        MapView mapView = new MapView();
        Parent mapContent = mapView.createContent(); // Get the map content

        // Wrap mapContent in a StackPane to center it
        StackPane root = new StackPane(mapContent);

        // Dynamically center the map when the window is resized
        root.widthProperty().addListener((obs, oldVal, newVal) -> {
            mapContent.setTranslateX((newVal.doubleValue() - mapContent.getBoundsInParent().getWidth()) / 2);
        });
        root.heightProperty().addListener((obs, oldVal, newVal) -> {
            mapContent.setTranslateY((newVal.doubleValue() - mapContent.getBoundsInParent().getHeight()) / 2);
        });

        Scene scene = new Scene(root, 950, 650); // Adjusted size slightly for controls

        primaryStage.setTitle("Addis Ababa Shortest Path Finder (Offline Simulation)");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}