package controller;

import javafx.application.HostServices;
import javafx.stage.Stage;
import model.Model;

/**
 * 
 * An abstract controller class for all methods that controllers ought to have.
 */
public abstract class Controller {

	private Stage stage;
	private Model model;
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

	Model getModel() {
		return model;
	}

	public void initialize(Stage stage, HostServices hostServices,
	                  MainController mainController, Model model) {
		this.stage = stage;
		this.hostServices = hostServices;
		this.mainController = mainController;
		this.model = model;
	}
}