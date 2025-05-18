package Services;

import Models.Municipality;
import Models.Parish;
import Models.PropertyPolygon;
import Models.District;
import Utils.Annotations.CyclomaticComplexity;
import Utils.Annotations.Layer;
import Utils.Enums.LayerType;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * The {@code PropertyCollector} class is responsible for collecting and filtering property data
 * from a list of districts. It provides methods to collect properties by owner, district, municipality,
 * and parish, as well as methods to retrieve unique owner IDs and names of districts, municipalities,
 * and parishes.
 */
@Layer(LayerType.BACK_END)
public class PropertyCollector {

    private final List<District> districts;

    /**
     * Constructs a PropertyCollector with the given list of districts.
     *
     * @param districts The list of districts containing property data.
     */
    public PropertyCollector(List<District> districts) {
        this.districts = districts;
    }

    /**
     * Collects all PropertyPolygons from all districts.
     *
     * @return A list of PropertyPolygon objects from all districts.
     */
    @CyclomaticComplexity(2)
    public List<PropertyPolygon> collectAllProperties() {
        List<PropertyPolygon> allPolygons = new ArrayList<>();
        for (District district : districts) {
            allPolygons.addAll(district.getAllPropertyPolygons());
        }
        return allPolygons;
    }

    /**
     * Returns all properties of the selected owner.
     *
     * @param id The id of the owner to filter properties by.
     * @return A list of properties.
     */
    @CyclomaticComplexity(4)
    public List<PropertyPolygon> collectAllPropertiesByOwner(String id) {
        List<PropertyPolygon> ownerProperties = new ArrayList<>();
        for (District district : districts) {
            for (PropertyPolygon property : district.getAllPropertyPolygons()) {
                if (property.getOwner().equals(id)) {
                    ownerProperties.add(property);
                }
            }
        }
        return ownerProperties;
    }

    /**
     * Collects all properties belonging to a specific owner within a specific district.
     *
     * @param ownerId      The ID of the owner.
     * @param districtName The name of the district.
     * @return A list of PropertyPolygon objects owned by the owner within the district.
     */
    @CyclomaticComplexity(1)
    public List<PropertyPolygon> collectPropertiesByOwnerAndDistrict(String ownerId, String districtName) {
        return districts.stream()
                .filter(d -> d.name().equalsIgnoreCase(districtName))
                .flatMap(d -> d.getMunicipalities().stream())
                .flatMap(m -> m.getParishes().stream())
                .flatMap(p -> p.getPropertyPolygons().stream())
                .filter(prop -> prop.getOwner().equals(ownerId))
                .collect(Collectors.toList());
    }

    /**
     * Collects all properties belonging to a specific owner within a specific municipality.
     *
     * @param ownerId          The ID of the owner.
     * @param municipalityName The name of the municipality.
     * @return A list of PropertyPolygon objects owned by the owner within the municipality.
     */
    @CyclomaticComplexity(1)
    public List<PropertyPolygon> collectPropertiesByOwnerAndMunicipality(String ownerId, String municipalityName) {
        return districts.stream()
                .flatMap(d -> d.getMunicipalities().stream())
                .filter(m -> m.name().equalsIgnoreCase(municipalityName))
                .flatMap(m -> m.getParishes().stream())
                .flatMap(p -> p.getPropertyPolygons().stream())
                .filter(prop -> prop.getOwner().equals(ownerId))
                .collect(Collectors.toList());
    }

    /**
     * Collects all properties belonging to a specific owner within a specific parish.
     *
     * @param ownerId    The ID of the owner.
     * @param parishName The name of the parish.
     * @return A list of PropertyPolygon objects owned by the owner within the parish.
     */
    @CyclomaticComplexity(1)
    public List<PropertyPolygon> collectPropertiesByOwnerAndParish(String ownerId, String parishName) {
        return districts.stream()
                .flatMap(d -> d.getMunicipalities().stream())
                .flatMap(m -> m.getParishes().stream())
                .filter(p -> p.name().equalsIgnoreCase(parishName))
                .flatMap(p -> p.getPropertyPolygons().stream())
                .filter(prop -> prop.getOwner().equals(ownerId))
                .collect(Collectors.toList());
    }

