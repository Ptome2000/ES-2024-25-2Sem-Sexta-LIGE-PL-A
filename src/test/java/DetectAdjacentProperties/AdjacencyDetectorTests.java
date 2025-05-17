package DetectAdjacentProperties;

import Models.Polygon;
import Models.PropertyPolygon;
import Models.VertexCoordinate;
import Utils.Mocks.MockedPropertyPolygon;
import io.qameta.allure.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * This class contains unit tests for the {@link AdjacencyDetector} class.
 * It validates the behavior of the AdjacencyDetector class, including
 * its ability to convert CSV data to property polygons and detect adjacent properties.
 * Each test case is designed to cover specific functionality and edge cases, ensuring
 * the robustness of the AdjacencyDetector implementation.
 *
 * <p><strong>Author:</strong> Ptome2000</p>
 * <p><strong>Date:</strong> 12/04/2025</p>
 *
 * <p><strong>Cyclomatic Complexity:</strong></p>
 * <ul>
 *     <li>convertToProperties: 6</li>
 *     <li>findAdjacentProperties: 8</li>
 *     <li>findValidAdjacentPairs: 11</li>
 *     <li>isValidProperty: 5</li>
 *     <li>shareVertex: 4</li>
 * </ul>
 */
@Feature("Detect adjacent properties")
class AdjacencyDetectorTests {

    @Test
    @DisplayName("Convert empty CSV data to properties")
    @Description("Validates that an empty list of CSV data rows results in an empty list of properties.")
    @Severity(SeverityLevel.MINOR)
    void convertToProperties1() {
        List<String[]> data = new ArrayList<>();
        List<PropertyPolygon> properties = AdjacencyDetector.convertToProperties(data);

        assertNotNull(properties, "Properties list should not be null.");
        assertTrue(properties.isEmpty(), "Properties list should be empty.");
    }

    @Test
    @DisplayName("Find no adjacent properties")
    @Description("Validates that no adjacent properties are found when none share vertices.")
    @Severity(SeverityLevel.NORMAL)
    void findAdjacentProperties1() {
        List<PropertyPolygon> properties = TestUtils.createNonAdjacentProperties();
        List<AdjacentPropertyPair> pairs = AdjacencyDetector.findAdjacentProperties(properties);

        assertTrue(pairs.isEmpty(), "No adjacent properties should be found.");
    }

    @Test
    @DisplayName("Find adjacent properties")
    @Description("Validates that adjacent properties are correctly identified.")
    @Severity(SeverityLevel.CRITICAL)
    void findAdjacentProperties2() {
        List<PropertyPolygon> properties = TestUtils.createAdjacentProperties();
        List<AdjacentPropertyPair> pairs = AdjacencyDetector.findAdjacentProperties(properties);

        assertEquals(1, pairs.size(), "One pair of adjacent properties should be found.");
    }

    @Test
    @DisplayName("Handle self-comparison in adjacency detection")
    @Description("Validates that a property is not considered adjacent to itself.")
    @Severity(SeverityLevel.MINOR)
    void findAdjacentProperties4() {
        List<PropertyPolygon> properties = TestUtils.createSelfComparisonProperties();
        List<AdjacentPropertyPair> pairs = AdjacencyDetector.findAdjacentProperties(properties);

        assertTrue(pairs.isEmpty(), "A property should not be adjacent to itself.");
    }

    @Test
    @DisplayName("Detect shared vertex between properties")
    @Description("Validates that two properties sharing a vertex are detected as adjacent.")
    @Severity(SeverityLevel.CRITICAL)
    void shareVertex1() {
        PropertyPolygon p1 = TestUtils.createPropertyWithVertices(0, 0, 1, 1);
        PropertyPolygon p2 = TestUtils.createPropertyWithVertices(1, 1, 2, 2);

        assertTrue(AdjacencyDetector.shareVertex(p1, p2), "Properties sharing a vertex should be detected as adjacent.");
    }

    @Test
    @DisplayName("Detect no shared vertex between properties")
    @Description("Validates that two properties with no shared vertices are not detected as adjacent.")
    @Severity(SeverityLevel.NORMAL)
    void shareVertex2() {
        PropertyPolygon p1 = TestUtils.createPropertyWithVertices(0, 0, 1, 1);
        PropertyPolygon p2 = TestUtils.createPropertyWithVertices(2, 2, 3, 3);

        assertFalse(AdjacencyDetector.shareVertex(p1, p2), "Properties with no shared vertices should not be detected as adjacent.");
    }

