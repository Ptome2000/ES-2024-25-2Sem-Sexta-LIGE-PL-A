package Regions;

import java.util.ArrayList;
import java.util.List;

/**
 * The Municipality class represents a municipality within a district.
 * It contains the name of the municipality and a list of parishes.
 */
public class Municipality implements Region {

    private final String name;
    private final List<Parish> parishes;

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

}
