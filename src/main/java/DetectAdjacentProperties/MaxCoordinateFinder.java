package DetectAdjacentProperties;

import java.util.List;

/**
 * This class is responsible for finding the maximum coordinates (X and Y) from a list of PropertyPolygon objects.
 * It iterates through the vertices of each property polygon and identifies the highest X and Y values.
 */
public class MaxCoordinateFinder {

    /**
     * Finds the maximum X and Y coordinates from a list of PropertyPolygon objects.
     * It iterates over all the vertices of all the properties to determine the highest X and Y values.
     *
     * @param properties A list of PropertyPolygon objects representing the properties.
     * @return An array containing the maximum X and Y coordinates, where the first element is maxX and the second is maxY.
     */
    public static double[] findMaxCoordinates(List<PropertyPolygon> properties) {
        double maxX = Double.MIN_VALUE;
        double maxY = Double.MIN_VALUE;

        // Iterate through all properties and their vertices to find the maximum X and Y values
        for (PropertyPolygon property : properties) {
            for (VertexCoordinate vertex : property.getPolygon().getCoordenadas()) {
                if (vertex.getX() > maxX) {
                    maxX = vertex.getX();
                }
                if (vertex.getY() > maxY) {
                    maxY = vertex.getY();
                }
            }
        }

        // Log the maximum X and Y values
        System.out.println("Max X = " + maxX + " Max Y = " + maxY);

        // Return both values as an array: [maxX, maxY]
        return new double[] { maxX, maxY };
    }
}