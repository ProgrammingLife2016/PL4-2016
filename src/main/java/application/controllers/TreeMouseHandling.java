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

    /**
     * Selects or unselects (a) given strain(s).
     */
    private EventHandler<MouseEvent> onMousePressedEventHandler = event -> {
            mainController.getTreeController().selectStrains();
    };

    private EventHandler<MouseEvent> onMouseEnteredEventHandler = event -> {
        if(event.getSource() instanceof Cell) {
            mainController.getTreeController().applyCellHighlight((Cell) event.getSource());
        }
        else {
            mainController.getTreeController().applyEdgeHighlight((Edge) event.getSource());

        }
    };

    private EventHandler<MouseEvent> onMouseExitedEventHandler = event -> {
        if(event.getSource() instanceof Cell) {
            mainController.getTreeController().revertCellHighlight((Cell) event.getSource());
        }
        else {
            mainController.getTreeController().revertEdgeHighlight((Edge) event.getSource());
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
