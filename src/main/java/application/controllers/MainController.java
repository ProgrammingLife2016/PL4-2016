package application.controllers;

import application.Constants;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

/**
 * MainController for GUI.
 */
public class MainController {
    private Rectangle zoomRectBorder;
    private Rectangle zoomRect;
    private Rectangle graphRect;

    public void launch(Stage primaryStage) {

        Scene scene = new Scene(makeRoot(), Constants.WINDOW_WIDTH, Constants.WINDOW_HEIGHT);
        scene.setOnKeyPressed(keyListener);
        scene.setOnScroll(scrollListener);

        primaryStage.setTitle("Zooming POC");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private Group makeRoot() {
        Group root = new Group();

        double rectX = Constants.WINDOW_WIDTH - Constants.ZOOMBOX_WIDTH - 10;
        double rectY = Constants.WINDOW_HEIGHT - Constants.ZOOMBOX_HEIGHT - 10;

        zoomRectBorder = new Rectangle(rectX, rectY, Constants.ZOOMBOX_WIDTH, Constants.ZOOMBOX_HEIGHT);
        zoomRectBorder.setFill(Color.WHITE);
        zoomRectBorder.setStroke(Color.LIGHTGREY);
        zoomRectBorder.setStrokeWidth(3);

        zoomRect = new Rectangle(rectX, rectY, Constants.ZOOMBOX_WIDTH, Constants.ZOOMBOX_HEIGHT);
        zoomRect.setFill(Color.TRANSPARENT);
        zoomRect.setStroke(Color.BLACK);
        zoomRect.setStrokeWidth(3);

        graphRect = new Rectangle(10, 10, Constants.GRAPHBOX_WIDTH, Constants.GRAPHOX_HEIGHT);
        graphRect.setFill(Color.WHITE);
        graphRect.setStroke(Color.BLACK);
        graphRect.setStrokeWidth(3);

        root.getChildren().addAll(zoomRectBorder, zoomRect, graphRect);
        return root;
    }
    /**
     * Change the size of zoomRect on scroll events.
     */
    private EventHandler<ScrollEvent> scrollListener = new EventHandler<ScrollEvent>() {
        public void handle(ScrollEvent event) {
            double delta = event.getTextDeltaY();

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
            res = (zoomRect.getX() + offsetX) > zoomRectBorder.getX();
        } else if (res && offsetX > 0) {
            res = (zoomRect.getX() + zoomRect.getWidth() + offsetX)
                    < (zoomRectBorder.getX() + zoomRectBorder.getWidth());
        }

        if (res && offsetY < 0) {
            res = (zoomRect.getY() + offsetY) > zoomRectBorder.getY();
        } else if (res && offsetY > 0) {
            res = (zoomRect.getY() + zoomRect.getHeight() + offsetY)
                    < (zoomRectBorder.getY() + zoomRectBorder.getHeight());
        }

        return res;
    }

    /**
     * Move the graph on WASD key presses.
     */
    private EventHandler<KeyEvent> keyListener = new EventHandler<KeyEvent>() {
        int offset = 3;

        public void handle(KeyEvent event) {
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
    };
}
