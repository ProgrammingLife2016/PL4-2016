package application.controllers;

import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.FileChooser;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.io.File;

/**
 * WindowFactory class.
 *
 * @version 1.0
 * @since 25-04-2016
 */
public final class WindowFactory {
    static Rectangle2D screenSize;
    static Stage window;
    static MainController mainController;

    /**
     * Private class constructor.
     */
    private WindowFactory() {
    }

    /**
     * Create method for windows.
     *
     * @param m parent of the window
     * @return the constructed window.
     */
    public static Stage createWindow(MainController m) {
        mainController = m;
        window = new Stage();
        Scene scene = createScene(m.getRoot());

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
     *
     * @return the directoryChooser.
     */
    public static FileChooser createDirectoryChooser() {
        FileChooser directoryChooser = new FileChooser();
        directoryChooser.setTitle("Select Graph File");

        File selectedFile = directoryChooser.showOpenDialog(window);

        mainController.getGraphController().getGraph().getNodeMapFromFile(selectedFile.toString());
        System.out.println("selected: " + selectedFile.getAbsolutePath());

        mainController.init();

        return directoryChooser;
    }


}