    @Test
    @DisplayName("Convert to properties with null or empty owner")
    @Description("Ensures properties with null or empty owner fields are skipped during conversion.")
    @Severity(SeverityLevel.MINOR)
    void convertToProperties_nullOrEmptyOwner() {
        List<String[]> data = new ArrayList<>();
        data.add(new String[]{"ID", "V1", "V2", "V3", "V4"});
        data.add(new String[]{"1", "0,0", "1,0", "1,1", "0,1", "", "X", "Y", ""});
        List<PropertyPolygon> result = AdjacencyDetector.convertToProperties(data);
        assertTrue(result.isEmpty());
    }

    @Test
    @DisplayName("Convert to properties with NA fields")
    @Description("Ensures properties with NA fields are skipped during conversion.")
    @Severity(SeverityLevel.MINOR)
    void convertToProperties_NAFields() {
        List<String[]> data = new ArrayList<>();
        data.add(new String[]{"ID", "V1", "V2", "V3", "V4"});
        data.add(new String[]{"1", "0,0", "1,0", "1,1", "0,1", "NA", "NA", "NA", "Owner"});
        List<PropertyPolygon> result = AdjacencyDetector.convertToProperties(data);
        assertTrue(result.isEmpty());
    }

    @Test
    @DisplayName("Find adjacent properties with empty list")
    @Description("Checks that no adjacent pairs are found in an empty property list.")
    @Severity(SeverityLevel.MINOR)
    void findAdjacentProperties_emptyList() {
        List<AdjacentPropertyPair> pairs = AdjacencyDetector.findAdjacentProperties(Collections.emptyList());
        assertTrue(pairs.isEmpty());
    }

    @Test
    @DisplayName("isValidProperty handles null cases")
    @Description("Ensures isValidProperty returns false for null or invalid polygons.")
    @Severity(SeverityLevel.NORMAL)
    void isValidProperty_nullCases() throws Exception {
        var method = AdjacencyDetector.class.getDeclaredMethod("isValidProperty", PropertyPolygon.class);
        method.setAccessible(true);

        assertFalse((Boolean) method.invoke(null, (Object) null));

        PropertyPolygon noPolygon = new MockedPropertyPolygon(1, null);
        assertFalse((Boolean) method.invoke(null, noPolygon));

        PropertyPolygon emptyVertices = new MockedPropertyPolygon(1, new Polygon(new ArrayList<>()));
        assertFalse((Boolean) method.invoke(null, emptyVertices));
    }

    @Test
    @DisplayName("isValidProperty handles zero or negative area")
    @Description("Ensures isValidProperty returns false for polygons with zero or negative area.")
    @Severity(SeverityLevel.NORMAL)
    void isValidProperty_zeroOrNegativeArea() throws Exception {
        var method = AdjacencyDetector.class.getDeclaredMethod("isValidProperty", PropertyPolygon.class);
        method.setAccessible(true);

        // Polygon with zero area (all points the same)
        List<VertexCoordinate> vertices = List.of(new VertexCoordinate(0, 0), new VertexCoordinate(0, 0), new VertexCoordinate(0, 0));
        Polygon polygon = new Polygon(vertices);
        PropertyPolygon property = new MockedPropertyPolygon(1, polygon);
        assertFalse((Boolean) method.invoke(null, property));
    }

    @Test
    @DisplayName("shareVertex with empty vertices")
    @Description("Ensures shareVertex returns false when both polygons have no vertices.")
    @Severity(SeverityLevel.MINOR)
    void shareVertex_emptyVertices() {
        PropertyPolygon p1 = new MockedPropertyPolygon(1, new Polygon(new ArrayList<>()));
        PropertyPolygon p2 = new MockedPropertyPolygon(2, new Polygon(new ArrayList<>()));
        assertFalse(AdjacencyDetector.shareVertex(p1, p2));
    }

    @Test
    @DisplayName("shareVertex with null polygons")
    @Description("Ensures shareVertex throws NullPointerException when polygons are null.")
    @Severity(SeverityLevel.NORMAL)
    void shareVertex_nullPolygon() {
        PropertyPolygon p1 = new MockedPropertyPolygon(1, null);
        PropertyPolygon p2 = new MockedPropertyPolygon(2, null);
        assertThrows(NullPointerException.class, () -> AdjacencyDetector.shareVertex(p1, p2));
    }

