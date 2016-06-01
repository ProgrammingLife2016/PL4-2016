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
    public IndelCell(int id, String collapseLevel) {
        this(id, new StackPane(), new Text(collapseLevel));
    }

    /**
     * Indel cell constructor.
     *
     * @param id            The ID of a cell.
     * @param pane          A given stack pane.
     * @param text          A given text element.
     */
    public IndelCell(int id, StackPane pane, Text text) {
        super(id);

        double width = 20;
        double height = 20;

        this.selected = false;
        this.view = new Polygon(width / 2, 0, width, height, 0, height);
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