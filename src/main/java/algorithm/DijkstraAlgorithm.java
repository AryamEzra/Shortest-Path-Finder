package algorithm;

import model.Edge;
import model.Graph;
import model.Node;

import java.util.*;

public class DijkstraAlgorithm {

    private final Graph graph;

    public DijkstraAlgorithm(Graph graph) {
        this.graph = graph;
    }

    // --- Simulation Step Event Handling ---
    public interface StepEvent {} // Marker interface

    public record NodeVisitedEvent(Node node, double distance) implements StepEvent {}
    public record EdgeRelaxedEvent(Edge edge, Node neighbor, double newDistance, boolean updated) implements StepEvent {} // Indicate if it was an improvement
    public record QueueUpdateEvent(Node node, double distance) implements StepEvent {} // When adding/updating node in queue

    // --- Path Result (remains the same structure) ---
    public static class PathResult {
        private final List<Node> path;
        private final double totalDistance;
        private final boolean reachable; // Added to indicate if end was found

        public PathResult(List<Node> path, double totalDistance, boolean reachable) {
            this.path = Collections.unmodifiableList(path);
            this.totalDistance = totalDistance;
            this.reachable = reachable;
        }

        public List<Node> getPath() { return path; }
        public double getTotalDistance() { return totalDistance; }
        public boolean isReachable() { return reachable; }

        @Override
        public String toString() {
            if (!reachable || path.isEmpty()) {
                return "End node is not reachable from the start node.";
            }

            StringBuilder sb = new StringBuilder();
            double distanceMeters = this.totalDistance;
            double distanceKilometers = distanceMeters / 1000.0; // Calculate kilometers
            sb.append(String.format("Total Distance: %.2f meters (%.3f km)\n",
                                      distanceMeters, distanceKilometers)); // Updated format (using %.3f for km for more precision here)
            // *********************

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

    // --- Result Containing Path and Simulation Steps ---
    public record SimulationResult(PathResult finalPathResult, List<StepEvent> steps) {}

    /**
     * Finds the shortest path and returns the result along with simulation steps.
     */
    public SimulationResult findShortestPathWithSimulation(Node start, Node end) {
        Map<Node, Double> distances = new HashMap<>();
        Map<Node, Node> previousNodes = new HashMap<>();
        // Store nodes currently in the queue for efficient updates check
        Set<Node> nodesInQueue = new HashSet<>();
        PriorityQueue<Node> queue = new PriorityQueue<>(
            Comparator.comparingDouble(distances::get)
        );
        List<StepEvent> simulationSteps = new ArrayList<>();

        // Initialize distances
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
        simulationSteps.add(new QueueUpdateEvent(start, 0.0)); // Initial queue add


        while (!queue.isEmpty()) {
            Node current = queue.poll();
            nodesInQueue.remove(current); // Remove from active queue set

            simulationSteps.add(new NodeVisitedEvent(current, distances.get(current)));

            // Optimization: If we extract the end node, we found the shortest path
            if (current.equals(end)) {
                break; // Found shortest path to end
            }

            // If current distance is infinity, remaining nodes are unreachable
            if (Double.isInfinite(distances.get(current))) {
                break; // All remaining nodes in queue are unreachable
            }


            for (Edge edge : graph.getEdgesFromNode(current)) {
                Node neighbor = edge.getDestination();
                double newDist = distances.get(current) + edge.getDistance();
                boolean updated = false;

                if (newDist < distances.get(neighbor)) {
                    distances.put(neighbor, newDist);
                    previousNodes.put(neighbor, current);
                    updated = true;
                    // If neighbor is already in queue, removing and re-adding updates its priority
                    // If not, just add it.
                    if (nodesInQueue.contains(neighbor)) {
                         queue.remove(neighbor); // O(log n) or O(n) depending on PQ implementation
                    }
                    queue.add(neighbor);
                    nodesInQueue.add(neighbor);
                    simulationSteps.add(new QueueUpdateEvent(neighbor, newDist)); // Queue updated/added
                }
                 // Add relaxation event regardless of update, indicating check happened
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
             // Ensure start node is in the path if reachable and start != end
             if (path.isEmpty() && start.equals(end)) {
                 path.add(start);
             } else if (!path.isEmpty() && !path.get(0).equals(start)) {
                 // This case indicates an issue, path should always start with 'start' if found
                  System.err.println("Path reconstruction error: Path doesn't start with the start node.");
                  // Attempt recovery or signal error more strongly
                  // For now, clear path if it's invalid
                   reachable = false;
                   path.clear();
             }
        }


        PathResult finalResult = new PathResult(path, distances.get(end), reachable);
        return new SimulationResult(finalResult, simulationSteps);
    }
}