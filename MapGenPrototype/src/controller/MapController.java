package controller;

import javafx.application.HostServices;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Separator;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import logic.CellularAutomaton;
import logic.DiamondSquare;
import model.Model;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

/**
 * 
 * Controller for the main view in form of a {@link TabPane}, which shows the
 * generated maps.
 * 
 * @author Michael Bowes
 * 
 */

public class MapController extends Controller {

	@FXML
	private TabPane tabPane;

	private static DiamondSquare diamondSquare = new DiamondSquare();
	private static CellularAutomaton cellularAutomaton = new CellularAutomaton();
	private static int[][] currentMap;
	private ImageView currentView;
	private static String SNOW_TILE = System.getProperty("user.dir") + "/resources/snow.jpg";
	private static String MOUNTAIN_TILE = System.getProperty("user.dir") + "/resources/mountain.jpg";
	private static String GRASS_TILE = System.getProperty("user.dir") + "/resources/grass.jpg";
	private static String FOREST_TILE = System.getProperty("user.dir") + "/resources/grass2.jpg";
	private static String WATER_TILE = System.getProperty("user.dir") + "/resources/dwater.jpg";
	private static String COAST_TILE = System.getProperty("user.dir") + "/resources/water.jpg";
	private static String BEACH_TILE = System.getProperty("user.dir") + "/resources/beach.jpg";
	private static String ROCKS_TILE = System.getProperty("user.dir") + "/resources/rocks.jpg";
	private static String TREE_TILE = System.getProperty("user.dir") + "/resources/tree.jpg";

	/**
	 * 
	 * Generates the ground of a map with the Diamond Square algorithm. <br>
	 * <br>
	 * Image Width & Height optimum: 10000 x 10000<br>
	 * Maximum size for image creation: 18798 x 18798 <br>
	 * <br>
	 * soliciting map sizes: 2^x+1; e.g. 5, 9, 17, 33, 65, 129, 257, 513, 1025,
	 * 2049, 4097, 8193
	 *
	 */
	public void generateDiamondSquare(int snowParam, int mountainParam, int forestParam, int grassParam, int beachParam,
			int coastParam, int oceanParam) {
		diamondSquare = new DiamondSquare();
		int mapSize = getMainController().getControlsController().getMapSize();
		int imgWidth = getMainController().getControlsController().getImageWidth();
		int imgHeight = getMainController().getControlsController().getImageHeight();
		currentMap = diamondSquare.generateMap(mapSize);
		Image mapImage = drawGround(currentMap, imgWidth, imgHeight, snowParam, mountainParam, forestParam, grassParam,
				beachParam, coastParam, oceanParam);
		currentView = new ImageView();

		currentView.setImage(mapImage);
		Tab tab = tabPane.getTabs().get(0);
		ScrollPane scrollPane = new ScrollPane();
		scrollPane.setContent(currentView);
		scrollPane.setPadding(new Insets(50));
		tab.setContent(scrollPane);
	}

	public void generateActorBased() {

	}

	public void generateCellularAutomaton(int iterations, int birthRule, int deathRule, float survival) {
		cellularAutomaton = new CellularAutomaton(iterations, birthRule, deathRule, survival);
		int mapSize = getMainController().getControlsController().getMapSize();
		int imgWidth = getMainController().getControlsController().getImageWidth();
		int imgHeight = getMainController().getControlsController().getImageHeight();
		currentMap = cellularAutomaton.generateMap(mapSize);
		Image mapImage = drawCave(currentMap, imgWidth, imgHeight);
		currentView = new ImageView();

		currentView.setImage(mapImage);
		Tab tab = tabPane.getTabs().get(1);
		ScrollPane scrollPane = new ScrollPane();
		final Separator sepVert2 = new Separator();
		sepVert2.setOrientation(Orientation.VERTICAL);
		scrollPane.setContent(currentView);
		tab.setContent(scrollPane);
	}

	/**
	 * 
	 * Draws the height map for the cellular automaton onto an image.
	 * 
	 */
	public Image drawCave(int[][] map, int width, int height) {

		BufferedImage bi = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);

