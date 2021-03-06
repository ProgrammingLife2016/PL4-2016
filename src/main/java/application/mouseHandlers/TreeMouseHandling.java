package application.mouseHandlers;

import application.controllers.MainController;
import application.fxobjects.Edge;
import application.fxobjects.treeCells.LeafCell;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;

/**
 * Class responsible for the handling of mouse events.
 */
public class TreeMouseHandling {
    private MainController mainController;

    /**
     * Selects or deselects (a) given strain(s).
     */
    private EventHandler<MouseEvent> onMousePressedEventHandler = event -> {
            mainController.getTreeController().selectStrains();
    };

    /**
     * Highlights strains based on hovering over them or corresponding Cells.
     */
    private EventHandler<MouseEvent> onMouseEnteredEventHandler = event -> {
        if (event.getSource() instanceof LeafCell) {
            mainController.getTreeController().applyCellHighlight((LeafCell) event.getSource());
        } else if (event.getSource() instanceof Edge) {

            mainController.getTreeController().applyEdgeHighlight((Edge) event.getSource());

        }
    };

    /**
     * Removes highlights from strains based on hovering over them or corresponding Cells.
     */
    private EventHandler<MouseEvent> onMouseExitedEventHandler = event -> {
        if (event.getSource() instanceof LeafCell) {
            mainController.getTreeController().revertCellHighlight((LeafCell) event.getSource());
        } else if (event.getSource() instanceof Edge) {
            mainController.getTreeController().revertEdgeHighlight((Edge) event.getSource());
        }
        mainController.getTreeController().colorSelectedStrains();
    };

    /**
     * Class constructor.
     *
     * @param m the mainController.
     */
    @SuppressFBWarnings("URF_UNREAD_FIELD")
    public TreeMouseHandling(MainController m) {
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
