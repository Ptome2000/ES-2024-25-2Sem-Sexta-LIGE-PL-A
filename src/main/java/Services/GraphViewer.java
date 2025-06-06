package Services;

import Models.PropertyPolygon;
import Models.VertexCoordinate;
import Utils.Annotations.CyclomaticComplexity;
import Utils.Annotations.Layer;
import Utils.Enums.LayerType;
import edu.uci.ics.jung.algorithms.layout.Layout;
import edu.uci.ics.jung.algorithms.layout.StaticLayout;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.visualization.VisualizationViewer;
import edu.uci.ics.jung.visualization.control.DefaultModalGraphMouse;
import edu.uci.ics.jung.visualization.control.GraphMouseListener;
import edu.uci.ics.jung.visualization.renderers.Renderer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Point2D;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The {@code GraphViewer} class provides utility methods to visualize
 * a graph of {@link PropertyPolygon} nodes using the JUNG library.
 * It includes methods to compute a static layout and render the graph
 * in a Swing {@link JPanel}.
 */
@Layer(LayerType.FRONT_END)
public class GraphViewer {

    /**
     * Calculates the positions (layout) of each {@link PropertyPolygon} node
     * based on the centroid of its polygon, scaling and translating them to fit
     * within the given window dimensions.
     *
     * @param graph        the graph containing property polygons as vertices
     * @param windowWidth  the width of the visualization window in pixels
     * @param windowHeight the height of the visualization window in pixels
     * @return a map associating each {@link PropertyPolygon} with a scaled screen position
     */
    @CyclomaticComplexity(4)
    public static Map<PropertyPolygon, Point2D> calculateGraphLayout(Graph<PropertyPolygon, String> graph, int windowWidth, int windowHeight) {
        Map<PropertyPolygon, Point2D> rawCentroids = new HashMap<>();
        double minX = Double.MAX_VALUE, minY = Double.MAX_VALUE;
        double maxX = Double.MIN_VALUE, maxY = Double.MIN_VALUE;

        for (PropertyPolygon polygon : graph.getVertices()) {
            List<VertexCoordinate> coords = polygon.getPolygon().getVertices();
            double sumX = 0, sumY = 0;
            for (VertexCoordinate vc : coords) {
                sumX += vc.x();
                sumY += vc.y();
            }
            double centerX = sumX / coords.size();
            double centerY = sumY / coords.size();

            rawCentroids.put(polygon, new Point2D.Double(centerX, centerY));

            minX = Math.min(minX, centerX);
            maxX = Math.max(maxX, centerX);
            minY = Math.min(minY, centerY);
            maxY = Math.max(maxY, centerY);
        }

        double dataWidth = maxX - minX;
        double dataHeight = maxY - minY;
        double padding = 20;
        double scaleX = (windowWidth - padding * 2) / dataWidth;
        double scaleY = (windowHeight - padding * 2) / dataHeight;
        double scale = Math.min(scaleX, scaleY);

        Map<PropertyPolygon, Point2D> locationMap = new HashMap<>();
        for (Map.Entry<PropertyPolygon, Point2D> entry : rawCentroids.entrySet()) {
            Point2D p = entry.getValue();
            double x = (p.getX() - minX) * scale + (windowWidth - dataWidth * scale) / 2;
            double y = (maxY - p.getY()) * scale + (windowHeight - dataHeight * scale) / 2;
            locationMap.put(entry.getKey(), new Point2D.Double(x, y));
        }

        return locationMap;
    }

    /**
     * Creates and returns a {@link JPanel} containing a graphical visualization
     * of the given {@link Graph}, using a static layout based on centroid positions.
     *
     * @param graph         the graph of property polygons to be visualized
     * @param width         the width of the panel in pixels
     * @param height        the height of the panel in pixels
     * @param showOwnerIds  if true, shows the owner ID as the label on each node
     * @return a {@link JPanel} containing the interactive graph visualization
     */
    @CyclomaticComplexity(3)
    public static JPanel createGraphPanel(Graph<PropertyPolygon, String> graph, int width, int height, boolean showOwnerIds) {
        Map<PropertyPolygon, Point2D> layoutMap = calculateGraphLayout(graph, width, height);
        Layout<PropertyPolygon, String> layout = new StaticLayout<>(graph, layoutMap::get);
        layout.setSize(new Dimension(width, height));

        VisualizationViewer<PropertyPolygon, String> vv = new VisualizationViewer<>(layout);
        vv.setPreferredSize(new Dimension(width, height));
        vv.getRenderer().getVertexLabelRenderer().setPosition(Renderer.VertexLabel.Position.CNTR);
        vv.getRenderContext().setVertexLabelTransformer(p -> showOwnerIds ? p.getOwner() : null);

        vv.getRenderContext().setEdgeLabelTransformer(e -> null);
        vv.getRenderContext().setVertexFillPaintTransformer(v -> v.getObjectId() < 0 ? Color.RED : Color.GRAY);
        vv.getRenderContext().setVertexShapeTransformer(v -> new Ellipse2D.Double(-4, -4, 16, 16));

        vv.getRenderContext().setVertexFontTransformer(v -> new Font("SansSerif", Font.PLAIN, 8));
        vv.getRenderingHints().put(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
        vv.getRenderingHints().put(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_SPEED);
        vv.getRenderingHints().put(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR);

        vv.setDoubleBuffered(true);
        vv.getRenderingHints().put(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_SPEED);
        vv.getRenderingHints().put(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_SPEED);

        DefaultModalGraphMouse<PropertyPolygon, String> graphMouse = new DefaultModalGraphMouse<>();
        graphMouse.setMode(DefaultModalGraphMouse.Mode.TRANSFORMING);
        vv.setGraphMouse(graphMouse);

        vv.addGraphMouseListener(new GraphMouseListener<>() {
            @Override
            public void graphClicked(PropertyPolygon vertex, MouseEvent me) {
                SwingUtilities.invokeLater(() -> {
                    Component parent = SwingUtilities.getWindowAncestor(vv);
                    PropertyInfoDialog dialog = new PropertyInfoDialog((JFrame) parent, vertex);
                    dialog.setVisible(true);
                });
            }

            @Override
            public void graphPressed(PropertyPolygon vertex, MouseEvent me) {}

            @Override
            public void graphReleased(PropertyPolygon vertex, MouseEvent me) {}
        });

        JPanel panel = new JPanel(new BorderLayout());
        panel.add(vv, BorderLayout.CENTER);
        return panel;
    }
}