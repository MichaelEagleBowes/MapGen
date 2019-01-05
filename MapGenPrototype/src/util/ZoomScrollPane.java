package util;

import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.ScrollPane;
import javafx.scene.transform.Scale;

/**
 * 
 * Applies a sclaing transformation to the given {@link Node} to create a zoom effect.
 * 
 * @author Michael Bowes
 */
public class ZoomScrollPane extends ScrollPane {

	Group zoomGroup;
	Scale scaleTransform;
	Node content;
	
	public void ZoomableScrollPane(Node content, double x, double y)
	  {
	    this.content = content;
	    Group contentGroup = new Group();
	    zoomGroup = new Group();
	    contentGroup.getChildren().add(zoomGroup);
	    zoomGroup.getChildren().add(content);
	    setContent(contentGroup);
	    scaleTransform = new Scale(x, y, 0, 0);
	    zoomGroup.getTransforms().add(scaleTransform);
	  }
	
}
