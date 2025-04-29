package Services;

import Models.Municipality;
import Models.Parish;
import Models.PropertyPolygon;
import Models.District;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class PropertyCollector {

    private final List<District> districts;

    public PropertyCollector(List<District> districts) {
        this.districts = districts;
    }

    /**
     * Collects all PropertyPolygons from all districts.
     *
     * @return A list of PropertyPolygon objects from all districts.
     */
    public List<PropertyPolygon> collectAllProperties() {
        List<PropertyPolygon> allPolygons = new ArrayList<>();
        for (District district : districts) {
            allPolygons.addAll(district.getAllPropertyPolygons());
        }
        return allPolygons;
    }

    /**
     * Returns the names of all districts.
     *
     * @return A list of district names.
     */
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
