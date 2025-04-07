package DetectAdjacentProperties;

import java.util.Objects;

public class VertexCoordinate {
    private double x;
    private double y;

    public VertexCoordinate(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public double getX() {
        return x;
    }

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

    @Override
    public String toString() {
        return "(" + x + ", " + y + ")";
    }
}