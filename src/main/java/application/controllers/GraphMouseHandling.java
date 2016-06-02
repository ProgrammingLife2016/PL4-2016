package application.controllers;

import application.fxobjects.cell.Cell;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import javafx.event.EventHandler;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;

import static java.lang.String.format;

/**
 * Class responsible for the handling of mouse events
 */
class GraphMouseHandling {
    private MainController mainController;
    private Cell prevClick;

    private EventHandler<MouseEvent> onMousePressedEventHandler = event -> {
        Cell node = (Cell) event.getSource();

        core.Node clicked = mainController.getGraphController().getGraph()
                .getModel().getLevelMaps().get(mainController.getCurrentView())
                .get(node.getCellId());

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

        mainController.getListFactory().modifyNodeInfo(info);

        if (prevClick == null) {
            prevClick = node;
            node.focus();
        } else if (prevClick.getCellId() != node.getCellId()) {
            prevClick.resetFocus();
            node.focus();
            prevClick = node;
        } else if (prevClick.getCellId() == node.getCellId()) {
            prevClick.resetFocus();
            prevClick = null;
        }
    };

    private EventHandler<MouseEvent> onMouseDraggedEventHandler = event -> {
        Node node = (Node) event.getSource();

        double offsetX = event.getX() + node.getLayoutX();
        double offsetY = event.getY() + node.getLayoutY();

        event.getSceneX();
        node.relocate(offsetX, offsetY);
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
    GraphMouseHandling(MainController m) {
        this.mainController = m;
        this.prevClick = null;
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
        node.setOnDragDetected(onMouseDraggedEventHandler);
    }

    /**
     * Getter for the prevClick
     *
     * @return the Cell that is last clicked.
     */
    public Cell getPrevClick() {
        return prevClick;
    }

    /**
     * Setter for the prevClick.
     *
     * @param prevClick the cell to set to.
     */
    public void setPrevClick(Cell prevClick) {
        this.prevClick = prevClick;
    }
}