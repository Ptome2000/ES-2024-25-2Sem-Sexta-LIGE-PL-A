package Utils.Mocks;

import Models.Parish;
import Models.PropertyPolygon;
import java.util.List;

/**
 * The {@code MockedParish} class is a mock implementation of the {@code Parish} class.
 * It is used for testing purposes to simulate a parish with predefined property polygons.
 */
public class MockedParish extends Parish {

    private final List<PropertyPolygon> mockProperties;

    /**
     * Constructor to initialize the MockedParish object with a name and a list of property polygons.
     *
     * @param name The name of the parish.
     * @param properties A list of PropertyPolygon objects representing the properties in the parish.
     */
    public MockedParish(String name, List<PropertyPolygon> properties) {
        super(name);
        this.mockProperties = properties;
    }

    /**
     * Gets the list of property polygons within the mocked parish.
     *
     * @return A list of PropertyPolygon objects representing the properties in the mocked parish.
     */
    @Override
    public List<PropertyPolygon> getPropertyPolygons() {
        return mockProperties;
    }

}
