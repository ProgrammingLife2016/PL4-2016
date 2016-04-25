package application.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.MenuBar;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import java.net.URL;
import java.util.ResourceBundle;


/**
 * MainController for GUI.
 */
public class MainController extends ControllerTemplate<StackPane> {

    MenuController menuController;

    @FXML
    StackPane screen;

    @FXML
    BorderPane mainPane;

    @FXML
    MenuBar menuBar;

    /**
     * Constructor: generate a ControllerTemplate
     */
    public MainController() {
        super(new StackPane());
        loadFXMLfile("/application/main.fxml");
    }


    public final void initialize(URL location, ResourceBundle resources) {

        HBox box = new HBox();
        menuController = new MenuController(this, menuBar);

        box.getChildren().addAll(screen, menuBar);
        screen.getChildren().setAll();


        mainPane.setTop(box);
        this.getRoot().getChildren().addAll(mainPane);
    }
}
