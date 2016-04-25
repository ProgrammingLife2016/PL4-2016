package application;

import javafx.application.Application;
import javafx.stage.Stage;

import application.controllers.MainController;

public class Main extends Application {
    private MainController mainController;
    
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        mainController = new MainController();

        mainController.launch(primaryStage);
    }
}
