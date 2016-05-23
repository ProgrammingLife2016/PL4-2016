package application.fxobjects;

import javafx.event.EventHandler;
import javafx.geometry.Rectangle2D;
import javafx.scene.Group;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import javafx.stage.Screen;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

/**
 * Created by Daphne van Tetering on 28-4-2016.
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
    private KeyHandler keyHandler;

    /**
     * Class constructor.
     */
    public ZoomBox() {
        this.keyHandler = new KeyHandler();
        initVariables();
        right = new StackPane();
        right.setPrefSize(zoomBoxWidth, zoomBoxHeight);
        right.getChildren().addAll(initZoomBox());

        this.setOnKeyPressed(keyHandler);
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

        double rectX = windowWidth - zoomBoxWidth - 20;
        zoomRectBorder = new Rectangle(rectX, 20, zoomBoxWidth, zoomBoxHeight);
        zoomRectBorder.setStroke(Color.LIGHTGREY);
        zoomRectBorder.setStrokeWidth(3);

        zoomRect = new Rectangle(rectX, 20, 20, zoomBoxHeight);
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

//        String snapshot = "/snapshot.png";
//        FileInputStream stream = null;
//        try {
//             stream = new FileInputStream(snapshot);
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        }
//
//        Image image = new Image(stream);
//        ImagePattern pattern = new ImagePattern(image);

//        zoomRectBorder.setFill(pattern);
        zoomBox.getChildren().addAll(zoomRectBorder, zoomRect);

        return zoomBox;

    }

    /**
     * Return the zoom box.
     *
     * @return The zoom box.
     */
    public StackPane getZoomBox() { return right; }

    public KeyHandler getKeyHandler() { return keyHandler; }


    /**
     * Check the boundaries of the zoombox to see if a movement is allowed.
     *
     * @param offsetX The offset changed in the X direction.
     * @param offsetY The offset changes in the Y direction.
     * @return True/False depending on the movement.
     */
    public Boolean checkRectBoundaries(double offsetX, double offsetY) {
        Boolean res = true;
        if (offsetX < 0) {
            res = (zoomRect.getX() + offsetX) >= zoomRectBorder.getX();
        } else if (offsetX > 0) {
            res = (zoomRect.getX() + zoomRect.getWidth() + offsetX)
                    <= (zoomRectBorder.getX() + zoomRectBorder.getWidth());
        }

        if (res && offsetY < 0) {
            res = (zoomRect.getY() + offsetY) >= zoomRectBorder.getY();
        } else if (res && offsetY > 0) {
            res = (zoomRect.getY() + zoomRect.getHeight() + offsetY)
                    <= (zoomRectBorder.getY() + zoomRectBorder.getHeight());
        }

        return res;
    }

    /**
     * zoom the view of zoombox in.
     *
     * @param delta Number of pixels to scale.
     */
    public void scaleZoomRectIn(double delta) {
        if (zoomRect.getWidth() > 182.0) {
            double adj = delta * (zoomRect.getHeight() / zoomRect.getWidth());
            zoomRect.setWidth(zoomRect.getWidth() + delta);
            zoomRect.setHeight(zoomRect.getHeight() + adj);

            zoomRect.setX(zoomRect.getX() - 0.5 * delta);
            zoomRect.setY(zoomRect.getY() - 0.5 * adj);
        }
    }

    /**
     * zoom the view of zoombox out.
     *
     * @param delta Number of pixels to scale.
     */
    public void scaleZoomRectOut(double delta) {
        double adj = delta * (zoomRect.getHeight() / zoomRect.getWidth());
        zoomRect.setWidth(zoomRect.getWidth() + delta);
        zoomRect.setHeight(zoomRect.getHeight() + adj);

        zoomRect.setX(zoomRect.getX() - 0.5 * delta);
        zoomRect.setY(zoomRect.getY() - 0.5 * adj);

    }

    /**
     * Zoom the zoombox based on a given delta.
     *
     * @param delta Offset to change.
     */
    public void zoom(double delta) {
        if (-delta > 0.0) {
            if (checkRectBoundaries(delta, (zoomRect.getHeight() / zoomRect.getWidth()) * delta)
                    && checkRectBoundaries(-delta, -(zoomRect.getHeight() / zoomRect.getWidth())
                    * delta)) {
                scaleZoomRectOut(-delta);
            }
        } else if (-delta < 0.0
                && (zoomRect.getHeight() + delta) >= (zoomRectBorder.getHeight() * 0.05)
                && zoomRect.getWidth() + delta * (zoomRect.getWidth()
                * zoomRect.getHeight()) >= (zoomRectBorder.getWidth() * 0.05)) {
            scaleZoomRectIn(-delta);
        }
    }

    /**
     * Zoom using the ASDW keys.
     *
     * @param event A KeyEvent.
     */
    public void moveRectangle(KeyEvent event) {
        double offset = 10;
        switch (event.getCode()) {
            case A:
                if (checkRectBoundaries(-offset, 0)) {
                    zoomRect.setX(zoomRect.getX() - offset);
                }
                break;
            case D:
                if (checkRectBoundaries(offset, 0)) {
                    zoomRect.setX(zoomRect.getX() + offset);
                }
                break;
            default:
                break;
        }
    }
    /**
     * Handles the move function
     */
    private class KeyHandler implements EventHandler<KeyEvent> {

        @Override
        public void handle(KeyEvent event) {
            moveRectangle(event);
        }
    }
}
