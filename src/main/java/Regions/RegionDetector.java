package Regions;

import UploadCSV.CsvException;
import UploadCSV.CsvLogger;
import UploadCSV.CsvUploader;
import UploadCSV.CsvValidator;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The RegionDetector class is responsible for converting CSV data into a list of District objects.
 * Each District contains a list of Municipalities, and each Municipality contains a list of Parishes.
 */
public class RegionDetector {

    /**
     * Converts a list of CSV data into a list of District objects.
     *
     * @param csvData the CSV data as a list of string arrays
     * @return a list of District objects
     */
    public static List<District> convertToRegions(List<String[]> csvData) {
        Map<String, District> districtMap = new HashMap<>();
        CsvLogger.logStart();

        for (int i = 1; i < csvData.size(); i++) { // Skip header row
            String[] columns = csvData.get(i);

            // Skip rows with "NA" in relevant columns
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

            // Add the parish
            if (municipality.getParishes().stream().noneMatch(p -> p.name().equals(parishName))) {
                municipality.addParish(new Parish(parishName));
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
     * Main method to test the RegionDetector functionality.
     *
     * @param args command line arguments
     * @throws IOException if an I/O error occurs
     * @throws CsvException if a CSV validation error occurs
     */
    public static void main(String[] args) throws IOException, CsvException {
        CsvUploader uploader = new CsvUploader();
        CsvValidator validator = new CsvValidator();
        List<String[]> data = uploader.uploadCsv("src/main/resources/Madeira-Moodle-1.1.csv");
        validator.validate(data);
        System.out.println("Ficheiro carregado e validado com sucesso!");
        List<District> districts = RegionDetector.convertToRegions(data);

        for (District district : districts) {
            System.out.println("District: " + district.name());
            for (Municipality municipality : district.getMunicipalities()) {
                System.out.println("  Municipality: " + municipality.name());
                for (Parish parish : municipality.getParishes()) {
                    System.out.println("    Parish: " + parish.name());
                }
            }
        }

    }

}
