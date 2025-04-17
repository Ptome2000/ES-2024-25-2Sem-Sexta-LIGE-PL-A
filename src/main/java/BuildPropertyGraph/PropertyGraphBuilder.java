/**
 * PropertyGraphBuilder is responsible for creating and manipulating a graph based on property polygons.
 * It provides methods to:
 * - Build a JGraphT graph where nodes represent properties and edges represent shared coordinates (adjacency)
 * - Export the graph to DOT format for textual visualization
 * - Convert the JGraphT graph to a GraphStream graph for graphical visualization
 */
package BuildPropertyGraph;

import DetectAdjacentProperties.PropertyPolygon;
import DetectAdjacentProperties.VertexCoordinate;
import org.graphstream.graph.Node;
import org.graphstream.graph.implementations.SingleGraph;
import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleGraph;
import org.jgrapht.nio.DefaultAttribute;
import org.jgrapht.nio.dot.DOTExporter;

import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class PropertyGraphBuilder {

    /**
     * Builds a graph where each node represents a property polygon and an edge connects
     * properties that share at least one vertex.
     *
     * @param properties List of PropertyPolygon objects
     * @return A graph with properties as vertices and adjacency as edges
     */
    public static Graph<PropertyPolygon, DefaultEdge> buildGraph(List<PropertyPolygon> properties) {
        Graph<PropertyPolygon, DefaultEdge> graph = new SimpleGraph<>(DefaultEdge.class);

        // Add all properties as vertices
        for (PropertyPolygon property : properties) {
            graph.addVertex(property);
        }

        // Map each vertex coordinate to the properties it belongs to
        Map<VertexCoordinate, List<PropertyPolygon>> vertexMap = new HashMap<>();
        for (PropertyPolygon property : properties) {
            for (VertexCoordinate vertex : property.getPolygon().getVertices()) {
                vertexMap.computeIfAbsent(vertex, k -> new ArrayList<>()).add(property);
            }
        }

        // Create edges between properties sharing at least one vertex
        for (List<PropertyPolygon> sharedProps : vertexMap.values()) {
            for (int i = 0; i < sharedProps.size(); i++) {
                for (int j = i + 1; j < sharedProps.size(); j++) {
                    PropertyPolygon p1 = sharedProps.get(i);
                    PropertyPolygon p2 = sharedProps.get(j);
                    if (!p1.equals(p2) && !graph.containsEdge(p1, p2)) {
                        graph.addEdge(p1, p2);
                        System.out.println("Added edge between " + p1.getObjectId() + " and " + p2.getObjectId());
                    }
                }
            }
        }

        return graph;
    }

    /**
     * Prints the number of vertices and edges, and lists all connections between properties.
     *
     * @param graph The graph to print
     */
    public static void printGraph(Graph<PropertyPolygon, DefaultEdge> graph) {
        System.out.println("NUMBER OF VERTICES: " + graph.vertexSet().size());
        System.out.println("NUMBER OF EDGES: " + graph.edgeSet().size());

        System.out.println("\nCONNECTIONS BETWEEN PROPERTIES:");
        for (DefaultEdge edge : graph.edgeSet()) {
            PropertyPolygon source = graph.getEdgeSource(edge);
            PropertyPolygon target = graph.getEdgeTarget(edge);
            System.out.println(" - " + source.getObjectId() + " <--> " + target.getObjectId());
        }
    }

    /**
     * Exports the graph to a DOT file for textual visualization.
     *
     * @param graph The graph to export
     */
    public static void exportGraphToDot(Graph<PropertyPolygon, DefaultEdge> graph) {
        DOTExporter<PropertyPolygon, DefaultEdge> exporter = new DOTExporter<>();
        exporter.setVertexAttributeProvider((PropertyPolygon p) -> {
            return Map.of("label", DefaultAttribute.createAttribute("ID: " + p.getObjectId()));
        });

        try (FileWriter writer = new FileWriter("graphCheck.dot", false)) {
            exporter.exportGraph(graph, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Converts a JGraphT graph into a GraphStream graph for GUI-based visualization.
     * Each polygon's vertices become nodes, and their edges are created accordingly.
     * Also adds simplified links between adjacent properties.
     *
     * @param jgraphtGraph The JGraphT graph to convert
     * @return The resulting GraphStream graph
     */
    public static org.graphstream.graph.Graph convertToGraphStream(Graph<PropertyPolygon, DefaultEdge> jgraphtGraph) {
        org.graphstream.graph.Graph gsGraph = new SingleGraph("PropertyGraph");

        // Styling and rendering hints
        gsGraph.setAttribute("ui.quality");
        gsGraph.setAttribute("ui.antialias");
        gsGraph.setAttribute("ui.stylesheet", """
            graph {
                padding: 50px;
            }
            node {
                fill-color: green;
                size: 2px;
                text-size: 0px;
                text-alignment: center;
            }
            node:hover {
                text-size: 10px;
                text-color: black;
            }
            edge {
                fill-color: gray;
                size: 0.3px;
            }
        """);

        // Add each coordinate of each property as a node
        for (PropertyPolygon p : jgraphtGraph.vertexSet()) {
            String id = String.valueOf(p.getObjectId());
            List<VertexCoordinate> coords = p.getPolygon().getVertices();

            for (int i = 0; i < coords.size(); i++) {
                VertexCoordinate v = coords.get(i);
                String vertexId = id + "_v" + i;
                if (gsGraph.getNode(vertexId) == null) {
                    Node vNode = gsGraph.addNode(vertexId);
                    vNode.setAttribute("xyz", v.getX(), v.getY(), 0);
                    vNode.setAttribute("ui.label", "");
                }
            }
        }

        // Add internal edges for each polygon
        for (PropertyPolygon p : jgraphtGraph.vertexSet()) {
            String id = String.valueOf(p.getObjectId());
            List<VertexCoordinate> coords = p.getPolygon().getVertices();

            for (int i = 0; i < coords.size(); i++) {
                String from = id + "_v" + i;
                String to = id + "_v" + ((i + 1) % coords.size());
                String edgeId = from + "_to_" + to;

                if (gsGraph.getNode(from) == null || gsGraph.getNode(to) == null) {
                    System.err.println("Nó inexistente: " + from + " ou " + to);
                    continue;
                }

                if (gsGraph.getEdge(edgeId) == null) {
                    try {
                        gsGraph.addEdge(edgeId, from, to);
                    } catch (Exception e) {
                        System.err.println("Erro ao adicionar aresta interna: " + edgeId + " -> " + e.getMessage());
                    }
                }
            }
        }

        // Add edges between polygons to represent adjacency
        for (DefaultEdge edge : jgraphtGraph.edgeSet()) {
            PropertyPolygon src = jgraphtGraph.getEdgeSource(edge);
            PropertyPolygon tgt = jgraphtGraph.getEdgeTarget(edge);
            String id1 = src.getObjectId() + "_v0";
            String id2 = tgt.getObjectId() + "_v0";
            String edgeId = src.getObjectId() + "-" + tgt.getObjectId();

            if (gsGraph.getNode(id1) != null && gsGraph.getNode(id2) != null) {
                try {
                    if (gsGraph.getEdge(edgeId) == null)
                        gsGraph.addEdge(edgeId, id1, id2);
                } catch (Exception e) {
                    System.err.println("Erro ao adicionar aresta entre polígonos: " + edgeId);
                }
            }
        }

        return gsGraph;
    }
}