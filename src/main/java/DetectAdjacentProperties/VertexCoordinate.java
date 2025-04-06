package DetectAdjacentProperties;

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
     * Returns a string representation of the vertex as (X, Y).
     *
     * @return A string in the format (X, Y) representing the coordinates of the vertex.
     */
    @Override
    public String toString() {
        return "(" + x + ", " + y + ")";
    }
}