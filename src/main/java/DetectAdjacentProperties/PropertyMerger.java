package DetectAdjacentProperties;

import Models.*;
import Utils.Annotations.CyclomaticComplexity;
import Utils.Annotations.Layer;
import Utils.Enums.LayerType;
import java.util.*;
import java.util.stream.Collectors;

/**
 * The {@code PropertyMerger} class provides functionality to merge adjacent properties
 * with the same owner into unified properties. Properties are considered adjacent if they
 * share at least one vertex.
 */
@Layer(LayerType.BACK_END)
public class PropertyMerger {

    /**
     * Merges adjacent properties with the same owner into unified properties.
     * It identifies groups of properties that are adjacent and have the same owner,
     * and merges them into a single property.
     *
     * @param properties A list of PropertyPolygon objects representing the properties to be merged.
     * @return A list of PropertyPolygon objects after merging adjacent properties.
     */
    @CyclomaticComplexity(1)
    public static List<PropertyPolygon> mergeOwnerAdjacentProperties(List<PropertyPolygon> properties) {
        Map<VertexCoordinate, List<PropertyPolygon>> vertexMap = buildVertexMap(properties);
        Map<Integer, Set<Integer>> adjacencyMap = buildAdjacencyMap(vertexMap);
        List<List<Integer>> groups = findConnectedComponents(properties, adjacencyMap);
        return mergeProperties(properties, groups);
    }

    /**
     * Merges adjacent properties with the same owner into unified properties.
     * It identifies groups of properties that are adjacent and have the same owner,
     * and merges them into a single property.
     *
     * @param properties A list of PropertyPolygon objects representing the properties to be merged.
     * @return A list of PropertyPolygon objects after merging adjacent properties.
     */
    @CyclomaticComplexity(3)
    private static Map<VertexCoordinate, List<PropertyPolygon>> buildVertexMap(List<PropertyPolygon> properties) {
        Map<VertexCoordinate, List<PropertyPolygon>> vertexMap = new HashMap<>();
        for (PropertyPolygon property : properties) {
            for (VertexCoordinate vertex : property.getPolygon().getVertices()) {
                vertexMap.computeIfAbsent(vertex, k -> new ArrayList<>()).add(property);
            }
        }
        return vertexMap;
    }

    /**
     * Builds an adjacency map from the vertex map, where each property ID is associated with a set of adjacent property IDs.
     *
     * @param vertexMap A map where keys are VertexCoordinate objects and values are lists of PropertyPolygon objects.
     * @return A map where keys are property IDs and values are sets of adjacent property IDs.
     */
    @CyclomaticComplexity(5)
    private static Map<Integer, Set<Integer>> buildAdjacencyMap(Map<VertexCoordinate, List<PropertyPolygon>> vertexMap) {
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
        return adjacencyMap;
    }

    /**
     * Finds connected components of properties that are adjacent and have the same owner.
     *
     * @param properties A list of PropertyPolygon objects representing the properties to be merged.
     * @param adjacencyMap A map where keys are property IDs and values are sets of adjacent property IDs.
     * @return A list of lists, where each inner list contains the IDs of properties in the same connected component.
     */
    @CyclomaticComplexity(9)
    private static List<List<Integer>> findConnectedComponents(List<PropertyPolygon> properties, Map<Integer, Set<Integer>> adjacencyMap) {
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
        return groups;
    }

    /**
     * Merges properties in the same group into a single property.
     *
     * @param properties A list of PropertyPolygon objects representing the properties to be merged.
     * @param groups A list of lists, where each inner list contains the IDs of properties in the same connected component.
     * @return A list of PropertyPolygon objects after merging adjacent properties.
     */
    @CyclomaticComplexity(5)
    private static List<PropertyPolygon> mergeProperties(List<PropertyPolygon> properties, List<List<Integer>> groups) {
        Map<Integer, PropertyPolygon> idToProperty = properties.stream()
                .collect(Collectors.toMap(PropertyPolygon::getObjectId, p -> p));

        Set<Integer> mergedIds = new HashSet<>();
        List<PropertyPolygon> mergedList = new ArrayList<>();

        for (List<Integer> group : groups) {
            List<PropertyPolygon> groupProps = group.stream()
                    .map(idToProperty::get)
                    .toList();

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
                    group.stream().min(Integer::compare).orElse(-1),            // Temporary ID for merged property
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

        for (PropertyPolygon p : properties) {
            if (!mergedIds.contains(p.getObjectId())) {
                mergedList.add(p);
            }
        }

        return mergedList;
    }

}