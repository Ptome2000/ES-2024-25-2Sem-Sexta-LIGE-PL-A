package DetectAdjacentProperties;

import Models.PropertyPolygon;
import Models.VertexCoordinate;
import Repository.CsvLogger;

import java.util.*;
import java.util.stream.Stream;

/**
 * This class is responsible for detecting adjacent properties based on their polygons.
 * It provides methods to convert CSV data to property polygons and identify pairs of adjacent properties.
 */
public class AdjacencyDetector {

    /**
     * Converts a list of CSV data rows into a list of PropertyPolygon objects.
     * It skips rows with polygons that have no vertices.
     *x
     * @param data A list of CSV rows, each representing a property with polygon data.
     * @return A list of PropertyPolygon objects.
     */
    public static List<PropertyPolygon> convertToProperties(List<String[]> data) {
        List<PropertyPolygon> properties = new ArrayList<>();

        for (int i = 1; i < data.size(); i++) { // Skipping headers
            PropertyPolygon property = PropertyPolygon.fromCsvRow(data.get(i));
            if (property != null) {
                if (property.getPolygon().getVertices().isEmpty()) {
                    CsvLogger.logError("Polygon without vertices in row " + (i + 1));
                    continue;  // Skip properties without vertices
                }
                if (property.getOwner().isEmpty()) {
                    CsvLogger.logError("Polygon without owner in row " + (i + 1));
                    continue;
                }
                if (Stream.of(property.getFreguesia(), property.getMunicipio(), property.getIlha()).anyMatch(val -> val.equalsIgnoreCase("NA"))) {
                    CsvLogger.logError("Propriedade ignorada por ter NA em freguesia, município ou ilha na linha " + (i + 1));
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

        // Create the spatial grid and insert properties into it
        SpatialGrid spatialGrid = new SpatialGrid(properties);
//        spatialGrid.printGridRanges();

        for (PropertyPolygon property : properties) {
            spatialGrid.insert(property);
        }


        // After inserting all properties, log the number of properties in each cell
        spatialGrid.logPropertiesInCells();

        // Compare each property only with others in the same grid

        for (PropertyPolygon prop1 : properties) {
            List<PropertyPolygon> nearbyProperties = spatialGrid.getNearbyProperties(prop1);

            for (PropertyPolygon prop2 : nearbyProperties) {

                if (prop1 == prop2) continue; // Avoid comparing with itself

                if (shareVertex(prop1, prop2)) {
                    String pair1 = prop1.getObjectId() + "-" + prop2.getObjectId();
                    String pair2 = prop2.getObjectId() + "-" + prop1.getObjectId();

                    // Check if this property pair has already been compared

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
     * Finds valid adjacent property pairs by checking if two properties share at least one vertex
     * and belong to different owners.
     *
     * @param properties A list of PropertyPolygon objects to check for adjacency.
     * @return A list of AdjacentPropertyPair objects, representing pairs of adjacent properties.
     */
    public static List<AdjacentPropertyPair> findValidAdjacentPairs(List<PropertyPolygon> properties) {
        List<AdjacentPropertyPair> adjacentPairs = new ArrayList<>();
        Set<String> seenPairs = new HashSet<>();
        int adjacentCount = 0;

        SpatialGrid spatialGrid = new SpatialGrid(properties);
        for (PropertyPolygon property : properties) {
            spatialGrid.insert(property);
        }

        for (PropertyPolygon prop1 : properties) {
            if (!isValidProperty(prop1)) continue;

            List<PropertyPolygon> nearbyProperties = spatialGrid.getNearbyProperties(prop1);

            for (PropertyPolygon prop2 : nearbyProperties) {
                if (prop1 == prop2 || !isValidProperty(prop2)) continue;

                // Verifica se os donos são diferentes
                if (prop1.getOwner().equals(prop2.getOwner())) continue;

                // Verifica se partilham pelo menos um vértice
                if (shareVertex(prop1, prop2)) {
                    String key1 = prop1.getObjectId() + "-" + prop2.getObjectId();
                    String key2 = prop2.getObjectId() + "-" + prop1.getObjectId();

                    if (!seenPairs.contains(key1) && !seenPairs.contains(key2)) {
                        adjacentPairs.add(new AdjacentPropertyPair(prop1.getObjectId(), prop2.getObjectId()));
                        seenPairs.add(key1);
                        adjacentCount++;
                    }
                }
            }
        }

        System.out.println("Total number of adjacent properties: " + adjacentCount);
        return adjacentPairs;
    }

    /**
     * Checks if a property is valid based on its polygon and area.
     *
     * @param property The PropertyPolygon object to check.
     * @return True if the property is valid, false otherwise.
     */
    private static boolean isValidProperty(PropertyPolygon property) {
        return property != null &&
                property.getPolygon() != null &&
                property.getPolygon().getVertices() != null &&
                !property.getPolygon().getVertices().isEmpty() &&
                property.getShapeArea() > 0;
    }

    /**
     * Checks if two properties share at least one vertex.
     *
     * @param p1 The first property polygon.
     * @param p2 The second property polygon.
     * @return True if the two properties share at least one vertex, false otherwise.
     */
    static boolean shareVertex(PropertyPolygon p1, PropertyPolygon p2) {
        Set<String> vertices1 = new HashSet<>();
        for (VertexCoordinate v : p1.getPolygon().getVertices()) {
            vertices1.add(v.getX() + "," + v.getY());
        }

        for (VertexCoordinate v : p2.getPolygon().getVertices()) {
            if (vertices1.contains(v.getX() + "," + v.getY())) {
                return true; // Found a shared vertex
            }
        }
        return false;
    }


}
