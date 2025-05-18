package Repository;

import Utils.Annotations.CyclomaticComplexity;
import Utils.Annotations.Layer;
import Utils.Enums.LayerType;
import java.io.IOException;
import java.util.List;

/**
 * The {@code CsvUploader} class provides methods to upload and read CSV files.
 * It includes a method to upload a CSV file and return its contents as a list of string arrays.
 */
@Layer(LayerType.BACK_END)
public class CsvUploader {

    /**
     * Uploads and reads the contents of a CSV file.
     *
     * @param filePath the path to the CSV file
     * @return a list of string arrays representing the CSV data
     * @throws IOException if an I/O error occurs while reading the file
     */
    @CyclomaticComplexity(1)
    public List<String[]> uploadCsv(String filePath) throws IOException {
        // Correctly instantiating CsvReader
        CsvReader reader = new CsvReader();
        return reader.readCsv(filePath); // Passing the path directly
    }
}