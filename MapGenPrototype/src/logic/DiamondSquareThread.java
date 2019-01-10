package logic;

import java.util.Random;

/**
 * 
 * Implements the diamond square algorithm.
 * The edge cases at the border are handled by ?
 * @see <a href="https://en.wikipedia.org/wiki/Diamond-square_algorithm">Diamond Square Wikipedia</a>
 * 
 * @author Michael Bowes
 * 
 */
public class DiamondSquareThread implements Runnable {
	
	private int arraySize;
	private int mapArray[][];
	private int noise;
	private Random random;
	private int size;

	/**
	 * Generates a random map by applying several diamond and square steps.
	 * 
	 * @param size
	 *            Size of the map to be generated.
	 */
	public DiamondSquareThread(int size) {
		this.size = size;
	}
	
	@Override
	public void run() {
		random = new Random();
		noise = random.nextInt(20) + 50;
		arraySize = size - 1;
		mapArray = new int[size][size];
		
		setCornerSeeds();
		size -= 1;
		/**
		 * Rounds size down from 1025 to 512. Then iterates over the map and halves the
		 * size with each iteration, until the entire map is filled.
		 */
		for (double x = size / 2; x >= 1; x /= 2) {
			size /= 2; // how far the distance between each point is.

			diamond(size); // diamond Step
			square(size); // square Step
			noise *= .7; // noise is the same for each tile within the same iteration
			// printMap(); remove comment "//" to see mapArray printed
		}
	}

	/**
	 * 
	 * Splits the Array into smaller arrays of the given size, and then assigns the
	 * averaged value of each corner of the array to the corners of the smaller
	 * sub-arrays.
	 * 
	 * @param size
	 *            Size of the array in the current iteration of DiamondSquare.
	 */
	private void diamond(int size) {
		for (int y = size; y < arraySize; y += size * 2) {
			for (int x = size; x < arraySize; x += size * 2) {
				mapArray[y][x] = calculateDiamondAverage(y, x, size);
			}
		}
	}

	/**
	 * Calculates the average out of the 4 corners of the given array and adds a
	 * random noise value to the average for randomness. This is only used in the
	 * diamond step.
	 * 
	 * @param y
	 * @param x
	 * @param size
	 *            Size of the current sub-array.
	 * @return Average of each corner of the given (sub-)array.
	 */
	private int calculateDiamondAverage(int y, int x, int size) {
		int average = ((mapArray[y - size][x - size] + mapArray[y + size][x - size] + mapArray[y - size][x + size]
				+ mapArray[y + size][x + size]) / 4);
		average = average + (random.nextInt(noise*2 + 1) - noise);

		return average;
	}
	
	/**
	 * Creates the squares from the given diamond-midpoint and assigns an average of
	 * their corners to the corners of the squares.
	 * 
	 * @param size
	 *            Size of the current iteration's sub-arrays.
	 */
	private void square(int size) {
		for (int y = size; y < arraySize; y += size * 2) {
			for (int x = size; x < arraySize; x += size * 2) {
				// e.g. at size=64:
				// e.g. 0(),32(mid)
				mapArray[y - size][x] = calculateSquareAverage(y - size, x, size);
				// e.g. 64,32(mid)
				mapArray[y + size][x] = calculateSquareAverage(y + size, x, size);
				// e.g. 32,0(top)
				mapArray[y][x - size] = calculateSquareAverage(y, x - size, size);
				// e.g. 32,64(bottom)
				mapArray[y][x + size] = calculateSquareAverage(y, x + size, size);
			}
		}
	}

	/**
	 * Calculates the average for the square points.
	 */
	private int calculateSquareAverage(int y, int x, int size) {

		int average = ((mapArray[(Math.abs(y - size))][x] + mapArray[(y + size) % arraySize][x]
				+ mapArray[y][(x + size) % arraySize] + mapArray[y][Math.abs(x - size)]) / 4);
		average = average + (random.nextInt(noise*2 + 1) - noise);

		return average;
	}

	/**
	 * Generates the corners of the map at the start of the algorithm.<br>
	 * The corner values can be adjusted here for control over the map generation on
	 * the edges.<br>
	 */
	private void setCornerSeeds() {
		for (int i = 0; i <= arraySize; i += arraySize) {
			for (int j = 0; j <= arraySize; j += arraySize) {
				mapArray[i][j] = random.nextInt(80); // this is the
				// random amount that each corner gets.
				// corners: i,j
				// corner1: 0,0 | top left
				// corner2: 0,1024 | bottom left
				// corner3: 1024,0 | top right
				// corner4: 1024,1024 | bottom right
			}
		}
	}

	/**
	 * 
	 * @return The map as int[][].
	 */
	public int[][] getMap() {
		return mapArray;
	}

}