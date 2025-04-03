package DetectAdjacentProperties;

import java.util.*;

/**
 * Represents a spatial grid to optimize the search for adjacent properties
 * by dividing the space into cells and storing properties based on their vertices.
 */
class SpatialGrid {
    private static final int CELL_SIZE = 10000;
    private static final int ORIGIN_X = 289132; // Canto inferior esquerdo
    private static final int ORIGIN_Y = 3612469;
    private static final int MAX_GRID_X = 9; // 9 células no total (0 a 8)
    private static final int MAX_GRID_Y = 5; // 5 células no total (0 a 4)
    private Map<String, List<PropertyPolygon>> grid;

    /**
     * Constructs an empty spatial grid.
     */
    public SpatialGrid() {
        this.grid = new HashMap<>();
    }

    /**
     * Computes a unique key for a grid cell based on a vertex's coordinates.
     *
     * @param x The x-coordinate of the vertex.
     * @param y The y-coordinate of the vertex.
     * @return A string representing the cell key.
     */
    // Converte uma coordenada (x, y) para o ID da célula correspondente
    private String getCellKey(double x, double y) {
        int gridX = (int) Math.floor((x - ORIGIN_X) / CELL_SIZE);
        int gridY = (int) Math.floor((y - ORIGIN_Y) / CELL_SIZE);

        // **Corrigir para não ultrapassar os limites da grid**
        gridX = Math.max(0, Math.min(gridX, MAX_GRID_X));
        gridY = Math.max(0, Math.min(gridY, MAX_GRID_Y));

        return gridX + "-" + gridY;
    }

    /**
     * Inserts a property into the spatial grid, distributing it across
     * multiple cells based on its vertices.
     *
     * @param property The property polygon to be inserted.
     */
    // Insere um terreno na grelha (pode estar em várias células)
    public void insert(PropertyPolygon property) {
        for (VertexCoordinate vertex : property.getPolygon().getCoordenadas()) {
            String cellKey = getCellKey(vertex.getX(), vertex.getY());
            grid.computeIfAbsent(cellKey, k -> new ArrayList<>()).add(property);
        }
        System.out.println("Inserted property " + property.getObjectId() +
                " into grids: " + getPropertyGridCells(property));
    }

    private List<String> getPropertyGridCells(PropertyPolygon property) {
        Set<String> cells = new HashSet<>();
        for (VertexCoordinate vertex : property.getPolygon().getCoordenadas()) {
            cells.add(getCellKey(vertex.getX(), vertex.getY()));
        }
        return new ArrayList<>(cells);
    }

    /**
     * Retrieves a list of properties that are near the given property,
     * based on the spatial grid.
     *
     * @param property The property whose neighbors are to be found.
     * @return A list of properties that share at least one grid cell with the given property.
     */
    // Obtém terrenos próximos com base nas células vizinhas
    public List<PropertyPolygon> getNearbyProperties(PropertyPolygon property) {
        Set<PropertyPolygon> nearby = new HashSet<>();

        for (String cellKey : getPropertyGridCells(property)) {
            if (grid.containsKey(cellKey)) {
                nearby.addAll(grid.get(cellKey));
            }
        }

        return new ArrayList<>(nearby);
    }
}