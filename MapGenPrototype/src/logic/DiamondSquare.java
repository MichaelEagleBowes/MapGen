package logic;

/**
 * 
 * Header class for starting the diamond square algorithm. Runs the generation
 * process on a separate thread, since the performance may drop for larger maps.
 * 
 * @author Michael Bowes
 * 
 */

public class DiamondSquare implements ProceduralAlgorithm {

	String name;
	int[][] map;
	private boolean hasMap;
	private int mapSize;

	public DiamondSquare() {
		this.name = "DiamondSquare";
	}

	/**
	 * 
	 * Generates a single square map with the given size and name.
	 * 
	 * @param size The square dimension for the map, e.g. 4 generates a map of size
	 *             4*4<br>
	 *             size needs to always take a value of 2^x + 1.
	 * @return
	 */
	@Override
	public int[][] generateMap(int size) {
		// TODO: Durchlaufe das 2D Array und ändere alle Zahlen
		// im Bereich des Grass-Gebiets(von X bis Y) ab.

		DiamondSquareImpl algo = new DiamondSquareImpl(size);
		Thread t = new Thread(algo);
		t.start();
		try {
			t.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		map = algo.getMap();

		hasMap = true;
		return map;
	}

	/**
	 * Calculates the number of areas for each terrain type.
	 * @return
	 */
	public static double calcNumberOfAreas() {
		double areaCount = 0;

		return areaCount;
	}

	/**
	 * Calculates the average distance of a terrain type to the center of the map
	 * and the border.
	 * 
	 * @return
	 */
	public static double calcAbsolutePositions() {
		double areaCount = 0;

		return areaCount;
	}

	/**
	 * Calculates the distance of each area of a terrain type to other areas of the
	 * same type.
	 * 
	 * @return
	 */
	public static double calcRelativePositions() {
		double areaCount = 0;

		return areaCount;
	}

	@Override
	public boolean mapPresent() {
		return hasMap;
	}

	@Override
	public int[][] getMap() {
		return map;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public String toString() {
		return name;
	}

}