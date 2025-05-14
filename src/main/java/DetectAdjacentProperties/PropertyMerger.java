package DetectAdjacentProperties;

import DetectAdjacentProperties.*;
import Models.*;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Utility class responsible for merging adjacent properties that belong to the same owner.
 */
public class PropertyMerger {

    /**
     * Merges adjacent properties with the same owner into single unified properties.
     * Properties are considered adjacent if they share at least one vertex.
     *
     * @param properties A list of {@link PropertyPolygon} representing all original properties.
     * @return A list of {@link PropertyPolygon} with merged properties, replacing the original adjacent groups.
     */
    public static List<PropertyPolygon> mergeOwnerAdjacentProperties(List<PropertyPolygon> properties) {
        // Map from vertex to the list of properties that contain that vertex
        Map<VertexCoordinate, List<PropertyPolygon>> vertexMap = new HashMap<>();
        for (PropertyPolygon property : properties) {
            for (VertexCoordinate vertex : property.getPolygon().getVertices()) {
                vertexMap.computeIfAbsent(vertex, k -> new ArrayList<>()).add(property);
            }
        }

        // Build adjacency graph based on shared vertices and matching owners
        Map<Integer, Set<Integer>> adjacencyMap = new HashMap<>();
        for (List<PropertyPolygon> sharedProps : vertexMap.values()) {
            for (int i = 0; i < sharedProps.size(); i++) {
                for (int j = i + 1; j < sharedProps.size(); j++) {
                    PropertyPolygon p1 = sharedProps.get(i);
                    PropertyPolygon p2 = sharedProps.get(j);
                    if (!p1.getOwner().equals(p2.getOwner())) continue;

                    int id1 = p1.getObjectId();
                    int id2 = p2.getObjectId();

                    adjacencyMap.computeIfAbsent(id1, k -> new HashSet<>()).add(id2);
                    adjacencyMap.computeIfAbsent(id2, k -> new HashSet<>()).add(id1);
                }
            }
        }

        // Identify connected components of adjacent properties with the same owner
        Set<Integer> visited = new HashSet<>();
        List<List<Integer>> groups = new ArrayList<>();

        for (PropertyPolygon prop : properties) {
            int startId = prop.getObjectId();
            if (visited.contains(startId)) continue;

            String owner = prop.getOwner();
            List<Integer> group = new ArrayList<>();
            Queue<Integer> queue = new LinkedList<>();
            queue.add(startId);
            visited.add(startId);

            while (!queue.isEmpty()) {
                int current = queue.poll();
                group.add(current);
                for (int neighbor : adjacencyMap.getOrDefault(current, Collections.emptySet())) {
                    PropertyPolygon neighborProp = properties.stream()
                            .filter(p -> p.getObjectId() == neighbor)
                            .findFirst().orElse(null);
                    if (neighborProp != null && neighborProp.getOwner().equals(owner) && visited.add(neighbor)) {
                        queue.add(neighbor);
                    }
                }
            }

            if (group.size() > 1) groups.add(group);
        }

        // Map from property ID to the PropertyPolygon instance
        Map<Integer, PropertyPolygon> idToProperty = properties.stream()
                .collect(Collectors.toMap(PropertyPolygon::getObjectId, p -> p));

        // Merge grouped properties into new PropertyPolygon instances
        Set<Integer> mergedIds = new HashSet<>();
        List<PropertyPolygon> mergedList = new ArrayList<>();

        for (List<Integer> group : groups) {
            List<PropertyPolygon> groupProps = group.stream()
                    .map(idToProperty::get)
                    .collect(Collectors.toList());

            PropertyPolygon base = groupProps.get(0);
            double totalArea = 0;
            double totalLength = 0;
            List<VertexCoordinate> allVertices = new ArrayList<>();

            for (PropertyPolygon prop : groupProps) {
                totalArea += prop.getShapeArea();
                totalLength += prop.getShapeLength();
                allVertices.addAll(prop.getPolygon().getVertices());
            }

            PropertyPolygon merged = new PropertyPolygon(
                    -group.hashCode(),             // Temporary ID for merged property
                    base.getParId(),
                    base.getParNum(),
                    totalLength,
                    totalArea,
                    new Polygon(allVertices),
                    base.getOwner(),
                    base.getFreguesia(),
                    base.getMunicipio(),
                    base.getIlha()
            );

            mergedList.add(merged);
            mergedIds.addAll(group);
        }

        // Add non-merged properties to the final list
        for (PropertyPolygon p : properties) {
            if (!mergedIds.contains(p.getObjectId())) {
                mergedList.add(p);
            }
        }

        return mergedList;
    }
}