package application.fxobjects.cell.graph;

import application.fxobjects.cell.Cell;
import core.graph.cell.CellType;
import javafx.scene.effect.DropShadow;
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
    private boolean selected;

    /**
     * Bubble cell constructor.
     *
     * @param id            The ID of a cell.
     * @param collapseLevel The collapse level of a cell.
     */
    public CollectionCell(int id, int nucleotides, String collapseLevel) {
        this(id, nucleotides,new StackPane(), new Text(collapseLevel));
    }

    /**
     * Bubble cell constructor.
     *
     * @param id   The ID of a cell.
     * @param pane A given stack pane.
     * @param text A given text element.
     */
    public CollectionCell(int id, int nucleotides, StackPane pane, Text text) {
        super(id);

        shape = new Circle(Math.min(10.0 + ((double)nucleotides) / 80000, 100));
        shape.setStroke(Color.LIGHTGREEN);
        shape.setStrokeWidth(1);
        shape.setFill(Color.LIGHTGREEN);
        pane.getChildren().addAll(shape, text);
        this.selected = false;

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
        this.setEffect(null);
        this.selected = false;
        shape.setStroke(Color.LIGHTGREEN);
        shape.setStrokeWidth(1);
    }

}