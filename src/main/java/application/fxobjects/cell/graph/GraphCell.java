package application.fxobjects.cell.graph;

import application.fxobjects.cell.Cell;
import core.graph.cell.CellType;
import javafx.scene.effect.DropShadow;
import javafx.scene.paint.Color;
import javafx.scene.shape.Shape;

/**
 * Interface defining overlapping cell behaviour.
 *
 * @author Niels Warnars
 */
public abstract class GraphCell extends Cell {
    public Shape shape = null;

    /**
     * Class constructor.
     *
     * @param cellId The ID of the cell.
     */
    public GraphCell(int cellId) {
        super(cellId);
    }

    /**
     * Returns the cellshape.
     * @return the cellshape.
     */
    public Shape getCellShape() { return shape; }

    /**
     * Method to set the focus.
     */
    public void focus() {
        DropShadow borderGlow = new DropShadow();
        borderGlow.setOffsetY(0f);
        borderGlow.setOffsetX(0f);
        borderGlow.setColor(Color.BLACK);
        borderGlow.setWidth(70);
        borderGlow.setHeight(70);
        this.setEffect(borderGlow);

        shape.setStroke(Color.PURPLE);
        shape.setStrokeWidth(4);
    }

    /**
     * Method to set the side focus.
     */
    public void sideFocus() {
        DropShadow borderGlow = new DropShadow();
        borderGlow.setOffsetY(0f);
        borderGlow.setOffsetX(0f);
        borderGlow.setColor(Color.BLACK);
        borderGlow.setWidth(40);
        borderGlow.setHeight(40);
        this.setEffect(borderGlow);

        shape.setStroke(Color.MEDIUMPURPLE);
        shape.setStrokeWidth(4);
    }

    /**
     * Method to set the original focus.
     */
    public void originalFocus() {
        DropShadow borderGlow = new DropShadow();
        borderGlow.setOffsetY(0f);
        borderGlow.setOffsetX(0f);
        borderGlow.setColor(Color.ORANGERED);
        borderGlow.setWidth(70);
        borderGlow.setHeight(70);
        this.setEffect(borderGlow);

        shape.setStroke(Color.ORANGE);
        shape.setStrokeWidth(4);
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
     * Abstract method to reset the focus.
     */
    public abstract void resetFocus();
}