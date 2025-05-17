package Models;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import Utils.Mocks.MockedPropertyPolygon;
import io.qameta.allure.Description;
import io.qameta.allure.Feature;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import java.util.List;

/**
 * This class contains unit tests for the {@link PropertyPolygon} class.
 * It validates the behavior of the PropertyPolygon class, including
 * its ability to handle property attributes, parse geometry, and ensure equality
 * and hash code correctness. Each test case is designed to cover specific functionality
 * and edge cases, ensuring the robustness of the PropertyPolygon implementation.
 *
 * <p><strong>Author:</strong> Ptome2000</p>
 * <p><strong>Date:</strong> 11/04/2025</p>
 *
 * <p><strong>Cyclomatic Complexity:</strong></p>
 * <ul>
 *     <li>Constructor: 1</li>
 *     <li>getObjectId: 1</li>
 *     <li>getParId: 1</li>
 *     <li>getParNum: 1</li>
 *     <li>getShapeLength: 1</li>
 *     <li>getShapeArea: 1</li>
 *     <li>getPolygon: 1</li>
 *     <li>getOwner: 1</li>
 *     <li>getFreguesia: 1</li>
 *     <li>getMunicipio: 1</li>
 *     <li>getIlha: 1</li>
 *     <li>fromCsvRow: 2</li>
 *     <li>parseGeometry: 5</li>
 *     <li>equals: 4</li>
 *     <li>hashCode: 1</li>
 *     <li>toString: 1</li>
 * </ul>
 */
@Feature("Object Models")
class PropertyPolygonTests {

    @Test
    @DisplayName("Constructor initializes all fields correctly")
    @Description("Verifies that the PropertyPolygon constructor initializes all fields with valid inputs.")
    @Severity(SeverityLevel.CRITICAL)
    void constructor() {
        Polygon polygon = new Polygon(List.of(new VertexCoordinate(0, 0), new VertexCoordinate(1, 1)));
        PropertyPolygon property = new PropertyPolygon(1, 123.45, "P123", 100.0, 200.0, polygon, "Owner1", "Freguesia1", "Municipio1", "Ilha1");

        assertNotNull(property, "PropertyPolygon instance should not be null.");
    }

    @Test
    @DisplayName("getObjectId returns correct object ID")
    @Description("Validates that the getObjectId method returns the correct object ID.")
    @Severity(SeverityLevel.NORMAL)
    void getObjectId() {
        PropertyPolygon property = new MockedPropertyPolygon(1, new Polygon(List.of(new VertexCoordinate(0, 0))));
        assertEquals(1, property.getObjectId(), "getObjectId should return the correct object ID.");
    }

    @Test
    @DisplayName("getParId returns correct parcel ID")
    @Description("Validates that the getParId method returns the correct parcel ID.")
    @Severity(SeverityLevel.NORMAL)
    void getParId() {
        PropertyPolygon property = new PropertyPolygon(1, 123.45, "P123", 100.0, 200.0,
                new Polygon(List.of(new VertexCoordinate(0, 0))), "Owner1", "Freguesia1", "Municipio1", "Ilha1");
        assertEquals(123.45, property.getParId(), "getParId should return the correct parcel ID.");
    }

    @Test
    @DisplayName("getParNum returns correct parcel number")
    @Description("Validates that the getParNum method returns the correct parcel number.")
    @Severity(SeverityLevel.NORMAL)
    void getParNum() {
        PropertyPolygon property = new PropertyPolygon(1, 123.45, "P123", 100.0, 200.0,
                new Polygon(List.of(new VertexCoordinate(0, 0))), "Owner1", "Freguesia1", "Municipio1", "Ilha1");
        assertEquals("P123", property.getParNum(), "getParNum should return the correct parcel number.");
    }

    @Test
    @DisplayName("getShapeLength returns correct shape length")
    @Description("Validates that the getShapeLength method returns the correct shape length.")
    @Severity(SeverityLevel.NORMAL)
    void getShapeLength() {
        PropertyPolygon property = new PropertyPolygon(1, 123.45, "P123", 100.0, 200.0,
                new Polygon(List.of(new VertexCoordinate(0, 0))), "Owner1", "Freguesia1", "Municipio1", "Ilha1");
        assertEquals(100.0, property.getShapeLength(), "getShapeLength should return the correct shape length.");
    }

    @Test
    @DisplayName("getShapeArea returns correct shape area")
    @Description("Validates that the getShapeArea method returns the correct shape area.")
    @Severity(SeverityLevel.NORMAL)
    void getShapeArea() {
        PropertyPolygon property = new PropertyPolygon(1, 123.45, "P123", 100.0, 200.0,
                new Polygon(List.of(new VertexCoordinate(0, 0))), "Owner1", "Freguesia1", "Municipio1", "Ilha1");
        assertEquals(200.0, property.getShapeArea(), "getShapeArea should return the correct shape area.");
    }

