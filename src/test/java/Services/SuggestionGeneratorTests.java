package Services;

import static org.junit.jupiter.api.Assertions.*;

import DetectAdjacentProperties.AdjacentPropertyPair;
import Models.ExchangeSuggestion;
import Models.PropertyPolygon;
import Utils.Mocks.MockedPropertyPolygon;
import io.qameta.allure.Description;
import io.qameta.allure.Feature;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * This class contains unit tests for the {@link SuggestionGenerator} class.
 * It validates the behavior of the SuggestionGenerator class, including
 * its ability to generate property exchange suggestions based on adjacency
 * and ownership. Each test case is designed to cover specific functionality
 * and edge cases, ensuring the robustness of the SuggestionGenerator implementation.
 *
 * <p><strong>Author:</strong> Ptome2000</p>
 * <p><strong>Date:</strong> 17/05/2025</p>
 */
@DisplayName("Suggestion Generator Tests")
@Feature("Property Suggestions")
class SuggestionGeneratorTests {

    @Nested
    @DisplayName("Suggestion Generator's Mapping Tests")
    class MappingTests {
        @Test
        @DisplayName("Should map properties correctly")
        @Description("Verifies that the mapProperties method correctly maps property IDs to PropertyPolygon objects.")
        @Severity(SeverityLevel.NORMAL)
        void mapPropertiesTest() {
            MockedPropertyPolygon property1 = new MockedPropertyPolygon(1, 100.0, "ParNum1", 0.0, 50.0, null, "OwnerA", "Parish1", "Funchal", "Madeira");
            MockedPropertyPolygon property2 = new MockedPropertyPolygon(2, 200.0, "ParNum2", 0.0, 50.0, null, "OwnerB", "Parish1", "Funchal", "Madeira");

            List<PropertyPolygon> properties = List.of(property1, property2);
            Map<Integer, PropertyPolygon> propertyMap = SuggestionGenerator.mapProperties(properties);

            assertEquals(2, propertyMap.size());
            assertEquals(property1, propertyMap.get(1));
            assertEquals(property2, propertyMap.get(2));
        }
    }

    @Nested
    @DisplayName("Suggestion Generator's Grouping Tests")
    class GroupingTests {
        @Test
        @DisplayName("Should group pairs by owner correctly")
        @Description("Verifies that adjacent property pairs are grouped correctly by their owners.")
        @Severity(SeverityLevel.MINOR)
        void groupPairsByOwnerTest() {
            MockedPropertyPolygon property1 = new MockedPropertyPolygon(1, 100.0, "ParNum1", 0.0, 50.0, null, "OwnerA", "Parish1", "Funchal", "Madeira");
            MockedPropertyPolygon property2 = new MockedPropertyPolygon(2, 200.0, "ParNum2", 0.0, 50.0, null, "OwnerB", "Parish1", "Funchal", "Madeira");
            MockedPropertyPolygon property3 = new MockedPropertyPolygon(3, 300.0, "ParNum3", 0.0, 50.0, null, "OwnerA", "Parish1", "Funchal", "Madeira");

            AdjacentPropertyPair pair1 = new AdjacentPropertyPair(1, 2);
            AdjacentPropertyPair pair2 = new AdjacentPropertyPair(1, 3);
            List<AdjacentPropertyPair> adjacentPairs = List.of(pair1, pair2);
            List<PropertyPolygon> properties = List.of(property1, property2, property3);

            Map<String, List<AdjacentPropertyPair>> groupedPairs = SuggestionGenerator.groupPairsByOwner(adjacentPairs, SuggestionGenerator.mapProperties(properties));

            assertEquals(2, groupedPairs.size());
            assertTrue(groupedPairs.containsKey("OwnerA|OwnerB"));
            assertTrue(groupedPairs.containsKey("OwnerA|OwnerA"));
        }

        @Test
        @DisplayName("Should group pairs by owner correctly")
        @Description("Verifies that adjacent property pairs are grouped correctly by their owners.")
        @Severity(SeverityLevel.MINOR)
        void groupPairsByOwner() {
            MockedPropertyPolygon property1 = new MockedPropertyPolygon(1, 100.0, "ParNum1", 0.0, 50.0, null, "OwnerA", "Parish1", "Funchal", "Madeira");
            MockedPropertyPolygon property2 = new MockedPropertyPolygon(2, 200.0, "ParNum2", 0.0, 50.0, null, "OwnerB", "Parish1", "Funchal", "Madeira");
            MockedPropertyPolygon property3 = new MockedPropertyPolygon(3, 300.0, "ParNum3", 0.0, 50.0, null, "OwnerA", "Parish1", "Funchal", "Madeira");

            AdjacentPropertyPair pair1 = new AdjacentPropertyPair(1, 2);
            AdjacentPropertyPair pair2 = new AdjacentPropertyPair(1, 3);
            List<AdjacentPropertyPair> adjacentPairs = List.of(pair1, pair2);
            List<PropertyPolygon> properties = List.of(property1, property2, property3);

            Map<String, List<AdjacentPropertyPair>> groupedPairs = SuggestionGenerator.groupPairsByOwner(adjacentPairs, SuggestionGenerator.mapProperties(properties));

            assertEquals(2, groupedPairs.size());
            assertTrue(groupedPairs.containsKey("OwnerA|OwnerB"));
            assertTrue(groupedPairs.containsKey("OwnerA|OwnerA"));
        }
    }

