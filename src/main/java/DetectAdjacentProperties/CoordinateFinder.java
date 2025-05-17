package DetectAdjacentProperties;

import Models.PropertyPolygon;
import Models.VertexCoordinate;
import Utils.Annotations.CyclomaticComplexity;
import Utils.Annotations.Layer;
import Utils.Enums.LayerType;

import java.util.List;
import java.util.function.Consumer;


/**
 * The {@code CoordinateFinder} class provides utility methods to find the maximum and minimum
 * X and Y coordinates from a list of {@link PropertyPolygon} objects. It processes the vertices
 * of the polygons to determine these values.
 */
@Layer(LayerType.BACK_END)
public class CoordinateFinder {

    /**
     * Finds the maximum X and Y coordinates from a list of PropertyPolygon objects.
     * It iterates over all the vertices of all the properties to determine the highest X and Y values.
     *
     * @param properties A list of PropertyPolygon objects representing the properties.
     * @return An array containing the maximum X and Y coordinates, where the first element is maxX and the second is maxY.
     */
    @CyclomaticComplexity(2)
    public static double[] findMaxCoordinates(List<PropertyPolygon> properties) {
        if (isNullOrEmpty(properties)) {
            return new double[] { Double.NaN, Double.NaN };
        }

        final double[] max = { Double.NEGATIVE_INFINITY, Double.NEGATIVE_INFINITY };
        forEachVertex(properties, vertex -> {
            max[0] = Math.max(max[0], vertex.x());
            max[1] = Math.max(max[1], vertex.y());
        });

        return max;
    }

    /**
     * Finds the minimum X and Y coordinates from a list of PropertyPolygon objects.
     * It iterates over all the vertices of all the properties to determine the lowest X and Y values.
     *
     * @param properties A list of PropertyPolygon objects representing the properties.
     * @return An array containing the minimum X and Y coordinates, where the first element is minX and the second is minY.
     */
    @CyclomaticComplexity(2)
    public static double[] findMinCoordinates(List<PropertyPolygon> properties) {
        if (isNullOrEmpty(properties)) {
            return new double[] { Double.NaN, Double.NaN };
        }

        final double[] min = { Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY };
        forEachVertex(properties, vertex -> {
            min[0] = Math.min(min[0], vertex.x());
            min[1] = Math.min(min[1], vertex.y());
        });

        return min;
    }

    /**
     * Checks if the given list of properties is null or empty.
     *
     * @param properties the list of PropertyPolygon objects to check
     * @return true if the list is null or empty, false otherwise
     */
    @CyclomaticComplexity(2)
    private static boolean isNullOrEmpty(List<PropertyPolygon> properties) {
        return properties == null || properties.isEmpty();
    }

    /**
     * Iterates over all vertices of the given property polygons and applies the specified action.
     * Skips null properties, polygons, or vertices.
     *
     * @param properties the list of property polygons
     * @param action the action to perform on each vertex
     */
    @CyclomaticComplexity(7)
    private static void forEachVertex(List<PropertyPolygon> properties, Consumer<VertexCoordinate> action) {
        for (PropertyPolygon property : properties) {
            if (property == null || property.getPolygon() == null) continue;
            List<VertexCoordinate> vertices = property.getPolygon().getVertices();
            if (vertices == null) continue;
            for (VertexCoordinate vertex : vertices) {
                if (vertex == null) continue;
                action.accept(vertex);
            }
        }
    }

}