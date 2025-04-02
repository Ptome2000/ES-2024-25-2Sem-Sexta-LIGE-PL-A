package UploadCSV;

import java.io.IOException;
import java.util.List;

// CsvUploader.java → Responsável pelo upload e leitura do CSV.
public class CsvUploader {

    public List<String[]> uploadCsv(String filePath) throws IOException {
        // Corrigido: instanciando CsvReader corretamente
        CsvReader reader = new CsvReader();
        return reader.readCsv(filePath); // Passando o caminho diretamente
    }
}