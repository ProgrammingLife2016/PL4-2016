package application.controllers;

import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Screen;
import javafx.stage.Stage;



/**
 * Created by Daphne van Tetering on 25-4-2016.
 */
public class WindowFactory {


    static Rectangle2D screenSize;

    public WindowFactory(){

    }

    public static Stage createWindow(Parent parent) {
        Stage window = new Stage();
        Scene scene = createScene(parent);

        screenSize = Screen.getPrimary().getVisualBounds();

        window.setWidth(screenSize.getWidth());
        window.setHeight(screenSize.getHeight());
        window.setScene(scene);
        window.show();
        return window;

    }

    public static Scene createScene(Parent parent) {
        Scene scene = new Scene(parent);
        return scene;
    }
}
