package controller;

import java.io.File;
import java.util.Optional;

import javafx.application.HostServices;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import model.Model;

import java.util.Optional;

/**
 * Controller for the menu bar.
 */
public class MenuBarController extends Controller {

	private static String MENU_BAR_FXML = "/resources/menubar.fxml";
	private static String MAP_FXML = "/resources/map.fxml";
	
	@FXML
	private MenuItem fileOpen;
	@FXML
	private MenuItem fileSave;
	@FXML
	private MenuItem fileSaveAs;
	@FXML
	private MenuItem fileQuit;
	
	@FXML
	private MenuItem statisticsControllability;
	@FXML
	private MenuItem statisticsPattern;

	@FXML
	private MenuItem helpHelp;
	@FXML
	private MenuItem helpAbout;


	@FXML
	protected void initialize() {
		initFileMenu();
		initHelpMenu();
		initStatisticsMenu();
	}

	private void initFileMenu() {

		fileOpen.setOnAction(event -> {

		});

		EventHandler<ActionEvent> fileSaveAsAction = event -> {

		};

		fileSave.setOnAction(event -> {

		});

		fileSaveAs.setOnAction(fileSaveAsAction);

		fileQuit.setOnAction(event -> {
			getStage().getOnCloseRequest().handle(
					new WindowEvent(getStage().getScene().getWindow(),
							WindowEvent.WINDOW_CLOSE_REQUEST));
		});
	}

	private void initHelpMenu() {
		helpHelp.setOnAction(event -> showHelpDialog());

		helpAbout.setOnAction(event -> showAboutDialog());
	}
	
	private void initStatisticsMenu() {
		statisticsControllability.setOnAction(event -> showHelpDialog());

		statisticsPattern.setOnAction(event -> showAboutDialog());
	}

	private void showHelpDialog() {
		Alert alert = new Alert(Alert.AlertType.INFORMATION);
		alert.setTitle("Help");
		alert.setHeaderText("Help");
		VBox mainVBox = new VBox();
		mainVBox.setSpacing(15);

		VBox thesisVBox = new VBox();
		Text text = new Text("For more details on the prototype and the thesis, visit " +
			"the GitHub Page.");
		text.setWrappingWidth(400);
		thesisVBox.getChildren().add(text);
		Hyperlink gitPage = new Hyperlink("Show GitHub Page");
		gitPage.setOnAction(event -> getHostServices().showDocument(
			"https://github.com/MichaelEagleBowes/MapGen"));
		thesisVBox.getChildren().add(gitPage);
		mainVBox.getChildren().add(thesisVBox);

		alert.getDialogPane().setContent(mainVBox);
		alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
		alert.getDialogPane().setPrefWidth(400);
		alert.showAndWait();
	}
	
	private void showAboutDialog() {
		Alert alert = new Alert(Alert.AlertType.INFORMATION);
		alert.setTitle("About MapGen");
		alert.setHeaderText("Controllable Procedural Map Generator");
		alert.setContentText("MapGen is an " +
			"application that demonstrates the different effects of various " +
			" procedural content generation algorithms by producing user-specified maps. " +
			" This project is a prorotype created for the bachelor thesis of Michael Bowes " +
			" at the University of Bamberg, Germany, for the chair of Media Information Technology." +
			System.lineSeparator() + System.lineSeparator() +
			"Version: " + getClass().getPackage().getImplementationVersion() +
			System.lineSeparator() + System.lineSeparator() +
			"Copyright © 2019 Michael Bowes" + System.lineSeparator() +
			System.lineSeparator() +
			"See the MIT Massachusetts Institute of Technology License for details.");
		alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
		alert.getDialogPane().setPrefWidth(400);
		alert.showAndWait();
	}

	private void showFxmlWindow(String fxmlPath, String title, int minWidth,
	                    int minHeight) {
		Stage stage = Util.loadFxml(fxmlPath, null, null, getModel(),
				getMainController());
		Util.showStage(stage, title, minWidth, minHeight);
	}

	@Override
	public void initialize(Stage stage, HostServices hostServices,
	                  MainController mainController, Model model) {
		super.initialize(stage, hostServices, mainController, model);
		//fileSave.disableProperty().bind(getModel().isChanged().not());
	}
}