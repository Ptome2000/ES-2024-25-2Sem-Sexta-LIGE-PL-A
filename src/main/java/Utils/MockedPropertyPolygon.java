package Utils;

import Models.Polygon;
import Models.PropertyPolygon;

// Class for testing purposes
public class MockedPropertyPolygon extends PropertyPolygon {

    public MockedPropertyPolygon(int objectId, Polygon polygon) {
        super(objectId, 0, "123", 0, 0, polygon, "John", "", "", "");
    }

    public MockedPropertyPolygon(int objectId, String owner, Polygon polygon) {
        super(objectId, 0, "123", 0, 0, polygon, owner, "", "", "");
    }

}
