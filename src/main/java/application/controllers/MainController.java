package application.controllers;

import core.graph.Graph;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Rectangle2D;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuBar;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Screen;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * MainController for GUI.
 */
public class MainController extends Controller<BorderPane> {


    @FXML
    ScrollPane screen;
    @FXML
    MenuBar menuBar;
    @FXML
    ListView list;


    static Rectangle2D screenSize;

    /**
     * Constructor to create MainController based on abstract Controller.
     */
    public MainController() {
        super(new BorderPane());
        loadFXMLfile("/fxml/main.fxml");
    }

    /**
     * Initialize method for the controller.
     *
     * @param location  location for relative paths.
     * @param resources resources to localize the root object.
     */
    public final void initialize(URL location, ResourceBundle resources) {
        screenSize = Screen.getPrimary().getVisualBounds();
        createMenu();
        createList();

//        this.getRoot().getStylesheets().add("application/css/main.css");
//        this.getRoot().getStyleClass().add("root");
    }

    private void createList() {
        list = new ListView<>();
        ObservableList<String> items = FXCollections.observableArrayList(
                "No Genomes Loaded.");
        list.setItems(items);
        this.getRoot().setRight(list);
    }

    /**
     * Method to fill the graph.
     */
    public void fillGraph(Object ref) {
        Graph graph = new Graph();
        GraphController graphController = new GraphController(this, graph, ref);
        screen = graphController.getRoot();
        this.getRoot().setCenter(screen);
        list.setItems(FXCollections.observableArrayList(graphController.getGenomes()));

        System.out.println(graphController.getGenomes().toString());

    }

    /**
     * Method that creates the Menubar.
     */
    public void createMenu() {
        MenuFactory mf = new MenuFactory(this);
        menuBar = mf.createMenu(menuBar);
        this.getRoot().setTop(menuBar);

    }

    /**
     * Switches the scene.
     */
    public void switchScene() {
        fillGraph(null);
    }

}
