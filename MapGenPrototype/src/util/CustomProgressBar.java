package util;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class CustomProgressBar extends ProgressBar {
        public CustomProgressBar(String styleClass, double progress) {
            super(progress);
            getStyleClass().add(styleClass);
        }
}
