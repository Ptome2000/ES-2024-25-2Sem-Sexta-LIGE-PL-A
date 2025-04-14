package DetectAdjacentProperties;

import io.qameta.allure.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

/**
 * This class contains unit tests for the {@link VertexCoordinate} class.
 * It verifies the functionality of various methods in the VertexCoordinate class,
 * ensuring that they behave as expected under different scenarios.
 *
 *
 * <p><strong>Author:</strong> Ptome2000</p>
 * <p><strong>Date:</strong> 11/04/2025</p>
 *
 * <p><strong>Cyclomatic Complexity:</strong></p>
 * <ul>
 *     <li>VertexCoordinate (constructor): 1</li>
 *     <li>getX: 1</li>
 *     <li>getY: 1</li>
 *     <li>equals: 2</li>
 *     <li>hashCode: 1</li>
 *     <li>toString: 1</li>
 * </ul>
 */

@Feature("Detect adjacent properties")
class VertexCoordinateTests {

    @Test
    @DisplayName("VertexCoordinate constructor with valid coordinates")
    @Description("Verifies that the VertexCoordinate instance is created successfully with valid X and Y values.")
    @Severity(SeverityLevel.NORMAL)
    void VertexCoordinate() {
        VertexCoordinate vertex = new VertexCoordinate(10.5, 20.5);

        assertNotNull(vertex, "VertexCoordinate instance should not be null.");
        assertEquals(10.5, vertex.getX(), "X coordinate should match the provided value.");
        assertEquals(20.5, vertex.getY(), "Y coordinate should match the provided value.");
    }

    @Test
    @DisplayName("getX returns correct X coordinate")
    @Description("Verifies that the getX method returns the correct X coordinate.")
    @Severity(SeverityLevel.MINOR)
    void getX() {
        VertexCoordinate vertex = new VertexCoordinate(15.0, 25.0);

        assertEquals(15.0, vertex.getX(), "getX should return the correct X coordinate.");
    }

    @Test
    @DisplayName("getY returns correct Y coordinate")
    @Description("Verifies that the getY method returns the correct Y coordinate.")
    @Severity(SeverityLevel.MINOR)
    void getY() {
        VertexCoordinate vertex = new VertexCoordinate(15.0, 25.0);

        assertEquals(25.0, vertex.getY(), "getY should return the correct Y coordinate.");
    }

    @Test
    @DisplayName("equals returns true for equal objects")
    @Description("Verifies that the equals method returns true for objects with the same coordinates.")
    @Severity(SeverityLevel.NORMAL)
    void equals1() {
        VertexCoordinate vertex1 = new VertexCoordinate(10.0, 20.0);
        VertexCoordinate vertex2 = new VertexCoordinate(10.0, 20.0);

        assertTrue(vertex1.equals(vertex2), "equals should return true for objects with the same coordinates.");
    }

    @Test
    @DisplayName("equals returns false for different objects")
    @Description("Verifies that the equals method returns false for objects with different coordinates.")
    @Severity(SeverityLevel.NORMAL)
    void equals2() {
        VertexCoordinate vertex1 = new VertexCoordinate(10.0, 20.0);
        VertexCoordinate vertex2 = new VertexCoordinate(15.0, 25.0);

        assertFalse(vertex1.equals(vertex2), "equals should return false for objects with different coordinates.");
    }

    @Test
    @DisplayName("hashCode returns consistent value")
    @Description("Verifies that the hashCode method returns a consistent value for the same object.")
    @Severity(SeverityLevel.MINOR)
    void hashCodeTest() {
        VertexCoordinate vertex = new VertexCoordinate(10.0, 20.0);

        int expectedHashCode = Objects.hash((int) (10.0 * 1000), (int) (20.0 * 1000));
        assertEquals(expectedHashCode, vertex.hashCode(), "hashCode should return the expected value.");
    }

    @Test
    @DisplayName("toString returns correct format")
    @Description("Verifies that the toString method returns the correct string representation of the vertex.")
    @Severity(SeverityLevel.TRIVIAL)
    void toStringTest() {
        VertexCoordinate vertex = new VertexCoordinate(10.0, 20.0);

        assertEquals("(10.0, 20.0)", vertex.toString(), "toString should return the correct format.");
    }
}