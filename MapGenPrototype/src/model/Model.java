package model;

import java.beans.PropertyChangeListener;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.Flow;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.ObservableList;
import logic.ProceduralAlgorithm;

public interface Model extends Map {

	void setAlgorithms();
	
	HashMap<String, ProceduralAlgorithm> getAlgorithms();
	
	/**
	 * Getter for the currently selected {@link Timetable}.
	 * @return
	 */
	String getSelectedTimetable();
	
	/**
	 * Setter for the currently selected {@link Timetable}.
	 * 
	 * @param selectedTimetable
	 */
	void setSelectedTimetable(String selectedTimetable);
	
	/**
	 * 	Setter for the currently selected {@link TimetableAssignment} in the currently selected
	 * {@link Timetable}.
	 * 
	 * @param selectedAssignment
	 */
	void setSelectedAssignment(String selectedAssignment);
	
	/**
	 * 	Getter for the currently selected {@link TimetableAssignment} in the currently selected
	 * {@link Timetable}.
	 * 
	 * @return
	 */
	String getSelectedAssignment();
	/**
	 * Adds a change listener to the model.
	 * 
	 * @param propertyName - property that needs to be observed.
	 * @param listener - Change listener.
	 */
	void addPropertyChangeListener(String propertyName, PropertyChangeListener listener);
	/**
	 * Getter for the path of the currently loaded semester.
	 *
	 * @return an Optional containing the path if the current semester is loaded
	 * from a file, otherwise an empty {@code Optional}.
	 */
	Optional<Path> getXmlPath();

	/**
	 * Setter for the path to the XML file of the currently loaded semester.
	 *
	 * @param xmlPath the new path, can be {@code null}.
	 */
	void setXmlPath(Path xmlPath);

	/**
	 * Indicates whether the currently loaded semester has changes that are not
	 * already saved to the respective XML file.
	 *
	 * @return whether there are unsaved changes.
	 */
	BooleanProperty isChanged();

	/**
	 * Setter for the indicator of unsaved changes.
	 *
	 * @param changed the new status of unsaved changes.
	 */
	void setChanged(boolean changed);

	/**
	 * Getter for the currently loaded semester.
	 *
	 * @return the currently loaded semester.
	 */
	String getSemester();

	/**
	 * Updates the display for saving changes after the timetable content was changed.
	 */
	void updateTimetableChange(String timetable);

	/**
	 * Setter for the loaded semester.
	 *
	 * @param xmlPath the path to the XML file of the new semester.
	 * @param semester the new semester.
	 */
	void setSemester(Path xmlPath, String semester);

	/**
	 * Getter for the list of teachers of the semester.
	 *
	 * @return the list of teachers of the semester.
	 */
	ObservableList<String> getTeachers();

	/**
	 * Getter for the list of periods of the semester.
	 *
	 * @return the list of periods of the semester.
	 */
	List<String> getPeriods();

	/**
	 * Getter for the window title text property.
	 *
	 * @return the window title text property.
	 */
	StringProperty getTitleProperty();

	/**
	 * Getter for the program state text property.
	 *
	 * @return the program state text property.
	 */
	StringProperty getStateTextProperty();

	/**
	 * Registers a subscriber to changes on the semester data. The boolean
	 * indicates whether a full reload is necessary or not.
	 *
	 * @param subscriber the subscriber that should be registered.
	 */
	void subscribeSemesterChanges(Flow.Subscriber<? super Boolean> subscriber);

	/**
	 * Registers a subscriber to changes on the timetable data. The boolean
	 * indicates whether a full reload is necessary or not.
	 *
	 * @param subscriber the subscriber that should be registered.
	 */
	void subscribeTimetablesChanges(
				Flow.Subscriber<? super Boolean> subscriber);

	/**
	 * Gracefully closes the model, should be called if the program is exited.
	 */
	void close();
}