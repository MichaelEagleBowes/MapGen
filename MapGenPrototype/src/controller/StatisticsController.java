package controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javafx.application.HostServices;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Orientation;
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
	private ComboBox<MapType> genreSelect;
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
	private void fillBarChart(BarChart<String, Double> barChart) {
		int[][] selectedMap = maps.get(algorithmSelect.getSelectionModel().getSelectedItem());
		//double areas = DiamondSquare.calcNumberOfAreas();
		List<Number> params = getMainController().getMapController().getParameters();
		List<List<Integer>> absPos = DiamondSquare.calcAbsolutePositions(
				(int)params.get(0), (double)params.get(1), (double)params.get(2),
				(double)params.get(3), (double)params.get(4), (double)params.get(5), 
				(double)params.get(6), (double)params.get(7));
		double relPos = DiamondSquare.calcRelativePositions();
		
		switch (genreSelect.getSelectionModel().getSelectedItem()) {
		case RTS:
			
			break;
		case OpenWorld:
			
			break;
		}
		
		XYChart.Series<String, Double> series1 = new XYChart.Series<>();
		series1.setName("Snow");
		series1.getData().add(new XYChart.Data<>("Separate Areas", 1d));
		series1.getData().add(new XYChart.Data<>("Spread", 3d));
		series1.getData().add(new XYChart.Data<>("Distances", 5d));

		XYChart.Series<String, Double> series2 = new XYChart.Series<>();
		series2.setName("Mountain");
		series2.getData().add(new XYChart.Data<>("Separate Areas", 1d));
		series2.getData().add(new XYChart.Data<>("Spread", 5d));
		series2.getData().add(new XYChart.Data<>("Distances", 5d));

		XYChart.Series<String, Double> series3 = new XYChart.Series<>();
		series3.setName("Ford");
		series3.getData().add(new XYChart.Data<>("Separate Areas", 1d));
		series3.getData().add(new XYChart.Data<>("Spread", 5d));
		series3.getData().add(new XYChart.Data<>("Distances", 5d));

		barChart.getData().addAll(series1, series2, series3);
	}

	private void initComboBoxes() {
		ObservableList<ProceduralAlgorithm> algorithmList = FXCollections.observableArrayList();
		algorithmList.add(getMainController().getMapController().getCellularAutomaton());
		algorithmList.add(getMainController().getMapController().getDiamondSquare());
		algorithmSelect.setItems(algorithmList);

		ObservableList<MapType> mapList = FXCollections.observableArrayList();
		mapList.add(MapType.OpenWorld);
		mapList.add(MapType.RTS);
		genreSelect.setItems(mapList);
	}

	private void createHistogram() {
		centerBox.getChildren().clear();
		CategoryAxis xAxis = new CategoryAxis();
		xAxis.setLabel("Neighbourhood Structures");

		NumberAxis yAxis = new NumberAxis();
		yAxis.setLabel("Use Count");

		expressivityBarChart = new BarChart(xAxis, yAxis);
		fillBarChart(expressivityBarChart);
		Separator vertSeparator = new Separator();
		vertSeparator.setOrientation(Orientation.VERTICAL);
		centerBox.getChildren().add(expressivityBarChart);
		centerBox.getChildren().add(vertSeparator);
	}

	private void createHeatMap() {
		centerBox.getChildren().clear();
		//System.out.println("Areas: "+CellularAutomaton.calcNumberOfAreas()); 1 bis (mapSize^2)/2
		//System.out.println(" Space: "+CellularAutomaton.calcRelativeOpenSpace()); 0 bis mapSize^2
		
		HeatMap heatMap = new HeatMap(300, 250);
        GraphicsContext gc = heatMap.getGraphicsContext2D();
        double[] firstDim = {0,30,100};
        double[] secondDim = {10,50,66};
        heatMap.createColorScale();
        ImageView imageView = heatMap.drawHeatMap(gc, firstDim, secondDim);
        
		double areas = ((CellularAutomaton) algorithmSelect.getSelectionModel().getSelectedItem()).calcNumberOfAreas();
		double relativeSpace = ((CellularAutomaton) algorithmSelect.getSelectionModel().getSelectedItem())
				.calcRelativeOpenSpace();
		
		centerBox.getChildren().add(imageView);
		Separator vertSeparator = new Separator();
		vertSeparator.setOrientation(Orientation.VERTICAL);
		centerBox.getChildren().add(vertSeparator);
	}

	private void createExpressiveRangeChart() {
		System.out.println(algorithmSelect.getSelectionModel().getSelectedItem().getClass());
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
