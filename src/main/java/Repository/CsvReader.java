package Repository;

import Utils.Annotations.CyclomaticComplexity;
import Utils.Annotations.Layer;
import Utils.Enums.LayerType;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * The {@code CsvReader} class provides methods to read CSV files.
 * It includes a method to read the contents of a CSV file and return the data as a list of string arrays.
 */
@Layer(LayerType.BACK_END)
public class CsvReader {

    /**
     * Reads the contents of a CSV file and returns the data as a list of string arrays.
     *
     * @param filePath the path to the CSV file
     * @return a list of string arrays representing the CSV data
     * @throws IOException if an I/O error occurs while reading the file
     */
    @CyclomaticComplexity(2)
    public List<String[]> readCsv(String filePath) throws IOException {
        List<String[]> data = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                // Split the lines by the delimiter ';'
                String[] values = line.split(";");
                data.add(values);
            }
        }
        return data;
    }
}