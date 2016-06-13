package application.fxobjects.graphCells;

import core.typeEnums.CellType;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;


/**
 * Class representing a collapsed bubble.
 */
public class BubbleCell extends GraphCell {

    /**
     * Bubble cell constructor.
     *
     * @param id            The ID of a cell.
     * @param nucleotides The amount of nucleotides contained in this cell.
     * @param collapseLevel The collapse level of this cell.
     */
    public BubbleCell(int id, int nucleotides, String collapseLevel) {
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
    public BubbleCell(int id, int nucleotides, StackPane pane, Text text) {
        super(id);
        shape = new Circle(Math.min(10.0 + ((double) nucleotides) / 80000, 100));
        shape.setStroke(Color.YELLOW);
        shape.setStrokeWidth(1);
        shape.setFill(Color.YELLOW);
        pane.getChildren().addAll(shape, text);
        text.setTextAlignment(TextAlignment.CENTER);
        setView(pane);

        type = CellType.BUBBLE;
    }

    /**
     * Method to reset the focus.
     */
    public void resetFocus() {
        this.setEffect(null);
        shape.setStroke(Color.YELLOW);
        shape.setStrokeWidth(1);
    }
}