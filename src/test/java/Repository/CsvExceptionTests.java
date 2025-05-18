package Repository;

import io.qameta.allure.Description;
import io.qameta.allure.Feature;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * This class contains unit tests for the {@link CsvException} class.
 * It validates the behavior of the CsvException class, including
 * its ability to return the correct error message and string representation.
 * Each test case is designed to cover specific functionality and edge cases, ensuring
 * the robustness of the CsvException implementation.
 *
 * <p><strong>Author:</strong> Ptome2000</p>
 * <p><strong>Date:</strong> 14/05/2025</p>
 */
@Feature("CSV Importation")
@DisplayName("CSV Exception Tests")
class CsvExceptionTests {

    @Test
    @DisplayName("Validate getMessage Method")
    @Description("Ensures that the getMessage method returns the same message passed during instantiation.")
    @Severity(SeverityLevel.CRITICAL)
    void validateGetMessage() {
        String expectedMessage = "Custom error occurred!";
        CsvException exception = new CsvException(expectedMessage);
        assertEquals(expectedMessage, exception.getMessage(), "The error message should be the same as the one passed during instantiation.");
    }

    @Test
    @DisplayName("Validate toString Method")
    @Description("Ensures that the toString method returns the correct format for the exception.")
    @Severity(SeverityLevel.NORMAL)
    void validateToString() {
        String expectedMessage = "Custom error occurred!";
        CsvException exception = new CsvException(expectedMessage);
        String expectedToString = "CsvException: " + expectedMessage;
        assertEquals(expectedToString, exception.toString(), "The toString method should return the correct format.");
    }

    @Test
    @DisplayName("Validate CsvException Instantiation")
    @Description("Ensures that the CsvException object is instantiated successfully.")
    @Severity(SeverityLevel.MINOR)
    void validateInstantiation() {
        String expectedMessage = "Custom error occurred!";
        CsvException exception = new CsvException(expectedMessage);
        assertNotNull(exception, "CsvException should be instantiated successfully.");
    }

}