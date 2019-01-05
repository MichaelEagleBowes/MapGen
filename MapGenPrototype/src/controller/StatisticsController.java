package controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javafx.application.HostServices;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Orientation;
import javafx.scene.Node;
import javafx.scene.canvas.Canvas;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Separator;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import logic.CellularAutomaton;
import logic.DiamondSquare;
import logic.ProceduralAlgorithm;
import model.MapType;
import util.HeatMap;
import javafx.scene.canvas.GraphicsContext;

public class StatisticsController extends Controller {

	@FXML
	private HBox centerBox;
	@FXML
	private ComboBox<ProceduralAlgorithm> algorithmSelect;
	@FXML
	private Button expressiveRangeBtn;
	@FXML
	private Button controllabilityBtn;
	private HashMap<ProceduralAlgorithm, int[][]> maps = new HashMap<ProceduralAlgorithm, int[][]>();
	private BarChart<String, Double> expressivityBarChart;

	private void showExpressivity() {

	}

	private void showControllability() {

	}

	/**
	 * Fills the BarChart with the count of appliances of the individual
	 * neighborhood structures that were used for generating that timetable.
	 */
	private BarChart<String, Double> fillBarChart(BarChart<String, Double> barChart) {
		CategoryAxis xAxis = new CategoryAxis();
		xAxis.setLabel("Expressivity");
		NumberAxis yAxis = new NumberAxis();
		yAxis.setLabel("Intensity");
		barChart = new BarChart(xAxis, yAxis);

		int[][] selectedMap = maps.get(algorithmSelect.getSelectionModel().getSelectedItem());
		/*
		 * List<Integer> params =
		 * getMainController().getMapController().getParameters(); List<Double>
		 * areaCount = DiamondSquare.calcNumberOfAreas( params.get(0), params.get(1),
		 * params.get(2) , params.get(3), params.get(4), params.get(5) , params.get(6));
		 */

		List<Number> spectraParams = getMainController().getMapController().getSpectraParameters();
		List<List<Integer>> absPos = DiamondSquare.calcAbsolutePositions((int) spectraParams.get(0),
				(double) spectraParams.get(1), (double) spectraParams.get(2), (double) spectraParams.get(3),
				(double) spectraParams.get(4), (double) spectraParams.get(5), (double) spectraParams.get(6),
				(double) spectraParams.get(7));
		double relPos = DiamondSquare.calcRelativePositions();

		XYChart.Series<String, Double> series1 = new XYChart.Series<>();
		series1.setName("Snow");
		series1.getData().add(new XYChart.Data<>("Separate Areas", 1d));
		series1.getData().add(new XYChart.Data<>("Spread", 3d));
		series1.getData().add(new XYChart.Data<>("Distance X", (double) absPos.get(6).get(0)));
		series1.getData().add(new XYChart.Data<>("Distance Y", (double) absPos.get(6).get(1)));

		XYChart.Series<String, Double> series2 = new XYChart.Series<>();
		series2.setName("Mountain");
		series2.getData().add(new XYChart.Data<>("Separate Areas", 1d));
		series2.getData().add(new XYChart.Data<>("Spread", 5d));
		series2.getData().add(new XYChart.Data<>("Distance X", (double) absPos.get(5).get(0)));
		series2.getData().add(new XYChart.Data<>("Distance Y", (double) absPos.get(5).get(1)));

		XYChart.Series<String, Double> series3 = new XYChart.Series<>();
		series3.setName("Forest");
		series3.getData().add(new XYChart.Data<>("Separate Areas", 1d));
		series3.getData().add(new XYChart.Data<>("Spread", 5d));
		series3.getData().add(new XYChart.Data<>("Distance X", (double) absPos.get(4).get(0)));
		series3.getData().add(new XYChart.Data<>("Distance Y", (double) absPos.get(4).get(1)));

		XYChart.Series<String, Double> series4 = new XYChart.Series<>();
		series4.setName("Grass");
		series4.getData().add(new XYChart.Data<>("Separate Areas", 1d));
		series4.getData().add(new XYChart.Data<>("Spread", 3d));
		series4.getData().add(new XYChart.Data<>("Distance X", (double) absPos.get(3).get(0)));
		series4.getData().add(new XYChart.Data<>("Distance Y", (double) absPos.get(3).get(1)));

		XYChart.Series<String, Double> series5 = new XYChart.Series<>();
		series5.setName("Beach");
		series5.getData().add(new XYChart.Data<>("Separate Areas", 1d));
		series5.getData().add(new XYChart.Data<>("Spread", 5d));
		series5.getData().add(new XYChart.Data<>("Distance X", (double) absPos.get(2).get(0)));
		series5.getData().add(new XYChart.Data<>("Distance Y", (double) absPos.get(2).get(1)));

		XYChart.Series<String, Double> series6 = new XYChart.Series<>();
		series6.setName("Coast");
		series6.getData().add(new XYChart.Data<>("Separate Areas", 1d));
		series6.getData().add(new XYChart.Data<>("Spread", 5d));
		series6.getData().add(new XYChart.Data<>("Distance X", (double) absPos.get(1).get(0)));
		series6.getData().add(new XYChart.Data<>("Distance Y", (double) absPos.get(1).get(1)));

		XYChart.Series<String, Double> series7 = new XYChart.Series<>();
		series7.setName("Ocean");
		series7.getData().add(new XYChart.Data<>("Separate Areas", 1d));
		series7.getData().add(new XYChart.Data<>("Spread", 5d));
		series7.getData().add(new XYChart.Data<>("Distance X", (double) absPos.get(0).get(0)));
		series7.getData().add(new XYChart.Data<>("Distance Y", (double) absPos.get(0).get(1)));

		barChart.getData().addAll(series1, series2, series3, series4, series5, series6, series7);

		barChart.setStyle("-fx-background-color: rgba(0,168,355,0.5);-fx-background-radius: 10;");

		return barChart;
	}

