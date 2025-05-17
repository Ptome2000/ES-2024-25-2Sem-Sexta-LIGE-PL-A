package DetectAdjacentProperties;

import Models.PropertyPolygon;
import Models.VertexCoordinate;

import java.util.*;

/**
 * Represents a spatial grid to optimize the search for adjacent properties
 * by dividing the space into cells and storing properties based on their vertices.
 */
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
    public void insert(PropertyPolygon property) {
        VertexCoordinate firstVertex = property.getPolygon().getVertices().get(0);
        String firstCellKey = getCellKey(firstVertex.x(), firstVertex.y());
        grid.computeIfAbsent(firstCellKey, k -> new ArrayList<>()).add(property);
    }

    /**
     * Prints the coordinate ranges for each grid cell to the console.
     * Used for debugging or inspection purposes.
     */
    public void printGridRanges() {
        System.out.println("\n--- Grid Cell Ranges ---");

        for (int i = 0; i < MAX_GRID_X; i++) {
            for (int j = 0; j < MAX_GRID_Y; j++) {
                double xStart = i * CELL_SIZE;
                double yStart = j * CELL_SIZE;
                double xEnd = xStart + CELL_SIZE;
                double yEnd = yStart + CELL_SIZE;

//                System.out.printf("Cell (%d, %d): X = [%.2f, %.2f], Y = [%.2f, %.2f]%n",
//                        i, j, xStart, xEnd, yStart, yEnd);
            }
        }
    }

    /**
     * Logs the number of properties present in each cell of the grid.
     * Useful for performance tuning and diagnostics.
     */
    public void logPropertiesInCells() {
        System.out.println("Properties in each grid cell:");

        for (Map.Entry<String, List<PropertyPolygon>> entry : grid.entrySet()) {
            String cellKey = entry.getKey();
            List<PropertyPolygon> propertiesInCell = entry.getValue();
//            System.out.println("Cell " + cellKey + " has " + propertiesInCell.size() + " properties.");
        }
    }

    /**
     * Retrieves the grid cells a given property spans, based on its vertices.
     *
     * @param property The property to locate in the grid.
     * @return A list of cell keys the property touches.
     */

    List<String> getPropertyGridCells(PropertyPolygon property) {
        Set<String> cells = new HashSet<>();
        for (VertexCoordinate vertex : property.getPolygon().getVertices()) {
            cells.add(getCellKey(vertex.x(), vertex.y()));
        }
        return new ArrayList<>(cells);
    }

    /**
     * Retrieves a list of properties that are near the given property,
     * by checking its current and adjacent grid cells.
     *
     * @param property The property whose neighbors are to be found.
     * @return A list of nearby properties.
     */
    public List<PropertyPolygon> getNearbyProperties(PropertyPolygon property) {
        Set<PropertyPolygon> nearby = new HashSet<>();
        List<String> propertyCells = getPropertyGridCells(property);

        System.out.println("\n--- Checking property " + property.getObjectId() + " -> " + propertyCells + " ---");

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

                    if (grid.containsKey(adjCellKey)) {
                        for (PropertyPolygon other : grid.get(adjCellKey)) {
                            if (!other.equals(property)) {
                                nearby.add(other);
                            }
                        }
                    }
                }
            }
        }
        return new ArrayList<>(nearby);
    }

    /**
     * Helper method to check and add properties from a specific cell key
     * into the provided nearby set.
     *
     * @param cellKey The cell key to search.
     * @param nearby The set of nearby properties being built.
     */
    private void checkAndAddNearbyProperties(String cellKey, Set<PropertyPolygon> nearby) {
        if (grid.containsKey(cellKey)) {
            for (PropertyPolygon property : grid.get(cellKey)) {
                nearby.add(property);
            }
        }
    }

}