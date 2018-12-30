package controller;

import javafx.application.HostServices;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import logic.DiamondSquare;
import model.Model;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.imageio.ImageIO;

/**
 * 
 * Controller for the map window.
 */

public class MapController extends Controller {
	
	@FXML
	private TabPane tabPane;
	@FXML
	private Tab tab1;
	@FXML
	private Tab tab2;
	@FXML
	private Tab tab3;
	
	private static DiamondSquare diamondSquare;
	private static int[][] currentMap;
	private static List<int[][]> fuser = new ArrayList<int[][]>();
	
	private void updateView() {
		//drawGround();
		//showImage();
	}
	
	/**
	 * 
	 * Generates the ground of a map with the Diamond Square algorithm.
	 * 
	 * Width & Height optimum: 10000 x 10000 maximum: 18798 x 18798
	 *
	 * soliciting map sizes: 2^x+1; e.g. 5, 9, 17, 33, 65, 129, 257, 513,
	 * 1025, 2049, 4097, 8193
	 *
	 */
	private void generateDiamondSquare() {
		diamondSquare = new DiamondSquare();
		currentMap = diamondSquare.generateMap(129);
	}

	private static BufferedImage scaleImage(BufferedImage Img, int width, int height) {
		BufferedImage resizedImg = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g2 = resizedImg.createGraphics();

		g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
		g2.drawImage(Img, 0, 0, width, height, null);
		g2.dispose();

		return resizedImg;
	}
	
	private void showImage(Image image) {
		ImageView iv1 = new ImageView();
        iv1.setImage(image);
	}
	
	/**
	 * 
	 * Draws the ground of the map based on the given height values in PNG format.
	 * Each map tile is 20*20 in size in the picture. If
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
	public BufferedImage DrawGround(int[][] map, int width, int height, String name) {

		BufferedImage bi = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
		
		try {
			// TYPE_INT_ARGB specifies the image format: 8-bit RGBA packed
			// into integer pixels
			BufferedImage snow = null;
			BufferedImage green = null;
			BufferedImage darkgreen = null;
			BufferedImage w = null;
			BufferedImage dw = null;
			BufferedImage mountain = null;
			BufferedImage beach = null;

			snow = ImageIO.read(new File("resources/snow.jpg"));
			green = ImageIO.read(new File("resources/green.jpg"));
			darkgreen = ImageIO.read(new File("resources/darkgreen.jpg"));
			w = ImageIO.read(new File("resources/water.jpg"));
			dw = ImageIO.read(new File("resources/deepWater.jpg"));
			mountain = ImageIO.read(new File("resources/mountain.jpg"));
			beach = ImageIO.read(new File("resources/beach.jpg"));

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
			
		} catch (IOException ie) {
			ie.printStackTrace();
		}
		
		return bi;
		
	}
	
	private void saveMap(BufferedImage bi, String name) {
		
		try {
			
			ImageIO.write(bi, "PNG", new File(System.getProperty("user.home") + "/Desktop" + File.separator + name));
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Debugging method that prints the generated map to the console.
	 */
	private static void printMap(int[][] mapArray, int arraySize) // this prints the 2D array.
	{
		for (int i = 0; i < arraySize; i++) {
			for (int j = 0; j < arraySize; j++) {
				System.out.print(mapArray[i][j] + "\t");
			}
			System.out.println("");
		}
	}
	
	@Override
	public void initialize(Stage stage, HostServices hostServices,
	                  MainController mainController, Model model) {
		super.initialize(stage, hostServices, mainController, model);
		
	}
	
}
