package application.fxobjects.cell.graph;

import application.fxobjects.cell.Cell;
import core.graph.cell.CellType;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;

/**
 * Created by Niek on 5/25/2016.
 */
public class CollectionCell extends Cell {

    private final CellType type = CellType.COLLECTION;
    private Circle view;
    private boolean selected;

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

        this.selected = false;
        this.view = new Circle(10);
        this.view.setStroke(Color.LIGHTGREEN);
        this.view.setStrokeWidth(1);
        this.view.setFill(Color.LIGHTGREEN);

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

    public void focus() {
        this.selected = true;
        this.view.setStroke(Color.PURPLE);
        this.view.setStrokeWidth(4);
    }

    public void resetFocus() {
        this.selected = false;
        this.view.setStroke(Color.LIGHTGREEN);
        this.view.setStrokeWidth(1);
    }

}