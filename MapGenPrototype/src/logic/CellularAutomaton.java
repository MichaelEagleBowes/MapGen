package logic;

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
	private static int[][] map;
	private static int mapSize;
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
	 */
	private int[][] applyObjects(int[][] mapTemplate) {
		int[][] newMap = new int[mapTemplate.length][mapTemplate.length];
		int treeCount = 6;
		int objectIterations = 20;

		for (int x = 0; x < mapTemplate.length; x++) {
			for (int y = 0; y < mapTemplate[0].length; y++) {
				int nbs2 = checkExtendedMooreNeighbors(mapTemplate, x, y, 1);
				int nbs1 = checkMooreNeighbors(mapTemplate, x, y, 0, 1);
				if (mapTemplate[x][y] == 1) {
					if (nbs2 < treeCount) {
						newMap[x][y] = 2;
					} else if (nbs1 < 2) {
						newMap[x][y] = 3;
					} else {
						newMap[x][y] = 1;
					}
				}
			}
		}

		int[][] filledMap = newMap;
		for (int i = 0; i < objectIterations; i++) {
			filledMap = refineObjects(filledMap);
		}

		for (int x = 0; x < filledMap.length; x++) {
			for (int y = 0; y < filledMap[0].length; y++) {
				int nbs1 = checkObjectNeighbors(filledMap, x, y);
				if (filledMap[x][y] == 1 && nbs1 >= 2) {
					filledMap[x][y] = 2;
				}
			}
		}

		return filledMap;
	}

	private int[][] refineObjects(int[][] newMap) {
		int[][] filledMap = new int[newMap.length][newMap.length];

		for (int x = 0; x < newMap.length; x++) {
			for (int y = 0; y < newMap[0].length; y++) {
				int nbs1 = checkMooreNeighbors(newMap, x, y, 0, 2);
				int nbs2 = checkMooreNeighbors(newMap, x, y, 1, 2);
				if ((newMap[x][y] == 2)) {
					if ((nbs1 < 3) && nbs2 < 3) {
						filledMap[x][y] = 1;
					} else {
						filledMap[x][y] = 2;
					}
				}
				if (newMap[x][y] == 1) {
					filledMap[x][y] = 1;
				}
				if (newMap[x][y] == 0) {
					filledMap[x][y] = 0;
				}
				if (newMap[x][y] == 3) {
					filledMap[x][y] = 3;
				}
			}
		}

		return filledMap;
	}

	private int checkObjectNeighbors(int[][] map, int x, int y) {
		int count = 0;
		for (int i = -1; i < 2; i++) {
			for (int j = -1; j < 2; j++) {
				int neighbour_x = x + i;
				int neighbour_y = y + j;
				if ((neighbour_x < 0 || neighbour_y < 0 || neighbour_x >= map.length || neighbour_y >= map[0].length)) {

				} else if ((map[neighbour_x][neighbour_y] == 2) && !(i == 0 && j == 0)) {
					count += 1;
				}
			}
		}

		return count;
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
	 * 
	 * Calculates the number of Moore neighbors and their Moore neighbors for a tile
	 * in the grid. For the edge cases at the border where neighbor tiles are
	 * missing, the algorithm assumes these missing tiles to have alive neighbors.
	 * 
	 * @param map
	 * @param x
	 * @param y
	 * @return
	 */
	public int checkExtendedMooreNeighbors(int[][] map, int x, int y, int range) {
		int count = 0;
		for (int i = -1 - range; i < 2 + range; i++) {
			for (int j = -1 - range; j < 2 + range; j++) {
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

	/**
	 * Uses the Flood Fill Algorithm to calculate the number of separated areas in a
	 * map.
	 */
	public static double calcNumberOfAreas() {
		int[][] testMap = new int[map.length][map.length];
		for(int i=0;i<testMap.length;i++) {
			for(int j=0;j<testMap.length;j++) {
			testMap[i][j] = map[i][j];
			}
		}
				
		double areaCount = 0;
		
	    for(int x = 0; x<testMap.length; x++){
	        for(int y = 0; y<testMap.length; y++){
	            if(floodfill(testMap, x,y)) {
	            	areaCount++;
	            };
	        }
	    }
		
		return areaCount;
	}
	
	public static boolean floodfill(int[][] testMap, int x, int y) {
		boolean count = false;
		if(x < 0 || y < 0 || x >= testMap.length || y >= testMap.length) {
			return count;
		} else {
			if (testMap[x][y] == 0) {
				testMap[x][y] = 1;
				count = true;
				boolean echo1 = floodfill(testMap, x - 1, y);
				boolean echo2 = floodfill(testMap, x + 1, y);
				boolean echo3 = floodfill(testMap, x, y - 1);
				boolean echo4 = floodfill(testMap, x, y + 1);
				
				return (count || echo1 || echo2 || echo3 || echo4);
			} else {
				return count;
			}
		}
		
	}

	/**
	 * Calculates the number of relative open space compared to wall segments.
	 * Ranges between 0, if no spaceCount, and the size of the map, with low
	 * wallCount.
	 */
	public static double calcRelativeOpenSpace() {
		int spaceCount = 0;
		int wallCount = 0;

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
		System.out.println(" Wallcount "+wallCount + " spaceC "+spaceCount);

		double space = spaceCount / wallCount;
		return space;
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
