package application.fxobjects.cell.graph;

import application.fxobjects.cell.Cell;
import core.graph.cell.CellType;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import javafx.scene.text.Text;

/**
 * Class representing a collapsed indel.
 */
public class IndelCell extends Cell {
    private final CellType type = CellType.INDEL;
    private Polygon view;
    private boolean selected;

    /**
     * Indel cell constructor.
     *
     * @param id            The ID of a cell.
     * @param collapseLevel The collapse level of a cell.
     */
    public IndelCell(int id,int nucleotides, String collapseLevel) {
        this(id, nucleotides, new StackPane(), new Text(collapseLevel));
    }

    /**
     * Indel cell constructor.
     *
     * @param id            The ID of a cell.
     * @param pane          A given stack pane.
     * @param text          A given text element.
     */
    public IndelCell(int id, int nucleotides, StackPane pane, Text text) {
        super(id);

        double sideSize = Math.min(10.0 + ((double)nucleotides) / 80000, 100);

        this.selected = false;
        this.view = new Polygon(sideSize / 2, 0, sideSize, sideSize, 0, sideSize);
        this.view.setStroke(Color.RED);
        this.view.setStrokeWidth(1);
        this.view.setFill(Color.RED);

        pane.getChildren().addAll(view, text);
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
     * Method to set the focus.
     */
    public void focus() {
        this.selected = true;
        this.view.setStroke(Color.PURPLE);
        this.view.setStrokeWidth(4);
    }

    /**
     * Method to reset the focus.
     */
    public void resetFocus() {
        this.selected = false;
        this.view.setStroke(Color.RED);
        this.view.setStrokeWidth(1);
    }
}