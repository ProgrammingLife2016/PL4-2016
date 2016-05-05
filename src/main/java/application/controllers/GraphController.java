package application.controllers;

import application.fxobjects.graph.cell.BaseLayout;
import application.fxobjects.graph.cell.CellLayout;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.AnchorPane;
import core.graph.Graph;
import javafx.stage.Screen;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * Controller responsible for setting up the graph visualisation.
 * Created by Daphne van Tetering on 4-5-2016.
 */
public class GraphController extends Controller<ScrollPane> {
    private Graph graph;
    private MainController mainController;
    private javafx.geometry.Rectangle2D screenSize;

    /**
     * Set-up a new graph controller.
     * @param controller    The main controller.
     * @param g A graph containing the model and node map.
     */
    public GraphController(MainController controller, Graph g) {
        super(new ScrollPane());
        this.graph = g;
        this.mainController = controller;
        this.screenSize = Screen.getPrimary().getVisualBounds();
        this.getRoot().setHbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);
        init();
    }

    /**
     * Get the graph instance.
     * @return  The graph instance.
     */
    public Graph getGraph() {
        return graph;
    }


    /**
     * Dummy initialize method.
     * @param location
     * @param resources
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    /**
     * Initialize the graph pane.
     */
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

        CellLayout layout = new BaseLayout(graph, 20, (int) (screenSize.getHeight() - 25) / 2);
        layout.execute();

        this.getRoot().setContent(root);
    }
}
