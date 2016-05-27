package application.fxobjects.cell.graph;

import application.fxobjects.cell.Cell;
import core.graph.cell.CellType;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;

/**
 * Class representing a collapsed bubble.
 */
public class BubbleCell extends Cell {
    private final CellType type = CellType.BUBBLE;
    private Text text;

    /**
     * Bubble cell constructor.
     *
     * @param id            The ID of a cell.
     * @param collapseLevel The collapse level of a cell.
     */
    public BubbleCell(int id, String collapseLevel) {
        super(id);
        text = new Text(collapseLevel);

        Circle view = new Circle(10);
        view.setStroke(Color.YELLOW);
        view.setFill(Color.YELLOW);

        StackPane pane = new StackPane();
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
     * Return the Cell's text.
     *
     * @return the Cell's text.
     */
    public Text getText() {
        return text;
    }
}