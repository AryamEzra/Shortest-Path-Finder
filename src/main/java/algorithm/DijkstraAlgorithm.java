package main.java.algorithm;

import main.java.model.Edge;
import main.java.model.Graph;
import main.java.model.Node;
import java.util.*;

public class DijkstraAlgorithm {
    private final Graph graph;
    
    public DijkstraAlgorithm(Graph graph) {
        this.graph = graph;
    }
    
    public PathResult findShortestPath(Node start, Node end) {
        Map<Node, Double> distances = new HashMap<>();
        Map<Node, Node> previousNodes = new HashMap<>();
        PriorityQueue<Node> queue = new PriorityQueue<>(
            Comparator.comparingDouble(distances::get)
        );
        
        // Initialize distances
        for (Node node : graph.getNodes()) {
            distances.put(node, Double.POSITIVE_INFINITY);
            previousNodes.put(node, null);
        }
        distances.put(start, 0.0);
        queue.add(start);
        
        while (!queue.isEmpty()) {
            Node current = queue.poll();
            
            if (current.equals(end)) break;
            
            for (Edge edge : graph.getEdgesFromNode(current)) {
                Node neighbor = edge.getDestination();
                double newDist = distances.get(current) + edge.getDistance();
                
                if (newDist < distances.get(neighbor)) {
                    distances.put(neighbor, newDist);
                    previousNodes.put(neighbor, current);
                    queue.add(neighbor);
                }
            }
        }
        
        // Build path
        List<Node> path = new ArrayList<>();
        for (Node at = end; at != null; at = previousNodes.get(at)) {
            path.add(at);
        }
        Collections.reverse(path);
        
        return new PathResult(path, distances.get(end));
    }
    
    // public static class PathResult {
    //     private final List<Node> path;
    //     private final double totalDistance;
        
    //     public PathResult(List<Node> path, double totalDistance) {
    //         this.path = path;
    //         this.totalDistance = totalDistance;
    //     }
        
    //     public List<Node> getPath() { return path; }
    //     public double getTotalDistance() { return totalDistance; }
    // }

    public static class PathResult {
        private final List<Node> path;
        private final double totalDistance;
    
        public PathResult(List<Node> path, double totalDistance) {
            this.path = path;
            this.totalDistance = totalDistance;
        }
    
        public List<Node> getPath() { return path; }
        public double getTotalDistance() { return totalDistance; }
    
        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder();
            sb.append("Shortest Path Details:\n");
            sb.append(String.format("Total Distance: %.2f units\n", totalDistance));
            sb.append("Path Sequence:\n");
            
            for (int i = 0; i < path.size(); i++) {
                Node node = path.get(i);
                sb.append(String.format("%d. %s (%.1f, %.1f)", 
                    i+1, node.getName(), node.getX(), node.getY()));
                
                if (i < path.size() - 1) {
                    Node next = path.get(i+1);
                    double segmentDistance = node.distanceTo(next);
                    sb.append(String.format(" → %.2f units → ", segmentDistance));
                } else {
                    sb.append("\n");
                }
            }
            return sb.toString();
        }
    }
}