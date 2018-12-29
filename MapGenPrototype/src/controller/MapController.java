package controller;

import javafx.application.HostServices;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import model.Model;

/**
 * 
 * Controller for the map window.
 */

public class MapController extends Controller {

	@FXML
	private VBox MapContainer;

	@Override
	public void initialize(Stage stage, HostServices hostServices,
	                  MainController mainController, Model model) {
		super.initialize(stage, hostServices, mainController, model);
	}
	
}
