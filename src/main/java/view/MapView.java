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
        graph = new Graph();
        
        // Central Hub Nodes (15 nodes)
        Node meskel = addNode("Meskel Square", 400, 300);
        Node arat = addNode("Arat Kilo", 350, 350);
        Node piazza = addNode("Piazza", 300, 400);
        Node bole = addNode("Bole Airport", 500, 250);
        Node mexico = addNode("Mexico", 450, 300);
        Node saris = addNode("Saris", 400, 350);
        Node summit = addNode("Summit", 350, 450);
        Node gotera = addNode("Gotera", 600, 250);
        Node legehar = addNode("Legehar", 550, 300);
        Node kebena = addNode("Kebena", 350, 320);
        Node megenagna = addNode("Megenagna", 420, 330);
        Node urael = addNode("Urael", 330, 300);

    
        // Bole Area Expansion (10 nodes)
        Node boleMed = addNode("Bole Medhanialem", 520, 220);
        Node boleBulb = addNode("Bole Bulbula", 550, 200);
        Node boleDembel = addNode("Dembel City", 480, 230);
        Node boleBridge = addNode("Bole Bridge", 470, 270);
        Node bole18 = addNode("Bole 18", 530, 280);
    
        // Piassa Area Expansion (8 nodes)
        Node enatBank = addNode("Enat Bank", 280, 380);
        Node posta = addNode("Posta Bet", 270, 350);
        Node shiroMeda = addNode("Shiro Meda", 250, 450);
        Node entoto = addNode("Entoto", 200, 500);
        Node merkato = addNode("Merkato", 320, 380);
    
        // Eastern Expansion (8 nodes)
        Node gotera2 = addNode("Gotera 2", 620, 270);
        Node legehar2 = addNode("Legehar 2", 580, 320);
        Node megenagna2 = addNode("Megenagna 2", 440, 360);
        Node saris2 = addNode("Saris 2", 430, 400);
    
        // Western Expansion (9 nodes)
        Node kolfe = addNode("Kolfe", 200, 350);
        Node gurdShola = addNode("Gurd Shola", 220, 320);
        Node mekaneYesus = addNode("Mekane Yesus", 240, 280);
        Node bantyketu = addNode("Bantyketu", 180, 400);
    
        // Connect Central Hub (Radial Pattern)
        connectBidirectional(meskel, arat, 1500);
        connectBidirectional(meskel, bole, 4000);
        connectBidirectional(meskel, kebena, 800);
        connectBidirectional(arat, piazza, 2000);
        connectBidirectional(arat, kebena, 700);
        connectBidirectional(piazza, summit, 2500);
        connectBidirectional(piazza, merkato, 800);
        connectBidirectional(bole, gotera, 3000);
        connectBidirectional(bole, mexico, 1500);
        connectBidirectional(mexico, legehar, 1800);
        connectBidirectional(mexico, saris, 2200);
        connectBidirectional(saris, legehar, 1200);
        connectBidirectional(saris, summit, 1500);
        connectBidirectional(gotera, legehar, 2500);
    
        // Connect Bole Area (Dense Network)
        connectBidirectional(bole, boleMed, 500);
        connectBidirectional(boleMed, boleBulb, 800);
        connectBidirectional(boleMed, boleDembel, 600);
        connectBidirectional(boleDembel, boleBridge, 400);
        connectBidirectional(boleBridge, mexico, 300);
        connectBidirectional(boleBridge, bole18, 700);
    
        // Connect Piassa Area (Historic Center)
        connectBidirectional(piazza, enatBank, 200);
        connectBidirectional(enatBank, posta, 150);
        connectBidirectional(posta, urael, 300);
        connectBidirectional(piazza, shiroMeda, 1000);
        connectBidirectional(shiroMeda, entoto, 1500);
        connectBidirectional(piazza, merkato, 800);
        connectBidirectional(merkato, kolfe, 1200);
    
        // Connect Ring Roads
        connectBidirectional(gotera, gotera2, 500);
        connectBidirectional(legehar, legehar2, 400);
        connectBidirectional(megenagna, megenagna2, 600);
        connectBidirectional(saris, saris2, 800);
    
        // Connect Western Areas
        connectBidirectional(kolfe, gurdShola, 300);
        connectBidirectional(gurdShola, mekaneYesus, 400);
        connectBidirectional(mekaneYesus, kebena, 500);
        connectBidirectional(kolfe, bantyketu, 900);
    }
    
    // Helper methods
    private Node addNode(String name, double x, double y) {
        Node node = new Node(name, x, y);
        graph.addNode(node);
        return node;
    }
    
    private void connectBidirectional(Node a, Node b, double meters) {
        graph.addEdge(new Edge(a, b));
        graph.addEdge(new Edge(b, a));
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