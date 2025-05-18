package Models;

import static org.junit.jupiter.api.Assertions.*;

import io.qameta.allure.Description;
import io.qameta.allure.Feature;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.List;

/**
 * This class contains unit tests for the {@link Municipality} class.
 * It validates the behavior of the Municipality class, including
 * its ability to manage parishes, retrieve all property polygons, and return its name.
 * Each test case is designed to cover specific functionality
 * and edge cases, ensuring the robustness of the Municipality implementation.
 *
 * <p><strong>Author:</strong> Ptome2000</p>
 * <p><strong>Date:</strong> 14/05/2025</p>
 */
@Feature("Object Models")
@DisplayName("Municipality Object Tests")
class MunicipalityTests {

    @Nested
    @DisplayName("Municipality's Construction Tests")
    class ConstructionTests {

        @Test
        @DisplayName("Constructor initializes Municipality with correct name and empty parish list")
        @Description("Ensures that the Municipality constructor sets the name correctly and initializes an empty parishes list.")
        @Severity(SeverityLevel.NORMAL)
        void constructor() {
            Municipality municipality = new Municipality("Municipality1");

            assertEquals("Municipality1", municipality.name(), "Municipality name should be initialized correctly.");
            assertTrue(municipality.getParishes().isEmpty(), "Parishes list should be empty on initialization.");
        }

        @Test
        @DisplayName("name method returns correct municipality name")
        @Description("Ensures that the name method correctly returns the name of the Municipality.")
        @Severity(SeverityLevel.NORMAL)
        void nameMethod() {
            Municipality municipality = new Municipality("Municipality1");

            assertEquals("Municipality1", municipality.name(), "The name method should return the correct municipality name.");
        }
    }

    @Nested
    @DisplayName("Municipality's Parish Management Tests")
    class ParishManagementTests {

        @Test
        @DisplayName("addParish adds a Parish to the list")
        @Description("Ensures that the addParish method adds a Parish to the Municipality's list.")
        @Severity(SeverityLevel.CRITICAL)
        void addParish() {
            Municipality municipality = new Municipality("Municipality1");
            Parish parish = new Parish("Parish1");

            municipality.addParish(parish);

            assertEquals(1, municipality.getParishes().size(), "Parishes list size should be 1 after adding a parish.");
            assertEquals(parish, municipality.getParishes().get(0), "Added Parish should match the one in the list.");
        }

        @Test
        @DisplayName("getParishes returns correct list of parishes")
        @Description("Ensures that the getParishes method returns the correct list of parishes.")
        @Severity(SeverityLevel.NORMAL)
        void getParishes() {
            Municipality municipality = new Municipality("Municipality1");
            Parish parish1 = new Parish("Parish1");
            Parish parish2 = new Parish("Parish2");

            municipality.addParish(parish1);
            municipality.addParish(parish2);

            List<Parish> parishes = municipality.getParishes();

            assertEquals(2, parishes.size(), "getParishes should return a list with 2 parishes.");
            assertTrue(parishes.contains(parish1), "List should contain the first added parish.");
            assertTrue(parishes.contains(parish2), "List should contain the second added parish.");
        }
    }

    @Nested
    @DisplayName("Municipality's Property Polygon Aggregation Tests")
    class PropertyPolygonAggregationTests {

        @Test
        @DisplayName("getAllPropertyPolygons returns all property polygons from all parishes")
        @Description("Ensures that the getAllPropertyPolygons method aggregates property polygons from all parishes.")
        @Severity(SeverityLevel.NORMAL)
        void getAllPropertyPolygons() {
            Municipality municipality = new Municipality("Municipality1");
            Parish parish1 = new Parish("Parish1");
            Parish parish2 = new Parish("Parish2");

            PropertyPolygon property1 = new PropertyPolygon(1, 123.45, "P123", 100.0, 200.0, null, "Owner1", "Parish1", "Municipality1", "Island1");
            PropertyPolygon property2 = new PropertyPolygon(2, 223.45, "P124", 200.0, 300.0, null, "Owner2", "Parish2", "Municipality1", "Island1");

            parish1.addPropertyPolygon(property1);
            parish2.addPropertyPolygon(property2);

            municipality.addParish(parish1);
            municipality.addParish(parish2);

            List<PropertyPolygon> allPolygons = municipality.getAllPropertyPolygons();

            assertEquals(2, allPolygons.size(), "getAllPropertyPolygons should return a list with 2 property polygons.");
            assertTrue(allPolygons.contains(property1), "List should contain the first property's polygon.");
            assertTrue(allPolygons.contains(property2), "List should contain the second property's polygon.");
        }
    }

    @Nested
    @DisplayName("Municipality's Tourism Score Tests")
    class TourismScoreTests {

        @Test
        @DisplayName("setTourismScore sets the tourism score correctly")
        @Description("Ensures that the setTourismScore method updates the tourism score of the municipality.")
        @Severity(SeverityLevel.NORMAL)
        void setTourismScore() {
            Municipality municipality = new Municipality("Municipality1");
            municipality.setTourismScore(8.5);

            assertEquals(8.5, municipality.getTourismScore(), "Tourism score should be set correctly.");
        }

        @Test
        @DisplayName("getTourismScore retrieves the correct tourism score")
        @Description("Ensures that the getTourismScore method retrieves the correct tourism score of the municipality.")
        @Severity(SeverityLevel.NORMAL)
        void getTourismScore() {
            Municipality municipality = new Municipality("Municipality1");
            municipality.setTourismScore(7.2);

            assertEquals(7.2, municipality.getTourismScore(), "Tourism score should be retrieved correctly.");
        }

    }
}