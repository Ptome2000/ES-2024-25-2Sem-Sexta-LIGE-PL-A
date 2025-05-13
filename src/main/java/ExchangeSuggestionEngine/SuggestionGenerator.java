package ExchangeSuggestionEngine;

import DetectAdjacentProperties.AdjacentPropertyPair;
import Models.ExchangeSuggestion;
import Models.PropertyPolygon;

import java.util.*;

/**
 * This class is responsible for generating exchange suggestions based on adjacent properties.
 * It takes a list of adjacent property pairs and a list of property polygons, and generates
 * suggestions for property exchanges that could be beneficial for the owners.
 */
public class SuggestionGenerator {

    public static List<ExchangeSuggestion> generateSuggestions(
            List<AdjacentPropertyPair> adjacentPairs,
            List<PropertyPolygon> properties) {

        Map<Integer, PropertyPolygon> propertyMap = new HashMap<>();
        for (PropertyPolygon p : properties) {
            propertyMap.put(p.getObjectId(), p);
        }

        Map<String, List<AdjacentPropertyPair>> pairsByOwnerPair = new HashMap<>();

        for (AdjacentPropertyPair pair : adjacentPairs) {
            PropertyPolygon p1 = propertyMap.get((int) pair.getPropertyId1());
            PropertyPolygon p2 = propertyMap.get((int) pair.getPropertyId2());

            if (p1 == null || p2 == null) continue;

            String owner1 = p1.getOwner();
            String owner2 = p2.getOwner();

            String key = owner1.compareTo(owner2) < 0
                    ? owner1 + "|" + owner2
                    : owner2 + "|" + owner1;

            pairsByOwnerPair.computeIfAbsent(key, k -> new ArrayList<>()).add(pair);
        }

        List<ExchangeSuggestion> suggestions = new ArrayList<>();

        for (List<AdjacentPropertyPair> pairList : pairsByOwnerPair.values()) {
            if (pairList.size() >= 2) {
                // Obter os 4 IDs únicos envolvidos
                Set<Integer> allIds = new HashSet<>();
                for (AdjacentPropertyPair pair : pairList.subList(0, 2)) {
                    allIds.add((int) pair.getPropertyId1());
                    allIds.add((int) pair.getPropertyId2());
                }

                if (allIds.size() < 4) continue;

                // Separar os terrenos por dono
                List<PropertyPolygon> groupA = new ArrayList<>();
                List<PropertyPolygon> groupB = new ArrayList<>();

                for (int id : allIds) {
                    PropertyPolygon prop = propertyMap.get(id);
                    if (prop == null) continue;

                    if (groupA.isEmpty()) {
                        groupA.add(prop);
                    } else if (prop.getOwner().equals(groupA.get(0).getOwner())) {
                        groupA.add(prop);
                    } else {
                        groupB.add(prop);
                    }
                }

                if (groupA.size() != 2 || groupB.size() != 2) continue;

                // Procurar a combinação com áreas mais semelhantes
                double minDiff = Double.MAX_VALUE;
                ExchangeSuggestion bestSuggestion = null;

                for (PropertyPolygon a : groupA) {
                    for (PropertyPolygon b : groupB) {
                        double diff = Math.abs(a.getShapeArea() - b.getShapeArea());
                        if (diff < minDiff) {
                            minDiff = diff;
                            bestSuggestion = new ExchangeSuggestion(a.getObjectId(), b.getObjectId());
                        }
                    }
                }

                if (bestSuggestion != null) {
                    suggestions.add(bestSuggestion);
                }
            }
        }

        return suggestions;
    }
}
