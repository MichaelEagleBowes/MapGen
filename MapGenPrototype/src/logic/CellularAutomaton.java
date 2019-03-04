package logic;

import java.util.LinkedList;
import java.util.Queue;
import model.Tile;

/**
 * Implementation of a Cellular Automaton that creates a tile-based map using
 * two rules to assign binary values to each tile. Stores the map as 2D Array
 * and saves integer values for extensibility, so that the generated map can be
 * processed further. <br>
 * <br>
 * The initial step is the basic implementation of the algorithm and assigns
 * values of 1 or 0, with 0 being an open area and 1 being a cave wall.
 * 
 * <br>
 * <br>
 * Further steps for adding objects are still to be implemented.
 *
 *
 * @see <a href="https://en.wikipedia.org/wiki/Cellular_automaton">Cellular
 *      Automaton Wikipedia</a>
 * @author Michael Bowes
 *
 */
public class CellularAutomaton implements ProceduralAlgorithm {

	private String name;
	private boolean hasMap;
	private int[][] map;
	private int mapSize;
	/**
	 * Chance that a tile is initialized with value 1.
	 */
	private float survivalChance;
	/**
	 * The number of alive neighbors necessary to assign the value 0 to a tile. With
	 * 9 possible neighbors, appropriate values are between 0 and 9.
	 */
	private int birthRule;
	/**
	 * The number of alive neighbors necessary to assign the value 0 to a tile. With
	 * 9 possible neighbors, appropriate values are between 0 and 9.
	 */
	private int deathRule;
	/**
	 * The number of iterations for the algorithm's initial step.
	 */
	private int iterations;

	public CellularAutomaton() {
		this.name = "CellularAutomata";
	}

	public CellularAutomaton(int iterations, int birthRule, int deathRule, float survival) {
		this.name = "CellularAutomata";
		this.iterations = iterations;
		this.birthRule = birthRule;
		this.deathRule = deathRule;
		this.survivalChance = survival;
	}

	@Override
	public int[][] generateMap(int size) {
		mapSize = size;
		map = new int[size][size];

		for (int i = 0; i < size; i++) {
			for (int j = 0; j < size; j++) {
				if (Math.random() < survivalChance) {
					map[j][i] = 1;
				} else {
					map[j][i] = 0;
				}
			}
		}

		for (int i = 0; i < iterations; i++) {
			map = iterate(map);
		}

		hasMap = true;
		// map = applyObjects(map);

		return map;
	}

	/**
	 * Iterates through the randomly generated map and changes cell values based on
	 * two rules: The birthRule revives dead cells if enough neighbors exist. The
	 * deathRule kills off cells if too many neighbors are present.
	 * 
	 * @param mapTemplate
	 * @return
	 */
	private int[][] iterate(int[][] mapTemplate) {

		int[][] newMap = new int[mapTemplate.length][mapTemplate.length];

		for (int x = 0; x < mapTemplate.length; x++) {
			for (int y = 0; y < mapTemplate[0].length; y++) {
				int nbs = checkMooreNeighbors(mapTemplate, x, y, 0, 1);
				if (mapTemplate[x][y] == 1) {
					if (nbs < deathRule) {
						newMap[x][y] = 0;
					} else {
						newMap[x][y] = 1;
					}
				} else {
					if (nbs > birthRule) {
						newMap[x][y] = 1;
					} else {
						newMap[x][y] = 0;
					}
				}
			}
		}

		return newMap;

	}

	/**
	 * 
	 * Calculates the number of Moore neighbors for a tile in the grid. For the edge
	 * cases at the border where neighbor tiles are missing, the algorithm assumes
	 * these missing tiles to have alive neighbors.
	 * 
	 * @param map
	 * @param x
	 * @param y
	 * @return
	 */
	public int checkMooreNeighbors(int[][] map, int x, int y, int range, int terrain) {
		int count = 0;
		for (int i = -1 - range; i < 2 + range; i++) {
			for (int j = -1 - range; j < 2 + range; j++) {
				int neighbour_x = x + i;
				int neighbour_y = y + j;
				if ((neighbour_x < 0 || neighbour_y < 0 || neighbour_x >= map.length || neighbour_y >= map[0].length)
						&& !(i == 0 && j == 0)) {
					// Edge case at the map's border
					count += 1;
				} else if ((map[neighbour_x][neighbour_y] == terrain) && !(i == 0 && j == 0)) {
					// Standard case.
					count += 1;
				}
			}
		}

		return count;
	}

	/**
	 * Implementation of the Flood Fill algorithm with a queue, to prevent potential
	 * StackOverflowErrors for larger map sizes.
	 * 
	 * @param testMap Map to flood
	 * @param x X-coordinate of the current tile
	 * @param y Y-coordinate of the current tile
	 */
	public static boolean floodQueue(int[][] testMap, int x, int y) {
		boolean count = false;
		boolean echo1 = false;
		boolean echo2 = false;
		boolean echo3 = false;
		boolean echo4 = false;
		Queue<Tile> queue = new LinkedList<Tile>();
		if (testMap[x][y] == 1) {
			return false;
		} else {
			queue.add(new Tile(x, y, testMap[x][y]));

			while (!queue.isEmpty()) {
				Tile t = queue.remove();
				if (testMap[t.x][t.y] < 1) {
					testMap[t.x][t.y] = 1;
					count = true;
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
				}
			}
			return (count || echo1 || echo2 || echo3 || echo4);
		}
	}
	

	/**
	 * Uses the Flood Fill Algorithm to calculate the number of separated areas in a
	 * map. Ranges between 0, with only wall segments, and half the square of the map's size, if a lattice grid
	 * of open space is created, i.e. every second tile is a wall segment.
	 */
	public double calculateNumberOfAreas() {
		int[][] testMap = new int[map.length][map.length];
		for (int i = 0; i < testMap.length; i++) {
			for (int j = 0; j < testMap.length; j++) {
				testMap[i][j] = map[i][j];
			}
		}

		double areaCount = 0;

		for (int x = 0; x < testMap.length; x++) {
			for (int y = 0; y < testMap.length; y++) {
				if (floodQueue(testMap, x, y)) {
					areaCount++;
				}
				;
			}
		}

		return areaCount;
	}

	/**
	 * Calculates the number of relative open space compared to wall segments.
	 * Ranges between 0, if no spaceCount, and the square of the map's size, with low
	 * wallCount.
	 */
	public double calculateRelativeOpenSpace() {
		double spaceCount = 0;
		double wallCount = 0;

		for (int i = 0; i < mapSize; i++) {
			for (int j = 0; j < mapSize; j++) {
				if (map[i][j] == 0) {
					spaceCount++;
				}
				if (map[i][j] == 1) {
					wallCount++;
				}
			}
		}
		double space;
		if(wallCount == 0) {
			space = spaceCount;
		} else {
			space = spaceCount / wallCount;
		}
		return Math.round(space * 1000.0) / 1000.0;
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
	
	public void setMap(int[][] map) {
		this.map = map;
		this.hasMap = true;
	}

	public float getSurvivalChance() {
		return survivalChance;
	}

	public void setSurvivalChance(float survivalChance) {
		this.survivalChance = survivalChance;
	}

	public int getBirthRule() {
		return birthRule;
	}

	public void setBirthRule(int birthRule) {
		this.birthRule = birthRule;
	}

	public int getDeathRule() {
		return deathRule;
	}

	public void setDeathRule(int deathRule) {
		this.deathRule = deathRule;
	}

	public int getIterations() {
		return iterations;
	}

	public void setIterations(int iterations) {
		this.iterations = iterations;
	}

}
