package DetectAdjacentProperties;

import io.qameta.allure.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
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
 *     <li>convertToProperties: 3</li>
 *     <li>findAdjacentProperties: 4</li>
 *     <li>shareVertex: 2</li>
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
    @DisplayName("Convert valid CSV data to properties")
    @Description("Validates that valid CSV data rows are correctly converted to PropertyPolygon objects.")
    @Severity(SeverityLevel.CRITICAL)
    void convertToProperties2() {
        List<String[]> data = TestUtils.createValidCsvData();
        List<PropertyPolygon> properties = AdjacencyDetector.convertToProperties(data);

        assertEquals(2, properties.size(), "Properties list should contain the correct number of elements.");
    }

    @Test
    @DisplayName("Skip invalid CSV rows during conversion")
    @Description("Validates that invalid CSV rows are skipped during the conversion process.")
    @Severity(SeverityLevel.NORMAL)
    void convertToProperties3() {
        List<String[]> data = TestUtils.createInvalidCsvData();
        List<PropertyPolygon> properties = AdjacencyDetector.convertToProperties(data);

        assertEquals(1, properties.size(), "Only valid rows should be converted to properties.");
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
    @DisplayName("Handle duplicate adjacency detection")
    @Description("Validates that duplicate adjacency pairs are not added.")
    @Severity(SeverityLevel.NORMAL)
    void findAdjacentProperties3() {
        List<PropertyPolygon> properties = TestUtils.createDuplicateAdjacentProperties();
        List<AdjacentPropertyPair> pairs = AdjacencyDetector.findAdjacentProperties(properties);

        assertEquals(1, pairs.size(), "Duplicate adjacency pairs should not be added.");
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
}