package logic;

/**
 * 
 * Utility class for functionality available to and used by logic classes.
 * 
 * @author Michael Bowes
 *
 */
public class Util {

	/**
	 * Calculates the n-th root of the base.
	 *
	 */
	public static double calcNthRoot(double base, double n) {
		return Math.pow(Math.E, Math.log(base) / n);
	}

	/**
	 * 
	 * Returns the maximum value out of five integers.
	 */
	public static int getMax(int one, int two, int three, int four, int five) {

		int maximum = Math.max(Math.max(Math.max(one, two), Math.max(two, three)),
				Math.max(Math.max(three, four), Math.max(four, five)));

		return maximum;
	}
	
	/**
	 * 
	 * Returns the minimum value out of five integers.
	 */
	public static int getMin(int one, int two, int three, int four, int five) {

		int minimum = Math.min(Math.min(Math.min(one, two), Math.min(two, three)),
				Math.min(Math.min(three, four), Math.min(four, five)));

		return minimum;
	}

	/**
	 * Prints the values of a generated map to the console.<br> 
	 * Used for debugging and testing purposes.
	 * 
	 * @param map to print.
	 */
	public void printMap(int[][] map) // this prints the 2D array.
	{
		for (int i = 0; i < map.length; i++) {
			for (int j = 0; j < map.length; j++) {
				System.out.print(map[j][i] + "\t");
			}
			System.out.println("");
		}
	}

}
