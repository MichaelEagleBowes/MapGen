package controller;

import javafx.application.HostServices;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import model.Model;

/**
 * 
 * Main controller that initializes other controllers and handles the logic for the main application.
 * 
 */
public class MainController extends Controller {

	@FXML
	private BorderPane mainPane;
	@FXML
	private MenuBarController menuBarController;
	@FXML
	private MapController mapController;
	@FXML
	private Label stateInfo;


	@Override
	public void initialize(Stage stage, HostServices hostServices,
	                  MainController mainController, Model model) {
		super.initialize(stage, hostServices, mainController, model);
		setCloseConfirmation();
		menuBarController.initialize(stage, hostServices, this, model);
		mapController.initialize(stage, hostServices, this, model);
		//getModel().getStateTextProperty().addListener((observable, oldValue, newValue) -> updateStateInfo(newValue));
	}

	MenuBarController getMenuBarController() {
		return menuBarController;
	}

	MapController getMapController() {
		return mapController;
	}
	
	BorderPane getMainPane() {
		return mainPane;
	}

	private void updateStateInfo(String newValue) {
		Platform.runLater(() -> stateInfo.setText(newValue));
	}

	private void setCloseConfirmation() {
		getStage().setOnCloseRequest(event -> {
				getStage().close();
				//getModel().close();
		});
	}

}