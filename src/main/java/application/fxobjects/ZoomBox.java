package application.fxobjects;

import application.controllers.GraphController;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import javafx.geometry.Rectangle2D;
import javafx.scene.Group;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import javafx.stage.Screen;

/**
 * ZoomBox class
 */
public class ZoomBox extends ScrollPane {
    private Rectangle zoomRectBorder;
    public Rectangle zoomRect;

    private StackPane right;

    private Rectangle2D screenSize;
    private double windowWidth;
    private double windowHeight;
    private double graphBoxWidth;
    private double graphBoxHeight;
    private double zoomBoxWidth;
    private double zoomBoxHeight;
    private double rectX;
    private GraphController graphController;
    private static Image image;

    /**
     * Class constructor.
     *
     * @param g the GraphController currently active
     */
    public ZoomBox(GraphController g) {
        this.graphController = g;
        initVariables();
        right = new StackPane();
        right.setPrefSize(zoomBoxWidth, zoomBoxHeight);
        right.getChildren().addAll(initZoomBox());

    }

    /**
     * Initialize the necessary attributes.
     */
    private void initVariables() {
        screenSize = Screen.getPrimary().getVisualBounds();
        windowWidth = screenSize.getWidth();
        windowHeight = screenSize.getHeight();
        graphBoxWidth = windowWidth - 10;
        graphBoxHeight = windowHeight - 10;
        zoomBoxWidth = graphBoxWidth / 5.0;
        zoomBoxHeight = graphBoxHeight / 5.0;

        rectX = windowWidth - zoomBoxWidth - 20;
        zoomRectBorder = new Rectangle(rectX, 20, zoomBoxWidth, zoomBoxHeight);
        zoomRectBorder.setStroke(Color.LIGHTGREY);
        zoomRectBorder.setStrokeWidth(3);

        zoomRect = new Rectangle(rectX, 20, zoomBoxWidth, zoomBoxHeight);
        zoomRect.setFill(Color.TRANSPARENT);
        zoomRect.setStroke(Color.BLACK);
        zoomRect.setStrokeWidth(3);

    }

    /**
     * Initialize the zoom box.
     *
     * @return The zoom box.
     */
    public Group initZoomBox() {
        Group zoomBox = new Group();

        zoomBox.getChildren().addAll(zoomRectBorder, zoomRect);

        return zoomBox;
    }

    /**
     * Method to take a snapshot
     *
     * @param snap boolean to indicate if we need to refresh the snapshot
     */
    @SuppressFBWarnings
    public void fillZoomBox(boolean snap) {
        Image snapshot = graphController.takeSnapshot();
        if (snap) {
            image = snapshot;
        }

        ImagePattern pattern = new ImagePattern(image, rectX, 20, zoomBoxWidth, zoomBoxHeight, false);

        zoomRectBorder.setFill(pattern);
    }

    /**
     * Method to reset the ZoomBox's place
     */
    public void reset() {
        zoomRect.setWidth(zoomBoxWidth);
    }


    /**
     * Method to set the ZoomBox to the new locations
     *
     * @param places the new locations
     */
    public void replaceZoomBox(double[] places) {
        double rightOffset = places[0];
        double shown = places[1];

        if (shown > 1.0) {
            shown = 1.0;
        }

        if (rightOffset > 1.0) {
            rightOffset = 1.0;
        }

        double right = rectX + rightOffset * zoomBoxWidth;
        double newWidth = shown * zoomBoxWidth;
        double maxX = rectX + zoomBoxWidth - newWidth;

        if (right > maxX) {
            right = maxX;
        }

        if (newWidth < 5) {
            newWidth = 5;
        }
        zoomRect.setX(right);
        zoomRect.setWidth(newWidth);
    }


    /**
     * Return the zoom box.
     *
     * @return The zoom box.
     */
    public StackPane getZoomBox() {
        return right;
    }

}