    @Test
    @DisplayName("Convert to properties skips empty owner")
    @Description("Ensures properties with empty owner are skipped during conversion.")
    @Severity(SeverityLevel.MINOR)
    void convertToProperties_skipsEmptyOwner() {
        List<String[]> data = new ArrayList<>();
        data.add(new String[]{"ID", "V1", "V2", "V3", "V4"});
        data.add(new String[]{"1", "0,0", "1,0", "1,1", "0,1", "", "X", "Y", ""});
        List<PropertyPolygon> result = AdjacencyDetector.convertToProperties(data);
        assertTrue(result.isEmpty());
    }

    @Test
    @DisplayName("Convert to properties skips NA fields")
    @Description("Ensures properties with NA in any field are skipped during conversion.")
    @Severity(SeverityLevel.MINOR)
    void convertToProperties_skipsNAFields() {
        List<String[]> data = new ArrayList<>();
        data.add(new String[]{"ID", "V1", "V2", "V3", "V4"});
        data.add(new String[]{"1", "0,0", "1,0", "1,1", "0,1", "NA", "B", "C", "Owner"});
        data.add(new String[]{"2", "0,0", "1,0", "1,1", "0,1", "A", "NA", "C", "Owner"});
        data.add(new String[]{"3", "0,0", "1,0", "1,1", "0,1", "A", "B", "NA", "Owner"});
        List<PropertyPolygon> result = AdjacencyDetector.convertToProperties(data);
        assertTrue(result.isEmpty());
    }

    @Test
    @DisplayName("Convert to properties skips rows with no vertices")
    @Description("Ensures properties with no vertices are skipped during conversion.")
    @Severity(SeverityLevel.MINOR)
    void convertToProperties_skipsNoVertices() {
        List<String[]> data = new ArrayList<>();
        data.add(new String[]{"ID", "V1", "V2"});
        data.add(new String[]{"1", "", "", "", "A", "B", "C", "Owner"});
        List<PropertyPolygon> result = AdjacencyDetector.convertToProperties(data);
        assertTrue(result.isEmpty());
    }

    @Test
    @DisplayName("Find valid adjacent pairs with different owners")
    @Description("Ensures adjacent properties with different owners are detected.")
    @Severity(SeverityLevel.CRITICAL)
    void findValidAdjacentPairs_adjacentDifferentOwners() {
        PropertyPolygon p1 = TestUtils.createPropertyWithVertices(0, 0, 1, 0, 1, 1, 0, 1);
        PropertyPolygon p2 = TestUtils.createPropertyWithVertices(1, 0, 2, 0, 2, 1, 1, 1);
        p1.setOwner("A");
        p2.setOwner("B");
        List<PropertyPolygon> properties = Arrays.asList(p1, p2);
        List<AdjacentPropertyPair> pairs = AdjacencyDetector.findValidAdjacentPairs(properties);
        assertEquals(1, pairs.size());
    }

    @Test
    @DisplayName("Find valid adjacent pairs with same owner")
    @Description("Ensures adjacent properties with the same owner are not paired.")
    @Severity(SeverityLevel.NORMAL)
    void findValidAdjacentPairs_adjacentSameOwner() {
        PropertyPolygon p1 = TestUtils.createPropertyWithVertices(0, 0, 1, 0, 1, 1, 0, 1);
        PropertyPolygon p2 = TestUtils.createPropertyWithVertices(1, 0, 2, 0, 2, 1, 1, 1);
        p1.setOwner("A");
        p2.setOwner("A");
        List<PropertyPolygon> properties = Arrays.asList(p1, p2);
        List<AdjacentPropertyPair> pairs = AdjacencyDetector.findValidAdjacentPairs(properties);
        assertTrue(pairs.isEmpty());
    }

