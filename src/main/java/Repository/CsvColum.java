package Repository;

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

    CsvColum(int index) {
        this.index = index;
    }

    /**
     * Gets the index of the column.
     *
     * @return The column index.
     */
    public int getIndex() {
        return index;
    }

}
