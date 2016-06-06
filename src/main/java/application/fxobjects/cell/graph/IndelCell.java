package application.fxobjects.cell.graph;

import application.fxobjects.cell.Cell;
import core.graph.cell.CellType;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Shape;
import javafx.scene.text.Text;

/**
 * Class representing a collapsed indel.
 */
public class IndelCell extends Cell {
    private final CellType type = CellType.INDEL;
    private Shape shape;

    /**
     * Indel cell constructor.
     * @param id            The ID of a cell.
     * @param nucleotides The amount of nucleotides contained in this cell.
     * @param collapseLevel The collapse level of a cell.
     */
    public IndelCell(int id, int nucleotides, String collapseLevel) {
        this(id, nucleotides, new StackPane(), new Text(collapseLevel));
    }

    /**
     * Indel cell constructor.
     *
     * @param id   The ID of a cell.
     * @param nucleotides The amount of nucleotides contained in this cell.
     * @param pane A given stack pane.
     * @param text A given text element.
     */
    public IndelCell(int id, int nucleotides, StackPane pane, Text text) {
        super(id);
        double sideSize = Math.min(10.0 + ((double) nucleotides) / 80000, 100);
        shape = new Polygon(sideSize / 2, 0, sideSize, sideSize, 0, sideSize);
        shape.setStroke(Color.RED);
        shape.setStrokeWidth(1);
        shape.setFill(Color.RED);
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
     * Method to reset the focus.
     */
    public void resetFocus() {
        this.setEffect(null);
        shape.setStroke(Color.RED);
        shape.setStrokeWidth(1);
    }
}