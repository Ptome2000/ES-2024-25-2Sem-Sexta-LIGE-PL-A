package DetectAdjacentProperties;

import java.util.*;

/**
 * Represents a spatial grid to optimize the search for adjacent properties
 * by dividing the space into cells and storing properties based on their vertices.
 */
class SpatialGrid {
    private static final int CELL_SIZE = 250;
    private double minX;
    private double minY;
    private int MAX_GRID_X;
    private int MAX_GRID_Y;
    private Map<String, List<PropertyPolygon>> grid;

    /**
     * Constructs an empty spatial grid.
     */
    public SpatialGrid(List<PropertyPolygon> properties) {
        this.grid = new HashMap<>();

        // Obter as coordenadas mínimas e máximas usando as classes MinCoordinateFinder e MaxCoordinateFinder
        double[] minCoordinates = MinCoordinateFinder.findMinCoordinates(properties);
        double[] maxCoordinates = MaxCoordinateFinder.findMaxCoordinates(properties);

        this.minX = minCoordinates[0];
        this.minY = minCoordinates[1];
        double maxX = maxCoordinates[0];
        double maxY = maxCoordinates[1];

        // Calcular o número de células para o grid
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
    // Converte uma coordenada (x, y) para o ID da célula correspondente
    private String getCellKey(double x, double y) {
        int gridX = (int) Math.floor((x - minX) / CELL_SIZE);
        int gridY = (int) Math.floor((y - minY) / CELL_SIZE);

        // Garantir que o valor de X e Y não ultrapasse o limite do grid
        gridX = Math.max(0, Math.min(gridX, MAX_GRID_X - 1));
        gridY = Math.max(0, Math.min(gridY, MAX_GRID_Y - 1));

        return gridX + "-" + gridY;
    }

    /**
     * Inserts a property into the spatial grid, distributing it across
     * multiple cells based on its vertices.
     *
     * @param property The property polygon to be inserted.
     */
    // Método de inserção da propriedade
    public void insert(PropertyPolygon property) {
        // Para garantir que apenas a célula do primeiro vértice será usada
        VertexCoordinate firstVertex = property.getPolygon().getCoordenadas().get(0); // primeiro vértice
        String firstCellKey = getCellKey(firstVertex.getX(), firstVertex.getY()); // célula do primeiro vértice

        // Adiciona a propriedade à célula do primeiro vértice
        grid.computeIfAbsent(firstCellKey, k -> new ArrayList<>()).add(property);
    }

    public void printGridRanges() {
        System.out.println("\n--- Grid Cell Ranges ---");

        for (int i = 0; i < MAX_GRID_X; i++) {
            for (int j = 0; j < MAX_GRID_Y; j++) {
                // Cálculo dos intervalos X e Y
                double xStart = i * CELL_SIZE;
                double yStart = j * CELL_SIZE;
                double xEnd = xStart + CELL_SIZE;
                double yEnd = yStart + CELL_SIZE;

                // Imprimir os intervalos da célula
                System.out.printf("Cell (%d, %d): X = [%.2f, %.2f], Y = [%.2f, %.2f]%n",
                        i, j, xStart, xEnd, yStart, yEnd);
            }
        }
    }

    // Método para imprimir a quantidade de propriedades em cada célula no final
    public void logPropertiesInCells() {
        System.out.println("Properties in each grid cell:");

        // Imprime a quantidade de propriedades por célula
        for (Map.Entry<String, List<PropertyPolygon>> entry : grid.entrySet()) {
            String cellKey = entry.getKey();
            List<PropertyPolygon> propertiesInCell = entry.getValue();
            System.out.println("Cell " + cellKey + " has " + propertiesInCell.size() + " properties.");
        }
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
     * based on the spatial grid, including properties in the same and adjacent cells.
     *
     * @param property The property whose neighbors are to be found.
     * @return A list of properties that share at least one grid cell with the given property.
     */
    // Obtém terrenos próximos com base nas células vizinhas
    public List<PropertyPolygon> getNearbyProperties(PropertyPolygon property) {
        Set<PropertyPolygon> nearby = new HashSet<>();

        // Recupera as células nas quais o terreno está presente (deve ser só uma)
        List<String> propertyCells = getPropertyGridCells(property);

        System.out.println("\n--- Checking property " + property.getObjectId() + " -> " + propertyCells + " ---");

        for (String cellKey : propertyCells) {
            String[] parts = cellKey.split("-");
            int x = Integer.parseInt(parts[0]);
            int y = Integer.parseInt(parts[1]);

            // Verifica a célula atual e as 8 células ao redor
            for (int dx = -1; dx <= 1; dx++) {
                for (int dy = -1; dy <= 1; dy++) {
                    int adjX = x + dx;
                    int adjY = y + dy;

                    if (adjX < 0 || adjX > MAX_GRID_X || adjY < 0 || adjY > MAX_GRID_Y) continue;

                    String adjCellKey = adjX + "-" + adjY;

                    if (grid.containsKey(adjCellKey)) {
                        for (PropertyPolygon other : grid.get(adjCellKey)) {
                            if (!other.equals(property)) {
//                                System.out.println(" → Property " + property.getObjectId() +
//                                        " compared with Property " + other.getObjectId() +
//                                        " in cell " + adjCellKey);
                                nearby.add(other);
                            }
                        }
                    }
                }
            }
        }
        return new ArrayList<>(nearby);
    }

    private void checkAndAddNearbyProperties(String cellKey, Set<PropertyPolygon> nearby) {
        // Verifica se a célula contém propriedades e as adiciona ao conjunto de propriedades próximas
        if (grid.containsKey(cellKey)) {
            for (PropertyPolygon property : grid.get(cellKey)) {
                nearby.add(property);  // Apenas adiciona se ainda não foi adicionada
            }
        }
    }
}