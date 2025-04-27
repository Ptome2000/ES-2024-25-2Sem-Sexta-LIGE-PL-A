package Repository;

import java.io.IOException;
import java.util.List;

/**
 * The CsvUploader class is responsible for uploading and reading CSV files.
 */
public class CsvUploader {

    /**
     * Uploads and reads the contents of a CSV file.
     *
     * @param filePath the path to the CSV file
     * @return a list of string arrays representing the CSV data
     * @throws IOException if an I/O error occurs while reading the file
     */
    public List<String[]> uploadCsv(String filePath) throws IOException {
        // Correctly instantiating CsvReader
        CsvReader reader = new CsvReader();
        return reader.readCsv(filePath); // Passing the path directly
    }
}