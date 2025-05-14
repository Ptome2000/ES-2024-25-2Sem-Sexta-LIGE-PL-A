package Models;

import static org.junit.jupiter.api.Assertions.*;

import io.qameta.allure.Description;
import io.qameta.allure.Feature;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.util.List;

/**
 * This class contains unit tests for the {@link District} class.
 * It validates the behavior of the District class, including
 * its ability to manage municipalities, retrieve all property polygons, and return its name.
 * Each test case is designed to cover specific functionality
 * and edge cases, ensuring the robustness of the District implementation.
 *
 * <p><strong>Author:</strong> Ptome2000</p>
 * <p><strong>Date:</strong> 14/05/2025</p>
 *
 * <p><strong>Cyclomatic Complexity:</strong></p>
 * <ul>
 *     <li>Constructor: 1</li>
 *     <li>addMunicipality: 1</li>
 *     <li>getMunicipalities: 1</li>
 *     <li>getAllPropertyPolygons: 2</li>
 *     <li>name: 1</li>
 * </ul>
 */
@Feature("Object Models")
class DistrictTests {

    @Test
    @DisplayName("Constructor initializes District with correct name and empty municipality list")
    @Description("Ensures that the District constructor sets the name correctly and initializes an empty municipalities list.")
    @Severity(SeverityLevel.NORMAL)
    void constructor() {
        District district = new District("District1");

        assertEquals("District1", district.name(), "District name should be initialized correctly.");
        assertTrue(district.getMunicipalities().isEmpty(), "Municipalities list should be empty on initialization.");
    }

    @Test
    @DisplayName("addMunicipality adds a Municipality to the list")
    @Description("Ensures that the addMunicipality method adds a Municipality to the District's list.")
    @Severity(SeverityLevel.CRITICAL)
    void addMunicipality() {
        District district = new District("District1");
        Municipality municipality = new Municipality("Municipality1");

        district.addMunicipality(municipality);

        assertEquals(1, district.getMunicipalities().size(), "Municipalities list size should be 1 after adding a municipality.");
        assertEquals(municipality, district.getMunicipalities().get(0), "Added Municipality should match the one in the list.");
    }

    @Test
    @DisplayName("getMunicipalities returns correct list of municipalities")
    @Description("Ensures that the getMunicipalities method returns the correct list of municipalities.")
    @Severity(SeverityLevel.NORMAL)
    void getMunicipalities() {
        District district = new District("District1");
        Municipality municipality1 = new Municipality("Municipality1");
        Municipality municipality2 = new Municipality("Municipality2");

        district.addMunicipality(municipality1);
        district.addMunicipality(municipality2);

        List<Municipality> municipalities = district.getMunicipalities();

        assertEquals(2, municipalities.size(), "getMunicipalities should return a list with 2 municipalities.");
        assertTrue(municipalities.contains(municipality1), "List should contain the first added municipality.");
        assertTrue(municipalities.contains(municipality2), "List should contain the second added municipality.");
    }

    @Test
    @DisplayName("getAllPropertyPolygons returns all property polygons from all municipalities")
    @Description("Ensures that the getAllPropertyPolygons method aggregates property polygons from all municipalities.")
    @Severity(SeverityLevel.NORMAL)
    void getAllPropertyPolygons() {
        District district = new District("District1");
        Municipality municipality1 = new Municipality("Municipality1");
        Municipality municipality2 = new Municipality("Municipality2");

        Parish parish1 = new Parish("Parish1");
        Parish parish2 = new Parish("Parish2");

        PropertyPolygon property1 = new PropertyPolygon(1, 123.45, "P123", 100.0, 200.0, null, "Owner1", "Parish1", "Municipality1", "Island1");
        PropertyPolygon property2 = new PropertyPolygon(2, 223.45, "P124", 200.0, 300.0, null, "Owner2", "Parish2", "Municipality2", "Island1");

        parish1.addPropertyPolygon(property1);
        parish2.addPropertyPolygon(property2);

        municipality1.addParish(parish1);
        municipality2.addParish(parish2);

        district.addMunicipality(municipality1);
        district.addMunicipality(municipality2);

        List<PropertyPolygon> allPolygons = district.getAllPropertyPolygons();

        assertEquals(2, allPolygons.size(), "getAllPropertyPolygons should return a list with 2 property polygons.");
        assertTrue(allPolygons.contains(property1), "List should contain the first property's polygon.");
        assertTrue(allPolygons.contains(property2), "List should contain the second property's polygon.");
    }

    @Test
    @DisplayName("name method returns correct district name")
    @Description("Ensures that the name method correctly returns the name of the District.")
    @Severity(SeverityLevel.NORMAL)
    void nameMethod() {
        District district = new District("District1");

        assertEquals("District1", district.name(), "The name method should return the correct district name.");
    }
}