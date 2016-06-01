package application.fxobjects.cell.graph;

import application.fxobjects.cell.Cell;
import core.graph.cell.CellType;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.scene.text.Text;

/**
 * Class representing a Rectangle shape. *
 */
public class RectangleCell extends Cell {
    private final CellType type = CellType.RECTANGLE;
    private Text text;
    private Shape shape;

    /**
     * Rectangle cell constructor.
     *
     * @param id  The ID of a cell.
     * @param seq The genome sequence of a cell.
     */
    public RectangleCell(int id, String seq) {
        super(id);
        StackPane pane = new StackPane();
        pane.setMaxHeight(10);
        text = new Text(seq);
        text.setVisible(false);
        text.setManaged(false);
        shape = new Rectangle(10, 10);
        shape.setStroke(Color.DODGERBLUE);
        shape.setFill(Color.DODGERBLUE);
        pane.getChildren().addAll(shape, text);

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
     * Return the Cell's text.
     *
     * @return the Cell's text.
     */
    public Text getText() {
        return text;
    }

    /**
     * Returns the cellshape.
     * @return the cellshape.
     */
    public Shape getCellShape() { return shape; }
}