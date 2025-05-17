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
        validateHeaders(headers);

        validateDataRows(data);
    }

    /**
     * Validates the headers of the CSV file.
     *
     * @param headers the array of header strings
     * @throws CsvException if the headers are incomplete or invalid
     */
    private void validateHeaders(String[] headers) throws CsvException {
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
    }
    /**
     * Validates all data rows in the CSV file except the header row.
     *
     * @param data the list of string arrays representing the CSV data
     * @throws CsvException if any row is invalid
     */
    private void validateDataRows(List<String[]> data) throws CsvException {
        boolean hasErrors = false;

        for (int i = 1; i < data.size(); i++) {
            boolean rowHasError = validateDataRow(data.get(i), i + 1);
            if (rowHasError) {
                hasErrors = true;
            }
        }

        if (hasErrors) {
            throw new CsvException("CSV contains invalid data rows. See log for details.");
        }
    }

    /**
     * Validates a single data row in the CSV file.
     *
     * @param row the array of strings representing a data row
     * @param lineNumber the line number of the row in the CSV file (1-based)
     * @return true if there was any error in the row
     */
    private boolean validateDataRow(String[] row, int lineNumber) {
        boolean hasError = false;

        System.out.println("🧪 Linha " + lineNumber + ":");
        for (int i = 0; i < row.length; i++) {
            System.out.println("   Coluna [" + i + "]: '" + row[i] + "'");
        }

        if (row.length != 10) {
            CsvLogger.logError("Line " + lineNumber + " has an invalid number of columns.");
            return true;
        }

        try {
            if (!row[1].matches("\\d+(\\.\\d+)?")) {
                CsvLogger.logError("PAR_ID invalid in line " + lineNumber);
                hasError = true;
            }
            if (!row[3].matches("\\d+(\\.\\d+)?")) {
                CsvLogger.logError("Shape_Length invalid in line " + lineNumber);
                hasError = true;
            }
            if (!row[4].matches("\\d+(\\.\\d+)?")) {
                CsvLogger.logError("Shape_Area invalid in line " + lineNumber);
                hasError = true;
            }
            if (!row[5].matches("^MULTIPOLYGON\\s*\\(\\(.*\\)\\)")) {
                CsvLogger.logError("Invalid geometry format in line " + lineNumber + ": " + row[5]);
                hasError = true;
            }

            for (int idx = 7; idx <= 9; idx++) {
                String value = row[idx].trim().toLowerCase();
                if (value.isEmpty() || value.equals("na") || value.equals("nd") ||
                        value.equals("0") || value.equals("null")) {
                    CsvLogger.logError("Invalid region data (Freguesia/Municipio/Ilha) in line " + lineNumber + ": " + row[idx]);
                    hasError = true;
                }
            }
        } catch (Exception e) {
            CsvLogger.logError("Error while validating line " + lineNumber + ": " + e.getMessage());
            hasError = true;
        }

        return hasError;
    }

}