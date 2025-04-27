package Repository;

import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * The CsvLogger class provides methods to log errors and process start/end times to a log file.
 */
public class CsvLogger {

    /**
     * Gets the current timestamp formatted as a string.
     *
     * @return the current timestamp in the format "yyyy-MM-dd HH:mm:ss"
     */
    static String getTimestamp() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        return now.format(formatter);
    }

    /**
     * Logs an error message to the log file with a timestamp.
     *
     * @param message the error message to log
     */
    public static void logError(String message) {
        try (FileWriter writer = new FileWriter("csv_errors.log", true)) {
            writer.write("[" + getTimestamp() + "] " + message + "\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Logs the start of the process with a timestamp.
     */
    public static void logStart() {
        try (FileWriter writer = new FileWriter("csv_errors.log", true)) {
            writer.write("=== start of execution: " + getTimestamp() + " ===\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Logs the end of the process with a timestamp.
     */
    public static void logEnd() {
        try (FileWriter writer = new FileWriter("csv_errors.log", true)) {
            writer.write("=== end of execution: " + getTimestamp() + " ===\n\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}