package Repository;

import Models.District;
import Models.Municipality;
import Models.Parish;
import Models.PropertyPolygon;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

/**
 * The CsvProcessor class is responsible for processing CSV files containing property data.
 * It converts the CSV data into a structured format of Districts, Municipalities, and Parishes with their respective properties.
 */
public class CsvProcessor {

    /**
     * Converts CSV data into a list of District objects, each containing a list of municipalities and parishes.
     *
     * @param filePath the path to the CSV file
     * @return a list of District objects
     * @throws IOException if an I/O error occurs
     */
    public static List<District> convertToRegionsAndProperties(String filePath) throws IOException {
        Map<String, District> districtMap = new HashMap<>();
        CsvLogger.logStart();
        List<String[]> csvData = validateCsvData(filePath);

        for (int i = 1; i < csvData.size(); i++) { // Skip header row
            String[] columns = csvData.get(i);

            // Skip rows with invalid data
            if (!validRegion(columns)) {
                CsvLogger.logError("Line " + i + " has invalid region data.");
                continue;
            }

            String districtName = columns[9]; // "Ilha" or "Distrito"
            String municipalityName = columns[8]; // "Municipio"
            String parishName = columns[7]; // "Freguesia"

            // Get or create the district
            District district = districtMap.computeIfAbsent(districtName, District::new);

            // Get or create the municipality
            Municipality municipality = district.getMunicipalities().stream()
                    .filter(m -> m.name().equals(municipalityName))
                    .findFirst()
                    .orElseGet(() -> {
                        Municipality newMunicipality = new Municipality(municipalityName);
                        district.addMunicipality(newMunicipality);
                        return newMunicipality;
                    });

            // Get or create the parish
            Parish parish = municipality.getParishes().stream()
                    .filter(p -> p.name().equals(parishName))
                    .findFirst()
                    .orElseGet(() -> {
                        Parish newParish = new Parish(parishName);
                        municipality.addParish(newParish);
                        return newParish;
                    });

            // Create the PropertyPolygon and add it to the parish
            PropertyPolygon property = PropertyPolygon.fromCsvRow(columns);
            if (property != null) {
                if (property.getPolygon().getVertices().isEmpty()) {
                    CsvLogger.logError("Polygon without vertices in row " + (i + 1));
                    continue;  // Skip properties without vertices
                }
                if (property.getOwner().isEmpty()) {
                    CsvLogger.logError("Polygon without owner in row " + (i + 1));
                    continue;
                }
                if (Stream.of(property.getFreguesia(), property.getMunicipio(), property.getIlha()).anyMatch(val -> val.equalsIgnoreCase("NA"))) {
                    continue; // Skip properties with "NA" in key fields
                }
                parish.addPropertyPolygon(property);
            }
        }

        CsvLogger.logEnd();
        return new ArrayList<>(districtMap.values());
    }

    /**
     * Validates if the region data is valid.
     *
     * @param columns the columns of the CSV row
     * @return true if the region data is valid, false otherwise
     */
    private static boolean validRegion(String[] columns) {
        return !columns[7].equalsIgnoreCase("NA") &&
                !columns[8].equalsIgnoreCase("NA") &&
                !columns[9].equalsIgnoreCase("NA");
    }

    /**
     * Validates the CSV data by checking for empty rows and invalid headers.
     *
     * @param filePath the path to the CSV file
     * @return a list of string arrays representing the validated CSV data
     * @throws IOException if an I/O error occurs
     */
    private static List<String[]> validateCsvData(String filePath) throws IOException {
        CsvUploader uploader = new CsvUploader();
        List<String[]> data = uploader.uploadCsv(filePath);
        try {
            CsvValidator validator = new CsvValidator();
            validator.validate(data);
        } catch (Repository.CsvException e) {
            CsvLogger.logError("CSV validation error: " + e.getMessage());
            throw new RuntimeException("CSV validation error: " + e.getMessage());
        }
        return data;
    }
}
