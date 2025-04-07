package BuildPropertyGraph;

import DetectAdjacentProperties.PropertyPolygon;
import DetectAdjacentProperties.VertexCoordinate;
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
     * Builds a graph from a list of PropertyPolygon objects.
     *
     * @param properties List of PropertyPolygon objects to be added to the graph.
     * @return A graph where vertices are PropertyPolygon objects and edges represent shared vertices.
     */
    public static Graph<PropertyPolygon, DefaultEdge> buildGraph(List<PropertyPolygon> properties) {
        Graph<PropertyPolygon, DefaultEdge> graph = new SimpleGraph<>(DefaultEdge.class);

        // Add all vertices
        for (PropertyPolygon property : properties) {
            graph.addVertex(property);
        }

        // Index vertices
        Map<VertexCoordinate, List<PropertyPolygon>> vertexMap = new HashMap<>();
        for (PropertyPolygon property : properties) {
            for (VertexCoordinate vertex : property.getPolygon().getCoordenadas()) {
                vertexMap.computeIfAbsent(vertex, k -> new ArrayList<>()).add(property);
            }
        }

        // Connect PropertyPolygon objects that share vertices
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
     * Prints the graph details including the number of vertices and edges.
     *
     * @param graph The graph to be printed.
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
     * Exports the graph to a DOT file.
     *
     * @param graph The graph to be exported.
     */
    public static void exportGraphToDot(Graph<PropertyPolygon, DefaultEdge> graph) {
        DOTExporter<PropertyPolygon, DefaultEdge> exporter = new DOTExporter<>();

        // Define how the node will be labeled (e.g., with the objectId)
        exporter.setVertexAttributeProvider((PropertyPolygon p) -> {
            return Map.of("label", DefaultAttribute.createAttribute("ID: " + p.getObjectId()));
        });

        try (FileWriter writer = new FileWriter("graphCheck.dot", false)) {
            exporter.exportGraph(graph, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}