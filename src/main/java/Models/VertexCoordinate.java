package Models;

import Utils.Annotations.CyclomaticComplexity;
import Utils.Annotations.Layer;
import Utils.Enums.LayerType;
import java.util.Objects;

/**
 * The {@code VertexCoordinate} record represents a vertex in a 2D space with X and Y coordinates.
 * It provides methods to access the X and Y coordinates, compare vertices, and generate a string
 * representation of the vertex.
 */
@Layer(LayerType.BACK_END)
public record VertexCoordinate(double x, double y) {
    /**
     * Constructor to initialize the vertex coordinates with X and Y values.
     *
     * @param x The X coordinate of the vertex.
     * @param y The Y coordinate of the vertex.
     */
    public VertexCoordinate {
    }

    /**
     * Gets the X coordinate of the vertex.
     *
     * @return The X coordinate of the vertex.
     */
    @Override
    @CyclomaticComplexity(1)
    public double x() {
        return x;
    }

    /**
     * Gets the Y coordinate of the vertex.
     *
     * @return The Y coordinate of the vertex.
     */
    @Override
    @CyclomaticComplexity(1)
    public double y() {
        return y;
    }

    /**
     * Compares this VertexCoordinate to the specified object.
     *
     * @param o the object to compare this VertexCoordinate against
     * @return true if the given object represents a VertexCoordinate with the same coordinates, false otherwise
     */
    @Override
    @CyclomaticComplexity(4)
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof VertexCoordinate that)) return false;
        return Math.abs(that.x - x) < 0.0001 && Math.abs(that.y - y) < 0.0001;
    }

    /**
     * Returns a hash code value for this VertexCoordinate.
     *
     * @return a hash code value for this VertexCoordinate
     */
    @Override
    @CyclomaticComplexity(1)
    public int hashCode() {
        return Objects.hash((int) (x * 1000), (int) (y * 1000));
    }

    /**
     * Returns a string representation of the vertex as (X, Y).
     *
     * @return A string in the format (X, Y) representing the coordinates of the vertex.
     */
    @Override
    @CyclomaticComplexity(1)
    public String toString() {
        return "(" + x + ", " + y + ")";
    }
}