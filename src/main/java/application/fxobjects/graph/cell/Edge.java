package application.fxobjects.graph.cell;

import javafx.scene.Group;
import javafx.scene.shape.Line;

/**
 * A class that represent an Edge between 2 nodes.
 */
public class Edge extends Group {

    protected Cell source;
    protected Cell target;

    Line line;

    /**
     * Edge constructor.
     * @param source From.
     * @param target To.
     * @param width The thickness of the edge when drawn.
     */
    public Edge(Cell source, Cell target, int width) {

        this.source = source;
        this.target = target;

        source.addCellChild(target);
        target.addCellParent(source);

        line = new Line();

        line.setStrokeWidth(width);
        line.startXProperty().bind(source.layoutXProperty().add(
        		source.getBoundsInParent().getWidth() / 2.0));
        line.startYProperty().bind(source.layoutYProperty().add(
        		source.getBoundsInParent().getHeight() / 2.0));

        line.endXProperty().bind(target.layoutXProperty().add(
        		target.getBoundsInParent().getWidth() / 2.0));
        line.endYProperty().bind(target.layoutYProperty().add(
        		target.getBoundsInParent().getHeight() / 2.0));

        getChildren().add(line);

    }

    /**
     * Getter Function.
     * @return the source.
     */
    public Cell getSource() {
        return source;
    }

    /**
     * Getter function.
     * @return the target.
     */
    public Cell getTarget() {
        return target;
    }

}