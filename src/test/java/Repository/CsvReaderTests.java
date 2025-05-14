package Repository;

import io.qameta.allure.Description;
import io.qameta.allure.Feature;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.util.List;

@Feature("CSV Importation")
class CsvReaderTests {

    @Test
    @DisplayName("Validate Reading Valid CSV File")
    @Description("Ensures that the readCsv method correctly reads a valid CSV file and returns the expected data.")
    @Severity(SeverityLevel.CRITICAL)
    void validFile() {
        String filePath = "src/main/resources/teste.csv";
        CsvReader reader = new CsvReader();
        try {
            List<String[]> result = reader.readCsv(filePath);

            assertNotNull(result);
            assertEquals(11, result.size());
            assertArrayEquals(new String[] {"OBJECTID", "PAR_ID", "PAR_NUM", "Shape_Length", "Shape_Area",
                    "geometry", "OWNER", "Freguesia", "Municipio", "Ilha"}, result.get(0));
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
        CsvReader reader = new CsvReader();
        try {
            List<String[]> result = reader.readCsv(filePath);

            assertNotNull(result);
            assertTrue(result.isEmpty());
        } catch (IOException e) {
            fail("IOException should not have occurred: " + e.getMessage());
        }
    }

    @Test
    @DisplayName("Validate Handling of Invalid File Path")
    @Description("Ensures that the readCsv method throws an IOException when provided with an invalid file path.")
    @Severity(SeverityLevel.CRITICAL)
    void invalidFilePath() {
        String filePath = "src/main/resources/non_existent_file.csv";
        CsvReader reader = new CsvReader();
        assertThrows(IOException.class, () -> {
            reader.readCsv(filePath);
        });
    }

}