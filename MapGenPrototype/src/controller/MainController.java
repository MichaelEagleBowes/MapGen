package controller;

import javafx.application.HostServices;
import javafx.fxml.FXML;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

/**
 * 
 * Main controller that initializes and manages other controllers, as well as the main scene.
 * 
 * @author Michael Bowes
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
	private ControlsController userControlsController;


	@Override
	public void initialize(Stage stage, HostServices hostServices,
	                  MainController mainController) {
		super.initialize(stage, hostServices, mainController);
		setCloseConfirmation();
		menuBarController.initialize(stage, hostServices, this);
		mapController.initialize(stage, hostServices, this);
		userControlsController.initialize(stage, hostServices, this);
	}

	MenuBarController getMenuBarController() {
		return menuBarController;
	}

	MapController getMapController() {
		return mapController;
	}
	
	ControlsController getControlsController() {
		return userControlsController;
	}
	
	BorderPane getMainPane() {
		return mainPane;
	}

	private void setCloseConfirmation() {
		getStage().setOnCloseRequest(event -> {
				getStage().close();
		});
	}

}