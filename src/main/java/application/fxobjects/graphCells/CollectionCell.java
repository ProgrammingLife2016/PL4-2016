package application.fxobjects.graphCells;

import core.typeEnums.CellType;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;


/**
 * Class representing a collection of horizontally collapsed cells.
 */
public class CollectionCell extends GraphCell {

    /**
     * Bubble cell constructor.
     * @param id            The ID of a cell.
     * @param nucleotides The amount of nucleotides contained in this cell.
     * @param collapseLevel The collapse level of a cell.
     */
    public CollectionCell(int id, int nucleotides, String collapseLevel) {
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
    public CollectionCell(int id, int nucleotides, StackPane pane, Text text) {
        super(id);
        shape = new Circle(Math.min(10.0 + ((double) nucleotides) / 80000, 100));
        shape.setStroke(Color.LIGHTGREEN);
        shape.setStrokeWidth(1);
        shape.setFill(Color.LIGHTGREEN);
        pane.getChildren().addAll(shape, text);
        setView(pane);

        type = CellType.COLLECTION;
    }

    /**
     * Method to reset the focus.
     */
    public void resetFocus() {
        this.setEffect(null);
        shape.setStroke(Color.LIGHTGREEN);
        shape.setStrokeWidth(1);
    }
}