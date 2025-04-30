package Repository;

import java.util.List;

/**
 * The CsvValidator class provides methods to validate the contents of a CSV file.
 */
public class CsvValidator {

    /**
     * Validates the contents of the CSV file.
     *
     * @param data the list of string arrays representing the CSV data
     * @throws CsvException if the CSV file is empty or contains invalid headers
     */
    public void validate(List<String[]> data) throws CsvException {
        if (data.isEmpty()) {
            CsvLogger.logError("csv file is empty!");
            throw new CsvException("csv file is empty!");
        }

        String[] headers = data.get(0);

        // Validate headers
        if (headers.length != 10) {
            CsvLogger.logError("incomplete or invalid headers.");
            throw new CsvException("incomplete or invalid headers.");
        }

        if (!headers[0].equalsIgnoreCase("OBJECTID") ||
                !headers[1].equalsIgnoreCase("PAR_ID") ||
                !headers[2].equalsIgnoreCase("PAR_NUM") ||
                !headers[3].equalsIgnoreCase("Shape_Length") ||
                !headers[4].equalsIgnoreCase("Shape_Area") ||
                !headers[5].equalsIgnoreCase("geometry") ||
                !headers[6].equalsIgnoreCase("OWNER") ||
                !headers[7].equalsIgnoreCase("Freguesia") ||
                !headers[8].equalsIgnoreCase("Municipio") ||
                !headers[9].equalsIgnoreCase("Ilha")) {
            CsvLogger.logError("invalid headers!");
            throw new CsvException("invalid headers!");
        }

        // Validate data rows (starting from the second row)
        for (int i = 1; i < data.size(); i++) {
            String[] row = data.get(i);
            System.out.println(data.get(i));

            // Check if the row has 10 columns
            if (row.length != 10) {
                CsvLogger.logError("line " + (i + 1) + " has an invalid number of columns.");
                continue; // Skip to the next row
            }

            try {
                // Validate numeric values
                if (!row[1].matches("\\d+(\\.\\d+)?")) { // PAR_ID: numeric, can be decimal
                    CsvLogger.logError("PAR_ID invalid in line " + (i + 1));
                }

                if (!row[3].matches("\\d+(\\.\\d+)?")) { // Shape_Length: numeric
                    CsvLogger.logError("Shape_Length invalid in line " + (i + 1));
                }

                if (!row[4].matches("\\d+(\\.\\d+)?")) { // Shape_Area: numeric
                    CsvLogger.logError("Shape_Area invalid in line " + (i + 1));
                }

                // Validate geometry (MULTIPOLYGON WKT)
                if (!row[5].matches("^MULTIPOLYGON\\s*\\(\\(.*\\)\\)")) { // Simple example to check MULTIPOLYGON
                    CsvLogger.logError("Formato de geometria invalid in line " + (i + 1) + ": " + row[5]);
                }

            } catch (Exception e) {
                // Any error within the try (if a row validation fails)
                CsvLogger.logError("error while validating line " + (i + 1) + ": " + e.getMessage());
            }
        }
    }
}