package main.java.view;

import main.java.algorithm.*;
import javafx.scene.Group;
import javafx.scene.control.Button;
// import javafx.scene.Node;
// import javafx.scene.Scene;
import javafx.scene.control.Label;
// import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import main.java.model.Graph;
import main.java.model.Node;
import main.java.model.Edge;


public class MapView {
    private Graph graph = new Graph();
    private Node startNode = null;
    private Node endNode = null;
    private Button resetButton;
    private Label resultLabel;
    private Group root;
    
    public MapView() {
        initializeSampleGraph();
    }
    
    private void initializeSampleGraph() {
        // Clear existing data
        graph = new Graph();
        
        // Create nodes (landmarks in Addis Ababa)
        Node meskel = new Node("Meskel Square", 300, 200);
        Node arat = new Node("Arat Kilo", 250, 300);
        Node piazza = new Node("Piazza", 200, 400);
        Node bole = new Node("Bole", 500, 150);
        Node mexico = new Node("Mexico", 450, 250);
        Node saris = new Node("Saris", 400, 350);
        Node summit = new Node("Summit", 350, 450);
        Node gotera = new Node("Gotera", 600, 200);
        Node legehar = new Node("Legehar", 550, 300);
        Node kebena = new Node("Kebena", 300, 350);
    
        // Add all nodes
        graph.addNode(meskel);
        graph.addNode(arat);
        graph.addNode(piazza);
        graph.addNode(bole);
        graph.addNode(mexico);
        graph.addNode(saris);
        graph.addNode(summit);
        graph.addNode(gotera);
        graph.addNode(legehar);
        graph.addNode(kebena);
    
        // Connect them with edges (distances calculated automatically)
        graph.addEdge(new Edge(meskel, arat));
        graph.addEdge(new Edge(meskel, bole));
        graph.addEdge(new Edge(meskel, kebena));
        graph.addEdge(new Edge(arat, piazza));
        graph.addEdge(new Edge(arat, kebena));
        graph.addEdge(new Edge(piazza, summit));
        graph.addEdge(new Edge(piazza, saris));
        graph.addEdge(new Edge(bole, gotera));
        graph.addEdge(new Edge(bole, mexico));
        graph.addEdge(new Edge(mexico, legehar));
        graph.addEdge(new Edge(mexico, saris));
        graph.addEdge(new Edge(saris, legehar));
        graph.addEdge(new Edge(saris, summit));
        graph.addEdge(new Edge(gotera, legehar));
    }
    
    public Group createContent() {
        root = new Group();

        // Create reset button
    resetButton = new Button("Reset");
    resetButton.setLayoutX(20);
    resetButton.setLayoutY(50);
    resetButton.setOnAction(e -> resetSelection());
    
    // Add the button to root
    root.getChildren().add(resetButton);
        
        // Draw edges first (so nodes appear on top)
        for (Edge edge : graph.getEdges()) {
            Line line = new Line(
                edge.getSource().getX(), edge.getSource().getY(),
                edge.getDestination().getX(), edge.getDestination().getY()
            );
            line.setStroke(Color.GRAY);
            root.getChildren().add(line);
        }
        
        // Draw nodes
        for (Node node : graph.getNodes()) {
            Circle circle = new Circle(node.getX(), node.getY(), 10, Color.BLUE);
            
            // Make nodes clickable
            circle.setOnMouseClicked(e -> {
                if (startNode == null) {
                    startNode = node;
                    circle.setFill(Color.GREEN);
                } else if (endNode == null && node != startNode) {
                    endNode = node;
                    circle.setFill(Color.RED);
                    calculateAndShowPath();
                }
            });
            
            // Add label
            Label label = new Label(node.getName());
            label.setLayoutX(node.getX() + 15);
            label.setLayoutY(node.getY() - 10);
            
            root.getChildren().addAll(circle, label);
        }
        
        return root;
    }
    
    private void calculateAndShowPath() {
        if (startNode != null && endNode != null) {
            DijkstraAlgorithm dijkstra = new DijkstraAlgorithm(graph);
            DijkstraAlgorithm.PathResult result = dijkstra.findShortestPath(startNode, endNode);
            
            // Print to terminal
            System.out.println("=================================");
            System.out.println(result);
            System.out.println("=================================");
    
            // Clear previous path if any
            root.getChildren().removeIf(node -> node instanceof Line && 
                ((Line)node).getStroke() == Color.RED);
    
            // Highlight the new path
            Node prev = null;
            for (Node node : result.getPath()) {
                if (prev != null) {
                    Line pathLine = new Line(
                        prev.getX(), prev.getY(),
                        node.getX(), node.getY()
                    );
                    pathLine.setStroke(Color.RED);
                    pathLine.setStrokeWidth(3);
                    root.getChildren().add(pathLine); // Add directly to root
                }
                prev = node;
            }
            
            // Show distance on screen
            if (resultLabel != null) {
                root.getChildren().remove(resultLabel);
            }
            resultLabel = new Label(
                String.format("Shortest path: %.1f units", result.getTotalDistance())
            );
            resultLabel.setLayoutX(20);
            resultLabel.setLayoutY(20);
            root.getChildren().add(resultLabel);
        }
    }

    private void resetSelection() {
        // Reset node colors
        for (javafx.scene.Node node : root.getChildren()) {
            if (node instanceof Circle) {
                ((Circle)node).setFill(Color.BLUE);
            }
        }
        
        // Clear path lines
        root.getChildren().removeIf(n -> n instanceof Line && 
            ((Line)n).getStroke() == Color.RED);
        
        // Clear result label
        if (resultLabel != null) {
            root.getChildren().remove(resultLabel);
            resultLabel = null;
        }
        
        // Reset selections
        startNode = null;
        endNode = null;
        
        System.out.println("\nSelection reset - ready for new pathfinding");
    }
}