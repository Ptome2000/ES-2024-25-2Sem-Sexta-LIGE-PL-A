package UploadCSV;

import DetectAdjacentProperties.*;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * The CsvProcessor class is responsible for processing CSV files by uploading, validating,
 * and logging the entire process. It manages the reading, validation, and processing of property data
 * and calculates adjacent property pairs.
 */
public class CsvProcessor {

    /**
     * The main method that initiates the CSV processing.
     * It handles the CSV upload, validation, and the detection of adjacent properties.
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

            // Format Properties
            List<PropertyPolygon> properties = AdjacencyDetector.convertToProperties(data);

            // Find adjacent properties
            List<AdjacentPropertyPair> adjacentProperties = AdjacencyDetector.findAdjacentProperties(properties);
            // Calcular o número de ligações únicas (pares de terrenos adjacentes)
            System.out.println("\nTotal de terrenos adjacentes: " + adjacentProperties.size());

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