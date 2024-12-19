package simulator;

public class Main {
	public static void main(String[] args) {

	        String configPath = "src/main/resources/config/config.json";
	        FireSimulation simulation = new FireSimulation(configPath);  

	        int[][] initialBurningCells = {{5, 5}};
	        simulation.startFire(initialBurningCells);

	        while (!simulation.isSimulationOver()) {
	            simulation.printGrid();
	            System.out.println();
	            simulation.propagateFire(); 
	            try {
	                Thread.sleep(2000); 
	            } catch (InterruptedException e) {
	                e.printStackTrace();
	            }
	        }

	        System.out.println("Simulation terminée.");
	    }
}
