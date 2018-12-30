package controller;

import java.util.stream.Collectors;

import javafx.application.HostServices;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import logic.DiamondSquare;
import logic.ProceduralAlgorithm;
import model.Model;

/**
 * 
 * Controls the window for the user to specify the parameters and the algorithm to generate a map.
 *
 */

public class GenerationController extends Controller {

	@FXML
	private VBox settingsContainer;
	
	@FXML
	private ComboBox<ProceduralAlgorithm> algorithmSelection;
	private ObservableList<ProceduralAlgorithm> algorithms = FXCollections.observableArrayList();
	
	//TODO: Parameters
	
	@FXML
	private Button generateButton;
	
	private void initAlgorithms() {
		algorithms.add(new DiamondSquare());
	     
		algorithmSelection.setItems(algorithms);
	}
	
	private void initButton() {
		generateButton.setOnAction(event -> {
			ProceduralAlgorithm alg = algorithmSelection.getSelectionModel().getSelectedItem();
			switch(alg.getName()) {
			case "DiamondSquare":
				getMainController().getMapController().generateDiamondSquare();
				break;
			case "ActorBased":
				getMainController().getMapController().generateActorBased();
				break;
			case "Voronoi":
				getMainController().getMapController().generateVoronoi();
				break;
			}
			Stage stage = (Stage)generateButton.getScene().getWindow();
			stage.close();
		});
	}
	
	@Override
	public void initialize(Stage stage, HostServices hostServices,
	                  MainController mainController, Model model) {
		super.initialize(stage, hostServices, mainController, model);
		generateButton.prefWidth(100);
		initAlgorithms();
		initButton();
	}
	
}
