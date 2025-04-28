package view;

import algorithm.DijkstraAlgorithm;
import algorithm.DijkstraAlgorithm.*; 
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.util.Duration;

import model.Edge;
import model.Graph;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MapView {

    private Graph graph;
    private model.Node startNode = null; 
    private model.Node endNode = null;

    // UI Elements
    private Group mapGroup; // Group for map elements (nodes, edges)
    private VBox controlPanel;
    private VBox rootLayout; // Panel for buttons and labels
    private Button resetButton;
    private Label statusLabel; // For messages like "Select start node"
    private Label resultLabel; // For distance result

    // Simulation control
    private Timeline simulationTimeline;
    private List<StepEvent> simulationSteps;
    private int currentStepIndex;

    // Map JavaFX Circles to model Nodes for easy lookup
    private Map<model.Node, Circle> nodeCircleMap = new HashMap<>();
    private Map<Edge, Line> edgeLineMap = new HashMap<>(); // To highlight edges if needed

    private static final Color NODE_DEFAULT_COLOR = Color.BLUE;
    private static final Color NODE_START_COLOR = Color.GREEN;
    private static final Color NODE_END_COLOR = Color.DARKRED; // Different end color
    private static final Color NODE_VISITED_COLOR = Color.ORANGE; // Color for visited nodes
    private static final Color NODE_IN_QUEUE_COLOR = Color.LIGHTBLUE; // Color for nodes added to queue
    private static final Color PATH_COLOR = Color.RED;
    private static final Color EDGE_DEFAULT_COLOR = Color.GRAY;

    public MapView() {
        loadGraphData();
    }

    private void loadGraphData() {
        graph = new Graph();
        try {
            graph.loadFromJSONResources("/data/nodes.json", "/data/edges.json");
        } catch (IOException e) {
            System.err.println("Failed to load graph data: " + e.getMessage());
            statusLabel = new Label("Error loading graph data!");
        }
    }

    public Parent createContent() {
        mapGroup = new Group();
        rootLayout = new VBox(10); 
        rootLayout.setPadding(new Insets(100));

        statusLabel = new Label("Select a start node.");
        resultLabel = new Label(""); 

        resetButton = new Button("Reset Selection");
        resetButton.setOnAction(e -> resetSelectionAndSimulation());
        resetButton.setDisable(true); 

        controlPanel = new VBox(10, statusLabel, resultLabel, resetButton); 
        controlPanel.setPadding(new Insets(10));
        controlPanel.setPrefWidth(200);
        controlPanel.setStyle("-fx-border-color: lightgray; -fx-border-width: 0 0 0 1;");
        
        drawGraph();
        rootLayout.getChildren().addAll(controlPanel, mapGroup);

        return rootLayout;
    }

    private void drawGraph() {
        mapGroup.getChildren().clear(); 
        nodeCircleMap.clear();
        edgeLineMap.clear();
        // Draw edges first (so nodes appear on top)
        for (Edge edge : graph.getEdges()) {
             if (edge.getSource().hashCode() < edge.getDestination().hashCode()) {
                Line line = new Line(
                        edge.getSource().getScreenX(), edge.getSource().getScreenY(),
                        edge.getDestination().getScreenX(), edge.getDestination().getScreenY()
                );
                line.setStroke(EDGE_DEFAULT_COLOR);
                line.setStrokeWidth(1.5); 
                mapGroup.getChildren().add(line);
                edgeLineMap.put(edge, line); 
             }
        }

        // Draw nodes
        for (model.Node node : graph.getNodes()) {
            Circle circle = new Circle(node.getScreenX(), node.getScreenY(), 8, NODE_DEFAULT_COLOR); // Smaller radius?
            nodeCircleMap.put(node, circle);

            Tooltip tt = new Tooltip(node.getName());
            Tooltip.install(circle, tt);

            circle.setOnMouseClicked(e -> handleNodeClick(node, circle));

            mapGroup.getChildren().add(circle);

            // Optional: Add labels (can make it cluttered)
            Label label = new Label(node.getName());
            label.setLayoutX(node.getScreenX() + 10);
            label.setLayoutY(node.getScreenY() - 10);
            mapGroup.getChildren().add(label);
        }
    }

     private void handleNodeClick(model.Node clickedNode, Circle clickedCircle) {
         // Ignore clicks if simulation is running
         if (simulationTimeline != null && simulationTimeline.getStatus() == Timeline.Status.RUNNING) {
             statusLabel.setText("Simulation running. Please wait or reset.");
             return;
         }

         if (startNode == null) {
             startNode = clickedNode;
             clickedCircle.setFill(NODE_START_COLOR);
             statusLabel.setText("Selected Start: " + startNode.getName() + ". Select end node.");
             resetButton.setDisable(false); // Enable reset button
         } else if (endNode == null && !clickedNode.equals(startNode)) {
             endNode = clickedNode;
             clickedCircle.setFill(NODE_END_COLOR);
             statusLabel.setText("Selected End: " + endNode.getName() + ". Calculating...");
             runSimulation(); // Start the simulation process
         } else if (clickedNode.equals(startNode)) {
              statusLabel.setText("Start node already selected. Select end node.");
         } else if (clickedNode.equals(endNode)) {
             statusLabel.setText("End node already selected. Reset to choose new path.");
         }
     }


    private void runSimulation() {
        if (startNode == null || endNode == null) return;

        resetButton.setDisable(true); 
        resultLabel.setText(""); 

        resetSimulationHighlights();

        DijkstraAlgorithm dijkstra = new DijkstraAlgorithm(graph);
        SimulationResult simResult = dijkstra.findShortestPathWithSimulation(startNode, endNode);

        simulationSteps = simResult.steps();
        PathResult finalPath = simResult.finalPathResult();
        currentStepIndex = 0;

        if (simulationSteps.isEmpty()) {
             statusLabel.setText("No steps to simulate.");
             displayFinalPath(finalPath); // Display result directly
             resetButton.setDisable(false);
             return;
        }

        // Setup Timeline for step-by-step visualization
        simulationTimeline = new Timeline();
        simulationTimeline.setCycleCount(simulationSteps.size()); 

        // Adjust duration for speed (100ms per step)
        KeyFrame kf = new KeyFrame(Duration.millis(100), e -> processSimulationStep());
        simulationTimeline.getKeyFrames().add(kf);

        simulationTimeline.setOnFinished(e -> {
             statusLabel.setText("Simulation finished.");
             displayFinalPath(finalPath);
             resetButton.setDisable(false); // Re-enable reset
        });

        statusLabel.setText("Simulating Dijkstra...");
        simulationTimeline.play();
    }

    private void processSimulationStep() {
        if (currentStepIndex >= simulationSteps.size()) {
            return;
        }

        StepEvent event = simulationSteps.get(currentStepIndex);

        // Update UI based on the event type
        if (event instanceof NodeVisitedEvent visitedEvent) {
            Circle circle = nodeCircleMap.get(visitedEvent.node());
            if (circle != null && !visitedEvent.node().equals(startNode) && !visitedEvent.node().equals(endNode)) {
                circle.setFill(NODE_VISITED_COLOR);
            }
             
        } else if (event instanceof QueueUpdateEvent queueEvent) {
             Circle circle = nodeCircleMap.get(queueEvent.node());
             if (circle != null && !queueEvent.node().equals(startNode) && !queueEvent.node().equals(endNode)) {
                 if(circle.getFill() != NODE_VISITED_COLOR) {
                    circle.setFill(NODE_IN_QUEUE_COLOR);
                 }
             }
        }

        currentStepIndex++;
    }

    private void displayFinalPath(PathResult result) {
         resetSimulationHighlights();

         if (result.isReachable()) {
            double distanceMeters = result.getTotalDistance();
            double distanceKilometers = distanceMeters / 1000.0; 
            resultLabel.setText(String.format("Shortest path: %.1f meters (%.2f km)",
                                              distanceMeters, distanceKilometers));
             model.Node prev = null;
             for (model.Node node : result.getPath()) {
                  Circle circle = nodeCircleMap.get(node);
                  if (circle != null) {
                      if (node.equals(startNode)) circle.setFill(NODE_START_COLOR);
                      else if (node.equals(endNode)) circle.setFill(NODE_END_COLOR);
                  }

                 // Draw path lines on top
                 if (prev != null) {
                     Line pathLine = new Line(
                             prev.getScreenX(), prev.getScreenY(),
                             node.getScreenX(), node.getScreenY()
                     );
                     pathLine.setStroke(PATH_COLOR);
                     pathLine.setStrokeWidth(3);
                     pathLine.setUserData("PATH_LINE"); // Mark as path line for easy removal
                     mapGroup.getChildren().add(pathLine);
                 }
                 prev = node;
             }
             System.out.println("=================================");
             System.out.println(result); // Print detailed path to console
             System.out.println("=================================");
         } else {
             resultLabel.setText("End node is not reachable.");
              System.out.println("End node is not reachable.");
         }
    }

    // Reset
    private void resetSelectionAndSimulation() {
         System.out.println("\nResetting selection and simulation...");
         if (simulationTimeline != null) {
             simulationTimeline.stop();
             simulationTimeline = null;
         }

         mapGroup.getChildren().removeIf(n -> "PATH_LINE".equals(n.getUserData()));

         resetSimulationHighlights();
         if(startNode != null) {
             Circle startCircle = nodeCircleMap.get(startNode);
             if(startCircle != null) startCircle.setFill(NODE_DEFAULT_COLOR);
         }
          if(endNode != null) {
             Circle endCircle = nodeCircleMap.get(endNode);
             if(endCircle != null) endCircle.setFill(NODE_DEFAULT_COLOR);
         }

        startNode = null;
        endNode = null;
        simulationSteps = null;
        currentStepIndex = 0;

        statusLabel.setText("Select a start node.");
        resultLabel.setText("");
        resetButton.setDisable(true);
    }

    private void resetSimulationHighlights() {
        for (Map.Entry<model.Node, Circle> entry : nodeCircleMap.entrySet()) {
            model.Node node = entry.getKey();
            Circle circle = entry.getValue();
            if (!node.equals(startNode) && !node.equals(endNode)) {
                circle.setFill(NODE_DEFAULT_COLOR);
            } else if (node.equals(startNode)) {
                circle.setFill(NODE_START_COLOR);
            } else if (node.equals(endNode)) {
                circle.setFill(NODE_END_COLOR); 
            }
        }
    }

}