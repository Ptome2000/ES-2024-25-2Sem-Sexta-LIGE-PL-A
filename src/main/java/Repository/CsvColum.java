package Repository;

import Utils.Annotations.CyclomaticComplexity;
import Utils.Annotations.Layer;
import Utils.Enums.LayerType;

/**
 * The {@code CsvColum} enum represents the columns in a CSV file related to geographical data.
 * Each enum constant corresponds to a specific column in the CSV file, identified by its index.
 */
@Layer(LayerType.BACK_END)
public enum CsvColum {

    OBJECT_ID(0),
    PAR_ID(1),
    PAR_NUM(2),
    SHAPE_LENGTH(3),
    SHAPE_AREA(4),
    POLYGON(5),
    OWNER(6),
    PARISH(7),
    MUNICIPALITY(8),
    DISTRICT(9);

    private final int index;

    /**
     * Constructor to initialize the CsvColum enum with the column index.
     *
     * @param index The index of the column in the CSV file.
     */
    CsvColum(int index) {
        this.index = index;
    }

    /**
     * Gets the index of the column.
     *
     * @return The column index.
     */
    @CyclomaticComplexity(1)
    public int getIndex() {
        return index;
    }

}
