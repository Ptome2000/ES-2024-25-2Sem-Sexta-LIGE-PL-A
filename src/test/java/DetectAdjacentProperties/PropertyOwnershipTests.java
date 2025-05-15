package DetectAdjacentProperties;

import static org.junit.jupiter.api.Assertions.*;

import Models.Polygon;
import Models.PropertyPolygon;
import Utils.MockedPropertyPolygon;
import io.qameta.allure.Description;
import io.qameta.allure.Feature;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * This class contains unit tests for the {@link PropertyOwnership} class.
 * It validates the behavior of the PropertyOwnership class, including
 * grouping properties by owner, filtering by owner, and retrieving sorted owner property counts.
 * Each test case is designed to cover specific functionality and edge cases, ensuring
 * the robustness of the PropertyOwnership implementation.
 *
 * <p><strong>Author:</strong> Ptome2000</p>
 * <p><strong>Date:</strong> 15/05/2025</p>
 *
 * <p><strong>Cyclomatic Complexity:</strong></p>
 * <ul>
 *     <li>Constructor: 1</li>
 *     <li>filterByOwner: 1</li>
 *     <li>getOwnerPropertyCounts: 1</li>
 *     <li>groupPropertiesByOwner: 1</li>
 * </ul>
 */
@Feature("Detect adjacent properties")
class PropertyOwnershipTests {

    @Test
    @DisplayName("Groups properties by owner correctly")
    @Description("Ensures properties are grouped by their owner in the map.")
    @Severity(SeverityLevel.CRITICAL)
    void groupsPropertiesByOwner() {
        PropertyPolygon p1 = new MockedPropertyPolygon(1, "Alice", new Polygon(List.of()));
        PropertyPolygon p2 = new MockedPropertyPolygon(2, "Bob", new Polygon(List.of()));
        PropertyPolygon p3 = new MockedPropertyPolygon(3, "Alice", new Polygon(List.of()));
        List<PropertyPolygon> properties = Arrays.asList(p1, p2, p3);

        PropertyOwnership ownership = new PropertyOwnership(properties);

        assertEquals(2, ownership.getOwnerPropertyCounts().size());
        assertTrue(ownership.getOwnerPropertyCounts().stream().anyMatch(e -> e.getKey().equals("Alice") && e.getValue() == 2));
        assertTrue(ownership.getOwnerPropertyCounts().stream().anyMatch(e -> e.getKey().equals("Bob") && e.getValue() == 1));
    }

    @Test
    @DisplayName("filterByOwner returns correct list")
    @Description("Checks that filterByOwner returns all properties for a given owner.")
    @Severity(SeverityLevel.NORMAL)
    void filterByOwner_returnsCorrectList() {
        PropertyPolygon p1 = new MockedPropertyPolygon(1, "Alice", new Polygon(List.of()));
        PropertyPolygon p2 = new MockedPropertyPolygon(2, "Bob", new Polygon(List.of()));
        PropertyPolygon p3 = new MockedPropertyPolygon(3, "Alice", new Polygon(List.of()));
        List<PropertyPolygon> properties = Arrays.asList(p1, p2, p3);

        PropertyOwnership ownership = new PropertyOwnership(properties);

        List<PropertyPolygon> aliceProps = ownership.filterByOwner("Alice");
        assertEquals(2, aliceProps.size());
        assertTrue(aliceProps.contains(p1));
        assertTrue(aliceProps.contains(p3));
    }

    @Test
    @DisplayName("filterByOwner returns empty list for unknown owner")
    @Description("Checks that filterByOwner returns empty list if owner does not exist.")
    @Severity(SeverityLevel.MINOR)
    void filterByOwner_returnsEmptyForUnknown() {
        PropertyPolygon p1 = new MockedPropertyPolygon(1, "Alice", new Polygon(List.of()));
        PropertyOwnership ownership = new PropertyOwnership(List.of(p1));

        List<PropertyPolygon> result = ownership.filterByOwner("NonExistent");
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    @DisplayName("getOwnerPropertyCounts returns sorted list")
    @Description("Ensures getOwnerPropertyCounts returns owners sorted by name.")
    @Severity(SeverityLevel.MINOR)
    void getOwnerPropertyCounts_sorted() {
        PropertyPolygon p1 = new MockedPropertyPolygon(1, "Charlie", new Polygon(List.of()));
        PropertyPolygon p2 = new MockedPropertyPolygon(2, "Alice", new Polygon(List.of()));
        PropertyPolygon p3 = new MockedPropertyPolygon(3, "Bob", new Polygon(List.of()));
        List<PropertyPolygon> properties = Arrays.asList(p1, p2, p3);

        PropertyOwnership ownership = new PropertyOwnership(properties);

        List<Map.Entry<String, Integer>> counts = ownership.getOwnerPropertyCounts();
        List<String> owners = new ArrayList<>();
        for (Map.Entry<String, Integer> entry : counts) {
            owners.add(entry.getKey());
        }
        assertEquals(List.of("Alice", "Bob", "Charlie"), owners);
    }

    @Test
    @DisplayName("Handles empty property list")
    @Description("Checks that class works with an empty property list.")
    @Severity(SeverityLevel.MINOR)
    void handlesEmptyPropertyList() {
        PropertyOwnership ownership = new PropertyOwnership(Collections.emptyList());
        assertTrue(ownership.getOwnerPropertyCounts().isEmpty());
        assertTrue(ownership.filterByOwner("Anyone").isEmpty());
    }

}