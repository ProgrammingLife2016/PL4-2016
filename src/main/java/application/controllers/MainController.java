package application.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.MenuBar;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

/**
 * MainController for GUI.
 */
public class MainController extends Controller<StackPane> {
    private MenuController menuController;

    @FXML
    StackPane screen;
    @FXML
    BorderPane mainPane;
    @FXML
    MenuBar menuBar;

    /**
     * Constructor to create MainController based on abstract Controller.
     */
    public MainController() {
        super(new StackPane());
        loadFXMLfile("/application/fxml/main.fxml");
    }

    /**
     * Initialize method for the controller.
     *
     * @param location  location for relative paths.
     * @param resources resources to localize the root object.
     */
    public final void initialize(URL location, ResourceBundle resources) {
        HBox box = new HBox();
        menuController = new MenuController(this, menuBar);

        box.getChildren().addAll(screen, menuBar);
        screen.getChildren().setAll();

        mainPane.setTop(box);
        this.getRoot().getChildren().addAll(mainPane);
    }
}
