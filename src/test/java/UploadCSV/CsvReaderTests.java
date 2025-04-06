package UploadCSV;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.util.List;

class CsvReaderTests {

    /**
     * Tests the readCsv method with a valid CSV file.
     * Ensures that the method returns non-null data with the expected number of rows and correct headers.
     */
    @Test
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

    /**
     * Tests the readCsv method with an empty CSV file.
     * Ensures that the method returns non-null, empty data.
     */
    @Test
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

    /**
     * Tests the readCsv method with an invalid file path.
     * Ensures that the method throws an IOException.
     */
    @Test
    void invalidFilePath() {
        String filePath = "src/main/resources/non_existent_file.csv";

        CsvReader reader = new CsvReader();
        assertThrows(IOException.class, () -> {
            reader.readCsv(filePath);
        });
    }

}