package controller;

import javafx.application.HostServices;
import javafx.stage.Stage;

/**
 * 
 * An abstract controller class for all methods that controllers ought to have.
 * 
 * @author Michael Bowes
 * 
 */
public abstract class Controller {

	private Stage stage;
	private MainController mainController;
	private HostServices hostServices;

	Stage getStage() {
		return stage;
	}

	HostServices getHostServices() {
		return hostServices;
	}

	MainController getMainController() {
		return mainController;
	}

	public void initialize(Stage stage, HostServices hostServices,
	                  MainController mainController) {
		this.stage = stage;
		this.hostServices = hostServices;
		this.mainController = mainController;
	}
}