package controller;
import javafx.application.HostServices;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.transform.Scale;
import javafx.stage.Stage;
import logic.CellularAutomaton;
import model.Model;

/**
 * 
 * Controller for the user control elements at the side of the scene.
 *
 * @author Michael Bowes
 *
 */

public class ControlsController extends Controller {

	@FXML
	Button plus;
	@FXML
	Button minus;
	@FXML
	TextField imageWidth;
	@FXML
	TextField imageHeight;
	@FXML
	TextField mapSize;
	
	TextField snowField;
	TextField mountainField;
	TextField forestField;
	TextField grassField;
	TextField beachField;
	TextField shallowWaterField;
	TextField deepWaterField;
	
	
	TextField survivalField;
	TextField birthRuleField;
	TextField deathRuleField;
	TextField iterationsField;

	@FXML
	private GridPane settingsContainer;
	@FXML
	private Button generateButton;

	private void initCellularAutomatonParamFields() {
		Label chooseLabel = new Label("Choose parameters:");
		chooseLabel.setUnderline(true);
		Label birthLabel = new Label("birthRate:");
		Label deathLabel = new Label("deathRate:");
		Label survivalLabel = new Label("survivalChance:");
		Label iterationsLabel = new Label("Iterations:");
		
		survivalField = new TextField();
		deathRuleField = new TextField();
		birthRuleField = new TextField();
		iterationsField = new TextField();
		
		survivalField.setText("0.5");
		survivalField.setMaxWidth(50);
		
		deathRuleField.setText("4");
		deathRuleField.setMaxWidth(50);
		
		birthRuleField.setText("5");
		birthRuleField.setMaxWidth(50);
		
		iterationsField.setText("3");
		iterationsField.setMaxWidth(50);
		
		settingsContainer.add(chooseLabel, 0, 0);
		settingsContainer.add(birthLabel, 0, 1);
		settingsContainer.add(deathLabel, 0, 2);
		settingsContainer.add(survivalLabel, 0, 3);
		settingsContainer.add(iterationsLabel, 0, 4);
		settingsContainer.add(birthRuleField, 1, 1);
		settingsContainer.add(deathRuleField, 1, 2);
		settingsContainer.add(survivalField, 1, 3);
		settingsContainer.add(iterationsField, 1, 4);
	}
	
	/**
	 * 
	 */
	private void initDiamondSquareParamFields() {
		Label chooseLabel = new Label("Choose parameters:");
		chooseLabel.setUnderline(true);
		Label snowLabel = new Label("Snow:");
		Label mountainLabel = new Label("Mountains:");
		Label forestLabel = new Label("Forest:");
		Label grassLabel = new Label("Plains:");
		Label beachLabel = new Label("Beach:");
		Label shallowWaterLabel = new Label("Coast:");
		Label deepWaterLabel = new Label("Ocean:");
		snowField = new TextField();
		mountainField = new TextField();
		grassField = new TextField();
		forestField = new TextField();
		beachField = new TextField();
		deepWaterField = new TextField();
		shallowWaterField = new TextField();
		
		snowField.setText("20");
		snowField.setMaxWidth(50);

		mountainField.setText("20");
		mountainField.setMaxWidth(50);

		forestField.setText("20");
		forestField.setMaxWidth(50);

		grassField.setText("10");
		grassField.setMaxWidth(50);

		beachField.setText("20");
		beachField.setMaxWidth(50);

		shallowWaterField.setText("5");
		shallowWaterField.setMaxWidth(50);

		deepWaterField.setText("5");
		deepWaterField.setMaxWidth(50);

		
		settingsContainer.add(chooseLabel, 0, 0);
		settingsContainer.add(snowLabel, 0, 1);
		settingsContainer.add(snowField, 1, 1);
		settingsContainer.add(mountainLabel, 0, 2);
		settingsContainer.add(mountainField, 1, 2);
		settingsContainer.add(forestLabel, 0, 3);
		settingsContainer.add(forestField, 1, 3);
		settingsContainer.add(grassLabel, 0, 4);
		settingsContainer.add(grassField, 1, 4);
		settingsContainer.add(beachLabel, 0, 5);
		settingsContainer.add(beachField, 1, 5);
		settingsContainer.add(shallowWaterLabel, 0, 6);
		settingsContainer.add(shallowWaterField, 1, 6);
		settingsContainer.add(deepWaterLabel, 0, 7);
		settingsContainer.add(deepWaterField, 1, 7);
	}

	public void loadDiamondSquareTab() {
		settingsContainer.getChildren().clear();
		initDiamondSquareParamFields();
		generateButton = new Button("generate");
		generateButton.setOnAction(event -> {
			int snowParam = Integer.parseInt(snowField.getText());
			int mountainParam = Integer.parseInt(mountainField.getText());
			int forestParam = Integer.parseInt(forestField.getText());
			int grassParam = Integer.parseInt(grassField.getText());
			int beachParam = Integer.parseInt(beachField.getText());
			int coastParam = Integer.parseInt(shallowWaterField.getText());
			int oceanParam = Integer.parseInt(deepWaterField.getText());
			getMainController().getMapController().generateDiamondSquare(snowParam, mountainParam, forestParam,
					grassParam, beachParam, coastParam, oceanParam);
		});
		settingsContainer.add(generateButton, 0, 9);
	}

	public void loadCellularAutomatonTab() {
		settingsContainer.getChildren().clear();
		initCellularAutomatonParamFields();
		generateButton = new Button("generate");
		generateButton.setOnAction(event -> {
			int iterations = Integer.parseInt(iterationsField.getText());
			int birthRule = Integer.parseInt(birthRuleField.getText());
			int deathRule = Integer.parseInt(deathRuleField.getText());
			float survival = Float.parseFloat(survivalField.getText());
			getMainController().getMapController().generateCellularAutomaton(
					iterations, birthRule, deathRule, survival);
			System.out.println("Areas: "+CellularAutomaton.calcNumberOfAreas());
			System.out.println(" Space: "+CellularAutomaton.calcRelativeOpenSpace());
		});
		settingsContainer.add(generateButton, 1, 5);
	}

	@Override
	public void initialize(Stage stage, HostServices hostServices, MainController mainController) {
		super.initialize(stage, hostServices, mainController);
		plus.setText("+");
		minus.setText("-");
		plus.setOnAction(event -> {
			Scale scale = new Scale();
			scale.setX(1.5);
			scale.setY(1.5);
			getMainController().getMapController().getCurrentView().getTransforms().add(scale);
		});
		minus.setOnAction(event -> {
			Scale scale = new Scale();
			scale.setX(0.5);
			scale.setY(0.5);
			getMainController().getMapController().getCurrentView().getTransforms().add(scale);
		});
		loadDiamondSquareTab();
	}
	
	public int getImageWidth() {
		return Integer.parseInt(imageWidth.getText());
	}
	
	public int getImageHeight() {
		return Integer.parseInt(imageHeight.getText());
	}
	
	public int getMapSize() {
		return Integer.parseInt(mapSize.getText());
	}
	
}
