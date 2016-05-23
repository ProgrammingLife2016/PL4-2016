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
     * @param id The ID of a cell.
     */
    public BubbleCell(int id) {
        super(id);
        StackPane pane = new StackPane();
        pane.setMaxHeight(10);
        Circle view = new Circle(10);
        view.setStroke(Color.YELLOW);
        view.setFill(Color.YELLOW);

        pane.getChildren().addAll(view);
        setView(pane);
    }

    /**
     * Return the type of the Cell.
     * @return the type of the Cell.
     */
    public CellType getType() {
        return type;
    }

    /**
     * Return the Cell's text.
     * @return the Cell's text.
     */
    public Text getText() {
        return text;
    }
}