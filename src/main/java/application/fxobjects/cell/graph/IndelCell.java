package application.fxobjects.cell.graph;

import core.graph.cell.CellType;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import javafx.scene.text.Text;


/**
 * Class representing a collapsed indel.
 */
public class IndelCell extends GraphCell {

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
        double sideSize = Math.min(20.0 + ((double) nucleotides) / 80000, 100);
        shape = new Polygon(sideSize / 2, 0, sideSize, sideSize, 0, sideSize);
        shape.setStroke(Color.RED);
        shape.setStrokeWidth(1);
        shape.setFill(Color.RED);
        pane.getChildren().addAll(shape, text);
        setView(pane);

        type = CellType.INDEL;
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