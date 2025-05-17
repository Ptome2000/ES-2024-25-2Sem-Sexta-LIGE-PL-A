package Models;

import Utils.Annotations.CyclomaticComplexity;
import Utils.Annotations.Layer;
import Utils.Enums.LayerType;
import java.util.List;

/**
 * The {@code Polygon} class represents a geometric shape defined by a list of vertices.
 * It provides methods to access and manipulate the vertices of the polygon.
 */
@Layer(LayerType.BACK_END)
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
    @CyclomaticComplexity(1)
    public List<VertexCoordinate> getVertices() {
        return vertices;
    }

    /**
     * Returns a string representation of the polygon as a list of its coordinates.
     *
     * @return A string representing the polygon, formatted as a list of coordinates.
     */
    @Override
    @CyclomaticComplexity(2)
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (VertexCoordinate c : vertices) {
            sb.append(c.toString()).append(" ");
        }
        return sb.toString();
    }

}