    @Nested
    @DisplayName("Suggestion Generator's Feasibility Calculation Tests")
    class FeasibilityCalculationTests {
        @Test
        @DisplayName("Should calculate area feasibility correctly")
        @Description("Verifies that the calculateAreaFeasibility method calculates feasibility based on property areas.")
        @Severity(SeverityLevel.NORMAL)
        void calculateAreaFeasibilityTest() {
            double feasibility = SuggestionGenerator.calculateAreaFeasibility(100.0, 90.0);
            assertEquals(0.9, feasibility, 0.001);

            feasibility = SuggestionGenerator.calculateAreaFeasibility(100.0, 50.0);
            assertEquals(0.5, feasibility, 0.001);

            feasibility = SuggestionGenerator.calculateAreaFeasibility(100.0, 0.0);
            assertEquals(0.0, feasibility, 0.001);
        }

        @Test
        @DisplayName("Should calculate net area change correctly")
        @Description("Verifies that the calculateNetAreaChange method calculates the percentage change in area.")
        @Severity(SeverityLevel.NORMAL)
        void calculateNetAreaChangeTest() {
            double netChange = SuggestionGenerator.calculateNetAreaChange(100.0, 120.0);
            assertEquals(0.2, netChange, 0.001);

            netChange = SuggestionGenerator.calculateNetAreaChange(100.0, 80.0);
            assertEquals(-0.2, netChange, 0.001);

            netChange = SuggestionGenerator.calculateNetAreaChange(0.0, 80.0);
            assertEquals(0.0, netChange, 0.001);
        }
    }

    @Nested
    @DisplayName("Suggestion Generator's Edge Case Tests")
    class EdgeCaseTests {
        @Test
        @DisplayName("Should handle empty input without errors")
        @Description("Ensures that the SuggestionGenerator handles empty lists of properties and adjacent pairs without throwing exceptions.")
        @Severity(SeverityLevel.NORMAL)
        void handleEmptyInput() {
            List<AdjacentPropertyPair> adjacentPairs = List.of();
            List<PropertyPolygon> properties = List.of();

            List<ExchangeSuggestion> suggestions = SuggestionGenerator.generateSuggestions(adjacentPairs, properties);

            assertTrue(suggestions.isEmpty());
        }
    }

    @Nested
    @DisplayName("Suggestion Generator's Processing Tests")
    class SuggestionProcessingTests {
        @Test
        @DisplayName("Should process pair list and return the best suggestion")
        @Description("Verifies that the processPairList method identifies and returns the best exchange suggestion from a list of adjacent property pairs.")
        @Severity(SeverityLevel.CRITICAL)
        void processPairListTest() {
            MockedPropertyPolygon property1 = new MockedPropertyPolygon(1, 100.0, "ParNum1", 0.0, 50.0, null, "OwnerA", "Parish1", "Funchal", "Madeira");
            MockedPropertyPolygon property2 = new MockedPropertyPolygon(2, 200.0, "ParNum2", 0.0, 50.0, null, "OwnerB", "Parish1", "Funchal", "Madeira");
            MockedPropertyPolygon property3 = new MockedPropertyPolygon(3, 150.0, "ParNum3", 0.0, 50.0, null, "OwnerA", "Parish1", "Funchal", "Madeira");
            MockedPropertyPolygon property4 = new MockedPropertyPolygon(4, 180.0, "ParNum4", 0.0, 50.0, null, "OwnerB", "Parish1", "Funchal", "Madeira");

            AdjacentPropertyPair pair1 = new AdjacentPropertyPair(1, 2);
            AdjacentPropertyPair pair2 = new AdjacentPropertyPair(3, 4);
            List<AdjacentPropertyPair> pairList = List.of(pair1, pair2);

            Map<Integer, PropertyPolygon> propertyMap = Map.of(
                    1, property1,
                    2, property2,
                    3, property3,
                    4, property4
            );

            Optional<ExchangeSuggestion> suggestion = SuggestionGenerator.processPairList(pairList, propertyMap);

            assertTrue(suggestion.isPresent());
            assertEquals(1, suggestion.get().getPropertyFromA());
            assertEquals(2, suggestion.get().getPropertyFromB());
        }

