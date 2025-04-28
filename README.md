# Addis Ababa Shortest Path Finder 🇪🇹📍

A JavaFX desktop application demonstrating Dijkstra's algorithm for finding the shortest path on a simplified map of Addis Ababa, featuring a step-by-step visualization. Runs entirely offline.

## 🎥 Demo Video

https://github.com/user-attachments/assets/b2c597de-59bb-4b77-af18-65f06792dc1c



---

## ✨ Features

*   🗺️ **Visual Graph:** Displays locations (nodes) and connections (edges).
*   🖱️ **Interactive Selection:** Click nodes to choose start (🟢) and end (🔴) points.
*   🏷️ **Tooltips:** Hover over nodes to see location names.
*   ⚙️ **Dijkstra Simulation:** Visualizes algorithm steps:
    *   🔵 Default Node
    *   🟡 Node Added to Queue (`NODE_IN_QUEUE_COLOR`) - *Note: Color might differ based on your code*
    *   🟠 Node Visited/Finalized (`NODE_VISITED_COLOR`)
*   **Path Highlighting:** Shows the shortest path in red (🔴).
*   📏 **Distance Display:** Shows path length in `meters (km)`.
*   🔄 **Reset:** Clears selection and visualization.
*   ⚠️ **Handles Unreachable Nodes.**

---

## 🛠️ Technology Stack

*   **Java:** JDK 21+ <img src="https://img.shields.io/badge/Java-21-blue.svg" height="13">
*   **UI:** JavaFX 21+ <img src="https://img.shields.io/badge/JavaFX-21-orange.svg" height="13">
*   **Build:** Apache Maven <img src="https://img.shields.io/badge/Maven-3.9+-red.svg" height="13">
*   **JSON Parsing:** Jackson Databind <img src="https://img.shields.io/maven-central/v/com.fasterxml.jackson.core/jackson-databind?label=Jackson&color=green" height="13">

---

## 🚀 Setup & Usage

1.  **Prerequisites:** JDK 21+, Apache Maven installed & configured.
2.  **Clone:** `git clone <your-repository-url>`
3.  **Build:** `cd <repo-folder>` then `mvn clean install`
4.  **Run:**
    *   Via Maven Plugin: `mvn javafx:run`
    *   Via IDE: Run `main.Main` class.
5.  **Interact:** Click start node (green), click end node (red) -> simulation runs -> path shown. Use Reset button.

---

## ⚙️ Algorithm

*   **Core:** Dijkstra's Algorithm
*   **Complexity:** O((V + E) log V) Time | O(V + E) Space

---
