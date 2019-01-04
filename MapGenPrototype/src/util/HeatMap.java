package util;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.effect.DropShadow;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.RadialGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.ArcType;
import javafx.scene.shape.Rectangle;

public class HeatMap extends Canvas {

	public HeatMap(int width, int height) {
		setWidth(width);
		setHeight(height);
	}

	public void drawShapes(GraphicsContext gc, double[][] dimensions) {

		for (int i = 0; i < dimensions.length; i++) {
			for (int j = 0; j < dimensions.length; j++) {
				Stop[] stops = new Stop[] { new Stop(0, Color.BLACK), new Stop(1, Color.RED) };
				LinearGradient lg1 = new LinearGradient(0, 0, 1, 0, true, CycleMethod.NO_CYCLE, stops);
				gc.setFill(lg1);
				gc.fillRect(i * 20, j * 20, 20, 20);
			}
		}
	}

	public void drawHeatMap(GraphicsContext gc, double[] dimensionOne, double[] dimensionTwo) {
		drawBezierCurve(gc, dimensionOne, dimensionTwo);
		drawLinearGradient(gc, Color.RED, Color.ORANGE);
		drawDropShadow(gc, Color.RED, Color.ORANGE, Color.GREEN, Color.BLUE);
	}

	private void drawBezierCurve(GraphicsContext gc, double[] dimensionOne, double[] dimensionTwo) {
		gc.moveTo(0, getHeight());
		gc.bezierCurveTo(dimensionOne[0], dimensionTwo[0], dimensionOne[1], dimensionTwo[1], dimensionOne[2],
				dimensionTwo[2]);
	}

	private void drawLinearGradient(GraphicsContext gc, Color firstColor, Color secondColor) {
		LinearGradient lg = new LinearGradient(0, 0, 1, 1, true, CycleMethod.REFLECT, new Stop(0.0, firstColor),
				new Stop(1.0, secondColor));
		gc.setStroke(lg);
		gc.setLineWidth(20);
		gc.stroke();
	}

	private void drawRadialGradient(GraphicsContext gc, Color firstColor, Color lastColor) {
		gc.setFill(new RadialGradient(0, 0, 0.5, 0.5, 0.1, true, CycleMethod.REFLECT, new Stop(0.0, firstColor),
				new Stop(1.0, lastColor)));
		gc.fill();
	}

	private void drawDropShadow(GraphicsContext gc, Color firstColor, Color secondColor, Color thirdColor,
			Color fourthColor) {
		gc.applyEffect(new DropShadow(20, 20, 0, firstColor));
		gc.applyEffect(new DropShadow(20, 0, 20, secondColor));
		gc.applyEffect(new DropShadow(20, -20, 0, thirdColor));
		gc.applyEffect(new DropShadow(20, 0, -20, fourthColor));
	}

}
