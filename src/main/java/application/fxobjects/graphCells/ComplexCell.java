package application.fxobjects.graphCells;

import core.typeEnums.CellType;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;

/**
 * ComplexCell class
 */
public class ComplexCell extends GraphCell {
    /**
     * Bubble cell constructor.
     * @param id            The ID of a cell.
     * @param nucleotides The amount of nucleotides contained in this cell.
     * @param collapseLevel The collapse level of a cell.
     */
    public ComplexCell(int id, int nucleotides, String collapseLevel) {
        this(id, nucleotides, new StackPane(), new Text(collapseLevel));
    }

    /**
     * Bubble cell constructor.
     *
     * @param id   The ID of a cell.
     * @param nucleotides The amount of nucleotides contained in this cell.
     * @param pane A given stack pane.
     * @param text A given text element.
     */
    public ComplexCell(int id, int nucleotides, StackPane pane, Text text) {
        super(id);
        shape = new Circle(Math.min(10.0 + ((double) nucleotides) / 80000, 60));
        shape.setStroke(Color.DARKRED);
        shape.setStrokeWidth(1);
        shape.setFill(Color.DARKRED);
        pane.getChildren().addAll(shape, text);
        setView(pane);

        type = CellType.COMPLEX;
    }

    /**
     * Method to reset the focus.
     */
    public void resetFocus() {
        this.setEffect(null);
        shape.setStroke(Color.DARKRED);
        shape.setStrokeWidth(1);
    }
}
