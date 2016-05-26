package application.controllers;

import application.fxobjects.cell.Cell;

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
    private double nodePositionX;
    private double nodePositionY;

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
        Cell node = (Cell) event.getSource();
        
        nodePositionX = node.getLayoutX();
        nodePositionY = node.getLayoutY();

        double offsetX = event.getX();
        double offsetY = event.getY();

        node.relocate(nodePositionX + offsetX, nodePositionY + offsetY);
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
}