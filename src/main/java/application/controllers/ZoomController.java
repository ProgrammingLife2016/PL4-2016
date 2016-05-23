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

    /**
     * Constructor method.
     */
    @SuppressFBWarnings("URF_UNREAD_FIELD")
    public ZoomController() {
        super(new BorderPane());
    }


    public ZoomBox createZoomBox() {
        ZoomBox z = new ZoomBox(this);
        zoomBox = z;

        this.getRoot().setBottom(zoomBox.getZoomBox());
        return zoomBox;
    }

    public ZoomBox getZoomBox() { return zoomBox; }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

}
