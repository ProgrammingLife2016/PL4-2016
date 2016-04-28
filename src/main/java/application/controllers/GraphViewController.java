package application.controllers;

import application.fxobjects.graph.Graph;
import application.fxobjects.graph.Model;
import application.fxobjects.graph.cell.BaseLayout;
import application.fxobjects.graph.cell.CellLayout;
import application.fxobjects.graph.cell.CellType;
import javafx.fxml.FXML;
import javafx.scene.control.MenuBar;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Controller class, used when creating other controllers.
 *
 * @author Daphne van Tetering.
 * @version 1.0
 * @since 26-04-2016
 */
public class GraphViewController extends Controller<StackPane> {
    private Graph graph;

    private MenuController menuController;

    private ZoomController zoomController;
    @FXML
    StackPane screen;
    @FXML
    MenuBar menuBar;

    /**
     * Constructor: generate a Controller.
     */
    public GraphViewController() {
        super(new StackPane());
        loadFXMLfile("/application/fxml/graphview.fxml");


        this.graph = new Graph();

        zoomController = graph.getZoomController();
    }

    public void initialize(URL location, ResourceBundle resources) {
        graph = new Graph();

        BorderPane root = graph.getZoomController().getPane();

        HBox hbox = new HBox();
        menuController = new MenuController(this, menuBar);

        hbox.getChildren().addAll(screen, menuBar);
        screen.getChildren().setAll();

        root.setTop(hbox);

        addGraphComponents();
        CellLayout layout = new BaseLayout(graph, 100);
        layout.execute();



        this.getRoot().getChildren().addAll(root);
    }

    private void addGraphComponents() {
        Model model = graph.getModel();

        graph.beginUpdate();

        model.addCell("Cell A", CellType.RECTANGLE);
        model.addCell("Cell AA", CellType.TRIANGLE);
        model.addCell("Cell AB", CellType.TRIANGLE);
        model.addCell("Cell B", CellType.RECTANGLE);

        model.addEdge("Cell A", "Cell AA");
        model.addEdge("Cell A", "Cell AB");
        model.addEdge("Cell AA", "Cell B");
        model.addEdge("Cell AB", "Cell B");

        graph.endUpdate();
    }

    public Graph getGraph() {
        return graph;
    }
}
