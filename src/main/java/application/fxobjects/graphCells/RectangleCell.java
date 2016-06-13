package application.fxobjects.graphCells;

import core.typeEnums.CellType;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;


/**
 * Class representing a Rectangle shape. *
 */
public class RectangleCell extends GraphCell {

    private Boolean highlighted;

    /**
     * Rectangle cell constructor.
     * @param id The ID of a cell.
     * @param nucleotides The amount of nucleotides contained in this cell.
     */
    public RectangleCell(int id, int nucleotides) {
        this(id, nucleotides, new StackPane());
    }

    /**
     * Rectangle cell constructor.
     *
     * @param id   The ID of a cell.
     * @param nucleotides The amount of nucleotides contained in this cell.
     * @param pane A given stack pane.
     */
    public RectangleCell(int id, int nucleotides, StackPane pane) {
        super(id);
        pane.setMaxHeight(10);
        double sideSize = Math.min(10.0 + ((double) nucleotides) / 80000, 100);
        shape = new Rectangle(sideSize, sideSize);
        shape.setStroke(Color.DODGERBLUE);
        shape.setStrokeWidth(1);
        shape.setFill(Color.DODGERBLUE);
        pane.getChildren().addAll(shape);
        setView(pane);

        type = CellType.RECTANGLE;
        highlighted = false;
    }

    /**
     * Highlight the rectangle cell.
     */
    public void setHighLight() {
        highlighted = true;

        if (!shape.getStroke().equals(Color.YELLOW) && shape.getStrokeWidth() != 4) {
            shape.setStroke(Color.YELLOW);
            shape.setStrokeWidth(4);
        }
    }

    /**
     * Deselect the highlight on the rectangle cell.
     */
    public void deselectHighLight() {
        highlighted = false;

        if (shape.getStroke().equals(Color.YELLOW) && shape.getStrokeWidth() == 4) {
            shape.setStroke(Color.DODGERBLUE);
            shape.setStrokeWidth(1);
        }
    }
    /**
     * Method to reset the focus.
     */
    public void resetFocus() {
        this.setEffect(null);
        shape.setStroke(Color.DODGERBLUE);
        shape.setStrokeWidth(1);

        if (highlighted) {
            shape.setStroke(Color.YELLOW);
            shape.setStrokeWidth(4);
        }
    }
}