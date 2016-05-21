package application.controllers;

import application.fxobjects.graph.cell.Cell;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import javafx.event.EventHandler;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;

/**
 * Class responsible for the handling of mouse events
 */
class GraphMouseHandling {
    private MainController mainController;

    final DragContext dragContext = new DragContext();

    private EventHandler<MouseEvent> onMousePressedEventHandler = event -> {
        Cell node = (Cell) event.getSource();

        core.Node clicked = mainController.getGraphController().getGraph()
                .getModel().getLevelMaps().get(0).get(node.getCellId());

        String info = "";
        info += "Genome ID: " + clicked.getId() + "\n";
        info += clicked.getGenomes().size() + " Genomes in Node: \n"
                + clicked.getGenomesAsString() + "\n";
        info += "Seq: \n" + clicked.getSequence() + "\n";

        mainController.modifyNodeInfo(info);
    };

    private EventHandler<MouseEvent> onMouseDraggedEventHandler = event -> {
        Node node = (Node) event.getSource();

        double offsetX = event.getSceneX();
        double offsetY = event.getSceneY();
        event.getSceneX();
        // adjust the offset in case we are zoomed
        double scale = 1;
        offsetX /= scale;
        offsetY /= scale;
        node.relocate(offsetX, offsetY);
    };


    private EventHandler<MouseEvent> onMouseEnteredEventHandler = event -> {
        Cell cell = (Cell) event.getSource();
        cell.setCursor(Cursor.HAND);
    };

    private EventHandler<MouseEvent> onMouseExitedEventHandler = event -> {
        Cell cell = (Cell) event.getSource();
        cell.getText().setVisible(false);
    };

    /**
     * Class constructor.
     *
     * @param m the mainController.
     */
    @SuppressFBWarnings("URF_UNREAD_FIELD")
    public GraphMouseHandling(MainController m) {
        this.mainController = m;
    }

    /**
     * Assign mouse events handlers to a given Node.
     *
     * @param node Node to get mouse handlers assigned.
     */
    public void setMouseHandling(final Node node) {
        node.setOnMousePressed(onMousePressedEventHandler);
        node.setOnMouseEntered(onMouseEnteredEventHandler);
        node.setOnMouseExited(onMouseExitedEventHandler);
        node.setOnDragDetected(onMouseDraggedEventHandler);
    }

    /**
     * Used for dragging of nodes.
     * Unused at this point of time.
     */
    @SuppressFBWarnings({"SIC_INNER_SHOULD_BE_STATIC", "UUF_UNUSED_FIELD"})
    static class DragContext {
        double x;
        double y;
    }
}