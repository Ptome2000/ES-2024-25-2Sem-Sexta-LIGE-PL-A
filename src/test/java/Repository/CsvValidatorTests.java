package Repository;

import io.qameta.allure.Description;
import io.qameta.allure.Feature;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * This class contains unit tests for the {@link CsvValidator} class.
 * It validates the behavior of the CsvValidator class, including
 * its ability to validate CSV file structure, headers, and data rows.
 * Each test case is designed to cover specific functionality and edge cases, ensuring
 * the robustness of the CsvValidator implementation.
 *
 * <p><strong>Author:</strong> Ptome2000</p>
 * <p><strong>Date:</strong> 15/05/2025</p>
 *
 * <p><strong>Cyclomatic Complexity:</strong></p>
 * <ul>
 *     <li>validate: 2</li>
 *     <li>validateHeaders: 12</li>
 *     <li>validateDataRows: 2</li>
 *     <li>validateDataRow: 7</li>
 * </ul>
 */
@Feature("CSV Importation")
class CsvValidatorTests {

    CsvValidator validator;
    static String[] exampleData;
    static String[] validHeaders;

    /**
     * Initializes the example data and valid headers before all tests.
     */
    @BeforeAll
    static void init() {
        exampleData =  new String[]{"1", "12345", "67890", "100.0", "200.0", "POINT(1 1)", "Owner1", "Freguesia1", "Municipio1", "Ilha1"};
        validHeaders = new String[]{"OBJECTID", "PAR_ID", "PAR_NUM", "Shape_Length", "Shape_Area",
                "geometry", "OWNER", "Freguesia", "Municipio", "Ilha"};
    }

    /**
     * Sets up the CsvValidator instance before each test.
     */
    @BeforeEach
    void setUp() {
        validator = new CsvValidator();
    }


