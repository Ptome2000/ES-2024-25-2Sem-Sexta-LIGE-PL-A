package DetectAdjacentProperties;

import java.util.*;

public class AdjacencyDetector {
    public static List<PropertyPolygon> convertToProperties(List<String[]> data) {
        List<PropertyPolygon> properties = new ArrayList<>();

        for (int i = 1; i < data.size(); i++) { // Ignorar cabeçalhos
            PropertyPolygon property = PropertyPolygon.fromCsvRow(data.get(i));
            if (property != null) {
                properties.add(property);
            }
        }
        return properties;
    }

    public static Map<PropertyPolygon, List<PropertyPolygon>> findAdjacentProperties(List<PropertyPolygon> properties) {
        Map<PropertyPolygon, List<PropertyPolygon>> adjacencyMap = new HashMap<>();

        for (int i = 0; i < properties.size(); i++) {
            PropertyPolygon prop1 = properties.get(i);
            System.out.println("Entrei na propriedade " + prop1.getObjectId());
            List<PropertyPolygon> adjacentList = new ArrayList<>();

            for (int j = 0; j < properties.size(); j++) {
                if (i == j) continue; // Evitar comparar com ela mesma

                PropertyPolygon prop2 = properties.get(j);

                if (shareVertex(prop1, prop2)) {
                    adjacentList.add(prop2);
                }
            }

            if (!adjacentList.isEmpty()) {
                adjacencyMap.put(prop1, adjacentList);
            }
        }
        return adjacencyMap;
    }

    private static boolean shareVertex(PropertyPolygon p1, PropertyPolygon p2) {
        Set<String> vertices1 = new HashSet<>();
        for (double[] v : p1.getVertices()) {
            vertices1.add(v[0] + "," + v[1]);
        }

        for (double[] v : p2.getVertices()) {
            if (vertices1.contains(v[0] + "," + v[1])) {
                return true; // Encontrou um vértice partilhado
            }
        }
        return false;
    }


}
