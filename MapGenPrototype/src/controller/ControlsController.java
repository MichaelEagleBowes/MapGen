package controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javafx.application.HostServices;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.scene.transform.Scale;
import javafx.stage.Stage;
import logic.DiamondSquare;
import logic.ProceduralAlgorithm;
import model.Model;

public class ControlsController extends Controller {

	@FXML
	Button plus;
	@FXML
	Button minus;
	@FXML
	TextField snowField;
	@FXML
	TextField mountainField;
	@FXML
	TextField forestField;
	@FXML
	TextField grassField;
	@FXML
	TextField beachField;
	@FXML
	TextField shallowWaterField;
	@FXML
	TextField deepWaterField;
	
	@FXML
	private VBox settingsContainer;
	@FXML
	private Button generateButton;
	
	private void initParamFields() {
		snowField.setText("20");
		snowField.setMaxWidth(50);
		
		mountainField.setText("20");
		mountainField.setMaxWidth(50);
		
		forestField.setText("20");
		forestField.setMaxWidth(50);
		
		grassField.setText("20");
		grassField.setMaxWidth(50);
		
		beachField.setText("20");
		beachField.setMaxWidth(50);
		
		shallowWaterField.setText("20");
		shallowWaterField.setMaxWidth(50);
		
		deepWaterField.setText("20");
		deepWaterField.setMaxWidth(50);
		
		
	}
	
	private void initButton() {
		generateButton.setOnAction(event -> {
			int snowParam = Integer.parseInt(snowField.getText());
			int mountainParam = Integer.parseInt(mountainField.getText());
			int forestParam = Integer.parseInt(forestField.getText());
			int grassParam = Integer.parseInt(grassField.getText());
			int beachParam = Integer.parseInt(beachField.getText());
			int coastParam = Integer.parseInt(shallowWaterField.getText());
			int oceanParam = Integer.parseInt(deepWaterField.getText());
				getMainController().getMapController().generateDiamondSquare(snowParam, mountainParam, forestParam, grassParam, 
						beachParam, coastParam, oceanParam);
		});
	}
	
	@Override
	public void initialize(Stage stage, HostServices hostServices,
	                  MainController mainController, Model model) {
		super.initialize(stage, hostServices, mainController, model);
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
		initButton();
		initParamFields();
	}
}
