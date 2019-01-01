package controller;

import javafx.application.HostServices;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.VBox;
import javafx.scene.transform.Scale;
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

	private static DiamondSquare diamondSquare;
	private static int[][] currentMap;
	private ImageView currentView;
	private static String SNOW_TILE = System.getProperty("user.dir") + "/resources/snow.jpg";
	private static String MOUNTAIN_TILE = System.getProperty("user.dir") + "/resources/mountain.jpg";
	private static String GRASS_TILE = System.getProperty("user.dir") + "/resources/grass.jpg";
	private static String FOREST_TILE = System.getProperty("user.dir") + "/resources/grass2.jpg";
	private static String WATER_TILE = System.getProperty("user.dir") + "/resources/dwater.jpg";
	private static String COAST_TILE = System.getProperty("user.dir") + "/resources/water.jpg";
	private static String BEACH_TILE = System.getProperty("user.dir") + "/resources/beach.jpg";

	/**
	 * 
	 * Generates the ground of a map with the Diamond Square algorithm.
	 * 
	 * Width & Height optimum: 10000 x 10000 maximum: 18798 x 18798
	 *
	 * soliciting map sizes: 2^x+1; e.g. 5, 9, 17, 33, 65, 129, 257, 513, 1025,
	 * 2049, 4097, 8193
	 *
	 */
	public void generateDiamondSquare(int snowParam, int mountainParam, int forestParam, int grassParam, int beachParam,
			int coastParam, int oceanParam) {
		diamondSquare = new DiamondSquare();
		currentMap = diamondSquare.generateMap(129);
		Image mapImage = drawGround(currentMap, 5000, 5000, snowParam, mountainParam, forestParam, grassParam,
				beachParam, coastParam, oceanParam);
		currentView = new ImageView();

		currentView.setImage(mapImage);
		Tab tab = tabPane.getTabs().get(0);
		ScrollPane scrollPane = new ScrollPane();
		scrollPane.setContent(currentView);
		tab.setContent(scrollPane);
	}

	public void generateActorBased() {

	}

	public void generateVoronoi() {

	}

	private static BufferedImage scaleImage(BufferedImage Img, int width, int height) {
		BufferedImage resizedImg = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g2 = resizedImg.createGraphics();

		g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
		g2.drawImage(Img, 0, 0, width, height, null);
		g2.dispose();

		return resizedImg;
	}

	/**
	 * 
	 * Draws the ground of the map based on the given height values in PNG format.
	 * Each map tile is 20*20 in size in the picture. If the generated map is bigger
	 * than the picture, the edges will be cut off in the image.
	 * 
	 * @param map    the 2D integer array representing the map.
	 * @param width  Width of the picture. Optimum value: 10.000; Maximum value:
	 *               18798
	 * @param height Height of the picture. Optimum value: 10.000; Maximum value:
	 *               18798
	 */
	public Image drawGround(int[][] map, int width, int height, int snowParam, int mountainParam, int forestParam,
			int grassParam, int beachParam, int coastParam, int oceanParam) {

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

			snow = ImageIO.read(new File(SNOW_TILE));
			green = ImageIO.read(new File(GRASS_TILE));
			darkgreen = ImageIO.read(new File(FOREST_TILE));
			w = ImageIO.read(new File(COAST_TILE));
			dw = ImageIO.read(new File(WATER_TILE));
			mountain = ImageIO.read(new File(MOUNTAIN_TILE));
			beach = ImageIO.read(new File(BEACH_TILE));

			Graphics2D ig2 = bi.createGraphics();

			// map[0].length necessary for non-square maps.

			int mapHeight = map.length;
			int mapWidth = map.length;
			System.out.println("picL: " + mapHeight);
			System.out.println("picW: " + mapWidth);

			int minimum = 0;
			int maximum = 0;
			for (int i = 0; i < mapWidth; i++) {
				for (int j = 0; j < mapHeight; j++) {
					if (map[j][i] > maximum) {
						maximum = map[j][i];
					}
					if (map[j][i] < minimum) {
						minimum = map[j][i];
					}
				}
			}
			int spectrum = Math.abs(minimum) + maximum;
			System.out.println("spec: "+ spectrum + " min: " + minimum + " max: " + maximum);
			double oceanSpectrum = spectrum*(oceanParam*0.01);
			double coastSpectrum = spectrum*(coastParam*0.01);
			double beachSpectrum = spectrum*(beachParam*0.01);
			double mountainSpectrum = spectrum*(mountainParam*0.01);
			double snowSpectrum = spectrum*(snowParam*0.01);
			double grassSpectrum = spectrum*(grassParam*0.01);
			double forestSpectrum = spectrum*(forestParam*0.01);
			
			for (int i = 0; i < mapWidth; i++) {
				for (int j = 0; j < mapHeight; j++) {
					if (map[j][i] < minimum + oceanSpectrum) // 15% chance to turn BLUE = deep water
					{
						ig2.drawImage(dw, i * 32, j * 32, null);
					} else if (map[j][i] < minimum + oceanSpectrum + coastParam) // chance for shallow water
					{
						ig2.drawImage(w, i * 32, j * 32, null);
					}

					else if (map[j][i] < minimum + oceanSpectrum + coastParam + beachParam) // chance for beach
					{
						ig2.drawImage(beach, i * 32, j * 32, null);
					}

					else if (map[j][i] < minimum + oceanSpectrum + coastParam + beachParam + grassParam) // chance for grass
					{
						ig2.drawImage(green, i * 32, j * 32, null);
					}

					else if (map[j][i] < minimum + oceanSpectrum + coastParam + beachParam + grassParam + forestParam) // chance
																													// for
																													// forest
					{
						ig2.drawImage(darkgreen, i * 32, j * 32, null);
					}

					else if (map[j][i] < minimum + oceanSpectrum + coastParam + beachParam + grassParam + forestParam
							+ mountainParam) // chance for mountain
					{
						ig2.drawImage(mountain, i * 32, j * 32, null);
					} else if (map[j][i] < minimum + oceanSpectrum + coastParam + beachParam + grassParam + forestParam
							+ mountainParam + snowParam) // chance for snow
					{
						ig2.drawImage(snow, i * 32, j * 32, null);
					}
				}
			}

			ig2.dispose();

		} catch (IOException ie) {
			ie.printStackTrace();
		}

		saveMap(bi, "map");
		Image image = convertBufferedImage(bi);

		return image;

	}

	/**
	 * Converts a BufferedImage to a FXImage.
	 * 
	 * @param bi
	 * @return
	 */
	private Image convertBufferedImage(BufferedImage bi) {
		WritableImage wr = null;
		if (bi != null) {
			wr = new WritableImage(bi.getWidth(), bi.getHeight());
			PixelWriter pw = wr.getPixelWriter();
			for (int x = 0; x < bi.getWidth(); x++) {
				for (int y = 0; y < bi.getHeight(); y++) {
					pw.setArgb(x, y, bi.getRGB(x, y));
				}
			}
		}
		return wr;
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

	public TabPane getTabPane() {
		return tabPane;
	}

	public ImageView getCurrentView() {
		return currentView;
	}

	@Override
	public void initialize(Stage stage, HostServices hostServices, MainController mainController, Model model) {
		super.initialize(stage, hostServices, mainController, model);

	}

}
