package application.controllers;

import application.fxobjects.ZoomBox;
import application.fxobjects.cell.Cell;
import application.fxobjects.cell.Edge;
import application.fxobjects.cell.graph.GraphCell;
import core.graph.Graph;
import core.graph.cell.CellType;
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
    private GraphMouseHandling graphMouseHandling;
    private AnchorPane root;
    private Rectangle2D screenSize;
    private MainController mainController;
    private ZoomBox zoomBox;


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
        this.graphMouseHandling = new GraphMouseHandling(m);
        this.root = new AnchorPane();
        this.mainController = m;

        this.zoomBox = new ZoomBox(this);
        this.getRoot().setHbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);
        this.getRoot().setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);

        this.getRoot().addEventFilter(ScrollEvent.SCROLL, event -> {
            if (graphMouseHandling.getPrevClick() != null) {
                graphMouseHandling.getPrevClick().resetFocus();
                sideFocus(false);
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

                    zoomBox.replaceZoomBox(updateZoomBox());
                    event.consume();
                }
                if (event.getDeltaY() > 0) {
                    mainController.switchScene(-1);

                    if (graphMouseHandling.getPrevClick() != null) {
                        focus(graphMouseHandling.getPrevClick());
                    }

                    zoomBox.replaceZoomBox(updateZoomBox());
                    event.consume();
                }
            }
        });
    }

    /**
     * Method to update the ZoomBox locations
     *
     * @return the new places to update
     */
    public double[] updateZoomBox() {
        double left = Math.abs(this.getRoot().getViewportBounds().getMinX());
        double middle = this.getRoot().getViewportBounds().getWidth();
        double total = graph.getModel().getGraphLayout().getMaxWidth();

        double rightOffset = left / total;
        double shown = middle / total;

        double[] places = new double[2];
        places[0] = rightOffset;
        places[1] = shown;

        return places;
    }

    /**
     * Method to either enable or disable sideFocus on nodes.
     * @param enable if true then all sideFocus is removed
     *               from the sideFocused nodes, reverse if false.
     */
    public void sideFocus(boolean enable) {
        //Remove sideFocus of all underlying nodes.
        for (int underlyingNodeId : graphMouseHandling.
                getFocusedNode().getPreviousLevelNodesIds()) {
            GraphCell cell = (GraphCell) graph.getModel().getCellMap().get(underlyingNodeId);
            if (cell != null) {
                if (enable) {
                    cell.sideFocus();
                } else {
                    cell.resetFocus();
                }

            }
        }
    }

    /**
     * Method to focus on a Cell.
     *
     * @param prevClick the cell to focus to.
     */
    public void focus(GraphCell prevClick) {
        sideFocus(false);
        prevClick.resetFocus();
        for (Cell c : graph.getModel().getAllCells()) {
            if (c.getCellId() == prevClick.getCellId()
                    || c.getCellId() > prevClick.getCellId()) {
                prevClick = (GraphCell) c;
                break;
            }
        }
        graphMouseHandling.setPrevClick(prevClick);
        graphMouseHandling.setFocusedNode(graph.getLevelMaps().get(mainController.
                getCurrentView()).get(prevClick.getCellId()));
        prevClick.focus();
        sideFocus(true);
        getRoot().setHvalue(prevClick.getLayoutX() / (graph.getMaxWidth() - 450));
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
        //We received a different reference of depth, so we need to redraw.
        if (depth <= graph.getLevelMaps().size() - 1 && depth >= 0
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
            double maxEdgeLength = screenSize.getWidth() / 6.4;
            double maxEdgeLengthLong = screenSize.getWidth();
            for (Edge e : graph.getModel().getAddedEdges()) {
                double xLength = e.getLine().endXProperty().get()
                        - e.getLine().startXProperty().get();
                double yLength = e.getLine().endYProperty().get()
                        - e.getLine().startYProperty().get();
                double length = Math.sqrt(Math.pow(xLength, 2) + Math.pow(yLength, 2));

                if (length > maxEdgeLength
                        && !(e.getSource().getType() == CellType.RECTANGLE)
                        || length > maxEdgeLengthLong) {
                    e.getLine().getStrokeDashArray().addAll(3d, 17d);
                    e.getLine().setOpacity(0.2d);
                    double newY = e.getSource().getLayoutY()
                            + ((GraphCell) e.getSource()).getCellShape().getLayoutBounds().getHeight() / 2
                            + (e.getSource().getLayoutY()
                            + ((GraphCell) e.getSource()).getCellShape().getLayoutBounds().getHeight() / 2
                            - (screenSize.getHeight() - 100) / 2) * 2.5;
                    newY = Math.max(newY, 10);
                    newY = Math.min(newY, screenSize.getHeight() * 0.67);
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
        int pref = (int) graph.getModel().getGraphLayout().getMaxWidth();
        if (pref + 50 > 2500) {
            pref = 2500;
        }

        WritableImage image = new WritableImage(pref,
                (int) screenSize.getHeight());
        WritableImage snapshot = this.getRoot().getContent().snapshot(
                new SnapshotParameters(), image);

        return snapshot;
    }

    /**
     * Getter for the graphMouseHandling
     * @return the graphMouseHandling object of the GraphController
     */
    public GraphMouseHandling getGraphMouseHandling() {
        return graphMouseHandling;
    }

    /**
     * Getter for the ZoomBox
     *
     * @return the zoomBox
     */
    public ZoomBox getZoomBox() {
        return zoomBox;
    }
}
