package application.fxobjects.cell.graph;

import application.fxobjects.cell.Cell;
import core.graph.cell.CellType;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

/**
 * Class representing a Rectangle shape. *
 */
public class RectangleCell extends Cell {
    private final CellType type = CellType.RECTANGLE;
    private Rectangle view;
    private boolean selected;

    /**
     * Rectangle cell constructor.
     *
     * @param id  The ID of a cell.
     */
    public RectangleCell(int id, int nucleotides) {
        this(id, nucleotides, new StackPane());
    }
    /**
     * Rectangle cell constructor.
     *
     * @param id  The ID of a cell.
     * @param pane A given stack pane.
     */
    public RectangleCell(int id, int nucleotides, StackPane pane) {
        super(id);

        pane.setMaxHeight(10);
        this.selected = false;
        double sideSize = Math.min(10.0 + ((double) nucleotides) / 80000, 100);
        this.view = new Rectangle(sideSize , sideSize);
        this.view.setStroke(Color.DODGERBLUE);
        this.view.setStrokeWidth(1);
        this.view.setFill(Color.DODGERBLUE);

        pane.getChildren().addAll(view);
        setView(pane);
    }

    /**
     * Return the type of the Cell.
     *
     * @return the type of the Cell.
     */
    public CellType getType() {
        return type;
    }

    /**
     * Method to set the focus.
     */
    public void focus() {
        this.selected = true;
        this.view.setStroke(Color.PURPLE);
        this.view.setStrokeWidth(4);
    }

    /**
     * Method to reset the focus.
     */
    public void resetFocus() {
        this.selected = false;
        this.view.setStroke(Color.DODGERBLUE);
        this.view.setStrokeWidth(1);
    }
}