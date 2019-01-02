package controller;

import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Optional;

import javafx.beans.property.SimpleStringProperty;
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
	 * @param bi
	 * @return
	 */
	public Image convertBufferedImage(BufferedImage bi) {
		WritableImage wr = null;
		if (bi != null) {
			wr = new WritableImage(bi.getWidth(), bi.getHeight());
			PixelWriter pw = wr.getPixelWriter();
			for (int x = 0; x < bi.getWidth(); x++) {
				for (int y = 0; y < bi.getHeight(); y++) {
					pw.setArgb(x, y, bi.getRGB(x, y));
				}
			}
		}
		return wr;
	}
	
	/**
	 * Scales a {@link BufferedImage} to the specified width and height.
	 * @param Img the image in question
	 * @param width the width of the output
	 * @param height the height of the output
	 * @return the scaled BufferedImage
	 */
	public static BufferedImage scaleImage(BufferedImage Img, int width, int height) {
		BufferedImage resizedImg = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g2 = resizedImg.createGraphics();

		g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
		g2.drawImage(Img, 0, 0, width, height, null);
		g2.dispose();

		return resizedImg;
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
	
	public static double round(double value, int places) {
	    if (places < 0) throw new IllegalArgumentException();

	    BigDecimal bd = new BigDecimal(value);
	    bd = bd.setScale(places, RoundingMode.HALF_UP);
	    return bd.doubleValue();
	}

	static Optional<File> chooseFileToOpenDialog(Window owner) {
		FileChooser fileChooser = new FileChooser();
		fileChooser.getExtensionFilters().addAll(
				new FileChooser.ExtensionFilter("XML files", "*.xml"),
				new FileChooser.ExtensionFilter("All files", "*"));
		return Optional.ofNullable(fileChooser.showOpenDialog(owner));
	}

	static Optional<File> chooseFileToSaveDialog(Window owner, String fileExtension) {
		FileChooser fileChooser = new FileChooser();
		if(fileExtension != null) {
			fileChooser.getExtensionFilters().add(
					new FileChooser.ExtensionFilter(fileExtension.toUpperCase()+" files",
							"*." + fileExtension.toLowerCase()));
		}
		fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("All files", "*"));
		return Optional.ofNullable(fileChooser.showSaveDialog(owner));
	}

	static Optional<String> textInputDialog(String initial, String title,
	                                        String header, String contentText) {
		TextInputDialog dialog = new TextInputDialog(initial);
		dialog.setTitle(title);
		dialog.setHeaderText(header);
		dialog.setContentText(contentText);
		return dialog.showAndWait();
	}

	static void showStage(Stage stage, String title, int minWidth,
	                      int minHeight) {
		stage.titleProperty().bind(
				new SimpleStringProperty(title));
		stage.setMinWidth(minWidth);
		stage.setMinHeight(minHeight);
		stage.show();
		stage.sizeToScene();
		stage.requestFocus();
	}
}