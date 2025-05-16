package Repository;

import io.qameta.allure.Description;
import io.qameta.allure.Feature;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import org.junit.jupiter.api.DisplayName;
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
 *
 * <p><strong>Cyclomatic Complexity:</strong></p>
 * <ul>
 *     <li>uploadCsv: 1</li>
 * </ul>
 */
@Feature("CSV Importation")
class CsvUploaderTests {

    @Test
    @DisplayName("Validate Uploading Valid CSV File")
    @Description("Ensures that the uploadCsv method correctly reads a valid CSV file and returns non-null, non-empty data.")
    @Severity(SeverityLevel.CRITICAL)
    void uploadValidCsv() {
        CsvUploader uploader = new CsvUploader();
        String filePath = "src/main/resources/tests/valid.csv";

        try {
            List<String[]> data = uploader.uploadCsv(filePath);
            assertNotNull(data, "Data should not be null");
            assertFalse(data.isEmpty(), "Data should not be empty");
        } catch (IOException e) {
            fail("IOException should not occur: " + e.getMessage());
        }
    }

    @Test
    @DisplayName("Validate Uploading Empty CSV File")
    @Description("Ensures that the uploadCsv method correctly handles an empty CSV file and returns an empty list.")
    @Severity(SeverityLevel.NORMAL)
    void uploadEmptyCsv() {
        CsvUploader uploader = new CsvUploader();
        String filePath = "src/main/resources/empty_teste.csv";

        try {
            List<String[]> data = uploader.uploadCsv(filePath);
            assertNotNull(data, "Data should not be null");
            assertTrue(data.isEmpty(), "Data should be empty");
        } catch (IOException e) {
            fail("IOException should not occur: " + e.getMessage());
        }
    }

    @Test
    @DisplayName("Validate Handling of Invalid File Path")
    @Description("Ensures that the uploadCsv method throws an IOException when provided with an invalid file path.")
    @Severity(SeverityLevel.CRITICAL)
    void uploadInvalidFilePath() {
        CsvUploader uploader = new CsvUploader();
        String filePath = "src/main/resources/non_existent_file.csv";

        assertThrows(IOException.class, () -> {
            uploader.uploadCsv(filePath);
        });
    }
}