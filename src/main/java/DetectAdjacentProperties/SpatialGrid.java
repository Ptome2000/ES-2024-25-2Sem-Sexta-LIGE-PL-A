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
        Set<String> uniqueCells = new HashSet<>();

        for (VertexCoordinate vertex : property.getPolygon().getCoordenadas()) {
            uniqueCells.add(getCellKey(vertex.getX(), vertex.getY()));
        }

        // Escolher apenas uma célula (por exemplo, a com menor valor lexicográfico)
        Optional<String> selectedCell = uniqueCells.stream().sorted().findFirst();

        if (selectedCell.isPresent()) {
            String cellKey = selectedCell.get();
            grid.computeIfAbsent(cellKey, k -> new ArrayList<>()).add(property);

            // Debug: Mostra em que célula foi colocado o terreno
            System.out.println("Property " + property.getObjectId() + " inserted in cell: " + cellKey);
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

        System.out.println("\n--- Checking property " + property.getObjectId() + " ---");

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
                                System.out.println(" → Property " + property.getObjectId() +
                                        " compared with Property " + other.getObjectId() +
                                        " in cell " + adjCellKey);
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