package Repository;

import javax.swing.*;
import Utils.Annotations.CyclomaticComplexity;
import Utils.Annotations.Layer;
import Utils.Enums.LayerType;
import java.util.List;

/**
 * The {@code CsvValidator} class provides methods to validate the contents of a CSV file.
 * It includes methods to check for empty files, validate headers, and validate data rows.
 */
@Layer(LayerType.BACK_END)
public class CsvValidator {

    /**
     * Validates the contents of the CSV file.
     *
     * @param data the list of string arrays representing the CSV data
     * @throws CsvException if the CSV file is empty or contains invalid headers
     */
    @CyclomaticComplexity(2)
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
    @CyclomaticComplexity(12)
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
    @CyclomaticComplexity(2)
    private void validateDataRows(List<String[]> data) throws CsvException {
        int validCount = 0;

        for (int i = 1; i < data.size(); i++) {
            boolean rowHasError = validateDataRow(data.get(i), i + 1);
            if (!rowHasError) {
                validCount++;
            }
        }

        if (validCount == 0) {
            throw new CsvException("CSV contains no valid data rows. Import cancelled.");
        }

        if (validCount < data.size() - 1) {
            JOptionPane.showMessageDialog(null,
                    "Partial import: Some rows were skipped due to errors. Check the log for details.",
                    "Warning", JOptionPane.WARNING_MESSAGE);
        }
    }

    /**
     * Validates a single data row in the CSV file.
     *
     * @param row the array of strings representing a data row
     * @param lineNumber the line number of the row in the CSV file (1-based)
     * @return true if there was any error in the row
     */
    @CyclomaticComplexity(7)
    private boolean validateDataRow(String[] row, int lineNumber) {
        boolean hasError = false;

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