package application.controllers;

import application.fxobjects.cell.Cell;
import application.fxobjects.cell.Edge;
import application.fxobjects.cell.tree.LeafCell;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.List;

/**
 * Class responsible for the handling of mouse events.
 */
public class TreeMouseHandling {
    private MainController mainController;

    private EventHandler<MouseEvent> onMousePressedEventHandler = event -> {
        if(event.getSource() instanceof Cell) {
            Cell node = (Cell) event.getSource();
            Cell clicked = mainController.getTreeController().getPT()
                    .getModel().getAddedCells().get(node.getCellId());
        }
        else {
            Edge node = (Edge) event.getSource();
//            Edge clicked = mainController.getTreeController().getPT()
//                    .getModel().getAddedCells().get(node.get);
        }
    };

    private EventHandler<MouseEvent> onMouseEnteredEventHandler = event -> {
        if(event.getSource() instanceof Cell) {
            Cell node = (Cell) event.getSource();
            List<Cell> parentList = new ArrayList<>();
            parentList.add(node);

            while(!parentList.isEmpty()) {
                Cell next = parentList.remove(0);
                parentList.addAll(next.getCellParents());

                if (next.getCellId() != 0) {
                    Edge edge = mainController.getTreeController().getPT().getModel().getEdgeFromChild(next);
                    edge.getLine().setStroke(Color.RED);
                    edge.getLine().setStrokeWidth(4.0);
                }
            }
        }
        else {
            Edge node = (Edge) event.getSource();
            List<Cell> parentList = new ArrayList<>();
            parentList.add(node.getTarget());
            node.getLine().setStroke(Color.RED);
            node.getLine().setStrokeWidth(4.0);


            while(!parentList.isEmpty()) {
                Cell next = parentList.remove(0);
                parentList.addAll(next.getCellChildren());

                if(!(next instanceof LeafCell)) {
                    List<Edge> edges = mainController.getTreeController().getPT().getModel().getEdgeFromParent(next);
                    edges.forEach(e -> {
                        e.getLine().setStroke(Color.RED);
                        e.getLine().setStrokeWidth(4.0);
                    });
                }
            }
        }
    };

    private EventHandler<MouseEvent> onMouseExitedEventHandler = event -> {
        if(event.getSource() instanceof Cell) {
            Cell node = (Cell) event.getSource();
            List<Cell> childList = new ArrayList<>();
            childList.add(node);

            while(!childList.isEmpty()) {
                Cell next = childList.remove(0);
                childList.addAll(next.getCellParents());

                if(next.getCellId() != 0) {
                    Edge edge = mainController.getTreeController().getPT().getModel().getEdgeFromChild(next);
                    edge.getLine().setStroke(Color.BLACK);
                    edge.getLine().setStrokeWidth(1.0);
                }
            }
        }
        else {
            Edge node = (Edge) event.getSource();
            List<Cell> childList = new ArrayList<>();
            childList.add(node.getTarget());
            node.getLine().setStroke(Color.BLACK);
            node.getLine().setStrokeWidth(1.0);

            while(!childList.isEmpty()) {
                Cell next = childList.remove(0);
                childList.addAll(next.getCellChildren());

                if(!(next instanceof LeafCell)) {
                    List<Edge> edges = mainController.getTreeController().getPT().getModel().getEdgeFromParent(next);
                    edges.forEach(e -> {
                        e.getLine().setStroke(Color.BLACK);
                        e.getLine().setStrokeWidth(1.0);
                    });
                }
            }
        }
    };

    /**
     * Class constructor.
     *
     * @param m the mainController.
     */
    @SuppressFBWarnings("URF_UNREAD_FIELD")
    TreeMouseHandling(MainController m) {
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
    }
}
