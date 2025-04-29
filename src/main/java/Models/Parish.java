package Models;

import java.util.ArrayList;
import java.util.List;

/**
 * The Parish class represents a parish within a municipality.
 * It contains the name of the parish and a list of PropertyPolygons.
 */
public class Parish implements Region {

    private final String name;
    private final List<PropertyPolygon> propertyPolygons;

    /**
     * Constructor to initialize the Parish object.
     *
     * @param name The name of the parish.
     */
    public Parish(String name) {
        this.name = name;
        this.propertyPolygons = new ArrayList<>();
    }

    /**
     * Gets the name of the parish.
     *
     * @return The name of the parish.
     */
    @Override
    public String name() {
        return name;
    }

    /**
     * Gets the list of PropertyPolygons in the parish.
     *
     * @return A list of PropertyPolygon objects.
     */
    public List<PropertyPolygon> getPropertyPolygons() {
        return propertyPolygons;
    }

    /**
     * Adds a PropertyPolygon to the parish.
     *
     * @param propertyPolygon The PropertyPolygon to add.
     */
    public void addPropertyPolygon(PropertyPolygon propertyPolygon) {
        this.propertyPolygons.add(propertyPolygon);
    }

    /**
     * Gets the count of PropertyPolygons in the parish.
     *
     * @return The count of PropertyPolygons.
     */
    public int getPropertyPolygonCount() {
        return propertyPolygons.size();
    }

}