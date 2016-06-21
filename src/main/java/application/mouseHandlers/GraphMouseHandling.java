package application.mouseHandlers;

import application.controllers.MainController;
import application.fxobjects.Cell;
import application.fxobjects.graphCells.GraphCell;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import javafx.event.EventHandler;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import org.apache.poi.ss.formula.functions.Even;

import static java.lang.String.format;

/**
 * Class responsible for the handling of mouse events
 */
public class GraphMouseHandling {
    private MainController mainController;
    private GraphCell prevClick;
    private core.graph.Node focusedNode;
    private core.graph.Node originallyFocusedNode;
    private int originalZoomLevel;

    private EventHandler<MouseEvent> onMousePressedEventHandler = event -> {
        GraphCell node = (GraphCell) event.getSource();

        core.graph.Node clicked = mainController.getGraphController().getGraph()
                .getModel().getLevelMaps().get(mainController.getCurrentView())
                .get(node.getCellId());

        originalZoomLevel = mainController.getCurrentView();

        StringBuilder builder = new StringBuilder();
        mainController.appendFilterNames(builder);

        String info = "";

        info += format("Genome ID: %d \n", clicked.getId());
        info += format("%d genomes in Node: \n%s\n",
                clicked.getGenomes().size(), clicked.getGenomesAsString());


        if (clicked.getAnnotations().size() > 0) {
            info += format("Responsible for: \n%s", clicked.getAnnotationsAsString());
        }

        if (clicked.getSequence().length() > 0) {
            info += format("Seq: %s\n", clicked.getSequence());
        }

        builder.append(info);

        mainController.getListFactory().modifyNodeInfo(builder.toString());

        if (prevClick == null) {
            prevClick = node;
            focusedNode = clicked;
            originallyFocusedNode = clicked;
            mainController.getGraphController().addNodeIdToZoomPath(clicked.getId());
            node.originalFocus();
        } else if (prevClick.getCellId() != node.getCellId()) {
            mainController.getGraphController().sideFocus(false);
            mainController.getGraphController().clearZoomPath();
            prevClick.resetFocus();
            node.originalFocus();
            prevClick = node;
            this.focusedNode = clicked;
            this.originallyFocusedNode = clicked;
            mainController.getGraphController().addNodeIdToZoomPath(clicked.getId());
        } else if (prevClick.getCellId() == node.getCellId()) {
            mainController.getGraphController().sideFocus(false);
            mainController.getGraphController().clearZoomPath();
            prevClick.resetFocus();
            prevClick = null;
            focusedNode = null;
            originallyFocusedNode = null;
        }
    };

    private EventHandler<MouseEvent> onMouseDraggedEventHandler = event -> {
        GraphCell node = (GraphCell) event.getSource();
        mainController.getGraphController().getRoot().setPannable(false);

        double offsetX = event.getX() + node.getLayoutX()
                - node.getCellShape().getLayoutBounds().getWidth() / 2;
        double offsetY = event.getY() + node.getLayoutY()
                - node.getCellShape().getLayoutBounds().getHeight() / 2;

        event.getSceneX();
        node.relocate(offsetX, offsetY);
        event.consume();

        mainController.getGraphController().getRoot().setPannable(true);
    };

    private EventHandler<MouseEvent> onMouseEnteredEventHandler = event -> {
        Cell cell = (Cell) event.getSource();
        cell.setCursor(Cursor.HAND);
    };

    /**
     * Class constructor.
     *
     * @param m the mainController.
     */
    @SuppressFBWarnings("URF_UNREAD_FIELD")
    public GraphMouseHandling(MainController m) {
        this.mainController = m;
        this.prevClick = null;
        this.focusedNode = null;
    }

    /**
     * Assign mouse events handlers to a given Node.
     *
     * @param node Node to get mouse handlers assigned.
     */
    public void setMouseHandling(final Node node) {
        node.setOnMousePressed(onMousePressedEventHandler);
        node.setOnMouseEntered(onMouseEnteredEventHandler);
        node.setOnMouseDragged(onMouseDraggedEventHandler);
    }

    /**
     * Getter for the prevClick
     *
     * @return the Cell that is last clicked.
     */
    public GraphCell getPrevClick() {
        return prevClick;
    }

    /**
     * Setter for the prevClick.
     *
     * @param prevClick the cell to set to.
     */
    public void setPrevClick(GraphCell prevClick) {
        this.prevClick = prevClick;
    }

    /**
     * Getter for the focused Node
     * @return the node that is focused.
     */
    public core.graph.Node getFocusedNode() {
        return focusedNode;
    }

    /**
     * Setter for the focused Node
     * @param focusedNode the node that is to be focused.
     */
    public void setFocusedNode(core.graph.Node focusedNode) {
        this.focusedNode = focusedNode;
    }

    /**
     * Getter for the originally focused Node
     * @return the node that is focused originally.
     */
    public core.graph.Node getOriginallyFocusedNode() {
        return originallyFocusedNode;
    }

    /**
     * Getter for the zoom level at which the
     * originally focused node resides.
     * @return the zoom level
     */
    public int getOriginalZoomLevel() {
        return originalZoomLevel;
    }
}