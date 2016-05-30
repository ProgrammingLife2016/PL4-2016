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
        Rectangle view = new Rectangle(10, 10);
        view.setStroke(Color.DODGERBLUE);
        view.setFill(Color.DODGERBLUE);

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

}