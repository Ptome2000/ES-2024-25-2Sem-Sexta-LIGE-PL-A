package Repository;

import io.qameta.allure.Description;
import io.qameta.allure.Feature;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Step;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

/**
 * This class contains unit tests for the {@link CsvLogger} class.
 * It validates the behavior of the CsvLogger class, including
 * its ability to log errors, start and end messages, and validate timestamps.
 * Each test case is designed to cover specific functionality and edge cases, ensuring
 * the robustness of the CsvLogger implementation.
 *
 * <p><strong>Author:</strong> Ptome2000</p>
 * <p><strong>Date:</strong> 14/05/2025</p>
 */
@Feature("CSV Importation")
@DisplayName("CSV Logger Tests")
class CsvLoggerTests {

    static final Path LOG_FILE_PATH = Path.of("csv_errors.log");

    @BeforeEach
    @Step("Setting up the test environment by clearing the log file")
    void setUp() {
        try {
            Files.write(LOG_FILE_PATH, new byte[0]);
        } catch (IOException e) {
            fail("Failed to clear log file: " + e.getMessage());
        }
    }

    @Nested
    @DisplayName("CSV Logging Timestamp Tests")
    class TimestampTests {

        @Test
        @DisplayName("Validate Timestamp Format")
        @Description("Ensures that the timestamp is not null, not empty, and matches the expected format.")
        @Severity(SeverityLevel.NORMAL)
        void correctTimeStamp() {
            String timestamp = CsvLogger.getTimestamp();
            assertNotNull(timestamp);
            assertFalse(timestamp.isEmpty());

            String regex = "\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2}";
            assertTrue(timestamp.matches(regex), "Timestamp does not match the expected format");
        }
    }

    @Nested
    @DisplayName("CSV Logging Functionality Tests")
    class LoggingTests {

        @Test
        @DisplayName("Validate Log Error")
        @Description("Ensures that the error message is correctly logged to the log file.")
        @Severity(SeverityLevel.CRITICAL)
        void validateLogError() {
            String errorMessage = "Test error message";
            CsvLogger.logError(errorMessage);
            try {
                String logContent = Files.readString(LOG_FILE_PATH);
                assertTrue(logContent.contains(errorMessage), "Log file does not contain the expected error message");
            } catch (IOException e) {
                fail("Failed to read log file: " + e.getMessage());
            }
        }

        @Test
        @DisplayName("Validate Log Start")
        @Description("Ensures that the start message is correctly logged to the log file.")
        @Severity(SeverityLevel.NORMAL)
        void validateLogStart() {
            CsvLogger.logStart();
            try {
                String logContent = Files.readString(LOG_FILE_PATH);
                assertTrue(logContent.contains("=== start of execution: "), "Log file does not contain the expected start message");
            } catch (IOException e) {
                fail("Failed to read log file: " + e.getMessage());
            }
        }

        @Test
        @DisplayName("Validate Log End")
        @Description("Ensures that the end message is correctly logged to the log file.")
        @Severity(SeverityLevel.NORMAL)
        void validateLogEnd() {
            CsvLogger.logEnd();
            try {
                String logContent = Files.readString(LOG_FILE_PATH);
                assertTrue(logContent.contains("=== end of execution: "), "Log file does not contain the expected end message");
            } catch (IOException e) {
                fail("Failed to read log file: " + e.getMessage());
            }
        }
    }

}