package Repository;


//CsvException.java → Define exceções personalizadas.
public class CsvException extends Exception {

    private String message;

    public CsvException(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    @Override
    public String toString() {
        return "CsvException: " + message;
    }

}
