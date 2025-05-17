package Models;

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
 *     <li>constructor: 1</li>
 *     <li>getX: 1</li>
 *     <li>getY: 1</li>
 *     <li>equals: 4</li>
 *     <li>hashCode: 1</li>
 *     <li>toString: 1</li>
 * </ul>
 */

@Feature("Object Models")
class VertexCoordinateTests {

    @Test
    @DisplayName("VertexCoordinate constructor with valid coordinates")
    @Description("Verifies that the VertexCoordinate instance is created successfully with valid X and Y values.")
    @Severity(SeverityLevel.NORMAL)
    void VertexCoordinate() {
        VertexCoordinate vertex = new VertexCoordinate(10.5, 20.5);

        assertNotNull(vertex, "VertexCoordinate instance should not be null.");
        assertEquals(10.5, vertex.x(), "X coordinate should match the provided value.");
        assertEquals(20.5, vertex.y(), "Y coordinate should match the provided value.");
    }

    @Test
    @DisplayName("getX returns correct X coordinate")
    @Description("Verifies that the getX method returns the correct X coordinate.")
    @Severity(SeverityLevel.MINOR)
    void getX() {
        VertexCoordinate vertex = new VertexCoordinate(15.0, 25.0);

        assertEquals(15.0, vertex.x(), "getX should return the correct X coordinate.");
    }

    @Test
    @DisplayName("getY returns correct Y coordinate")
    @Description("Verifies that the getY method returns the correct Y coordinate.")
    @Severity(SeverityLevel.MINOR)
    void getY() {
        VertexCoordinate vertex = new VertexCoordinate(15.0, 25.0);

        assertEquals(25.0, vertex.y(), "getY should return the correct Y coordinate.");
    }

    @Test
    @DisplayName("equals returns true for equal objects")
    @Description("Verifies that the equals method returns true for objects with the same coordinates.")
    @Severity(SeverityLevel.NORMAL)
    void equals1() {
        VertexCoordinate vertex1 = new VertexCoordinate(10.0, 20.0);
        VertexCoordinate vertex2 = new VertexCoordinate(10.0, 20.0);

        assertEquals(vertex1, vertex2, "equals should return true for objects with the same coordinates.");
    }

    @Test
    @DisplayName("equals returns false for different objects")
    @Description("Verifies that the equals method returns false for objects with different coordinates.")
    @Severity(SeverityLevel.NORMAL)
    void equals2() {
        VertexCoordinate vertex1 = new VertexCoordinate(10.0, 20.0);
        VertexCoordinate vertex2 = new VertexCoordinate(15.0, 25.0);

        assertNotEquals(vertex1, vertex2, "equals should return false for objects with different coordinates.");
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

    @Test
    @DisplayName("equals returns true when comparing the same object (line 54)")
    @Description("Verifies that equals returns true when the same object is compared to itself.")
    @Severity(SeverityLevel.NORMAL)
    void equals_SameObject() {
        VertexCoordinate vertex = new VertexCoordinate(1.0, 2.0);
        assertEquals(vertex, vertex);
    }

    @Test
    @DisplayName("equals returns false when comparing with a different type (line 55)")
    @Description("Verifies that equals returns false when compared with an object of a different type.")
    @Severity(SeverityLevel.NORMAL)
    void equals_DifferentType() {
        VertexCoordinate vertex = new VertexCoordinate(1.0, 2.0);
        String notAVertex = "not a vertex";
        assertNotEquals(notAVertex, vertex);
    }

    @Test
    @DisplayName("equals returns true for objects with nearly equal coordinates (line 57)")
    @Description("Verifies that equals returns true for objects with coordinates within the tolerance.")
    @Severity(SeverityLevel.NORMAL)
    void equals_EqualCoordinates() {
        VertexCoordinate v1 = new VertexCoordinate(1.00005, 2.00005);
        VertexCoordinate v2 = new VertexCoordinate(1.00004, 2.00004);
        assertEquals(v1, v2);
    }

    @Test
    @DisplayName("equals returns false for objects with different coordinates (line 57)")
    @Description("Verifies that equals returns false for objects with coordinates outside the tolerance.")
    @Severity(SeverityLevel.NORMAL)
    void equals_DifferentCoordinates() {
        VertexCoordinate v1 = new VertexCoordinate(1.0, 2.0);
        VertexCoordinate v2 = new VertexCoordinate(1.1, 2.1);
        assertNotEquals(v1, v2);
    }
}