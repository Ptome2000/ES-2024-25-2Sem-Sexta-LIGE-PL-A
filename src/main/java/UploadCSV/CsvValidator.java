package UploadCSV;

import java.util.List;

public class CsvValidator {

    public void validate(List<String[]> data) throws CsvException {
        if (data.isEmpty()) {
            CsvLogger.logError("Ficheiro CSV está vazio!");
            throw new CsvException("Ficheiro CSV está vazio!");
        }

        String[] headers = data.get(0);

        // Validar cabeçalhos
        if (headers.length != 10) {
            CsvLogger.logError("Cabeçalhos incompletos ou inválidos.");
            throw new CsvException("Cabeçalhos incompletos ou inválidos.");
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
            CsvLogger.logError("Cabeçalhos inválidos!");
            throw new CsvException("Cabeçalhos inválidos!");
        }

        // Validar as linhas de dados (a partir da segunda linha)
        for (int i = 1; i < data.size(); i++) {
            String[] row = data.get(i);

            // Verificar se a linha tem 10 colunas
            if (row.length != 10) {
                CsvLogger.logError("Linha " + (i + 1) + " tem número incorreto de colunas.");
                continue; // Pular para a próxima linha
            }

            try {
                // Validar valores numéricos
                if (!row[1].matches("\\d+(\\.\\d+)?")) { // PAR_ID: numérico, pode ser decimal
                    CsvLogger.logError("PAR_ID inválido na linha " + (i + 1));
                }

                if (!row[3].matches("\\d+(\\.\\d+)?")) { // Shape_Length: numérico
                    CsvLogger.logError("Shape_Length inválido na linha " + (i + 1));
                }

                if (!row[4].matches("\\d+(\\.\\d+)?")) { // Shape_Area: numérico
                    CsvLogger.logError("Shape_Area inválido na linha " + (i + 1));
                }

                // Validar a geometria (MULTIPOLYGON WKT)
                if (!row[5].matches("^MULTIPOLYGON\\s*\\(\\(.*\\)\\)")) { // Exemplo simples para verificar MULTIPOLYGON
                    CsvLogger.logError("Formato de geometria inválido na linha " + (i + 1) + ": " + row[5]);
                }

            } catch (Exception e) {
                // Qualquer erro dentro do try (caso falhe a validação de uma linha)
                CsvLogger.logError("Erro ao validar a linha " + (i + 1) + ": " + e.getMessage());
            }
        }
    }
}