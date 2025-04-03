package DetectAdjacentProperties;

import java.util.List;

public class Polygon {
    private List<VertexCoordinate> coordenadas;

    public Polygon(List<VertexCoordinate> coordenadas) {
        this.coordenadas = coordenadas;
    }

    public List<VertexCoordinate> getCoordenadas() {
        return coordenadas;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (VertexCoordinate c : coordenadas) {
            sb.append(c.toString()).append(" ");
        }
        return sb.toString();
    }
}
