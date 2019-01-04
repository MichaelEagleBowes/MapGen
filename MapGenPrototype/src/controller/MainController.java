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
	@FXML
	private Label stateInfo;


	@Override
	public void initialize(Stage stage, HostServices hostServices,
	                  MainController mainController) {
		super.initialize(stage, hostServices, mainController);
		setCloseConfirmation();
		menuBarController.initialize(stage, hostServices, this);
		mapController.initialize(stage, hostServices, this);
		userControlsController.initialize(stage, hostServices, this);
		//getModel().getStateTextProperty().addListener((observable, oldValue, newValue) -> updateStateInfo(newValue));
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