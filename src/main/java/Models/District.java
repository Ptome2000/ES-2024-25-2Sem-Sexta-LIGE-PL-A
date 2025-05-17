package Models;

import Utils.Annotations.CyclomaticComplexity;
import Utils.Annotations.Layer;
import Utils.Enums.LayerType;
import java.util.ArrayList;
import java.util.List;

/**
 * The {@code Municipality} class represents a municipality within a district.
 * It provides methods to access the municipality's name and other related information.
 */
@Layer(LayerType.BACK_END)
public class District implements Region {

    private final String name;
    private final List<Municipality> municipalities;

    /**
     * Constructor to initialize the District object.
     *
     * @param name The name of the district.
     */
    public District(String name) {
        this.name = name;
        this.municipalities = new ArrayList<>();
    }

    /**
     * Gets the name of the district.
     *
     * @return The name of the district.
     */
    @Override
    @CyclomaticComplexity(1)
    public String name() {
        return name;
    }

    /**
     * Gets the list of municipalities within the district.
     *
     * @return A list of Municipality objects representing the municipalities in the district.
     */
    @CyclomaticComplexity(1)
    public List<Municipality> getMunicipalities() {
        return municipalities;
    }

    /**
     * Adds a municipality to the district.
     *
     * @param municipality The Municipality object to be added to the district.
     */
    @CyclomaticComplexity(1)
    public void addMunicipality(Municipality municipality) {
        this.municipalities.add(municipality);
    }

    /**
     * Gets the list of all PropertyPolygons in the district.
     *
     * @return A list of PropertyPolygon objects from all municipalities in the district.
     */
    @CyclomaticComplexity(2)
    public List<PropertyPolygon> getAllPropertyPolygons() {
        List<PropertyPolygon> allPolygons = new ArrayList<>();
        for (Municipality municipality : municipalities) {
            allPolygons.addAll(municipality.getAllPropertyPolygons());
        }
        return allPolygons;
    }
}
