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
     */
    private void validateDataRows(List<String[]> data) {
        for (int i = 1; i < data.size(); i++) {
            String[] row = data.get(i);
            validateDataRow(row, i + 1);
        }
    }

    /**
     * Validates a single data row in the CSV file.
     *
     * @param row the array of strings representing a data row
     * @param lineNumber the line number of the row in the CSV file (1-based)
     */
    private void validateDataRow(String[] row, int lineNumber) {
        if (row.length != 10) {
            CsvLogger.logError("line " + lineNumber + " has an invalid number of columns.");
            return;
        }

        try {
            if (!row[1].matches("\\d+(\\.\\d+)?")) {
                CsvLogger.logError("PAR_ID invalid in line " + lineNumber);
            }
            if (!row[3].matches("\\d+(\\.\\d+)?")) {
                CsvLogger.logError("Shape_Length invalid in line " + lineNumber);
            }
            if (!row[4].matches("\\d+(\\.\\d+)?")) {
                CsvLogger.logError("Shape_Area invalid in line " + lineNumber);
            }
            if (!row[5].matches("^MULTIPOLYGON\\s*\\(\\(.*\\)\\)")) {
                CsvLogger.logError("Formato de geometria invalid in line " + lineNumber + ": " + row[5]);
            }
        } catch (Exception e) {
            CsvLogger.logError("error while validating line " + lineNumber + ": " + e.getMessage());
        }
    }
}