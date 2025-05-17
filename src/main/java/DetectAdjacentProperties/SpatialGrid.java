package DetectAdjacentProperties;

import Models.PropertyPolygon;
import Models.VertexCoordinate;
import Utils.Annotations.CyclomaticComplexity;
import Utils.Annotations.Layer;
import Utils.Enums.LayerType;

import java.util.*;

/**
 * The {@code SpatialGrid} class manages a spatial grid for property polygons.
 * It allows for efficient insertion and retrieval of properties based on their spatial location.
 * The grid is divided into cells of a fixed size, and properties are stored in the cell corresponding
 * to their first vertex.
 */
@Layer(LayerType.BACK_END)
class SpatialGrid {

    private static final int CELL_SIZE = 150;
    private final double minX;
    private final double minY;
    private final int MAX_GRID_X;
    private final int MAX_GRID_Y;
    private final Map<String, List<PropertyPolygon>> grid;

    /**
     * Constructs a spatial grid using a list of properties.
     * It automatically calculates the grid dimensions based on min/max coordinates.
     *
     * @param properties The list of property polygons to be managed by the grid.
     */
    public SpatialGrid(List<PropertyPolygon> properties) {
        this.grid = new HashMap<>();

        double[] minCoordinates = CoordinateFinder.findMinCoordinates(properties);
        double[] maxCoordinates = CoordinateFinder.findMaxCoordinates(properties);

        this.minX = minCoordinates[0];
        this.minY = minCoordinates[1];
        double maxX = maxCoordinates[0];
        double maxY = maxCoordinates[1];

        this.MAX_GRID_X = (int) Math.ceil((maxX - minX) / (double) CELL_SIZE);
        this.MAX_GRID_Y = (int) Math.ceil((maxY - minY) / (double) CELL_SIZE);

        System.out.printf("Grid criada automaticamente com %d colunas e %d linhas.%n", MAX_GRID_X, MAX_GRID_Y);

    }

    /**
     * Computes a unique key for a grid cell based on a vertex's coordinates.
     *
     * @param x The x-coordinate of the vertex.
     * @param y The y-coordinate of the vertex.
     * @return A string representing the cell key.
     */
    @CyclomaticComplexity(1)
    String getCellKey(double x, double y) {
        int gridX = (int) Math.floor((x - minX) / CELL_SIZE);
        int gridY = (int) Math.floor((y - minY) / CELL_SIZE);

        gridX = Math.max(0, Math.min(gridX, MAX_GRID_X - 1));
        gridY = Math.max(0, Math.min(gridY, MAX_GRID_Y - 1));

        return gridX + "-" + gridY;
    }

    /**
     * Inserts a property into the spatial grid by placing it in the cell
     * corresponding to its first vertex.
     *
     * @param property The property polygon to be inserted.
     */
    @CyclomaticComplexity(1)
    public void insert(PropertyPolygon property) {
        VertexCoordinate firstVertex = property.getPolygon().getVertices().get(0);
        String firstCellKey = getCellKey(firstVertex.x(), firstVertex.y());
        grid.computeIfAbsent(firstCellKey, k -> new ArrayList<>()).add(property);
    }

    /**
     * Retrieves the grid cells a given property spans, based on its vertices.
     *
     * @param property The property to locate in the grid.
     * @return A list of cell keys the property touches.
     */
    @CyclomaticComplexity(2)
    List<String> getPropertyGridCells(PropertyPolygon property) {
        Set<String> cells = new HashSet<>();
        for (VertexCoordinate vertex : property.getPolygon().getVertices()) {
            cells.add(getCellKey(vertex.x(), vertex.y()));
        }
        return new ArrayList<>(cells);
    }

    /**
     * Retrieves a list of properties that are nearby a given property.
     * It checks the adjacent cells in the grid to find nearby properties.
     *
     * @param property The property to check for nearby properties.
     * @return A list of nearby properties.
     */
    @CyclomaticComplexity(8)
    public List<PropertyPolygon> getNearbyProperties(PropertyPolygon property) {
        Set<PropertyPolygon> nearby = new HashSet<>();
        List<String> propertyCells = getPropertyGridCells(property);

        for (String cellKey : propertyCells) {
            String[] parts = cellKey.split("-");
            int x = Integer.parseInt(parts[0]);
            int y = Integer.parseInt(parts[1]);

            for (int dx = -1; dx <= 1; dx++) {
                for (int dy = -1; dy <= 1; dy++) {
                    int adjX = x + dx;
                    int adjY = y + dy;

                    if (adjX < 0 || adjX > MAX_GRID_X || adjY < 0 || adjY > MAX_GRID_Y) continue;

                    String adjCellKey = adjX + "-" + adjY;
                    addPropertiesFromCell(adjCellKey, property, nearby);
                }
            }
        }
        return new ArrayList<>(nearby);
    }

    /**
     * Adds properties from a specific cell to the nearby set, excluding the given property.
     *
     * @param cellKey The key of the cell to check.
     * @param property The property to exclude from the nearby set.
     * @param nearby   The set of nearby properties to add to.
     */
    @CyclomaticComplexity(4)
    private void addPropertiesFromCell(String cellKey, PropertyPolygon property, Set<PropertyPolygon> nearby) {
        if (grid.containsKey(cellKey)) {
            for (PropertyPolygon other : grid.get(cellKey)) {
                if (!other.equals(property)) {
                    nearby.add(other);
                }
            }
        }
    }

}