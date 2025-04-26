package main.java.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Graph {
    private List<Node> nodes;
    private List<Edge> edges;
    private Map<Node, List<Edge>> adjacencyList;
    
    public Graph() {
        nodes = new ArrayList<>();
        edges = new ArrayList<>();
        adjacencyList = new HashMap<>();
    }
    
    public void addNode(Node node) {
        nodes.add(node);
        adjacencyList.put(node, new ArrayList<>());
    }
    
    public void addEdge(Edge edge) {
        edges.add(edge);
        adjacencyList.get(edge.getSource()).add(edge);
        // For undirected graph, add the reverse edge as well
        Edge reverseEdge = new Edge(edge.getDestination(), edge.getSource());
        edges.add(reverseEdge);
        adjacencyList.get(edge.getDestination()).add(reverseEdge);
    }
    
    public List<Node> getNodes() {
        return nodes;
    }
    
    public List<Edge> getEdges() {
        return edges;
    }
    
    public List<Edge> getEdgesFromNode(Node node) {
        return adjacencyList.get(node);
    }
    
    public Node getNodeByName(String name) {
        for (Node node : nodes) {
            if (node.getName().equalsIgnoreCase(name)) {
                return node;
            }
        }
        return null;
    }
}
