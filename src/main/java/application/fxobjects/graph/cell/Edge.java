package application.fxobjects.graph.cell;

import javafx.scene.Group;
import javafx.scene.shape.Line;

/**
 * A class that represent an Edge between 2 nodes.
 */
public class Edge extends Group {
    private Cell source;
    private Cell target;
    private Line line;

    /**
     * Edge constructor.
     *
     * @param source cell for edge start.
     * @param target cell for edge destination.
     * @param width the width of the Edge.
     */
    public Edge(Cell source, Cell target, int width) {

        this.source = source;
        this.target = target;

        source.addCellChild(target);
        target.addCellParent(source);

        line = new Line();

        width /= 1;

        line.setStrokeWidth(Math.max(width, 1));
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
     * Getter Function for the source cell.
     *
     * @return the source cell.
     */
    public Cell getSource() {
        return source;
    }

    /**
     * Setter method for the source cell.
     *
     * @param source cell to set as source.
     */
    public void setSource(Cell source) {
        this.source = source;
    }

    /**
     * Getter function for the destination cell.
     *
     * @return the target cell.
     */
    public Cell getTarget() {
        return target;
    }

    /**
     * Setter method for the destination cell.
     *
     * @param target cell to set as destination.
     */
    public void setTarget(Cell target) {
        this.target = target;
    }

    /**
     * Getter method for the Line.
     *
     * @return the Line.
     */
    public Line getLine() {
        return line;
    }

    /**
     * Setter method for the Line.
     *
     * @param line the Line to set as line.
     */
    public void setLine(Line line) {
        this.line = line;
    }
}