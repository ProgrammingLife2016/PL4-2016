package application.controllers;

import application.fxobjects.graph.MouseHandling;
import application.fxobjects.graph.cell.BaseLayout;
import application.fxobjects.graph.cell.Cell;
import application.fxobjects.graph.cell.CellLayout;
import javafx.scene.layout.AnchorPane;
import core.graph.Graph;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by Daphne van Tetering on 4-5-2016.
 */
public class GraphController extends Controller<AnchorPane> {
    private Graph graph;
    private MainController mainController;

    public GraphController(MainController controller, Graph g) {
        super(new AnchorPane());
        this.graph = g;
        this.mainController = controller;

        init();
    }

    public Graph getGraph() { return graph; }


    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    public void init() {
        AnchorPane root = new AnchorPane();

        graph.addGraphComponents();

        // add components to graph pane
        root.getChildren().addAll(graph.getModel().getAddedEdges());
        root.getChildren().addAll(graph.getModel().getAddedCells());

        // remove components from graph pane
        root.getChildren().removeAll(graph.getModel().getRemovedCells());
        root.getChildren().removeAll(graph.getModel().getRemovedEdges());

        graph.endUpdate();

        CellLayout layout = new BaseLayout(graph, 20);
        layout.execute();

        System.out.println("fill root");
        this.getRoot().getChildren().add(root);
    }
}
