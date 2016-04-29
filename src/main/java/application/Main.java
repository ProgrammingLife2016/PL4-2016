package application;

import application.controllers.GraphViewController;
import application.controllers.WindowFactory;
import application.fxobjects.ZoomBox;
import javafx.application.Application;
import javafx.stage.Stage;

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
        mainController = new GraphViewController();
        ZoomBox z = mainController.getGraph().getZoomController().getZoomBox();
        WindowFactory.createWindow(mainController.getRoot(), z, mainController.getGraph());

    }
}
