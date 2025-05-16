package Utils;

import Models.District;
import Models.Municipality;
import Models.PropertyPolygon;
import java.util.List;

public class MockedDistrict extends District {
    private final List<Municipality> mockMunicipalities;

    public MockedDistrict(String name, List<Municipality> municipalities) {
        super(name);
        this.mockMunicipalities = municipalities;
    }

    @Override
    public List<Municipality> getMunicipalities() {
        return mockMunicipalities;
    }

    @Override
    public List<PropertyPolygon> getAllPropertyPolygons() {
        return mockMunicipalities.stream()
                .flatMap(m -> m.getAllPropertyPolygons().stream())
                .toList();
    }
}