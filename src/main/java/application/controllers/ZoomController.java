package application.controllers;

import application.fxobjects.ZoomBox;
import application.fxobjects.ZoomableScrollPane;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.transform.Scale;

/**
 * Created by Daphne van Tetering on 28-4-2016.
 */
public class ZoomController extends ScrollPane {

    private ZoomableScrollPane zoomPane;
    private ZoomBox  zoomBox;
    private BorderPane pane;
    private ZoomHandler zoomHandler;
    private KeyHandler keyHandler;

    Scale scaleTransform;
    double scaleValue = 1.0;

    public ZoomController(Node content) {
        zoomPane = new ZoomableScrollPane(content);
        zoomBox = new ZoomBox();

        zoomHandler = new ZoomHandler();
        keyHandler = new KeyHandler();

        scaleTransform = new Scale(scaleValue, scaleValue, 0, 0);

        this.setOnScroll(new ZoomHandler());

        init();
    }

    public ZoomableScrollPane getZoomPane() { return zoomPane; }

    public ZoomBox getZoomBox() { return zoomBox; }

    public BorderPane getPane() { return pane; }

    public ZoomHandler getZoomHandler() {
        return zoomHandler;
    }

    public KeyHandler getKeyHandler() { return keyHandler; }


    public BorderPane init() {
        pane = new BorderPane();

        pane.setCenter(zoomPane);
        pane.setBottom(zoomBox.getZoomBox());

        pane.setOnScroll(new ZoomHandler());
        pane.setOnKeyPressed(new KeyHandler());

        return pane;
    }


    private class ZoomHandler implements EventHandler<ScrollEvent> {

        @Override
        public void handle(ScrollEvent scrollEvent) {

            double delta = scrollEvent.getDeltaY();
            {
                zoomPane.zoom(delta);
                zoomBox.zoom(delta);
                scrollEvent.consume();
            }
        }
    }

    private class KeyHandler implements EventHandler<KeyEvent> {

        @Override
        public void handle(KeyEvent event) {
            zoomBox.moveRectangle(event);
        }

    }
}
