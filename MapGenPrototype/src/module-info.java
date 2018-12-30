/**
 * 
 */
module mapgenprototype {
	requires javafx.graphics;
	requires javafx.fxml;
	requires java.desktop;
	requires javafx.controls;
	requires javafx.base;

	exports main;
	
	opens controller to javafx.fxml;
}