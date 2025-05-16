package Utils;

import Models.Municipality;
import Models.Parish;
import Models.PropertyPolygon;
import java.util.List;

public class MockedMunicipality extends Municipality {
    private final List<Parish> mockParishes;

    public MockedMunicipality(String name, List<Parish> parishes) {
        super(name);
        this.mockParishes = parishes;
    }

    @Override
    public List<Parish> getParishes() {
        return mockParishes;
    }

    @Override
    public List<PropertyPolygon> getAllPropertyPolygons() {
        return mockParishes.stream()
                .flatMap(p -> p.getPropertyPolygons().stream())
                .toList();
    }
}