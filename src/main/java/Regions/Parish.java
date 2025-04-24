package Regions;

/**
 * The Parish class represents a parish within a municipality.
 * It contains the name of the parish and implements the Region interface.
 */
public record Parish(String name) implements Region {

    /**
     * Constructor to initialize the Parish object.
     *
     * @param name The name of the parish.
     */
    public Parish {}

    /**
     * Gets the name of the parish.
     *
     * @return The name of the parish.
     */
    @Override
    public String name() {
        return name;
    }

}
