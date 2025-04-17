package DetectAdjacentProperties;

import io.qameta.allure.Description;
import io.qameta.allure.Feature;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * This class contains unit tests for the {@link AdjacentPropertyPair} class.
 * It validates the behavior of the AdjacentPropertyPair class, including
 * its ability to handle property IDs and provide a string representation of the pair.
 * Each test case is designed to cover specific functionality and edge cases, ensuring
 * the robustness of the AdjacentPropertyPair implementation.</p>
 *
 * <p><strong>Author:</strong> Ptome2000</p>
 * <p><strong>Date:</strong> 12/04/2025</p>
 *
 * <p><strong>Cyclomatic Complexity:</strong></p>
 * <ul>
 *     <li>Constructor: 1</li>
 *     <li>getPropertyId1: 1</li>
 *     <li>getPropertyId2: 1</li>
 *     <li>toString: 1</li>
 * </ul>
 */
@Feature("Detect adjacent properties")
class AdjacentPropertyPairTests {

    @Test
    @DisplayName("Constructor initializes property IDs correctly")
    @Description("Validates that the AdjacentPropertyPair constructor initializes the property IDs correctly.")
    @Severity(SeverityLevel.CRITICAL)
    void constructor() {
        AdjacentPropertyPair pair = new AdjacentPropertyPair(101, 202);

        assertEquals(101, pair.getPropertyId1(), "Property ID 1 should be initialized correctly.");
        assertEquals(202, pair.getPropertyId2(), "Property ID 2 should be initialized correctly.");
    }

    @Test
    @DisplayName("getPropertyId1 returns correct property ID")
    @Description("Validates that the getPropertyId1 method returns the correct ID of the first property.")
    @Severity(SeverityLevel.NORMAL)
    void getPropertyId1() {
        AdjacentPropertyPair pair = new AdjacentPropertyPair(101, 202);

        assertEquals(101, pair.getPropertyId1(), "getPropertyId1 should return the correct property ID.");
    }

    @Test
    @DisplayName("getPropertyId2 returns correct property ID")
    @Description("Validates that the getPropertyId2 method returns the correct ID of the second property.")
    @Severity(SeverityLevel.NORMAL)
    void getPropertyId2() {
        AdjacentPropertyPair pair = new AdjacentPropertyPair(101, 202);

        assertEquals(202, pair.getPropertyId2(), "getPropertyId2 should return the correct property ID.");
    }

    @Test
    @DisplayName("toString returns correct string representation")
    @Description("Validates that the toString method returns the correct string representation of the AdjacentPropertyPair object.")
    @Severity(SeverityLevel.TRIVIAL)
    void toStringTest() {
        AdjacentPropertyPair pair = new AdjacentPropertyPair(101, 202);

        String expected = "AdjacentPropertyPair{propertyId1=101, propertyId2=202}";
        assertEquals(expected, pair.toString(), "toString should return the correct string representation.");
    }
}