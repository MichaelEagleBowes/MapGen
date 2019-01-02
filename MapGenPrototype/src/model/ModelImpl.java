package model;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.nio.file.Path;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.Flow;
import java.util.concurrent.Flow.Subscriber;
import java.util.concurrent.SubmissionPublisher;
import logic.ProceduralAlgorithm;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;

public class ModelImpl implements Model {

	private static final String WCTTT = "WIAI Course Timetabling Tool";
	private static final String SEMESTER_LOADED = "Semester loaded successfully";
	private static final String SEMESTER_UPDATED = "Semester data were updated";
	private static final String COURSES_UPDATED = "Course data were updated";
	private static final String ROOMS_UPDATED = "Room data were updated";
	private static final String CHAIRS_UPDATED = "Chair data were updated";
	private static final String CURRICULA_UPDATED = "Curriculum data were updated";
	private static final String TIMETABLES_UPDATED = "Timetable data were updated";
	public static final String SELECTED_ASSIGNMENT_PROPERTY = "selectedAssignment";
	public static final String SELECTED_TIMETABLE_PROPERTY = "selectedTimetable";

	private HashMap maps = new HashMap<String, int[][]>();
	private Path xmlPath;
	private BooleanProperty changed;
	private SubmissionPublisher<Boolean> semesterChangesNotifier =
			new SubmissionPublisher<>();
	private SubmissionPublisher<Boolean> timetablesChangesNotifier =
			new SubmissionPublisher<>();
	private StringProperty title = new SimpleStringProperty();

	private StringProperty lastAction = new SimpleStringProperty();
	private StringProperty unsavedChanges = new SimpleStringProperty();
	private StringProperty stateText = new SimpleStringProperty();
	private final PropertyChangeSupport state = new PropertyChangeSupport(this);

	public ModelImpl() {
		Platform.runLater(() -> stateText.bind(
				Bindings.concat(lastAction, " - ", unsavedChanges)));
		changed = new SimpleBooleanProperty(true); // to trigger change listener
		changed.addListener((observable, oldValue, newValue) -> {
			if (newValue) {
				unsavedChanges.setValue("Unsaved changes");
			} else {
				unsavedChanges.setValue("No unsaved changes");
			}
		});
		
	}

	@Override
	public void addPropertyChangeListener(String propertyName, PropertyChangeListener listener) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Optional<Path> getXmlPath() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setXmlPath(Path xmlPath) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public BooleanProperty isChanged() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setChanged(boolean changed) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public StringProperty getTitleProperty() {
		return title;
	}

	@Override
	public StringProperty getStateTextProperty() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void subscribeSemesterChanges(Subscriber<? super Boolean> subscriber) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void subscribeTimetablesChanges(Subscriber<? super Boolean> subscriber) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void close() {
		// TODO Auto-generated method stub
		
	}

}