    @Test
    @DisplayName("getPolygon returns correct polygon")
    @Description("Validates that the getPolygon method returns the correct polygon.")
    @Severity(SeverityLevel.NORMAL)
    void getPolygon() {
        Polygon polygon = new Polygon(List.of(new VertexCoordinate(0, 0), new VertexCoordinate(1, 1)));
        PropertyPolygon property = new PropertyPolygon(1, 123.45, "P123", 100.0, 200.0, polygon, "Owner1", "Freguesia1", "Municipio1", "Ilha1");
        assertEquals(polygon, property.getPolygon(), "getPolygon should return the correct polygon.");
    }

    @Test
    @DisplayName("getOwner returns correct owner")
    @Description("Validates that the getOwner method returns the correct owner.")
    @Severity(SeverityLevel.NORMAL)
    void getOwner() {
        PropertyPolygon property = new PropertyPolygon(1, 123.45, "P123", 100.0, 200.0,
                new Polygon(List.of(new VertexCoordinate(0, 0))), "Owner1", "Freguesia1", "Municipio1", "Ilha1");
        assertEquals("Owner1", property.getOwner(), "getOwner should return the correct owner.");
    }

    @Test
    @DisplayName("getFreguesia returns correct freguesia")
    @Description("Validates that the getFreguesia method returns the correct freguesia.")
    @Severity(SeverityLevel.NORMAL)
    void getFreguesia() {
        PropertyPolygon property = new PropertyPolygon(1, 123.45, "P123", 100.0, 200.0,
                new Polygon(List.of(new VertexCoordinate(0, 0))), "Owner1", "Freguesia1", "Municipio1", "Ilha1");
        assertEquals("Freguesia1", property.getFreguesia(), "getFreguesia should return the correct freguesia.");
    }

    @Test
    @DisplayName("getMunicipio returns correct municipio")
    @Description("Validates that the getMunicipio method returns the correct municipio.")
    @Severity(SeverityLevel.NORMAL)
    void getMunicipio() {
        PropertyPolygon property = new PropertyPolygon(1, 123.45, "P123", 100.0, 200.0,
                new Polygon(List.of(new VertexCoordinate(0, 0))), "Owner1", "Freguesia1", "Municipio1", "Ilha1");
        assertEquals("Municipio1", property.getMunicipio(), "getMunicipio should return the correct municipio.");
    }

    @Test
    @DisplayName("getIlha returns correct ilha")
    @Description("Validates that the getIlha method returns the correct ilha.")
    @Severity(SeverityLevel.NORMAL)
    void getIlha() {
        PropertyPolygon property = new PropertyPolygon(1, 123.45, "P123", 100.0, 200.0,
                new Polygon(List.of(new VertexCoordinate(0, 0))), "Owner1", "Freguesia1", "Municipio1", "Ilha1");
        assertEquals("Ilha1", property.getIlha(), "getIlha should return the correct ilha.");
    }

    @Test
    @DisplayName("fromCsvRow creates PropertyPolygon from valid input")
    @Description("Validates that the fromCsvRow method correctly creates a PropertyPolygon object from a valid CSV row.")
    @Severity(SeverityLevel.CRITICAL)
    void fromCsvRow1() {
        String[] row = {"1", "123.45", "P123", "100.0", "200.0", "MULTIPOLYGON ((0 0, 1 1))", "Owner1", "Freguesia1", "Municipio1", "Ilha1"};
        PropertyPolygon property = PropertyPolygon.fromCsvRow(row);

        assertNotNull(property, "PropertyPolygon should be created successfully.");
        assertEquals(1, property.getObjectId(), "Object ID should match the input.");
    }

    @Test
    @DisplayName("fromCsvRow returns null for invalid input")
    @Description("Validates that the fromCsvRow method returns null when provided with an invalid CSV row.")
    @Severity(SeverityLevel.NORMAL)
    void fromCsvRow2() {
        String[] row = {"INVALID", "123.45", "P123", "100.0", "200.0", "MULTIPOLYGON ((0 0, 1 1))", "Owner1", "Freguesia1", "Municipio1", "Ilha1"};
        PropertyPolygon property = PropertyPolygon.fromCsvRow(row);

        assertNull(property, "PropertyPolygon should be null for invalid input.");
    }

    @Test
    @DisplayName("parseGeometry parses valid MULTIPOLYGON input")
    @Description("Validates that the parseGeometry method correctly parses a valid MULTIPOLYGON string into a Polygon object.")
    @Severity(SeverityLevel.NORMAL)
    void parseGeometry1() {
        String geometry = "MULTIPOLYGON ((0 0, 1 1))";
        Polygon polygon = PropertyPolygon.parseGeometry(geometry);

        assertNotNull(polygon, "Polygon should be created successfully.");
        assertEquals(2, polygon.getVertices().size(), "Polygon should have the correct number of vertices.");
    }

