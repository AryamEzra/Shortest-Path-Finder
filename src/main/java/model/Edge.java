package main.java.model;

public class Edge {
    private Node source;
    private Node destination;
    
    public Edge(Node source, Node destination, double d) {
        this.source = source;
        this.destination = destination;
    }
    
    // Getters
    public Node getSource() { return source; }
    public Node getDestination() { return destination; }
    public double getDistance() { 
        return source.distanceTo(destination); 
    }
}
