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

		DiamondSquareImpl algo = new DiamondSquareImpl(size);
		Thread t = new Thread(algo);
		t.start();
		try {
			t.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		map = algo.getMap();
		
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