package DetectAdjacentProperties;

import UploadCSV.CsvLogger;
import UploadCSV.CsvValidator;

import java.util.*;

/**
 * This class is responsible for detecting adjacent properties based on their polygons.
 * It provides methods to convert CSV data to property polygons and identify pairs of adjacent properties.
 */
public class AdjacencyDetector {

    /**
     * Converts a list of CSV data rows into a list of PropertyPolygon objects.
     * It skips rows with polygons that have no vertices.
     *
     * @param data A list of CSV rows, each representing a property with polygon data.
     * @return A list of PropertyPolygon objects.
     */
    public static List<PropertyPolygon> convertToProperties(List<String[]> data) {
        List<PropertyPolygon> properties = new ArrayList<>();

        for (int i = 1; i < data.size(); i++) { // Skipping headers
            PropertyPolygon property = PropertyPolygon.fromCsvRow(data.get(i));
            if (property != null) {
                if (property.getPolygon().getCoordenadas().isEmpty()) {
                    CsvLogger.logError("Polygon without vertices in row " + (i + 1));
                    continue;  // Skip properties without vertices
                }
                if (property.getOwner().isEmpty()) {
                    CsvLogger.logError("Polygon without owner in row " + (i + 1));
                    continue;
                }
                properties.add(property);
            }
        }
        return properties;
    }

    /**
     * Finds adjacent properties by checking if two properties share at least one vertex.
     * It returns a list of unique pairs of adjacent properties.
     *
     * @param properties A list of PropertyPolygon objects to check for adjacency.
     * @return A list of AdjacentPropertyPair objects, representing pairs of adjacent properties.
     */
    public static List<AdjacentPropertyPair> findAdjacentProperties(List<PropertyPolygon> properties) {
        List<AdjacentPropertyPair> adjacentPairs = new ArrayList<>();
        Set<String> seenPairs = new HashSet<>();
        int adjacentCount = 0;

        // Criar a grade espacial e inserir os terrenos nela
        SpatialGrid spatialGrid = new SpatialGrid();
        for (PropertyPolygon property : properties) {
            spatialGrid.insert(property);
        }

        // Comparar cada propriedade apenas com as que estão na mesma grid
        for (PropertyPolygon prop1 : properties) {
            List<PropertyPolygon> nearbyProperties = spatialGrid.getNearbyProperties(prop1);

            System.out.println("Testing property: " + prop1.getObjectId() +
                    " | Nearby properties: " + nearbyProperties.size());

            for (PropertyPolygon prop2 : nearbyProperties) {
                if (prop1 == prop2) continue; // Evitar comparar com ele mesmo

                if (shareVertex(prop1, prop2)) {
                    String pair1 = prop1.getObjectId() + "-" + prop2.getObjectId();
                    String pair2 = prop2.getObjectId() + "-" + prop1.getObjectId();

                    if (!seenPairs.contains(pair1) && !seenPairs.contains(pair2)) {
                        adjacentPairs.add(new AdjacentPropertyPair(prop1.getObjectId(), prop2.getObjectId()));
                        seenPairs.add(pair1);
                        adjacentCount++;
                    }
                }
            }
        }

        System.out.println("Total number of adjacent properties: " + adjacentCount);
        return adjacentPairs;
    }

    /**
     * Checks if two properties share at least one vertex.
     *
     * @param p1 The first property polygon.
     * @param p2 The second property polygon.
     * @return True if the two properties share at least one vertex, false otherwise.
     */
    private static boolean shareVertex(PropertyPolygon p1, PropertyPolygon p2) {
        Set<String> vertices1 = new HashSet<>();
        for (VertexCoordinate v : p1.getPolygon().getCoordenadas()) {
            vertices1.add(v.getX() + "," + v.getY());
        }

        for (VertexCoordinate v : p2.getPolygon().getCoordenadas()) {
            if (vertices1.contains(v.getX() + "," + v.getY())) {
                return true; // Found a shared vertex
            }
        }
        return false;
    }
}