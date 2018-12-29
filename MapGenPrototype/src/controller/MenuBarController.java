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

	private static String VIEW_SESSIONSESSION_FXML = "/fxml/view-session-session.fxml";
	private static String VIEW_SESSIONROOM_FXML = "/fxml/view-session-room.fxml";
	private static String VIEW_TEACHERPERIOD_FXML = "/fxml/view-teacher-period.fxml";
	private static String EDIT_SEMESTER_FXML = "/fxml/edit-semester.fxml";
	private static String EDIT_COURSES_FXML = "/fxml/edit-courses.fxml";
	private static String EDIT_ROOMS_FXML = "/fxml/edit-rooms.fxml";
	private static String EDIT_CHAIRS_FXML = "/fxml/edit-chairs.fxml";
	private static String EDIT_CURRICULA_FXML = "/fxml/edit-curricula.fxml";
	private static String EDIT_GENERATE = "/fxml/edit-generate-algorithm.fxml";
	private static String EXPORT_PDF_FXML = "/fxml/main-menubar-pdfexport.fxml";
	private static String MANUAL_TABLE_FXML = "/fxml/main-manual-table.fxml";
	
	@FXML
	private MenuItem fileNew;
	@FXML
	private MenuItem fileOpen;
	@FXML
	private MenuItem fileSave;
	@FXML
	private MenuItem fileSaveAs;
	@FXML
	private MenuItem exportPdf;
	@FXML
	private MenuItem manualTable;
	@FXML
	private MenuItem fileQuit;

	@FXML
	private MenuItem editSemester;
	@FXML
	private MenuItem editCourses;
	@FXML
	private MenuItem editRooms;
	@FXML
	private MenuItem editChairs;
	@FXML
	private MenuItem editCurricula;
	@FXML
	private MenuItem editGenerate;

	@FXML
	private MenuItem viewSessionSession;
	@FXML
	private MenuItem viewSessionRoom;
	@FXML
	private MenuItem viewTeacherPeriod;

	@FXML
	private MenuItem helpHelp;
	@FXML
	private MenuItem helpAbout;


	@FXML
	protected void initialize() {
		initFileMenu();
		initEditMenu();
		initViewMenu();
		initHelpMenu();
	}

	private void initFileMenu() {
		fileNew.setOnAction(event -> {

		});

		fileOpen.setOnAction(event -> {

		});

		EventHandler<ActionEvent> fileSaveAsAction = event -> {

		};

		fileSave.setOnAction(event -> {

		});

		fileSaveAs.setOnAction(fileSaveAsAction);
		
		manualTable.setOnAction(event -> {
			showFxmlWindow(MANUAL_TABLE_FXML, "Create Table Manually", 290, 200);
		});
		
		exportPdf.setOnAction(event -> {
			showFxmlWindow(EXPORT_PDF_FXML, "Export as PDF", 290, 200);
		});

		fileQuit.setOnAction(event -> {
			getStage().getOnCloseRequest().handle(
					new WindowEvent(getStage().getScene().getWindow(),
							WindowEvent.WINDOW_CLOSE_REQUEST));
		});
	}

	private void initEditMenu() {
		editSemester.setOnAction(event ->
				showFxmlWindow(EDIT_SEMESTER_FXML, "Edit semester data", 550, 510));

		editCourses.setOnAction(event ->
				showFxmlWindow(EDIT_COURSES_FXML, "Edit course data", 600, 550));

		editRooms.setOnAction(event ->
				showFxmlWindow(EDIT_ROOMS_FXML, "Edit rooms", 600, 450));

		editChairs.setOnAction(event ->
				showFxmlWindow(EDIT_CHAIRS_FXML, "Edit chairs", 600, 450));

		editCurricula.setOnAction(event ->
				showFxmlWindow(EDIT_CURRICULA_FXML, "Edit curricula", 600, 450));

		editGenerate.setOnAction(event ->
				showFxmlWindow(EDIT_GENERATE, "Generate timetable", 400, 160));
	}

	private void initViewMenu() {
		
		viewSessionSession.setOnAction(event ->
		showFxmlWindow(VIEW_SESSIONSESSION_FXML, "View Session-Session conflicts", 550, 510));

		viewSessionRoom.setOnAction(event ->
		showFxmlWindow(VIEW_SESSIONROOM_FXML, "View Session-Room conflicts", 550, 510));

		viewTeacherPeriod.setOnAction(event ->
		showFxmlWindow(VIEW_TEACHERPERIOD_FXML, "View Teacher-Period conflicts", 550, 510));
	}

	private void initHelpMenu() {
		helpHelp.setOnAction(event -> showHelpDialog());

		helpAbout.setOnAction(event -> showAboutDialog());
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