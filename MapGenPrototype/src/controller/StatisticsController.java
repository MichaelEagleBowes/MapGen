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

	private HashMap<Double, Tuple<Number, Number, Number>> createHeatMapSamples(List<Tuple<Number, Number, Number>> tuples) {
		HashMap<Double, Tuple<Number, Number, Number>> newTuples = new HashMap<Double, Tuple<Number, Number, Number>>();
		if (algorithmSelect.getSelectionModel().getSelectedItem() instanceof DiamondSquare) {
			List<Integer> params = getMainController().getMapController().getParametersDiamondSquare();
			DiamondSquare ds = new DiamondSquare();
			HashMap<Integer, List<Integer>> absPos = DiamondSquare.calcAbsolutePositions(params.get(0),
					params.get(1), params.get(2), params.get(3), params.get(4), params.get(5), params.get(6));
			List<Double> areaCount = DiamondSquare.calcNumberOfAreas(params.get(0), params.get(1), params.get(2),
					params.get(3), params.get(4), params.get(5), params.get(6));
			
			for (int i = 0; i < 100; i++) {
				ds.generateMap(getMainController().getControlsController().getMapSize());
				Tuple<Number, Number, Number> tuple = new Tuple<Number, Number, Number>();
				tuple.addFirstValue(1); // The type of parameter combination
				double absPosDeviation = 0;
				double x;
				double xDeviation = 0;
				double y;
				double yDeviation = 0;
				for (int k = 0; k < absPos.size(); k++) {
					List<Integer> coords = absPos.get(k);
					x = absPos.get(k).get(0);
					double x2;
					for (Integer o : coords) {
						x2 = x - o;
						xDeviation += x2;
					}
				}
				xDeviation /= (absPos.size() - 1);
				for (int k = 0; k < absPos.size(); k++) {
					List<Integer> coords = absPos.get(k);
					y = absPos.get(k).get(1);
					double y2;
					for (Integer o : coords) {
						y2 = y - o;
						yDeviation += y2;
					}
				}
				yDeviation /= (absPos.size() - 1);
				absPosDeviation = (xDeviation + yDeviation) / 2;
				tuple.addSecondValue(absPosDeviation);
				tuple.addThirdValue(absPosDeviation);
				tuples.add(tuple);
			}

		} else if (algorithmSelect.getSelectionModel().getSelectedItem() instanceof CellularAutomaton) {
			CellularAutomaton ca = new CellularAutomaton(
					getMainController().getControlsController().getIterations(),
					getMainController().getControlsController().getBirthRule(),
					getMainController().getControlsController().getDeathRule(),
					getMainController().getControlsController().getSurvivalChance());

			Tuple<Number, Number, Number> tuple = new Tuple<Number, Number, Number>();
			for (int j = 0; j < 10; j++) {
				for (int i = 1; i < 11; i++) {
					double size = Math.pow(2, i) + 1;
					ca.generateMap((int)size);
					tuple.addSecondValue(ca.calcRelativeOpenSpace());
					tuple.addThirdValue(ca.calcNumberOfAreas());

					int cap = 8193;
					double c1=0; //outer iteration count
					double c2=0; //inner iteration count
					int previousM = 0;
					for (int m = 1; m < cap; m = m+1*m) {
						int previousN = 0;
						for (int n = 1; n < cap; n = n+1*n) {
							/*
							System.out.println("--");
							if((double)tuple.getSecondValue() > Math.pow(2, 10) / m) {
								System.out.println("con1");
							}
							if((double) tuple.getSecondValue() < Math.pow(2, 10) / (m - previousM)) {
								System.out.println("con2");
							}
							if((double) tuple.getThirdValue() > (Math.pow(2, 10) / 2) / n) {
								System.out.println("con3");
							}
							if((double) tuple.getThirdValue() < (Math.pow(2, 10) / 2) / (n - previousN)) {
								System.out.println("con4");
							}*/
							if ((double)tuple.getSecondValue() > Math.pow(2, 10) / m
									&& (double) tuple.getSecondValue() < Math.pow(2, 10) / (m - previousM)
									&& (double) tuple.getThirdValue() > (Math.pow(2, 10) / 2) / n
									&& (double) tuple.getThirdValue() < (Math.pow(2, 10) / 2) / (n - previousN)) {
								tuple.addFirstValue(m + (n / 10000)); // e.g. 1.01 - 1.1
								tuples.add(tuple);
							}
							previousN = n;
						}
						previousM = m;
					}
				}
			}
			
			for (Tuple t2 : tuples) {
				if(newTuples.containsKey(t2.getFirstValue())) {
					Tuple t3 = newTuples.get(t2.getFirstValue());
					t3.addFirstValue((int)t3.getFirstValue()+(int)t2.getFirstValue());
				} else {
					newTuples.put(Double.valueOf((int)t2.getFirstValue()), t2);
				}
			}
		}
		System.out.println(newTuples);

		return newTuples;
	}

	private void createHeatMap() {
		centerBox.getChildren().clear();

		List<Tuple<Number, Number, Number>> tuples = new ArrayList<Tuple<Number, Number, Number>>();
		HashMap<Double, Tuple<Number, Number, Number>> newTuples = createHeatMapSamples(tuples);

		HeatMap heatMap = new HeatMap(400, 400);
		GraphicsContext gc = heatMap.getGraphicsContext2D();
		ImageView colorScale = heatMap.createColorScale();
		ImageView heatmapImg = heatMap.drawHeatMap(gc, newTuples);

		centerBox.getChildren().add(heatmapImg);
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
