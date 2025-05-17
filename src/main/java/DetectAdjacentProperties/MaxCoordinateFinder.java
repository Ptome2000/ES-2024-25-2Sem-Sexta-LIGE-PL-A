package DetectAdjacentProperties;

import Models.PropertyPolygon;
import Models.VertexCoordinate;

import java.util.List;

/**
 * @deprecated This class is deprecated and will be removed in a future release.
 * Use {@link CoordinateFinder#findMaxCoordinates(List)} instead.
 * This class provides methods to find the maximum coordinates (X and Y) from a list of PropertyPolygon objects.
 * It iterates through the vertices of each property polygon and identifies the highest X and Y values.
 */
@Deprecated
public class MaxCoordinateFinder {

    /**
     * Finds the maximum X and Y coordinates from a list of PropertyPolygon objects.
     * It iterates over all the vertices of all the properties to determine the highest X and Y values.
     *
     * @param properties A list of PropertyPolygon objects representing the properties.
     * @return An array containing the maximum X and Y coordinates, where the first element is maxX and the second is maxY.
     * @deprecated Use {@link CoordinateFinder#findMaxCoordinates(List)} instead.
     */
    @Deprecated
    public static double[] findMaxCoordinates(List<PropertyPolygon> properties) {
        double maxX = Double.MIN_VALUE;
        double maxY = Double.MIN_VALUE;

        // Iterate through all properties and their vertices to find the maximum X and Y values
        for (PropertyPolygon property : properties) {
            for (VertexCoordinate vertex : property.getPolygon().getVertices()) {
                if (vertex.x() > maxX) {
                    maxX = vertex.x();
                }

                if (vertex.y() > maxY) {
                    maxY = vertex.y();
                }
            }
        }

        // Log the maximum X and Y values
        System.out.println("Max X = " + maxX + " Max Y = " + maxY);

        // Return both values as an array: [maxX, maxY]
        return new double[] { maxX, maxY };

    }
}