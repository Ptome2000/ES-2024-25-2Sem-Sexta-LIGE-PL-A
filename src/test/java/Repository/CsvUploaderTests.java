package Repository;

import org.junit.jupiter.api.Test;
import java.io.IOException;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class CsvUploaderTests {

    /**
     * Tests the uploadCsv method of the CsvUploader class.
     * Ensures that the method correctly reads a CSV file and returns non-null, non-empty data.
     */
    @Test
    void uploadCsv() {
        CsvUploader uploader = new CsvUploader();
        String filePath = "src/main/resources/teste.csv";

        try {
            List<String[]> data = uploader.uploadCsv(filePath);
            assertNotNull(data, "Data should not be null");
            assertFalse(data.isEmpty(), "Data should not be empty");
        } catch (IOException e) {
            fail("IOException should not occur: " + e.getMessage());
        }
    }

}