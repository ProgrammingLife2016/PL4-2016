package application.fxobjects.treeCells;

import application.fxobjects.Cell;
import core.typeEnums.CellType;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

/**
 * Class representing a Phylogenetic middle node.
 */
@SuppressFBWarnings("UWF_UNWRITTEN_FIELD")
public class MiddleCell extends Cell {

    /**
     * Middle cell constructor.
     *
     * @param id The ID of the cell.
     */
    public MiddleCell(int id) {
        this(id, new StackPane());
    }

    /**
     * Middle cell constructor.
     *
     * @param id The ID of the cell.
     * @param pane A given StackPane.
     */
    public MiddleCell(int id, StackPane pane) {
        super(id);
        pane.setMaxHeight(10);
        Rectangle view = new Rectangle(10, 10);
        view.setStroke(Color.TRANSPARENT);
        view.setFill(Color.TRANSPARENT);
        pane.getChildren().addAll(view);
        setView(pane);

        type = CellType.TREEMIDDLE;
    }
}