	private void initComboBoxes() {
		ObservableList<ProceduralAlgorithm> algorithmList = FXCollections.observableArrayList();
		algorithmList.add(getMainController().getMapController().getCellularAutomaton());
		algorithmList.add(getMainController().getMapController().getDiamondSquare());
		algorithmSelect.setItems(algorithmList);
	}

	private void createHistogram() {
		centerBox.getChildren().clear();
		expressivityBarChart = fillBarChart(expressivityBarChart);
		Separator vertSeparator = new Separator();
		vertSeparator.setOrientation(Orientation.VERTICAL);
		centerBox.getChildren().add(expressivityBarChart);
		centerBox.getChildren().add(vertSeparator);
	}

	private double[] createHeatMapSamples(double[] spaceList) {
		for (int i = 1; i < 101; i++) {
			for (int d = 1; d < 9; d++) {
				for (int b = 1; b < 9; b++) {
					for (int s = 0; s < 1; s += 0.1f) {
						CellularAutomaton ca = new CellularAutomaton(1 * i, 1 * b, 1 * d, 0.1f+s);
						ca.generateMap(getMainController().getControlsController().getMapSize());
						// areaList.add(ca.calcNumberOfAreas());
						spaceList[i] = CellularAutomaton.calcRelativeOpenSpace();
					}
				}
			}
		}
		return spaceList;
	}

	private void createHeatMap() {
		centerBox.getChildren().clear();
		// System.out.println("Areas: "+CellularAutomaton.calcNumberOfAreas()); 1 bis
		// (mapSize^2)/2
		// System.out.println(" Space: "+CellularAutomaton.calcRelativeOpenSpace()); 0
		// bis mapSize^2

		// TODO: add sample size field instead of 100
		List<Double> areaList = new ArrayList<Double>();
		double[] spaceList = new double[getMainController().getControlsController().getMapSize()];
		spaceList = createHeatMapSamples(spaceList);
		// for current map:
		// double areas = ((CellularAutomaton)
		// algorithmSelect.getSelectionModel().getSelectedItem()).calcNumberOfAreas();
		// double relativeSpace = ((CellularAutomaton)
		// algorithmSelect.getSelectionModel().getSelectedItem())
		// .calcRelativeOpenSpace();

		HeatMap heatMap = new HeatMap(300, 250);
		GraphicsContext gc = heatMap.getGraphicsContext2D();
		double[] firstDim = { 0, 30, 100 };
		ImageView colorScale = heatMap.createColorScale();
		ImageView heatmapImg = heatMap.drawHeatMap(gc, firstDim, spaceList);

		centerBox.getChildren().add(heatmapImg);
		centerBox.getChildren().add(colorScale);
	}

	private void createExpressiveRangeChart() {
		if (algorithmSelect.getSelectionModel().getSelectedItem() instanceof DiamondSquare) {
			createHistogram();
		} else if (algorithmSelect.getSelectionModel().getSelectedItem() instanceof CellularAutomaton) {
			createHeatMap();
		}

	}

	private void initButtons() {
		expressiveRangeBtn.setOnAction(event -> {
			System.out.println(algorithmSelect.getSelectionModel().getSelectedItem());
			if (algorithmSelect.getSelectionModel().getSelectedItem().mapPresent()) {
				createExpressiveRangeChart();
			} else {
				Util.informationAlert("No Map available", "Please generate a map for the selected Algorithm first.");
			}
		});

		controllabilityBtn.setOnAction(event -> {

		});
	}

	@Override
	public void initialize(Stage stage, HostServices hostServices, MainController mainController) {
		super.initialize(stage, hostServices, mainController);
		initComboBoxes();
		initButtons();
		if (getMainController().getMapController().getDiamondSquare().mapPresent()) {
			maps.put(getMainController().getMapController().getDiamondSquare(),
					getMainController().getMapController().getDiamondSquare().getMap());
		}
		if (getMainController().getMapController().getCellularAutomaton().mapPresent()) {
			maps.put(getMainController().getMapController().getCellularAutomaton(),
					getMainController().getMapController().getCellularAutomaton().getMap());
		}
	}

}
