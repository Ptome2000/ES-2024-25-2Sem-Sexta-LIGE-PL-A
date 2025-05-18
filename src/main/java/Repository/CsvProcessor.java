package Repository;

import Services.PropertyScoreCalculator;
import Models.District;
import Models.Municipality;
import Models.Parish;
import Models.PropertyPolygon;
import Utils.Annotations.CyclomaticComplexity;
import Utils.Annotations.Layer;
import Utils.Enums.LayerType;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * The {@code CsvProcessor} class processes CSV data related to geographical regions, municipalities, and parishes.
 * It converts the CSV data into a list of District objects, each containing a list of municipalities and parishes.
 */
@Layer(LayerType.BACK_END)
public class CsvProcessor {

    /**
     * Converts CSV data into a list of District objects, each containing a list of municipalities and parishes.
     *
     * @param filePath the path to the CSV file
     * @return a list of District objects
     * @throws IOException if an I/O error occurs
     */
    @CyclomaticComplexity(6)
    public static List<District> convertToRegionsAndProperties(String filePath) throws IOException {
        Map<String, District> districtMap = new HashMap<>();
        CsvLogger.logStart();
        List<String[]> csvData = validateCsvData(filePath);

        for (int i = 1; i < csvData.size(); i++) {
            String[] columns = csvData.get(i);

            if (!validRegion(columns)) {
                CsvLogger.logError("Line " + i + " has invalid region data.");
                continue;
            }

            String districtName = columns[CsvColum.DISTRICT.getIndex()]; // "Ilha" or "Distrito"
            String municipalityName = columns[CsvColum.MUNICIPALITY.getIndex()]; // "Municipio"
            String parishName = columns[CsvColum.PARISH.getIndex()]; // "Freguesia"

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
                parish.addPropertyPolygon(property);
            }
        }

        CsvLogger.logEnd();

        List<PropertyPolygon> allProperties = districtMap.values().stream()
                .flatMap(d -> d.getMunicipalities().stream())
                .flatMap(m -> m.getParishes().stream())
                .flatMap(p -> p.getPropertyPolygons().stream())
                .toList();

        PropertyScoreCalculator.assignScoresToRegions(new ArrayList<>(districtMap.values()), allProperties);


        return new ArrayList<>(districtMap.values());
    }

    /**
     * Validates if the region data is valid.
     *
     * @param columns the columns of the CSV row
     * @return true if the region data is valid, false otherwise
     */
    @CyclomaticComplexity(3)
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
    @CyclomaticComplexity(2)
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
