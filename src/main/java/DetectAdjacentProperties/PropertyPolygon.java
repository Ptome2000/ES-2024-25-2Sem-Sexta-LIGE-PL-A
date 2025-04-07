package DetectAdjacentProperties;


import java.util.ArrayList;
import java.util.List;

/**
 * This class represents a property polygon with various attributes such as
 * object ID, parcel ID, geometry (polygon), and other property details.
 */
public class PropertyPolygon {

    private final int objectId;
    private final double parId;
    private final String parNum;
    private final double shapeLength;
    private final double shapeArea;
    private final Polygon polygon; // Using Polygon instead of List<double[]>
    private final String owner;
    private final String freguesia;
    private final String municipio;
    private final String ilha;

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

    public int getObjectId() {
        return objectId;
    }

    public double getParId() {
        return parId;
    }

    public String getParNum() {
        return parNum;
    }

    public double getShapeLength() {
        return shapeLength;
    }

    public double getShapeArea() {
        return shapeArea;
    }

    public Polygon getPolygon() {
        return polygon;
    }

    public String getOwner() {
        return owner;
    }

    public String getFreguesia() {
        return freguesia;
    }

    public String getMunicipio() {
        return municipio;
    }

    public String getIlha() {
        return ilha;
    }

    /**
     * Creates a PropertyPolygon object from a CSV row.
     *
     * @param row A CSV row representing the properties of the polygon.
     * @return A PropertyPolygon object or null if an error occurs during parsing.
     */
    public static PropertyPolygon fromCsvRow(String[] row) {
        try {
            int objectId = Integer.parseInt(row[0]);
            double parId = Double.parseDouble(row[1]);
            String parNum = row[2];
            double shapeLength = Double.parseDouble(row[3]);
            double shapeArea = Double.parseDouble(row[4]);
            Polygon polygon = parseGeometry(row[5]);
            String owner = row[6];
            String freguesia = row[7];
            String municipio = row[8];
            String ilha = row[9];

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
    private static Polygon parseGeometry(String geometry) {
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
    @Override
    public int hashCode() {
        return Integer.hashCode(objectId);
    }

    /**

     * Returns a string representation of the PropertyPolygon.
     *
     * @return A string containing the property ID, owner, and the list of vertices.
     */
    @Override
    public String toString() {
        return "ID: " + objectId + ", Owner: " + owner + ", Vertices: " + polygon.toString();
    }
}