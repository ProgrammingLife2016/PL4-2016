package application.controllers;

import application.fxobjects.ZoomBox;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import javafx.scene.layout.BorderPane;
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

    /**
     * Method to create the ZoomBox, which will be controlled
     * by the ZoomController
     * @return the created ZoomBox
     */
    public ZoomBox createZoomBox() {
        ZoomBox z = new ZoomBox();
        zoomBox = z;

        this.getRoot().setBottom(zoomBox.getZoomBox());
        return zoomBox;
    }

    public ZoomBox getZoomBox() { return zoomBox; }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

}
