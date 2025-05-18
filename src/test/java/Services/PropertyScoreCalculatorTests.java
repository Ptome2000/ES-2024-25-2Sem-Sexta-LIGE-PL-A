package Services;

import static org.junit.jupiter.api.Assertions.*;

import Models.District;
import Models.Municipality;
import Models.Parish;
import Models.Polygon;
import Models.PropertyPolygon;
import Utils.Mocks.MockedDistrict;
import Utils.Mocks.MockedMunicipality;
import Utils.Mocks.MockedParish;
import Utils.Mocks.MockedPropertyPolygon;
import io.qameta.allure.Description;
import io.qameta.allure.Feature;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import java.util.List;

/**
 * This class contains unit tests for the {@link PropertyScoreCalculator} class.
 * It validates the behavior of the PropertyScoreCalculator class, including
 * its ability to assign normalized tourism and urbanization scores to regions.
 * Each test case is designed to cover specific functionality and edge cases,
 * ensuring the robustness of the PropertyScoreCalculator implementation.
 *
 * <p><strong>Author:</strong> Ptome2000</p>
 * <p><strong>Date:</strong> 17/05/2025</p>
 */
@DisplayName("Score Calculator for Properties Tests")
@Feature("Property Suggestions")
class PropertyScoreCalculatorTests {

    @Test
    @DisplayName("Should assign normalized tourism and urbanization scores to regions")
    @Description("Verifies that tourism and urbanization scores are correctly normalized and assigned to municipalities, parishes, and properties.")
    @Severity(SeverityLevel.NORMAL)
    void assignScoresToRegions() {
        Polygon polygon1 = new Polygon(List.of());
        Polygon polygon2 = new Polygon(List.of());
        Polygon polygon3 = new Polygon(List.of());

        MockedPropertyPolygon monument1 = new MockedPropertyPolygon(-1, 100.0, "ParNum1", 0.0, 0.0, polygon1, "OwnerA", "Parish1", "Funchal", "Madeira");
        MockedPropertyPolygon property1 = new MockedPropertyPolygon(1, 200.0, "ParNum2", 0.0, 0.0, polygon2, "OwnerB", "Parish1", "Funchal", "Madeira");
        MockedPropertyPolygon property2 = new MockedPropertyPolygon(2, 300.0, "ParNum3", 0.0, 0.0, polygon3, "OwnerC", "Parish2", "Funchal", "Madeira");

        Parish parish1 = new MockedParish("Parish1", List.of(monument1, property1));
        Parish parish2 =  new MockedParish("Parish2", List.of(property2));
        Municipality municipality = new MockedMunicipality("Funchal", List.of(parish1, parish2));
        District district = new MockedDistrict("Madeira", List.of(municipality));

        List<District> districts = List.of(district);
        List<PropertyPolygon> allProperties = List.of(monument1, property1, property2);

        PropertyScoreCalculator.assignScoresToRegions(districts, allProperties);

        assertEquals(1.0, municipality.getTourismScore(), 0.001);
        assertEquals(1.0, parish1.getUrbanizationScore(), 0.001);
        assertEquals(0.5, parish2.getUrbanizationScore(), 0.001);

        assertEquals(1.0, property1.getTourismScore(), 0.001);
        assertEquals(1.0, property1.getUrbanizationScore(), 0.001);
        assertEquals(0.5, property2.getUrbanizationScore(), 0.001);
    }

    @Test
    @DisplayName("Should handle empty districts and properties without errors")
    @Description("Ensures that the method handles empty input data without throwing exceptions.")
    @Severity(SeverityLevel.NORMAL)
    void assignScoresToRegionsWithEmptyData() {
        List<District> districts = List.of();
        List<PropertyPolygon> allProperties = List.of();

        assertDoesNotThrow(() -> PropertyScoreCalculator.assignScoresToRegions(districts, allProperties));
    }

}