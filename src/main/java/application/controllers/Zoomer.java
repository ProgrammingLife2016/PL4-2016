package application.controllers;

import application.fxobjects.ZoomBox;
import javafx.beans.value.ChangeListener;
import javafx.event.EventHandler;
import javafx.geometry.Bounds;
import javafx.scene.Group;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Region;

import javax.swing.border.Border;
import java.util.List;

/**
 * Created by Daphne van Tetering on 3-5-2016.
 */
public class Zoomer {

    public static final double SCALE_VALUE = 0.1;
    public static final double MAX_RADIUS = 25;
    public static final double MAX_DIST = 120;

    final ScrollPane zoomPane;
    final BorderPane borderPane;
    final Group zoomGroup;
    final ZoomBox zoomBox;
    final KeyHandler keyHandler;

    double mouseLocationX;
    double mouseLocationY;
    double left;
    double top;

    public Zoomer(ScrollPane pane, Group group) {
        this.zoomPane = pane;
        this.zoomGroup = group;
        this.mouseLocationX = 0.0;
        this.mouseLocationY = 0.0;

        zoomBox = new ZoomBox();
        keyHandler = new KeyHandler();
        borderPane = new BorderPane();

        borderPane.setCenter(zoomPane);
        borderPane.setBottom(zoomBox.getZoomBox());
        borderPane.setOnKeyPressed(keyHandler);

//        zoomPane.widthProperty().addListener(event -> bounds());
//        zoomPane.hvalueProperty().addListener(event -> bounds());
//        zoomPane.vvalueProperty().addListener(event -> bounds());
    }

    private void bounds() {
        Bounds bounds = zoomPane.getContent().layoutBoundsProperty().getValue();
        left = getCurrentBound(bounds.getWidth(), zoomPane.getWidth(), zoomPane.getHvalue());
        top = getCurrentBound(bounds.getHeight(), zoomPane.getHeight(), zoomPane.getVvalue());
    }

    public ChangeListener<List<Region>> addListeners() {
        return (observable, oldValue, newValue) -> {
            zoomPane.setOnMouseEntered(createMouseHandler(newValue));
            zoomPane.setOnMouseMoved(createMouseHandler(newValue));
        };
    }

    private EventHandler<MouseEvent> createMouseHandler(final List<Region> labels) {
        return event -> {
            updateMouse(event);
            zoom(labels);
        };
    }

    public void updateMouse(final MouseEvent event) {
        mouseLocationX = event.getX();
        mouseLocationY = event.getY();
    }

    public void zoom(final List<Region> regions) {
        regions.forEach(label ->
                addScale(label,
                        getScale(
                                label.getTranslateX(),
                                label.getTranslateY(),
                                label.getWidth(),
                                label.getHeight(),
                                mouseLocationX - zoomGroup.getLayoutX() + left,
                                mouseLocationY - zoomGroup.getLayoutY() + top)));
    }

    public void addScale(final Region region, final double ratio) {
        double scale = 1 + SCALE_VALUE * (Math.cos(ratio * Math.PI) + 1);
        region.setScaleX(scale);
        region.setScaleY(scale);
    }

    public double getScale(final double labelX, final double labelY, final double labelW, final double labelH,
                           final double mouseX, final double mouseY) {
        double dx = mouseX - labelX - (labelW / 2);
        double dy = mouseY - labelY - (labelH / 2);
        double distance = Math.sqrt(dx * dx + dy * dy);
        distance = Math.max(0, Math.min(MAX_DIST, distance - MAX_RADIUS));
        return distance / MAX_DIST;
    }

    public double getCurrentBound(final double boundSize, final double scrollSize, final double scrollScalar) {
        return (boundSize - scrollSize) * scrollScalar;
    }

    public double getScaleValue() {
        return SCALE_VALUE;
    }

    private class KeyHandler implements EventHandler<KeyEvent> {

        @Override
        public void handle(KeyEvent event) {
            zoomBox.moveRectangle(event);
        }

    }
}
