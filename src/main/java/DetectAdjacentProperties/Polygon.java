package DetectAdjacentProperties;

import java.util.List;

/**
 * This class represents a polygon with a list of vertex coordinates.
 * It is used to define the shape of a property, represented by its coordinates.
 */
public class Polygon {
    private List<VertexCoordinate> vertices;

    /**
     * Constructor to initialize the polygon with a list of coordinates.
     *
     * @param vertices A list of VertexCoordinate objects representing the vertices of the polygon.
     */
    public Polygon(List<VertexCoordinate> vertices) {
        this.vertices = vertices;
    }

    /**
     * Gets the list of coordinates (vertices) of the polygon.
     *
     * @return A list of VertexCoordinate objects representing the vertices of the polygon.
     */
    public List<VertexCoordinate> getVertices() {
        return vertices;
    }

    /**
     * Returns a string representation of the polygon as a list of its coordinates.
     *
     * @return A string representing the polygon, formatted as a list of coordinates.
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (VertexCoordinate c : vertices) {
            sb.append(c.toString()).append(" ");
        }
        return sb.toString();
    }

}

