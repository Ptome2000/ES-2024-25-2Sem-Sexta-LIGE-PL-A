package Services;

import Models.District;
import Models.Municipality;
import Models.Parish;
import Models.PropertyPolygon;
import Utils.Mocks.MockedDistrict;
import Utils.Mocks.MockedMunicipality;
import Utils.Mocks.MockedParish;
import Utils.Mocks.MockedPropertyPolygon;
import io.qameta.allure.Feature;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import io.qameta.allure.Description;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * This class contains unit tests for the {@link PropertyCollector} class.
 * It validates the behavior of the PropertyCollector class, including
 * its ability to collect and filter property data across districts, municipalities, and parishes.
 * Each test case is designed to cover specific functionality and edge cases, ensuring
 * the robustness of the PropertyCollector implementation.
 *
 * <p><strong>Author:</strong> Ptome2000</p>
 * <p><strong>Date:</strong> 15/05/2025</p>
 */
@Feature("Property Collection")
@DisplayName("Property Collector Tests")
class PropertyCollectorTests {

    private PropertyCollector collector;
    private PropertyPolygon property1;
    private PropertyPolygon property2;
    private PropertyPolygon property3;

    @BeforeEach
    void setUp() {
        property1 = new MockedPropertyPolygon(1, "Owner1", null);
        property2 = new MockedPropertyPolygon(2, "Owner2", null);
        property3 = new MockedPropertyPolygon(3, "Owner1", null);

        Parish parishA = new MockedParish("ParishA", List.of(property1));
        Parish parishB = new MockedParish("ParishB", List.of(property2, property3));
        Municipality municipality = new MockedMunicipality("MunicipalityA", List.of(parishA, parishB));
        District district = new MockedDistrict("DistrictA", List.of(municipality));
        collector = new PropertyCollector(List.of(district));
    }

    @Nested
    @DisplayName("Property Collector's District Operations")
    class DistrictTests {

        @Test
        @DisplayName("getDistrictNames returns all district names")
        @Description("Validates that getDistrictNames returns a list of all district names.")
        @Severity(SeverityLevel.NORMAL)
        void testGetDistrictNames() {
            District district1 = new MockedDistrict("DistrictA", List.of());
            District district2 = new MockedDistrict("DistrictB", List.of());
            PropertyCollector localCollector = new PropertyCollector(List.of(district1, district2));

            List<String> districtNames = localCollector.getDistrictNames();

            assertNotNull(districtNames, "District names list should not be null.");
            assertEquals(2, districtNames.size(), "District names list should contain 2 items.");
            assertTrue(districtNames.contains("DistrictA"), "District names should include 'DistrictA'.");
            assertTrue(districtNames.contains("DistrictB"), "District names should include 'DistrictB'.");
        }

        @Test
        @DisplayName("getDistrictNames returns empty list for no districts")
        @Description("Validates that getDistrictNames returns an empty list when no districts are provided.")
        @Severity(SeverityLevel.NORMAL)
        void testGetDistrictNamesEmpty() {
            PropertyCollector localCollector = new PropertyCollector(List.of());

            List<String> districtNames = localCollector.getDistrictNames();

            assertNotNull(districtNames, "District names list should not be null.");
            assertTrue(districtNames.isEmpty(), "District names list should be empty.");
        }

        @Test
        @DisplayName("getDistrictNames handles null district list")
        @Description("Validates that getDistrictNames returns an empty list when the district list is null.")
        @Severity(SeverityLevel.NORMAL)
        void testGetDistrictNamesNull() {
            PropertyCollector localCollector = new PropertyCollector(null);

            List<String> districtNames = localCollector.getDistrictNames();

            assertNotNull(districtNames, "District names list should not be null.");
            assertTrue(districtNames.isEmpty(), "District names list should be empty for null districts.");
        }

        @Test
        @DisplayName("filterByDistrict returns properties in district")
        @Description("Checks that filterByDistrict returns all PropertyPolygons in the specified district.")
        @Severity(SeverityLevel.NORMAL)
        void testFilterByDistrict() {
            List<PropertyPolygon> props = collector.filterByDistrict("DistrictA");
            assertTrue(props.contains(property1));
            assertTrue(props.contains(property2));
        }

