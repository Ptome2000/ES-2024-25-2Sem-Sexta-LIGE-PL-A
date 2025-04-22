package BuildPropertyGraph;

import DetectAdjacentProperties.*;
import DetectAdjacentProperties.Polygon;
import edu.uci.ics.jung.algorithms.layout.Layout;
import edu.uci.ics.jung.algorithms.layout.StaticLayout;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.SparseMultigraph;
import edu.uci.ics.jung.visualization.Layer;
import edu.uci.ics.jung.visualization.VisualizationViewer;
import edu.uci.ics.jung.visualization.control.DefaultModalGraphMouse;
import edu.uci.ics.jung.visualization.renderers.Renderer;
import javax.swing.*;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Point2D;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The GraphViewer class is responsible for visualizing a graph of property polygons
 * using the JUNG library. It provides methods to calculate the layout of the graph
 * and render it in a graphical window.
 */
public class GraphViewer {

    /**
     * Displays the given graph in a graphical window.
     *
     * @param graph The graph to be visualized, where vertices represent property polygons
     *              and edges represent relationships between them.
     */
    public static void showGraph(Graph<PropertyPolygon, String> graph) {
        Map<PropertyPolygon, Point2D> locationMap = calculateGraphLayout(graph, 1024, 1024);
        renderGraph(locationMap, graph, 1024, 1024);
    }

    /**
     * Calculates the layout of the graph by determining the positions of each vertex
     * based on their centroids and scaling them to fit within the specified window dimensions.
     *
     * @param graph        The graph whose layout is to be calculated.
     * @param windowWidth  The width of the window in which the graph will be displayed.
     * @param windowHeight The height of the window in which the graph will be displayed.
     * @return A map of property polygons to their calculated positions in the layout.
     */
    public static Map<PropertyPolygon, Point2D> calculateGraphLayout(Graph<PropertyPolygon, String> graph, int windowWidth, int windowHeight) {
        Map<PropertyPolygon, Point2D> rawCentroids = new HashMap<>();

        double minX = Double.MAX_VALUE, minY = Double.MAX_VALUE;
        double maxX = Double.MIN_VALUE, maxY = Double.MIN_VALUE;

        // Calculate centroids without normalizing Y
        for (PropertyPolygon polygon : graph.getVertices()) {
            List<VertexCoordinate> coords = polygon.getPolygon().getVertices();
            double sumX = 0, sumY = 0;
            for (VertexCoordinate vc : coords) {
                sumX += vc.getX();
                sumY += vc.getY();
            }
            double centerX = sumX / coords.size();
            double centerY = sumY / coords.size();

            rawCentroids.put(polygon, new Point2D.Double(centerX, centerY));

            if (centerX < minX) minX = centerX;
            if (centerX > maxX) maxX = centerX;
            if (centerY < minY) minY = centerY;
            if (centerY > maxY) maxY = centerY;
        }

        // Calculate data dimensions
        double dataWidth = maxX - minX;
        double dataHeight = maxY - minY;

        double padding = 20;
        double scaleX = (windowWidth - padding * 2) / dataWidth;
        double scaleY = (windowHeight - padding * 2) / dataHeight;
//        double scaleX = (windowWidth * 0.9) / dataWidth;
//        double scaleY = (windowHeight * 0.9) / dataHeight;
        double scale = Math.min(scaleX, scaleY); // maintain proportion

        // Apply transformation (invert Y after scaling)
        Map<PropertyPolygon, Point2D> locationMap = new HashMap<>();
        for (Map.Entry<PropertyPolygon, Point2D> entry : rawCentroids.entrySet()) {
            Point2D p = entry.getValue();
            double x = (p.getX() - minX) * scale;
            double y = (maxY - p.getY()) * scale;  // invert Y here

            // Center the graph
            double finalX = x + (windowWidth - dataWidth * scale) / 2;
            double finalY = y + (windowHeight - dataHeight * scale) / 2;

            locationMap.put(entry.getKey(), new Point2D.Double(finalX, finalY));
        }

        return locationMap;
    }

