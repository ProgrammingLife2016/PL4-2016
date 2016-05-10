package application.controllers;

import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.DirectoryChooser;
import javafx.stage.Screen;
import javafx.stage.Stage;

/**
 * WindowFactory class.
 *
 * @version 1.0
 * @since 25-04-2016
 */
public class WindowFactory {
    static Rectangle2D screenSize;
    static Stage window;

    /**
     * Constructor method for WindowFactory.
     */
    public WindowFactory() {
    }

    /**
     * Create method for windows.
     *
     * @param c parent of the window
     * @return the constructed window.
     */
    public static Stage createWindow(Parent c) {
        window = new Stage();
        Scene scene = createScene(c);

        screenSize = Screen.getPrimary().getVisualBounds();

        window.setWidth(screenSize.getWidth());
        window.setHeight(screenSize.getHeight());
        window.setMaxHeight(screenSize.getHeight());
        window.setScene(scene);
        window.show();
        return window;
    }

    /**
     * Method to create the scene.
     *
     * @param parent parent object for the scene.
     * @return the constructed scene.
     */
    public static Scene createScene(Parent parent) {
        Scene scene = new Scene(parent);
        return scene;
    }

    /**
     * Method that creates a directoryChooser.
     * @return the directoryChooser.
     */
    public static DirectoryChooser createDirectoryChooser() {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setTitle("Select Graph File");

        directoryChooser.showDialog(window);

        return directoryChooser;
    }


}