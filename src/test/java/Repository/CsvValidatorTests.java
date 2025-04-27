package Repository;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

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

    /**
     * Tests the validation of an empty CSV file.
     * Ensures that a CsvException is thrown with the expected message.
     */
    @Test
    void emptyData() {
        List<String[]> data = List.of();
        Exception exception = assertThrows(CsvException.class, () -> {
            validator.validate(data);
        });

        String expectedMessage = "csv file is empty!";
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }

    /**
     * Tests the validation of a CSV file with correct data.
     * Ensures that no exception is thrown.
     */
    @Test
    void correctData() {
        List<String[]> data = List.of(validHeaders, exampleData);
        assertDoesNotThrow(() -> validator.validate(data));
    }

    @Nested
    class HeaderTests {

        /**
         * Tests the validation of a CSV file with fewer than 10 columns in the header.
         * Ensures that a CsvException is thrown with the expected message.
         */
        @Test
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

        /**
         * Tests the validation of a CSV file with more than 10 columns in the header.
         * Ensures that a CsvException is thrown with the expected message.
         */
        @Test
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

        /**
         * Tests the validation of a CSV file with an incorrect OBJECTID header.
         * Ensures that a CsvException is thrown with the expected message.
         */
        @Test
        void headerObjectIdIsIncorrect() {
            List<String[]> data = List.of(
                    new String[]{"BAD_OBJECTID", "PAR_ID", "PAR_NUM", "Shape_Length", "Shape_Area",
                            "geometry", "OWNER", "Freguesia", "Municipio", "Ilha"}, exampleData
            );

            CsvException exception = assertThrows(CsvException.class, () -> validator.validate(data));
            assertTrue(exception.getMessage().contains("invalid headers"));
        }

        /**
         * Tests the validation of a CSV file with an incorrect PAR_ID header.
         * Ensures that a CsvException is thrown with the expected message.
         */
        @Test
        void headerParIdIsIncorrect() {
            List<String[]> data = List.of(
                    new String[]{"OBJECTID", "BAD_PAR_ID", "PAR_NUM", "Shape_Length", "Shape_Area",
                            "geometry", "OWNER", "Freguesia", "Municipio", "Ilha"}, exampleData
            );

            CsvException exception = assertThrows(CsvException.class, () -> validator.validate(data));
            assertTrue(exception.getMessage().contains("invalid headers"));
        }

        /**
         * Tests the validation of a CSV file with an incorrect PAR_NUM header.
         * Ensures that a CsvException is thrown with the expected message.
         */
        @Test
        void headerParNumIsIncorrect() {
            List<String[]> data = List.of(
                    new String[]{"OBJECTID", "PAR_ID", "BAD_PAR_NUM", "Shape_Length", "Shape_Area",
                            "geometry", "OWNER", "Freguesia", "Municipio", "Ilha"}, exampleData
            );

            CsvException exception = assertThrows(CsvException.class, () -> validator.validate(data));
            assertTrue(exception.getMessage().contains("invalid headers"));
        }

        /**
         * Tests the validation of a CSV file with an incorrect Shape_Length header.
         * Ensures that a CsvException is thrown with the expected message.
         */
        @Test
        void headerShapeLengthIsIncorrect() {
            List<String[]> data = List.of(
                    new String[]{"OBJECTID", "PAR_ID", "PAR_NUM", "BAD_Shape_Length", "Shape_Area",
                            "geometry", "OWNER", "Freguesia", "Municipio", "Ilha"}, exampleData
            );

            CsvException exception = assertThrows(CsvException.class, () -> validator.validate(data));
            assertTrue(exception.getMessage().contains("invalid headers"));
        }

        /**
         * Tests the validation of a CSV file with an incorrect Shape_Area header.
         * Ensures that a CsvException is thrown with the expected message.
         */
        @Test
        void headerShapeAreaIsIncorrect() {
            List<String[]> data = List.of(
                    new String[]{"OBJECTID", "PAR_ID", "PAR_NUM", "Shape_Length", "BAD_Shape_Area",
                            "geometry", "OWNER", "Freguesia", "Municipio", "Ilha"}, exampleData
            );

            CsvException exception = assertThrows(CsvException.class, () -> validator.validate(data));
            assertTrue(exception.getMessage().contains("invalid headers"));
        }

        /**
         * Tests the validation of a CSV file with an incorrect geometry header.
         * Ensures that a CsvException is thrown with the expected message.
         */
        @Test
        void headerGeometryIsIncorrect() {
            List<String[]> data = List.of(
                    new String[]{"OBJECTID", "PAR_ID", "PAR_NUM", "Shape_Length", "Shape_Area",
                            "BAD_geometry", "OWNER", "Freguesia", "Municipio", "Ilha"}, exampleData
            );

            CsvException exception = assertThrows(CsvException.class, () -> validator.validate(data));
            assertTrue(exception.getMessage().contains("invalid headers"));
        }

        /**
         * Tests the validation of a CSV file with an incorrect OWNER header.
         * Ensures that a CsvException is thrown with the expected message.
         */
        @Test
        void headerOwnerIsIncorrect() {
            List<String[]> data = List.of(
                    new String[]{"OBJECTID", "PAR_ID", "PAR_NUM", "Shape_Length", "Shape_Area",
                            "geometry", "BAD_OWNER", "Freguesia", "Municipio", "Ilha"}, exampleData
            );

            CsvException exception = assertThrows(CsvException.class, () -> validator.validate(data));
            assertTrue(exception.getMessage().contains("invalid headers"));
        }

        /**
         * Tests the validation of a CSV file with an incorrect Freguesia header.
         * Ensures that a CsvException is thrown with the expected message.
         */
        @Test
        void headerFreguesiaIsIncorrect() {
            List<String[]> data = List.of(
                    new String[]{"OBJECTID", "PAR_ID", "PAR_NUM", "Shape_Length", "Shape_Area",
                            "geometry", "OWNER", "BAD_Freguesia", "Municipio", "Ilha"}, exampleData
            );

            CsvException exception = assertThrows(CsvException.class, () -> validator.validate(data));
            assertTrue(exception.getMessage().contains("invalid headers"));
        }

        /**
         * Tests the validation of a CSV file with an incorrect Municipio header.
         * Ensures that a CsvException is thrown with the expected message.
         */
        @Test
        void headerMunicipioIsIncorrect() {
            List<String[]> data = List.of(
                    new String[]{"OBJECTID", "PAR_ID", "PAR_NUM", "Shape_Length", "Shape_Area",
                            "geometry", "OWNER", "Freguesia", "BAD_Municipio", "Ilha"}, exampleData
            );

            CsvException exception = assertThrows(CsvException.class, () -> validator.validate(data));
            assertTrue(exception.getMessage().contains("invalid headers"));
        }

        /**
         * Tests the validation of a CSV file with an incorrect Ilha header.
         * Ensures that a CsvException is thrown with the expected message.
         */
        @Test
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
    class DataRowTests {

        /**
         * Tests the validation of a CSV file with a data row having fewer than 10 columns.
         * Ensures that no exception is thrown.
         */
        @Test
        void dataRowFewerThan10Columns() {
            List<String[]> data = List.of( validHeaders,
                    new String[]{"1", "12345", "67890", "100.0", "200.0", "POINT(1 1)", "Owner1", "Freguesia1"}
            );
            assertDoesNotThrow(() -> validator.validate(data));
        }

        /**
         * Tests the validation of a CSV file with a data row having more than 10 columns.
         * Ensures that no exception is thrown.
         */
        @Test
        void dataRowMoreThan10Columns() {
            List<String[]> data = List.of(validHeaders,
                    new String[]{"1", "12345", "67890", "100.0", "200.0", "POINT(1 1)", "Owner1", "Freguesia1", "Municipio1", "Ilha1", "ExtraColumn"}
            );

            assertDoesNotThrow(() -> validator.validate(data));
        }

        /**
         * Tests the validation of a CSV file with an invalid PAR_ID in a data row.
         * Ensures that no exception is thrown.
         */
        @Test
        void parIdIsInvalid() {
            List<String[]> data = List.of(validHeaders,
                    new String[]{"1", "INVALID_PAR_ID", "67890", "100.0", "200.0", "POINT(1 1)", "Owner1", "Freguesia1", "Municipio1", "Ilha1"}
            );

            assertDoesNotThrow(() -> validator.validate(data));
        }

        /**
         * Tests the validation of a CSV file with an invalid Shape_Length in a data row.
         * Ensures that no exception is thrown.
         */
        @Test
        void shapeLengthIsInvalid() {
            List<String[]> data = List.of(validHeaders,
                    new String[]{"1", "12345", "67890", "INVALID_Shape_Length", "200.0", "POINT(1 1)", "Owner1", "Freguesia1", "Municipio1", "Ilha1"}
            );

            assertDoesNotThrow(() -> validator.validate(data));
        }

        /**
         * Tests the validation of a CSV file with an invalid Shape_Area in a data row.
         * Ensures that no exception is thrown.
         */
        @Test
        void shapeAreaIsInvalid() {
            List<String[]> data = List.of(validHeaders,
                    new String[]{"1", "12345", "67890", "100.0", "INVALID_Shape_Area", "POINT(1 1)", "Owner1", "Freguesia1", "Municipio1", "Ilha1"}
            );

            assertDoesNotThrow(() -> validator.validate(data));
        }

        /**
         * Tests the validation of a CSV file with an invalid geometry in a data row.
         * Ensures that no exception is thrown.
         */
        @Test
        void geometryIsInvalid() {
            List<String[]> data = List.of(validHeaders,
                    new String[]{"1", "12345", "67890", "100.0", "200.0", "INVALID_GEOMETRY", "Owner1", "Freguesia1", "Municipio1", "Ilha1"}
            );

            assertDoesNotThrow(() -> validator.validate(data));
        }
    }

}