package application;

import application.controllers.MainController;
import application.controllers.WindowFactory;
import javafx.application.Application;
import javafx.stage.Stage;

/**
 * The main class of the application.
 */
public class Main extends Application {

    /**
     * MainController used for the application.
     */
    private MainController mainController;

    /**
     * Usual main method to start the application.
     *
     * @param args runtime arguments.
     */
    public static void main(String[] args) {
        launch(args);
    }

    /**
     * Method to launch the application.
     *
     * @param primaryStage stage used to initialize GUI.
     */
    @Override
    public void start(Stage primaryStage) {
        mainController = new MainController();
        WindowFactory.createWindow(mainController);


    }
}
