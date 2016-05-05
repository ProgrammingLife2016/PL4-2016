package application.controllers;

import application.fxobjects.ZoomBox;
import javafx.event.EventHandler;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.BorderPane;
import javafx.stage.Screen;


/**
 * Created by Daphne van Tetering on 28-4-2016.
 */
public class ZoomController extends BorderPane {

    private ZoomBox zoomBox;
    private ZoomHandler zoomHandler;
    private KeyHandler keyHandler;
    private Rectangle2D screenSize;


    public ZoomController(Node content) {
        zoomBox = new ZoomBox();

        screenSize = Screen.getPrimary().getVisualBounds();

        zoomHandler = new ZoomHandler();
        keyHandler = new KeyHandler();

//        this.setOnScroll(new ZoomHandler());
//        this.setOnKeyPressed(new KeyHandler());

        init();
    }


    /**
     * Getter for keyHandler.
     * @return the keyHandler.
     */
    public KeyHandler getKeyHandler() { return keyHandler; }


    /**
     * Init method.
     * @return the pane.
     */
    public void init() {
        this.setBottom(zoomBox.getZoomBox());

    }

    /**
     * Handler for the zoom function.
     */
    private class ZoomHandler implements EventHandler<ScrollEvent> {

        @Override
        public void handle(ScrollEvent scrollEvent) {


            double delta = scrollEvent.getDeltaY();
            {
                zoomBox.zoom(delta);
                scrollEvent.consume();
            }
        }
    }

    /**
     * Handles the move funtion.
     */
    private class KeyHandler implements EventHandler<KeyEvent> {

        @Override
        public void handle(KeyEvent event) {
            zoomBox.moveRectangle(event);
        }

    }
}
