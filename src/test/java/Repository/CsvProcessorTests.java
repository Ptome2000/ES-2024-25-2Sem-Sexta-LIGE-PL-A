package Repository;

import Models.District;
import io.qameta.allure.Description;
import io.qameta.allure.Feature;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;


import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.util.List;

/**
 * This class contains unit tests for the {@link CsvProcessor} class.
 * It validates the behavior of the CsvProcessor class, including
 * its ability to process CSV files and handle various scenarios.
 * Each test case is designed to cover specific functionality and edge cases, ensuring
 * the robustness of the CsvProcessor implementation.
 *
 * <p><strong>Author:</strong> Ptome2000</p>
 * <p><strong>Date:</strong> 15/05/2025</p>
 *
 * <p><strong>Cyclomatic Complexity:</strong></p>
 * <ul>
 *     <li>convertToRegionsAndProperties: 6</li>
 *     <li>validRegion: 3</li>
 *     <li>validateCsvData: 2</li>
 * </ul>
 */
@Feature("CSV Importation")
class CsvProcessorTests {

    private static final String VALID_CSV_PATH = "src/main/resources/tests/valid.csv";
    private static final String INVALID_CSV_PATH = "src/main/resources/tests/invalid.csv";

    @Test
    @DisplayName("Convert valid CSV data to regions and properties")
    @Description("Ensures that valid CSV data is correctly converted into District, Municipality, and Parish objects.")
    @Severity(SeverityLevel.CRITICAL)
    void testConvertToRegionsAndProperties() throws IOException {
        List<District> districts = CsvProcessor.convertToRegionsAndProperties(VALID_CSV_PATH);

        assertNotNull(districts, "Districts list should not be null.");
        assertFalse(districts.isEmpty(), "Districts list should not be empty.");
        assertEquals(1, districts.size(), "There should be one district.");
        assertEquals(1, districts.get(0).getMunicipalities().size(), "There should be one municipality in the district.");
        assertEquals(1, districts.get(0).getMunicipalities().get(0).getParishes().size(), "There should be one parish in the municipality.");
        assertEquals(1, districts.get(0).getMunicipalities().get(0).getParishes().get(0).getPropertyPolygonCount(), "There should be one property polygon in the parish.");
    }

    @Test
    @DisplayName("Handle invalid CSV data gracefully")
    @Description("Ensures that invalid CSV data is logged and skipped without crashing the application.")
    @Severity(SeverityLevel.NORMAL)
    void testHandleInvalidCsvData() throws IOException {
        List<District> districts = CsvProcessor.convertToRegionsAndProperties(INVALID_CSV_PATH);

        assertNotNull(districts, "Districts list should not be null.");
        assertTrue(districts.isEmpty(), "Districts list should be empty for invalid data.");
    }

    @Test
    @DisplayName("Handle CSV file with missing headers")
    @Description("Ensures that the CsvProcessor throws an exception when the CSV file has missing headers.")
    @Severity(SeverityLevel.CRITICAL)
    void testMissingHeaders() {
        String filePath = "src/main/resources/tests/missing_headers.csv";

        Exception exception = assertThrows(RuntimeException.class, () -> {
            CsvProcessor.convertToRegionsAndProperties(filePath);
        });

        assertTrue(exception.getMessage().contains("CSV validation error"), "Exception message should indicate a validation error.");
    }

    @Test
    @DisplayName("Handle CSV file with duplicate entries")
    @Description("Ensures that duplicate entries in the CSV file are processed correctly without duplication in the output.")
    @Severity(SeverityLevel.NORMAL)
    void testDuplicateEntries() throws IOException {
        String filePath = "src/main/resources/tests/duplicate_entries.csv";
        List<District> districts = CsvProcessor.convertToRegionsAndProperties(filePath);

        assertNotNull(districts, "Districts list should not be null.");
        assertEquals(1, districts.size(), "There should be one district.");
        assertEquals(1, districts.get(0).getMunicipalities().size(), "There should be one municipality in the district.");
        assertEquals(1, districts.get(0).getMunicipalities().get(0).getParishes().size(), "There should be one parish in the municipality.");
        assertEquals(1, districts.get(0).getMunicipalities().get(0).getParishes().get(0).getPropertyPolygonCount(), "Duplicate entries should not result in duplicate property polygons.");
    }

    @Test
    @DisplayName("Handle CSV file with no valid rows")
    @Description("Ensures that the CsvProcessor returns an empty list when the CSV file contains no valid rows.")
    @Severity(SeverityLevel.NORMAL)
    void testNoValidRows() throws IOException {
        String filePath = "src/main/resources/tests/no_valid_rows.csv";
        List<District> districts = CsvProcessor.convertToRegionsAndProperties(filePath);

        assertNotNull(districts, "Districts list should not be null.");
        assertTrue(districts.get(0).getAllPropertyPolygons().isEmpty(), "Property polygons list should be empty when there are no valid rows.");
    }

}