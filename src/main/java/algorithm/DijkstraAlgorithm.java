package algorithm;

import model.Edge;
import model.Graph;
import model.Node;

import java.util.*;

// Time Complexity:
// - Initialization of distances and previousNodes: O(V), where V is the number of nodes.
// - Priority queue operations (add/remove/update): O((V + E) log V), where E is the number of edges.
// - Overall: O((V + E) log V).

// Space Complexity:
// - Graph representation (adjacency list): O(V + E).
// - Auxiliary data structures (distances, previousNodes, priority queue, etc.): O(V).
// - Simulation steps storage: O(E).
// - Overall: O(V + E).

public class DijkstraAlgorithm {

    private final Graph graph;

    public DijkstraAlgorithm(Graph graph) {
        this.graph = graph;
    }

    // Simulation Step Event Handling 
    public interface StepEvent {} 

    public record NodeVisitedEvent(Node node, double distance) implements StepEvent {} // orange - node is removed from priority queue and visited
    public record EdgeRelaxedEvent(Edge edge, Node neighbor, double newDistance, boolean updated) implements StepEvent {} 
    public record QueueUpdateEvent(Node node, double distance) implements StepEvent {} // lgiht blue - node in queue - added to the priority queue OR its distance is updated while it's already in the queue

    public static class PathResult {
        private final List<Node> path;
        private final double totalDistance;
        private final boolean reachable; 

        public PathResult(List<Node> path, double totalDistance, boolean reachable) {
            this.path = Collections.unmodifiableList(path);
            this.totalDistance = totalDistance;
            this.reachable = reachable;
        }

        public List<Node> getPath() { return path; }
        public double getTotalDistance() { return totalDistance; }
        public boolean isReachable() { return reachable; }

        // Returns a string representation of the path and distance
        @Override
        public String toString() {
            if (!reachable || path.isEmpty()) {
                return "End node is not reachable from the start node.";
            }

            StringBuilder sb = new StringBuilder();
            double distanceMeters = this.totalDistance;
            double distanceKilometers = distanceMeters / 1000.0; 
            sb.append(String.format("Total Distance: %.2f meters (%.3f km)\n",
                                      distanceMeters, distanceKilometers)); 

            sb.append("Path Sequence (" + path.size() + " nodes):\n");
            for (int i = 0; i < path.size(); i++) {
                Node node = path.get(i);
                sb.append(String.format("%d. %s", i + 1, node.getName()));
                if (i < path.size() - 1) {
                    sb.append(" -> ");
                }
            }
             sb.append("\n");
            return sb.toString();
        }
    }

    public record SimulationResult(PathResult finalPathResult, List<StepEvent> steps) {}

    // Dijkstra's algorithm with simulation steps
    public SimulationResult findShortestPathWithSimulation(Node start, Node end) {
        Map<Node, Double> distances = new HashMap<>(); 
        Map<Node, Node> previousNodes = new HashMap<>();
        
        Set<Node> nodesInQueue = new HashSet<>();
        PriorityQueue<Node> queue = new PriorityQueue<>(
            Comparator.comparingDouble(distances::get)
        );
        List<StepEvent> simulationSteps = new ArrayList<>();


        for (Node node : graph.getNodes()) {
            distances.put(node, Double.POSITIVE_INFINITY);
            previousNodes.put(node, null);
        }

        if (start == null) {
             System.err.println("Start node is null!");
             return new SimulationResult(new PathResult(Collections.emptyList(), Double.POSITIVE_INFINITY, false), simulationSteps);
        }

        distances.put(start, 0.0);
        queue.add(start);
        nodesInQueue.add(start);
        simulationSteps.add(new QueueUpdateEvent(start, 0.0));

        // Main loop of Dijkstra's algorithm
        while (!queue.isEmpty()) {
            Node current = queue.poll();
            nodesInQueue.remove(current); 
            simulationSteps.add(new NodeVisitedEvent(current, distances.get(current)));

        
            if (current.equals(end)) {
                break; 
            }

            if (Double.isInfinite(distances.get(current))) {
                break;
            }

            for (Edge edge : graph.getEdgesFromNode(current)) {
                Node neighbor = edge.getDestination();
                double newDist = distances.get(current) + edge.getDistance();
                boolean updated = false;

                if (newDist < distances.get(neighbor)) {
                    distances.put(neighbor, newDist);
                    previousNodes.put(neighbor, current);
                    updated = true;
                   
                    if (nodesInQueue.contains(neighbor)) {
                         queue.remove(neighbor); 
                    }
                    queue.add(neighbor);
                    nodesInQueue.add(neighbor);
                    simulationSteps.add(new QueueUpdateEvent(neighbor, newDist)); 
                }
                 simulationSteps.add(new EdgeRelaxedEvent(edge, neighbor, newDist, updated));
            }
        }

        // Build path
        List<Node> path = new ArrayList<>();
        boolean reachable = distances.get(end) != Double.POSITIVE_INFINITY;
        if (reachable) {
             for (Node at = end; at != null; at = previousNodes.get(at)) {
                 path.add(at);
             }
             Collections.reverse(path);
             
             if (path.isEmpty() && start.equals(end)) {
                 path.add(start);
             } else if (!path.isEmpty() && !path.get(0).equals(start)) {
                 
                  System.err.println("Path reconstruction error: Path doesn't start with the start node.");
                  
                   reachable = false;
                   path.clear();
             }
        }

        PathResult finalResult = new PathResult(path, distances.get(end), reachable);
        return new SimulationResult(finalResult, simulationSteps);
    }
}