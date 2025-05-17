package Utils.Mocks;

import Models.District;
import Models.Municipality;
import Models.PropertyPolygon;
import java.util.List;

/**
 * The {@code MockedDistrict} class is a mock implementation of the {@code District} class.
 * It is used for testing purposes to simulate a district with predefined municipalities.
 */
public class MockedDistrict extends District {
    private final List<Municipality> mockMunicipalities;

    /**
     * Constructor to initialize the MockedDistrict object with a name and a list of municipalities.
     *
     * @param name The name of the district.
     * @param municipalities A list of Municipality objects representing the municipalities in the district.
     */
    public MockedDistrict(String name, List<Municipality> municipalities) {
        super(name);
        this.mockMunicipalities = municipalities;
    }

    /**
     * Gets the list of municipalities within the mocked district.
     *
     * @return A list of Municipality objects representing the municipalities in the mocked district.
     */
    @Override
    public List<Municipality> getMunicipalities() {
        return mockMunicipalities;
    }

    /**
     * Gets all property polygons from all municipalities in the mocked district.
     *
     * @return A list of PropertyPolygon objects representing all property polygons in the mocked district.
     */
    @Override
    public List<PropertyPolygon> getAllPropertyPolygons() {
        return mockMunicipalities.stream()
                .flatMap(m -> m.getAllPropertyPolygons().stream())
                .toList();
    }
}