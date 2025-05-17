package DetectAdjacentProperties;

import Models.Polygon;
import Models.PropertyPolygon;
import Models.VertexCoordinate;
import Utils.Mocks.MockedPropertyPolygon;

import java.util.ArrayList;
import java.util.List;

/**
 * Utility class providing helper methods for creating test data and graphs
 * used in unit tests for the {@link DetectAdjacentProperties} package.
 *
 * <p>This class includes methods to create sample properties, polygons, and graphs
 * for testing purposes. It is designed to simplify the setup of test cases
 * by providing reusable test data.</p>
 *
 * <p><strong>Author:</strong> Ptome2000</p>
 * <p><strong>Date:</strong> 09/04/2025</p>
 */
public class TestUtils {

    /**
     * Creates a list of two non-adjacent properties.
     *
     * @return A list of {@link PropertyPolygon} objects with no shared vertices.
     */
    public static List<PropertyPolygon> createNonAdjacentProperties() {
        PropertyPolygon property1 = new MockedPropertyPolygon(1, createPolygon(new double[][]{{0, 0}, {1, 0}, {1, 1}, {0, 1}}));
        PropertyPolygon property2 = new MockedPropertyPolygon(2, createPolygon(new double[][]{{3, 3}, {4, 3}, {4, 4}, {3, 4}}));
        List<PropertyPolygon> properties = new ArrayList<>();
        properties.add(property1);
        properties.add(property2);
        return properties;
    }

    /**
     * Creates a list of two adjacent properties.
     *
     * @return A list of {@link PropertyPolygon} objects with shared vertices.
     */
    public static List<PropertyPolygon> createAdjacentProperties() {
        PropertyPolygon property1 = new MockedPropertyPolygon(1, createPolygon(new double[][]{{0, 0}, {1, 0}, {1, 1}, {0, 1}}));
        PropertyPolygon property2 = new MockedPropertyPolygon(2, createPolygon(new double[][]{{1, 0}, {2, 0}, {2, 1}, {1, 1}}));
        List<PropertyPolygon> properties = new ArrayList<>();
        properties.add(property1);
        properties.add(property2);
        return properties;
    }

    /**
     * Creates a single property.
     *
     * @return A {@link PropertyPolygon} object representing a single property.
     */
    public static PropertyPolygon createSingleProperty() {
        return new MockedPropertyPolygon(1, createPolygon(new double[][]{{0, 0}, {1, 0}, {1, 1}, {0, 1}}));
    }

    /**
     * Helper method to create a polygon from an array of coordinates.
     *
     * @param coordinates A 2D array of doubles representing the vertices of the polygon.
     * @return A {@link Polygon} object created from the given coordinates.
     */
    private static Polygon createPolygon(double[][] coordinates) {
        List<VertexCoordinate> vertices = new ArrayList<>();
        for (double[] coord : coordinates) {
            vertices.add(new VertexCoordinate(coord[0], coord[1]));
        }
        return new Polygon(vertices);
    }

    /**
     * Creates a list of valid CSV data rows for testing.
     *
     * @return A list of string arrays, where each array represents a valid CSV row.
     */
    public static List<String[]> createValidCsvData() {
        List<String[]> data = new ArrayList<>();
        data.add(new String[]{"1", "0,0", "1,0", "1,1", "0,1"});
        data.add(new String[]{"2", "2,2", "3,2", "3,3", "2,3"});
        return data;
    }

    /**
     * Creates a list of mixed valid and invalid CSV data rows for testing.
     *
     * @return A list of string arrays, where some arrays represent valid CSV rows and others are invalid.
     */
    public static List<String[]> createInvalidCsvData() {
        List<String[]> data = new ArrayList<>();
        data.add(new String[]{"1", "0,0", "1,0", "1,1", "0,1"}); // Valid
        data.add(new String[]{"Invalid", "Invalid"}); // Invalid
        return data;
    }

    /**
     * Creates a list of properties with duplicate adjacency.
     *
     * @return A list of {@link PropertyPolygon} objects where some properties are duplicates.
     */
    public static List<PropertyPolygon> createDuplicateAdjacentProperties() {
        List<PropertyPolygon> properties = new ArrayList<>();
        properties.add(createPropertyWithVertices(0, 0, 1, 0, 1, 1, 0, 1));
        properties.add(createPropertyWithVertices(1, 1, 2, 1, 2, 2, 1, 2));
        properties.add(createPropertyWithVertices(1, 1, 2, 1, 2, 2, 1, 2)); // Duplicate
        return properties;
    }

    /**
     * Creates a list of properties for testing self-comparison.
     *
     * @return A list of {@link PropertyPolygon} objects containing a single property.
     */
    public static List<PropertyPolygon> createSelfComparisonProperties() {
        List<PropertyPolygon> properties = new ArrayList<>();
        properties.add(createPropertyWithVertices(0, 0, 1, 0, 1, 1, 0, 1));
        return properties;
    }

    /**
     * Creates a {@link PropertyPolygon} object with the given vertices.
     *
     * @param coordinates A variable number of doubles representing the vertices of the polygon.
     *                    Each pair of doubles represents the x and y coordinates of a vertex.
     * @return A {@link PropertyPolygon} object created from the given vertices.
     */
    public static PropertyPolygon createPropertyWithVertices(double... coordinates) {
        List<VertexCoordinate> vertices = new ArrayList<>();
        for (int i = 0; i < coordinates.length; i += 2) {
            vertices.add(new VertexCoordinate(coordinates[i], coordinates[i + 1]));
        }
        Polygon polygon = new Polygon(vertices);
        return new MockedPropertyPolygon(1, polygon);
    }
}