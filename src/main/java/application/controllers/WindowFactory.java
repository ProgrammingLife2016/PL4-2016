package application.controllers;

import application.fxobjects.ZoomBox;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Screen;
import javafx.stage.Stage;

/**
 * WindowFactory class.
 * @version 1.0
 * @since 25-04-2016
 */
public class WindowFactory {
    static Rectangle2D screenSize;

    /**
     * Constructor method for WindowFactory.
     */
    public WindowFactory(){

    }

    /**
     * Create method for windows.
     * @param c parent of the window
     * @return the constructed window.
     */
    public static Stage createWindow(Parent c, ZoomBox z) {
        Stage window = new Stage();
        Scene scene = createScene(c);

        int offset = 3;
        screenSize = Screen.getPrimary().getVisualBounds();


        scene.setOnKeyPressed(event -> {
            System.out.println("key");
            switch(event.getCode()) {
                case A:
                    System.out.println("A");
                    if (z.checkRectBoundaries(offset, 0)) {
                        z.getZoomRect().setX(z.getZoomRect().getX() - offset);
                        System.out.println("if");
                    }
                    break;
                case D:
                    if (z.checkRectBoundaries(offset, 0)) {
                        System.out.println("if");
                        z.getZoomRect().setX(z.getZoomRect().getX() + offset);
                    }
                    break;
                case W:
                    if (z.checkRectBoundaries(0, -offset)) {
                        z.getZoomRect().setY(z.getZoomRect().getY() - offset);
                    }
                    break;
                case S:
                    if (z.checkRectBoundaries(0, offset)) {
                        z.getZoomRect().setY(z.getZoomRect().getY() + offset);
                    }
                    break;
                default:
                    break;
            }
        });


        window.setWidth(screenSize.getWidth());
        window.setHeight(screenSize.getHeight());
        window.setScene(scene);
        window.show();
        return window;
    }

    /**
     * Method to create the scene.
     * @param parent parent object for the scene.
     * @return the constructed scene.
     */
    public static Scene createScene(Parent parent) {
        Scene scene = new Scene(parent);
        return scene;
    }
}