package BuildPropertyGraph;

import DetectAdjacentProperties.PropertyPolygon;
import io.qameta.allure.*;
import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultEdge;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * This class contains unit tests for the {@link PropertyGraphBuilder} class.
 * It verifies the functionality of various methods in the PropertyGraphBuilder class,
 * ensuring that they behave as expected under different scenarios.
 *
 *
 * <p><strong>Author:</strong> Ptome2000</p>
 * <p><strong>Date:</strong> 09/04/2025</p>
 *
 * <p><strong>Cyclomatic Complexity:</strong></p>
 * <ul>
 *     <li>buildGraph: 3</li>
 *     <li>printGraph: 1</li>
 *     <li>exportGraphToDot: 1</li>
 *     <li>convertToGraphStream: 3</li>
 * </ul>
 */

@Feature("Build property graph")
class PropertyGraphBuilderTests {

    @Test
    @DisplayName("Build graph with an empty list of properties")
    @Description("Verifies that the graph is empty when no properties are provided.")
    @Severity(SeverityLevel.MINOR)
    void buildGraph1() {
        List<PropertyPolygon> properties = new ArrayList<>();
        Graph<PropertyPolygon, DefaultEdge> graph = PropertyGraphBuilder.buildGraph(properties);

        assertNotNull(graph, "Graph should not be null.");
        assertTrue(graph.vertexSet().isEmpty(), "Graph should have no vertices.");
        assertTrue(graph.edgeSet().isEmpty(), "Graph should have no edges.");
    }

    @Test
    @DisplayName("Build graph with two non-adjacent properties")
    @Description("Verifies that the graph contains vertices but no edges when properties are not adjacent.")
    @Severity(SeverityLevel.NORMAL)
    void buildGraph2() {
        List<PropertyPolygon> properties = TestUtils.createNonAdjacentProperties();
        Graph<PropertyPolygon, DefaultEdge> graph = PropertyGraphBuilder.buildGraph(properties);

        assertEquals(2, graph.vertexSet().size(), "Graph should have exactly 2 vertices.");
        assertTrue(graph.edgeSet().isEmpty(), "Graph should have no edges.");
    }

    @Test
    @DisplayName("Build graph with two adjacent properties")
    @Description("Verifies that the graph contains vertices and edges when properties are adjacent.")
    @Severity(SeverityLevel.CRITICAL)
    void buildGraph3() {
        List<PropertyPolygon> properties = TestUtils.createAdjacentProperties();
        Graph<PropertyPolygon, DefaultEdge> graph = PropertyGraphBuilder.buildGraph(properties);

        assertEquals(2, graph.vertexSet().size(), "Graph should have exactly 2 vertices.");
        assertEquals(1, graph.edgeSet().size(), "Graph should have exactly 1 edge.");
    }

    @Test
    @DisplayName("Print graph details")
    @Description("Verifies that the graph details are printed correctly.")
    @Severity(SeverityLevel.TRIVIAL)
    void printGraph() {
        Graph<PropertyPolygon, DefaultEdge> graph = TestUtils.createSampleGraph();
        PropertyGraphBuilder.printGraph(graph);
    }

    @Test
    @DisplayName("Export graph to DOT format")
    @Description("Verifies that the graph is exported to DOT format without errors.")
    @Severity(SeverityLevel.NORMAL)
    void exportGraphToDot() {
        Graph<PropertyPolygon, DefaultEdge> graph = TestUtils.createSampleGraph();
        assertDoesNotThrow(() -> PropertyGraphBuilder.exportGraphToDot(graph), "Exporting graph to DOT should not throw an exception.");
    }

    @Test
    @DisplayName("Convert an empty graph to GraphStream")
    @Description("Verifies that an empty graph is correctly converted to a GraphStream graph.")
    @Severity(SeverityLevel.MINOR)
    void convertToGraphStream1() {
        Graph<PropertyPolygon, DefaultEdge> graph = TestUtils.createEmptyGraph();
        org.graphstream.graph.Graph gsGraph = PropertyGraphBuilder.convertToGraphStream(graph);

        assertNotNull(gsGraph, "GraphStream graph should not be null.");
        assertEquals(0, gsGraph.getNodeCount(), "GraphStream graph should have no nodes.");
        assertEquals(0, gsGraph.getEdgeCount(), "GraphStream graph should have no edges.");
    }

    @Test
    @DisplayName("Convert a single property graph to GraphStream")
    @Description("Verifies that a graph with one property and no edges is correctly converted to a GraphStream graph.")
    @Severity(SeverityLevel.NORMAL)
    void convertToGraphStream2() {
        Graph<PropertyPolygon, DefaultEdge> graph = TestUtils.createSinglePropertyGraph();
        org.graphstream.graph.Graph gsGraph = PropertyGraphBuilder.convertToGraphStream(graph);

        assertEquals(1, gsGraph.getNodeCount(), "GraphStream graph should have exactly 1 node.");
        assertEquals(0, gsGraph.getEdgeCount(), "GraphStream graph should have no edges.");
    }

    @Test
    @DisplayName("Convert a graph with adjacent properties to GraphStream")
    @Description("Verifies that a graph with two properties and one edge is correctly converted to a GraphStream graph.")
    @Severity(SeverityLevel.CRITICAL)
    void convertToGraphStream3() {
        Graph<PropertyPolygon, DefaultEdge> graph = TestUtils.createSampleGraph();
        org.graphstream.graph.Graph gsGraph = PropertyGraphBuilder.convertToGraphStream(graph);

        assertEquals(2, gsGraph.getNodeCount(), "GraphStream graph should have exactly 2 nodes.");
        assertEquals(1, gsGraph.getEdgeCount(), "GraphStream graph should have exactly 1 edge.");
    }
}