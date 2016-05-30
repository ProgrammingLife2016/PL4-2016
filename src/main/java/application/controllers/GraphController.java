package application.controllers;

import application.fxobjects.cell.Cell;
import core.graph.Graph;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import javafx.geometry.Rectangle2D;
import javafx.scene.SnapshotParameters;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.WritableImage;
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
    private ZoomController zoomController;
    private GraphMouseHandling graphMouseHandling;
    private AnchorPane root;
    private Rectangle2D screenSize;

    /**
     * Constructor method for this class.
     *
     * @param ref             the reference string.
     * @param m               the mainController.
     * @param depth           the depth to draw.
     * @param selectedGenomes the genomes to display.
     */
    @SuppressFBWarnings("URF_UNREAD_FIELD")
    public GraphController(Object ref, MainController m, int depth,
                           List<String> selectedGenomes) {
        super(new ScrollPane());
        this.graph = new Graph();
        this.screenSize = Screen.getPrimary().getVisualBounds();
        this.zoomController = new ZoomController(this);
        this.graphMouseHandling = new GraphMouseHandling(m);
        this.root = new AnchorPane();

        this.getRoot().setHbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);
        this.getRoot().setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);

        //TODO: Remove event filter, for enabling zooming in and out
        this.getRoot().addEventFilter(ScrollEvent.SCROLL, event -> {
            if (event.getDeltaY() != 0) {
                event.consume();
            }
        });

        try {
            init(ref, depth, selectedGenomes);
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

    /**
     * Getter method for the ZoomController
     *
     * @return the ZoomController
     */
    public ZoomController getZoomController() {
        return zoomController;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
    }

    /**
     * Init method for this class.
     *
     * @param ref             the reference string.
     * @param depth           the depth to draw.
     * @param selectedGenomes the genomes to be selected.
     * @throws IOException Throw exception on read GFA read failure.
     */
    public void init(Object ref, int depth, List<String> selectedGenomes) throws IOException {
        if (ref != graph.getCurrentRef()) {
            root.getChildren().clear();
        }
        int size = graph.getModel().getLevelMaps().size();

        if (depth <= size - 1 && depth >= 0 && depth != graph.getCurrentInt()) {
            root.getChildren().clear();
        }

        graph.addGraphComponents(ref, depth, selectedGenomes);

        // add components to graph pane
        if (graph.getModel().getAllCells().size() > 0) {
            root.getChildren().addAll(graph.getModel().getAllEdges());
            root.getChildren().addAll(graph.getModel().getAllCells());
        } else {
            root.getChildren().addAll(graph.getModel().getAddedEdges());
            root.getChildren().addAll(graph.getModel().getAddedCells());
        }

        initKeyHandler();
        initMouseHandler();

        graph.endUpdate();
        this.getRoot().setContent(root);

    }

    /**
     * Method to attach the keyHandler to the root of the Controller
     */
    public void initKeyHandler() {
        this.getRoot().setOnKeyPressed(zoomController.getZoomBox().getKeyHandler());
    }


    public void initMouseHandler() {
        List<Cell> list = graph.getModel().getAddedCells();

        for (Cell c : list) {
            graphMouseHandling.setMouseHandling(c);
        }
    }

    /**
     * Getter method for the genomes.
     *
     * @return the list of genomes.
     */
    public List<String> getGenomes() {
        return graph.getGenomes();
    }

    /**
     * Method take a snapshot of the current graph.
     *
     * @return A snapshot taken of the graph.
     * @throws IOException Throw exception on write failure.
     */
    public Image takeSnapshot() {
        WritableImage image = new WritableImage(2500,
                (int) screenSize.getHeight());
        WritableImage snapshot = this.getRoot().getContent().snapshot(
                new SnapshotParameters(), image);

        return snapshot;
    }
}