    /**
     * Renders the graph in a graphical window using the calculated layout.
     *
     * @param locationMap  A map of property polygons to their positions in the layout.
     * @param graph        The graph to be rendered.
     * @param windowWidth  The width of the window in which the graph will be displayed.
     * @param windowHeight The height of the window in which the graph will be displayed.
     */
    private static void renderGraph(Map<PropertyPolygon, Point2D> locationMap, Graph<PropertyPolygon, String> graph,
                                    int windowWidth, int windowHeight) {

        Layout<PropertyPolygon, String> layout = new StaticLayout<>(graph, locationMap::get);
        layout.setSize(new Dimension(windowWidth, windowHeight));

        VisualizationViewer<PropertyPolygon, String> vv = new VisualizationViewer<>(layout);
        vv.setPreferredSize(new Dimension(windowWidth, windowHeight));
        vv.getRenderContext().setVertexLabelTransformer(p -> null);
        vv.getRenderContext().setEdgeLabelTransformer(e -> null);
        vv.getRenderContext().setVertexFillPaintTransformer(v -> Color.GRAY);
        vv.getRenderContext().setVertexShapeTransformer(v -> new Ellipse2D.Double(-4, -4, 8, 8));
        vv.getRenderer().getVertexLabelRenderer().setPosition(Renderer.VertexLabel.Position.CNTR);

        DefaultModalGraphMouse<PropertyPolygon, String> graphMouse = new DefaultModalGraphMouse<>();
        graphMouse.setMode(DefaultModalGraphMouse.Mode.TRANSFORMING);
        vv.setGraphMouse(graphMouse);

        JFrame frame = new JFrame("Property Map");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().add(vv);
        frame.pack();
        frame.setVisible(true);
    }

    public static JPanel createGraphPanel(Graph<PropertyPolygon, String> graph, int width, int height) {
        Map<PropertyPolygon, Point2D> layoutMap = calculateGraphLayout(graph, width, height);
        Layout<PropertyPolygon, String> layout = new StaticLayout<>(graph, layoutMap::get);
        layout.setSize(new Dimension(width, height));

        VisualizationViewer<PropertyPolygon, String> vv = new VisualizationViewer<>(layout);
        vv.setPreferredSize(new Dimension(width, height));
        vv.getRenderContext().setVertexLabelTransformer(p -> null);
        vv.getRenderContext().setEdgeLabelTransformer(e -> null);
        vv.getRenderContext().setVertexFillPaintTransformer(v -> Color.GRAY);
        vv.getRenderContext().setVertexShapeTransformer(v -> new Ellipse2D.Double(-4, -4, 8, 8));

        DefaultModalGraphMouse<PropertyPolygon, String> graphMouse = new DefaultModalGraphMouse<>();
        graphMouse.setMode(DefaultModalGraphMouse.Mode.TRANSFORMING);
        vv.setGraphMouse(graphMouse);

        JPanel panel = new JPanel(new BorderLayout());
        panel.add(vv, BorderLayout.CENTER);
        return panel;
    }

    /**
     * Main method for testing the GraphViewer class. Creates a sample graph and displays it.
     *
     * @param args Command-line arguments (not used).
     */
    public static void main(String[] args) {
        System.out.println("Test start");

        // Create a test graph
        Graph<PropertyPolygon, String> testGraph = new SparseMultigraph<>();

        Polygon polygon1 = new Polygon(List.of(
                new VertexCoordinate(0, 0),
                new VertexCoordinate(0, 10),
                new VertexCoordinate(10, 10),
                new VertexCoordinate(10, 0)
        ));

        Polygon polygon2 = new Polygon(List.of(
                new VertexCoordinate(20, 20),
                new VertexCoordinate(20, 30),
                new VertexCoordinate(30, 30),
                new VertexCoordinate(30, 20)
        ));

        PropertyPolygon property1 = new PropertyPolygon(
                1, 1001.0, "P001", 40.0, 100.0, polygon1, "Owner1", "Parish1", "Municipality1", "Island1"
        );

        PropertyPolygon property2 = new PropertyPolygon(
                2, 1002.0, "P002", 40.0, 100.0, polygon2, "Owner2", "Parish2", "Municipality2", "Island2"
        );
        testGraph.addVertex(property1);
        testGraph.addVertex(property2);
        testGraph.addEdge("e1", property1, property2);

        showGraph(testGraph);
    }
}