package Utils.Mocks;

import Models.Municipality;
import Models.Parish;
import Models.PropertyPolygon;
import java.util.List;

/**
 * The {@code MockedMunicipality} class is a mock implementation of the {@code Municipality} class.
 * It is used for testing purposes to simulate a municipality with predefined parishes.
 */
public class MockedMunicipality extends Municipality {
    private final List<Parish> mockParishes;

    /**
     * Constructor to initialize the MockedMunicipality object with a name and a list of parishes.
     *
     * @param name The name of the municipality.
     * @param parishes A list of Parish objects representing the parishes in the municipality.
     */
    public MockedMunicipality(String name, List<Parish> parishes) {
        super(name);
        this.mockParishes = parishes;
    }

    /**
     * Gets the list of parishes within the mocked municipality.
     *
     * @return A list of Parish objects representing the parishes in the mocked municipality.
     */
    @Override
    public List<Parish> getParishes() {
        return mockParishes;
    }

    /**
     * Gets all property polygons from all parishes in the mocked municipality.
     *
     * @return A list of PropertyPolygon objects representing all property polygons in the mocked municipality.
     */
    @Override
    public List<PropertyPolygon> getAllPropertyPolygons() {
        return mockParishes.stream()
                .flatMap(p -> p.getPropertyPolygons().stream())
                .toList();
    }
}