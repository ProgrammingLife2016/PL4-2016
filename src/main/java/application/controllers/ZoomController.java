package application.controllers;

import application.fxobjects.ZoomBox;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import javafx.event.EventHandler;
import javafx.geometry.Rectangle2D;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.BorderPane;
import javafx.stage.Screen;

import java.net.URL;
import java.util.ResourceBundle;


/**
 * Created by Daphne van Tetering on 28-4-2016.
 */
@SuppressWarnings("PMD.UnusedPrivateField")
public class ZoomController extends Controller<BorderPane> {

    private ZoomBox zoomBox;
    private KeyHandler keyHandler;
    private Rectangle2D screenSize;


    /**
     * Constructor method.
     */
    @SuppressFBWarnings("URF_UNREAD_FIELD")
    public ZoomController() {
        super(new BorderPane());
        zoomBox = new ZoomBox();
        screenSize = Screen.getPrimary().getVisualBounds();
        keyHandler = new KeyHandler();

        this.getRoot().setOnKeyPressed(new KeyHandler());

        init();
    }

    /**
     * Getter for keyHandler.
     *
     * @return the keyHandler.
     */
    public KeyHandler getKeyHandler() {
        return keyHandler;
    }

    public ZoomBox getZoomBox() { return zoomBox; }

    /**
     * Init method.
     */
    public void init() {
        this.getRoot().setBottom(zoomBox.getZoomBox());
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

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
