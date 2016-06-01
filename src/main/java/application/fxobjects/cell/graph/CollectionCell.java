package application.fxobjects.cell.graph;

import application.fxobjects.cell.Cell;
import core.graph.cell.CellType;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Shape;
import javafx.scene.text.Text;

/**
 * Created by Niek on 5/25/2016.
 */
public class CollectionCell extends Cell {

    private final CellType type = CellType.COLLECTION;
    private Text text;
    private Shape shape;

    /**
     * Bubble cell constructor.
     *
     * @param id            The ID of a cell.
     * @param collapseLevel The collapse level of a cell.
     */
    public CollectionCell(int id, String collapseLevel) {
        this(id, new StackPane(), new Text(collapseLevel));
    }

    /**
     * Bubble cell constructor.
     *
     * @param id            The ID of a cell.
     * @param pane          A given stack pane.
     * @param text          A given text element.
     */
    public CollectionCell(int id, StackPane pane, Text text) {
        super(id);

        shape = new Circle(10);
        shape.setStroke(Color.LIGHTGREEN);
        shape.setFill(Color.LIGHTGREEN);
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