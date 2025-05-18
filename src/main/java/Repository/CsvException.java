package Repository;


import Utils.Annotations.CyclomaticComplexity;
import Utils.Annotations.Layer;
import Utils.Enums.LayerType;

/**
 * The {@code CsvColum} enum represents the columns in a CSV file related to geographical data.
 * Each enum constant corresponds to a specific column in the CSV file, identified by its index.
 */
@Layer(LayerType.BACK_END)
public class CsvException extends Exception {

    private final String message;

    /**
     * Constructor to initialize the CsvException with a specific message.
     *
     * @param message The message describing the exception.
     */
    public CsvException(String message) {
        this.message = message;
    }

    /**
     * Gets the message associated with the exception.
     *
     * @return The exception message.
     */
    @CyclomaticComplexity(1)
    public String getMessage() {
        return message;
    }

    /**
     * Returns a string representation of the CsvException.
     *
     * @return A string representation of the exception.
     */
    @Override
    @CyclomaticComplexity(1)
    public String toString() {
        return "CsvException: " + message;
    }

}