        @Test
        @DisplayName("Should return empty for invalid pair list in processPairList")
        @Description("Ensures that the processPairList method returns an empty result when provided with an invalid or incomplete pair list.")
        @Severity(SeverityLevel.NORMAL)
        void processPairListInvalidTest() {
            MockedPropertyPolygon property1 = new MockedPropertyPolygon(1, 100.0, "ParNum1", 0.0, 50.0, null, "OwnerA", "Parish1", "Funchal", "Madeira");

            AdjacentPropertyPair pair1 = new AdjacentPropertyPair(1, 2);
            List<AdjacentPropertyPair> pairList = List.of(pair1);

            Map<Integer, PropertyPolygon> propertyMap = Map.of(1, property1);

            Optional<ExchangeSuggestion> suggestion = SuggestionGenerator.processPairList(pairList, propertyMap);

            assertTrue(suggestion.isEmpty());
        }

        @Test
        @DisplayName("Should find the best suggestion between two groups")
        @Description("Verifies that the findBestSuggestion method identifies the best exchange suggestion between two groups of properties.")
        @Severity(SeverityLevel.CRITICAL)
        void findBestSuggestionTest() {
            MockedPropertyPolygon property1 = new MockedPropertyPolygon(1, 100.0, "ParNum1", 0.0, 50.0, null, "OwnerA", "Parish1", "Funchal", "Madeira");
            MockedPropertyPolygon property2 = new MockedPropertyPolygon(2, 200.0, "ParNum2", 0.0, 50.0, null, "OwnerB", "Parish1", "Funchal", "Madeira");
            MockedPropertyPolygon property3 = new MockedPropertyPolygon(3, 150.0, "ParNum3", 0.0, 50.0, null, "OwnerA", "Parish1", "Funchal", "Madeira");
            MockedPropertyPolygon property4 = new MockedPropertyPolygon(4, 180.0, "ParNum4", 0.0, 50.0, null, "OwnerB", "Parish1", "Funchal", "Madeira");

            List<PropertyPolygon> groupA = List.of(property1, property3);
            List<PropertyPolygon> groupB = List.of(property2, property4);

            Optional<ExchangeSuggestion> suggestion = SuggestionGenerator.findBestSuggestion(groupA, groupB);

            assertTrue(suggestion.isPresent(), "A suggestion should be present.");
            assertEquals(1, suggestion.get().getPropertyFromA(), "PropertyFromA should match property1.");
            assertEquals(2, suggestion.get().getPropertyFromB(), "PropertyFromB should match property2.");
        }

        @Test
        @DisplayName("Should return empty for invalid groups in findBestSuggestion")
        @Description("Ensures that the findBestSuggestion method returns an empty result when provided with invalid or incomplete groups.")
        @Severity(SeverityLevel.NORMAL)
        void findBestSuggestionInvalidTest() {
            MockedPropertyPolygon property1 = new MockedPropertyPolygon(1, 100.0, "ParNum1", 0.0, 50.0, null, "OwnerA", "Parish1", "Funchal", "Madeira");

            List<PropertyPolygon> groupA = List.of(property1);
            List<PropertyPolygon> groupB = List.of();

            Optional<ExchangeSuggestion> suggestion = SuggestionGenerator.findBestSuggestion(groupA, groupB);

            assertTrue(suggestion.isEmpty());
        }

        @Test
        @DisplayName("Should process all pairs by owner and add valid suggestions")
        @Description("Validates that the loop in generateSuggestions processes all pairs by owner and adds valid suggestions to the list.")
        @Severity(SeverityLevel.CRITICAL)
        void generateSuggestions() {
            MockedPropertyPolygon property1 = new MockedPropertyPolygon(1, 100.0, "ParNum1", 0.0, 50.0, null, "OwnerA", "Parish1", "Funchal", "Madeira");
            MockedPropertyPolygon property2 = new MockedPropertyPolygon(2, 200.0, "ParNum2", 0.0, 50.0, null, "OwnerB", "Parish1", "Funchal", "Madeira");
            MockedPropertyPolygon property3 = new MockedPropertyPolygon(3, 150.0, "ParNum3", 0.0, 50.0, null, "OwnerA", "Parish1", "Funchal", "Madeira");
            MockedPropertyPolygon property4 = new MockedPropertyPolygon(4, 180.0, "ParNum4", 0.0, 50.0, null, "OwnerB", "Parish1", "Funchal", "Madeira");

            AdjacentPropertyPair pair1 = new AdjacentPropertyPair(property1.getObjectId(), property2.getObjectId());
            AdjacentPropertyPair pair2 = new AdjacentPropertyPair(property3.getObjectId(), property4.getObjectId());

            List<AdjacentPropertyPair> adjacentPairs = List.of(pair1, pair2);
            List<PropertyPolygon> properties = List.of(property1, property2, property3, property4);
            List<ExchangeSuggestion> suggestions = SuggestionGenerator.generateSuggestions(adjacentPairs, properties);

            assertEquals(1, suggestions.size(), "The loop should process all pairs and generate two suggestions.");
            assertTrue(suggestions.stream().anyMatch(s -> s.getPropertyFromA() == property1.getObjectId() && s.getPropertyFromB() == property2.getObjectId()), "Suggestion for pair1 should be present.");
        }
    }
}
