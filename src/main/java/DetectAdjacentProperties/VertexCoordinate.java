package DetectAdjacentProperties;

import java.util.Objects;


/**
 * This class represents a coordinate of a vertex with X and Y values.
 * It is used to store the coordinates of a point in a polygon representing a property.
 */

public class VertexCoordinate {
    private double x;
    private double y;

    /**
     * Constructor to initialize the vertex coordinates with X and Y values.
     *
     * @param x The X coordinate of the vertex.
     * @param y The Y coordinate of the vertex.
     */

    public VertexCoordinate(double x, double y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Gets the X coordinate of the vertex.
     *
     * @return The X coordinate of the vertex.
     */

    public double getX() {
        return x;
    }

    /**
     * Gets the Y coordinate of the vertex.
     *
     * @return The Y coordinate of the vertex.
     */
    public double getY() {
        return y;
    }

    /**
     * Compares this VertexCoordinate to the specified object.
     *
     * @param o the object to compare this VertexCoordinate against
     * @return true if the given object represents a VertexCoordinate with the same coordinates, false otherwise
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof VertexCoordinate)) return false;
        VertexCoordinate that = (VertexCoordinate) o;
        return Math.abs(that.x - x) < 0.0001 && Math.abs(that.y - y) < 0.0001;
    }

    /**
     * Returns a hash code value for this VertexCoordinate.
     *
     * @return a hash code value for this VertexCoordinate
     */
    @Override
    public int hashCode() {
        return Objects.hash((int)(x * 1000), (int)(y * 1000));
    }

    /**
     * Returns a string representation of the vertex as (X, Y).
     *
     * @return A string in the format (X, Y) representing the coordinates of the vertex.
     */
    @Override
    public String toString() {
        return "(" + x + ", " + y + ")";
    }
}