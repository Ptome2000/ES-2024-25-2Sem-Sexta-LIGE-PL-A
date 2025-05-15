package Services;

import Models.Polygon;
import Models.PropertyPolygon;
import Models.VertexCoordinate;
import Utils.MockedPropertyPolygon;
import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleGraph;

import java.util.ArrayList;
import java.util.List;

/**
 * Utility class providing helper methods for creating test data and graphs
 * used in unit tests for the {@link Services} package.
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
        PropertyPolygon property2 = new MockedPropertyPolygon(2, createPolygon(new double[][]{{2, 2}, {3, 2}, {3, 3}, {2, 3}}));
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
     * Creates a sample graph with two properties and one edge.
     *
     * @return A {@link Graph} containing two vertices and one edge.
     */
    public static Graph<PropertyPolygon, DefaultEdge> createSampleGraph() {
        Graph<PropertyPolygon, DefaultEdge> graph = new SimpleGraph<>(DefaultEdge.class);
        PropertyPolygon property1 = new MockedPropertyPolygon(1, createPolygon(new double[][]{{0, 0}, {1, 0}, {1, 1}, {0, 1}}));
        PropertyPolygon property2 = new MockedPropertyPolygon(2, createPolygon(new double[][]{{1, 0}, {2, 0}, {2, 1}, {1, 1}}));
        graph.addVertex(property1);
        graph.addVertex(property2);
        graph.addEdge(property1, property2);
        return graph;
    }

    /**
     * Creates an empty graph.
     *
     * @return An empty {@link Graph} with no vertices or edges.
     */
    public static Graph<PropertyPolygon, DefaultEdge> createEmptyGraph() {
        return new SimpleGraph<>(DefaultEdge.class);
    }

    /**
     * Creates a graph with a single property and no edges.
     *
     * @return A {@link Graph} containing one vertex and no edges.
     */
    public static Graph<PropertyPolygon, DefaultEdge> createSinglePropertyGraph() {
        Graph<PropertyPolygon, DefaultEdge> graph = new SimpleGraph<>(DefaultEdge.class);
        PropertyPolygon property = new MockedPropertyPolygon(1, createPolygon(new double[][]{{0, 0}, {1, 0}, {1, 1}, {0, 1}}));
        graph.addVertex(property);
        return graph;
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
}
