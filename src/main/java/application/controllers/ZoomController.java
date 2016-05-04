package application.controllers;

import application.fxobjects.ZoomBox;
import application.fxobjects.ZoomableScrollPane;
import javafx.event.EventHandler;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.transform.Scale;
import javafx.stage.Screen;

/**
 * Created by Daphne van Tetering on 28-4-2016.
 */
public class ZoomController extends BorderPane {

//    private ZoomableScrollPane zoomPane;
//    private ZoomBox  zoomBox;
//    private ZoomHandler zoomHandler;
//    private KeyHandler keyHandler;
//    private Rectangle2D screenSize;
//
//    Scale scaleTransform;
//    double scaleValue = 1.0;
//
//    public ZoomController(Node content) {
//        zoomPane = new ZoomableScrollPane(content);
//        zoomBox = new ZoomBox();
//
//        screenSize = Screen.getPrimary().getVisualBounds();
//
//        zoomHandler = new ZoomHandler();
//        keyHandler = new KeyHandler();
//
//        scaleTransform = new Scale(scaleValue, scaleValue, 0, 0);
//
////        this.setOnScroll(new ZoomHandler());
////        this.setOnKeyPressed(new KeyHandler());
//
//        init();
//    }
//
//    /**
//     * Getter for zoomPane.
//     * @return the zoomPane.
//     */
//    public ZoomableScrollPane getZoomPane() { return zoomPane; }
//
//
//    /**
//     * Getter for keyHandler.
//     * @return the keyHandler.
//     */
//    public KeyHandler getKeyHandler() { return keyHandler; }
//
//
//    /**
//     * Init method.
//     * @return the pane.
//     */
//    public void init() {
//        this.setCenter(zoomPane);
//        this.setBottom(zoomBox.getZoomBox());
//
//    }
//
//    /**
//     * Handler for the zoom function.
//     */
//    private class ZoomHandler implements EventHandler<ScrollEvent> {
//
//        @Override
//        public void handle(ScrollEvent scrollEvent) {
//
//
//            double delta = scrollEvent.getDeltaY();
//            {
//                zoomPane.zoom(delta);
//                zoomBox.zoom(delta);
//                scrollEvent.consume();
//            }
//        }
//    }
//
//    /**
//     * Handles the move funtion.
//     */
//    private class KeyHandler implements EventHandler<KeyEvent> {
//
//        @Override
//        public void handle(KeyEvent event) {
//            zoomBox.moveRectangle(event);
//        }
//
//    }
}
