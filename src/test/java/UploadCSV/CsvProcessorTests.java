package UploadCSV;


import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class CsvProcessorTests {

    /**
     * Tests the main method of the CsvProcessor class.
     * Ensures that no exceptions are thrown during the execution.
     */
    @Test
    void processorSuccess() {
        assertDoesNotThrow(() -> CsvProcessor.main(new String[] {}));
    }
  
}