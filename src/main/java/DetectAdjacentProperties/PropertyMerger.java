package DetectAdjacentProperties;

import DetectAdjacentProperties.*;
import Models.*;
import java.util.*;
import java.util.stream.Collectors;

public class PropertyMerger {

    public static List<PropertyPolygon> mergeOwnerAdjacentProperties(List<PropertyPolygon> properties) {
        // Mapa: vértice -> propriedades com esse vértice
        Map<VertexCoordinate, List<PropertyPolygon>> vertexMap = new HashMap<>();
        for (PropertyPolygon property : properties) {
            for (VertexCoordinate vertex : property.getPolygon().getVertices()) {
                vertexMap.computeIfAbsent(vertex, k -> new ArrayList<>()).add(property);
            }
        }

        // Criar grafo de adjacência (por ID)
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

        // Explorar componentes ligadas (por owner)
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

        // Map ID to Property
        Map<Integer, PropertyPolygon> idToProperty = properties.stream()
                .collect(Collectors.toMap(PropertyPolygon::getObjectId, p -> p));

        // Gerar propriedades mescladas
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
                    -group.hashCode(),             // ID fictício
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

        // Adicionar propriedades não mescladas
        for (PropertyPolygon p : properties) {
            if (!mergedIds.contains(p.getObjectId())) {
                mergedList.add(p);
            }
        }

        return mergedList;
    }
}