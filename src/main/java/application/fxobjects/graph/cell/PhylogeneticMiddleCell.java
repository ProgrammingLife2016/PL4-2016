package application.fxobjects.graph.cell;

import core.graph.cell.CellType;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;

/**
 * Class representing a Phylogenetic middle node shape.
 */
public class PhylogeneticMiddleCell extends Cell {
    private final CellType type = CellType.PHYLOGENETIC_MIDDLE;
    private Text text;

    /**
     * Phylogenetic middle cell constructor.
     * @param id The ID of a cell.
     * @param name The genome sequence of a cell.
     */
    public PhylogeneticMiddleCell(int id, String name) {
        super(id);
        StackPane pane = new StackPane();
        pane.setMaxHeight(20);
        text = new Text(name);
        text.setVisible(false);
        text.setManaged(false);
        Rectangle view = new Rectangle(20, 20);
        view.setStroke(Color.BLACK);
        view.setFill(Color.BLACK);

        pane.getChildren().addAll(view, text);

        setView(pane);
    }

    public void setText(Text text) {
        this.text = text;
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