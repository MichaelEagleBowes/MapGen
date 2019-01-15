package controller;

import java.util.ArrayList;
import java.util.Collections;
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
import javafx.scene.chart.ScatterChart;
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
import model.MapContainer;
import model.MapType;
import util.HeatMap;
import util.Triple;
import util.Tuple;
import javafx.scene.canvas.GraphicsContext;

public class StatisticsController extends Controller {

	@FXML
	private HBox centerBox;
	@FXML
	private ComboBox<ProceduralAlgorithm> algorithmSelect;
	@FXML
	private Button currentMapStatisticsBtn;
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
	 * 
	 * Fills the BarChart with the statistics of the currently loaded map of the
	 * selected algorithm.
	 */
	private BarChart<String, Double> fillBarChart(BarChart<String, Double> barChart, ProceduralAlgorithm algorithm) {
		CategoryAxis xAxis = new CategoryAxis();
		xAxis.setLabel("Expressivity");
		NumberAxis yAxis = new NumberAxis();
		yAxis.setLabel("Intensity");
		barChart = new BarChart(xAxis, yAxis);

		int[][] selectedMap = maps.get(algorithmSelect.getSelectionModel().getSelectedItem());

		if (algorithmSelect.getSelectionModel().getSelectedItem() instanceof CellularAutomaton) {
			double spaceList = getMainController().getMapController().getCellularAutomaton().calcRelativeOpenSpace();
			double areaList = getMainController().getMapController().getCellularAutomaton().calcNumberOfAreas();

			XYChart.Series<String, Double> series1 = new XYChart.Series<>();
			series1.setName("Current CA");
			series1.getData().add(new XYChart.Data<>("Separate Areas", areaList));
			series1.getData().add(new XYChart.Data<>("Relative Open Space", spaceList));

			barChart.getData().add(series1);

			barChart.setStyle("-fx-background-color: rgba(0,168,355,0.5);-fx-background-radius: 10;");

			return barChart;

		} else if (algorithmSelect.getSelectionModel().getSelectedItem() instanceof DiamondSquare) {
			List<Integer> params = getMainController().getMapController().getParametersDiamondSquare();
			HashMap<Integer, List<Integer>> absPos = DiamondSquare.calcAbsolutePositions(params.get(0), params.get(1),
					params.get(2), params.get(3), params.get(4), params.get(5), params.get(6));

			// List<Double> relPos = DiamondSquare.calcRelativePositions();

			List<Double> areaCount = DiamondSquare.calcNumberOfAreas(params.get(0), params.get(1), params.get(2),
					params.get(3), params.get(4), params.get(5), params.get(6));

			XYChart.Series<String, Double> series1 = new XYChart.Series<>();
			series1.setName("Snow");
			series1.getData().add(new XYChart.Data<>("Separate Areas", areaCount.get(6)));
			series1.getData().add(new XYChart.Data<>("Distance X", (double) absPos.get(5).get(0)));
			series1.getData().add(new XYChart.Data<>("Distance Y", (double) absPos.get(5).get(1)));

			XYChart.Series<String, Double> series2 = new XYChart.Series<>();
			series2.setName("Mountain");
			series2.getData().add(new XYChart.Data<>("Separate Areas", areaCount.get(5)));
			series2.getData().add(new XYChart.Data<>("Distance X", (double) absPos.get(6).get(0)));
			series2.getData().add(new XYChart.Data<>("Distance Y", (double) absPos.get(6).get(1)));

			XYChart.Series<String, Double> series3 = new XYChart.Series<>();
			series3.setName("Forest");
			series3.getData().add(new XYChart.Data<>("Separate Areas", areaCount.get(4)));
			series3.getData().add(new XYChart.Data<>("Distance X", (double) absPos.get(4).get(0)));
			series3.getData().add(new XYChart.Data<>("Distance Y", (double) absPos.get(4).get(1)));

			XYChart.Series<String, Double> series4 = new XYChart.Series<>();
			series4.setName("Grass");
			series4.getData().add(new XYChart.Data<>("Separate Areas", areaCount.get(3)));
			series4.getData().add(new XYChart.Data<>("Distance X", (double) absPos.get(3).get(0)));
			series4.getData().add(new XYChart.Data<>("Distance Y", (double) absPos.get(3).get(1)));

			XYChart.Series<String, Double> series5 = new XYChart.Series<>();
			series5.setName("Beach");
			series5.getData().add(new XYChart.Data<>("Separate Areas", areaCount.get(2)));
			series5.getData().add(new XYChart.Data<>("Distance X", (double) absPos.get(2).get(0)));
			series5.getData().add(new XYChart.Data<>("Distance Y", (double) absPos.get(2).get(1)));

			XYChart.Series<String, Double> series6 = new XYChart.Series<>();
			series6.setName("Coast");
			series6.getData().add(new XYChart.Data<>("Separate Areas", areaCount.get(1)));
			series6.getData().add(new XYChart.Data<>("Distance X", (double) absPos.get(1).get(0)));
			series6.getData().add(new XYChart.Data<>("Distance Y", (double) absPos.get(1).get(1)));

			XYChart.Series<String, Double> series7 = new XYChart.Series<>();
			series7.setName("Ocean");
			series7.getData().add(new XYChart.Data<>("Separate Areas", areaCount.get(0)));
			series7.getData().add(new XYChart.Data<>("Distance X", (double) absPos.get(0).get(0)));
			series7.getData().add(new XYChart.Data<>("Distance Y", (double) absPos.get(0).get(1)));

			barChart.getData().addAll(series1, series2, series3, series4, series5, series6, series7);

			barChart.setStyle("-fx-background-color: rgba(0,168,355,0.5);-fx-background-radius: 10;");

			return barChart;
		} else {
			return null;
		}
	}

