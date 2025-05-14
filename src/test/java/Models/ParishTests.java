package Models;

import static org.junit.jupiter.api.Assertions.*;

import io.qameta.allure.Description;
import io.qameta.allure.Feature;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import java.util.List;

/**
 * This class contains unit tests for the {@link Parish} class.
 * It validates the behavior of the Parish class, including
 * its ability to manage property polygons, count them, and retrieve them.
 * Each test case is designed to cover specific functionality
 * and edge cases, ensuring the robustness of the Parish implementation.
 *
 * <p><strong>Author:</strong> Ptome2000</p>
 * <p><strong>Date:</strong> 14/05/2025</p>
 *
 * <p><strong>Cyclomatic Complexity:</strong></p>
 * <ul>
 *     <li>Constructor: 1</li>
 *     <li>addPropertyPolygon: 1</li>
 *     <li>getPropertyPolygonCount: 1</li>
 *     <li>getPropertyPolygons: 1</li>
 * </ul>
 */
@Feature("Object Models")
class ParishTests {

    @Test
    @DisplayName("Constructor initializes Parish with correct name and empty property list")
    @Description("Ensures that the Parish constructor sets the name correctly and initializes an empty PropertyPolygons list.")
    @Severity(SeverityLevel.NORMAL)
    void constructor() {
        Parish parish = new Parish("Parish1");

        assertEquals("Parish1", parish.name(), "Parish name should be initialized correctly.");
        assertTrue(parish.getPropertyPolygons().isEmpty(), "PropertyPolygons list should be empty on initialization.");
    }

    @Test
    @DisplayName("addPropertyPolygon adds a PropertyPolygon to the list")
    @Description("Ensures that the addPropertyPolygon method adds a PropertyPolygon to the Parish's list.")
    @Severity(SeverityLevel.CRITICAL)
    void addPropertyPolygon() {
        Parish parish = new Parish("Parish1");
        PropertyPolygon property = new PropertyPolygon(1, 123.45, "P123", 100.0, 200.0, null, "Owner1", "Parish1", "Municipality1", "Island1");

        parish.addPropertyPolygon(property);

        assertEquals(1, parish.getPropertyPolygonCount(), "PropertyPolygon count should be 1 after adding a property.");
        assertEquals(property, parish.getPropertyPolygons().get(0), "Added PropertyPolygon should match the one in the list.");
    }

    @Test
    @DisplayName("getPropertyPolygonCount returns correct count")
    @Description("Ensures that the getPropertyPolygonCount method returns the correct number of PropertyPolygons.")
    @Severity(SeverityLevel.NORMAL)
    void getPropertyPolygonCount() {
        Parish parish = new Parish("Parish1");
        PropertyPolygon property1 = new PropertyPolygon(1, 123.45, "P123", 100.0, 200.0, null, "Owner1", "Parish1", "Municipality1", "Island1");
        PropertyPolygon property2 = new PropertyPolygon(2, 223.45, "P124", 200.0, 300.0, null, "Owner2", "Parish1", "Municipality1", "Island1");

        parish.addPropertyPolygon(property1);
        parish.addPropertyPolygon(property2);

        assertEquals(2, parish.getPropertyPolygonCount(), "PropertyPolygon count should be 2 after adding two properties.");
    }

    @Test
    @DisplayName("getPropertyPolygons returns correct list of PropertyPolygons")
    @Description("Ensures that the getPropertyPolygons method returns the correct list of PropertyPolygons.")
    @Severity(SeverityLevel.NORMAL)
    void getPropertyPolygons() {
        Parish parish = new Parish("Parish1");
        PropertyPolygon property1 = new PropertyPolygon(1, 123.45, "P123", 100.0, 200.0, null, "Owner1", "Parish1", "Municipality1", "Island1");
        PropertyPolygon property2 = new PropertyPolygon(2, 223.45, "P124", 200.0, 300.0, null, "Owner2", "Parish1", "Municipality1", "Island1");

        parish.addPropertyPolygon(property1);
        parish.addPropertyPolygon(property2);

        List<PropertyPolygon> properties = parish.getPropertyPolygons();

        assertEquals(2, properties.size(), "getPropertyPolygons should return a list with 2 properties.");
        assertTrue(properties.contains(property1), "List should contain the first added property.");
        assertTrue(properties.contains(property2), "List should contain the second added property.");
    }

}