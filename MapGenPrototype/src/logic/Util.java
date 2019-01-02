package logic;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import javax.imageio.ImageIO;

public class Util {

	/**
	 * Returns a part of the map that surrounds a specified object.
	 * 
	 * @param distanceX The tiles surrounding the object in x direction
	 * @param distanceY The tiles surrounding the object in y direction
	 * @param objectX The X position of the object in map-tile-coordinates
	 * @param objectY The Y position of the object in map-tile-coordinates
	 * @param path to where the map file is stored.
	 */
	public void getMapPart(int distanceX, int distanceY, int objectX, int objectY, String path) {
		File file = new File(path);
		try (RandomAccessFile raf = new RandomAccessFile(file, "rw")) {
			int xStart;
			int xEnd = objectX + distanceX;
			int yStart;
			int yEnd = objectY + distanceY;
			//if object distance not outside of beginning or end of map
			if ((objectX - distanceX > 0)) {
				xStart = objectX - distanceX;
			} else {
				xStart = 0;
			}
			if(xEnd < 257) {
			} else {
				xEnd = 257;
			}
			if (objectY - distanceY > 0) {
				yStart = objectY - distanceY;
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
					l++;
				}
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 
	 * Takes a 2D Array and saves each tile as byte.
	 * 
	 * @param map The 2D map to save.
	 */
	public void saveMap(int[][] map, String mapPath) {
		File file = new File(mapPath);
		try (FileOutputStream fop = new FileOutputStream(file)) {

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
	 * Loads a map file from the given path with the given name and returns it as byte array.
	 * 
	 * @param loadPath where the map file is located.
	 * @return
	 * @throws IOException
	 */
	public byte[] loadMap(String loadPath, String filename) throws IOException {

		File file = new File(loadPath);

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
	 * 
	 * Takes a generated map and saves it in csv format.
	 * 
	 * @param map the 2D map to save.
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
	 * Loads a map from a .csv file and returns it as 2D Integer array.
	 * 
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
	 * Creates an image file of the generated map. Requires a map to be generated with
	 * generateMap() beforehand. Each map tile is 20*20 in size in the picture. If
	 * the generated map is bigger than the picture, the edges will be cut off in
	 * the image.
	 * 
	 * @param map
	 *            the 2D integer array representing the map.
	 * @param width
	 *            width of the picture. Optimum value: 10.000; Maximum value: 18798
	 * @param height
	 *            height of the picture. Optimum value: 10.000; Maximum value: 18798
	 * @param name of the file
	 * @param imageType
	 * 			  0 outputs as JPEG, 1 outputs as PNG
	 * @param path where to save the image.
	 */
	public void createPng(int[][] map, int width, int height, String name, boolean imageType, String path) {

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

			if(imageType) {
				ImageIO.write(bi, "PNG", new File(path));
			} else {
				ImageIO.write(bi, "JPEG", new File(path));
			}
		} catch (IOException ie) {
			ie.printStackTrace();
		}
	}
	
	/**
	 * Prints the values of a generated map to the console.
	 * Used for debugging and testing purposes.
	 * @param map to print.
	 */
	public void printMap(int[][] map) // this prints the 2D array.
	{
		for (int i = 0; i < map.length; i++) {
			for (int j = 0; j < map.length; j++) {
				System.out.print(map[j][i] + "\t");
			}
			System.out.println("");
		}
	}
	
}
