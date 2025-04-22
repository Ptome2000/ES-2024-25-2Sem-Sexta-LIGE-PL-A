package DetectAdjacentProperties;

import UploadCSV.CsvLogger;
import UploadCSV.CsvUploader;
import UploadCSV.CsvValidator;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * This class is responsible for managing property ownership data.
 * It groups properties by their owners and provides methods to filter and retrieve property information.
 */
public class PropertyOwnership {

    final private Map<String, List<PropertyPolygon>> propertiesByOwner;

    /**
     * Constructor to initialize the PropertyOwnership object.
     *
     * @param properties List of PropertyPolygon objects.
     */
    public PropertyOwnership(List<PropertyPolygon> properties) {
        this.propertiesByOwner = groupPropertiesByOwner(properties);
    }

    /**
     * Groups properties by their owner.
     *
     * @param properties List of PropertyPolygon objects.
     * @return A map where the key is the owner's name and the value is a list of PropertyPolygon objects owned by that owner.
     */
    private Map<String, List<PropertyPolygon>> groupPropertiesByOwner(List<PropertyPolygon> properties) {
        return properties.stream()
                .collect(Collectors.groupingBy(PropertyPolygon::getOwner));
    }

    /**
     * Filters the map of properties by a specific owner.
     *
     * @param owner The name of the owner to filter by.
     * @return A list of PropertyPolygon objects owned by the specified owner, or null if the owner does not exist.
     */
    public List<PropertyPolygon> filterByOwner(String owner) {
        return propertiesByOwner.getOrDefault(owner, java.util.Collections.emptyList());
    }

    /**
     * Generates a sorted list of unique owners along with the number of properties they own.
     *
     * @return A sorted list of owners and their property counts.
     */
    public List<Map.Entry<String, Integer>> getOwnerPropertyCounts() {
        return propertiesByOwner.entrySet().stream()
                .map(entry -> Map.entry(entry.getKey(), entry.getValue().size()))
                .sorted(Map.Entry.comparingByKey())
                .collect(Collectors.toList());
    }

    public static void main(String[] args) {
        CsvUploader uploader = new CsvUploader();
        CsvValidator validator = new CsvValidator();

        try {
            // Start logging
            CsvLogger.logStart();

            // Upload and validate the CSV file
            List<String[]> data = uploader.uploadCsv("src/main/resources/Madeira-Moodle-1.1.csv");
            validator.validate(data);
            System.out.println("Ficheiro carregado e validado com sucesso!");

            // Convert CSV data to property polygons and detect adjacency
            List<PropertyPolygon> properties = AdjacencyDetector.convertToProperties(data);
            List<AdjacentPropertyPair> adjacentProperties = AdjacencyDetector.findAdjacentProperties(properties);

            // Print the number of adjacent properties
            PropertyOwnership ownership = new PropertyOwnership(properties);
            List<Map.Entry<String, Integer>> ownerPropertyCounts = ownership.getOwnerPropertyCounts();

            System.out.println("Owner List:");
            ownerPropertyCounts.forEach(entry ->
                    System.out.println("Owner: " + entry.getKey() + ", Properties: " + entry.getValue())
            );

            CsvLogger.logEnd();

        } catch (IOException e) {
            CsvLogger.logError("Erro ao ler ficheiro CSV: " + e.getMessage());
            System.err.println("Erro ao ler ficheiro: " + e.getMessage());
        } catch (UploadCSV.CsvException e) {
            CsvLogger.logError("Erro ao validar ficheiro CSV: " + e.getMessage());
            System.err.println("Erro ao validar ficheiro: " + e.getMessage());
        } catch (Exception e) {
            CsvLogger.logError("Erro inesperado: " + e.getMessage());
            System.err.println("Erro inesperado: " + e.getMessage());
        }
    }

}
