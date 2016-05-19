package application.controllers;

import application.fxobjects.graph.cell.BaseLayout;
import application.fxobjects.graph.cell.CellLayout;
import core.graph.Graph;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Screen;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

/**
 * Created by Daphne van Tetering on 4-5-2016.
 */
@SuppressWarnings("PMD.UnusedPrivateField")
public class GraphController extends Controller<ScrollPane> {
    private Graph graph;
    private GraphMouseHandling graphMouseHandling;

    private javafx.geometry.Rectangle2D screenSize;

    /**
     * Constructor method for this class.
     *
     * @param g   the graph.
     * @param ref the reference string.
     * @param m the mainController.
     * @param depth the depth to draw.
     */
    @SuppressFBWarnings("URF_UNREAD_FIELD")
    public GraphController(Graph g, Object ref, MainController m, int depth) {
        super(new ScrollPane());
        this.graph = g;
        this.graphMouseHandling = new GraphMouseHandling(m);
        this.screenSize = Screen.getPrimary().getVisualBounds();
        this.getRoot().setHbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);
        this.getRoot().setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);

        this.getRoot().addEventFilter(ScrollEvent.SCROLL, event -> {
            if (event.getDeltaY() != 0) {
                event.consume();
            }
        });

        try {
            init(ref, depth);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Getter method for the graph.
     *
     * @return the graph.
     */
    public Graph getGraph() {
        return graph;
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    /**
     * Init method for this class.
     *
     * @param ref the reference string.
     * @param depth the depth to draw.
     * @throws IOException Throw exception on read GFA read failure.
     */
    public void init(Object ref, int depth) throws IOException {
        AnchorPane root = new AnchorPane();

        graph.addGraphComponents(ref, depth);

        // add components to graph pane
        root.getChildren().addAll(graph.getModel().getAddedEdges());
        root.getChildren().addAll(graph.getModel().getAddedCells());

        graph.getModel().getAddedCells().forEach(graphMouseHandling::setMouseHandling);

        // remove components from graph pane
        root.getChildren().removeAll(graph.getModel().getRemovedCells());
        root.getChildren().removeAll(graph.getModel().getRemovedEdges());

        graph.endUpdate();
        CellLayout layout = new BaseLayout(graph.getModel(), 20,
                (int) (screenSize.getHeight() - 25) / 2);
        layout.execute();

        this.getRoot().setContent(root);
    }

    /**
     * Getter method for the genomes.
     *
     * @return the list of genomes.
     */
    public List<String> getGenomes() {
        return graph.getGenomes();
    }
}