        @Test
        @DisplayName("filterByDistrict returns empty list for non-existent district")
        @Description("Validates that filterByDistrict returns an empty list when the district does not exist.")
        @Severity(SeverityLevel.NORMAL)
        void testFilterByNonExistentDistrict() {
            List<PropertyPolygon> props = collector.filterByDistrict("NonExistentDistrict");

            assertNotNull(props, "Property list should not be null.");
            assertTrue(props.isEmpty(), "Property list should be empty for a non-existent district.");
        }
    }

    @Nested
    @DisplayName("Property Collector's Municipality Operations")
    class MunicipalityTests {

        @Test
        @DisplayName("getMunicipalityNames returns correct names for district")
        @Description("Validates that getMunicipalityNames returns all municipality names for a given district.")
        @Severity(SeverityLevel.CRITICAL)
        void testGetMunicipalityNames() {
            List<String> names = collector.getMunicipalityNames("DistrictA");
            assertEquals(List.of("MunicipalityA"), names);
        }

        @Test
        @DisplayName("filterByMunicipality returns properties in municipality")
        @Description("Checks that filterByMunicipality returns all PropertyPolygons in the specified municipality.")
        @Severity(SeverityLevel.NORMAL)
        void testFilterByMunicipality() {
            List<PropertyPolygon> props = collector.filterByMunicipality("MunicipalityA");
            assertTrue(props.contains(property1));
            assertTrue(props.contains(property2));
        }

        @Test
        @DisplayName("filterByMunicipality returns empty list for non-existent municipality")
        @Description("Validates that filterByMunicipality returns an empty list when the municipality does not exist.")
        @Severity(SeverityLevel.NORMAL)
        void testFilterByNonExistentMunicipality() {
            List<PropertyPolygon> props = collector.filterByMunicipality("NonExistentMunicipality");

            assertNotNull(props, "Property list should not be null.");
            assertTrue(props.isEmpty(), "Property list should be empty for a non-existent municipality.");
        }
    }

    @Nested
    @DisplayName("Property Collector's Parish Operations")
    class ParishTests {

        @Test
        @DisplayName("getParishNames returns correct names for municipality")
        @Description("Validates that getParishNames returns all parish names for a given municipality.")
        @Severity(SeverityLevel.NORMAL)
        void testGetParishNames() {
            List<String> names = collector.getParishNames("MunicipalityA");
            assertTrue(names.containsAll(List.of("ParishA", "ParishB")));
        }

        @Test
        @DisplayName("filterByParish returns properties in parish")
        @Description("Checks that filterByParish returns all PropertyPolygons in the specified parish.")
        @Severity(SeverityLevel.NORMAL)
        void testFilterByParish() {
            List<PropertyPolygon> props = collector.filterByParish("ParishA");
            assertEquals(List.of(property1), props);
        }

        @Test
        @DisplayName("filterByParish returns empty list for non-existent parish")
        @Description("Validates that filterByParish returns an empty list when the parish does not exist.")
        @Severity(SeverityLevel.NORMAL)
        void testFilterByNonExistentParish() {
            List<PropertyPolygon> props = collector.filterByParish("NonExistentParish");

            assertNotNull(props, "Property list should not be null.");
            assertTrue(props.isEmpty(), "Property list should be empty for a non-existent parish.");
        }

        @Test
        @DisplayName("filterByParish throws exception for null input")
        @Description("Ensures IllegalArgumentException is thrown when parish name is null.")
        @Severity(SeverityLevel.CRITICAL)
        void testFilterByParishNullInput() {
            assertThrows(IllegalArgumentException.class, () -> collector.filterByParish(null),
                    "Expected IllegalArgumentException for null parish name.");
        }
    }

    @Nested
    @DisplayName("Property Collector's Property Collecting tests")
    class PropertyCollectionTests {

