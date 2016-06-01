package application.fxobjects.cell.graph;

import application.fxobjects.cell.Cell;
import core.graph.cell.CellType;
import javafx.scene.effect.DropShadow;
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
     * @param id The ID of a cell.
     */
    public RectangleCell(int id) {
        this(id, new StackPane());
    }

    /**
     * Rectangle cell constructor.
     *
     * @param id   The ID of a cell.
     * @param pane A given stack pane.
     */
    public RectangleCell(int id, StackPane pane) {
        super(id);

        pane.setMaxHeight(10);
        this.selected = false;
        this.view = new Rectangle(10, 10);
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
        DropShadow borderGlow = new DropShadow();
        borderGlow.setOffsetY(0f);
        borderGlow.setOffsetX(0f);
        borderGlow.setColor(Color.BLACK);
        borderGlow.setWidth(70);
        borderGlow.setHeight(70);
        this.setEffect(borderGlow);

        this.selected = true;
        this.view.setStroke(Color.PURPLE);
        this.view.setStrokeWidth(4);
    }

    /**
     * Method to reset the focus.
     */
    public void resetFocus() {
        this.selected = false;
        this.setEffect(null);
        this.view.setStroke(Color.DODGERBLUE);
        this.view.setStrokeWidth(1);
    }
}