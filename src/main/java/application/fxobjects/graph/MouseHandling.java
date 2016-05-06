package application.fxobjects.graph;

import application.fxobjects.graph.cell.Cell;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;

/**
 * Class responsible for the handling of mouse events
 */
public class MouseHandling {
    final DragContext dragContext = new DragContext();

    Graph graph;
    EventHandler<MouseEvent> onMousePressedEventHandler = new EventHandler<MouseEvent>() {

        @Override
        public void handle(MouseEvent event) {
            Cell node = (Cell) event.getSource();
            System.out.println(node.getCellId());
        }
    };

   /* EventHandler<MouseEvent> onMouseDraggedEventHandler = new EventHandler<MouseEvent>() {

        @Override
        public void handle(MouseEvent event) {
            Node node = (Node) event.getSource();

            double offsetX = event.getScreenX() + dragContext.x;
            double offsetY = event.getScreenY() + dragContext.y;

            // adjust the offset in case we are zoomed
            double scale = graph.getScale();

            offsetX /= scale;
            offsetY /= scale;

            node.relocate(offsetX, offsetY);

        }
    };*/

    EventHandler<MouseEvent> onMouseReleasedEventHandler = new EventHandler<MouseEvent>() {

        @Override
        public void handle(MouseEvent event) {

        }
    };

    EventHandler<MouseEvent> onMouseEnteredEventHandler = new EventHandler<MouseEvent>() {
        @Override
        public void handle(MouseEvent event) {
            Cell cell = (Cell) event.getSource();
            cell.getText().setVisible(true);

        }
    };

    EventHandler<MouseEvent> onMouseExitedEventHandler = new EventHandler<MouseEvent>() {
        @Override
        public void handle(MouseEvent event) {
            Cell cell = (Cell) event.getSource();
            cell.getText().setVisible(false);
        }
    };

    /**
     * Class constructor.
     * @param graph A given graph.
     */
    public MouseHandling(Graph graph) {
        this.graph = graph;
    }

    /**
     * Assign mouse events handlers to a given Node.
     * @param node Node to get mouse handlers assigned.
     */
    public void setMouseHandling(final Node node) {
        node.setOnMousePressed(onMousePressedEventHandler);
        //node.setOnMouseDragged(onMouseDraggedEventHandler);
        node.setOnMouseReleased(onMouseReleasedEventHandler);
        node.setOnMouseEntered(onMouseEnteredEventHandler);
        node.setOnMouseExited(onMouseExitedEventHandler);
    }

    class DragContext {

        double x;
        double y;

    }
}