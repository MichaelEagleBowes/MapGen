package controller;

import java.util.function.UnaryOperator;

import javafx.application.HostServices;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.control.TextFormatter.Change;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import javafx.scene.transform.Scale;
import javafx.stage.Stage;

/**
 * 
 * Controller for the user control elements at the right-hand side of the scene.
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

	UnaryOperator<Change> filter;
	UnaryOperator<Change> decimalFilter;
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

	/**
	 * Initializes the user-input fields specific to the cellular automaton's tab at the start of the application.
	 */
	private void initCellularAutomatonParamFields() {
		Label chooseLabel = new Label("Choose parameters:");
		chooseLabel.setUnderline(true);
		Label birthLabel = new Label("birthRate:");
		Label deathLabel = new Label("deathRate:");
		Label survivalLabel = new Label("survivalChance:");
		Label iterationsLabel = new Label("Iterations:");

		TextFormatter<String> deathRuleFormatter = new TextFormatter<>(filter);
		TextFormatter<String> birthRuleFormatter = new TextFormatter<>(filter);
		TextFormatter<String> iterationsFormatter = new TextFormatter<>(filter);
		TextFormatter<String> survivalFormatter = new TextFormatter<>(decimalFilter);

		survivalField = new TextField();
		deathRuleField = new TextField();
		birthRuleField = new TextField();
		iterationsField = new TextField();

		survivalField.setText("0.5");
		survivalField.setMaxWidth(50);
		survivalField.setTextFormatter(survivalFormatter);

		deathRuleField.setText("4");
		deathRuleField.setMaxWidth(50);
		deathRuleField.setTextFormatter(deathRuleFormatter);

		birthRuleField.setText("5");
		birthRuleField.setMaxWidth(50);
		birthRuleField.setTextFormatter(birthRuleFormatter);

		iterationsField.setText("3");
		iterationsField.setMaxWidth(50);
		iterationsField.setTextFormatter(iterationsFormatter);

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
	 * Initializes the user-input fields specific to the diamond-square's tab at the start of the application.
	 */
	private void initDiamondSquareParamFields() {
		Label chooseLabel = new Label("Choose terrain percentage:");
		chooseLabel.setUnderline(true);
		Label snowLabel = new Label("Snow:");
		Label mountainLabel = new Label("Mountains:");
		Label forestLabel = new Label("Forest:");
		Label grassLabel = new Label("Plains:");
		Label beachLabel = new Label("Beach:");
		Label shallowWaterLabel = new Label("Coast:");
		Label deepWaterLabel = new Label("Ocean:");

		TextFormatter<String> snowFormatter = new TextFormatter<>(filter);
		TextFormatter<String> mountainFormatter = new TextFormatter<>(filter);
		TextFormatter<String> grassFormatter = new TextFormatter<>(filter);
		TextFormatter<String> forestFormatter = new TextFormatter<>(filter);
		TextFormatter<String> beachFormatter = new TextFormatter<>(filter);
		TextFormatter<String> deepWaterFormatter = new TextFormatter<>(filter);
		TextFormatter<String> shallowWaterFormatter = new TextFormatter<>(filter);

		snowField = new TextField();
		mountainField = new TextField();
		grassField = new TextField();
		forestField = new TextField();
		beachField = new TextField();
		deepWaterField = new TextField();
		shallowWaterField = new TextField();

		snowField.setText("20");
		snowField.setMaxWidth(40);
		snowField.setTextFormatter(snowFormatter);

		mountainField.setText("20");
		mountainField.setMaxWidth(40);
		mountainField.setTextFormatter(mountainFormatter);

		forestField.setText("20");
		forestField.setMaxWidth(40);
		forestField.setTextFormatter(forestFormatter);

		grassField.setText("10");
		grassField.setMaxWidth(40);
		grassField.setTextFormatter(grassFormatter);

		beachField.setText("20");
		beachField.setMaxWidth(40);
		beachField.setTextFormatter(beachFormatter);

		shallowWaterField.setText("5");
		shallowWaterField.setMaxWidth(40);
		shallowWaterField.setTextFormatter(shallowWaterFormatter);

		deepWaterField.setText("5");
		deepWaterField.setMaxWidth(40);
		deepWaterField.setTextFormatter(deepWaterFormatter);

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
		settingsContainer.setPadding(new Insets(10, 10, 10, 10));
	}
	
	/**
	 * Updates the content of the selected tab in the {@link TabPane} upon interaction.
	 * 
	 * @param tabIndex Index of the selected tab; 0 = DS, 1 = CA.
	 */
	public void loadTab(int tabIndex) {
		settingsContainer.getChildren().clear();
		generateButton = new Button("Generate");
		generateButton.setPrefWidth(120);
		generateButton.setPrefHeight(120);
		generateButton.setFont(new Font(20));
		
		if(tabIndex == 0) {
			initDiamondSquareParamFields();
			generateButton.setOnAction(event -> {
				int snowParam = Integer.parseInt(snowField.getText());
				int mountainParam = Integer.parseInt(mountainField.getText());
				int forestParam = Integer.parseInt(forestField.getText());
				int grassParam = Integer.parseInt(grassField.getText());
				int beachParam = Integer.parseInt(beachField.getText());
				int coastParam = Integer.parseInt(shallowWaterField.getText());
				int oceanParam = Integer.parseInt(deepWaterField.getText());
				getMainController().getMapController().generateDiamondSquare(oceanParam, coastParam, beachParam, grassParam,
						forestParam, snowParam, mountainParam);
			});
		} else if (tabIndex == 1) {
			initCellularAutomatonParamFields();
			generateButton.setOnAction(event -> {
				int iterations = Integer.parseInt(iterationsField.getText());
				int birthRule = Integer.parseInt(birthRuleField.getText());
				int deathRule = Integer.parseInt(deathRuleField.getText());
				float survival = 0f;
				try {
					survival = Float.parseFloat(survivalField.getText());
				} catch (NumberFormatException e) {
					Util.informationAlert("Wrong Format", "Please enter a valid decimal for survivalChance.");
				}
				getMainController().getMapController().generateCellularAutomaton(iterations, birthRule, deathRule,
						survival);
			});
		}
		
		
		settingsContainer.add(generateButton, 0, 9);
		settingsContainer.setPadding(new Insets(10, 10, 10, 10));
	}

	@Override
	public void initialize(Stage stage, HostServices hostServices, MainController mainController) {
		super.initialize(stage, hostServices, mainController);
		plus.setText("+");
		minus.setText("-");
		plus.setOnAction(event -> {
			zoomMap(1.5f);
		});
		minus.setOnAction(event -> {
			zoomMap(0.5f);
		});
		filter = change -> {
			String text = change.getText();

			if (text.matches("[0-9]*")) {
				return change;
			}

			return null;
		};
		decimalFilter = change -> {
			String text = change.getText();
			if (text.matches("\\d*(\\.\\d*)?")) {
				return change;
			}

			return null;
		};
		TextFormatter<String> imageWidthFormatter = new TextFormatter<>(filter);
		TextFormatter<String> imageHeightFormatter = new TextFormatter<>(filter);
		TextFormatter<String> mapSizeFormatter = new TextFormatter<>(filter);
		imageWidth.setTextFormatter(imageWidthFormatter);
		imageHeight.setTextFormatter(imageHeightFormatter);
		mapSize.setTextFormatter(mapSizeFormatter);
		loadTab(0);
	}
	
	/**
	 * 
	 * Applies a scaling transformation to the image of the current imageView.
	 * 
	 * @param scaleFactor The factor to scale the image.
	 */
	private void zoomMap(float scaleFactor) {
		if (getMainController().getMapController().getTabPane().getSelectionModel().getSelectedIndex() == 0) {
			if (getMainController().getMapController().getDiamondSquare().mapPresent()) {
				Scale scale = new Scale();
				scale.setX(scaleFactor);
				scale.setY(scaleFactor);
				getMainController().getMapController().getDiamondSquareView().getTransforms().add(scale);
			}
		} else if (getMainController().getMapController().getTabPane().getSelectionModel()
				.getSelectedIndex() == 1) {
			if (getMainController().getMapController().getCellularAutomaton().mapPresent()) {
				Scale scale = new Scale();
				scale.setX(scaleFactor);
				scale.setY(scaleFactor);
				getMainController().getMapController().getCellularAutomatonView().getTransforms().add(scale);
			}
		}
	}
	
	public int getBirthRule() {
		return Integer.parseInt(birthRuleField.getText());
	}

	public int getDeathRule() {
		return Integer.parseInt(deathRuleField.getText());
	}

	public int getIterations() {
		return Integer.parseInt(iterationsField.getText());
	}

	public float getSurvivalChance() {
		return Float.parseFloat(survivalField.getText());
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
