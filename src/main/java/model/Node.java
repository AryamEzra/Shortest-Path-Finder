package model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Objects;

public class Node {
    private final String name;
    private final double latitude;
    private final double longitude;
    private final double screenX; // Renamed for clarity
    private final double screenY; // Renamed for clarity

    @JsonCreator // Helps Jackson use this constructor
    public Node(@JsonProperty("name") String name,
                @JsonProperty("latitude") double latitude,
                @JsonProperty("longitude") double longitude,
                @JsonProperty("screenX") double screenX,
                @JsonProperty("screenY") double screenY) {
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
        this.screenX = screenX;
        this.screenY = screenY;
    }

    // Getters
    public String getName() { return name; }
    public double getLatitude() { return latitude; }
    public double getLongitude() { return longitude; }
    public double getScreenX() { return screenX; }
    public double getScreenY() { return screenY; }

    // We no longer calculate distance based on screen coordinates here

    @Override
    public String toString() {
        return name + " (" + String.format("%.4f", latitude) + ", " + String.format("%.4f", longitude) + ")";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Node node = (Node) o;
        // Unique name is sufficient for equality in this context
        return Objects.equals(name, node.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}