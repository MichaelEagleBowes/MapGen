package logic;

/**
 * 
 * The interface for PCG algorithms.
 * 
 * @author Michael Bowes
 * 
 */
public interface ProceduralAlgorithm {

	/**
	 * Getter for the name of an algorithm.
	 *
	 * @return the name of the algorithm.
	 */
	String getName();
	
	/**
	 * 
	 */
	int[][] getMap();
	
	boolean mapPresent();

	/**
	 * Generates a new map with the given parameters.
	 * @return
	 */
	int[][] generateMap(int size);
}