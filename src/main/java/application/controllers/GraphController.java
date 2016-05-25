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
    AnchorPane root = new AnchorPane();
    private Rectangle2D screenSize;
    private int maxWidth;
    private int maxHeight;


    private String position;

    /**
     * Constructor method for this class.
     *
     * @param g     the graph.
     * @param ref   the reference string.
     * @param m     the mainController.
     * @param depth the depth to draw.
     */
    @SuppressFBWarnings("URF_UNREAD_FIELD")
    public GraphController(Graph g, Object ref, MainController m, int depth) {
        super(new ScrollPane());
        this.graph = g;
        this.screenSize = Screen.getPrimary().getVisualBounds();
        this.zoomController = new ZoomController(this);
        this.graphMouseHandling = new GraphMouseHandling(m);
        this.getRoot().setHbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);
        this.getRoot().setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        this.position = "";
        maxWidth = 0;
        maxHeight = (int) screenSize.getHeight();

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
     * @param ref   the reference string.
     * @param depth the depth to draw.
     * @throws IOException Throw exception on read GFA read failure.
     */
    public void init(Object ref, int depth) throws IOException {
        if(ref != graph.getCurrentRef()) {
            System.out.println("New reference, redraw");
            root.getChildren().clear();
        }
        int size = graph.getModel().getLevelMaps().size();
        System.out.println(size + " IS SIZE ");
        if (depth <= size - 1 && depth >= 0 && depth != graph.getCurrentInt()) {
            root.getChildren().clear();
            System.out.println("Remove all children, received depth: " + depth);
        }

        graph.addGraphComponents(ref, depth);

        // add components to graph pane
        root.getChildren().addAll(graph.getModel().getAddedEdges());
        root.getChildren().addAll(graph.getModel().getAddedCells());

        List<Cell> list = graph.getModel().getAddedCells();

        for (Cell c : list) {
            graphMouseHandling.setMouseHandling(c);
        }

        graph.endUpdate();


        this.getRoot().setContent(root);

    }

    /**
     * Method to attach the keyHandler to the root of the Controller
     */
    public void initKeyHandler() {
        this.getRoot().setOnKeyPressed(zoomController.getZoomBox().getKeyHandler());
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
     * Getter method for the position of the generated
     * snapshot
     *
     * @return the position of the snapshot
     */
    public String getPosition() {
        return position;
    }

    /**
     * Method take a snapshot of the current graph.
     *
     * @throws IOException Throw exception on write failure.
     */
    public Image takeSnapshot() throws IOException {
        WritableImage image = new WritableImage(maxWidth + 50,
                (int) screenSize.getHeight());
        WritableImage snapshot = this.getRoot().getContent().snapshot(
                new SnapshotParameters(), image);
        return snapshot;

    }
}