		try {
			BufferedImage wallSegment = null;
			BufferedImage ground = null;
			BufferedImage trees = null;
			BufferedImage rocks = null;

			wallSegment = ImageIO.read(new File(MOUNTAIN_TILE));
			ground = ImageIO.read(new File(FOREST_TILE));
			trees = ImageIO.read(new File(TREE_TILE));
			rocks = ImageIO.read(new File(ROCKS_TILE));

			Graphics2D ig2 = bi.createGraphics();

			int mapHeight = map.length;
			int mapWidth = map.length;

			for (int i = 0; i < mapWidth; i++) {
				for (int j = 0; j < mapHeight; j++) {
					if (map[j][i] == 0) {
						ig2.drawImage(ground, i * 45, j * 45, null);
					} else if (map[j][i] == 1) {
						ig2.drawImage(wallSegment, i * 45, j * 45, null);
					}
					/*
					 * else if (map[j][i] == 2) { ig2.drawImage(trees, i * 45, j * 45, null); } else
					 * if (map[j][i] == 3) { ig2.drawImage(rocks, i * 45, j * 45, null); }
					 */
				}
			}

			ig2.dispose();

		} catch (IOException ie) {
			ie.printStackTrace();
		}

		Image image = new Util().convertBufferedImage(bi);

		return image;
	}

	/**
	 * 
	 * Draws the created height map for the diamond square algorithm onto an image.
	 * If the generated map is bigger than the picture size, the image will still be
	 * created with the edges cut off.
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
			System.out.println("spec: " + spectrum + " min: " + minimum + " max: " + maximum);
			double oceanSpectrum = spectrum * (oceanParam * 0.01);
			double coastSpectrum = spectrum * (coastParam * 0.01);
			double beachSpectrum = spectrum * (beachParam * 0.01);
			double mountainSpectrum = spectrum * (mountainParam * 0.01);
			double snowSpectrum = spectrum * (snowParam * 0.01);
			double grassSpectrum = spectrum * (grassParam * 0.01);
			double forestSpectrum = spectrum * (forestParam * 0.01);

			for (int i = 0; i < mapWidth; i++) {
				for (int j = 0; j < mapHeight; j++) {
					if (map[j][i] < minimum + oceanSpectrum) // 15% chance to turn BLUE = deep water
					{
						ig2.drawImage(dw, i * 32, j * 32, null);
					} else if (map[j][i] < minimum + oceanSpectrum + coastSpectrum) // chance for shallow water
					{
						ig2.drawImage(w, i * 32, j * 32, null);
					}

					else if (map[j][i] < minimum + oceanSpectrum + coastSpectrum + beachSpectrum) // chance for beach
					{
						ig2.drawImage(beach, i * 32, j * 32, null);
					}

					else if (map[j][i] < minimum + oceanSpectrum + coastSpectrum + beachSpectrum + grassSpectrum) // chance
																													// for
																													// grass
					{
						ig2.drawImage(green, i * 32, j * 32, null);
					}

					else if (map[j][i] < minimum + oceanSpectrum + coastSpectrum + beachSpectrum + grassSpectrum
							+ forestSpectrum) // chance
					// for
					// forest
					{
						ig2.drawImage(darkgreen, i * 32, j * 32, null);
					}

					else if (map[j][i] < minimum + oceanSpectrum + coastSpectrum + beachSpectrum + grassSpectrum
							+ forestSpectrum + mountainSpectrum) // chance for mountain
					{
						ig2.drawImage(mountain, i * 32, j * 32, null);
					} else if (map[j][i] < minimum + oceanSpectrum + coastSpectrum + beachSpectrum + grassSpectrum
							+ forestSpectrum + mountainSpectrum + snowSpectrum) // chance for snow
					{
						ig2.drawImage(snow, i * 32, j * 32, null);
					}
				}
			}

			ig2.dispose();

		} catch (IOException ie) {
			ie.printStackTrace();
		}

		Image image = new Util().convertBufferedImage(bi);

		return image;

	}

	public TabPane getTabPane() {
		return tabPane;
	}

	public ImageView getCurrentView() {
		return currentView;
	}

	public CellularAutomaton getCellularAutomaton() {
		return cellularAutomaton;
	}

	public DiamondSquare getDiamondSquare() {
		return diamondSquare;
	}

	@Override
	public void initialize(Stage stage, HostServices hostServices, MainController mainController) {
		super.initialize(stage, hostServices, mainController);
		tabPane.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {
			@Override
			public void changed(ObservableValue<? extends Number> ov, Number oldValue, Number newValue) {
				if ((int) newValue == 0) {
					getMainController().getControlsController().loadDiamondSquareTab();
				}
				if ((int) newValue == 1) {
					getMainController().getControlsController().loadCellularAutomatonTab();
				}
			}
		});
	}

}
