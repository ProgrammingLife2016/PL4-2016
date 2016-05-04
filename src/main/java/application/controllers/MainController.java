package application.controllers;

import core.graph.Graph;
import javafx.fxml.FXML;
import javafx.scene.control.MenuBar;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * MainController for GUI.
 */
public class MainController extends Controller<BorderPane> {


    @FXML
    AnchorPane screen;
    @FXML
    MenuBar menuBar;

    AnchorPane anchorPane;

    /**
     * Constructor to create MainController based on abstract Controller.
     */
    public MainController() {
        super(new BorderPane());
        anchorPane = new AnchorPane();
        loadFXMLfile("/application/fxml/main.fxml");
    }

    /**
     * Initialize method for the controller.
     *
     * @param location  location for relative paths.
     * @param resources resources to localize the root object.
     */
    public final void initialize(URL location, ResourceBundle resources) {
        MenuFactory menuFactory = new MenuFactory(this);
        menuBar = menuFactory.createMenu(menuBar);
        Graph graph = new Graph();

        GraphController graphController = new GraphController(this, graph);
        AnchorPane anchorPane = graphController.getRoot();

        this.getRoot().setTop(menuBar);
        this.getRoot().setCenter(anchorPane);


//        this.getRoot().getStylesheets().add("application/css/main.css");
//        this.getRoot().getStyleClass().add("root");
    }


    public void switchScene() {
//        System.out.println("switch scene");
//        BorderPane borderPane = this.getRoot();
//        GraphController graph = new GraphController(this);
//        borderPane.setCenter(graph.init());

    }

}
