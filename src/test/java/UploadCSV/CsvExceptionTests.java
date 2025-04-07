package UploadCSV;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CsvExceptionTests {

    /**
     * Tests the getMessage method of the CsvException class.
     * Ensures that the message returned is the same as the one passed during instantiation.
     */
    @Test
    void validateGetMessage() {
        String expectedMessage = "Custom error occurred!";
        CsvException exception = new CsvException(expectedMessage);
        assertEquals(expectedMessage, exception.getMessage(), "The error message should be the same as the one passed during instantiation.");
    }

    /**
     * Tests the toString method of the CsvException class.
     * Ensures that the toString method returns the correct format.
     */
    @Test
    void validateToString() {
        String expectedMessage = "Custom error occurred!";
        CsvException exception = new CsvException(expectedMessage);
        String expectedToString = "CsvException: " + expectedMessage;
        assertEquals(expectedToString, exception.toString(), "The toString method should return the correct format.");
    }

    /**
     * Tests the instantiation of the CsvException class.
     * Ensures that the CsvException object is instantiated successfully.
     */
    @Test
    void validateInstantiation() {
        String expectedMessage = "Custom error occurred!";
        CsvException exception = new CsvException(expectedMessage);
        assertNotNull(exception, "CsvException should be instantiated successfully.");
    }

}