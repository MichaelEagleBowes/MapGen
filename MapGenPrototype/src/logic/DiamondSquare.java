package logic;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import javax.imageio.ImageIO;

import model.Map;

/**
 * 
 * Generates, loads, saves, fuses maps and creates images from maps created by
 * the {@link DiamondSquareAlgorithm3}
 * 
 */

public class DiamondSquare implements ProceduralAlgorithm {

	String name;
	int mapSize;
	int mapCount = 0;
	int[][] map;

	/**
	 * Default constructor.
	 */
	public DiamondSquare() {
		this.name = "DiamondSquare";
	}

	/**
	 * Constructor lets you choose the size of the map to be generated.
	 * 
	 * @param size
	 *            The square dimension for the map, e.g. 4 generates a map of size
	 *            (2^4+1)*(2^4+1)<br>
	 *            size needs to always take a value of 2^x + 1.
	 */
	public DiamondSquare(int size) {
		this.map = generateMap(size);
	}

	/**
	 * 
	 * Generates a single square map with the given size and name.
	 * 
	 * @param size
	 *            The square dimension for the map, e.g. 4 generates a map of size
	 *            4*4<br>
	 *            size needs to always take a value of 2^x + 1.
	 * @return
	 */
	@Override
	public int[][] generateMap(int size) {
		// TODO: Durchlaufe das 2D Array und ändere alle Zahlen
		// im Bereich des Grass-Gebiets(von X bis Y) ab.

		// Creates a raw map without drawing it.
		DiamondSquareImpl algo = new DiamondSquareImpl(size);
		Thread t = new Thread(algo);
		t.start();
		try {
			t.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		map = algo.getMap();
		mapCount += 1;
		/*
		 * System.out.println("generatedMap: "); // Prints the map in the console for
		 * (int i = 0; i < map.length; i++) { for (int j = 0; j < map[i].length; j++) {
		 * if (j == map[i].length - 1) { System.out.println(" [" + map[i][j] + "] "); }
		 * else { System.out.print(" [" + map[i][j] + "] "); } } }
		 */
		return map;
	}
	
	/**
	 * 
	 * 
	 * 
	 * @param northMap
	 * @param southMap
	 * @param name
	 * @return
	 */
	public int[][] fuseVertical(int[][] northMap, int[][] southMap, String name) {
		int fuseLength = northMap.length + southMap.length;
		int[][] fuseMap = new int[fuseLength][northMap.length];
		
		
		int northLength = 0;
		for (int i = 0; i < fuseMap[0].length; i++) {
			for (int j = 0; j < fuseMap.length / 2; j++) {
				fuseMap[j][i] = northMap[northLength][northLength];
			}
			northLength++;
		}

		int southWidth = 0;
		for (int i = 0; i < fuseMap[0].length; i++) {
			int southLength = 0;
			for (int j = fuseMap.length / 2; j < fuseMap.length; j++) {
				fuseMap[j][i] = southMap[southWidth][southLength];
				southLength++;
			}
			southWidth++;
		}

		for (int i = 0; i < fuseMap[0].length; i++) {
			for (int j = 0; j < fuseMap.length; j++) {
				System.out.print(fuseMap[j][i] + "\t");
			}
			System.out.println("");
		}

		return fuseMap;
	}

	/**
	 * Fuses 2 maps horizontally.
	 *
	 * @param eastMap
	 * @param westMap
	 * @param name
	 * @return
	 */
	public int[][] fuseHorizontal(int[][] eastMap, int[][] westMap, String name) {
		int fuseLength = eastMap.length + westMap.length;
		int[][] fuseMap = new int[eastMap.length][fuseLength];

		for (int i = 0; i < fuseMap[0].length / 2; i++) {
			for (int j = 0; j < fuseMap.length; j++) {
				fuseMap[j][i] = eastMap[j][i];
			}

		}

		int eastWidth = 0;
		for (int i = fuseMap[0].length / 2; i < fuseMap[0].length; i++) {
			int eastLength = 0;
			for (int j = 0; j < fuseMap.length; j++) {
				fuseMap[j][i] = westMap[eastLength][eastWidth];
				eastLength++;
			}
			eastWidth++;
		}

		/*
		 * System.out.println(fuseMap[0].length); System.out.println(fuseMap.length);
		 * for (int i = 0; i < fuseMap[0].length; i++) { for (int j = 0; j <
		 * fuseMap.length; j++) { System.out.print(fuseMap[j][i] + "\t"); }
		 * System.out.println(""); }
		 */

		return fuseMap;
	}

	/**
	 * Creates a .png file of the generated map. Requires a map to be generated with
	 * generateMap() beforehand. Each map tile is 20*20 in size in the picture. If
	 * the generated map is bigger than the picture, the edges will be cut off in
	 * the image.
	 * 
	 * @param map
	 *            the 2D integer array representing the map.
	 * @param width
	 *            Width of the picture. Optimum value: 10.000; Maximum value: 18798
	 * @param height
	 *            Height of the picture. Optimum value: 10.000; Maximum value: 18798
	 */
	public void createPng(int[][] map, int width, int height, String name) {

		try {
			// TYPE_INT_ARGB specifies the image format: 8-bit RGBA packed
			// into integer pixels
			BufferedImage bi = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
			BufferedImage snow = null;
			BufferedImage green = null;
			BufferedImage darkgreen = null;
			BufferedImage w = null;
			BufferedImage dw = null;
			BufferedImage mountain = null;
			BufferedImage beach = null;

			snow = ImageIO.read(new File("res/pictures/snow.jpg"));
			green = ImageIO.read(new File("res/pictures/green.jpg"));
			darkgreen = ImageIO.read(new File("res/pictures/darkgreen.jpg"));
			w = ImageIO.read(new File("res/pictures/water.jpg"));
			dw = ImageIO.read(new File("res/pictures/deepWater.jpg"));
			mountain = ImageIO.read(new File("res/pictures/mountain.jpg"));
			beach = ImageIO.read(new File("res/pictures/beach.jpg"));

			Graphics2D ig2 = bi.createGraphics();

			// map[0].length necessary for non-square maps.

			int mapHeight = 0;
			int mapWidth = 0;
			if (map[0].length > map.length) {
				System.out.println("l1");
				mapHeight = map.length;
				mapWidth = map[0].length;
			} else if (map[0].length < map.length) {
				System.out.println("l2");
				mapHeight = map.length;
				mapWidth = map[0].length;
			} else {
				System.out.println("l3");
				mapHeight = map.length;
				mapWidth = map.length;
			}
			System.out.println("picL: " + mapHeight);
			System.out.println("picW: " + mapWidth);
			for (int i = 0; i < mapWidth; i++) {
				for (int j = 0; j < mapHeight; j++) {
					if (map[j][i] < -5) // 15% chance to turn BLUE = deep water
					{
						ig2.drawImage(dw, i * 32, j * 32, null);
					}

					else if (map[j][i] < 10) // 10% chance for shallow water
					{
						ig2.drawImage(w, i * 32, j * 32, null);
					}

					else if (map[j][i] < 15) // 15% chance for beach
					{
						ig2.drawImage(beach, i * 32, j * 32, null);
					}

					else if (map[j][i] < 25) // 15% chance for grass
					{
						ig2.drawImage(green, i * 32, j * 32, null);
					}

					else if (map[j][i] < 40) // 20% chance for forest
					{
						ig2.drawImage(darkgreen, i * 32, j * 32, null);
					}

					else if (map[j][i] < 60) // 20% chance for mountain
					{
						ig2.drawImage(mountain, i * 32, j * 32, null);
					} else // 20% chance for snow
					{
						ig2.drawImage(snow, i * 32, j * 32, null);
					}
				}
			}

			ig2.dispose();

			ImageIO.write(bi, "PNG", new File(System.getProperty("user.home") + "/Desktop" + File.separator + name));
		} catch (IOException ie) {
			ie.printStackTrace();
		}
	}

	
	
	/**
	 * 
	 * Takes a generated map and saves each tile as byte.
	 * 
	 * @param map
	 *            The 2D map to save.
	 */
	public void saveMap(int[][] map, Path mapPath) {
		File file = new File("res/Maps/map");
		try (FileOutputStream fop = new FileOutputStream(file)) {

			// if file doesnt exists, then create it
			if (!file.exists()) {
				file.createNewFile();
			}

			for (int i = 0; i < map.length; i++) {
				for (int j = 0; j < map[i].length; j++) {
					byte b = 0;
					String s = Integer.toString(map[i][j]);
					b = Byte.parseByte(s);
					System.out.println(b);
					fop.write(b);

				}
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	/**
	 * 
	 * Takes a generated map and saves it in csv format.
	 * Mainly needed to view and fuse maps.
	 * 
	 * @param map
	 *            The 2D map to save.
	 */
	public void saveMapCSV(int[][] map, Path mapPath) {
		try (BufferedWriter writer = Files.newBufferedWriter(mapPath, Charset.forName("UTF-8"))) {
			for (int i = 0; i < map.length; i++) {
				for (int j = 0; j < map[i].length; j++) {
					if (j == map[i].length - 1) {
						writer.write(map[i][j] + "," + System.getProperty("line.separator"));
					} else {
						writer.write(map[i][j] + ",");
					}
				}
			}
		} catch (IOException e) {
			System.out.println("Could not save map file");
		}
	}

	/**
	 * 
	 * Loads the world map from within a file and saves it as byte array.
	 * 
	 * @param loadPath
	 * @return
	 * @throws IOException
	 */
	public byte[] loadMap(String loadPath, String filename, int mapSize) throws IOException {

		File file = new File("res/Maps/map");

		try (FileInputStream fis = new FileInputStream(file)) {
			ByteArrayOutputStream buffer = new ByteArrayOutputStream();
			
			int nRead;
			byte[] data = new byte[1089];

			while ((nRead = fis.read(data, 0, data.length)) != -1) {
			  buffer.write(data, 0, nRead);
			}

			buffer.flush();

			return buffer.toByteArray();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	/**
	 * Loads a map from a .csv file and returns it as 2D int array.
	 * @param loadPath
	 * @param filename
	 * @param mapSize
	 * @return
	 * @throws IOException
	 */
	public int[][] loadMapCSV(String loadPath, String filename, int mapSize) throws IOException {

		int[][] map = new int[mapSize][mapSize];
		Path folder = Paths.get(loadPath);
		try (DirectoryStream<Path> stream = Files.newDirectoryStream(folder)) {
			for (Path path : stream) {
				if (path.getFileName().toString().endsWith(filename)) {
					List<String> lines = Files.readAllLines(path, StandardCharsets.UTF_8);
					int i = 0;
					for (String line : lines) {
						String[] line2 = line.split(",");
						for (int j = 0; j < line2.length; j++) {
							map[i][j] = Integer.parseInt(line2[j]);
						}
						i++;
					}

				}
			}

		} catch (IOException e) {
			e.printStackTrace();
		}

		return map;
	}

	/**
	 * For testing random access of byte arrays.
	 * @param relativePath
	 */
	public void readMap2(String relativePath) {
		try {
			File file = new File("res/Maps/map");
			RandomAccessFile raf = new RandomAccessFile(file, "rw");

			raf.seek(32);
			byte[] byteArray = new byte[1089];
			byteArray[0] = raf.readByte();
			System.out.println(byteArray[0]);

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Returns the part of the map surrounding an object within the specified
	 * parameters. Current recommended params: 60x40 (6, 4, 25, 0)
	 * @param playerRadiusX The tiles the player is looking into X direction on both sides
	 * @param playerRadiusY The tiles the player is looking into Y direction on both sides
	 * @param playerX The X position of the player in map-tile-coordinates
	 * @param playerY The Y position of the player in map-tile-coordinates
	 */
	public void getMapPart(int playerRadiusX, int playerRadiusY, int playerX, int playerY) {
		File file = new File("res/Maps/map");
		try (RandomAccessFile raf = new RandomAccessFile(file, "rw")) {
			int xStart;
			int xEnd = playerX + playerRadiusX;
			int yStart;
			int yEnd = playerY + playerRadiusY;;
			//if player radius not over beginning or end of map
			if ((playerX - playerRadiusX > 0)) {
				xStart = playerX - playerRadiusX;
			} else {
				xStart = 0;
			}
			if(xEnd < 257) {
			} else {
				xEnd = 257;
			}
			if (playerY - playerRadiusY > 0) {
				yStart = playerY - playerRadiusY;
			} else {
				yStart = 0;
			}
			if(yEnd < 257) {
			} else {
				yEnd = 257;
			}

			raf.seek(xStart);
			byte[] byteArray = new byte[66049];
			int l = 0;
			for (int i = xStart; i < xEnd; i++) {
				for(int j = yStart;j< yEnd; j++) {
					byteArray[l] = raf.readByte();
					System.out.println(l);
					l++;
				}
			}
			System.out.println(xStart+" "+xEnd+" "+yStart+" "+yEnd);

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void printMap(int[][] map) // this prints the 2D array.
	{
		for (int i = 0; i < map.length; i++) {
			for (int j = 0; j < map.length; j++) {
				System.out.print(map[j][i] + "\t");
			}
			System.out.println("");
		}
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