package Services;

import DetectAdjacentProperties.AdjacentPropertyPair;
import Models.ExchangeSuggestion;
import Models.PropertyPolygon;

import java.util.*;

public class SuggestionGenerator {


    /**
     * Generates exchange suggestions based on adjacent property pairs and their properties.
     *
     * @param adjacentPairs The list of adjacent property pairs.
     * @param properties    The list of properties.
     * @return A list of exchange suggestions.
     */
    public static List<ExchangeSuggestion> generateSuggestions(
            List<AdjacentPropertyPair> adjacentPairs,
            List<PropertyPolygon> properties) {

        Map<Integer, PropertyPolygon> propertyMap = mapProperties(properties);
        Map<String, List<AdjacentPropertyPair>> pairsByOwnerPair = groupPairsByOwner(adjacentPairs, propertyMap);
        List<ExchangeSuggestion> suggestions = new ArrayList<>();

        for (List<AdjacentPropertyPair> pairList : pairsByOwnerPair.values()) {
            Optional<ExchangeSuggestion> bestSuggestion = processPairList(pairList, propertyMap);
            bestSuggestion.ifPresent(suggestions::add);
        }

        suggestions.sort(Comparator.comparingDouble(ExchangeSuggestion::getScore).reversed());
        return suggestions;
    }

    /**
     * Maps property IDs to PropertyPolygon objects for quick access.
     *
     * @param properties The list of properties.
     * @return A map where the key is the property ID and the value is the PropertyPolygon object.
     */
    private static Map<Integer, PropertyPolygon> mapProperties(List<PropertyPolygon> properties) {
        Map<Integer, PropertyPolygon> propertyMap = new HashMap<>();
        for (PropertyPolygon p : properties) {
            propertyMap.put(p.getObjectId(), p);
        }
        return propertyMap;
    }

    /**
     * Groups adjacent property pairs by their owners.
     *
     * @param adjacentPairs The list of adjacent property pairs.
     * @param propertyMap   A map of property IDs to PropertyPolygon objects.
     * @return A map where the key is a string representing the owner pair and the value is a list of adjacent property pairs.
     */
    private static Map<String, List<AdjacentPropertyPair>> groupPairsByOwner(
            List<AdjacentPropertyPair> adjacentPairs,
            Map<Integer, PropertyPolygon> propertyMap) {

        Map<String, List<AdjacentPropertyPair>> pairsByOwnerPair = new HashMap<>();

        for (AdjacentPropertyPair pair : adjacentPairs) {
            PropertyPolygon p1 = propertyMap.get((int) pair.getPropertyId1());
            PropertyPolygon p2 = propertyMap.get((int) pair.getPropertyId2());

            if (p1 == null || p2 == null) continue;

            String owner1 = p1.getOwner();
            String owner2 = p2.getOwner();
            String key = owner1.compareTo(owner2) < 0 ? owner1 + "|" + owner2 : owner2 + "|" + owner1;

            pairsByOwnerPair.computeIfAbsent(key, k -> new ArrayList<>()).add(pair);
        }

        return pairsByOwnerPair;
    }

    /**
     * Processes a list of adjacent property pairs to find the best exchange suggestion.
     *
     * @param pairList    The list of adjacent property pairs.
     * @param propertyMap A map of property IDs to PropertyPolygon objects.
     * @return An Optional containing the best exchange suggestion, or empty if no valid suggestion is found.
     */
    private static Optional<ExchangeSuggestion> processPairList(
            List<AdjacentPropertyPair> pairList,
            Map<Integer, PropertyPolygon> propertyMap) {

        if (pairList.size() < 2) return Optional.empty();

        Set<Integer> allIds = new HashSet<>();
        for (AdjacentPropertyPair pair : pairList.subList(0, 2)) {
            allIds.add((int) pair.getPropertyId1());
            allIds.add((int) pair.getPropertyId2());
        }

        if (allIds.size() < 4) return Optional.empty();

        List<PropertyPolygon> groupA = new ArrayList<>();
        List<PropertyPolygon> groupB = new ArrayList<>();

        for (int id : allIds) {
            PropertyPolygon prop = propertyMap.get(id);
            if (prop == null) continue;
            if (groupA.isEmpty() || prop.getOwner().equals(groupA.get(0).getOwner())) {
                groupA.add(prop);
            } else {
                groupB.add(prop);
            }
        }

        if (groupA.size() != 2 || groupB.size() != 2) return Optional.empty();

        return findBestSuggestion(groupA, groupB);
    }

    /**
     * Finds the best suggestion for an exchange between two groups of properties.
     *
     * @param groupA The first group of properties.
     * @param groupB The second group of properties.
     * @return An Optional containing the best suggestion, or empty if no valid suggestion is found.
     */
    private static Optional<ExchangeSuggestion> findBestSuggestion(
            List<PropertyPolygon> groupA, List<PropertyPolygon> groupB) {

        double bestFeasibility = -1;
        ExchangeSuggestion bestSuggestion = null;

        for (PropertyPolygon a : groupA) {
            for (PropertyPolygon b : groupB) {
                double feasibility = calculateAreaFeasibility(a.getShapeArea(), b.getShapeArea());
                if (feasibility < 0.85 || feasibility <= bestFeasibility) continue;

                PropertyPolygon otherA = groupA.get(0).equals(a) ? groupA.get(1) : groupA.get(0);
                PropertyPolygon otherB = groupB.get(0).equals(b) ? groupB.get(1) : groupB.get(0);

                double percentA = calculateNetAreaChange(a.getShapeArea() + otherA.getShapeArea(), b.getShapeArea() + otherA.getShapeArea());
                double percentB = calculateNetAreaChange(b.getShapeArea() + otherB.getShapeArea(), a.getShapeArea() + otherB.getShapeArea());

                ExchangeSuggestion suggestion = new ExchangeSuggestion(a.getObjectId(), b.getObjectId(), feasibility);
                suggestion.setPercentChangeA(percentA);
                suggestion.setPercentChangeB(percentB);
                suggestion.computeValueSimilarity(a, b);

                double equidade = 100.0 - Math.abs(percentA * 100.0 - percentB * 100.0);
                double areaMedia = (a.getShapeArea() + b.getShapeArea()) / 2.0;
                double areaFactor = Math.min(1.0, areaMedia / 1000.0);

                double score = equidade * areaFactor * feasibility;
                suggestion.setScore(score);

                bestSuggestion = suggestion;
                bestFeasibility = feasibility;
            }
        }

        return Optional.ofNullable(bestSuggestion);
    }




    /**
     * Calculates the feasibility of an exchange based on the areas of two properties.
     *
     * @param area1 Area of the first property.
     * @param area2 Area of the second property.
     * @return A value between 0 and 1 representing the feasibility of the exchange.
     */
    public static double calculateAreaFeasibility(double area1, double area2) {
        if (area1 <= 0 || area2 <= 0) return 0.0;
        return 1.0 - (Math.abs(area1 - area2) / Math.max(area1, area2));
    }

    /**
     * Calculates the net area change as a percentage.
     *
     * @param before Area before the exchange.
     * @param after  Area after the exchange.
     * @return The net area change as a percentage.
     */
    private static double calculateNetAreaChange(double before, double after) {
        if (before <= 0) return 0.0;
        return (after - before) / before;
    }
}