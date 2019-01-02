package logic;

/**
 * Implementation of a Cellular Automaton that creates a tile-based map using
 * two rules to assign binary values to each tile. Stores the map as 2D Array
 * and saves integer values for extensibility, so that the generated map can be
 * processed further.
 * <br><br>
 * The initial step is the basic implementation of the algorithm and assigns
 * values of 1 or 0, with 0 being an open area and 1 being a cave wall.
 * 
 * <br><br>
 * Further steps for adding objects are still to be implemented.
 *
 *
 * @see <a href="https://en.wikipedia.org/wiki/Cellular_automaton">Cellular Automaton Wikipedia</a>
 * @author Michael Bowes
 *
 */
public class CellularAutomaton implements ProceduralAlgorithm {

	private String name;
	private int[][] map;
	/**
	 * Chance that a tile is initialized with value 1.
	 */
	private float survivalChance;
	/**
	 * The number of alive neighbors necessary to assign the value 0 to a tile.
	 * With 9 possible neighbors, appropriate values are between 0 and 9. 
	 */
	private int birthRule;
	/**
	 * The number of alive neighbors necessary to assign the value 0 to a tile.
	 * With 9 possible neighbors, appropriate values are between 0 and 9.
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
		this.iterations = iterations;
		this.birthRule = birthRule;
		this.deathRule = deathRule;
		this.survivalChance = survival;
	}

	@Override
	public int[][] generateMap(int size) {
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
				int nbs = checkMooreNeighbors(mapTemplate, x, y);
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
	public int checkMooreNeighbors(int[][] map, int x, int y) {
		int count = 0;
		for (int i = -1; i < 2; i++) {
			for (int j = -1; j < 2; j++) {
				int neighbour_x = x + i;
				int neighbour_y = y + j;

				if ((neighbour_x < 0 || neighbour_y < 0 || neighbour_x >= map.length || neighbour_y >= map[0].length)
						&& !(i == 0 && j == 0)) {
					// Edge case at the map's border
					count += 1;
				} else if ((map[neighbour_x][neighbour_y] == 1) && !(i == 0 && j == 0)) {
					// Standard case.
					count += 1;
				}
			}
		}

		return count;
	}

	@Override
	public String getName() {
		return name;
	}

}
