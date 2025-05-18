package Utils.Mocks;

import Models.Polygon;
import Models.PropertyPolygon;
import Models.VertexCoordinate;
import java.util.List;

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

    /**
     * Constructor to initialize the MockedPropertyPolygon object with various attributes.
     *
     * @param objectId    Unique identifier for the property.
     * @param parId       Parcel ID associated with the property.
     * @param parNum      Parcel number for the property.
     * @param shapeLength Length of the shape's boundary.
     * @param shapeArea   Area of the property shape.
     * @param polygon     The polygon representing the shape of the property.
     * @param owner       ID of the owner of the property.
     * @param freguesia   The parish where the property is located.
     * @param municipio   The municipality where the property is located.
     * @param ilha        The island where the property is located.
     */
    public MockedPropertyPolygon(int objectId, double parId, String parNum, double shapeLength, double shapeArea, Polygon polygon, String owner, String freguesia, String municipio, String ilha) {
        super(objectId, parId, parNum, shapeLength, shapeArea, polygon, owner, freguesia, municipio, ilha);
    }


    /**
     * Predefined constructor to create a MockedPropertyPolygon object with specific values to test suggestions
     */
    public static MockedPropertyPolygon createMockedProperty1() {
        return new MockedPropertyPolygon(
                5515, 999.6396231222137, "3,16E+12", 164.15196950073607, 7309470.0,
                new Polygon(List.of(
                        new VertexCoordinate(315229.8312999997, 3615390.897),
                        new VertexCoordinate(315240.53139999975, 3615386.831599999),
                        new VertexCoordinate(315240.58579999954, 3615386.8894999996),
                        new VertexCoordinate(315240.8492999999, 3615386.7913000006),
                        new VertexCoordinate(315239.7818999998, 3615385.430299999),
                        new VertexCoordinate(315239.31149999984, 3615385.6099999994),
                        new VertexCoordinate(315238.91000000015, 3615385.268100001),
                        new VertexCoordinate(315228.1051000003, 3615373.4749),
                        new VertexCoordinate(315227.8502000002, 3615373.647500001),
                        new VertexCoordinate(315223.0900999997, 3615369.2682000007),
                        new VertexCoordinate(315205.1699999999, 3615381.0365999993),
                        new VertexCoordinate(315196.29030000046, 3615386.8681000005),
                        new VertexCoordinate(315187.41309999954, 3615395.3015),
                        new VertexCoordinate(315172.5488999998, 3615409.4225999992),
                        new VertexCoordinate(315172.5658, 3615409.4410999995),
                        new VertexCoordinate(315174.12490000017, 3615411.141899999),
                        new VertexCoordinate(315174.4753999999, 3615411.5242),
                        new VertexCoordinate(315174.48000000045, 3615411.5199999996)
                )),
                "129", "Câmara de Lobos", "Câmara de Lobos", "Ilha da Madeira (Madeira)"
        );
    }

    /**
     * Predefined constructor to create a MockedPropertyPolygon object with specific values to test suggestions
     */
    public static MockedPropertyPolygon createMockedProperty2() {
        return new MockedPropertyPolygon(
                5993, 1002.8309794334668, "3,16E+12", 179.86098189111513, 7309462.0,
                new Polygon(List.of(
                        new VertexCoordinate(315178.1484000003, 3615414.8808999993),
                        new VertexCoordinate(315189.9628999997, 3615413.931),
                        new VertexCoordinate(315194.8200000003, 3615413.2699999996),
                        new VertexCoordinate(315211.6200000001, 3615412.369999999),
                        new VertexCoordinate(315220.4199999999, 3615410.7699999996),
                        new VertexCoordinate(315252.1794999996, 3615405.4767000005),
                        new VertexCoordinate(315254.01999999955, 3615405.17),
                        new VertexCoordinate(315254.8596000001, 3615404.6326),
                        new VertexCoordinate(315256.66110000014, 3615404.5611000005),
                        new VertexCoordinate(315253.3200000003, 3615399.84),
                        new VertexCoordinate(315254.70610000007, 3615399.0776000004),
                        new VertexCoordinate(315246.6796000004, 3615393.5657),
                        new VertexCoordinate(315243.1618999997, 3615389.74),
                        new VertexCoordinate(315243.0809000004, 3615389.6367000006),
                        new VertexCoordinate(315242.8958999999, 3615389.400800001),
                        new VertexCoordinate(315242.6579999998, 3615389.0975),
                        new VertexCoordinate(315241.32129999995, 3615387.393100001),
                        new VertexCoordinate(315240.8492999999, 3615386.7913000006),
                        new VertexCoordinate(315240.58579999954, 3615386.8894999996),
                        new VertexCoordinate(315240.53139999975, 3615386.831599999),
                        new VertexCoordinate(315229.8312999997, 3615390.897),
                        new VertexCoordinate(315175.4199999999, 3615411.5700000003),
                        new VertexCoordinate(315176.6200000001, 3615412.2699999996)
                )),
                "164", "Câmara de Lobos", "Câmara de Lobos", "Ilha da Madeira (Madeira)"
        );
    }
}
