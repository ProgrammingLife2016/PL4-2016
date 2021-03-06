package application.fxobjects;

import core.typeEnums.EdgeType;
import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;

import static core.typeEnums.EdgeType.GRAPH_REF;
import static core.typeEnums.EdgeType.TREE;

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
     * @param width  width of the edge.
     * @param type   the type of the edge (graph or tree edge)
     */
    public Edge(Cell source, Cell target, int width, EdgeType type) {
        this.source = source;
        this.target = target;

        source.addCellChild(target);
        target.addCellParent(source);

        line = new Line();

        width /= 1;

        if (type == TREE) {
            line.startXProperty().bind(source.layoutXProperty());
            line.startYProperty().bind(source.layoutYProperty());
            line.endXProperty().bind(target.layoutXProperty());
            line.endYProperty().bind(target.layoutYProperty());
        } else {
            line.setStrokeWidth(Math.max(width, 1));
            line.startXProperty().bind(source.layoutXProperty().add(
                    source.getBoundsInParent().getWidth() / 2.0));
            line.startYProperty().bind(source.layoutYProperty().add(
                    source.getBoundsInParent().getHeight() / 2.0));

            line.endXProperty().bind(target.layoutXProperty().add(
                    target.getBoundsInParent().getWidth() / 2.0));
            line.endYProperty().bind(target.layoutYProperty().add(
                    target.getBoundsInParent().getHeight() / 2.0));
            if (type == GRAPH_REF) {
                line.setStroke(Color.DARKORANGE);

            }
        }


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

    /**
     * Tostring method for this method.
     *
     * @return this object as a string.
     */
    public String toString() {
        return ("(" + source.getCellId() + "," + target.getCellId() + ")");
    }

    /**
     * Returns the length of this Edge.
     *
     * @return the Edge.
     */
    public double getLength() {
        double xLength = getLine().endXProperty().get()
                - getLine().startXProperty().get();
        double yLength = getLine().endYProperty().get()
                - getLine().startYProperty().get();
        return Math.sqrt(Math.pow(xLength, 2) + Math.pow(yLength, 2));
    }
}