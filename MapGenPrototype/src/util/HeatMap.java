package util;

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

public class HeatMap extends Canvas {

    private final static double MIN = 0 ;
    private final static double MAX = 1000;
    private final static double BLUE_HUE = Color.BLUE.getHue();
    private final static double RED_HUE = Color.RED.getHue();
    Image colorScale;
	
	public HeatMap(int width, int height) {
		setWidth(width);
		setHeight(height);
	}
	
	public ImageView drawHeatMap(GraphicsContext gc, double[] dimensionOne, double[] dimensionTwo) {
		WritableImage wImage = new WritableImage((int) getWidth(), (int) getHeight());
		PixelWriter writer = wImage.getPixelWriter();

		for (int y = 0; y < dimensionOne.length; y++) {
			for (int x = 0; x < dimensionTwo.length; x++) {
				
				PixelReader pr = colorScale.getPixelReader();
				Color color = pr.getColor((int)dimensionOne[x], (int)dimensionTwo[y]);

				writer.setColor(x, y, color.darker());
			}
		}
		
		return new ImageView(wImage);
	}
	
	public ImageView createColorScale() {
        colorScale = createColorScaleImage(600, 120, Orientation.VERTICAL);
        ImageView imageView = new ImageView(colorScale);
        
        return imageView;
	}
	
    private Color getColorForValue(double value) {
        if (value < MIN || value > MAX) {
            return Color.BLACK ;
        }
        double hue = BLUE_HUE + (RED_HUE - BLUE_HUE) * (value - MIN) / (MAX - MIN) ;
        return Color.hsb(hue, 1.0, 1.0);
    }

    private Image createColorScaleImage(int width, int height, Orientation orientation) {
        WritableImage image = new WritableImage(width, height);
        PixelWriter pixelWriter = image.getPixelWriter();
        if (orientation == Orientation.HORIZONTAL) {
            for (int x=0; x<width; x++) {
                double value = MIN + (MAX - MIN) * x / width;
                Color color = getColorForValue(value);
                for (int y=0; y<height; y++) {
                    pixelWriter.setColor(x, y, color);
                }
            }
        } else {
            for (int y=0; y<height; y++) {
                double value = MAX - (MAX - MIN) * y / height ;
                Color color = getColorForValue(value);
                for (int x=0; x<width; x++) {
                    pixelWriter.setColor(x, y, color);
                }
            }
        }
        return image ;
    }

}
