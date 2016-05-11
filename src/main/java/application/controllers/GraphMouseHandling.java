package application.controllers;

import application.fxobjects.graph.cell.Cell;
import core.graph.Graph;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;

/**
 * Class responsible for the handling of mouse events
 */
public class GraphMouseHandling {

    final DragContext dragContext = new DragContext();

    Graph graph;
    EventHandler<MouseEvent> onMousePressedEventHandler = event -> {
        Cell node = (Cell) event.getSource();
        //graph.addGraphComponents(null);
        System.out.println(node.getCellId());
    };

    EventHandler<MouseEvent> onMouseDraggedEventHandler = event -> {
        //Node node = (Node) event.getSource();
        Cell node = (Cell) event.getSource();
        System.out.println(node.getCellId());
    };


    EventHandler<MouseEvent> onMouseReleasedEventHandler = event -> {

    };

    EventHandler<MouseEvent> onMouseEnteredEventHandler = event -> {
        Cell cell = (Cell) event.getSource();
        cell.getText().setVisible(true);

    };

    EventHandler<MouseEvent> onMouseExitedEventHandler = event -> {
        Cell cell = (Cell) event.getSource();
        cell.getText().setVisible(false);
    };

    /**
     * Class constructor.
     *
     * @param graph A given graph.
     */
    public GraphMouseHandling(Graph graph) {
        this.graph = graph;
    }

    /**
     * Assign mouse events handlers to a given Node.
     *
     * @param node Node to get mouse handlers assigned.
     */
    public void setMouseHandling(final Node node) {
        node.setOnMousePressed(onMousePressedEventHandler);
        //node.setOnMouseDragged(onMouseDraggedEventHandler);
        node.setOnMouseReleased(onMouseReleasedEventHandler);
        node.setOnMouseEntered(onMouseEnteredEventHandler);
        node.setOnMouseExited(onMouseExitedEventHandler);
    }

    /**
     * Used for dragging of nodes.
     * Unused at this point of time.
     */
    class DragContext {

        double x;
        double y;

    }
}