    @Test
    @DisplayName("Empty CSV Data Throws Exception")
    @Description("Validates that an empty CSV file triggers a CsvException with the correct message.")
    @Severity(SeverityLevel.BLOCKER)
    void emptyData() {
        List<String[]> data = List.of();
        Exception exception = assertThrows(CsvException.class, () -> {
            validator.validate(data);
        });

        String expectedMessage = "csv file is empty!";
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    @DisplayName("Valid CSV Data Passes Validation")
    @Description("Checks that a CSV file with correct headers and data rows does not throw any exception.")
    @Severity(SeverityLevel.CRITICAL)
    void correctData() {
        List<String[]> data = List.of(validHeaders, exampleData);
        assertDoesNotThrow(() -> validator.validate(data));
    }

    @Nested
    @DisplayName("Header Validation Tests")
    @Severity(SeverityLevel.CRITICAL)
    class HeaderTests {

        @Test
        @DisplayName("Header With Fewer Than 10 Columns")
        @Description("Ensures a CsvException is thrown when the header row has fewer than 10 columns.")
        @Severity(SeverityLevel.NORMAL)
        void headerFewerThan10Columns() {
            List<String[]> data = List.of(
                    new String[]{"OBJECTID", "PAR_ID", "PAR_NUM", "Shape_Length", "Shape_Area", "geometry", "OWNER", "Freguesia", "Municipio"},
                    exampleData
            );

            Exception exception = assertThrows(CsvException.class, () -> {
                validator.validate(data);
            });

            String actualMessage = exception.getMessage();
            assertTrue(actualMessage.contains("incomplete or invalid headers."));
        }

        @Test
        @DisplayName("Header With More Than 10 Columns")
        @Description("Ensures a CsvException is thrown when the header row has more than 10 columns.")
        @Severity(SeverityLevel.NORMAL)
        void headerMoreThan10Columns() {
            List<String[]> data = List.of(
                    new String[]{"OBJECTID", "PAR_ID", "PAR_NUM", "Shape_Length", "Shape_Area", "geometry", "OWNER", "Freguesia", "Municipio", "Ilha", "ExtraColumn"},
                    exampleData
            );

            Exception exception = assertThrows(CsvException.class, () -> {
                validator.validate(data);
            });

            String actualMessage = exception.getMessage();
            assertTrue(actualMessage.contains("incomplete or invalid headers."));
        }

        @Test
        @DisplayName("Incorrect OBJECTID Header")
        @Description("Ensures a CsvException is thrown when the OBJECTID header is incorrect.")
        @Severity(SeverityLevel.NORMAL)
        void headerObjectIdIsIncorrect() {
            List<String[]> data = List.of(
                    new String[]{"BAD_OBJECTID", "PAR_ID", "PAR_NUM", "Shape_Length", "Shape_Area",
                            "geometry", "OWNER", "Freguesia", "Municipio", "Ilha"}, exampleData
            );

            CsvException exception = assertThrows(CsvException.class, () -> validator.validate(data));
            assertTrue(exception.getMessage().contains("invalid headers"));
        }

        @Test
        @DisplayName("Incorrect PAR_ID Header")
        @Description("Ensures a CsvException is thrown when the PAR_ID header is incorrect.")
        @Severity(SeverityLevel.NORMAL)
        void headerParIdIsIncorrect() {
            List<String[]> data = List.of(
                    new String[]{"OBJECTID", "BAD_PAR_ID", "PAR_NUM", "Shape_Length", "Shape_Area",
                            "geometry", "OWNER", "Freguesia", "Municipio", "Ilha"}, exampleData
            );

            CsvException exception = assertThrows(CsvException.class, () -> validator.validate(data));
            assertTrue(exception.getMessage().contains("invalid headers"));
        }

        @Test
        @DisplayName("Incorrect PAR_NUM Header")
        @Description("Ensures a CsvException is thrown when the PAR_NUM header is incorrect.")
        @Severity(SeverityLevel.NORMAL)
        void headerParNumIsIncorrect() {
            List<String[]> data = List.of(
                    new String[]{"OBJECTID", "PAR_ID", "BAD_PAR_NUM", "Shape_Length", "Shape_Area",
                            "geometry", "OWNER", "Freguesia", "Municipio", "Ilha"}, exampleData
            );

            CsvException exception = assertThrows(CsvException.class, () -> validator.validate(data));
            assertTrue(exception.getMessage().contains("invalid headers"));
        }

        @Test
        @DisplayName("Incorrect Shape_Length Header")
        @Description("Ensures a CsvException is thrown when the Shape_Length header is incorrect.")
        @Severity(SeverityLevel.NORMAL)
        void headerShapeLengthIsIncorrect() {
            List<String[]> data = List.of(
                    new String[]{"OBJECTID", "PAR_ID", "PAR_NUM", "BAD_Shape_Length", "Shape_Area",
                            "geometry", "OWNER", "Freguesia", "Municipio", "Ilha"}, exampleData
            );

            CsvException exception = assertThrows(CsvException.class, () -> validator.validate(data));
            assertTrue(exception.getMessage().contains("invalid headers"));
        }

        @Test
        @DisplayName("Incorrect Shape_Area Header")
        @Description("Ensures a CsvException is thrown when the Shape_Area header is incorrect.")
        @Severity(SeverityLevel.NORMAL)
        void headerShapeAreaIsIncorrect() {
            List<String[]> data = List.of(
                    new String[]{"OBJECTID", "PAR_ID", "PAR_NUM", "Shape_Length", "BAD_Shape_Area",
                            "geometry", "OWNER", "Freguesia", "Municipio", "Ilha"}, exampleData
            );

            CsvException exception = assertThrows(CsvException.class, () -> validator.validate(data));
            assertTrue(exception.getMessage().contains("invalid headers"));
        }

        @Test
        @DisplayName("Incorrect geometry Header")
        @Description("Ensures a CsvException is thrown when the geometry header is incorrect.")
        @Severity(SeverityLevel.NORMAL)
        void headerGeometryIsIncorrect() {
            List<String[]> data = List.of(
                    new String[]{"OBJECTID", "PAR_ID", "PAR_NUM", "Shape_Length", "Shape_Area",
                            "BAD_geometry", "OWNER", "Freguesia", "Municipio", "Ilha"}, exampleData
            );

            CsvException exception = assertThrows(CsvException.class, () -> validator.validate(data));
            assertTrue(exception.getMessage().contains("invalid headers"));
        }

        @Test
        @DisplayName("Incorrect OWNER Header")
        @Description("Ensures a CsvException is thrown when the OWNER header is incorrect.")
        @Severity(SeverityLevel.NORMAL)
        void headerOwnerIsIncorrect() {
            List<String[]> data = List.of(
                    new String[]{"OBJECTID", "PAR_ID", "PAR_NUM", "Shape_Length", "Shape_Area",
                            "geometry", "BAD_OWNER", "Freguesia", "Municipio", "Ilha"}, exampleData
            );

            CsvException exception = assertThrows(CsvException.class, () -> validator.validate(data));
            assertTrue(exception.getMessage().contains("invalid headers"));
        }

        @Test
        @DisplayName("Incorrect OWNER Header")
        @Description("Ensures a CsvException is thrown when the OWNER header is incorrect.")
        @Severity(SeverityLevel.NORMAL)
        void headerFreguesiaIsIncorrect() {
            List<String[]> data = List.of(
                    new String[]{"OBJECTID", "PAR_ID", "PAR_NUM", "Shape_Length", "Shape_Area",
                            "geometry", "OWNER", "BAD_Freguesia", "Municipio", "Ilha"}, exampleData
            );

            CsvException exception = assertThrows(CsvException.class, () -> validator.validate(data));
            assertTrue(exception.getMessage().contains("invalid headers"));
        }

        @Test
        @DisplayName("Incorrect Municipio Header")
        @Description("Ensures a CsvException is thrown when the Municipio header is incorrect.")
        @Severity(SeverityLevel.NORMAL)
        void headerMunicipioIsIncorrect() {
            List<String[]> data = List.of(
                    new String[]{"OBJECTID", "PAR_ID", "PAR_NUM", "Shape_Length", "Shape_Area",
                            "geometry", "OWNER", "Freguesia", "BAD_Municipio", "Ilha"}, exampleData
            );

            CsvException exception = assertThrows(CsvException.class, () -> validator.validate(data));
            assertTrue(exception.getMessage().contains("invalid headers"));
        }

        @Test
        @DisplayName("Incorrect Ilha Header")
        @Description("Ensures a CsvException is thrown when the Ilha header is incorrect.")
        @Severity(SeverityLevel.NORMAL)
        void headerIlhaIsIncorrect() {
            List<String[]> data = List.of(
                    new String[]{"OBJECTID", "PAR_ID", "PAR_NUM", "Shape_Length", "Shape_Area",
                            "geometry", "OWNER", "Freguesia", "Municipio", "BAD_Ilha"}, exampleData
            );

            CsvException exception = assertThrows(CsvException.class, () -> validator.validate(data));
            assertTrue(exception.getMessage().contains("invalid headers"));
        }
    }

    @Nested
    @DisplayName("Data Row Validation Tests")
    @Severity(SeverityLevel.NORMAL)
    class DataRowTests {

        @Test
        @DisplayName("Data Row With Fewer Than 10 Columns")
        @Description("Checks that a data row with fewer than 10 columns does not throw an exception.")
        @Severity(SeverityLevel.MINOR)
        void dataRowFewerThan10Columns() {
            List<String[]> data = List.of( validHeaders,
                    new String[]{"1", "12345", "67890", "100.0", "200.0", "POINT(1 1)", "Owner1", "Freguesia1"}
            );
            assertDoesNotThrow(() -> validator.validate(data));
        }

        @Test
        @DisplayName("Data Row With More Than 10 Columns")
        @Description("Checks that a data row with more than 10 columns does not throw an exception, as extra columns are ignored.")
        @Severity(SeverityLevel.MINOR)
        void dataRowMoreThan10Columns() {
            List<String[]> data = List.of(validHeaders,
                    new String[]{"1", "12345", "67890", "100.0", "200.0", "POINT(1 1)", "Owner1", "Freguesia1", "Municipio1", "Ilha1", "ExtraColumn"}
            );

            assertDoesNotThrow(() -> validator.validate(data));
        }

        @Test
        @DisplayName("Invalid PAR_ID In Data Row")
        @Description("Ensures that a data row with an invalid PAR_ID format does not throw an exception, but logs an error.")
        @Severity(SeverityLevel.MINOR)
        void parIdIsInvalid() {
            List<String[]> data = List.of(validHeaders,
                    new String[]{"1", "INVALID_PAR_ID", "67890", "100.0", "200.0", "POINT(1 1)", "Owner1", "Freguesia1", "Municipio1", "Ilha1"}
            );

            assertDoesNotThrow(() -> validator.validate(data));
        }

        @Test
        @DisplayName("Invalid Shape_Length In Data Row")
        @Description("Ensures that a data row with an invalid Shape_Length format does not throw an exception, but logs an error.")
        @Severity(SeverityLevel.MINOR)
        void shapeLengthIsInvalid() {
            List<String[]> data = List.of(validHeaders,
                    new String[]{"1", "12345", "67890", "INVALID_Shape_Length", "200.0", "POINT(1 1)", "Owner1", "Freguesia1", "Municipio1", "Ilha1"}
            );

            assertDoesNotThrow(() -> validator.validate(data));
        }

        @Test
        @DisplayName("Invalid Shape_Area In Data Row")
        @Description("Ensures that a data row with an invalid Shape_Area format does not throw an exception, but logs an error.")
        @Severity(SeverityLevel.MINOR)
        void shapeAreaIsInvalid() {
            List<String[]> data = List.of(validHeaders,
                    new String[]{"1", "12345", "67890", "100.0", "INVALID_Shape_Area", "POINT(1 1)", "Owner1", "Freguesia1", "Municipio1", "Ilha1"}
            );

            assertDoesNotThrow(() -> validator.validate(data));
        }

        @Test
        @DisplayName("Invalid Geometry In Data Row")
        @Description("Ensures that a data row with an invalid geometry format does not throw an exception, but logs an error.")
        @Severity(SeverityLevel.MINOR)
        void geometryIsInvalid() {
            List<String[]> data = List.of(validHeaders,
                    new String[]{"1", "12345", "67890", "100.0", "200.0", "INVALID_GEOMETRY", "Owner1", "Freguesia1", "Municipio1", "Ilha1"}
            );

            assertDoesNotThrow(() -> validator.validate(data));
        }
    }

}