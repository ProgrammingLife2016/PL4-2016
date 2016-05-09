package application.controllers;

import application.fxobjects.graph.MouseHandling;
import application.fxobjects.graph.cell.BaseLayout;
import application.fxobjects.graph.cell.CellLayout;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.AnchorPane;
import core.graph.Graph;
import javafx.stage.Screen;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by Daphne van Tetering on 4-5-2016.
 */
public class GraphController extends Controller<ScrollPane> {
    private Graph graph;
    private MainController mainController;
    private javafx.geometry.Rectangle2D screenSize;

    public GraphController(MainController controller, Graph g) {
        super(new ScrollPane());
        this.graph = g;
        this.mainController = controller;
        this.screenSize = Screen.getPrimary().getVisualBounds();
        this.getRoot().setHbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);
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

        CellLayout layout = new BaseLayout(graph, 20,(int) (screenSize.getHeight()-25)/2);
        layout.execute();

        this.getRoot().setContent(root);
    }
}
