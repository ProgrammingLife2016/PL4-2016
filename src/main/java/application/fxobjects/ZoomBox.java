package application.fxobjects;

import javafx.event.EventHandler;
import javafx.geometry.Rectangle2D;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.transform.Scale;
import javafx.stage.Screen;

import java.awt.event.KeyListener;

/**
 * Created by Daphne van Tetering on 28-4-2016.
 */
public class ZoomBox extends ScrollPane {
//    Group zoomGroup;
//    Scale scaleTransform;
//    Node content;
//    double scaleValue = 1.0;
//    double delta = 0.1;

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

    public ZoomBox(/*Node content*/) {
       // this.content = content;

        initVariables();

        right = new StackPane();
        right.setPrefSize(zoomBoxWidth, zoomBoxHeight);
        right.setOnKeyPressed(keyListener);
        right.getChildren().addAll(initZoomBox());

    }

    private void initVariables() {
        screenSize = Screen.getPrimary().getVisualBounds();
        windowWidth = screenSize.getWidth();
        windowHeight = screenSize.getHeight();
        graphBoxWidth = windowWidth - 10;
        graphBoxHeight = windowHeight - 10;
        zoomBoxWidth = graphBoxWidth / 5.0;
        zoomBoxHeight = graphBoxHeight / 5.0;
    }

    public Group initZoomBox() {
        Group zoomBox = new Group();

        double rectX = windowWidth - zoomBoxWidth - 20;

        zoomRectBorder = new Rectangle(rectX, 20, zoomBoxWidth, zoomBoxHeight);
        zoomRectBorder.setFill(Color.WHITE);
        zoomRectBorder.setStroke(Color.LIGHTGREY);
        zoomRectBorder.setStrokeWidth(3);

        zoomRect = new Rectangle(rectX, 20, zoomBoxWidth, zoomBoxHeight);
        zoomRect.setFill(Color.TRANSPARENT);
        zoomRect.setStroke(Color.BLACK);
        zoomRect.setStrokeWidth(3);


        zoomBox.getChildren().addAll(zoomRectBorder, zoomRect);
        zoomBox.setOnScroll(scrollListener);


        return zoomBox;

    }

    public StackPane getZoomBox() {
        return right;
    }

    public Rectangle getZoomRect() {
        return getZoomRect();
    }

    public EventHandler<KeyEvent> getKeyListener() { return keyListener; }

    public EventHandler<ScrollEvent> getScrollListener() { return scrollListener; }

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


    public void scaleZoomRect(double delta) {
        double adj = delta * (zoomRect.getHeight() / zoomRect.getWidth());
        zoomRect.setWidth(zoomRect.getWidth() + delta);
        zoomRect.setHeight(zoomRect.getHeight() + adj);

        zoomRect.setX(zoomRect.getX() - 0.5 * delta);
        zoomRect.setY(zoomRect.getY() - 0.5 * adj);
    }

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

    private EventHandler<KeyEvent> keyListener = new EventHandler<KeyEvent>() {
        int offset = 3;

        public void handle(KeyEvent event) {
            System.out.println("key");
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
        }
    };

}
