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
    private final Map<String, Node> nodesByName = new HashMap<>();
    private final Map<Node, List<Edge>> adjacencyList = new HashMap<>();

    
    // nodesResourcePath Path to nodes JSON file within resources (/data/nodes.json)
    // edgesResourcePath Path to edges JSON file within resources (/data/edges.json)
    // IOException If files cannot be read or parsed

    public void loadFromJSONResources(String nodesResourcePath, String edgesResourcePath) throws IOException {
        ObjectMapper mapper = new ObjectMapper();

        // Load Nodes
        InputStream nodesStream = getClass().getResourceAsStream(nodesResourcePath);
        if (nodesStream == null) {
            throw new IOException("Cannot find nodes resource file: " + nodesResourcePath);
        }
        List<Node> nodes = mapper.readValue(nodesStream, new TypeReference<List<Node>>() {});
        nodesStream.close();

        // Store nodes and initialize adjacency list entries
        nodesByName.clear();
        adjacencyList.clear();
        for (Node node : nodes) {
            if (nodesByName.containsKey(node.getName())) {
                 System.err.println("Warning: Duplicate node name found '" + node.getName() + "'. Ignoring duplicate.");
                 continue; 
            }
            nodesByName.put(node.getName(), node);
            adjacencyList.put(node, new ArrayList<>()); // Initialize empty list for edges
        }

        // Load Edges 
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
}