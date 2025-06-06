package DetectAdjacentProperties;

import Models.PropertyPolygon;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * The {@code PropertyOwnership} class provides functionality to manage and analyze property ownership.
 * It groups properties by their owners and provides methods to filter properties by owner and
 * generate a sorted list of owners with their property counts.
 */
@Deprecated
public class PropertyOwnership {

    final private Map<String, List<PropertyPolygon>> propertiesByOwner;

    /**
     * Constructor to initialize the PropertyOwnership object.
     *
     * @param properties List of PropertyPolygon objects.
     */
    @Deprecated
    public PropertyOwnership(List<PropertyPolygon> properties) {
        this.propertiesByOwner = groupPropertiesByOwner(properties);
    }

    /**
     * Groups properties by their owner.
     *
     * @param properties List of PropertyPolygon objects.
     * @return A map where the key is the owner's name and the value is a list of
     *         PropertyPolygon objects owned by that owner.
     */
    @Deprecated
    private Map<String, List<PropertyPolygon>> groupPropertiesByOwner(List<PropertyPolygon> properties) {
        return properties.stream()
                .collect(Collectors.groupingBy(PropertyPolygon::getOwner));
    }

    /**
     * Filters the map of properties by a specific owner.
     *
     * @param owner The name of the owner to filter by.
     * @return A list of PropertyPolygon objects owned by the specified owner, or
     *         null if the owner does not exist.
     */
    @Deprecated
    public List<PropertyPolygon> filterByOwner(String owner) {
        return propertiesByOwner.getOrDefault(owner, java.util.Collections.emptyList());
    }

    /**
     * Generates a sorted list of unique owners along with the number of properties
     * they own.
     *
     * @return A sorted list of owners and their property counts.
     */
    @Deprecated
    public List<Map.Entry<String, Integer>> getOwnerPropertyCounts() {
        return propertiesByOwner.entrySet().stream()
                .map(entry -> Map.entry(entry.getKey(), entry.getValue().size()))
                .sorted(Map.Entry.comparingByKey())
                .collect(Collectors.toList());
    }

}
