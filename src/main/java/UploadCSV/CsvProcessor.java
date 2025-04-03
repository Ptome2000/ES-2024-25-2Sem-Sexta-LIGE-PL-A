package UploadCSV;

import DetectAdjacentProperties.AdjacencyDetector;
import DetectAdjacentProperties.PropertyPolygon;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * The CsvProcessor class is responsible for processing CSV files by uploading, validating, and logging the process.
 */
public class CsvProcessor {

    /**
     * The main method that initiates the CSV processing.
     *
     * @param args command-line arguments (not used)
     */
    public static void main(String[] args) {
        CsvUploader uploader = new CsvUploader();
        CsvValidator validator = new CsvValidator();

        try {
            // Log the start of the process
            CsvLogger.logStart();

            // Load data from the CSV file
            List<String[]> data = uploader.uploadCsv("src/main/resources/Madeira-Moodle-1.1.csv");

            // Validate the data
            validator.validate(data);
            System.out.println("File uploaded and validated successfully!");

            // Convert data to PropertyPolygon objects
            List<PropertyPolygon> properties = AdjacencyDetector.convertToProperties(data);

//            // Exibir os terrenos carregados
//            for (PropertyPolygon property : properties) {
//                System.out.println(property);
//            }

            // Find adjacent properties
            Map<PropertyPolygon, List<PropertyPolygon>> adjacentProperties = AdjacencyDetector.findAdjacentProperties(properties);

            // Display adjacent properties
            System.out.println("\n======= Terrenos Adjacentes =======");
            for (Map.Entry<PropertyPolygon, List<PropertyPolygon>> entry : adjacentProperties.entrySet()) {
                PropertyPolygon property = entry.getKey();
                List<PropertyPolygon> adjacentList = entry.getValue();

                System.out.println("Terreno: " + property.getObjectId() + " (" + property.getOwner() + ")");
                System.out.println("Adjacentes:");

                for (PropertyPolygon adjacent : adjacentList) {
                    System.out.println(" - " + adjacent.getObjectId() + " (" + adjacent.getOwner() + ")");
                }

                System.out.println("-----------------------------------");
            }

//            // Display the data (for testing purposes only)
//            for (String[] row : data) {
//                for (String cell : row) {
//                    System.out.print(cell + " | ");
//                }
//                System.out.println();
//            }

            // Log the end of the process
            CsvLogger.logEnd();

        } catch (IOException e) {
            // Handle file reading errors
            CsvLogger.logError("Error reading CSV file: " + e.getMessage());
            System.err.println("Error reading file: " + e.getMessage());
        } catch (UploadCSV.CsvException e) {
            // Handle custom validation errors defined in CsvException
            CsvLogger.logError("Error validating CSV file: " + e.getMessage());
            System.err.println("Error validating CSV file: " + e.getMessage());
        } catch (Exception e) {
            // Catch any other unexpected exceptions
            CsvLogger.logError("Unexpected error: " + e.getMessage());
            System.err.println("Unexpected error: " + e.getMessage());
        }
    }
}