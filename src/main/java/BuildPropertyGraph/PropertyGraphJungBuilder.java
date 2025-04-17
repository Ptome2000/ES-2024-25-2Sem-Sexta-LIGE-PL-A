package BuildPropertyGraph;

import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.SparseGraph;
import DetectAdjacentProperties.PropertyPolygon;
import DetectAdjacentProperties.VertexCoordinate;
import java.util.*;

/**
 * The PropertyGraphJungBuilder class is responsible for creating a graph representation
 * of property polygons using the JUNG library. Each property polygon is represented as a vertex,
 * and edges are created between properties that share at least one vertex coordinate.
 */
public class PropertyGraphJungBuilder {

    /**
     * Builds a graph where each vertex represents a property polygon, and an edge connects
     * properties that share at least one vertex coordinate.
     *
     * @param properties A list of PropertyPolygon objects to be added to the graph.
     * @return A JUNG graph with PropertyPolygon objects as vertices and edges representing shared coordinates.
     */
    public static Graph<PropertyPolygon, String> buildGraph(List<PropertyPolygon> properties) {
        Graph<PropertyPolygon, String> graph = new SparseGraph<>();
        int edgeId = 0;

        // Add all properties as vertices
        for (PropertyPolygon property : properties) {
            graph.addVertex(property);
        }

        // Map each vertex coordinate to the properties it belongs to
        Map<VertexCoordinate, List<PropertyPolygon>> vertexMap = new HashMap<>();
        for (PropertyPolygon property : properties) {
            for (VertexCoordinate vertex : property.getPolygon().getCoordenadas()) {
                vertexMap.computeIfAbsent(vertex, k -> new ArrayList<>()).add(property);
            }
        }

        // Create edges between properties sharing at least one vertex
        for (List<PropertyPolygon> sharedProps : vertexMap.values()) {
            for (int i = 0; i < sharedProps.size(); i++) {
                for (int j = i + 1; j < sharedProps.size(); j++) {
                    PropertyPolygon p1 = sharedProps.get(i);
                    PropertyPolygon p2 = sharedProps.get(j);
                    if (!p1.equals(p2) && !graph.isNeighbor(p1, p2)) {
                        graph.addEdge("e" + (edgeId++), p1, p2);
                        System.out.println("Added edge between " + p1.getObjectId() + " and " + p2.getObjectId());
                    }
                }
            }
        }

        return graph;
    }
}