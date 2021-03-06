package logic;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import model.Tile;

public class FloodFillAlgorithm implements Runnable {

	int[][] testMap;
	int x;
	int y;
	int threshold;
	boolean result;
	int lowerBound;
	int upperBound;
	boolean mode = false;
	static int tileCount;
	static List<Integer> xCoordinates;
	static List<Integer> yCoordinates;
	static int avgXCoordinate;
	static int avgYCoordinate;

	public FloodFillAlgorithm(int[][] testMap, int x, int y, int threshold) {
		this.testMap = testMap;
		this.x = x;
		this.y = y;
		this.threshold = threshold;
		xCoordinates = new ArrayList<Integer>();
		yCoordinates = new ArrayList<Integer>();
	}

	public FloodFillAlgorithm(int[][] testMap, int x, int y, int lowerBound, int upperBound) {
		mode = true;
		this.testMap = testMap;
		this.x = x;
		this.y = y;
		this.upperBound = upperBound;
		this.lowerBound = lowerBound;
		xCoordinates = new ArrayList<Integer>();
		yCoordinates = new ArrayList<Integer>();
	}

	@Override
	public void run() {
		if (mode) {
			result = floodLandQueue(testMap, x, y, lowerBound, upperBound);
		} else {
			result = floodOceanQueue(testMap, x, y, threshold);
		}
	}

	/**
	 * Queue implementation of the flood fill algorithm, to fix the
	 * StackOverflowError.
	 * 
	 * @param testMap
	 * @param x
	 * @param y
	 * @param threshold
	 * @param fill
	 */
	public static boolean floodOceanQueue(int[][] testMap, int x, int y, int threshold) {
		boolean count = false;
		boolean echo1 = false;
		boolean echo2 = false;
		boolean echo3 = false;
		boolean echo4 = false;
		Queue<Tile> queue = new LinkedList<Tile>();
		if (testMap[x][y] >= threshold) {
			return false;
		} else {
			queue.add(new Tile(x, y, testMap[x][y]));

			while (!queue.isEmpty()) {
				Tile t = queue.remove();
				if (testMap[t.x][t.y] < threshold) {
					testMap[t.x][t.y] = threshold;
					count = true;
					xCoordinates.add(x);
					yCoordinates.add(y);
					tileCount++;
					if (t.x > 0) {
						echo1 = queue.add(new Tile(t.x - 1, t.y, testMap[t.x - 1][t.y]));
					}
					if (t.y > 0) {
						echo3 = queue.add(new Tile(t.x, t.y - 1, testMap[t.x][t.y - 1]));
					}
					if (t.x < testMap.length - 1) {
						echo2 = queue.add(new Tile(t.x + 1, t.y, testMap[t.x + 1][t.y]));
					}
					if (t.y < testMap.length - 1) {
						echo4 = queue.add(new Tile(t.x, t.y + 1, testMap[t.x][t.y + 1]));
					}
					for (int coord : xCoordinates) {
						avgXCoordinate += coord;
					}
					avgXCoordinate /= tileCount;

					for (int coord : yCoordinates) {
						avgYCoordinate += coord;
					}
					avgYCoordinate /= tileCount;
				}
			}
			return (count || echo1 || echo2 || echo3 || echo4);
		}
	}

	public static boolean floodLandQueue(int[][] testMap, int x, int y, int lowerBound, int upperBound) {
		boolean count = false;
		boolean echo1 = false;
		boolean echo2 = false;
		boolean echo3 = false;
		boolean echo4 = false;
		Queue<Tile> queue = new LinkedList<Tile>();
		if (testMap[x][y] < lowerBound || testMap[x][y] >= upperBound) {
			return false;
		} else {
			queue.add(new Tile(x, y, testMap[x][y]));

			while (!queue.isEmpty()) {
				Tile t = queue.remove();
				if (testMap[t.x][t.y] < upperBound && testMap[t.x][t.y] >= lowerBound) {
					testMap[t.x][t.y] = upperBound;
					count = true;
					xCoordinates.add(x);
					yCoordinates.add(y);
					tileCount++;
					if (t.x > 0) {
						echo1 = queue.add(new Tile(t.x - 1, t.y, testMap[t.x - 1][t.y]));
					}
					if (t.y > 0) {
						echo3 = queue.add(new Tile(t.x, t.y - 1, testMap[t.x][t.y - 1]));
					}
					if (t.x < testMap.length - 1) {
						echo2 = queue.add(new Tile(t.x + 1, t.y, testMap[t.x + 1][t.y]));
					}
					if (t.y < testMap.length - 1) {
						echo4 = queue.add(new Tile(t.x, t.y + 1, testMap[t.x][t.y + 1]));
					}
					for (int coord : xCoordinates) {
						avgXCoordinate += coord;
					}
					avgXCoordinate /= tileCount;

					for (int coord : yCoordinates) {
						avgYCoordinate += coord;
					}
					avgYCoordinate /= tileCount;
				}
			}
			return (count || echo1 || echo2 || echo3 || echo4);
		}
	}

