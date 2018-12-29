/**
 * 
 */
module mapgenprototype {
	requires javafx.graphics;
	requires javafx.fxml;
	requires java.desktop;
	requires javafx.controls;

	exports main;
	
	opens controller to javafx.fxml;
}