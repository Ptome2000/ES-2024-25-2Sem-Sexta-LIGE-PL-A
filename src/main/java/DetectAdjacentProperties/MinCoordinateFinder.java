package DetectAdjacentProperties;

import java.util.List;

/**
 * This class provides methods to find the maximum and minimum coordinates in a list of properties.
 */
public class MinCoordinateFinder {

    public static double[] findMinCoordinates(List<PropertyPolygon> properties) {
        double minX = Double.MAX_VALUE;
        double minY = Double.MAX_VALUE;

        for (PropertyPolygon property : properties) {
            for (VertexCoordinate vertex : property.getPolygon().getCoordenadas()) {
                if (vertex.getX() < minX) {
                    minX = vertex.getX();
                }
                if (vertex.getY() < minY) {
                    minY = vertex.getY();
                }
            }
        }
        System.out.println("Min X = " + minX + " Min Y = " + minY);
        return new double[] { minX, minY }; // Retorna ambos os valores
    }
}