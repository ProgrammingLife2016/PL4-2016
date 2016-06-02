package application.controllers;

import application.fxobjects.cell.Cell;
import application.fxobjects.cell.Edge;
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
    private MainController mainController;
    private static final double MAX_EDGE_LENGTH = 300;

    /**
     * Constructor method for this class.
     *
     * @param m the mainController.
     */
    @SuppressFBWarnings("URF_UNREAD_FIELD")
    public GraphController(MainController m) {
        super(new ScrollPane());
        this.graph = new Graph();
        this.screenSize = Screen.getPrimary().getVisualBounds();
        this.zoomController = new ZoomController(this);
        this.graphMouseHandling = new GraphMouseHandling(m);
        this.root = new AnchorPane();
        this.mainController = m;

        this.getRoot().setHbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);
        this.getRoot().setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);

        this.getRoot().addEventFilter(ScrollEvent.SCROLL, event -> {
            if (graphMouseHandling.getPrevClick() != null) {
                graphMouseHandling.getPrevClick().resetFocus();
            }
            if (event.getDeltaY() != 0) {
                if (graphMouseHandling.getPrevClick() != null) {
                    graphMouseHandling.getPrevClick().resetFocus();
                }
                if (event.getDeltaY() < 0) {
                    mainController.switchScene(+1);
                    if (graphMouseHandling.getPrevClick() != null) {
                        focus(graphMouseHandling.getPrevClick());
                    }
                    event.consume();
                }
                if (event.getDeltaY() > 0) {
                    mainController.switchScene(-1);
                    if (graphMouseHandling.getPrevClick() != null) {
                        focus(graphMouseHandling.getPrevClick());
                    }
                    event.consume();
                }
            }
        });
    }

    /**
     * Method to focus on a Cell.
     *
     * @param prevClick the cell to focus to.
     */
    public void focus(Cell prevClick) {
        prevClick.resetFocus();
        for (Cell c : graph.getModel().getAllCells()) {
            if ((c.getCellId() == prevClick.getCellId())
                    || (c.getCellId() > prevClick.getCellId())) {
                prevClick = c;
                break;
            }
        }
        graphMouseHandling.setPrevClick(prevClick);
        prevClick.focus();
        getRoot().setHvalue((prevClick.getLayoutX() - 300) / graph.getMaxWidth());
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

    /**
     * Init method.
     *
     * @param location  unused.
     * @param resources unused.
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
    }

    /**
     * Init method for this class.
     *
     * @param ref   the reference string.
     * @param depth the depth to draw.
     */
    public void update(Object ref, int depth) {
        int size = graph.getLevelMaps().size();

        //We received a different reference of depth, so we need to redraw.
        if (depth <= size - 1 && depth >= 0
                && (ref != graph.getCurrentRef() || depth != graph.getCurrentInt())) {
            root.getChildren().clear();

            graph.addGraphComponents(ref, depth);

            // add components to graph pane
            if (graph.getModel().getAllCells().size() > 0) {
                root.getChildren().addAll(graph.getModel().getAllEdges());
                root.getChildren().addAll(graph.getModel().getAllCells());
            } else {
                root.getChildren().addAll(graph.getModel().getAddedEdges());
                root.getChildren().addAll(graph.getModel().getAddedCells());
            }

            for (Edge e : graph.getModel().getAddedEdges()) {
                double xLength = e.getLine().endXProperty().get()
                        - e.getLine().startXProperty().get();
                double yLength = e.getLine().endYProperty().get()
                        - e.getLine().startYProperty().get();
                double length = Math.sqrt(Math.pow(xLength, 2) + Math.pow(yLength, 2));
                System.out.println(e.getSource().toString() + " " + length);
                if (length > MAX_EDGE_LENGTH) {
                    e.getLine().getStrokeDashArray().addAll(3d, 17d);
                    e.getLine().setOpacity(0.2d);
                    double newY = e.getSource().getLayoutY()
                            + (e.getSource().getLayoutY() - (screenSize.getHeight() - 150) / 2) * 2.5;
                    newY = Math.max(newY, 10);
                    newY = Math.min(newY, screenSize.getHeight() * 0.7);
                    e.getSource().relocate(e.getSource().getLayoutX(), newY);
                }
            }
            initMouseHandler();
            graph.endUpdate();
        }
        //Set Graph as center.
        this.getRoot().setContent(root);
    }

    /**
     * Method to attach the keyHandler to the root of the Controller
     */
    public void initKeyHandler() {
        this.getRoot().setOnKeyPressed(zoomController.getZoomBox().getKeyHandler());
    }

    /**
     * Method to attach the mouseHandler to each cell in the graph
     */
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
     */
    public Image takeSnapshot() {
        WritableImage image = new WritableImage(2500,
                (int) screenSize.getHeight());
        WritableImage snapshot = this.getRoot().getContent().snapshot(
                new SnapshotParameters(), image);

        return snapshot;
    }

    /**
     * Getter for the graphMouseHandling.
     *
     * @return the graphMouseHandling.
     */
    public GraphMouseHandling getGraphMouseHandling() {
        return graphMouseHandling;
    }
}
