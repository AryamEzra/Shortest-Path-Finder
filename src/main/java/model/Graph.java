package model;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Graph {
    // Store nodes by name for easy lookup during edge loading
    private final Map<String, Node> nodesByName = new HashMap<>();
    // Adjacency list: Map<Node, List<Edge starting from Node>>
    private final Map<Node, List<Edge>> adjacencyList = new HashMap<>();

    public Graph() {
        // Constructor is now empty, loading happens explicitly
    }

    /**
     * Loads graph data from JSON files within the application's resources.
     * @param nodesResourcePath Path to nodes JSON file within resources (e.g., "/data/nodes.json")
     * @param edgesResourcePath Path to edges JSON file within resources (e.g., "/data/edges.json")
     * @throws IOException If files cannot be read or parsed
     */
    public void loadFromJSONResources(String nodesResourcePath, String edgesResourcePath) throws IOException {
        ObjectMapper mapper = new ObjectMapper();

        // --- Load Nodes ---
        InputStream nodesStream = getClass().getResourceAsStream(nodesResourcePath);
        if (nodesStream == null) {
            throw new IOException("Cannot find nodes resource file: " + nodesResourcePath);
        }
        List<Node> nodes = mapper.readValue(nodesStream, new TypeReference<List<Node>>() {});
        nodesStream.close(); // Close stream after reading

        // Store nodes and initialize adjacency list entries
        nodesByName.clear();
        adjacencyList.clear();
        for (Node node : nodes) {
            if (nodesByName.containsKey(node.getName())) {
                 System.err.println("Warning: Duplicate node name found '" + node.getName() + "'. Ignoring duplicate.");
                 continue; // Skip duplicates to avoid issues
            }
            nodesByName.put(node.getName(), node);
            adjacencyList.put(node, new ArrayList<>()); // Initialize empty list for edges
        }
         System.out.println("Loaded " + nodesByName.size() + " unique nodes.");


        // --- Load Edges ---
        // Define a simple helper class or use Map for temporary edge data
         TypeReference<List<Map<String, Object>>> typeRef = new TypeReference<>() {};
         InputStream edgesStream = getClass().getResourceAsStream(edgesResourcePath);
         if (edgesStream == null) {
            throw new IOException("Cannot find edges resource file: " + edgesResourcePath);
         }
         List<Map<String, Object>> edgeDataList = mapper.readValue(edgesStream, typeRef);
         edgesStream.close();

        int edgesLoaded = 0;
        for (Map<String, Object> edgeData : edgeDataList) {
            String nodeAName = (String) edgeData.get("nodeA");
            String nodeBName = (String) edgeData.get("nodeB");
            // Jackson might parse numbers as Integer or Double
            double distance = ((Number) edgeData.get("distance")).doubleValue();

            Node nodeA = nodesByName.get(nodeAName);
            Node nodeB = nodesByName.get(nodeBName);

            if (nodeA == null) {
                System.err.println("Warning: Edge references unknown node '" + nodeAName + "'. Skipping edge.");
                continue;
            }
            if (nodeB == null) {
                 System.err.println("Warning: Edge references unknown node '" + nodeBName + "'. Skipping edge.");
                 continue;
            }

            // Create the edge and add it to the adjacency list for the source node
            Edge edge = new Edge(nodeA, nodeB, distance);
            adjacencyList.get(nodeA).add(edge);
            edgesLoaded++;
        }
         System.out.println("Loaded " + edgesLoaded + " directed edges.");
    }

    // Getters
    public Collection<Node> getNodes() {
        return nodesByName.values();
    }

    public Collection<Edge> getEdges() {
        List<Edge> allEdges = new ArrayList<>();
        for (List<Edge> edges : adjacencyList.values()) {
            allEdges.addAll(edges);
        }
        return allEdges;
    }

    public List<Edge> getEdgesFromNode(Node node) {
        return adjacencyList.getOrDefault(node, Collections.emptyList());
    }

     public Node getNodeByName(String name) {
         return nodesByName.get(name);
     }

     // Optional: Find node closest to screen click (basic implementation)
     public Node findNodeClosestToScreenXY(double screenX, double screenY) {
         Node closest = null;
         double minDistSq = Double.POSITIVE_INFINITY;

         for (Node node : nodesByName.values()) {
             double dx = node.getScreenX() - screenX;
             double dy = node.getScreenY() - screenY;
             double distSq = dx * dx + dy * dy;
             if (distSq < minDistSq) {
                 minDistSq = distSq;
                 closest = node;
             }
         }
         // Define a threshold - only return if click is reasonably close
         double clickRadiusSq = 20.0 * 20.0; // Within 20 pixels
         return (minDistSq <= clickRadiusSq) ? closest : null;
     }
}