package Models;

import Utils.Annotations.CyclomaticComplexity;
import Utils.Annotations.Layer;
import Utils.Enums.LayerType;
import java.util.ArrayList;
import java.util.List;


/**
 * The {@code Parish} class represents a parish within a municipality.
 * It contains a list of property polygons and provides methods to manage them.
 * This class implements the {@link Region} interface.
 */
@Layer(LayerType.BACK_END)
public class Parish implements Region {

    private final String name;
    private final List<PropertyPolygon> propertyPolygons;
    private double urbanizationScore;


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
    @CyclomaticComplexity(1)
    public String name() {
        return name;
    }

    /**
     * Gets the list of PropertyPolygons in the parish.
     *
     * @return A list of PropertyPolygon objects.
     */
    @CyclomaticComplexity(1)
    public List<PropertyPolygon> getPropertyPolygons() {
        return propertyPolygons;
    }

    /**
     * Adds a PropertyPolygon to the parish.
     *
     * @param propertyPolygon The PropertyPolygon to add.
     */
    @CyclomaticComplexity(1)
    public void addPropertyPolygon(PropertyPolygon propertyPolygon) {
        this.propertyPolygons.add(propertyPolygon);
    }

    /**
     * Gets the count of PropertyPolygons in the parish.
     *
     * @return The count of PropertyPolygons.
     */
    @CyclomaticComplexity(1)
    public int getPropertyPolygonCount() {
        return propertyPolygons.size();
    }

    /**
     * Gets the name of the municipality to which the parish belongs.
     *
     * @return The name of the municipality.
     */
    public double getUrbanizationScore() { return urbanizationScore; }

    /**
     * Sets the urbanization score for the parish.
     *
     * @param urbanizationScore The urbanization score to set.
     */
    public void setUrbanizationScore(double urbanizationScore) { this.urbanizationScore = urbanizationScore; }


}
