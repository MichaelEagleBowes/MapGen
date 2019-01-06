package logic;

public class FloodFillAlgorithm implements Runnable {

	int[][] testMap;
	int x;
	int y;
	int threshold;
	boolean result;
	int lowerBound;
	int upperBound;
	boolean mode = false;
	
	public FloodFillAlgorithm(int[][] testMap, int x, int y, int threshold) {
		this.testMap = testMap;
		this.x = x;
		this.y = y;
		this.threshold = threshold;
	}
	
	public FloodFillAlgorithm(int[][] testMap, int x, int y, int lowerBound, int upperBound) {
		mode = true;
		this.testMap = testMap;
		this.x = x;
		this.y = y;
		this.upperBound = upperBound;
		this.lowerBound = lowerBound;
	}
	
	@Override
	public void run() {
		if(mode) {
			result = floodLand(testMap, x, y, lowerBound, upperBound);
		} else {
			result = floodOcean(testMap, x, y, threshold);
		}
	}
	
	public boolean getResult() {
		return result;
	}

	/**
	 * Applies the flood fill algorithm to the terrain type with the minimum height values of the height map,
	 * to find out the number of areas of this terrain type.
	 * @param testMap
	 * @param x
	 * @param y
	 * @param threshold
	 * @return
	 */
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

				return (count || ff1.getResult() || ff2.getResult() || ff3.getResult() || ff4.getResult());
			}
		}
	}
	
	/**
	 * Applies the flood fill algorithm to all terrain types that do not contain minimum or maximum height values,
	 * to find out the number of areas of the specific terrain type.
	 * @param testMap
	 * @param x
	 * @param y
	 * @param lowerBound
	 * @param upperBound
	 * @return
	 */
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

}
