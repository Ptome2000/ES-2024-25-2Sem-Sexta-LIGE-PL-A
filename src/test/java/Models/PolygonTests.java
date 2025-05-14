package Models;

import io.qameta.allure.Description;
import io.qameta.allure.Feature;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * This class contains unit tests for the {@link Polygon} class.
 * It validates the behavior of the Polygon class, including
 * its ability to handle vertex coordinates and provide a string representation
 * of the polygon. Each test case is designed to cover specific functionality
 * and edge cases, ensuring the robustness of the Polygon implementation.
 *
 * <p><strong>Author:</strong> Ptome2000</p>
 * <p><strong>Date:</strong> 11/04/2025</p>
 *
 * <p><strong>Cyclomatic Complexity:</strong></p>
 * <ul>
 *     <li>Constructor: 1</li>
 *     <li>getVertices: 1</li>
 *     <li>toString: 2</li>
 * </ul>
 */
@Feature("Object Models")
class PolygonTests {

    @Test
    @DisplayName("Constructor initializes vertices correctly")
    @Description("Validates that the Polygon constructor initializes the vertices list correctly.")
    @Severity(SeverityLevel.CRITICAL)
    void constructor() {
        List<VertexCoordinate> vertices = List.of(new VertexCoordinate(0, 0), new VertexCoordinate(1, 1));
        Polygon polygon = new Polygon(vertices);

        assertNotNull(polygon.getVertices(), "Vertices list should not be null.");
        assertEquals(2, polygon.getVertices().size(), "Vertices list should contain the correct number of elements.");
        assertEquals(vertices, polygon.getVertices(), "Vertices list should match the input list.");
    }

    @Test
    @DisplayName("getVertices returns correct vertices")
    @Description("Validates that the getVertices method returns the correct list of vertices.")
    @Severity(SeverityLevel.NORMAL)
    void getVertices() {
        List<VertexCoordinate> vertices = List.of(new VertexCoordinate(0, 0), new VertexCoordinate(1, 1));
        Polygon polygon = new Polygon(vertices);

        assertNotNull(polygon.getVertices(), "getVertices should not return null.");
        assertEquals(2, polygon.getVertices().size(), "getVertices should return the correct number of vertices.");
        assertEquals(vertices, polygon.getVertices(), "getVertices should return the correct list of vertices.");
    }

    @Test
    @DisplayName("toString returns correct string representation")
    @Description("Validates that the toString method returns the correct string representation of the Polygon object.")
    @Severity(SeverityLevel.TRIVIAL)
    void toStringTest() {
        List<VertexCoordinate> vertices = List.of(new VertexCoordinate(0, 0), new VertexCoordinate(1, 1));
        Polygon polygon = new Polygon(vertices);

        String expected = "(0.0, 0.0) (1.0, 1.0) ";
        assertEquals(expected, polygon.toString(), "toString should return the correct string representation.");
    }
}