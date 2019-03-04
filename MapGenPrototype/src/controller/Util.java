package controller;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.nio.ByteBuffer;
import java.util.NoSuchElementException;
import java.util.Optional;

import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextInputDialog;
import javafx.scene.image.Image;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;

/**
 * 
 * Utility class for functionality available to and used by controller classes.
 * 
 */
public class Util {

	/**
	 * Converts a BufferedImage to a FXImage.
	 * 
	 * @param bufferedImage BufferedImage to convert.
	 * @return
	 */
	static Image convertBufferedImage(BufferedImage bufferedImage) {
		WritableImage wr = null;
		if (bufferedImage != null) {
			wr = new WritableImage(bufferedImage.getWidth(), bufferedImage.getHeight());
			PixelWriter pw = wr.getPixelWriter();
			for (int x = 0; x < bufferedImage.getWidth(); x++) {
				for (int y = 0; y < bufferedImage.getHeight(); y++) {
					pw.setArgb(x, y, bufferedImage.getRGB(x, y));
				}
			}
		}
		return wr;
	}

	static void exceptionAlert(Throwable ex) {
		Alert alert = new Alert(Alert.AlertType.ERROR);
		alert.setTitle("Error");
		alert.setHeaderText("An unexpected error has occurred");
		alert.setContentText(ex.getMessage());
		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw);
		ex.printStackTrace(pw);
		String exceptionText = sw.toString();
		Label label = new Label("The exception stacktrace was:");
		TextArea textArea = new TextArea(exceptionText);
		textArea.setEditable(false);
		textArea.setWrapText(true);
		textArea.setMaxWidth(Double.MAX_VALUE);
		textArea.setMaxHeight(Double.MAX_VALUE);
		GridPane.setVgrow(textArea, Priority.ALWAYS);
		GridPane.setHgrow(textArea, Priority.ALWAYS);
		GridPane expContent = new GridPane();
		expContent.setMaxWidth(Double.MAX_VALUE);
		expContent.add(label, 0, 0);
		expContent.add(textArea, 0, 1);
		alert.getDialogPane().setExpandableContent(expContent);
		alert.showAndWait();
	}

	static boolean confirmationAlert(String header, String question) {
		Alert alert = new Alert(Alert.AlertType.WARNING);
		alert.setTitle("Warning");
		alert.setHeaderText(header);
		alert.setContentText(question);
		alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);

		alert.getButtonTypes().setAll(ButtonType.YES, ButtonType.CANCEL);

		Optional<ButtonType> result = alert.showAndWait();
		return result.map(button -> button == ButtonType.YES).orElse(false);
	}

	static void errorAlert(String header, String message) {
		Alert alert = new Alert(Alert.AlertType.ERROR);
		alert.setTitle("Error");
		alert.setHeaderText(header);
		alert.setContentText(message);
		alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
		alert.showAndWait();
	}

	static void informationAlert(String header, String message) {
		Alert alert = new Alert(Alert.AlertType.INFORMATION);
		alert.setTitle("Information");
		alert.setHeaderText(header);
		alert.setContentText(message);
		alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
		alert.showAndWait();
	}

	static Optional<String> textInputDialog(String initial, String title, String header, String contentText) {
		TextInputDialog dialog = new TextInputDialog(initial);
		dialog.setTitle(title);
		dialog.setHeaderText(header);
		dialog.setContentText(contentText);
		return dialog.showAndWait();
	}

	static Stage loadFxml(String fxmlPath, Controller controller, Stage stage, MainController mainController) {
		FXMLLoader loader = new FXMLLoader(Util.class.getResource(fxmlPath));
		if (controller != null) {
			loader.setController(controller);
		}
		try {
			Parent root = loader.load();
			Scene scene = new Scene(root);
			Stage actualStage;
			if (stage == null) {
				actualStage = new Stage();
				actualStage.initModality(Modality.APPLICATION_MODAL);
			} else {
				actualStage = stage;
			}
			actualStage.setScene(scene);
			Controller assignedController = loader.getController();
			assignedController.initialize(actualStage, mainController.getHostServices(), mainController);
			return actualStage;
		} catch (IOException e) {
			throw new RuntimeException("Could not load '" + fxmlPath + "'", e);
		}
	}

	static void showStage(Stage stage, String title, int minWidth, int minHeight) {
		stage.titleProperty().bind(new SimpleStringProperty(title));
		stage.setMinWidth(minWidth);
		stage.setMinHeight(minHeight);
		stage.show();
		stage.sizeToScene();
		stage.requestFocus();
	}
	

	/**
	 * Converts a byte array into the 2D int array format.
	 * 
	 * @return
	 */
	static int[][] convertByteArray(byte[] map) {
		int mapSize = (int) Math.sqrt(map.length);
		int[][] convertedMap = new int[mapSize][mapSize];
		
		for (int i = 0; i < mapSize; i++) {
			for (int j = 0; j < mapSize; j++) {
				byte b = ByteBuffer.wrap(map).get();
				String s = Byte.toString(b);
				int integer = Integer.parseInt(s);
				convertedMap[i][j] = integer;
			}
		}

		return convertedMap;
	}
	
	/**
	 * 
	 * Returns false if diamond-square map is loaded, otherwise returns true.
	 * 
	 * @param map The byte array of the map to check for the map's type
	 * @return
	 */
	static boolean getLoadedMapType(byte[] map) {
		if(ByteBuffer.wrap(map).get()==0) {
			return false;
		} else {
			return true;
		}
	}

	/**
	 * 
	 * Takes a 2D Array and saves each tile as byte.
	 * 
	 * @param map The 2D map to save.
	 * @param loadedFile The file to save the map in.
	 * @param mapType The type of map to save. 0 = DS, 1 = CA.
	 */
	static void saveMap(int[][] map, File loadedFile, int mapType) {
		File file = loadedFile;
		try (FileOutputStream fop = new FileOutputStream(file)) {

			if (!file.exists()) {
				file.createNewFile();
			}

			if(mapType == 0) {
				byte y = 0;
				fop.write(y);
			} else {
				byte y = 1;
				fop.write(y);
			}
			
			
			for (int i = 0; i < map.length; i++) {
				for (int j = 0; j < map[i].length; j++) {
					byte b = 0;
					String s = Integer.toString(map[i][j]);
					b = Byte.parseByte(s);
					fop.write(b);

				}
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 
	 * Loads a map file from the given path with the given name and returns it as
	 * byte array.
	 * 
	 * @param loadPath where the map file is located.
	 * @return
	 * @throws IOException
	 */
	static byte[] loadMap(Optional<File> loadedFile) throws IOException, NoSuchElementException {

		File file = loadedFile.get();

		try (FileInputStream fis = new FileInputStream(file)) {
			ByteArrayOutputStream buffer = new ByteArrayOutputStream();

			int nRead;
			byte[] data = new byte[1089];

			while ((nRead = fis.read(data, 0, data.length)) != -1) {
				buffer.write(data, 0, nRead);
			}

			buffer.flush();

			return buffer.toByteArray();

		} catch (IOException e) {
			e.printStackTrace();
		} catch (NoSuchElementException e2) {
			e2.printStackTrace();
		}
		return null;
	}

	static Optional<File> chooseFileToSaveDialog(Window owner) {
		FileChooser fileChooser = new FileChooser();
		fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("MapGen file", "*.mapgen"),
				new FileChooser.ExtensionFilter("All files", "*"));
		return Optional.ofNullable(fileChooser.showSaveDialog(owner));
	}

	static Optional<File> chooseFileToOpenDialog(Window owner) {
		FileChooser fileChooser = new FileChooser();
		fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("MapGen file", "*.mapgen"),
				new FileChooser.ExtensionFilter("XML files", "*.xml"),
				new FileChooser.ExtensionFilter("All files", "*"));
		return Optional.ofNullable(fileChooser.showOpenDialog(owner));
	}
}