package controller;

import javafx.application.HostServices;
import javafx.fxml.FXML;
import javafx.scene.control.ProgressBar;
import javafx.stage.Stage;

public class ProgressController extends Controller {

	@FXML
	ProgressBar progressBar;
	
	@Override
	public void initialize(Stage stage, HostServices hostServices, MainController mainController) {
		super.initialize(stage, hostServices, mainController);
	}
	
	public ProgressBar getProgressBar() {
		return progressBar;
	}
}
