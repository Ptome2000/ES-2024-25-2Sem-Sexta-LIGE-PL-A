package DetectAdjacentProperties;

import java.util.List;

public class MaxCoordinateFinder {

    public static void findMaxCoordinates(List<PropertyPolygon> properties) {
        double maxX = Double.MIN_VALUE; // Inicializa com o menor valor possível
        double maxY = Double.MIN_VALUE; // Inicializa com o menor valor possível

        // Percorrer todas as propriedades
        for (PropertyPolygon property : properties) {
            // Percorrer todos os vértices de cada propriedade
            for (VertexCoordinate vertex : property.getPolygon().getCoordenadas()) {
                // Verificar se o X do vértice é maior que o maior X atual
                if (vertex.getX() > maxX) {
                    maxX = vertex.getX();
                }

                // Verificar se o Y do vértice é maior que o maior Y atual
                if (vertex.getY() > maxY) {
                    maxY = vertex.getY();
                }
            }
        }

        // Exibir os maiores valores encontrados
        System.out.println("Maior valor de X: " + maxX);
        System.out.println("Maior valor de Y: " + maxY);
    }
}