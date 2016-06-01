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
    public RectangleCell(int id) {
        this(id, new StackPane());
    }
    /**
     * Rectangle cell constructor.
     *
     * @param id  The ID of a cell.
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

    public void focus() {
        this.selected = true;
        this.view.setStroke(Color.PURPLE);
        this.view.setStrokeWidth(4);
    }

    public void resetFocus() {
        this.selected = false;
        this.view.setStroke(Color.DODGERBLUE);
        this.view.setStrokeWidth(1);
    }
}