    /**
     * Returns a list of all unique owner IDs present in the property data.
     *
     * @return A list of unique owner IDs.
     */
    @CyclomaticComplexity(1)
    public List<String> getOwnerIds() {
        return districts.stream()
                .flatMap(district -> district.getAllPropertyPolygons().stream())
                .map(PropertyPolygon::getOwner)
                .distinct()
                .collect(Collectors.toList());
    }


    /**
     * Returns the names of all districts.
     *
     * @return A list of district names.
     */
    @CyclomaticComplexity(3)
    public List<String> getDistrictNames() {
        if (districts == null || districts.isEmpty()) {
            return new ArrayList<>();
        }
        return districts.stream()
                .map(District::name)
                .collect(Collectors.toList());
    }

    /**
     * Returns the names of all municipalities in the specified district.
     *
     * @param districtName The name of the district to filter municipalities by.
     * @return A list of municipality names in the specified district.
     */
    @CyclomaticComplexity(3)
    public List<String> getMunicipalityNames(String districtName) {
        if (districtName == null || districtName.isBlank()) {
            throw new IllegalArgumentException("District name cannot be null or empty");
        }
        return districts.stream()
                .filter(district -> district.name().equalsIgnoreCase(districtName))
                .flatMap(district -> district.getMunicipalities().stream())
                .map(Municipality::name)
                .collect(Collectors.toList());
    }

    /**
     * Returns the names of all parishes in the specified municipality.
     *
     * @param municipalityName The name of the municipality to filter parishes by.
     * @return A list of parish names in the specified municipality.
     */
    @CyclomaticComplexity(3)
    public List<String> getParishNames(String municipalityName) {
        if (municipalityName == null || municipalityName.isBlank()) {
            throw new IllegalArgumentException("Municipality name cannot be null or empty");
        }
        return districts.stream()
                .flatMap(district -> district.getMunicipalities().stream())
                .filter(municipality -> municipality.name().equalsIgnoreCase(municipalityName))
                .flatMap(municipality -> municipality.getParishes().stream())
                .map(Parish::name)
                .collect(Collectors.toList());
    }

    /**
     * Filters PropertyPolygons by district name.
     *
     * @param districtName The name of the district to filter by.
     * @return A list of PropertyPolygon objects in the specified district.
     */
    @CyclomaticComplexity(3)
    public List<PropertyPolygon> filterByDistrict(String districtName) {
        if (districtName == null || districtName.isBlank()) {
            throw new IllegalArgumentException("District name cannot be null or empty");
        }
        return districts.stream()
                .filter(district -> district.name().equalsIgnoreCase(districtName))
                .flatMap(district -> district.getAllPropertyPolygons().stream())
                .collect(Collectors.toList());
    }

    /**
     * Filters PropertyPolygons by municipality name.
     *
     * @param municipalityName The name of the municipality to filter by.
     * @return A list of PropertyPolygon objects in the specified municipality.
     */
    @CyclomaticComplexity(3)
    public List<PropertyPolygon> filterByMunicipality(String municipalityName) {
        if (municipalityName == null || municipalityName.isBlank()) {
            throw new IllegalArgumentException("Municipality name cannot be null or empty");
        }
        return districts.stream()
                .flatMap(district -> district.getMunicipalities().stream())
                .filter(municipality -> municipality.name().equalsIgnoreCase(municipalityName))
                .flatMap(municipality -> municipality.getAllPropertyPolygons().stream())
                .collect(Collectors.toList());
    }

    /**
     * Filters PropertyPolygons by parish name.
     *
     * @param parishName The name of the parish to filter by.
     * @return A list of PropertyPolygon objects in the specified parish.
     */
    @CyclomaticComplexity(3)
    public List<PropertyPolygon> filterByParish(String parishName) {
        if (parishName == null || parishName.isBlank()) {
            throw new IllegalArgumentException("Parish name cannot be null or empty");
        }
        return districts.stream()
                .flatMap(district -> district.getMunicipalities().stream())
                .flatMap(municipality -> municipality.getParishes().stream())
                .filter(parish -> parish.name().equalsIgnoreCase(parishName))
                .flatMap(parish -> parish.getPropertyPolygons().stream())
                .collect(Collectors.toList());
    }

}
