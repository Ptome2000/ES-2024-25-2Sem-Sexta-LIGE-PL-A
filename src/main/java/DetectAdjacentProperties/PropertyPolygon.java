package DetectAdjacentProperties;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class PropertyPolygon {
    private final int objectId;
    private final double parId;
    private final String parNum;
    private final double shapeLength;
    private final double shapeArea;
    private final List<double[]> vertices; // Lista de pontos (x, y)
    private final String owner;
    private final String freguesia;
    private final String municipio;
    private final String ilha;

    public PropertyPolygon(int objectId, double parId, String parNum, double shapeLength, double shapeArea,
                           List<double[]> vertices, String owner, String freguesia, String municipio, String ilha) {
        this.objectId = objectId;
        this.parId = parId;
        this.parNum = parNum;
        this.shapeLength = shapeLength;
        this.shapeArea = shapeArea;
        this.vertices = vertices;
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

    public List<double[]> getVertices() {
        return vertices;
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

    public static PropertyPolygon fromCsvRow(String[] row) {
        try {
            int objectId = Integer.parseInt(row[0]);
            double parId = Double.parseDouble(row[1]);
            String parNum = row[2];
            double shapeLength = Double.parseDouble(row[3]);
            double shapeArea = Double.parseDouble(row[4]);
            List<double[]> vertices = parseGeometry(row[5]);
            String owner = row[6];
            String freguesia = row[7];
            String municipio = row[8];
            String ilha = row[9];

            return new PropertyPolygon(objectId, parId, parNum, shapeLength, shapeArea, vertices, owner, freguesia, municipio, ilha);
        } catch (Exception e) {
            System.err.println("Erro ao processar linha do CSV: " + e.getMessage());
            return null; // Ignora a linha se der erro
        }
    }

    private static List<double[]> parseGeometry(String geometry) {
        List<double[]> vertices = new ArrayList<>();

        if (!geometry.startsWith("MULTIPOLYGON ((")) return vertices;

        // Extrair os pares de coordenadas
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
                    vertices.add(new double[]{x, y});
                } catch (NumberFormatException ignored) {}
            }
        }
        return vertices;
    }

    @Override
    public String toString() {
        return "PropertyPolygon{" +
                "objectId=" + objectId +
                ", parId=" + parId +
                ", parNum='" + parNum + '\'' +
                ", Property Length=" + shapeLength +
                ", Property Area=" + shapeArea +
                ", Owner='" + owner + '\'' +
                ", freguesia='" + freguesia + '\'' +
                ", municipio='" + municipio + '\'' +
                ", ilha='" + ilha + '\'' +
                ", vertices=" + vertices +
                '}';
    }
}
