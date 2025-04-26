package model;

public class Node {
    private String name;
    private double x; // x-coordinate on screen
    private double y; // y-coordinate on screen
    
    public Node(String name, double x, double y) {
        this.name = name;
        this.x = x;
        this.y = y;
    }
    
    // Getters
    public String getName() { return name; }
    public double getX() { return x; }
    public double getY() { return y; }
    
    // Calculate distance to another node (for edge weight)
    public double distanceTo(Node other) {
        double dx = x - other.x;
        double dy = y - other.y;
        return Math.sqrt(dx*dx + dy*dy);
    }
}