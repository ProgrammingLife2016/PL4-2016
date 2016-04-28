package application.fxobjects;

import javafx.event.EventHandler;
import javafx.geometry.Rectangle2D;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.transform.Scale;
import javafx.stage.Screen;



public class ZoomableScrollPane extends ScrollPane {
    Group zoomGroup;
    Scale scaleTransform;
    Node content;
    double scaleValue = 1.0;
    double delta = 0.1;

    private Rectangle zoomRectBorder;
    private Rectangle zoomRect;

    private Rectangle2D screenSize;
    private double windowWidth;
    private double windowHeight;
    private double graphBoxWidth;
    private double graphBoxHeight;
    private double zoomBoxWidth;
    private double zoomBoxHeight;


    public ZoomableScrollPane(Node content) {
        this.content = content;

        screenSize = Screen.getPrimary().getVisualBounds();
        windowWidth = Screen.getPrimary().getVisualBounds().getWidth();
        zoomBoxWidth = graphBoxWidth / 5.0;
        windowWidth = screenSize.getWidth();
        windowHeight = screenSize.getHeight();
        graphBoxWidth = windowWidth - 10;
        graphBoxHeight = windowHeight - 10;
        zoomBoxWidth = graphBoxWidth / 5.0;
        zoomBoxHeight = graphBoxHeight / 5.0;


        Group contentGroup = new Group();
        zoomGroup = new Group();
        contentGroup.getChildren().add(zoomGroup);
        zoomGroup.getChildren().add(content);
        setContent(contentGroup);
        scaleTransform = new Scale(scaleValue, scaleValue, 0, 0);
        zoomGroup.getTransforms().add(scaleTransform);

        zoomGroup.setOnScroll(new ZoomHandler());
    }

//    public Rectangle2D zoomBox() {
//        Rectangle2D box = new Rectangle2D();
//
//        double rectX = windowWidth - zoomBoxWidth - 20;
//
//        zoomRectBorder = new Rectangle(rectX, 20, zoomBoxWidth, zoomBoxHeight);
//        zoomRectBorder.setFill(Color.WHITE);
//        zoomRectBorder.setStroke(Color.LIGHTGREY);
//        zoomRectBorder.setStrokeWidth(3);
//
//        zoomRect = new Rectangle(rectX, 20, zoomBoxWidth, zoomBoxHeight);
//        zoomRect.setFill(Color.TRANSPARENT);
//        zoomRect.setStroke(Color.BLACK);
//        zoomRect.setStrokeWidth(3);
//
//
//        return box;
//
//    }

    public double getScaleValue() {
        return scaleValue;
    }

    public void zoomToActual() {
        zoomTo(1.0);
    }

    public void zoomTo(double scaleValue) {

        this.scaleValue = scaleValue;

        scaleTransform.setX(scaleValue);
        scaleTransform.setY(scaleValue);

    }

    public void zoomActual() {

        scaleValue = 1;
        zoomTo(scaleValue);

    }

    public void zoomOut() {
        scaleValue -= delta;

        if (Double.compare(scaleValue, 0.1) < 0) {
            scaleValue = 0.1;
        }

        zoomTo(scaleValue);
    }

    public void zoomIn() {

        scaleValue += delta;

        if (Double.compare(scaleValue, 10) > 0) {
            scaleValue = 10;
        }

        zoomTo(scaleValue);

    }

    /**
     * @param minimizeOnly If the content fits already into the viewport, then we don't
     *                     zoom if this parameter is true.
     */
    public void zoomToFit(boolean minimizeOnly) {

        double scaleX = getViewportBounds().getWidth() / getContent().getBoundsInLocal().getWidth();
        double scaleY = getViewportBounds().getHeight() / getContent().getBoundsInLocal().getHeight();

        // consider current scale (in content calculation)
        scaleX *= scaleValue;
        scaleY *= scaleValue;

        // distorted zoom: we don't want it => we search the minimum scale
        // factor and apply it
        double scale = Math.min(scaleX, scaleY);

        // check precondition
        if (minimizeOnly) {

            // check if zoom factor would be an enlargement and if so, just set
            // it to 1
            if (Double.compare(scale, 1) > 0) {
                scale = 1;
            }
        }

        // apply zoom
        zoomTo(scale);

    }


    private EventHandler<ScrollEvent> scrollListener = new EventHandler<ScrollEvent>() {
        public void handle(ScrollEvent event) {
            double delta = event.getDeltaY();

            if (delta > 0) {
                // Both positive and negative offsets have to be tested as scaleZoomRect
                // will enlarge zoomRect in all directions.
                if (checkRectBoundaries(delta, (zoomRect.getHeight() / zoomRect.getWidth()) * delta) && checkRectBoundaries(-delta, -(zoomRect.getHeight() / zoomRect.getWidth()) * delta)) {
                    scaleZoomRect(delta);
                }
            } else if (delta < 0) {
                if ((zoomRect.getHeight() + delta) >= (zoomRectBorder.getHeight() * 0.05)
                        && zoomRect.getWidth() + delta * (zoomRect.getWidth() /
                        zoomRect.getHeight()) >= (zoomRectBorder.getWidth() * 0.05)) {
                    scaleZoomRect(delta);
                }
            }
        }
    };


    private void scaleZoomRect(double delta) {
        double adj = delta * (zoomRect.getHeight() / zoomRect.getWidth());
        zoomRect.setWidth(zoomRect.getWidth() + delta);
        zoomRect.setHeight(zoomRect.getHeight() + adj);

        zoomRect.setX(zoomRect.getX() - 0.5 * delta);
        zoomRect.setY(zoomRect.getY() - 0.5 * adj);
    }

    private Boolean checkRectBoundaries(double offsetX, double offsetY) {
        Boolean res = true;
        if (res && offsetX < 0) {
            res = (zoomRect.getX() + offsetX) >= zoomRectBorder.getX();
        } else if (res && offsetX > 0) {
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

    public void handle(KeyEvent event) {
        int offset = 3;
        switch(event.getCode()) {
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
            case W:
                if (checkRectBoundaries(0, -offset)) {
                    zoomRect.setY(zoomRect.getY() - offset);
                }
                break;
            case S:
                if (checkRectBoundaries(0, offset)) {
                    zoomRect.setY(zoomRect.getY() + offset);
                }
                break;
            default:
                break;
        }
    };

    private class ZoomHandler implements EventHandler<ScrollEvent> {

        public void handle(ScrollEvent scrollEvent) {
            // if (scrollEvent.isControlDown())
            {

                if (scrollEvent.getDeltaY() < 0) {
                    scaleValue -= delta;
                } else {
                    scaleValue += delta;
                }

                zoomTo(scaleValue);

                scrollEvent.consume();
            }
        }
    }
}