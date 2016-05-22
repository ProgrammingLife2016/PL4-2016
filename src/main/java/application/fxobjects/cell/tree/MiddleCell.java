package application.fxobjects.cell.tree;

import application.fxobjects.cell.Cell;
import core.graph.cell.CellType;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;

/**
 * Class representing a Phylogenetic middle node.
 */
@SuppressFBWarnings("UWF_UNWRITTEN_FIELD")
public class MiddleCell extends Cell {
    private final CellType type = CellType.TREEMIDDLE;
    private Text text;

    /**
     * Middle cell constructor.
     * @param id The ID of the cell.
     */
    public MiddleCell(int id) {
        super(id);
        StackPane pane = new StackPane();
        pane.setMaxHeight(10);
        Rectangle view = new Rectangle(10, 10);
        view.setStroke(Color.TRANSPARENT);
        view.setFill(Color.TRANSPARENT);

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