package logic;

import javafx.beans.property.StringProperty;
import model.Map;

/**
 * 
 * The interface for PCG algorithms.
 */
public interface ProceduralAlgorithm {

	/**
	 * Getter for the name of an algorithm.
	 *
	 * @return the name of the algorithm.
	 */
	String getName();

	/**
	 * Generates a new map with the given parameters.
	 * @return
	 */
	Map generate();
}