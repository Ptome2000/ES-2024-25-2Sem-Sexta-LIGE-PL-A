package DetectAdjacentProperties;

import Models.PropertyPolygon;
import Models.VertexCoordinate;

import java.util.List;

/**
 * @deprecated This class is deprecated and will be removed in a future release.
 * Use {@link CoordinateFinder#findMinCoordinates(List)} instead.
 * This class provides methods to find the minimum coordinates (X and Y) from a list of PropertyPolygon objects.
 * It iterates through the vertices of each property polygon and identifies the lowest X and Y values.
 */
@Deprecated
public class MinCoordinateFinder {

    /**
     * Finds the minimum X and Y coordinates from a list of PropertyPolygon objects.
     * It iterates over all the vertices of all the properties to determine the lowest X and Y values.
     *
     * @param properties A list of PropertyPolygon objects representing the properties.
     * @return An array containing the minimum X and Y coordinates, where the first element is minX and the second is minY.
     * @deprecated Use {@link CoordinateFinder#findMinCoordinates(List)} instead.
     */
    @Deprecated
    public static double[] findMinCoordinates(List<PropertyPolygon> properties) {
        double minX = Double.MAX_VALUE;
        double minY = Double.MAX_VALUE;

        // Iterate through all properties and their vertices to find the minimum X and Y values

        for (PropertyPolygon property : properties) {
            for (VertexCoordinate vertex : property.getPolygon().getVertices()) {
                if (vertex.x() < minX) {
                    minX = vertex.x();
                }
                if (vertex.y() < minY) {
                    minY = vertex.y();
                }
            }
        }

        // Log the minimum X and Y values
        System.out.println("Min X = " + minX + " Min Y = " + minY);

        // Return both values as an array: [minX, minY]
        return new double[] { minX, minY };

    }
}