package controller;

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

/**
 * 
 * Controller for the menu bar.
 * 
 * @author Michael Bowes
 * 
 */
public class MenuBarController extends Controller {

	private static String MENU_BAR_FXML = "/resources/fxml/menu-bar.fxml";
	private static String MAP_FXML = "/resources/fxml/map.fxml";
	private static String STATISTICS_FXML = "/resources/statistics.fxml";

	@FXML
	private MenuItem fileOpen;
	@FXML
	private MenuItem fileSave;
	@FXML
	private MenuItem fileSaveAs;
	@FXML
	private MenuItem fileQuit;

	@FXML
	private MenuItem statisticsExpressivity;

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
			getStage().getOnCloseRequest()
					.handle(new WindowEvent(getStage().getScene().getWindow(), WindowEvent.WINDOW_CLOSE_REQUEST));
		});
	}

	private void initHelpMenu() {
		helpHelp.setOnAction(event -> openHelpDialog());

		helpAbout.setOnAction(event -> openAboutDialog());
	}

	private void initStatisticsMenu() {
		statisticsExpressivity.setOnAction(
				event -> openWindow(STATISTICS_FXML, "Statistics: Expressivity of Maps", 550, 520));
	}

	private void openWindow(String fxmlPath, String title, int minWidth, int minHeight) {
		Stage stage = Util.loadFxml(fxmlPath, null, null, getMainController());
		stage.getScene().getStylesheets().add(getClass().getResource("barChart.css").toExternalForm());
		stage.getScene().getStylesheets().add(getClass().getResource("scatterChartIcon.css").toExternalForm());
		Util.showStage(stage, title, minWidth, minHeight);
	}

	private void openHelpDialog() {
		Alert alert = new Alert(Alert.AlertType.INFORMATION);
		alert.setTitle("Help");
		alert.setHeaderText("Help");
		VBox mainVBox = new VBox();
		mainVBox.setSpacing(15);

		VBox thesisVBox = new VBox();
		Text text = new Text("For more details on the prototype and the thesis, visit " + "the GitHub Page.");
		text.setWrappingWidth(400);
		thesisVBox.getChildren().add(text);
		Hyperlink gitPage = new Hyperlink("Show GitHub Page");
		gitPage.setOnAction(event -> getHostServices().showDocument("https://github.com/MichaelEagleBowes/MapGen"));
		thesisVBox.getChildren().add(gitPage);
		mainVBox.getChildren().add(thesisVBox);

		alert.getDialogPane().setContent(mainVBox);
		alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
		alert.getDialogPane().setPrefWidth(400);
		alert.showAndWait();
	}

	private void openAboutDialog() {
		Alert alert = new Alert(Alert.AlertType.INFORMATION);
		alert.setTitle("About MapGen");
		alert.setHeaderText("Controllable Procedural Map Generator");
		alert.setContentText("MapGen is an " + "application that demonstrates the different effects of various "
				+ " procedural content generation algorithms by producing user-specified maps. "
				+ " This project is a prorotype created for the bachelor thesis of Michael Bowes "
				+ " at the University of Bamberg, Germany, for the chair of Media Information Technology."
				+ System.lineSeparator() + System.lineSeparator() + "Version: "
				+ getClass().getPackage().getImplementationVersion() + System.lineSeparator() + System.lineSeparator()
				+ "Copyright © 2019 Michael Bowes" + System.lineSeparator() + System.lineSeparator()
				+ "See the MIT Massachusetts Institute of Technology License for details.");
		alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
		alert.getDialogPane().setPrefWidth(400);
		alert.showAndWait();
	}

	@Override
	public void initialize(Stage stage, HostServices hostServices, MainController mainController) {
		super.initialize(stage, hostServices, mainController);
		// fileSave.disableProperty().bind(getModel().isChanged().not());
	}
}