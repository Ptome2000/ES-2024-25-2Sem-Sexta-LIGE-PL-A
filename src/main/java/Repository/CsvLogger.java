package Repository;

import Utils.Annotations.CyclomaticComplexity;
import Utils.Annotations.Layer;
import Utils.Enums.LayerType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * The {@code CsvLogger} class provides methods to log messages to a CSV file.
 * It includes methods to log errors, start and end of the process with timestamps.
 */
@Layer(LayerType.BACK_END)
public class CsvLogger {

    private static final Logger logger = LoggerFactory.getLogger(CsvLogger.class);

    /**
     * Gets the current timestamp formatted as a string.
     *
     * @return the current timestamp in the format "yyyy-MM-dd HH:mm:ss"
     */
    @CyclomaticComplexity(1)
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
    @CyclomaticComplexity(2)
    public static void logError(String message) {
        try (FileWriter writer = new FileWriter("csv_errors.log", true)) {
            writer.write("[" + getTimestamp() + "] " + message + "\n");
        } catch (IOException e) {
            logger.error(message, e);
        }
    }

    /**
     * Logs the start of the process with a timestamp.
     */
    @CyclomaticComplexity(2)
    public static void logStart() {
        try (FileWriter writer = new FileWriter("csv_errors.log", true)) {
            writer.write("=== start of execution: " + getTimestamp() + " ===\n");
        } catch (IOException e) {
            logger.error(String.valueOf(e));
        }
    }

    /**
     * Logs the end of the process with a timestamp.
     */
    @CyclomaticComplexity(2)
    public static void logEnd() {
        try (FileWriter writer = new FileWriter("csv_errors.log", true)) {
            writer.write("=== end of execution: " + getTimestamp() + " ===\n\n");
        } catch (IOException e) {
            logger.error(String.valueOf(e));
        }
    }
}