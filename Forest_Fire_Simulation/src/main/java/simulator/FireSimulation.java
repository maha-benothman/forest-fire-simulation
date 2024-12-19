package simulator; 

import java.io.IOException; 
import java.nio.file.Files; 
import java.nio.file.Paths; 
import java.util.Random; 

import com.google.gson.JsonObject; 
import com.google.gson.JsonParser;

/**
 * Class representing a fire simulation on a grid.
 */
public class FireSimulation {

    
    enum CellState {
        FIRE,   
        ASH,     
        EMPTY    
    }

    private CellState[][] grid; // Grid representing the state of cells
    private int width;          // Width of the grid
    private int height;         // Height of the grid
    private double probability; // Probability of fire spreading

    /**
     * Constructor initializing the simulation with a configuration file.
     * 
     * @param configPath Path to the configuration JSON file
     */
    public FireSimulation(String configPath) {
        JsonObject config = loadConfig(configPath);
        this.height = config.get("height").getAsInt(); 
        this.width = config.get("width").getAsInt(); 
        this.probability = config.get("probability").getAsDouble();
        this.grid = new CellState[height][width]; 
        initializeGrid(); 
    }

    /**
     * Loads the configuration from a JSON file.
     * 
     * @param path Path to the JSON file
     * @return Parsed JsonObject
     */
    private JsonObject loadConfig(String path) {
        try {
            String content = new String(Files.readAllBytes(Paths.get(path))); 
            return JsonParser.parseString(content).getAsJsonObject();
        } catch (IOException e) {
            throw new RuntimeException("Failed to load configuration file", e); 
        }
    }

    
    /**
     * Initializes the grid with EMPTY cells.
     */
    private void initializeGrid() {
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                grid[i][j] = CellState.EMPTY; 
            }
        }
    }

    /**
     * Starts fire at specific initial positions.
     * 
     * @param initialBurningCells Initial burning cell positions
     */
    public void startFire(int[][] initialBurningCells) {
        for (int[] cell : initialBurningCells) {
            int x = cell[0];
            int y = cell[1];
            grid[y][x] = CellState.FIRE; 
        }
    }

    /**
     * Displays the grid in the console.
     */
    public void printGrid() {
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                switch (grid[i][j]) {
                    case FIRE:
                        System.out.print("🔥 "); 
                        break;
                    case ASH:
                        System.out.print("🪶 "); 
                        break;
                    case EMPTY:
                        System.out.print("⚪ "); 
                        break;
                }
            }
            System.out.println();
        }
    }

    /**
     * Propagates fire across the grid.
     */
    public void propagateFire() {
        CellState[][] nextGrid = new CellState[height][width]; 

        for (int i = 0; i < height; i++) {
            System.arraycopy(grid[i], 0, nextGrid[i], 0, width); 
        }

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                if (grid[y][x] == CellState.FIRE) {
                    nextGrid[y][x] = CellState.ASH; 
                    tryToSpreadFire(x, y, nextGrid);
                }
            }
        }

        grid = nextGrid; 
    }

    /**
     * Attempts to spread fire to neighboring cells.
     * 
     * @param x         X-coordinate of the burning cell
     * @param y         Y-coordinate of the burning cell
     * @param nextGrid  New grid for propagation
     */
    private void tryToSpreadFire(int x, int y, CellState[][] nextGrid) {
        int[] directions = {-1, 0, 1, 0, 0, -1, 0, 1}; // Left, Right, Up, Down directions
        Random rand = new Random(); 

        for (int i = 0; i < 4; i++) {
            int newX = x + directions[2 * i];
            int newY = y + directions[2 * i + 1];

            if (newX >= 0 && newX < width && newY >= 0 && newY < height) {
                if (nextGrid[newY][newX] == CellState.EMPTY && rand.nextDouble() < probability) {
                    nextGrid[newY][newX] = CellState.FIRE; // Spread fire
                }
            }
        }
    }

    /**
     * Checks if the simulation is over (no more burning cells).
     * 
     * @return True if the simulation is over, otherwise false
     */
    public boolean isSimulationOver() {
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                if (grid[i][j] == CellState.FIRE) {
                    return false;
                }
            }
        }
        return true; 
    }
}
