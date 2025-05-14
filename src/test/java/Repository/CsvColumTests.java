package Repository;

import static org.junit.jupiter.api.Assertions.*;

import io.qameta.allure.Description;
import io.qameta.allure.Feature;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * This class contains unit tests for the {@link CsvColum} enum.
 * It validates the behavior of the CsvColum enum, including
 * its ability to return the correct index for each column.
 * Each test case is designed to cover specific functionality and edge cases, ensuring
 * the robustness of the CsvColum implementation.
 *
 * <p><strong>Author:</strong> Ptome2000</p>
 * <p><strong>Date:</strong> 13/05/2025</p>
 *
 * <p><strong>Cyclomatic Complexity:</strong></p>
 * <ul>
 *     <li>getIndex: 1</li>
 *     <li>values: 1</li>
 *     <li>valueOf: 1</li>
 * </ul>
 */
@Feature("CSV Importation")
class CsvColumTests {

    @Test
    @DisplayName("Validate CsvColum Indexes")
    @Description("Ensures that each CsvColum enum constant returns the correct index.")
    @Severity(SeverityLevel.CRITICAL)
    void testGetIndex() {
        assertEquals(0, CsvColum.OBJECT_ID.getIndex());
        assertEquals(1, CsvColum.PAR_ID.getIndex());
        assertEquals(2, CsvColum.PAR_NUM.getIndex());
        assertEquals(3, CsvColum.SHAPE_LENGTH.getIndex());
        assertEquals(4, CsvColum.SHAPE_AREA.getIndex());
        assertEquals(5, CsvColum.POLYGON.getIndex());
        assertEquals(6, CsvColum.OWNER.getIndex());
        assertEquals(7, CsvColum.PARISH.getIndex());
        assertEquals(8, CsvColum.MUNICIPALITY.getIndex());
        assertEquals(9, CsvColum.DISTRICT.getIndex());
    }

    @Test
    @DisplayName("Validate CsvColum Enum Values")
    @Description("Ensures that the CsvColum.values() method returns all enum constants in the correct order.")
    @Severity(SeverityLevel.NORMAL)
    void testEnumValues() {
        CsvColum[] values = CsvColum.values();
        assertEquals(10, values.length);
        assertEquals(CsvColum.OBJECT_ID, values[0]);
        assertEquals(CsvColum.DISTRICT, values[9]);
    }

    @Test
    @DisplayName("Validate CsvColum ValueOf Method")
    @Description("Ensures that the CsvColum.valueOf method works for valid names and throws an exception for invalid names.")
    @Severity(SeverityLevel.MINOR)
    void testValueOf() {
        assertEquals(CsvColum.OWNER, CsvColum.valueOf("OWNER"));
        assertThrows(IllegalArgumentException.class, () -> CsvColum.valueOf("INVALID_COLUMN"));
    }

}