        @Test
        @DisplayName("collectAllProperties returns all properties")
        @Description("Should return all PropertyPolygon objects from all districts")
        @Severity(SeverityLevel.CRITICAL)
        void testCollectAllProperties() {
            List<PropertyPolygon> all = collector.collectAllProperties();
            assertEquals(3, all.size());
            assertTrue(all.contains(property1));
            assertTrue(all.contains(property2));
            assertTrue(all.contains(property3));
        }

        @Test
        @DisplayName("collectAllPropertiesByOwner returns only properties of given owner")
        @Description("Should return only properties owned by the specified owner")
        @Severity(SeverityLevel.NORMAL)
        void testCollectAllPropertiesByOwner() {
            List<PropertyPolygon> owner1Props = collector.collectAllPropertiesByOwner("Owner1");
            assertEquals(2, owner1Props.size());
            assertTrue(owner1Props.contains(property1));
            assertTrue(owner1Props.contains(property3));
            assertFalse(owner1Props.contains(property2));
        }

        @Test
        @DisplayName("collectPropertiesByOwnerAndDistrict returns correct properties")
        @Description("Should return properties for owner in specified district")
        @Severity(SeverityLevel.NORMAL)
        void testCollectPropertiesByOwnerAndDistrict() {
            List<PropertyPolygon> props = collector.collectPropertiesByOwnerAndDistrict("Owner1", "DistrictA");
            assertEquals(2, props.size());
            assertTrue(props.contains(property1));
            assertTrue(props.contains(property3));
        }

        @Test
        @DisplayName("collectPropertiesByOwnerAndMunicipality returns correct properties")
        @Description("Should return properties for owner in specified municipality")
        @Severity(SeverityLevel.NORMAL)
        void testCollectPropertiesByOwnerAndMunicipality() {
            List<PropertyPolygon> props = collector.collectPropertiesByOwnerAndMunicipality("Owner1", "MunicipalityA");
            assertEquals(2, props.size());
            assertTrue(props.contains(property1));
            assertTrue(props.contains(property3));
        }

        @Test
        @DisplayName("collectPropertiesByOwnerAndParish returns correct properties")
        @Description("Should return properties for owner in specified parish")
        @Severity(SeverityLevel.NORMAL)
        void testCollectPropertiesByOwnerAndParish() {
            List<PropertyPolygon> propsA = collector.collectPropertiesByOwnerAndParish("Owner1", "ParishA");
            assertEquals(1, propsA.size());
            assertTrue(propsA.contains(property1));

            List<PropertyPolygon> propsB = collector.collectPropertiesByOwnerAndParish("Owner1", "ParishB");
            assertEquals(1, propsB.size());
            assertTrue(propsB.contains(property3));
        }

        @Test
        @DisplayName("getOwnerIds returns all unique owner ids")
        @Description("Should return all unique owner ids in the data")
        @Severity(SeverityLevel.NORMAL)
        void testGetOwnerIds() {
            List<String> owners = collector.getOwnerIds();
            assertEquals(2, owners.size());
            assertTrue(owners.contains("Owner1"));
            assertTrue(owners.contains("Owner2"));
        }
    }

    @Nested
    @DisplayName("Property Collector's Invalid Input Handling")
    class InvalidInputTests {

        @Test
        @DisplayName("Throws exception for null or blank input")
        @Description("Ensures IllegalArgumentException is thrown for null or blank input parameters.")
        @Severity(SeverityLevel.CRITICAL)
        void testNullOrBlankInput() {
            assertThrows(IllegalArgumentException.class, () -> collector.getMunicipalityNames(null));
            assertThrows(IllegalArgumentException.class, () -> collector.getMunicipalityNames(" "));
            assertThrows(IllegalArgumentException.class, () -> collector.getParishNames(null));
            assertThrows(IllegalArgumentException.class, () -> collector.filterByDistrict(""));
            assertThrows(IllegalArgumentException.class, () -> collector.filterByMunicipality(null));
            assertThrows(IllegalArgumentException.class, () -> collector.filterByParish(""));
        }
    }

}