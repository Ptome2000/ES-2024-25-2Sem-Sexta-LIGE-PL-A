package UploadCSV;

import com.opencsv.exceptions.CsvException;
import java.io.IOException;
import java.util.List;

public class CsvProcessor {
    public static void main(String[] args) {
        CsvUploader uploader = new CsvUploader();
        CsvValidator validator = new CsvValidator();

        try {
            // Registrar o início do processo
            CsvLogger.logStart();

            // Carregar dados do CSV
            List<String[]> data = uploader.uploadCsv("src/main/resources/Madeira-Moodle-1.1.csv");

            // Validar os dados
            validator.validate(data);
            System.out.println("Ficheiro carregado e validado com sucesso!");

            // Exibir os dados (apenas para fins de teste)
            for (String[] row : data) {
                for (String cell : row) {
                    System.out.print(cell + " | ");
                }
                System.out.println();
            }

            // Registrar o fim do processo
            CsvLogger.logEnd();

        } catch (IOException e) {
            // Lidar com erros de leitura de arquivo
            CsvLogger.logError("Erro ao ler o arquivo CSV: " + e.getMessage());
            System.err.println("Erro ao ler o arquivo: " + e.getMessage());
        } catch (UploadCSV.CsvException e) {
            // Lidar com erros personalizados definidos em CsvException
            CsvLogger.logError("Erro de validação do CSV: " + e.getMessage());
            System.err.println("Erro de validação do CSV: " + e.getMessage());
        } catch (Exception e) {
            // Capturar qualquer outra exceção inesperada
            CsvLogger.logError("Erro inesperado: " + e.getMessage());
            System.err.println("Erro inesperado: " + e.getMessage());
        }
    }
}