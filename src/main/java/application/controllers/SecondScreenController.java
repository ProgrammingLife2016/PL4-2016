package application.controllers;

import application.fxobjects.graph.Graph;
import javafx.fxml.FXML;
import javafx.scene.control.MenuBar;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by Daphne van Tetering on 28-4-2016.
 */
public class SecondScreenController extends Controller<StackPane> {

    private MenuController menuController;

    private GraphViewController graphViewer;

    @FXML
    StackPane screen;
    @FXML
    BorderPane mainPane;
    @FXML
    MenuBar menuBar;

    public SecondScreenController() {
        super(new StackPane());
        // loadFXMLfile("/application/fxml/main.fxml");

    }

    /**
     * Initialize method for the controller.
     *
     * @param location  location for relative paths.
     * @param resources resources to localize the root object.
     */
    public final void initialize(URL location, ResourceBundle resources) {
        //graphViewer = new GraphViewController();
        Graph g = graphViewer.getGraph();

        HBox box = new HBox();
        menuController = new MenuController(this, menuBar);

        HBox middleBox = new HBox();
        // middleBox.getChildren().addAll(g);


        box.getChildren().addAll(screen, menuBar);
        screen.getChildren().setAll();

        mainPane.setTop(box);
        this.getRoot().getChildren().addAll(mainPane);
    }

}