	private void initComboBoxes() {
		ObservableList<ProceduralAlgorithm> algorithmList = FXCollections.observableArrayList();
		algorithmList.add(getMainController().getMapController().getCellularAutomaton());
		algorithmList.add(getMainController().getMapController().getDiamondSquare());
		algorithmSelect.setItems(algorithmList);
	}

	private void createHistogram(ProceduralAlgorithm algorithm) {
		centerBox.getChildren().clear();
		expressivityBarChart = fillBarChart(expressivityBarChart, algorithm);
		centerBox.getChildren().add(expressivityBarChart);
	}

	private HashMap<Tuple, MapContainer> createHeatMapSamples() {
		List<MapContainer> mapSamples = new ArrayList<MapContainer>();
		HashMap<Tuple, MapContainer> collectiveMapSamples = new HashMap();
			CellularAutomaton ca = new CellularAutomaton(getMainController().getControlsController().getIterations(),
					getMainController().getControlsController().getBirthRule(),
					getMainController().getControlsController().getDeathRule(),
					getMainController().getControlsController().getSurvivalChance());
			
			MapContainer map = new MapContainer();

			for (int j = 0; j < 10; j++) {
				for (int i = 1; i < 11; i++) {
					double size = Math.pow(2, i) + 1;
					ca.generateMap((int) size);
					map.setRelativeSpace(ca.calcRelativeOpenSpace());
					map.setAreaCount(ca.calcNumberOfAreas());

					int cap = 8193;
					double previousM = 0;
					for (double m = 1; m < cap; m = m + 1 * m) {
						double previousN = 0;
						for (double n = 1; n < cap; n = n + 1 * n) {
							if (map.getRelativeSpace() >= Math.pow(2, 10) / m
									&& map.getRelativeSpace() < Math.pow(2, 10) / (m - previousM)
									&& map.getAreaCount() >= (Math.pow(2, 10) / 2) / n
									&& map.getAreaCount() < (Math.pow(2, 10) / 2) / (n - previousN)) {
								map.setX(j);
								map.setY(i - 1);
								if (mapSamples.contains(map)) {
								} else {
									mapSamples.add(map);
								}
							} else {
							}
							previousN = n;
						}
						previousM = m;
					}
				}
			}

			for (int j = 0; j < 10; j++) {
				for (int i = 0; i < 10; i++) {
					int count = 0;
					MapContainer currentContainer = new MapContainer();
					for (MapContainer container : mapSamples) {
						if (container.getX() == j && container.getY() == i) {
							count++;
							currentContainer = container;
						}
					}
					currentContainer.setMapCount(count);
					currentContainer.setTotalMapCount(j * i);
					collectiveMapSamples.put(new Tuple(j, i), currentContainer);
				}
			}
		System.out.println(collectiveMapSamples);

		return collectiveMapSamples;
	}
	
