package Services;

import static org.junit.jupiter.api.Assertions.*;

import Models.Polygon;
import Models.PropertyPolygon;
import Models.VertexCoordinate;
import Utils.Mocks.MockedPropertyPolygon;
import edu.uci.ics.jung.graph.Graph;
import io.qameta.allure.Description;
import io.qameta.allure.Feature;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import java.util.List;


/**
 * This class contains unit tests for the {@link PropertyGraphJungBuilder} class.
 * It validates the behavior of the PropertyGraphJungBuilder class, including
 * its ability to construct a graph from property polygons using the JUNG library.
 * Each test case is designed to cover specific functionality and edge cases,
 * ensuring the robustness of the PropertyGraphJungBuilder implementation.
 *
 * <p><strong>Author:</strong> Ptome2000</p>
 * <p><strong>Date:</strong> 17/05/2025</p>
 */
@Feature("Graph Construction")
@DisplayName("Graph Builder with Jung Tests")
class PropertyGraphJungBuilderTests {

    @Nested
    @DisplayName("Empty Graph Tests")
    class EmptyGraphTests {

        @Test
        @DisplayName("Should create an empty graph when no properties are provided")
        @Description("Ensures that the graph is empty when no properties are passed to the builder.")
        @Severity(SeverityLevel.NORMAL)
        void buildGraphWithNoProperties() {
            List<PropertyPolygon> properties = List.of();
            Graph<PropertyPolygon, String> graph = PropertyGraphJungBuilder.buildGraph(properties);

            assertNotNull(graph);
            assertTrue(graph.getVertices().isEmpty());
            assertTrue(graph.getEdges().isEmpty());
        }
    }

    @Nested
    @DisplayName("Single Property Graph Tests")
    class SinglePropertyGraphTests {

        @Test
        @DisplayName("Should create a graph with one vertex and no edges")
        @Description("Ensures that a single property results in a graph with one vertex and no edges.")
        @Severity(SeverityLevel.NORMAL)
        void buildGraphWithSingleProperty() {
            PropertyPolygon property = new MockedPropertyPolygon(1, "OwnerA", new Polygon(List.of()));

            List<PropertyPolygon> properties = List.of(property);
            Graph<PropertyPolygon, String> graph = PropertyGraphJungBuilder.buildGraph(properties);

            assertNotNull(graph);
            assertEquals(1, graph.getVertexCount());
            assertTrue(graph.getEdges().isEmpty());
        }
    }

    @Nested
    @DisplayName("Shared Vertices Graph Tests")
    class SharedVerticesGraphTests {

        @Test
        @DisplayName("Should create edges between properties sharing vertices")
        @Description("Ensures that properties sharing vertices are connected by edges in the graph.")
        @Severity(SeverityLevel.NORMAL)
        void buildGraphWithSharedVertices() {
            VertexCoordinate vertex1 = new VertexCoordinate(0.8, 0.6);
            VertexCoordinate vertex2 = new VertexCoordinate(0.7, 0.5);

            PropertyPolygon property1 = new MockedPropertyPolygon(1, "OwnerA", new Polygon(List.of(vertex1, vertex2)));
            PropertyPolygon property2 = new MockedPropertyPolygon(2, "OwnerB", new Polygon(List.of(vertex1)));
            PropertyPolygon property3 = new MockedPropertyPolygon(3, "OwnerC", new Polygon(List.of(vertex2)));

            List<PropertyPolygon> properties = List.of(property1, property2, property3);
            Graph<PropertyPolygon, String> graph = PropertyGraphJungBuilder.buildGraph(properties);

            assertNotNull(graph);
            assertEquals(3, graph.getVertexCount());
            assertEquals(2, graph.getEdgeCount());
            assertTrue(graph.isNeighbor(property1, property2));
            assertTrue(graph.isNeighbor(property1, property3));
            assertFalse(graph.isNeighbor(property2, property3));
        }
    }

    @Nested
    @DisplayName("No Shared Vertices Graph Tests")
    class NoSharedVerticesGraphTests {

        @Test
        @DisplayName("Should create a graph with no edges when properties do not share vertices")
        @Description("Ensures that properties with no shared vertices result in a graph with no edges.")
        @Severity(SeverityLevel.NORMAL)
        void buildGraphWithNoSharedVertices() {
            VertexCoordinate vertex1 = new VertexCoordinate(0.8, 0.6);
            VertexCoordinate vertex2 = new VertexCoordinate(0.7, 0.5);

            PropertyPolygon property1 = new MockedPropertyPolygon(1, "OwnerA", new Polygon(List.of(vertex1)));
            PropertyPolygon property2 = new MockedPropertyPolygon(2, "OwnerB", new Polygon(List.of(vertex2)));

            List<PropertyPolygon> properties = List.of(property1, property2);
            Graph<PropertyPolygon, String> graph = PropertyGraphJungBuilder.buildGraph(properties);

            assertNotNull(graph);
            assertEquals(2, graph.getVertexCount());
            assertTrue(graph.getEdges().isEmpty());
        }
    }

}