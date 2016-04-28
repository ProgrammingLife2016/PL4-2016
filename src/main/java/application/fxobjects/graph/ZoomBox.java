package application.fxobjects.graph;

import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.ScrollPane;
import javafx.scene.transform.Scale;

/**
 * Created by Daphne van Tetering on 28-4-2016.
 */
public class ZoomBox extends ScrollPane {
    Group zoomGroup;
    Scale scaleTransform;
    Node content;
    double scaleValue = 1.0;
    double delta = 0.1;


    public ZoomBox(Node content, Group zoomGroup, Scale scaleTransform, Node content1, double scaleValue, double delta) {
        super(content);
        this.zoomGroup = zoomGroup;
        this.scaleTransform = scaleTransform;
        content = content1;
        this.scaleValue = scaleValue;
        this.delta = delta;
    }



}
