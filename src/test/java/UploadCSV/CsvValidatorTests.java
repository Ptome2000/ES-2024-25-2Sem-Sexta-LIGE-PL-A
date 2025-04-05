package UploadCSV;

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

    @BeforeAll
    static void init() {
        exampleData =  new String[]{"1", "12345", "67890", "100.0", "200.0", "POINT(1 1)", "Owner1", "Freguesia1", "Municipio1", "Ilha1"};
        validHeaders = new String[]{"OBJECTID", "PAR_ID", "PAR_NUM", "Shape_Length", "Shape_Area",
                "geometry", "OWNER", "Freguesia", "Municipio", "Ilha"};
    }

    @BeforeEach
    void setUp() {
        validator = new CsvValidator();
    }

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

    @Test
    void correctData() {
        List<String[]> data = List.of(validHeaders, exampleData);
        assertDoesNotThrow(() -> validator.validate(data));
    }

    @Nested
    class HeaderTests {

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

        @Test
        void headerObjectIdIsIncorrect() {
            List<String[]> data = List.of(
                    new String[]{"BAD_OBJECTID", "PAR_ID", "PAR_NUM", "Shape_Length", "Shape_Area",
                            "geometry", "OWNER", "Freguesia", "Municipio", "Ilha"}, exampleData
            );

            CsvException exception = assertThrows(CsvException.class, () -> validator.validate(data));
            assertTrue(exception.getMessage().contains("invalid headers"));
        }

        @Test
        void headerParIdIsIncorrect() {
            List<String[]> data = List.of(
                    new String[]{"OBJECTID", "BAD_PAR_ID", "PAR_NUM", "Shape_Length", "Shape_Area",
                            "geometry", "OWNER", "Freguesia", "Municipio", "Ilha"}, exampleData
            );

            CsvException exception = assertThrows(CsvException.class, () -> validator.validate(data));
            assertTrue(exception.getMessage().contains("invalid headers"));
        }

        @Test
        void headerParNumIsIncorrect() {
            List<String[]> data = List.of(
                    new String[]{"OBJECTID", "PAR_ID", "BAD_PAR_NUM", "Shape_Length", "Shape_Area",
                            "geometry", "OWNER", "Freguesia", "Municipio", "Ilha"}, exampleData
            );

            CsvException exception = assertThrows(CsvException.class, () -> validator.validate(data));
            assertTrue(exception.getMessage().contains("invalid headers"));
        }

        @Test
        void headerShapeLengthIsIncorrect() {
            List<String[]> data = List.of(
                    new String[]{"OBJECTID", "PAR_ID", "PAR_NUM", "BAD_Shape_Length", "Shape_Area",
                            "geometry", "OWNER", "Freguesia", "Municipio", "Ilha"}, exampleData
            );

            CsvException exception = assertThrows(CsvException.class, () -> validator.validate(data));
            assertTrue(exception.getMessage().contains("invalid headers"));
        }

        @Test
        void headerShapeAreaIsIncorrect() {
            List<String[]> data = List.of(
                    new String[]{"OBJECTID", "PAR_ID", "PAR_NUM", "Shape_Length", "BAD_Shape_Area",
                            "geometry", "OWNER", "Freguesia", "Municipio", "Ilha"}, exampleData
            );

            CsvException exception = assertThrows(CsvException.class, () -> validator.validate(data));
            assertTrue(exception.getMessage().contains("invalid headers"));
        }

        @Test
        void headerGeometryIsIncorrect() {
            List<String[]> data = List.of(
                    new String[]{"OBJECTID", "PAR_ID", "PAR_NUM", "Shape_Length", "Shape_Area",
                            "BAD_geometry", "OWNER", "Freguesia", "Municipio", "Ilha"}, exampleData
            );

            CsvException exception = assertThrows(CsvException.class, () -> validator.validate(data));
            assertTrue(exception.getMessage().contains("invalid headers"));
        }

        @Test
        void headerOwnerIsIncorrect() {
            List<String[]> data = List.of(
                    new String[]{"OBJECTID", "PAR_ID", "PAR_NUM", "Shape_Length", "Shape_Area",
                            "geometry", "BAD_OWNER", "Freguesia", "Municipio", "Ilha"}, exampleData
            );

            CsvException exception = assertThrows(CsvException.class, () -> validator.validate(data));
            assertTrue(exception.getMessage().contains("invalid headers"));
        }

        @Test
        void headerFreguesiaIsIncorrect() {
            List<String[]> data = List.of(
                    new String[]{"OBJECTID", "PAR_ID", "PAR_NUM", "Shape_Length", "Shape_Area",
                            "geometry", "OWNER", "BAD_Freguesia", "Municipio", "Ilha"}, exampleData
            );

            CsvException exception = assertThrows(CsvException.class, () -> validator.validate(data));
            assertTrue(exception.getMessage().contains("invalid headers"));
        }

        @Test
        void headerMunicipioIsIncorrect() {
            List<String[]> data = List.of(
                    new String[]{"OBJECTID", "PAR_ID", "PAR_NUM", "Shape_Length", "Shape_Area",
                            "geometry", "OWNER", "Freguesia", "BAD_Municipio", "Ilha"}, exampleData
            );

            CsvException exception = assertThrows(CsvException.class, () -> validator.validate(data));
            assertTrue(exception.getMessage().contains("invalid headers"));
        }

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

        @Test
        void dataRowFewerThan10Columns() {
            List<String[]> data = List.of( validHeaders,
                    new String[]{"1", "12345", "67890", "100.0", "200.0", "POINT(1 1)", "Owner1", "Freguesia1"}
            );
            assertDoesNotThrow(() -> validator.validate(data));
        }

        @Test
        void dataRowMoreThan10Columns() {
            List<String[]> data = List.of(validHeaders,
                    new String[]{"1", "12345", "67890", "100.0", "200.0", "POINT(1 1)", "Owner1", "Freguesia1", "Municipio1", "Ilha1", "ExtraColumn"}
            );

            assertDoesNotThrow(() -> validator.validate(data));
        }

        @Test
        void parIdIsInvalid() {
            List<String[]> data = List.of(validHeaders,
                    new String[]{"1", "INVALID_PAR_ID", "67890", "100.0", "200.0", "POINT(1 1)", "Owner1", "Freguesia1", "Municipio1", "Ilha1"}
            );

            assertDoesNotThrow(() -> validator.validate(data));
        }

        @Test
        void shapeLengthIsInvalid() {
            List<String[]> data = List.of(validHeaders,
                    new String[]{"1", "12345", "67890", "INVALID_Shape_Length", "200.0", "POINT(1 1)", "Owner1", "Freguesia1", "Municipio1", "Ilha1"}
            );

            assertDoesNotThrow(() -> validator.validate(data));
        }

        @Test
        void shapeAreaIsInvalid() {
            List<String[]> data = List.of(validHeaders,
                    new String[]{"1", "12345", "67890", "100.0", "INVALID_Shape_Area", "POINT(1 1)", "Owner1", "Freguesia1", "Municipio1", "Ilha1"}
            );

            assertDoesNotThrow(() -> validator.validate(data));
        }

        @Test
        void geometryIsInvalid() {
            List<String[]> data = List.of(validHeaders,
                    new String[]{"1", "12345", "67890", "100.0", "200.0", "INVALID_GEOMETRY", "Owner1", "Freguesia1", "Municipio1", "Ilha1"}
            );

            assertDoesNotThrow(() -> validator.validate(data));
        }
    }

}