package logic;

import java.util.ArrayList;
import java.util.List;

import util.Tuple;

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
	private static int[][] map;
	private boolean hasMap;
	private int mapSize;

	private static List<Double> areaCount;
	/**
	 * 
	 * Stores the sum of all X and Y coordinates for each terrain type for
	 * calculation of the Bowes Distance. The terrain types are sorted in ascending
	 * order by height values.
	 */
	private static List<Tuple<Integer, Integer, Integer>> sumValues;
	/**
	 * The arithmetic mean values of each terrains' X and Y coordinates across the map. The
	 * terrain types are sorted in ascending order by height values.
	 */
	private static List<List<Integer>> bowesDistance;

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
	 * Calculates the number of separate areas for each terrain type and returns a
	 * list of the area count per terrain in ascending order of height values, e.g.
	 * index 0 is the number of water bodies on the map.
	 * 
	 * @return areaCount A list of the number of each terrain's separate areas
	 */
	public static List<Double> calcNumberOfAreas(int snowParam, int mountainParam, int forestParam, int grassParam,
			int beachParam, int coastParam, int oceanParam) {
		int mapHeight = map.length;
		int mapWidth = map.length;

		int minimum = 0;
		int maximum = 0;
		for (int i = 0; i < mapWidth; i++) {
			for (int j = 0; j < mapHeight; j++) {
				if (map[j][i] > maximum) {
					maximum = map[j][i];
				}
				if (map[j][i] < minimum) {
					minimum = map[j][i];
				}
			}
		}
		int spectrum = Math.abs(minimum) + maximum;
		double oceanSpectrum = spectrum * (oceanParam * 0.01);
		double coastSpectrum = spectrum * (coastParam * 0.01);
		double beachSpectrum = spectrum * (beachParam * 0.01);
		double grassSpectrum = spectrum * (grassParam * 0.01);
		double forestSpectrum = spectrum * (forestParam * 0.01);
		double mountainSpectrum = spectrum * (mountainParam * 0.01);
		double snowSpectrum = spectrum * (snowParam * 0.01);

		areaCount = new ArrayList<Double>();
		double oceanCount = 0;
		double coastCount = 0;
		double beachCount = 0;
		double grassCount = 0;
		double forestCount = 0;
		double mountainCount = 0;
		double snowCount = 0;

		int[][] testMap = new int[map.length][map.length];
		for (int i = 0; i < testMap.length; i++) {
			for (int j = 0; j < testMap.length; j++) {
				testMap[i][j] = map[i][j];
			}
		}

		for (int x = 0; x < testMap.length; x++) {
			for (int y = 0; y < testMap.length; y++) {
				int areaValue = floodfill(testMap, x, y, minimum, oceanSpectrum, coastSpectrum, beachSpectrum,
						grassSpectrum, forestSpectrum, mountainSpectrum, snowSpectrum);

				switch (areaValue) {
				case 0:
					oceanCount++;
					break;
				case 1:
					coastCount++;
					break;
				case 2:
					beachCount++;
					break;
				case 3:
					grassCount++;
					break;
				case 4:
					forestCount++;
					break;
				case 5:
					mountainCount++;
					break;
				case 6:
					snowCount++;
					break;
				}
			}
		}

		areaCount.add(0, oceanCount);
		areaCount.add(1, coastCount);
		areaCount.add(2, beachCount);
		areaCount.add(3, grassCount);
		areaCount.add(4, forestCount);
		areaCount.add(5, mountainCount);
		areaCount.add(6, snowCount);

		return areaCount;
	}

	public static int floodfill(int[][] testMap, int x, int y, int minimum, double oceanSpectrum, double coastSpectrum,
			double beachSpectrum, double grassSpectrum, double forestSpectrum, double mountainSpectrum,
			double snowSpectrum) {
		int count = 0;
		if (x < 0 || y < 0 || x >= testMap.length || y >= testMap.length) {
			return count;
		} else {
			if (testMap[x][y] < minimum + oceanSpectrum) {
				testMap[x][y] = 999;
				count = 0;
				int echo1 = floodfill(testMap, x - 1, y, minimum, oceanSpectrum, coastSpectrum, beachSpectrum,
						grassSpectrum, forestSpectrum, mountainSpectrum, snowSpectrum);
				int echo2 = floodfill(testMap, x + 1, y, minimum, oceanSpectrum, coastSpectrum, beachSpectrum,
						grassSpectrum, forestSpectrum, mountainSpectrum, snowSpectrum);
				int echo3 = floodfill(testMap, x, y - 1, minimum, oceanSpectrum, coastSpectrum, beachSpectrum,
						grassSpectrum, forestSpectrum, mountainSpectrum, snowSpectrum);
				int echo4 = floodfill(testMap, x, y + 1, minimum, oceanSpectrum, coastSpectrum, beachSpectrum,
						grassSpectrum, forestSpectrum, mountainSpectrum, snowSpectrum);

				return Util.getMax(count, echo1, echo2, echo3, echo4);
			} else if (testMap[x][y] < minimum + oceanSpectrum + coastSpectrum) {
				testMap[x][y] = 999;
				count = 1;
				int echo1 = floodfill(testMap, x - 1, y, minimum, oceanSpectrum, coastSpectrum, beachSpectrum,
						grassSpectrum, forestSpectrum, mountainSpectrum, snowSpectrum);
				int echo2 = floodfill(testMap, x + 1, y, minimum, oceanSpectrum, coastSpectrum, beachSpectrum,
						grassSpectrum, forestSpectrum, mountainSpectrum, snowSpectrum);
				int echo3 = floodfill(testMap, x, y - 1, minimum, oceanSpectrum, coastSpectrum, beachSpectrum,
						grassSpectrum, forestSpectrum, mountainSpectrum, snowSpectrum);
				int echo4 = floodfill(testMap, x, y + 1, minimum, oceanSpectrum, coastSpectrum, beachSpectrum,
						grassSpectrum, forestSpectrum, mountainSpectrum, snowSpectrum);
				return Util.getMax(count, echo1, echo2, echo3, echo4);
			} else if (testMap[x][y] < minimum + oceanSpectrum + coastSpectrum + beachSpectrum) {
				testMap[x][y] = 999;
				count = 2;
				int echo1 = floodfill(testMap, x - 1, y, minimum, oceanSpectrum, coastSpectrum, beachSpectrum,
						grassSpectrum, forestSpectrum, mountainSpectrum, snowSpectrum);
				int echo2 = floodfill(testMap, x + 1, y, minimum, oceanSpectrum, coastSpectrum, beachSpectrum,
						grassSpectrum, forestSpectrum, mountainSpectrum, snowSpectrum);
				int echo3 = floodfill(testMap, x, y - 1, minimum, oceanSpectrum, coastSpectrum, beachSpectrum,
						grassSpectrum, forestSpectrum, mountainSpectrum, snowSpectrum);
				int echo4 = floodfill(testMap, x, y + 1, minimum, oceanSpectrum, coastSpectrum, beachSpectrum,
						grassSpectrum, forestSpectrum, mountainSpectrum, snowSpectrum);
				return Util.getMax(count, echo1, echo2, echo3, echo4);
			} else if (testMap[x][y] < minimum + oceanSpectrum + coastSpectrum + beachSpectrum + grassSpectrum) {
				testMap[x][y] = 999;
				count = 3;
				int echo1 = floodfill(testMap, x - 1, y, minimum, oceanSpectrum, coastSpectrum, beachSpectrum,
						grassSpectrum, forestSpectrum, mountainSpectrum, snowSpectrum);
				int echo2 = floodfill(testMap, x + 1, y, minimum, oceanSpectrum, coastSpectrum, beachSpectrum,
						grassSpectrum, forestSpectrum, mountainSpectrum, snowSpectrum);
				int echo3 = floodfill(testMap, x, y - 1, minimum, oceanSpectrum, coastSpectrum, beachSpectrum,
						grassSpectrum, forestSpectrum, mountainSpectrum, snowSpectrum);
				int echo4 = floodfill(testMap, x, y + 1, minimum, oceanSpectrum, coastSpectrum, beachSpectrum,
						grassSpectrum, forestSpectrum, mountainSpectrum, snowSpectrum);
				return Util.getMax(count, echo1, echo2, echo3, echo4);
			} else if (testMap[x][y] < minimum + oceanSpectrum + coastSpectrum + beachSpectrum + grassSpectrum
					+ forestSpectrum) {
				testMap[x][y] = 999;
				count = 4;
				int echo1 = floodfill(testMap, x - 1, y, minimum, oceanSpectrum, coastSpectrum, beachSpectrum,
						grassSpectrum, forestSpectrum, mountainSpectrum, snowSpectrum);
				int echo2 = floodfill(testMap, x + 1, y, minimum, oceanSpectrum, coastSpectrum, beachSpectrum,
						grassSpectrum, forestSpectrum, mountainSpectrum, snowSpectrum);
				int echo3 = floodfill(testMap, x, y - 1, minimum, oceanSpectrum, coastSpectrum, beachSpectrum,
						grassSpectrum, forestSpectrum, mountainSpectrum, snowSpectrum);
				int echo4 = floodfill(testMap, x, y + 1, minimum, oceanSpectrum, coastSpectrum, beachSpectrum,
						grassSpectrum, forestSpectrum, mountainSpectrum, snowSpectrum);
				return Util.getMax(count, echo1, echo2, echo3, echo4);
			} else if (testMap[x][y] < minimum + oceanSpectrum + coastSpectrum + beachSpectrum + grassSpectrum
					+ forestSpectrum + mountainSpectrum) {
				testMap[x][y] = 999;
				count = 5;
				int echo1 = floodfill(testMap, x - 1, y, minimum, oceanSpectrum, coastSpectrum, beachSpectrum,
						grassSpectrum, forestSpectrum, mountainSpectrum, snowSpectrum);
				int echo2 = floodfill(testMap, x + 1, y, minimum, oceanSpectrum, coastSpectrum, beachSpectrum,
						grassSpectrum, forestSpectrum, mountainSpectrum, snowSpectrum);
				int echo3 = floodfill(testMap, x, y - 1, minimum, oceanSpectrum, coastSpectrum, beachSpectrum,
						grassSpectrum, forestSpectrum, mountainSpectrum, snowSpectrum);
				int echo4 = floodfill(testMap, x, y + 1, minimum, oceanSpectrum, coastSpectrum, beachSpectrum,
						grassSpectrum, forestSpectrum, mountainSpectrum, snowSpectrum);
				return Util.getMax(count, echo1, echo2, echo3, echo4);
			} else if (testMap[x][y] < minimum + oceanSpectrum + coastSpectrum + beachSpectrum + grassSpectrum
					+ forestSpectrum + mountainSpectrum + snowSpectrum) {
				testMap[x][y] = 999;
				count = 6;
				int echo1 = floodfill(testMap, x - 1, y, minimum, oceanSpectrum, coastSpectrum, beachSpectrum,
						grassSpectrum, forestSpectrum, mountainSpectrum, snowSpectrum);
				int echo2 = floodfill(testMap, x + 1, y, minimum, oceanSpectrum, coastSpectrum, beachSpectrum,
						grassSpectrum, forestSpectrum, mountainSpectrum, snowSpectrum);
				int echo3 = floodfill(testMap, x, y - 1, minimum, oceanSpectrum, coastSpectrum, beachSpectrum,
						grassSpectrum, forestSpectrum, mountainSpectrum, snowSpectrum);
				int echo4 = floodfill(testMap, x, y + 1, minimum, oceanSpectrum, coastSpectrum, beachSpectrum,
						grassSpectrum, forestSpectrum, mountainSpectrum, snowSpectrum);
				return Util.getMax(count, echo1, echo2, echo3, echo4);
			} else {
				return count;
			}
		}
	}

	/**
	 * Calculates the average distance of a terrain type to the center of the map
	 * and the border by building the arithmetic mean of all X and the Y coordinates of
	 * the tiles belonging to the same terrain.
	 * 
	 * @return
	 */
	public static List<List<Integer>> calcAbsolutePositions(int minimum, double oceanSpectrum,
			double coastSpectrum, double beachSpectrum, double grassSpectrum, double forestSpectrum,
			double mountainSpectrum, double snowSpectrum) {

		bowesDistance = new ArrayList<List<Integer>>();
		sumValues = calcSumValues(minimum, oceanSpectrum, coastSpectrum, beachSpectrum, grassSpectrum,
				forestSpectrum, mountainSpectrum, snowSpectrum);
		List<Integer> oceanAvg = new ArrayList<Integer>();
		int oceanX = (int)sumValues.get(0).getSecondValue()/(int)sumValues.get(0).getFirstValue();
		int oceanY = (int)sumValues.get(0).getThirdValue()/(int)sumValues.get(0).getFirstValue();
		oceanAvg.add(0, oceanX);
		oceanAvg.add(0, oceanY);
		
		List<Integer> coastAvg = new ArrayList<Integer>();
		int coastX = (int)sumValues.get(1).getSecondValue()/(int)sumValues.get(1).getFirstValue();
		int coastY = (int)sumValues.get(1).getThirdValue()/(int)sumValues.get(1).getFirstValue();
		coastAvg.add(0, coastX);
		coastAvg.add(0, coastY);
		
		List<Integer> beachAvg = new ArrayList<Integer>();
		int beachX = (int)sumValues.get(2).getSecondValue()/(int)sumValues.get(2).getFirstValue();
		int beachY = (int)sumValues.get(2).getThirdValue()/(int)sumValues.get(2).getFirstValue();
		oceanAvg.add(0, oceanX);
		oceanAvg.add(0, oceanY);
		
		List<Integer> grassAvg = new ArrayList<Integer>();
		int grassX = (int)sumValues.get(3).getSecondValue()/(int)sumValues.get(3).getFirstValue();
		int grassY = (int)sumValues.get(3).getThirdValue()/(int)sumValues.get(3).getFirstValue();
		grassAvg.add(0, grassX);
		grassAvg.add(0, grassY);
		
		List<Integer> forestAvg = new ArrayList<Integer>();
		int forestX = (int)sumValues.get(4).getSecondValue()/(int)sumValues.get(4).getFirstValue();
		int forestY = (int)sumValues.get(4).getThirdValue()/(int)sumValues.get(4).getFirstValue();
		forestAvg.add(0, forestX);
		forestAvg.add(0, forestY);
		
		List<Integer> mountainAvg = new ArrayList<Integer>();
		int mountainX = (int)sumValues.get(5).getSecondValue()/(int)sumValues.get(5).getFirstValue();
		int mountainY = (int)sumValues.get(5).getThirdValue()/(int)sumValues.get(5).getFirstValue();
		mountainAvg.add(0, mountainX);
		mountainAvg.add(0, mountainY);
		
		List<Integer> snowAvg = new ArrayList<Integer>();
		int snowX = (int)sumValues.get(6).getSecondValue()/(int)sumValues.get(6).getFirstValue();
		int snowY = (int)sumValues.get(6).getThirdValue()/(int)sumValues.get(6).getFirstValue();
		snowAvg.add(0, snowX);
		snowAvg.add(0, snowY);
		
		return bowesDistance;
	}

	/**
	 * 
	 * Calculates the sums for X and Y coordinates of each terrain type.
	 * 
	 * @param minimum
	 * @param oceanSpectrum
	 * @param coastSpectrum
	 * @param beachSpectrum
	 * @param grassSpectrum
	 * @param forestSpectrum
	 * @param mountainSpectrum
	 * @param snowSpectrum
	 * @return
	 */
	private static List<Tuple<Integer, Integer, Integer>> calcSumValues(int minimum, double oceanSpectrum,
			double coastSpectrum, double beachSpectrum, double grassSpectrum, double forestSpectrum,
			double mountainSpectrum, double snowSpectrum) {
		double areaCount = 0;
		sumValues = new ArrayList<Tuple<Integer, Integer, Integer>>();
		Tuple<Integer, Integer, Integer> oceanTuple = new Tuple<Integer, Integer, Integer>();
		Tuple<Integer, Integer, Integer> coastTuple = new Tuple<Integer, Integer, Integer>();
		Tuple<Integer, Integer, Integer> beachTuple = new Tuple<Integer, Integer, Integer>();
		Tuple<Integer, Integer, Integer> grassTuple = new Tuple<Integer, Integer, Integer>();
		Tuple<Integer, Integer, Integer> forestTuple = new Tuple<Integer, Integer, Integer>();
		Tuple<Integer, Integer, Integer> mountainTuple = new Tuple<Integer, Integer, Integer>();
		Tuple<Integer, Integer, Integer> snowTuple = new Tuple<Integer, Integer, Integer>();

		for (int i = 0; i < map.length; i++) {
			for (int j = 0; j < map.length; j++) {
				if (map[i][j] < minimum + oceanSpectrum) {
					oceanTuple.addFirstValue((int) oceanTuple.getFirstValue() + 1);
					oceanTuple.addSecondValue((int) oceanTuple.getSecondValue() + i);
					oceanTuple.addThirdValue((int) oceanTuple.getThirdValue() + j);
				} else if (map[i][j] < minimum + oceanSpectrum + coastSpectrum) {
					coastTuple.addFirstValue((int) coastTuple.getFirstValue() + 1);
					coastTuple.addSecondValue((int) coastTuple.getSecondValue() + i);
					coastTuple.addThirdValue((int) coastTuple.getThirdValue() + j);
				} else if (map[i][j] < minimum + oceanSpectrum + coastSpectrum + beachSpectrum) {
					beachTuple.addFirstValue((int) beachTuple.getFirstValue() + 1);
					beachTuple.addSecondValue((int) beachTuple.getSecondValue() + i);
					beachTuple.addThirdValue((int) beachTuple.getThirdValue() + j);
				} else if (map[i][j] < minimum + oceanSpectrum + coastSpectrum + beachSpectrum + grassSpectrum) {
					grassTuple.addFirstValue((int) grassTuple.getFirstValue() + 1);
					grassTuple.addSecondValue((int) grassTuple.getSecondValue() + i);
					grassTuple.addThirdValue((int) grassTuple.getThirdValue() + j);
				} else if (map[i][j] < minimum + oceanSpectrum + coastSpectrum + beachSpectrum + grassSpectrum
						+ forestSpectrum) {
					forestTuple.addFirstValue((int) forestTuple.getFirstValue() + 1);
					forestTuple.addSecondValue((int) forestTuple.getSecondValue() + i);
					forestTuple.addThirdValue((int) forestTuple.getThirdValue() + j);
				} else if (map[i][j] < minimum + oceanSpectrum + coastSpectrum + beachSpectrum + grassSpectrum
						+ forestSpectrum + mountainSpectrum) {
					mountainTuple.addFirstValue((int) mountainTuple.getFirstValue() + 1);
					mountainTuple.addSecondValue((int) mountainTuple.getSecondValue() + i);
					mountainTuple.addThirdValue((int) mountainTuple.getThirdValue() + j);
				} else if (map[i][j] < minimum + oceanSpectrum + coastSpectrum + beachSpectrum + grassSpectrum
						+ forestSpectrum + mountainSpectrum + snowSpectrum) {
					snowTuple.addFirstValue((int) snowTuple.getFirstValue() + 1);
					snowTuple.addSecondValue((int) snowTuple.getSecondValue() + i);
					snowTuple.addThirdValue((int) snowTuple.getThirdValue() + j);
				}

			}
		}

		return sumValues;
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