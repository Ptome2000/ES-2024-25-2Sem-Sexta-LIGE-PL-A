package DetectAdjacentProperties;

import java.util.List;

/**
 * This class provides methods to find the maximum and minimum coordinates in a list of properties.
 */
public class MinCoordinateFinder {

    /**
     * Finds the minimum X and Y coordinates among the given properties.
     *
     * @param properties A list of PropertyPolygon objects.
     */
    public static void findMinCoordinates(List<PropertyPolygon> properties) {
        double minX = Double.MAX_VALUE;
        double minY = Double.MAX_VALUE;

        // Loop through each property and each vertex to find the minimum coordinates
        for (PropertyPolygon property : properties) {
            for (VertexCoordinate vertex : property.getPolygon().getCoordenadas()) {
                if (vertex.getX() < minX) {
                    minX = vertex.getX();
                }
                if (vertex.getY() < minY) {
                    minY = vertex.getY();
                }
            }
        }

        // Print the minimum values for X and Y
        System.out.println("Minimum X Coordinate: " + minX);
        System.out.println("Minimum Y Coordinate: " + minY);
    }
}