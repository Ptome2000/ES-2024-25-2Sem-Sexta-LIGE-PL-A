package Utils.Mocks;

import Models.Polygon;
import Models.PropertyPolygon;

/**
 * The {@code MockedPropertyPolygon} class is a mock implementation of the {@code PropertyPolygon} class.
 * It is used for testing purposes to simulate a property polygon with predefined attributes.
 */
public class MockedPropertyPolygon extends PropertyPolygon {

    /**
     * Constructor to initialize the MockedPropertyPolygon object with an object ID and a polygon.
     *
     * @param objectId The unique identifier for the property polygon.
     * @param polygon The Polygon object representing the shape of the property.
     */
    public MockedPropertyPolygon(int objectId, Polygon polygon) {
        super(objectId, 0, "123", 0, 0, polygon, "John", "", "", "");
    }

    /**
     * Constructor to initialize the MockedPropertyPolygon object with an object ID, owner, and a polygon.
     *
     * @param objectId The unique identifier for the property polygon.
     * @param owner The owner of the property.
     * @param polygon The Polygon object representing the shape of the property.
     */
    public MockedPropertyPolygon(int objectId, String owner, Polygon polygon) {
        super(objectId, 0, "123", 0, 0, polygon, owner, "", "", "");
    }

}
