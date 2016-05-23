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
    private Text text;

    /**
     * Indel cell constructor.
     * @param id The ID of a cell.
     */
    public IndelCell(int id) {
        super(id);
        double width = 10;
        double height = 10;

        StackPane pane = new StackPane();
        pane.setMaxHeight(10);

        Polygon view = new Polygon(width / 2, 0, width, height, 0, height);

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