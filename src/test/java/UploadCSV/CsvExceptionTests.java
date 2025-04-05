package UploadCSV;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CsvExceptionTests {

    @Test
    void validateGetMessage() {
        String expectedMessage = "Custom error occurred!";
        CsvException exception = new CsvException(expectedMessage);
        assertEquals(expectedMessage, exception.getMessage(), "The error message should be the same as the one passed during instantiation.");
    }

    @Test
    void validateToString() {
        String expectedMessage = "Custom error occurred!";
        CsvException exception = new CsvException(expectedMessage);
        String expectedToString = "CsvException: " + expectedMessage;
        assertEquals(expectedToString, exception.toString(), "The toString method should return the correct format.");
    }

    @Test
    void validateInstantiation() {
        String expectedMessage = "Custom error occurred!";
        CsvException exception = new CsvException(expectedMessage);
        assertNotNull(exception, "CsvException should be instantiated successfully.");
    }

}