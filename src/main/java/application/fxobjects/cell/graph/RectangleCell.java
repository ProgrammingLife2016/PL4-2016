package application.fxobjects.cell.graph;

import application.fxobjects.cell.Cell;
import core.graph.cell.CellType;
import javafx.scene.effect.DropShadow;
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
    private boolean selected;

    /**
     * Rectangle cell constructor.
     *
     * @param id The ID of a cell.
     */
    public RectangleCell(int id, int nucleotides) {
        this(id, nucleotides, new StackPane());
    }

    /**
     * Rectangle cell constructor.
     *
     * @param id   The ID of a cell.
     * @param pane A given stack pane.
     */
    public RectangleCell(int id, int nucleotides, StackPane pane) {
        super(id);

        pane.setMaxHeight(10);

        this.selected = false;
        double sideSize = Math.min(10.0 + ((double) nucleotides) / 80000, 100);
        shape = new Rectangle(sideSize , sideSize);
        shape.setStroke(Color.DODGERBLUE);
        shape.setStrokeWidth(1);
        shape.setFill(Color.DODGERBLUE);
        pane.getChildren().addAll(shape);

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
        shape.setStroke(Color.PURPLE);
        shape.setStrokeWidth(4);
    }

    /**
     * Method to reset the focus.
     */
    public void resetFocus() {
        this.selected = false;
        this.setEffect(null);
        shape.setStroke(Color.DODGERBLUE);
        shape.setStrokeWidth(1);
    }
}