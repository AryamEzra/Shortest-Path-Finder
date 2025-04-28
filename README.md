# Addis Ababa Shortest Path Finder (Dijkstra Simulation)

This project is a desktop application built with JavaFX that finds and visualizes the shortest path between two locations within a simplified graph representation of Addis Ababa, Ethiopia. It uses Dijkstra's algorithm and provides a step-by-step visual simulation of the algorithm's execution. The application runs entirely offline, loading graph data from local JSON files.

![Screenshot/GIF Placeholder](placeholder.png)
*(Replace placeholder.png with an actual screenshot or GIF of your application running)*

## Features

*   Loads a graph representing locations (nodes) and connections (edges) in Addis Ababa from local JSON files.
*   Displays the graph visually using JavaFX shapes (circles for nodes, lines for edges).
*   Allows users to select a start node (turns green) and an end node (turns red) by clicking on them.
*   Displays node names via tooltips on hover.
*   Calculates the shortest path using Dijkstra's algorithm based on pre-defined distances (in meters).
*   Visually simulates the steps of Dijkstra's algorithm:
    *   Nodes added to the priority queue turn light blue.
    *   Nodes whose shortest path is finalized turn orange.
*   Highlights the calculated shortest path on the map with red lines.
*   Displays the total distance of the shortest path in meters and kilometers (e.g., "1500 m (1.50 km)").
*   Provides status updates (e.g., "Select start node", "Simulating...", "Simulation finished").
*   Includes a "Reset" button to clear selections and the path visualization.
*   Handles cases where the end node is not reachable.

## Technology Stack

*   **Language:** Java (Requires JDK 21 or later)
*   **Framework:** JavaFX (Version 21 or later)
*   **Build Tool:** Apache Maven (Configuration via `pom.xml`)
*   **Libraries:**
    *   Jackson Databind: For parsing graph data from JSON files.

## Setup and Installation

1.  **Prerequisites:**
    *   Java Development Kit (JDK) version 21 or higher installed and configured (with `JAVA_HOME` environment variable set).
    *   Apache Maven installed and configured (with `MAVEN_HOME` environment variable set and `%MAVEN_HOME%\bin` added to your system's PATH).
2.  **Clone the Repository:**
    ```bash
    git clone <your-repository-url>
    cd Shortest-Path-Finder
    ```
3.  **Build the Project:** Open a terminal or command prompt in the project's root directory (where `pom.xml` is located) and run the Maven install command. This will download dependencies (like JavaFX and Jackson) and compile the code.
    ```bash
    mvn clean install
    ```
4.  **Data Files:** The graph data is located in `src/main/resources/data/`:
    *   `nodes.json`: Defines the locations, their Lat/Lon (for reference), and their screen coordinates for drawing.
    *   `edges.json`: Defines the connections between nodes and their distances in meters.

## Usage

1.  **Run the Application:**
    *   **Recommended:** Using the `javafx-maven-plugin` (ensure it's configured in `pom.xml`):
        ```bash
        mvn javafx:run
        ```
    *   **Alternative (from IDE):** Open the project in an IDE (like IntelliJ IDEA, Eclipse, VS Code with Java extensions) that supports Maven. Run the `main.Main` class directly.

2.  **Interact with the Map:**
    *   The application window will open, displaying the graph and a control panel.
    *   Hover over blue nodes to see their names in tooltips.
    *   Click a blue node to select it as the **start point** (it will turn green).
    *   Click a different blue node to select it as the **end point** (it will turn dark red).
    *   The application will automatically start simulating Dijkstra's algorithm. Watch the node colors change (light blue for nodes in the queue, orange for finalized nodes).
    *   Once the simulation finishes, the shortest path will be highlighted with thick red lines.
    *   The total distance (in meters and kilometers) will be displayed in the control panel.
    *   Click the "Reset Selection" button to clear the current path and selections and start over.

## Algorithm Details

*   The core pathfinding uses **Dijkstra's algorithm** implemented in `algorithm.DijkstraAlgorithm.java`.
*   The algorithm finds the shortest path in a weighted, directed graph where edge weights represent distance in meters.
*   **Time Complexity:** O((V + E) log V) using a priority queue.
*   **Space Complexity:** O(V + E) for graph storage and algorithm data structures.
*   The simulation visually represents the algorithm's state changes:
    *   **Light Blue:** Node added/updated in the priority queue.
    *   **Orange:** Node finalized (shortest path found *to* this node).