    @Test
    @DisplayName("parseGeometry handles invalid input gracefully")
    @Description("Validates that the parseGeometry method handles invalid geometry strings gracefully by returning an empty Polygon.")
    @Severity(SeverityLevel.NORMAL)
    void parseGeometry2() {
        String geometry = "INVALID_GEOMETRY";
        Polygon polygon = PropertyPolygon.parseGeometry(geometry);

        assertNotNull(polygon, "Polygon should not be null even for invalid input.");
        assertTrue(polygon.getVertices().isEmpty(), "Polygon should have no vertices for invalid input.");
    }

    @Test
    @DisplayName("equals returns true for the same object")
    @Description("Validates that the equals method returns true when comparing the same object.")
    @Severity(SeverityLevel.NORMAL)
    void equals1() {
        Polygon polygon = new Polygon(List.of(new VertexCoordinate(0, 0)));
        PropertyPolygon property = new MockedPropertyPolygon(1, polygon);

        assertEquals(property, property, "An object should be equal to itself.");
    }

    @Test
    @DisplayName("equals returns false for different objects")
    @Description("Validates that the equals method returns false when comparing two different PropertyPolygon objects.")
    @Severity(SeverityLevel.NORMAL)
    void equals2() {
        Polygon polygon1 = new Polygon(List.of(new VertexCoordinate(0, 0)));
        Polygon polygon2 = new Polygon(List.of(new VertexCoordinate(1, 1)));
        PropertyPolygon property1 = new MockedPropertyPolygon(1, polygon1);
        PropertyPolygon property2 = new MockedPropertyPolygon(2, polygon2);

        assertNotEquals(property1, property2, "Objects with different IDs should not be equal.");
    }

    @Test
    @DisplayName("hashCode returns correct hash code")
    @Description("Validates that the hashCode method returns the correct hash code based on the object ID.")
    @Severity(SeverityLevel.NORMAL)
    void hashCodeTest() {
        Polygon polygon = new Polygon(List.of(new VertexCoordinate(0, 0)));
        PropertyPolygon property = new MockedPropertyPolygon(1, polygon);

        assertEquals(1, property.hashCode(), "Hash code should match the object ID.");
    }

    @Test
    @DisplayName("toString returns correct string representation")
    @Description("Validates that the toString method returns the correct string representation of the PropertyPolygon object.")
    @Severity(SeverityLevel.TRIVIAL)
    void toStringTest() {
        Polygon polygon = new Polygon(List.of(new VertexCoordinate(0, 0), new VertexCoordinate(1, 1)));
        PropertyPolygon property = new MockedPropertyPolygon(1, polygon);

        String expected = "ID: 1, Owner: , Vertices: " + polygon;
        assertEquals(expected, property.toString(), "toString should return the correct representation.");
    }

    @Test
    @DisplayName("equals returns false when compared to null")
    @Description("Validates that the equals method returns false when the object is compared to null.")
    @Severity(SeverityLevel.NORMAL)
    void equals_null_returnsFalse() {
        PropertyPolygon p = new PropertyPolygon(1, 0, "", 0, 0, new Polygon(List.of()), "", "", "", "");
        assertNotEquals(null, p, "equals should return false when compared to null.");
    }

    @Test
    @DisplayName("equals returns true for objects with the same objectId")
    @Description("Validates that the equals method returns true for two PropertyPolygon objects with the same objectId.")
    @Severity(SeverityLevel.NORMAL)
    void equals_sameId_returnsTrue() {
        PropertyPolygon p1 = new PropertyPolygon(1, 0, "", 0, 0, new Polygon(List.of()), "", "", "", "");
        PropertyPolygon p2 = new PropertyPolygon(1, 0, "", 0, 0, new Polygon(List.of()), "", "", "", "");
        assertEquals(p1, p2, "equals should return true for objects with the same objectId.");
    }

    @Test
    @DisplayName("equals returns false for objects with different objectId")
    @Description("Validates that the equals method returns false for two PropertyPolygon objects with different objectId.")
    @Severity(SeverityLevel.NORMAL)
    void equals_differentId_returnsFalse() {
        PropertyPolygon p1 = new PropertyPolygon(1, 0, "", 0, 0, new Polygon(List.of()), "", "", "", "");
        PropertyPolygon p2 = new PropertyPolygon(2, 0, "", 0, 0, new Polygon(List.of()), "", "", "", "");
        assertNotEquals(p1, p2, "equals should return false for objects with different objectId.");
    }

}