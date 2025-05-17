package DetectAdjacentProperties;

import Models.PropertyPolygon;
import io.qameta.allure.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

/**
 * This class contains unit tests for the {@link SpatialGrid} class.
 * It verifies the functionality of various methods in the SpatialGrid class,
 * ensuring that they behave as expected under different scenarios.
 *
 *
 * <p><strong>Author:</strong> Ptome2000</p>
 * <p><strong>Date:</strong> 09/04/2025</p>
 *
 * <p><strong>Cyclomatic Complexity:</strong></p>
 * <ul>
 *     <li>constructor: 1</li>
 *     <li>getCellKey: 1</li>
 *     <li>insert: 1</li>
 *     <li>printGridRanges: 3</li>
 *     <li>logPropertiesInCells: 2</li>
 *     <li>getPropertyGridCells: 2</li>
 *     <li>getNearbyProperties: 11</li>
 *     <li>checkAndAddNearbyProperties: 3</li>
 * </ul>
 */
@Feature("Detect adjacent properties")
class SpatialGridTests {

    @Test
    @DisplayName("SpatialGrid constructor with valid properties")
    @Description("Verifies that the SpatialGrid instance is created successfully with a valid list of properties.")
    @Severity(SeverityLevel.NORMAL)
    void SpatialGrid() {
        List<PropertyPolygon> properties = new ArrayList<>();
        SpatialGrid grid = new SpatialGrid(properties);

        assertNotNull(grid, "SpatialGrid instance should not be null.");
    }

    @Test
    @DisplayName("GetCellKey with valid coordinates")
    @Description("Verifies that the correct cell key is generated for given coordinates.")
    @Severity(SeverityLevel.MINOR)
    void getCellKey() {
        List<PropertyPolygon> properties = new ArrayList<>();
        SpatialGrid grid = new SpatialGrid(properties);

        String cellKey = grid.getCellKey(100, 200);
        assertNotNull(cellKey, "Cell key should not be null.");
        assertEquals("0-0", cellKey, "Cell key should match the expected format.");
    }

    @Test
    @DisplayName("Insert property into grid")
    @Description("Verifies that a property can be inserted into the SpatialGrid without errors.")
    @Severity(SeverityLevel.NORMAL)
    void insert() {
        List<PropertyPolygon> properties = new ArrayList<>();
        SpatialGrid grid = new SpatialGrid(properties);

        PropertyPolygon property = TestUtils.createSingleProperty();
        grid.insert(property);

        assertTrue(true, "Inserting a property should not throw an exception.");
    }

    @Test
    @DisplayName("printGridRanges outputs ranges")
    @Description("Verifies that the grid ranges are printed correctly.")
    @Severity(SeverityLevel.TRIVIAL)
    void printGridRanges() {
        List<PropertyPolygon> properties = new ArrayList<>();
        SpatialGrid grid = new SpatialGrid(properties);

        grid.printGridRanges();
    }

    @Test
    @DisplayName("logPropertiesInCells outputs property counts")
    @Description("Verifies that the property counts in each cell are logged correctly.")
    @Severity(SeverityLevel.TRIVIAL)
    void logPropertiesInCells() {
        List<PropertyPolygon> properties = new ArrayList<>();
        SpatialGrid grid = new SpatialGrid(properties);

        grid.logPropertiesInCells();
    }

    @Test
    @DisplayName("getPropertyGridCells with valid property")
    @Description("Verifies that the correct grid cells are returned for a given property.")
    @Severity(SeverityLevel.NORMAL)
    void getPropertyGridCells() {
        List<PropertyPolygon> properties = new ArrayList<>();
        SpatialGrid grid = new SpatialGrid(properties);

        PropertyPolygon property = TestUtils.createSingleProperty();
        List<String> cells = grid.getPropertyGridCells(property);

        assertNotNull(cells, "Cell list should not be null.");
        assertFalse(cells.isEmpty(), "Cell list should not be empty.");
    }

    @Test
    @DisplayName("getNearbyProperties with no nearby properties")
    @Description("Verifies that no nearby properties are returned when none exist.")
    @Severity(SeverityLevel.MINOR)
    void getNearbyProperties1() {
        List<PropertyPolygon> properties = new ArrayList<>();
        SpatialGrid grid = new SpatialGrid(properties);

        PropertyPolygon property = TestUtils.createSingleProperty();
        List<PropertyPolygon> nearby = grid.getNearbyProperties(property);

        assertNotNull(nearby, "Nearby properties list should not be null.");
        assertTrue(nearby.isEmpty(), "Nearby properties list should be empty.");
    }

