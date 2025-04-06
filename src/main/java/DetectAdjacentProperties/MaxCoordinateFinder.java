package DetectAdjacentProperties;

import java.util.List;

public class MaxCoordinateFinder {

    public static double[] findMaxCoordinates(List<PropertyPolygon> properties) {
        double maxX = Double.MIN_VALUE;
        double maxY = Double.MIN_VALUE;

        for (PropertyPolygon property : properties) {
            for (VertexCoordinate vertex : property.getPolygon().getCoordenadas()) {
                if (vertex.getX() > maxX) {
                    maxX = vertex.getX();
                }
                if (vertex.getY() > maxY) {
                    maxY = vertex.getY();
                }
            }
        }
        System.out.println("Max X = " + maxX + " Max Y = " + maxY);

        return new double[] { maxX, maxY }; // Retorna ambos os valores
    }
}