package util;

import java.math.BigDecimal;
import java.math.MathContext;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javafx.animation.Interpolator;
import javafx.geometry.Orientation;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.RadialGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.ArcType;
import javafx.scene.shape.Rectangle;
import model.MapContainer;

/**
 * Draws a heat map and a color scale onto a canvas.
 * 
 * @author Michael Bowes
 *
 */
public class HeatMap extends Canvas {

	private final static double MIN = 0;
	private final static double MAX = 1000;
	private final static double YELLOW_HUE = Color.YELLOW.getHue();
	private final static double RED_HUE = Color.RED.getHue();
	Image colorScale;

	public HeatMap(int width, int height) {
		setWidth(width);
		setHeight(height);
	}

	public ImageView drawHeatMap(GraphicsContext gc, HashMap<Tuple, MapContainer> mapSamples) {
		WritableImage wImage = new WritableImage(500, 500);
		PixelWriter writer = wImage.getPixelWriter();

		int cumulativeColorId = 0;
		for (int j = 0; j < 10; j++) {
			for (int i = 0; i < 10; i++) {

				Tuple t = new Tuple(j, i);
				MapContainer currentContainer = mapSamples.get(t);
				if(mapSamples.containsKey(t)) {
					int colorId = 0;
					cumulativeColorId += colorId;
					if(currentContainer.getMapCount() > 0) {
						colorId = 400*currentContainer.getMapCount()/currentContainer.getTotalMapCount();
						System.out.println(currentContainer.getMapCount());
						System.out.println("color: "+colorId);
					}
					PixelReader pr = colorScale.getPixelReader();

					// System.out.println("col: "+colorId);
					Color color = pr.getColor(40, colorId);

					for (int l = 0; l < 50; l++)
						for (int k = 0; k < 50; k++) {
							writer.setColor(j*50+l, i*50+k, color);
						}
				}
			}
		}

		return new ImageView(wImage);
	}

	public ImageView createColorScale() {
		colorScale = createColorScaleImage(80, 400, Orientation.VERTICAL);
		ImageView imageView = new ImageView(colorScale);

		return imageView;
	}

	private Color getColorForValue(double value) {
		if (value < MIN || value > MAX) {
			return Color.BLACK;
		}
		double hue = RED_HUE + (YELLOW_HUE - RED_HUE) * (value - MIN) / (MAX - MIN);
		return Color.hsb(hue, 1.0, 1.0);
	}

	private Image createColorScaleImage(int width, int height, Orientation orientation) {
		WritableImage image = new WritableImage(width, height);
		PixelWriter pixelWriter = image.getPixelWriter();
		if (orientation == Orientation.HORIZONTAL) {
			for (int x = 0; x < width; x++) {
				double value = MIN + (MAX - MIN) * x / width;
				Color color = getColorForValue(value);
				for (int y = 0; y < height; y++) {
					pixelWriter.setColor(x, y, color);
				}
			}
		} else {
			for (int y = 0; y < height; y++) {
				double value = MAX - (MAX - MIN) * y / height;
				Color color = getColorForValue(value);
				for (int x = 0; x < width; x++) {
					pixelWriter.setColor(x, y, color);
				}
			}
		}
		return image;
	}

}