    @Test
    @DisplayName("Find valid adjacent pairs for non-adjacent properties")
    @Description("Ensures non-adjacent properties are not paired.")
    @Severity(SeverityLevel.NORMAL)
    void findValidAdjacentPairs_nonAdjacentDifferentOwners() {
        PropertyPolygon p1 = TestUtils.createPropertyWithVertices(0, 0, 1, 0, 1, 1, 0, 1);
        PropertyPolygon p2 = TestUtils.createPropertyWithVertices(2, 2, 3, 2, 3, 3, 2, 3);
        p1.setOwner("A");
        p2.setOwner("B");
        List<PropertyPolygon> properties = Arrays.asList(p1, p2);
        List<AdjacentPropertyPair> pairs = AdjacencyDetector.findValidAdjacentPairs(properties);
        assertTrue(pairs.isEmpty());
    }

    @Test
    @DisplayName("Convert to properties handles null rows")
    @Description("Ensures null rows are skipped during conversion.")
    @Severity(SeverityLevel.MINOR)
    void convertToProperties_handlesNullRows() {
        List<String[]> data = new ArrayList<>();
        data.add(new String[]{"ID", "V1", "V2", "V3", "V4"});
        data.add(null); // Null row
        List<PropertyPolygon> result = AdjacencyDetector.convertToProperties(data);
        assertTrue(result.isEmpty());
    }

    @Test
    @DisplayName("Convert to properties handles malformed row")
    @Description("Ensures malformed rows are skipped during conversion.")
    @Severity(SeverityLevel.MINOR)
    void convertToProperties_handlesMalformedRow() {
        List<String[]> data = new ArrayList<>();
        data.add(new String[]{"ID", "V1", "V2", "V3", "V4"});
        data.add(new String[]{"not_an_int", "bad", "row"});
        List<PropertyPolygon> result = AdjacencyDetector.convertToProperties(data);
        assertTrue(result.isEmpty());
    }

    @Test
    @DisplayName("Find valid adjacent pairs with empty list")
    @Description("Ensures no pairs are found in an empty property list.")
    @Severity(SeverityLevel.MINOR)
    void findValidAdjacentPairs_emptyList() {
        List<AdjacentPropertyPair> pairs = AdjacencyDetector.findValidAdjacentPairs(Collections.emptyList());
        assertTrue(pairs.isEmpty());
    }

    @Test
    @DisplayName("Find valid adjacent pairs with all invalid properties")
    @Description("Ensures exception is thrown when all properties are invalid.")
    @Severity(SeverityLevel.NORMAL)
    void findValidAdjacentPairs_allInvalidProperties() {
        PropertyPolygon invalid1 = new MockedPropertyPolygon(1, null);
        PropertyPolygon invalid2 = new MockedPropertyPolygon(2, new Polygon(new ArrayList<>()));
        List<PropertyPolygon> properties = Arrays.asList(invalid1, invalid2);
        assertThrows(NullPointerException.class, () -> AdjacencyDetector.findValidAdjacentPairs(properties));
    }

    @Test
    @DisplayName("shareVertex with identical polygons")
    @Description("Ensures shareVertex returns true for identical polygons.")
    @Severity(SeverityLevel.NORMAL)
    void shareVertex_identicalPolygons() {
        PropertyPolygon p1 = TestUtils.createPropertyWithVertices(0, 0, 1, 0, 1, 1, 0, 1);
        PropertyPolygon p2 = TestUtils.createPropertyWithVertices(0, 0, 1, 0, 1, 1, 0, 1);
        assertTrue(AdjacencyDetector.shareVertex(p1, p2));
    }

    @Test
    @DisplayName("shareVertex with partial overlap")
    @Description("Ensures shareVertex returns true for polygons with partial vertex overlap.")
    @Severity(SeverityLevel.NORMAL)
    void shareVertex_partialOverlap() {
        PropertyPolygon p1 = TestUtils.createPropertyWithVertices(0, 0, 1, 0, 1, 1);
        PropertyPolygon p2 = TestUtils.createPropertyWithVertices(1, 1, 2, 2, 0, 0);
        assertTrue(AdjacencyDetector.shareVertex(p1, p2));
    }

    @Test
    @DisplayName("shareVertex with no overlap")
    @Description("Ensures shareVertex returns false for polygons with no overlapping vertices.")
    @Severity(SeverityLevel.NORMAL)
    void shareVertex_noOverlap() {
        PropertyPolygon p1 = TestUtils.createPropertyWithVertices(0, 0, 1, 0);
        PropertyPolygon p2 = TestUtils.createPropertyWithVertices(2, 2, 3, 3);
        assertFalse(AdjacencyDetector.shareVertex(p1, p2));
    }

}