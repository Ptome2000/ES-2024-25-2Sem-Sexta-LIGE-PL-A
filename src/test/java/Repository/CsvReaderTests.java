package Repository;

import io.qameta.allure.Description;
import io.qameta.allure.Feature;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.util.List;

/**
 * This class contains unit tests for the {@link CsvReader} class.
 * It verifies the correct reading of CSV files, including handling of valid files,
 * empty files, and invalid file paths. Each test ensures the robustness and correctness
 * of the CsvReader implementation.
 *
 * <p><strong>Author:</strong> Ptome2000</p>
 * <p><strong>Date:</strong> 15/05/2025</p>
 */
@Feature("CSV Importation")
@DisplayName("CSV Reader Tests")
class CsvReaderTests {

    private final CsvReader reader = new CsvReader();

    @Nested
    @DisplayName("CSV Reader: Valid File Tests")
    class ValidFileTests {

        @Test
        @DisplayName("Validate Reading Valid CSV File")
        @Description("Ensures that the readCsv method correctly reads a valid CSV file and returns the expected data.")
        @Severity(SeverityLevel.CRITICAL)
        void validFile() {
            String filePath = "src/main/resources/tests/valid.csv";
            try {
                List<String[]> result = reader.readCsv(filePath);

                assertNotNull(result, "Result should not be null");
                assertEquals(2, result.size(), "Expected 2 rows in the CSV file");
                assertArrayEquals(new String[]{
                        "OBJECTID", "PAR_ID", "PAR_NUM", "Shape_Length", "Shape_Area",
                        "geometry", "OWNER", "Freguesia", "Municipio", "Ilha"
                }, result.get(0), "Header row does not match expected values");
            } catch (IOException e) {
                fail("IOException should not have occurred: " + e.getMessage());
            }
        }

        @Test
        @DisplayName("Validate Reading Empty CSV File")
        @Description("Ensures that the readCsv method correctly handles an empty CSV file and returns an empty list.")
        @Severity(SeverityLevel.NORMAL)
        void emptyFile() {
            String filePath = "src/main/resources/empty_teste.csv";
            try {
                List<String[]> result = reader.readCsv(filePath);

                assertNotNull(result, "Result should not be null");
                assertTrue(result.isEmpty(), "Result should be empty for an empty file");
            } catch (IOException e) {
                fail("IOException should not have occurred: " + e.getMessage());
            }
        }
    }

    @Nested
    @DisplayName("CSV Reader: Invalid File Tests")
    class InvalidFileTests {

        @Test
        @DisplayName("Validate Handling of Invalid File Path")
        @Description("Ensures that the readCsv method throws an IOException when provided with an invalid file path.")
        @Severity(SeverityLevel.CRITICAL)
        void invalidFilePath() {
            String filePath = "src/main/resources/non_existent_file.csv";

            assertThrows(IOException.class, () -> reader.readCsv(filePath),
                    "Expected IOException for nonexistent file path");
        }
    }

}