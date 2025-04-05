package UploadCSV;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

class CsvLoggerTests {

    static final Path LOG_FILE_PATH = Path.of("csv_errors.log");

    @BeforeEach
    void setUp() {
        try {
            Files.write(LOG_FILE_PATH, new byte[0]);
        } catch (IOException e) {
            fail("Failed to clear log file: " + e.getMessage());
        }
    }

    @Test
    void correctTimeStamp() {
        String timestamp = CsvLogger.getTimestamp();
        assertNotNull(timestamp);
        assertFalse(timestamp.isEmpty());

        String regex = "\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2}";
        assertTrue(timestamp.matches(regex), "Timestamp does not match the expected format");
    }

    @Test
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
    void validatelogEnd() {
        CsvLogger.logEnd();

        try {
            String logContent = Files.readString(LOG_FILE_PATH);
            assertTrue(logContent.contains("=== end of execution: "), "Log file does not contain the expected end message");
        } catch (IOException e) {
            fail("Failed to read log file: " + e.getMessage());
        }
    }

}