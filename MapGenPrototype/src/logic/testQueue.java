package logic;

import java.util.LinkedList;
import java.util.Queue;

import model.Tile;

public class testQueue {

	static int[][] map;
	static int mapSize;
	static double survivalChance = 0.6;
	static int birthRule = 5;
	static int deathRule = 4;
	static int iterations = 30;
	
	public static void main(String[] args) {
		map = new int[129][129];
		map = generateMap(129);
		//System.out.println(calcNumberOfAreas(map));
		
		//1 area
		int[][] tm = {
				{0,0,0},
				{0,0,0},
				{0,0,0}
		};
		//2 areas
		int[][] tm2 = {
				{0,1,0},
				{0,1,0},
				{0,1,0}
		};
		// 9 areas
		int[][] tm3 = {
				{0,0,1,0,0,1,0,0,0},
				{1,1,1,1,1,1,1,1,1},
				{0,1,0,0,0,1,0,0,0},
				{0,1,0,0,0,1,1,1,1},
				{1,1,1,1,1,0,0,0,0},
				{0,1,0,0,1,0,1,1,0},
				{0,1,0,0,1,0,0,1,0},
				{0,1,0,0,1,1,1,1,0},
				{0,1,0,0,1,0,0,0,0}
		};
		//System.out.println(calcNumberOfAreas(tm));
		//System.out.println(calcNumberOfAreas(tm2));
		System.out.println(calcNumberOfAreas(tm3));
	}
	
	public static double calcNumberOfAreas(int[][] map) {
		int[][] testMap = new int[map.length][map.length];
		for(int i=0;i<testMap.length;i++) {
			for(int j=0;j<testMap.length;j++) {
			testMap[i][j] = map[i][j];
			}
		}
				
		double areaCount = 0;
		

		System.out.println("len: "+testMap.length +" x "+testMap[0].length);
	    for(int x = 0; x<testMap.length; x++){
	        for(int y = 0; y<testMap.length; y++){
	            if(fillArea(testMap,x,y, 1)) {
	            	areaCount++;
	            };
	        }
	    }
		
		return areaCount;
	}
	
	public static boolean fillArea(int[][] testMap, int x, int y, int threshold) {
		boolean count = false;
		boolean echo1 = false;
		boolean echo2 = false;
		boolean echo3 = false;
		boolean echo4 = false;
			Queue<Tile> queue = new LinkedList<Tile>();
			if (testMap[x][y] >= threshold) {
				System.out.println(false);
				return false;
			} else {
				queue.add(new Tile(x, y, testMap[x][y]));
				
				while (!queue.isEmpty()) {
					Tile p = queue.remove();
					if (testMap[p.x][p.y] < threshold) {
						testMap[p.x][p.y] = threshold;
						count = true;
						if(p.x > 0) {
							echo1 = queue.add(new Tile(p.x - 1, p.y, testMap[p.x - 1][p.y]));
						}
						if(p.y > 0) {
							echo3 = queue.add(new Tile(p.x, p.y - 1, testMap[p.x][p.y - 1]));
						}
						if(p.x < testMap.length-1) {
							echo2 = queue.add(new Tile(p.x + 1, p.y, testMap[p.x + 1][p.y]));
						}
						if(p.y < testMap.length-1) {
							echo4 = queue.add(new Tile(p.x, p.y + 1, testMap[p.x][p.y + 1]));
						}
					}
				}
				System.out.println(count + " " + echo1 + " " + echo2 + " " + echo3 + " " + echo4);
				return (count || echo1 || echo2 || echo3 || echo4);
				}
	}
	
	public static int[][] generateMap(int size) {
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
	private static int[][] iterate(int[][] mapTemplate) {

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
	
	public static int checkMooreNeighbors(int[][] map, int x, int y, int range, int terrain) {
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
	
}