	private ScatterChart<String, Number> createScatterPlot() {
		NumberAxis xAxis = new NumberAxis(0, 10, 0);
		xAxis.setLabel("X-Axis");

		NumberAxis yAxis = new NumberAxis(0, 10, 0);
		yAxis.setLabel("Y-Axis");
		ScatterChart<String, Number> scatterChart = new ScatterChart(xAxis, yAxis);
		
		if (algorithmSelect.getSelectionModel().getSelectedItem() instanceof DiamondSquare) {
			List<Integer> params = getMainController().getMapController().getParametersDiamondSquare();
			DiamondSquare ds = new DiamondSquare();
			
			double maximumAbsPos=0;
			double maximumAreaCount=0;
			
			XYChart.Series series1 = new XYChart.Series();
			XYChart.Series series2 = new XYChart.Series();
			XYChart.Series series3 = new XYChart.Series();
			XYChart.Series series4 = new XYChart.Series();
			XYChart.Series series5 = new XYChart.Series();
			XYChart.Series series6 = new XYChart.Series();
			XYChart.Series series7 = new XYChart.Series();
			series1.setName("Ocean");
			series2.setName("Coast");
			series3.setName("Beach");
			series4.setName("Grass");
			series5.setName("Forest");
			series6.setName("Mountain");
			series7.setName("Snow");
			
			for (int j = 0; j < 10; j++) {
				for (int i = 1; i < 11; i++) {
					double size = Math.pow(2, i) + 1;
					ds.generateMap((int) size);
					List<Double> absPos = DiamondSquare.calcAbsolutePositionsAverage(params.get(0), params.get(1),
							params.get(2), params.get(3), params.get(4), params.get(5), params.get(6));
					List<Double> areaCount = DiamondSquare.calcNumberOfAreas(params.get(0), params.get(1), params.get(2),
							params.get(3), params.get(4), params.get(5), params.get(6));
					
					if(Collections.max(absPos)> maximumAbsPos) {
						maximumAbsPos = Collections.max(absPos);
					}
					if(Collections.max(areaCount)> maximumAreaCount) {
						maximumAreaCount = Collections.max(areaCount);
					}
					series1.getData().add(new XYChart.Data(absPos.get(0), areaCount.get(0)));
					series2.getData().add(new XYChart.Data(absPos.get(1), areaCount.get(1)));
					series3.getData().add(new XYChart.Data(absPos.get(2), areaCount.get(2)));
					series4.getData().add(new XYChart.Data(absPos.get(3), areaCount.get(3)));
					series5.getData().add(new XYChart.Data(absPos.get(4), areaCount.get(4)));
					series6.getData().add(new XYChart.Data(absPos.get(5), areaCount.get(5)));
					series7.getData().add(new XYChart.Data(absPos.get(6), areaCount.get(6)));
				}
			}
			
			NumberAxis caXAxis = new NumberAxis(0, maximumAbsPos, 0);
			caXAxis.setLabel("Absolute Positions");

			NumberAxis caYAxis = new NumberAxis(0, maximumAreaCount, 0);
			caYAxis.setLabel("Area Counts");

			scatterChart = new ScatterChart(xAxis, yAxis);

			// Setting the data to scatter chart
			scatterChart.getData().addAll(series1, series2, series3, series4, series5, series6, series7);
			
		} else if(algorithmSelect.getSelectionModel().getSelectedItem() instanceof CellularAutomaton) {
			CellularAutomaton ca = new CellularAutomaton(getMainController().getControlsController().getIterations(),
					getMainController().getControlsController().getBirthRule(),
					getMainController().getControlsController().getDeathRule(),
					getMainController().getControlsController().getSurvivalChance());

			double maximumRelativeSpace=0;
			double maximumNumberOfAreas=0;
			
			XYChart.Series series = new XYChart.Series();
			series.setName("Caves");
			for (int j = 0; j < 10; j++) {
				for (int i = 1; i < 11; i++) {
					double size = Math.pow(2, i) + 1;
					ca.generateMap((int) size);
					if(ca.calcRelativeOpenSpace() > maximumRelativeSpace) {
						maximumRelativeSpace = ca.calcRelativeOpenSpace();
					}
					if(ca.calcNumberOfAreas() > maximumNumberOfAreas) {
						maximumNumberOfAreas = ca.calcNumberOfAreas();
					}
					series.getData().add(new XYChart.Data(ca.calcRelativeOpenSpace(), ca.calcNumberOfAreas()));
				}
			}
			
			NumberAxis caXAxis = new NumberAxis(0, maximumRelativeSpace, 0);
			caXAxis.setLabel("Relative Open Space");

			NumberAxis caYAxis = new NumberAxis(0, maximumNumberOfAreas, 0);
			caYAxis.setLabel("Number of Areas");

			scatterChart = new ScatterChart(xAxis, yAxis);

			// Setting the data to scatter chart
			scatterChart.getData().addAll(series);
		}
		
		return scatterChart;
	}

	private void createHeatMap() {
		centerBox.getChildren().clear();

		//HashMap<Tuple, MapContainer> newTuples = createHeatMapSamples();

		HeatMap heatMap = new HeatMap(400, 400);
		GraphicsContext gc = heatMap.getGraphicsContext2D();
		ImageView colorScale = heatMap.createColorScale();
		//ImageView heatmapImg = heatMap.drawHeatMap(gc, newTuples);

		centerBox.getChildren().add(createScatterPlot());
		centerBox.getChildren().add(colorScale);
	}

	private void createCurrentMapStatistics() {
		createHistogram(algorithmSelect.getSelectionModel().getSelectedItem());
	}

	private void createExpressiveRangeChart() {
		if (algorithmSelect.getSelectionModel().getSelectedItem() instanceof DiamondSquare) {
			createHeatMap();
		} else if (algorithmSelect.getSelectionModel().getSelectedItem() instanceof CellularAutomaton) {
			createHeatMap();
		} else {
			Util.informationAlert("No Algorithm selected", "Please make sure to select an Algorithm first.");
		}
	}

	private void initButtons() {
		currentMapStatisticsBtn.setOnAction(event -> {
			if (algorithmSelect.getSelectionModel().getSelectedItem().mapPresent()) {
				createCurrentMapStatistics();
			} else {
				Util.informationAlert("No Map available", "Please generate a map for the selected Algorithm first.");
			}
		});
		expressiveRangeBtn.setOnAction(event -> {
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
