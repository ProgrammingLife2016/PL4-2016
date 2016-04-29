package application.controllers;

import application.fxobjects.ZoomBox;
import application.fxobjects.graph.Graph;
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
    public static Stage createWindow(Parent c, ZoomBox z, Graph g) {
        Stage window = new Stage();
        Scene scene = createScene(c, g.getZoomController());

        screenSize = Screen.getPrimary().getVisualBounds();

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
    public static Scene createScene(Parent parent, ZoomController c) {
        Scene scene = new Scene(parent);
        scene.setOnScroll(c.getZoomHandler());
        return scene;
    }
}