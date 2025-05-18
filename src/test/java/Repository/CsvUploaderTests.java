package Repository;

import io.qameta.allure.Description;
import io.qameta.allure.Feature;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import java.io.IOException;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

/**
 * This class contains unit tests for the {@link CsvUploader} class.
 * It verifies the correct uploading and reading of CSV files, including handling of valid files,
 * empty files, and invalid file paths. Each test ensures the robustness and correctness
 * of the CsvUploader implementation.
 *
 * <p><strong>Author:</strong> Ptome2000</p>
 * <p><strong>Date:</strong> 15/05/2025</p>
 */
@Feature("CSV Importation")
@DisplayName("CSV Uploader Tests")
class CsvUploaderTests {

    private final CsvUploader uploader = new CsvUploader();

    @Test
    @DisplayName("Upload and read valid CSV file")
    @Description("Ensures that the CsvUploader correctly uploads and reads a valid CSV file.")
    @Severity(SeverityLevel.CRITICAL)
    void uploadValidFile() throws IOException {
        String filePath = "src/main/resources/tests/valid.csv";
        List<String[]> result = uploader.uploadCsv(filePath);

        assertNotNull(result, "Result should not be null");
        assertEquals(2, result.size(), "Expected 2 rows in the CSV file");
        assertArrayEquals(new String[]{
                "OBJECTID", "PAR_ID", "PAR_NUM", "Shape_Length", "Shape_Area",
                "geometry", "OWNER", "Freguesia", "Municipio", "Ilha"
        }, result.get(0), "Header row does not match expected values");
    }

    @Test
    @DisplayName("Upload and handle empty CSV file")
    @Description("Ensures that the CsvUploader correctly handles an empty CSV file.")
    @Severity(SeverityLevel.NORMAL)
    void uploadEmptyFile() throws IOException {
        String filePath = "src/main/resources/empty_teste.csv";
        List<String[]> result = uploader.uploadCsv(filePath);

        assertNotNull(result, "Result should not be null");
        assertTrue(result.isEmpty(), "Result should be empty for an empty file");
    }

    @Test
    @DisplayName("Handle invalid file path")
    @Description("Ensures that the CsvUploader throws an IOException for an invalid file path.")
    @Severity(SeverityLevel.CRITICAL)
    void uploadInvalidFilePath() {
        String filePath = "src/main/resources/non_existent_file.csv";

        assertThrows(IOException.class, () -> uploader.uploadCsv(filePath),
                "Expected IOException for nonexistent file path");
    }
}