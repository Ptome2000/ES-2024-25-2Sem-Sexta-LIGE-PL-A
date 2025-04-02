package UploadCSV;

import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class CsvLogger {

    // Método para obter o timestamp atual formatado
    private static String getTimestamp() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        return now.format(formatter);
    }

    // Método para registrar erros
    public static void logError(String message) {
        try (FileWriter writer = new FileWriter("csv_errors.log", true)) {
            // Escrever timestamp antes de cada erro registrado
            writer.write("[" + getTimestamp() + "] " + message + "\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Método para registrar o início do processo com timestamp
    public static void logStart() {
        try (FileWriter writer = new FileWriter("csv_errors.log", true)) {
            writer.write("=== Início da execução: " + getTimestamp() + " ===\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Método para registrar o fim do processo com timestamp
    public static void logEnd() {
        try (FileWriter writer = new FileWriter("csv_errors.log", true)) {
            writer.write("=== Fim da execução: " + getTimestamp() + " ===\n\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}