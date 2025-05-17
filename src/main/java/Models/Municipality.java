package Models;

import java.util.ArrayList;
import java.util.List;

/**
 * The Municipality class represents a municipality within a district.
 * It contains the name of the municipality and a list of parishes.
 */
public class Municipality implements Region {

    private final String name;
    private final List<Parish> parishes;
    private double tourismScore;


    /**
     * Constructor to initialize the Municipality object.
     *
     * @param name The name of the municipality.
     */
    public Municipality(String name) {
        this.name = name;
        this.parishes = new ArrayList<>();
    }

    /**
     * Gets the name of the municipality.
     *
     * @return The name of the municipality.
     */
    public String name() {
        return name;
    }

    /**
     * Gets the list of parishes within the municipality.
     *
     * @return A list of Parish objects representing the parishes in the municipality.
     */
    public List<Parish> getParishes() {
        return parishes;
    }

    /**
     * Adds a parish to the municipality.
     *
     * @param parish The Parish object to be added to the municipality.
     */
    public void addParish(Parish parish) {
        this.parishes.add(parish);
    }

    /**
     * Gets the tourism score for the municipality.
     *
     * @return The tourism score of the municipality.
     */
    public double getTourismScore() { return tourismScore; }

    /**
     * Sets the tourism score for the municipality.
     *
     * @param tourismScore The tourism score to be set.
     */
    public void setTourismScore(double tourismScore) { this.tourismScore = tourismScore; }


    /**
     * Gets the list of all PropertyPolygons in the municipality.
     *
     * @return A list of PropertyPolygon objects from all parishes in the municipality.
     */
    public List<PropertyPolygon> getAllPropertyPolygons() {
        List<PropertyPolygon> allPolygons = new ArrayList<>();
        for (Parish parish : parishes) {
            allPolygons.addAll(parish.getPropertyPolygons());
        }
        return allPolygons;
    }

}
