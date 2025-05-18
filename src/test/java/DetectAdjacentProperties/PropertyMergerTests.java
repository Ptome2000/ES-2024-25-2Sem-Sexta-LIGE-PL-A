package DetectAdjacentProperties;

import static DetectAdjacentProperties.TestUtils.getPropertyPolygons;
import static org.junit.jupiter.api.Assertions.*;

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
import java.util.Arrays;
import java.util.List;

/**
 * This class contains unit tests for the {@link PropertyMerger} class.
 * It validates the behavior of the PropertyMerger class, including
 * merging adjacent properties with the same owner into a single group.
 * Each test case is designed to cover specific functionality and edge cases,
 * ensuring the robustness of the PropertyMerger implementation.
 *
 * <p><strong>Author:</strong> Ptome2000</p>
 * <p><strong>Date:</strong> 17/05/2025</p>
 */
@Feature("Detect adjacent properties")
@DisplayName("Property Merger Tests")
class PropertyMergerTests {

    @Nested
    @DisplayName("Merging Adjacent Properties Tests")
    class MergingAdjacentProperties {

        @Test
        @DisplayName("Single group merge for adjacent properties with same owner")
        @Description("Test merging of adjacent properties with the same owner into a single group.")
        @Severity(SeverityLevel.CRITICAL)
        void mergeOwnerAdjacentPropertiesSingleGroup() {
            PropertyPolygon property1 = new MockedPropertyPolygon(1, 1, "1", 10.0, 100.0,
                    new Polygon(Arrays.asList(new VertexCoordinate(0, 0), new VertexCoordinate(1, 1))),
                    "OwnerA", "FreguesiaA", "MunicipioA", "IlhaA");
            PropertyPolygon property2 = new MockedPropertyPolygon(2, 2, "2", 15.0, 150.0,
                    new Polygon(Arrays.asList(new VertexCoordinate(1, 1), new VertexCoordinate(2, 2))),
                    "OwnerA", "FreguesiaA", "MunicipioA", "IlhaA");

            List<PropertyPolygon> properties = Arrays.asList(property1, property2);
            List<PropertyPolygon> mergedProperties = PropertyMerger.mergeOwnerAdjacentProperties(properties);

            assertEquals(1, mergedProperties.size());
            assertEquals(250.0, mergedProperties.get(0).getShapeArea());
            assertEquals(25.0, mergedProperties.get(0).getShapeLength());
        }

        @Test
        @DisplayName("Multiple groups for adjacent properties with different owners")
        @Description("Test merging of adjacent properties with the same owner into multiple groups.")
        @Severity(SeverityLevel.CRITICAL)
        void mergeOwnerAdjacentPropertiesMultipleGroups() {
            PropertyPolygon property1 = new MockedPropertyPolygon(1, 1, "1", 10.0, 100.0,
                    new Polygon(Arrays.asList(new VertexCoordinate(0, 0), new VertexCoordinate(1, 1))),
                    "OwnerA", "FreguesiaA", "MunicipioA", "IlhaA");

            List<PropertyPolygon> properties = getPropertyPolygons(property1);
            List<PropertyPolygon> mergedProperties = PropertyMerger.mergeOwnerAdjacentProperties(properties);

            assertEquals(2, mergedProperties.size());
        }

    }

    @Nested
    @DisplayName("Non-Adjacent Properties Tests")
    class NonAdjacentProperties {

        @Test
        @DisplayName("No merge for non-adjacent properties with same owner")
        @Description("Test that properties with the same owner but not adjacent are not merged.")
        @Severity(SeverityLevel.NORMAL)
        void noMergeForNonAdjacentProperties() {
            PropertyPolygon property1 = new MockedPropertyPolygon(1, 1, "1", 10.0, 100.0,
                    new Polygon(Arrays.asList(new VertexCoordinate(0, 0), new VertexCoordinate(1, 1))),
                    "OwnerA", "FreguesiaA", "MunicipioA", "IlhaA");
            PropertyPolygon property2 = new MockedPropertyPolygon(2, 2, "2", 15.0, 150.0,
                    new Polygon(Arrays.asList(new VertexCoordinate(2, 2), new VertexCoordinate(3, 3))),
                    "OwnerA", "FreguesiaA", "MunicipioA", "IlhaA");

            List<PropertyPolygon> properties = Arrays.asList(property1, property2);
            List<PropertyPolygon> mergedProperties = PropertyMerger.mergeOwnerAdjacentProperties(properties);

            assertEquals(2, mergedProperties.size());
        }
    }
}