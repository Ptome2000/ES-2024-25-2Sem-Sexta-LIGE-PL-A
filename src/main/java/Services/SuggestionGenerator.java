package Services;

import DetectAdjacentProperties.AdjacentPropertyPair;
import Models.ExchangeSuggestion;
import Models.PropertyPolygon;

import java.util.*;

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
                Set<Integer> allIds = new HashSet<>();
                for (AdjacentPropertyPair pair : pairList.subList(0, 2)) {
                    allIds.add((int) pair.getPropertyId1());
                    allIds.add((int) pair.getPropertyId2());
                }

                if (allIds.size() < 4) continue;

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

                double bestFeasibility = -1;
                ExchangeSuggestion bestSuggestion = null;

                for (PropertyPolygon a : groupA) {
                    for (PropertyPolygon b : groupB) {
                        double areafeasibility = calculateFeasibility(a.getShapeArea(), b.getShapeArea());
                        if (areafeasibility < 0.85 || areafeasibility <= bestFeasibility) continue;

                        PropertyPolygon outroDeA = groupA.get(0).equals(a) ? groupA.get(1) : groupA.get(0);
                        PropertyPolygon outroDeB = groupB.get(0).equals(b) ? groupB.get(1) : groupB.get(0);

                        // NOVOS CÁLCULOS DE PERCENTAGEM
                        double beforeA = a.getShapeArea() + outroDeA.getShapeArea();
                        double afterA = b.getShapeArea() + outroDeA.getShapeArea();
                        double percentA = calculateNetAreaChange(beforeA, afterA);

                        double beforeB = b.getShapeArea() + outroDeB.getShapeArea();
                        double afterB = a.getShapeArea() + outroDeB.getShapeArea();
                        double percentB = calculateNetAreaChange(beforeB, afterB);

                        ExchangeSuggestion suggestion = new ExchangeSuggestion(
                                a.getObjectId(), b.getObjectId(), areafeasibility
                        );
                        suggestion.setPercentChangeA(percentA);
                        suggestion.setPercentChangeB(percentB);
                        suggestion.computeValueSimilarity(a, b);

                        double equidade = 100.0 - Math.abs(percentA * 100.0 - percentB * 100.0);

                        double areaMedia = (a.getShapeArea() + b.getShapeArea()) / 2.0;
                        double areaFactor = Math.min(1.0, areaMedia / 1000.0); // até 1000 m² conta a 100%

                        double viabilidadeFactor = areafeasibility;

                        double score = equidade * areaFactor * viabilidadeFactor;

                        suggestion.setScore(score);

                        bestSuggestion = suggestion;
                        bestFeasibility = areafeasibility;
                    }
                }

                if (bestSuggestion != null) {
                    suggestions.add(bestSuggestion);
                }
            }
        }

        suggestions.sort(Comparator.comparingDouble(ExchangeSuggestion::getScore).reversed());
        return suggestions;
    }

    public static double calculateFeasibility(double area1, double area2) {
        if (area1 <= 0 || area2 <= 0) return 0.0;
        return 1.0 - (Math.abs(area1 - area2) / Math.max(area1, area2));
    }

    private static double calculateNetAreaChange(double before, double after) {
        if (before <= 0) return 0.0;
        return (after - before) / before;
    }
}