package main;

// Add these imports for the Jackson test
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Map;
// End of added imports

import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import view.MapView; // Make sure this imports your original view for now

public class Main extends Application {
    @Override
    public void start(Stage primaryStage) {
        // NOTE: For this test, we assume the original MapView exists.
        // We haven't modified it yet as requested.
        // This start method will likely fail later if MapView tries
        // to use the old graph initialization, but that's okay for now.
        // The goal is *only* to test Jackson in the main method below.
        MapView mapView = new MapView();
        Parent root = mapView.createContent(); // Get the Parent layout
        Scene scene = new Scene(root, 800, 600); // Original size

        primaryStage.setTitle("Addis Ababa Shortest Path Finder"); // Original title
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {

        // --- Simple Jackson Dependency Test START ---
        System.out.println("=======================================");
        System.out.println("Attempting simple Jackson test...");
        try {
            ObjectMapper mapper = new ObjectMapper(); // Create Jackson's main object
            String simpleJsonString = "{\"testKey\": \"testValue\", \"number\": 123}";

            // Try to read the simple JSON string into a generic Map
            Map<String, Object> result = mapper.readValue(simpleJsonString, Map.class);

            // If we reach here without errors, Jackson parsed the JSON
            System.out.println("Jackson test SUCCESSFUL!");
            System.out.println("Parsed JSON map: " + result);
            System.out.println("Value for 'testKey': " + result.get("testKey"));
            System.out.println("Value for 'number': " + result.get("number"));

        } catch (JsonProcessingException e) {
            // This error means Jackson is present but failed to parse the JSON (unlikely for this simple string)
            System.err.println("Jackson test FAILED: Error processing JSON.");
            e.printStackTrace();
        } catch (NoClassDefFoundError e) { // Catch only NoClassDefFoundError
            // This error means the Jackson classes themselves couldn't be found - dependency issue!
            System.err.println("Jackson test FAILED: Jackson classes not found on classpath!");
            System.err.println("Check your pom.xml <dependencies> and ensure Maven downloaded the library.");
            e.printStackTrace();
        } catch (Exception e) {
            // Catch any other unexpected errors
            System.err.println("Jackson test FAILED: An unexpected error occurred.");
            e.printStackTrace();
        }
        System.out.println("=======================================");
        // --- Simple Jackson Dependency Test END ---


        // Launch the JavaFX application (existing line)
        launch(args);
    }
}