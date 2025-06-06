package Models;


import Utils.Annotations.CyclomaticComplexity;
import Utils.Annotations.Layer;
import Utils.Enums.LayerType;
import Repository.CsvColum;

import java.util.ArrayList;
import java.util.List;

/**
 * The {@code PropertyPolygon} class represents a property with a polygonal shape.
 * It includes attributes such as the property ID, owner, geographical location, and shape details.
 */
@Layer(LayerType.BACK_END)
public class PropertyPolygon {

    private final int objectId;
    private final double parId;
    private final String parNum;
    private final double shapeLength;
    private final double shapeArea;
    private final Polygon polygon; // Using Polygon instead of List<double[]>
    private String owner;
    private final String freguesia;
    private final String municipio;
    private final String ilha;
    private double urbanizationScore;
    private double tourismScore;

    /**
     * Constructor to initialize a PropertyPolygon object.
     *
     * @param objectId    Unique identifier for the property.
     * @param parId       Parcel ID associated with the property.
     * @param parNum      Parcel number for the property.
     * @param shapeLength Length of the shape's boundary.
     * @param shapeArea   Area of the property shape.
     * @param polygon     The polygon representing the shape of the property.
     * @param owner       ID of the owner of the property.
     * @param freguesia   The parish where the property is located.
     * @param municipio   The municipality where the property is located.
     * @param ilha        The island where the property is located.
     */
    public PropertyPolygon(int objectId, double parId, String parNum, double shapeLength, double shapeArea,
                           Polygon polygon, String owner, String freguesia, String municipio, String ilha) {
        this.objectId = objectId;
        this.parId = parId;
        this.parNum = parNum;
        this.shapeLength = shapeLength;
        this.shapeArea = shapeArea;
        this.polygon = polygon;
        this.owner = owner;
        this.freguesia = freguesia;
        this.municipio = municipio;
        this.ilha = ilha;
    }

    /**
     * Gets the object ID of the property.
     *
     * @return The object ID.
     */
    @CyclomaticComplexity(1)
    public int getObjectId() {
        return objectId;
    }

    /**
     * Gets the parcel ID of the property.
     *
     * @return The parcel ID.
     */
    @CyclomaticComplexity(1)
    public double getParId() {
        return parId;
    }

    /**
     * Gets the parcel number of the property.
     *
     * @return The parcel number.
     */
    @CyclomaticComplexity(1)
    public String getParNum() {
        return parNum;
    }

    /**
     * Gets the length of the shape's boundary.
     *
     * @return The shape length.
     */
    @CyclomaticComplexity(1)
    public double getShapeLength() {
        return shapeLength;
    }

    /**
     * Gets the area of the property shape.
     *
     * @return The shape area.
     */
    @CyclomaticComplexity(1)
    public double getShapeArea() {
        return shapeArea;
    }

    /**
     * Gets the polygon representing the shape of the property.
     *
     * @return The polygon object.
     */
    @CyclomaticComplexity(1)
    public Polygon getPolygon() {
        return polygon;
    }

    /**
     * Gets the owner ID of the property.
     *
     * @return The owner ID.
     */
    @CyclomaticComplexity(1)
    public String getOwner() {
        return owner;
    }

    /**
     * Gets the parish where the property is located.
     *
     * @return The parish name.
     */
    @CyclomaticComplexity(1)
    public String getFreguesia() {
        return freguesia;
    }

    /**
     * Gets the municipality where the property is located.
     *
     * @return The municipality name.
     */
    @CyclomaticComplexity(1)
    public String getMunicipio() {
        return municipio;
    }

    /**
     * Gets the island where the property is located.
     *
     * @return The island name.
     */
    @CyclomaticComplexity(1)
    public String getIlha() {
        return ilha;
    }

    public double getUrbanizationScore() { return urbanizationScore; }

    public void setUrbanizationScore(double urbanizationScore) { this.urbanizationScore = urbanizationScore; }

    public double getTourismScore() { return tourismScore; }

    public void setTourismScore(double tourismScore) { this.tourismScore = tourismScore; }



    /**
     * Creates a PropertyPolygon object from a CSV row.
     *
     * @param row A CSV row representing the properties of the polygon.
     * @return A PropertyPolygon object or null if an error occurs during parsing.
     */
    @CyclomaticComplexity(2)
    public static PropertyPolygon fromCsvRow(String[] row) {
        try {
            int objectId = Integer.parseInt(row[CsvColum.OBJECT_ID.getIndex()]);
            double parId = Double.parseDouble(row[CsvColum.PAR_ID.getIndex()]);
            String parNum = row[CsvColum.PAR_NUM.getIndex()];
            double shapeLength = Double.parseDouble(row[CsvColum.SHAPE_LENGTH.getIndex()]);
            double shapeArea = Double.parseDouble(row[CsvColum.SHAPE_AREA.getIndex()]);
            Polygon polygon = parseGeometry(row[CsvColum.POLYGON.getIndex()]);
            String owner = row[CsvColum.OWNER.getIndex()];
            String freguesia = row[CsvColum.PARISH.getIndex()];
            String municipio = row[CsvColum.MUNICIPALITY.getIndex()];
            String ilha = row[CsvColum.DISTRICT.getIndex()];

            return new PropertyPolygon(objectId, parId, parNum, shapeLength, shapeArea, polygon, owner, freguesia, municipio, ilha);
        } catch (Exception e) {
            System.err.println("Error processing CSV row: " + e.getMessage());
            return null; // Ignore the row if there is an error
        }
    }

    /**
     * Parses the geometry string from the CSV into a Polygon object with its vertices.
     *
     * @param geometry The geometry string representing the polygon.
     * @return A Polygon object representing the parsed geometry.
     */
    @CyclomaticComplexity(5)
    static Polygon parseGeometry(String geometry) {
        List<VertexCoordinate> vertices = new ArrayList<>();

        if (!geometry.startsWith("MULTIPOLYGON ((")) return new Polygon(vertices);

        // Extract the coordinate pairs
        String coords = geometry.replace("MULTIPOLYGON ((", "")
                .replace("))", "")
                .trim();
        String[] points = coords.split(", ");

        for (String point : points) {
            String[] xy = point.split(" ");
            if (xy.length == 2) {
                try {
                    double x = Double.parseDouble(xy[0]);
                    double y = Double.parseDouble(xy[1]);
                    vertices.add(new VertexCoordinate(x, y));
                } catch (NumberFormatException ignored) {}
            }
        }
        return new Polygon(vertices);
    }

    /**

     * Compares this PropertyPolygon to the specified object.
     *
     * @param obj the object to compare this PropertyPolygon against
     * @return true if the given object represents a PropertyPolygon with the same objectId, false otherwise
     */
    @CyclomaticComplexity(4)
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        PropertyPolygon other = (PropertyPolygon) obj;
        return objectId == other.objectId;
    }

    /**
     * Returns a hash code value for this PropertyPolygon.
     *
     * @return a hash code value for this PropertyPolygon
     */
    @CyclomaticComplexity(1)
    @Override
    public int hashCode() {
        return Integer.hashCode(objectId);
    }

    /**

     * Returns a string representation of the PropertyPolygon.
     *
     * @return A string containing the property ID, owner, and the list of vertices.
     */
    @CyclomaticComplexity(1)
    @Override
    public String toString() {
        return "ID: " + objectId + ", Owner: " + owner + ", Vertices: " + polygon.toString();
    }


    /**
     * Sets the owner of the property. This method is used for testing purposes
     *
     * @param owner The new owner ID to set.
     */
    @CyclomaticComplexity(1)
    public void setOwner(String owner) {
        this.owner = owner;
    }
}