	/**
	 * Applies the flood fill algorithm to the terrain type with the minimum height
	 * values of the height map, to find out the number of areas of this terrain
	 * type.
	 * 
	 * @param testMap
	 * @param x
	 * @param y
	 * @param threshold
	 * @return
	 */
	@Deprecated
	public static boolean floodOcean(int[][] testMap, int x, int y, int threshold) {
		boolean count = false;
		if (x < 0 || y < 0 || x >= testMap.length || y >= testMap.length) {
			return false;
		} else {
			if (testMap[x][y] >= threshold) {
				return false;
			} else {
				testMap[x][y] = threshold;
				count = true;
				xCoordinates.add(x);
				yCoordinates.add(y);
				tileCount++;
				FloodFillAlgorithm ff1 = new FloodFillAlgorithm(testMap, x - 1, y, threshold);
				FloodFillAlgorithm ff2 = new FloodFillAlgorithm(testMap, x + 1, y, threshold);
				FloodFillAlgorithm ff3 = new FloodFillAlgorithm(testMap, x, y - 1, threshold);
				FloodFillAlgorithm ff4 = new FloodFillAlgorithm(testMap, x, y + 1, threshold);
				Thread thread = new Thread(ff1);
				Thread thread2 = new Thread(ff2);
				Thread thread3 = new Thread(ff3);
				Thread thread4 = new Thread(ff4);
				thread.start();
				thread2.start();
				thread3.start();
				thread4.start();
				try {
					thread.join();
					thread2.join();
					thread3.join();
					thread4.join();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}

				for (int coord : xCoordinates) {
					avgXCoordinate += coord;
				}
				avgXCoordinate /= tileCount;

				for (int coord : yCoordinates) {
					avgYCoordinate += coord;
				}
				avgYCoordinate /= tileCount;

				return (count || ff1.getResult() || ff2.getResult() || ff3.getResult() || ff4.getResult());
			}
		}
	}

	/**
	 * Applies the flood fill algorithm to all terrain types that do not contain
	 * minimum or maximum height values, to find out the number of areas of the
	 * specific terrain type.
	 * 
	 * @param testMap
	 * @param x
	 * @param y
	 * @param lowerBound
	 * @param upperBound
	 * @return
	 */
	@Deprecated
	public static boolean floodLand(int[][] testMap, int x, int y, int lowerBound, int upperBound) {
		boolean count = false;
		if (x < 0 || y < 0 || x >= testMap.length || y >= testMap.length) {
			return false;
		} else {
			if (testMap[x][y] < lowerBound || testMap[x][y] >= upperBound) {
				return false;
			} else {
				testMap[x][y] = upperBound;
				count = true;
				boolean echo1 = floodLand(testMap, x - 1, y, lowerBound, upperBound);
				boolean echo2 = floodLand(testMap, x + 1, y, lowerBound, upperBound);
				boolean echo3 = floodLand(testMap, x, y - 1, lowerBound, upperBound);
				boolean echo4 = floodLand(testMap, x, y + 1, lowerBound, upperBound);

				return (count || echo1 || echo2 || echo3 || echo4);
			}
		}
	}

	public boolean getResult() {
		return result;
	}

	public int getAverageXCoordinate() {
		return avgXCoordinate;
	}

	public int getAverageYCoordinate() {
		return avgYCoordinate;
	}

}
