package Utils;

import Models.Parish;
import Models.PropertyPolygon;
import java.util.List;

public class MockedParish extends Parish {

    private final List<PropertyPolygon> mockProperties;

    public MockedParish(String name, List<PropertyPolygon> properties) {
        super(name);
        this.mockProperties = properties;
    }

    @Override
    public List<PropertyPolygon> getPropertyPolygons() {
        return mockProperties;
    }

}
