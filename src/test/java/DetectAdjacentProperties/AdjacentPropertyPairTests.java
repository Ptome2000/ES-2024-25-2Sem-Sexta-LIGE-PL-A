package DetectAdjacentProperties;

import Models.Polygon;
import Models.PropertyPolygon;
import Models.VertexCoordinate;
import Utils.Mocks.MockedPropertyPolygon;
import io.qameta.allure.Description;
import io.qameta.allure.Feature;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * This class contains unit tests for the {@link AdjacentPropertyPair} class.
 * It validates the behavior of the AdjacentPropertyPair class, including
 * its ability to handle property IDs and provide a string representation of the pair.
 * Each test case is designed to cover specific functionality and edge cases, ensuring
 * the robustness of the AdjacentPropertyPair implementation.</p>
 *
 * <p><strong>Author:</strong> Ptome2000</p>
 * <p><strong>Date:</strong> 12/04/2025</p>
 */
@Feature("Detect adjacent properties")
@DisplayName("Adjacent Property Pair Tests")
class AdjacentPropertyPairTests {

    @Nested
    @DisplayName("Adjacent Property Pair Constructor & Getter Tests")
    class ConstructorAndGetterTests {

        @Test
        @DisplayName("Constructor initializes property IDs correctly")
        @Description("Validates that the AdjacentPropertyPair constructor initializes the property IDs correctly.")
        @Severity(SeverityLevel.CRITICAL)
        void constructor() {
            AdjacentPropertyPair pair = new AdjacentPropertyPair(101, 202);

            assertEquals(101, pair.getPropertyId1());
            assertEquals(202, pair.getPropertyId2());
        }

        @Test
        @DisplayName("getPropertyId1 returns correct property ID")
        @Description("Validates that the getPropertyId1 method returns the correct ID of the first property.")
        @Severity(SeverityLevel.NORMAL)
        void getPropertyId1() {
            AdjacentPropertyPair pair = new AdjacentPropertyPair(101, 202);
            assertEquals(101, pair.getPropertyId1());
        }

        @Test
        @DisplayName("getPropertyId2 returns correct property ID")
        @Description("Validates that the getPropertyId2 method returns the correct ID of the second property.")
        @Severity(SeverityLevel.NORMAL)
        void getPropertyId2() {
            AdjacentPropertyPair pair = new AdjacentPropertyPair(101, 202);
            assertEquals(202, pair.getPropertyId2());
        }
    }

    @Test
    @DisplayName("toString returns correct string representation")
    @Description("Validates that the toString method returns the correct string representation of the AdjacentPropertyPair object.")
    @Severity(SeverityLevel.TRIVIAL)
    void toStringTest() {
        AdjacentPropertyPair pair = new AdjacentPropertyPair(101, 202);
        String expected = "AdjacentPropertyPair{propertyId1=101, propertyId2=202}";
        assertEquals(expected, pair.toString());
    }

    @Nested
    @DisplayName("Adjacent Property Pair getOwners Method Tests")
    class GetOwnersTests {

        @Test
        @DisplayName("Returns correct owners for both properties")
        @Description("Validates that getOwners returns the correct owners when both properties are present in the list.")
        @Severity(SeverityLevel.CRITICAL)
        void getOwners_returnsCorrectOwners() {
            PropertyPolygon p1 = new MockedPropertyPolygon(1, "Alice", new Polygon(List.of(new VertexCoordinate(0, 0))));
            PropertyPolygon p2 = new MockedPropertyPolygon(2, "Bob", new Polygon(List.of(new VertexCoordinate(1, 1))));
            List<PropertyPolygon> properties = Arrays.asList(p1, p2);

            AdjacentPropertyPair pair = new AdjacentPropertyPair(1, 2);
            String[] owners = pair.getOwners(properties);

            assertArrayEquals(new String[]{"Alice", "Bob"}, owners);
        }

        @Test
        @DisplayName("Returns null for missing property")
        @Description("Validates that getOwners returns null for an owner if the property is not found in the list.")
        @Severity(SeverityLevel.NORMAL)
        void getOwners_missingProperty() {
            PropertyPolygon p1 = new MockedPropertyPolygon(1, "Alice", new Polygon(List.of(new VertexCoordinate(0, 0))));
            List<PropertyPolygon> properties = Collections.singletonList(p1);

            AdjacentPropertyPair pair = new AdjacentPropertyPair(1, 2);
            String[] owners = pair.getOwners(properties);

            assertArrayEquals(new String[]{"Alice", null}, owners);
        }

        @Test
        @DisplayName("Returns nulls for empty property list")
        @Description("Validates that getOwners returns null for both owners if the property list is empty.")
        @Severity(SeverityLevel.MINOR)
        void getOwners_emptyList() {
            AdjacentPropertyPair pair = new AdjacentPropertyPair(1, 2);
            String[] owners = pair.getOwners(Collections.emptyList());

            assertArrayEquals(new String[]{null, null}, owners);
        }
    }
}