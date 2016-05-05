package application.controllers;

import core.graph.Graph;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.geometry.Rectangle2D;
import javafx.scene.control.MenuBar;
import javafx.scene.control.ScrollBar;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.AnchorPane;
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

    ScrollBar horizontalScrollBar;

    static Rectangle2D screenSize;
    /**
     * Constructor to create MainController based on abstract Controller.
     */
    public MainController() {
        super(new BorderPane());
        loadFXMLfile("/application/fxml/main.fxml");
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

//        this.getRoot().getStylesheets().add("application/css/main.css");
//        this.getRoot().getStyleClass().add("root");
    }

    public void fillGraph() {
        Graph graph = new Graph();
        GraphController graphController = new GraphController(this, graph);
        screen = graphController.getRoot();
       // screen = graphController.getRoot();
        this.getRoot().setCenter(screen);

    }

    public void createMenu(){
        MenuFactory menuFactory = new MenuFactory(this);
        menuBar = menuFactory.createMenu(menuBar);
        this.getRoot().setTop(menuBar);

    }

//    public void createSrollBar() {
//        ScrollFactory scrollFactory = new ScrollFactory(this);
//        horizontalScrollBar = scrollFactory.createHorizontalScrollBar();
//        horizontalScrollBar.setPrefWidth(screenSize.getWidth() - 20);
//        horizontalScrollBar.setLayoutY(screenSize.getHeight() - 80);
//
//        horizontalScrollBar.valueProperty().addListener(new ChangeListener<Number>() {
//            @Override
//            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
//                System.out.println("semantic zoom");
//
//            }
//        });
//
//        screen.getChildren().add(horizontalScrollBar);
//
//    }

    public void switchScene() {
        System.out.println("switch scene");
        //BorderPane borderPane = this.getRoot();
        //GraphController graph = new GraphController(this);
        //borderPane.setCenter(graph.init());
        //createSrollBar();
        fillGraph();


    }

}