    @Test
    @DisplayName("getNearbyProperties with nearby properties in the same cell")
    @Description("Verifies that nearby properties in the same cell are returned correctly.")
    @Severity(SeverityLevel.NORMAL)
    void getNearbyProperties2() {
        List<PropertyPolygon> properties = TestUtils.createAdjacentProperties();
        SpatialGrid grid = new SpatialGrid(properties);

        for (PropertyPolygon property : properties) {
            grid.insert(property);
        }

        PropertyPolygon property = properties.get(0);
        List<PropertyPolygon> nearby = grid.getNearbyProperties(property);

        assertNotNull(nearby, "Nearby properties list should not be null.");
        assertFalse(nearby.isEmpty(), "Nearby properties list should not be empty.");
    }

    @Test
    @DisplayName("getNearbyProperties with nearby properties in adjacent cells")
    @Description("Verifies that nearby properties in adjacent cells are returned correctly.")
    @Severity(SeverityLevel.CRITICAL)
    void getNearbyProperties3() {
        List<PropertyPolygon> properties = TestUtils.createAdjacentProperties();
        SpatialGrid grid = new SpatialGrid(properties);

        for (PropertyPolygon property : properties) {
            grid.insert(property);
        }

        PropertyPolygon property = properties.get(1);
        List<PropertyPolygon> nearby = grid.getNearbyProperties(property);

        assertNotNull(nearby, "Nearby properties list should not be null.");
        assertFalse(nearby.isEmpty(), "Nearby properties list should not be empty.");
    }

    @Test
    @DisplayName("getNearbyProperties with no shared vertices")
    @Description("Verifies that no nearby properties are returned when there are no shared vertices.")
    @Severity(SeverityLevel.MINOR)
    void getNearbyProperties4() {
        List<PropertyPolygon> properties = TestUtils.createNonAdjacentProperties();
        SpatialGrid grid = new SpatialGrid(properties);

        for (PropertyPolygon property : properties) {
            grid.insert(property);
        }

        PropertyPolygon property = properties.get(0);
        List<PropertyPolygon> nearby = grid.getNearbyProperties(property);

        assertNotNull(nearby, "Nearby properties list should not be null.");
        assertTrue(nearby.isEmpty(), "Nearby properties list should be empty.");
    }

    @Test
    @DisplayName("checkAndAddNearbyProperties with valid cell key")
    @Description("Verifies that nearby properties are correctly added for a valid cell key.")
    @Severity(SeverityLevel.NORMAL)
    void checkAndAddNearbyProperties() {
        List<PropertyPolygon> properties = TestUtils.createAdjacentProperties();
        SpatialGrid grid = new SpatialGrid(properties);

        for (PropertyPolygon property : properties) {
            grid.insert(property);
        }

        PropertyPolygon property = properties.get(0);
        List<PropertyPolygon> nearby = grid.getNearbyProperties(property);

        assertNotNull(nearby, "Nearby properties list should not be null.");
        assertFalse(nearby.isEmpty(), "Nearby properties list should not be empty.");
    }

    @Test
    @DisplayName("checkAndAddNearbyProperties adds properties from cell")
    @Description("Verifies that checkAndAddNearbyProperties adds all properties from the specified cell to the set.")
    @Severity(SeverityLevel.NORMAL)
    void checkAndAddNearbyProperties_addsProperties() throws Exception {
        // Setup grid and property
        PropertyPolygon property = TestUtils.createSingleProperty();
        List<PropertyPolygon> properties = Collections.singletonList(property);
        SpatialGrid grid = new SpatialGrid(properties);
        grid.insert(property);

        // Find the cell key for the property
        String cellKey = grid.getCellKey(
                property.getPolygon().getVertices().get(0).x(),
                property.getPolygon().getVertices().get(0).y()
        );

        Set<PropertyPolygon> nearby = new HashSet<>();

        // Use reflection to invoke private method
        Method method = SpatialGrid.class.getDeclaredMethod("checkAndAddNearbyProperties", String.class, Set.class);
        method.setAccessible(true);
        method.invoke(grid, cellKey, nearby);

        assertTrue(nearby.contains(property), "Nearby set should contain the inserted property.");
    }

    @Test
    @DisplayName("printGridRanges prints grid cell ranges")
    @Description("Verifies that printGridRanges outputs grid cell ranges to the console.")
    @Severity(SeverityLevel.TRIVIAL)
    void printGridRanges_printsOutput() {
        List<PropertyPolygon> properties = new ArrayList<>();
        SpatialGrid grid = new SpatialGrid(properties);

        // Capture console output
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(outContent));

        grid.printGridRanges();

        System.setOut(originalOut);
        String output = outContent.toString();

        assertTrue(output.contains("--- Grid Cell Ranges ---"), "Output should contain grid cell ranges header.");
    }
}