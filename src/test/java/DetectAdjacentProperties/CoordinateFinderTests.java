package DetectAdjacentProperties;

import static org.junit.jupiter.api.Assertions.*;

import Models.Polygon;
import Models.PropertyPolygon;
import Models.VertexCoordinate;
import Utils.MockedPropertyPolygon;
import io.qameta.allure.Description;
import io.qameta.allure.Feature;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * This class contains unit tests for the {@link CoordinateFinder} class.
 * It validates the behavior of the CoordinateFinder class, including
 * its ability to find maximum and minimum coordinates from a list of properties,
 * and its robustness against empty, null, and partially invalid input.
 * Each test case is designed to cover specific functionality and edge cases, ensuring
 * the reliability of the CoordinateFinder implementation.
 *
 * <p><strong>Author:</strong> Ptome2000</p>
 * <p><strong>Date:</strong> 15/05/2025</p>
 *
 * <p><strong>Cyclomatic Complexity:</strong></p>
 * <ul>
 *     <li>findMaxCoordinates: 2</li>
 *     <li>findMinCoordinates: 2</li>
 *     <li>isNullOrEmpty: 2</li>
 *     <li>forEachVertex: 7</li>
 * </ul>
 */
@Feature("Detect adjacent properties")
class CoordinateFinderTests {

    @Test
    @DisplayName("findMaxCoordinates returns correct max for valid input")
    @Description("Checks that findMaxCoordinates returns the correct maximum X and Y values.")
    @Severity(SeverityLevel.CRITICAL)
    void findMaxCoordinates_validInput() {
        PropertyPolygon p1 = new MockedPropertyPolygon(1, "A", new Polygon(Arrays.asList(
                new VertexCoordinate(1, 2),
                new VertexCoordinate(3, 4)
        )));
        PropertyPolygon p2 = new MockedPropertyPolygon(2, "B", new Polygon(Arrays.asList(
                new VertexCoordinate(-1, 5),
                new VertexCoordinate(2, 0)
        )));
        List<PropertyPolygon> properties = Arrays.asList(p1, p2);

        double[] max = CoordinateFinder.findMaxCoordinates(properties);

        assertEquals(3, max[0]);
        assertEquals(5, max[1]);
    }

    @Test
    @DisplayName("findMinCoordinates returns correct min for valid input")
    @Description("Checks that findMinCoordinates returns the correct minimum X and Y values.")
    @Severity(SeverityLevel.CRITICAL)
    void findMinCoordinates_validInput() {
        PropertyPolygon p1 = new MockedPropertyPolygon(1, "A", new Polygon(Arrays.asList(
                new VertexCoordinate(1, 2),
                new VertexCoordinate(3, 4)
        )));
        PropertyPolygon p2 = new MockedPropertyPolygon(2, "B", new Polygon(Arrays.asList(
                new VertexCoordinate(-1, 5),
                new VertexCoordinate(2, 0)
        )));
        List<PropertyPolygon> properties = Arrays.asList(p1, p2);

        double[] min = CoordinateFinder.findMinCoordinates(properties);

        assertEquals(-1, min[0]);
        assertEquals(0, min[1]);
    }

    @Test
    @DisplayName("findMaxCoordinates returns NaN for empty list")
    @Description("Checks that findMaxCoordinates returns NaN for both coordinates when input is empty.")
    @Severity(SeverityLevel.MINOR)
    void findMaxCoordinates_emptyList() {
        double[] max = CoordinateFinder.findMaxCoordinates(Collections.emptyList());
        assertTrue(Double.isNaN(max[0]));
        assertTrue(Double.isNaN(max[1]));
    }

    @Test
    @DisplayName("findMinCoordinates returns NaN for empty list")
    @Description("Checks that findMinCoordinates returns NaN for both coordinates when input is empty.")
    @Severity(SeverityLevel.MINOR)
    void findMinCoordinates_emptyList() {
        double[] min = CoordinateFinder.findMinCoordinates(Collections.emptyList());
        assertTrue(Double.isNaN(min[0]));
        assertTrue(Double.isNaN(min[1]));
    }

    @Test
    @DisplayName("findMaxCoordinates returns NaN for null input")
    @Description("Checks that findMaxCoordinates returns NaN for both coordinates when input is null.")
    @Severity(SeverityLevel.MINOR)
    void findMaxCoordinates_nullInput() {
        double[] max = CoordinateFinder.findMaxCoordinates(null);
        assertTrue(Double.isNaN(max[0]));
        assertTrue(Double.isNaN(max[1]));
    }

    @Test
    @DisplayName("findMinCoordinates returns NaN for null input")
    @Description("Checks that findMinCoordinates returns NaN for both coordinates when input is null.")
    @Severity(SeverityLevel.MINOR)
    void findMinCoordinates_nullInput() {
        double[] min = CoordinateFinder.findMinCoordinates(null);
        assertTrue(Double.isNaN(min[0]));
        assertTrue(Double.isNaN(min[1]));
    }

    @Test
    @DisplayName("findMaxCoordinates skips nulls and empty polygons")
    @Description("Checks that null properties and empty polygons are skipped.")
    @Severity(SeverityLevel.NORMAL)
    void findMaxCoordinates_skipsNulls() {
        PropertyPolygon p1 = null;
        PropertyPolygon p2 = new MockedPropertyPolygon(2, "B", null);
        PropertyPolygon p3 = new MockedPropertyPolygon(3, "C", new Polygon(null));
        PropertyPolygon p4 = new MockedPropertyPolygon(4, "D", new Polygon(List.of(
                new VertexCoordinate(7, 8)
        )));
        List<PropertyPolygon> properties = Arrays.asList(p1, p2, p3, p4);

        double[] max = CoordinateFinder.findMaxCoordinates(properties);

        assertEquals(7, max[0]);
        assertEquals(8, max[1]);
    }

}