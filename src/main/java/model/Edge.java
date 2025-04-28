package model;

public class Edge {
    private final Node source;
    private final Node destination;
    private final double distance; // Real-world distance

    // Constructor now takes the real distance
    public Edge(Node source, Node destination, double distance) {
        this.source = source;
        this.destination = destination;
        this.distance = distance;
    }

    // Getters
    public Node getSource() { return source; }
    public Node getDestination() { return destination; }
    public double getDistance() { return distance; } // Return stored distance

     @Override
    public String toString() {
        return source.getName() + " -> " + destination.getName() + " (" + String.format("%.1f", distance) + "m)";
    }
}