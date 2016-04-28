package application;

import application.controllers.Controller;
import application.controllers.GraphViewController;
import application.controllers.MainController;
import application.controllers.WindowFactory;
import application.fxobjects.ZoomBox;
import javafx.application.Application;
import javafx.stage.Stage;


/**
 * Main class.
 * @author Arthur Breurkes
 * @version 1.0
 * @since 18-04-2016
 */
public class Main extends Application {
    /**
     * MainController used for the application.
     */
    //private Controller mainController;

    private GraphViewController mainController;

    /**
     * Usual main method to start the application.
     * @param args runtime arguments.
     */
    public static void main(String[] args) {
        launch(args);
    }

    /**
     * Method to launch the application.
     * @param primaryStage stage used to initialize GUI.
     */
    @Override
    public void start(Stage primaryStage) {
        //mainController = new MainController();
        mainController = new GraphViewController(primaryStage);
        ZoomBox z = mainController.getGraph().getZoomBox();
        WindowFactory.createWindow(mainController.getRoot(), z);
    }
}
