package DetectAdjacentProperties;

import Models.PropertyPolygon;
import io.qameta.allure.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * This class contains unit tests for the {@link SpatialGrid} class.
 * It verifies the functionality of various methods in the SpatialGrid class,
 * ensuring that they behave as expected under different scenarios.
 *
 *
 * <p><strong>Author:</strong> Ptome2000</p>
 * <p><strong>Date:</strong> 09/04/2025</p>
 */
@Feature("Detect adjacent properties")
@DisplayName("Spatial Grid Tests")
class SpatialGridTests {

    @Test
    @DisplayName("Constructor with valid properties")
    @Description("Verifies that the SpatialGrid instance is created successfully with a valid list of properties.")
    @Severity(SeverityLevel.NORMAL)
    void spatialGridConstructor() {
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
    @DisplayName("GetPropertyGridCells with valid property")
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

    @Nested
    @DisplayName("Spatial Grid Nearby Properties Detection Tests")
    class NearbyPropertiesTests {

        @Test
        @DisplayName("No nearby properties")
        @Description("Verifies that no nearby properties are returned when none exist.")
        @Severity(SeverityLevel.MINOR)
        void getNearbyPropertiesNone() {
            List<PropertyPolygon> properties = new ArrayList<>();
            SpatialGrid grid = new SpatialGrid(properties);

            PropertyPolygon property = TestUtils.createSingleProperty();
            List<PropertyPolygon> nearby = grid.getNearbyProperties(property);

            assertNotNull(nearby, "Nearby properties list should not be null.");
            assertTrue(nearby.isEmpty(), "Nearby properties list should be empty.");
        }

        @Test
        @DisplayName("Nearby properties in same cell")
        @Description("Verifies that nearby properties in the same cell are returned correctly.")
        @Severity(SeverityLevel.NORMAL)
        void getNearbyPropertiesSameCell() {
            List<PropertyPolygon> properties = TestUtils.createAdjacentProperties();
            SpatialGrid grid = new SpatialGrid(properties);

            properties.forEach(grid::insert);
            PropertyPolygon property = properties.get(0);
            List<PropertyPolygon> nearby = grid.getNearbyProperties(property);

            assertNotNull(nearby, "Nearby properties list should not be null.");
            assertFalse(nearby.isEmpty(), "Nearby properties list should not be empty.");
        }

        @Test
        @DisplayName("Nearby properties in adjacent cells")
        @Description("Verifies that nearby properties in adjacent cells are returned correctly.")
        @Severity(SeverityLevel.CRITICAL)
        void getNearbyPropertiesAdjacentCells() {
            List<PropertyPolygon> properties = TestUtils.createAdjacentProperties();
            SpatialGrid grid = new SpatialGrid(properties);

            properties.forEach(grid::insert);
            PropertyPolygon property = properties.get(1);
            List<PropertyPolygon> nearby = grid.getNearbyProperties(property);

            assertNotNull(nearby, "Nearby properties list should not be null.");
            assertFalse(nearby.isEmpty(), "Nearby properties list should not be empty.");
        }

        @Test
        @DisplayName("No nearby properties when no shared vertices")
        @Description("Verifies that no nearby properties are returned when there are no shared vertices.")
        @Severity(SeverityLevel.MINOR)
        void getNearbyPropertiesNoSharedVertices() {
            List<PropertyPolygon> properties = TestUtils.createNonAdjacentProperties();
            SpatialGrid grid = new SpatialGrid(properties);

            properties.forEach(grid::insert);
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

            properties.forEach(grid::insert);
            PropertyPolygon property = properties.get(0);
            List<PropertyPolygon> nearby = grid.getNearbyProperties(property);

            assertNotNull(nearby, "Nearby properties list should not be null.");
            assertFalse(nearby.isEmpty(), "Nearby properties list should not be empty.");
        }
    }

}