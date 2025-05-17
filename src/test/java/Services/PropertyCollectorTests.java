package Services;

import Models.District;
import Models.Municipality;
import Models.Parish;
import Models.PropertyPolygon;
import Utils.Mocks.MockedDistrict;
import Utils.Mocks.MockedMunicipality;
import Utils.Mocks.MockedParish;
import Utils.Mocks.MockedPropertyPolygon;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
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
 *
 * <p><strong>Cyclomatic Complexity:</strong></p>
 * <ul>
 *     <li>constructor: 1</li>
 *     <li>collectAllProperties: 2</li>
 *     <li>collectAllPropertiesByOwner: 4</li>
 *     <li>collectPropertiesByOwnerAndDistrict: 1</li>
 *     <li>collectPropertiesByOwnerAndMunicipality: 1</li>
 *     <li>collectPropertiesByOwnerAndParish: 1</li>
 *     <li>getOwnerIds: 1</li>
 *     <li>getDistrictNames: 3</li>
 *     <li>filterByDistrict: 3</li>
 *     <li>filterByMunicipality: 3</li>
 *     <li>filterByParish: 3</li>
 *     <li>getParishNames: 3</li>
 *     <li>getMunicipalityNames: 3</li>
 * </ul>
 */
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

    @Test
    @DisplayName("getMunicipalityNames returns correct names for district")
    @Description("Validates that getMunicipalityNames returns all municipality names for a given district.")
    @Severity(SeverityLevel.CRITICAL)
    void testGetMunicipalityNames() {
        List<String> names = collector.getMunicipalityNames("DistrictA");
        assertEquals(List.of("MunicipalityA"), names);
    }

    @Test
    @DisplayName("getParishNames returns correct names for municipality")
    @Description("Validates that getParishNames returns all parish names for a given municipality.")
    @Severity(SeverityLevel.NORMAL)
    void testGetParishNames() {
        List<String> names = collector.getParishNames("MunicipalityA");
        assertTrue(names.containsAll(List.of("ParishA", "ParishB")));
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
    @DisplayName("filterByMunicipality returns properties in municipality")
    @Description("Checks that filterByMunicipality returns all PropertyPolygons in the specified municipality.")
    @Severity(SeverityLevel.NORMAL)
    void testFilterByMunicipality() {
        List<PropertyPolygon> props = collector.filterByMunicipality("MunicipalityA");
        assertTrue(props.contains(property1));
        assertTrue(props.contains(property2));
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

    @Test
    @DisplayName("getDistrictNames returns all district names")
    @Description("Should return all district names")
    @Severity(SeverityLevel.NORMAL)
    void testGetDistrictNames() {
        List<String> names = collector.getDistrictNames();
        assertEquals(1, names.size());
        assertEquals("DistrictA", names.get(0));
    }
}