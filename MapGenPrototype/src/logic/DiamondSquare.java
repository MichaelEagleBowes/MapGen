package logic;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import util.Triple;

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
	private int[][] map;
	private boolean hasMap;
	private int mapSize;
	private int[][] testMap;

	private List<Double> areaCount;
	private List<Double> minima;
	private HashMap<Integer, List<Integer>> averageAreaXCoordinates;
	private HashMap<Integer, List<Integer>> averageAreaYCoordinates;
	private double oceanSpectrum;
	private double coastSpectrum;
	private double beachSpectrum;
	private double grassSpectrum;
	private double forestSpectrum;
	private double mountainSpectrum;
	private double snowSpectrum;
	private int minimum;
	private int oceanParam;
	private int coastParam;
	private int beachParam;
	private int grassParam;
	private int forestParam;
	private int snowParam;
	private int mountainParam;
	
	/**
	 * 
	 * Stores the sum of all X and Y coordinates for each terrain type for
	 * calculation of the Bowes Distance. The terrain types are sorted in ascending
	 * order by height values.
	 */
	//private HashMap<Integer, Triple<Integer, Integer, Integer>> sumValues;
	/**
	 * The arithmetic mean values of each terrains' X and Y coordinates across the
	 * map. The terrain types are sorted in ascending order by height values.
	 */
	private List<Double> bowesDistanceAvg;
	/**
	 * The average of the arithmetic mean values of each terrains' X and Y coordinates across the
	 * map. The terrain types are sorted in ascending order by height values.
	 */
	private HashMap<Integer, List<Double>> bowesDistance;

	public DiamondSquare() {
		this.name = "DiamondSquare";
		averageAreaXCoordinates = new HashMap<>();
		averageAreaYCoordinates = new HashMap<>();
		for (int i = 0; i < 7; i++) {
			averageAreaXCoordinates.put(i, new ArrayList<Integer>());
			averageAreaYCoordinates.put(i, new ArrayList<Integer>());
		}
	}

	public DiamondSquare(int oceanParam, int coastParam, int beachParam, int grassParam,
			int forestParam, int snowParam, int mountainParam) {
		this.oceanParam = oceanParam;
		this.coastParam = coastParam;
		this.beachParam = beachParam;
		this.grassParam = grassParam;
		this.forestParam = forestParam;
		this.snowParam = snowParam;
		this.mountainParam = mountainParam;
		this.name = "DiamondSquare";
		averageAreaXCoordinates = new HashMap<>();
		averageAreaYCoordinates = new HashMap<>();
		for (int i = 0; i < 7; i++) {
			averageAreaXCoordinates.put(i, new ArrayList<Integer>());
			averageAreaYCoordinates.put(i, new ArrayList<Integer>());
		}
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
		hasMap = true;

		DiamondSquareThread algo = new DiamondSquareThread(size);
		Thread t = new Thread(algo);
		t.start();
		try {
			t.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		map = algo.getMap();
		
		minimum = 0;
		int maximum = 0;
		for (int i = 0; i < map.length; i++) {
			for (int j = 0; j < map.length; j++) {
				if (map[j][i] > maximum) {
					maximum = map[j][i];
				}
				if (map[j][i] < minimum) {
					minimum = map[j][i];
				}
			}
		}
		int spectrum = Math.abs(minimum) + maximum;
		oceanSpectrum = spectrum * (oceanParam * 0.01);
		coastSpectrum = spectrum * (coastParam * 0.01);
		beachSpectrum = spectrum * (beachParam * 0.01);
		grassSpectrum = spectrum * (grassParam * 0.01);
		forestSpectrum = spectrum * (forestParam * 0.01);
		snowSpectrum = spectrum * (snowParam * 0.01);
		mountainSpectrum = spectrum * (mountainParam * 0.01);

		return map;
	}

	/**
	 * Calculates the number of separate areas for each terrain type and returns a
	 * list of the area count per terrain in ascending order of height values, e.g.
	 * index 0 is the number of water bodies on the map.
	 * 
	 * @return areaCount A list of the number of each terrain's separate areas
	 */
	public List<Double> calcNumberOfAreas() {
		areaCount = new ArrayList<Double>();
		double oceanCount = 0;
		double coastCount = 0;
		double beachCount = 0;
		double grassCount = 0;
		double forestCount = 0;
		double mountainCount = 0;
		double snowCount = 0;

		testMap = new int[map.length][map.length];
		for (int i = 0; i < testMap.length; i++) {
			for (int j = 0; j < testMap.length; j++) {
				testMap[i][j] = map[i][j];
			}
		}

		for (int x = 0; x < testMap.length; x++) {
			for (int y = 0; y < testMap.length; y++) {
				double i = minimum + oceanSpectrum;
				FloodFillAlgorithm ff = new FloodFillAlgorithm(testMap, x, y, (int) Math.ceil(i));
				Thread thread = new Thread(ff);
				thread.start();
				try {
					thread.join();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				if (ff.getResult()) {
					oceanCount++;
				}
				averageAreaXCoordinates.get(0).add(ff.getAverageXCoordinate());
				averageAreaYCoordinates.get(0).add(ff.getAverageYCoordinate());
			}
		}

		for (int x = 0; x < testMap.length; x++) {
			for (int y = 0; y < testMap.length; y++) {
				double upperBound = minimum + oceanSpectrum + coastSpectrum;
				double lowerBound = minimum + oceanSpectrum;
				FloodFillAlgorithm ff = new FloodFillAlgorithm(testMap, x, y, (int) Math.ceil(lowerBound),
						(int) Math.ceil(upperBound));
				Thread thread = new Thread(ff);
				thread.start();
				try {
					thread.join();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				if (ff.getResult()) {
					coastCount++;
				}
				averageAreaXCoordinates.get(1).add(ff.getAverageXCoordinate());
				averageAreaYCoordinates.get(1).add(ff.getAverageYCoordinate());
			}
		}

		for (int x = 0; x < testMap.length; x++) {
			for (int y = 0; y < testMap.length; y++) {
				double lowerBound = minimum + oceanSpectrum + coastSpectrum;
				double upperBound = minimum + oceanSpectrum + coastSpectrum + beachSpectrum;
				FloodFillAlgorithm ff = new FloodFillAlgorithm(testMap, x, y, (int) Math.ceil(lowerBound),
						(int) Math.ceil(upperBound));
				Thread thread = new Thread(ff);
				thread.start();
				try {
					thread.join();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				if (ff.getResult()) {
					beachCount++;
				}
				averageAreaXCoordinates.get(2).add(ff.getAverageXCoordinate());
				averageAreaYCoordinates.get(2).add(ff.getAverageYCoordinate());
			}
		}

		for (int x = 0; x < testMap.length; x++) {
			for (int y = 0; y < testMap.length; y++) {
				double upperBound = minimum + oceanSpectrum + coastSpectrum + beachSpectrum + grassSpectrum;
				double lowerBound = minimum + oceanSpectrum + coastSpectrum + beachSpectrum;
				FloodFillAlgorithm ff = new FloodFillAlgorithm(testMap, x, y, (int) Math.ceil(lowerBound),
						(int) Math.ceil(upperBound));
				Thread thread = new Thread(ff);
				thread.start();
				try {
					thread.join();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				if (ff.getResult()) {
					grassCount++;
				}
				averageAreaXCoordinates.get(3).add(ff.getAverageXCoordinate());
				averageAreaYCoordinates.get(3).add(ff.getAverageYCoordinate());
			}
		}

		for (int x = 0; x < testMap.length; x++) {
			for (int y = 0; y < testMap.length; y++) {
				double lowerBound = minimum + oceanSpectrum + coastSpectrum + beachSpectrum + grassSpectrum;
				double upperBound = minimum + oceanSpectrum + coastSpectrum + beachSpectrum + grassSpectrum
						+ forestSpectrum;
				FloodFillAlgorithm ff = new FloodFillAlgorithm(testMap, x, y, (int) Math.ceil(lowerBound),
						(int) Math.ceil(upperBound));
				Thread thread = new Thread(ff);
				thread.start();
				try {
					thread.join();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				if (ff.getResult()) {
					forestCount++;
				}
				averageAreaXCoordinates.get(4).add(ff.getAverageXCoordinate());
				averageAreaYCoordinates.get(4).add(ff.getAverageYCoordinate());
			}
		}

		for (int x = 0; x < testMap.length; x++) {
			for (int y = 0; y < testMap.length; y++) {
				double upperBound = minimum + oceanSpectrum + coastSpectrum + beachSpectrum + grassSpectrum
						+ forestSpectrum + mountainSpectrum;
				double lowerBound = minimum + oceanSpectrum + coastSpectrum + beachSpectrum + grassSpectrum
						+ forestSpectrum;
				FloodFillAlgorithm ff = new FloodFillAlgorithm(testMap, x, y, (int) Math.ceil(lowerBound),
						(int) Math.ceil(upperBound));
				Thread thread = new Thread(ff);
				thread.start();
				try {
					thread.join();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				if (ff.getResult()) {
					mountainCount++;
				}
				averageAreaXCoordinates.get(5).add(ff.getAverageXCoordinate());
				averageAreaYCoordinates.get(5).add(ff.getAverageYCoordinate());
			}
		}

		for (int x = 0; x < testMap.length; x++) {
			for (int y = 0; y < testMap.length; y++) {
				double lowerBound = minimum + oceanSpectrum + coastSpectrum + beachSpectrum + grassSpectrum
						+ forestSpectrum + mountainSpectrum;
				double upperBound = minimum + oceanSpectrum + coastSpectrum + beachSpectrum + grassSpectrum
						+ forestSpectrum + mountainSpectrum + snowSpectrum;
				FloodFillAlgorithm ff = new FloodFillAlgorithm(testMap, x, y, (int) Math.ceil(lowerBound),
						(int) Math.ceil(upperBound));
				Thread thread = new Thread(ff);
				thread.start();
				try {
					thread.join();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				if (ff.getResult()) {
					snowCount++;
				}
				averageAreaXCoordinates.get(6).add(ff.getAverageXCoordinate());
				averageAreaYCoordinates.get(6).add(ff.getAverageYCoordinate());
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

	/**
	 * Calculates the average distance of a terrain type to the center of the map
	 * and the border by building the average of the arithmetic mean of all X and the Y coordinates
	 * of the tiles belonging to the same terrain.
	 * 
	 * @return
	 */
	public List<Double> calcAbsolutePositionsAverage() {
		bowesDistanceAvg = new ArrayList<Double>();
		calcSumValues();
		double oceanAvg = 0;
		List<Double> oceanList = bowesDistance.get(0);
		oceanAvg = Math.sqrt(oceanList.get(0) * oceanList.get(1));

		double coastAvg = 0;
		List<Double> coastList = bowesDistance.get(1);
		coastAvg = Math.sqrt(coastList.get(0) * coastList.get(1));

		double beachAvg = 0;
		List<Double> beachList = bowesDistance.get(2);
		beachAvg = Math.sqrt(beachList.get(0) * beachList.get(1));

		double grassAvg = 0;
		List<Double> grassList = bowesDistance.get(3);
		grassAvg = Math.sqrt(grassList.get(0) * grassList.get(1));

		double forestAvg = 0;
		List<Double> forestList = bowesDistance.get(4);
		forestAvg = Math.sqrt(forestList.get(0) * forestList.get(1));

		double mountainAvg = 0;
		List<Double> mountainList = bowesDistance.get(5);
		mountainAvg = Math.sqrt(mountainList.get(0) * mountainList.get(1));

		double snowAvg = 0;
		List<Double> snowList = bowesDistance.get(6);
		snowAvg = Math.sqrt(snowList.get(0) * snowList.get(1));

		bowesDistanceAvg.add(0, oceanAvg);
		bowesDistanceAvg.add(1, coastAvg);
		bowesDistanceAvg.add(2, beachAvg);
		bowesDistanceAvg.add(3, grassAvg);
		bowesDistanceAvg.add(4, forestAvg);
		bowesDistanceAvg.add(5, mountainAvg);
		bowesDistanceAvg.add(6, snowAvg);

		return bowesDistanceAvg;
	}
	
	/**
	 * Calculates the average distance of a terrain type to the center of the map
	 * and the border by building the arithmetic mean of all X and the Y coordinates
	 * of the tiles belonging to the same terrain.
	 * 
	 * @return
	 */
	public HashMap<Integer, List<Double>> calcAbsolutePositions() {
		calcSumValues();
		/*
		List<Double> oceanAvg = new ArrayList<Double>();
		double oceanX = 0;
		double oceanY = 0;
		if ((int) sumValues.get(0).getFirstValue() != 0) {
			oceanX = (int) sumValues.get(0).getSecondValue() / (int) sumValues.get(0).getFirstValue();
			oceanY = (int) sumValues.get(0).getThirdValue() / (int) sumValues.get(0).getFirstValue();
		}
		oceanAvg.add(0, oceanX);
		oceanAvg.add(0, oceanY);

		List<Double> coastAvg = new ArrayList<Double>();
		double coastX = 0;
		double coastY = 0;
		if ((int) sumValues.get(1).getFirstValue() != 0) {
			coastX = (int) sumValues.get(1).getSecondValue() / (int) sumValues.get(1).getFirstValue();
			coastY = (int) sumValues.get(1).getThirdValue() / (int) sumValues.get(1).getFirstValue();
		}
		coastAvg.add(0, coastX);
		coastAvg.add(0, coastY);

		List<Double> beachAvg = new ArrayList<Double>();
		double beachX = 0;
		double beachY = 0;
		if ((int) sumValues.get(2).getFirstValue() != 0) {
			beachX = (int) sumValues.get(2).getSecondValue() / (int) sumValues.get(2).getFirstValue();
			beachY = (int) sumValues.get(2).getThirdValue() / (int) sumValues.get(2).getFirstValue();
		}
		beachAvg.add(0, beachX);
		beachAvg.add(0, beachY);

		List<Double> grassAvg = new ArrayList<Double>();
		double grassX = 0;
		double grassY = 0;
		if ((int) sumValues.get(3).getFirstValue() != 0) {
			grassX = (int) sumValues.get(3).getSecondValue() / (int) sumValues.get(3).getFirstValue();
			grassY = (int) sumValues.get(3).getThirdValue() / (int) sumValues.get(3).getFirstValue();
		}
		grassAvg.add(0, grassX);
		grassAvg.add(0, grassY);

		List<Double> forestAvg = new ArrayList<Double>();
		double forestX = 0;
		double forestY = 0;
		if ((int) sumValues.get(4).getFirstValue() != 0) {
			forestX = (int) sumValues.get(4).getSecondValue() / (int) sumValues.get(4).getFirstValue();
			forestY = (int) sumValues.get(4).getThirdValue() / (int) sumValues.get(4).getFirstValue();
		}
		forestAvg.add(0, forestX);
		forestAvg.add(0, forestY);

		List<Double> mountainAvg = new ArrayList<Double>();
		double mountainX = 0;
		double mountainY = 0;
		if ((int) sumValues.get(5).getFirstValue() != 0) {
			mountainX = (int) sumValues.get(5).getSecondValue() / (int) sumValues.get(5).getFirstValue();
			mountainY = (int) sumValues.get(5).getThirdValue() / (int) sumValues.get(5).getFirstValue();
		}
		mountainAvg.add(0, mountainX);
		mountainAvg.add(0, mountainY);

		List<Double> snowAvg = new ArrayList<Double>();
		double snowX = 0;
		double snowY = 0;
		if ((int) sumValues.get(6).getFirstValue() != 0) {
			snowX = (int) sumValues.get(6).getSecondValue() / (int) sumValues.get(6).getFirstValue();
			snowY = (int) sumValues.get(6).getThirdValue() / (int) sumValues.get(6).getFirstValue();
		}
		snowAvg.add(0, snowX);
		snowAvg.add(0, snowY);

		bowesDistanceAvg.put(0, oceanAvg);
		bowesDistanceAvg.put(1, coastAvg);
		bowesDistanceAvg.put(2, beachAvg);
		bowesDistanceAvg.put(3, grassAvg);
		bowesDistanceAvg.put(4, forestAvg);
		bowesDistanceAvg.put(5, mountainAvg);
		bowesDistanceAvg.put(6, snowAvg);
		*/

		return bowesDistance;
	}

	/**
	 * 
	 * Calculates the sums for X and Y coordinates of each terrain type. Necessary
	 * to calculate the absolute positions.
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
	private HashMap<Integer, Triple<Integer, Integer, Integer>> calcSumValues() {
		HashMap<Integer, Triple<Integer, Integer, Integer>> sumValues = new HashMap<Integer, Triple<Integer, Integer, Integer>>();
		Triple<Integer, Integer, Integer> oceanTuple = new Triple<Integer, Integer, Integer>();
		Triple<Integer, Integer, Integer> coastTuple = new Triple<Integer, Integer, Integer>();
		Triple<Integer, Integer, Integer> beachTuple = new Triple<Integer, Integer, Integer>();
		Triple<Integer, Integer, Integer> grassTuple = new Triple<Integer, Integer, Integer>();
		Triple<Integer, Integer, Integer> forestTuple = new Triple<Integer, Integer, Integer>();
		Triple<Integer, Integer, Integer> mountainTuple = new Triple<Integer, Integer, Integer>();
		Triple<Integer, Integer, Integer> snowTuple = new Triple<Integer, Integer, Integer>();

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

		bowesDistance = new HashMap<Integer, List<Double>>();
		sumValues.put(0, oceanTuple);
		sumValues.put(1, coastTuple);
		sumValues.put(2, beachTuple);
		sumValues.put(3, grassTuple);
		sumValues.put(4, forestTuple);
		sumValues.put(5, mountainTuple);
		sumValues.put(6, snowTuple);
		
		List<Double> oceanAvg = new ArrayList<Double>();
		double oceanX = 0;
		double oceanY = 0;
		if ((int) sumValues.get(0).getFirstValue() != 0) {
			oceanX = (int) sumValues.get(0).getSecondValue() / (int) sumValues.get(0).getFirstValue();
			oceanY = (int) sumValues.get(0).getThirdValue() / (int) sumValues.get(0).getFirstValue();
		}
		oceanAvg.add(0, oceanX);
		oceanAvg.add(1, oceanY);

		List<Double> coastAvg = new ArrayList<Double>();
		double coastX = 0;
		double coastY = 0;
		if ((int) sumValues.get(1).getFirstValue() != 0) {
			coastX = (int) sumValues.get(1).getSecondValue() / (int) sumValues.get(1).getFirstValue();
			coastY = (int) sumValues.get(1).getThirdValue() / (int) sumValues.get(1).getFirstValue();
		}
		coastAvg.add(0, coastX);
		coastAvg.add(1, coastY);

		List<Double> beachAvg = new ArrayList<Double>();
		double beachX = 0;
		double beachY = 0;
		if ((int) sumValues.get(2).getFirstValue() != 0) {
			beachX = (int) sumValues.get(2).getSecondValue() / (int) sumValues.get(2).getFirstValue();
			beachY = (int) sumValues.get(2).getThirdValue() / (int) sumValues.get(2).getFirstValue();
		}
		beachAvg.add(0, beachX);
		beachAvg.add(1, beachY);

		List<Double> grassAvg = new ArrayList<Double>();
		double grassX = 0;
		double grassY = 0;
		if ((int) sumValues.get(3).getFirstValue() != 0) {
			grassX = (int) sumValues.get(3).getSecondValue() / (int) sumValues.get(3).getFirstValue();
			grassY = (int) sumValues.get(3).getThirdValue() / (int) sumValues.get(3).getFirstValue();
		}
		grassAvg.add(0, grassX);
		grassAvg.add(1, grassY);

		List<Double> forestAvg = new ArrayList<Double>();
		double forestX = 0;
		double forestY = 0;
		if ((int) sumValues.get(4).getFirstValue() != 0) {
			forestX = (int) sumValues.get(4).getSecondValue() / (int) sumValues.get(4).getFirstValue();
			forestY = (int) sumValues.get(4).getThirdValue() / (int) sumValues.get(4).getFirstValue();
		}
		forestAvg.add(0, forestX);
		forestAvg.add(1, forestY);

		List<Double> mountainAvg = new ArrayList<Double>();
		double mountainX = 0;
		double mountainY = 0;
		if ((int) sumValues.get(5).getFirstValue() != 0) {
			mountainX = (int) sumValues.get(5).getSecondValue() / (int) sumValues.get(5).getFirstValue();
			mountainY = (int) sumValues.get(5).getThirdValue() / (int) sumValues.get(5).getFirstValue();
		}
		mountainAvg.add(0, mountainX);
		mountainAvg.add(1, mountainY);

		List<Double> snowAvg = new ArrayList<Double>();
		double snowX = 0;
		double snowY = 0;
		if ((int) sumValues.get(6).getFirstValue() != 0) {
			snowX = (int) sumValues.get(6).getSecondValue() / (int) sumValues.get(6).getFirstValue();
			snowY = (int) sumValues.get(6).getThirdValue() / (int) sumValues.get(6).getFirstValue();
		}
		snowAvg.add(0, snowX);
		snowAvg.add(1, snowY);

		bowesDistance.put(0, oceanAvg);
		bowesDistance.put(1, coastAvg);
		bowesDistance.put(2, beachAvg);
		bowesDistance.put(3, grassAvg);
		bowesDistance.put(4, forestAvg);
		bowesDistance.put(5, mountainAvg);
		bowesDistance.put(6, snowAvg);
		
		return sumValues;
	}

	/**
	 * Calculates the distance of each area of a terrain type to other areas of the
	 * same type.
	 * 
	 * @return
	 */
	public List<Double> calcRelativePositions() {
		
		minima = new ArrayList<Double>();
		
		for(int terrain=0; terrain<7;terrain++) {
			List<Integer> deltaX = new ArrayList<Integer>();
			for (int i=0; i<averageAreaXCoordinates.get(terrain).size();i++) {
				for (int j=0; j<averageAreaXCoordinates.get(terrain).size();j++) {
					if(i != j) {
						deltaX.add(averageAreaXCoordinates.get(terrain).get(i)-averageAreaXCoordinates.get(terrain).get(j));
					}
				}
			}
			List<Integer> deltaY = new ArrayList<Integer>();
			for (int i=0; i<averageAreaYCoordinates.get(terrain).size();i++) {
				for (int j=0; j<averageAreaYCoordinates.get(terrain).size();j++) {
					if(i != j) {
						deltaY.add(averageAreaYCoordinates.get(terrain).get(i)-averageAreaYCoordinates.get(terrain).get(j));
					}
				}
			}
			
			int minX = 0;
			int minY = 0;
			if(averageAreaXCoordinates.get(terrain).size()!=0) {
				minX = averageAreaXCoordinates.get(terrain).get(0);
		        for (int i : averageAreaXCoordinates.get(terrain)){
		            minX = minX < i ? minX : i;
		        }
			}
			if(averageAreaYCoordinates.get(terrain).size()!=0) {
		        minY = averageAreaYCoordinates.get(terrain).get(0);
		        for (int i : averageAreaYCoordinates.get(terrain)){
		            minY = minY < i ? minY : i;
		        }
			}
			
			minima.add((double)Math.min(minX, minY));
		}

		return minima;
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

	public double getOceanSpectrum() {
		return oceanSpectrum;
	}

	public double getCoastSpectrum() {
		return coastSpectrum;
	}

	public double getBeachSpectrum() {
		return beachSpectrum;
	}

	public double getGrassSpectrum() {
		return grassSpectrum;
	}

	public double getForestSpectrum() {
		return forestSpectrum;
	}

	public double getMountainSpectrum() {
		return mountainSpectrum;
	}

	public double getSnowSpectrum() {
		return snowSpectrum;
	}

	public int getMinimum() {
		return minimum;
	}

}