package controller;

import javafx.application.HostServices;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.transform.Scale;
import javafx.stage.Stage;
import model.Model;

public class ControlsController extends Controller {

	@FXML
	Button plus;
	@FXML
	Button minus;
	
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
	}
}
