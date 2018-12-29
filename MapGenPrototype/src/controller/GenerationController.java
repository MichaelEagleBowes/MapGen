package controller;

import javafx.application.HostServices;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import logic.ProceduralAlgorithm;
import model.Model;

/**
 * 
 * Controls the window where the user specifies the procedural algorithm and its parameters
 * for generating a map.
 *
 */

public class GenerationController extends Controller {

	@FXML
	private VBox settingsContainer;
	
	@FXML
	private ComboBox<ProceduralAlgorithm> algorithmSelection;
	
	//TODO: Parameters
	
	@FXML
	private Button generateButton;
	
	@Override
	public void initialize(Stage stage, HostServices hostServices,
	                  MainController mainController, Model model) {
		super.initialize(stage, hostServices, mainController, model);
		generateButton.prefWidth(100);
	}
	
}
