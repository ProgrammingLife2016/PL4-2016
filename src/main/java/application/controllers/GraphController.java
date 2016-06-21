package application.controllers;

import application.fxobjects.Cell;
import application.fxobjects.Edge;
import application.fxobjects.ZoomBox;
import application.fxobjects.graphCells.GraphCell;
import application.mouseHandlers.GraphMouseHandling;
import core.graph.Graph;
import core.model.Model;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import javafx.beans.property.DoubleProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Bounds;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.SnapshotParameters;
import javafx.scene.control.ScrollBar;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.WritableImage;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.stage.Screen;
import java.net.URL;
import java.util.*;


/**
 * Created by Daphne van Tetering on 4-5-2016.
 */
public class GraphController extends Controller<ScrollPane> {
    private Graph graph;
    private GraphMouseHandling graphMouseHandling;
    private AnchorPane root;
    private Rectangle2D screenSize;
    private MainController mainController;
    private ZoomBox zoomBox;
    private DoubleProperty visibleAmount;
    private Stack<Integer> zoomPath;

    public void setDrawFrom(int drawFrom) {
        this.drawFrom = drawFrom;
    }

    private int drawFrom = 0;

    /**
     * Constructor method for this class.
     *s
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
        this.zoomPath = new Stack();

        this.zoomBox = new ZoomBox(this);
        this.getRoot().setHbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);
        this.getRoot().setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        this.getRoot().setPannable(true);
        ChangeListener<Object> changeListener = (observable, oldValue, newValue) -> {
            Bounds bounds = getRoot().getViewportBounds();
            drawFrom = -1 * (int) bounds.getMinX();
            update(graph.getCurrentRef(), graph.getCurrentInt());
        };

        ChangeListener<Object> changeHListener = (observable, oldValue, newValue) -> {
            Bounds bounds = getRoot().getViewportBounds();
            drawFrom = -1 * (int) bounds.getMinX();
            System.out.println("----");
            System.out.println("!changelistener!");
            System.out.println("observable: " + observable.toString());
            System.out.println("old value: " + oldValue);
            System.out.println("new value: " + newValue);
            System.out.println("----");
            update(graph.getCurrentRef(), graph.getCurrentInt());
        };

        this.getRoot().viewportBoundsProperty().addListener(changeListener);
        this.getRoot().hvalueProperty().addListener(changeHListener);

        setScrolling(this);

    }

    /**
     * Add EventHandlers to the GraphController
     *
     * @param gc the current GraphController
     */
    private void setScrolling(GraphController gc) {

        gc.getRoot().addEventFilter(ScrollEvent.SCROLL, event -> {
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
                        zoomOutFocus(graphMouseHandling.getPrevClick());
                    }
                    zoomBox.replaceZoomBox(updateZoomBox());

                } else if (event.getDeltaY() > 0) {
                    mainController.switchScene(-1);
                    if (graphMouseHandling.getPrevClick() != null) {
                        zoomInFocus(graphMouseHandling.getPrevClick());
                    }
                    zoomBox.replaceZoomBox(updateZoomBox());
                }
                event.consume();
                drawFrom = -1 * (int) getRoot().getViewportBounds().getMinX();
                update(graph.getCurrentRef(), graph.getCurrentInt());
            }
            System.out.println("end of gc update");
        });
    }

    /**
     * Add a node ID to the path along which we
     * should zoom in to reach the originally
     * focused node again.
     *
     * @param nodeId the ID of the node to be added
     */
    public void addNodeIdToZoomPath(int nodeId) {
        zoomPath.push(nodeId);
    }

    /**
     * Clear the path of node IDs which
     * we should follow while zooming in.
     */
    public void clearZoomPath() {
        zoomPath = new Stack();
    }

    /**
     * Method to update the ZoomBox locations
     *
     * @return the new places to update
     */
    public double[] updateZoomBox() {
        Set<Node> nodes = this.getRoot().lookupAll(".scroll-bar");
        nodes.stream().filter(node -> node instanceof ScrollBar).forEach(node -> {
            ScrollBar sb = (ScrollBar) node;
            visibleAmount = sb.visibleAmountProperty();
        });

        double rightOffset = this.getRoot().getHvalue();
        double shown = visibleAmount.doubleValue();

        double[] places = new double[2];
        places[0] = rightOffset;
        places[1] = shown;

        return places;
    }

    /**
     * Method to either enable or disable sideFocus on nodes.
     *
     * @param enable if true then all sideFocus is removed
     *               from the sideFocused nodes, reverse if false.
     */
    public void sideFocus(boolean enable) {
        //Remove sideFocus of all underlying nodes.
        for (int underlyingNodeId : graphMouseHandling.
                getOriginallyFocusedNode().getPreviousLevelNodesIds()) {
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
     * Method to focus on a cell while zooming out
     *
     * @param prevClick the previously focused cell
     */
    public void zoomOutFocus(GraphCell prevClick) {
        //De-focus everything.
        sideFocus(false);
        prevClick.resetFocus();

        //Add the cellId to the zoomPath
        addNodeIdToZoomPath(prevClick.getCellId());

        //Find the node in the next zoom level and add
        int nextLevelNodeId = graphMouseHandling.getFocusedNode().getNextLevelNodeId();

        focus(nextLevelNodeId);
    }

    /**
     * Method to focus on a cell while zooming in
     *
     * @param prevClick the previously focused cell
     */
    public void zoomInFocus(GraphCell prevClick) {
        sideFocus(false);
        prevClick.resetFocus();

        int nextLevelNodeId = findNextInZoomPath();

        focus(nextLevelNodeId);
    }

    /**
     * Method to focus on a cell.
     *
     * @param nextLevelNodeId the ID of the cell to focus on.
     */
    public void focus(int nextLevelNodeId) {
        GraphCell prevClick = (GraphCell) graph.getModel().getCellMap().get(nextLevelNodeId);
        graphMouseHandling.setPrevClick(prevClick);
        graphMouseHandling.setFocusedNode(graph.getLevelMaps().get(mainController.
                getCurrentView()).get(prevClick.getCellId()));
        if (mainController.getCurrentView() == graphMouseHandling.getOriginalZoomLevel()) {
            prevClick.originalFocus();
        } else {
            prevClick.focus();
        }
        sideFocus(true);
//        slideToPercent((prevClick.getLayoutX() - (mainController.getScreen().getWidth() / 4))
//                / (graph.getMaxWidth() - 450));

        update(getGraph().getCurrentRef(), getGraph().getCurrentInt());
    }

    /**
     * Method to zoom to focus when zooming
     *
     * @return id of the node to focus
     */
    private int findNextInZoomPath() {
        if (zoomPath.size() == 1) {
            return zoomPath.peek();
        } else {
            return zoomPath.pop();
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
    public void update(ArrayList<String> ref, int depth) {
        int min = drawFrom;
        //We received a different reference of depth, so we need to redraw.
        if (depth <= graph.getLevelMaps().size() - 1 && depth >= 0
                && (ref != null && (!(ref.equals(graph.getCurrentRef()))) || depth != graph.getCurrentInt())) {
            root.getChildren().clear();
            graph.addGraphComponents(ref, depth);
            // add components to graph pane
            addToPane(min);

            for (Edge e : graph.getModel().getAddedEdges()) {
                checkEdgeLength(e);
            }

            Set<Node> nodes = this.getRoot().lookupAll(".scroll-bar");
            nodes.stream().filter(node -> node instanceof ScrollBar).forEach(node -> {
                ScrollBar sb = (ScrollBar) node;
                sb.valueProperty().addListener((ObservableValue<? extends Number> ov, Number old, Number newval) -> {
                    zoomBox.replaceZoomBox(updateZoomBox());
                });
            });
            initMouseHandler();
        } else {
            addToPane(min);
        }

        graph.endUpdate();


        this.getRoot().setContent(root);

        if (graphMouseHandling.getPrevClick() != null) {
            slideToPercent((graphMouseHandling.getPrevClick().getLayoutX() / (graph.getMaxWidth() - 450)));
        }


    }

    /**
     * Method to check whether an edge should be dashed because it is too long
     * @param e the edge to check
     */
    private void checkEdgeLength(Edge e) {
        double maxEdgeLength = screenSize.getWidth() / 4;
        double maxEdgeLengthLong = screenSize.getWidth();
        double xLength = e.getLine().endXProperty().get() - e.getLine().startXProperty().get();
        double yLength = e.getLine().endYProperty().get() - e.getLine().startYProperty().get();
        double length = Math.sqrt(Math.pow(xLength, 2) + Math.pow(yLength, 2));

        if (length > maxEdgeLength || length > maxEdgeLengthLong) {
            e.getLine().getStrokeDashArray().addAll(3d, 17d);
            if (e.getLine().getStroke() == Color.BLACK) {
                e.getLine().setStroke(Color.LIGHTGRAY);
            } else {
                e.getLine().setStroke(Color.ORANGE);
            }
            double newY = e.getSource().getLayoutY()
                    + ((GraphCell) e.getSource()).getCellShape().getLayoutBounds().getHeight() / 2
                    + screenSize.getHeight() / 20;
            newY = Math.max(newY, 10);
            newY = Math.min(newY, screenSize.getHeight() * 0.67);
            e.getSource().relocate(e.getSource().getLayoutX(), newY);
        }
    }

    /**
     * Method that gets the nodes that are in the view, and adds it to the model.
     *
     * @param min left side of the view.
     */
    private void addToPane(int min) {
        root.getChildren().clear();
        Model model = graph.getModelAllInView(min);
        root.getChildren().addAll(model.getAddedEdges());
        root.getChildren().addAll(model.getAddedCells());
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
        int pref = (int) graph.getModel().getGraphLayout().getMaxWidth() + 1;
        int height = ((int) graph.getModel().getGraphLayout().getMaxHeight()) * 2 + 1;

        WritableImage image;
        if (graph.getModel().getAllCells().isEmpty()) {
            image = new WritableImage(1, 1);
        } else {
            image = new WritableImage(pref, height);
        }

        WritableImage snapshot = this.getRoot().getContent().snapshot(
                new SnapshotParameters(), image);

        return snapshot;
    }

    /**
     * Getter for the graphMouseHandling
     *
     * @return the graphMouseHandling object of the GraphController
     */
    public GraphMouseHandling getGraphMouseHandling() {
        return graphMouseHandling;
    }

    /**
     * Method to slide to a certain percent of the screen
     * 
     * @param percent the percent to slide to
     */
    public void slideToPercent(double percent) {
        if (getRoot().getHvalue() != percent) {
            getRoot().setHvalue((percent));
        }
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
