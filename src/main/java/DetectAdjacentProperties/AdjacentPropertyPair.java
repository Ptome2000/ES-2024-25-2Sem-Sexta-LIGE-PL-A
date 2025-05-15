package DetectAdjacentProperties;
import Models.PropertyPolygon;

import java.util.List;

/**
 * Represents a pair of adjacent properties identified by their unique property IDs.
 * This class is used to store and manage two properties that are adjacent to each other.
 */

public class AdjacentPropertyPair {
    private final int propertyId1;
    private final int propertyId2;

    /**
     * Constructs an AdjacentPropertyPair with the given property IDs.
     *
     * @param propertyId1 The ID of the first property.
     * @param propertyId2 The ID of the second property.
     */

    public AdjacentPropertyPair(int propertyId1, int propertyId2) {
        this.propertyId1 = propertyId1;
        this.propertyId2 = propertyId2;
    }

    /**
     * Retrieves the owners of the properties in the pair.
     *
     * @param properties List of PropertyPolygon objects to search for owners.
     * @return An array containing the owners of the two properties.
     */
    public String[] getOwners(List<PropertyPolygon> properties) {
        String owner1 = null;
        String owner2 = null;

        for (PropertyPolygon property : properties) {
            if (property.getObjectId() == propertyId1) {
                owner1 = property.getOwner();
            } else if (property.getObjectId() == propertyId2) {
                owner2 = property.getOwner();
            }

            if (owner1 != null && owner2 != null) break;
        }

        return new String[]{ owner1, owner2 };
    }

    /**
     * Retrieves the ID of the first property in the pair.
     *
     * @return The ID of the first property.
     */
    public long getPropertyId1() {
        return propertyId1;
    }

    /**
     * Retrieves the ID of the second property in the pair.
     *
     * @return The ID of the second property.
     */
    public long getPropertyId2() {
        return propertyId2;
    }

    /**
     * Provides a string representation of the AdjacentPropertyPair object.
     *
     * @return A string representation of the adjacent property pair.
     */

    @Override
    public String toString() {
        return "AdjacentPropertyPair{" +
                "propertyId1=" + propertyId1 +
                ", propertyId2=" + propertyId2 +
                '}';
    }



}

