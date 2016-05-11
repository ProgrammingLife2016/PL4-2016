package application.fxobjects.graph.cell;

import core.graph.cell.CellType;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;

/**
 * Class representing a Phylogenetic leave node shape.
 */
public class PhylogeneticCell extends Cell {
    private final CellType type = CellType.PHYLOGENETIC;
    private Text text;

    /**
     * Rectangle cell constructor.
     * @param id The ID of a cell.
     * @param name The genome sequence of a cell.
     */
    public PhylogeneticCell(int id, String name) {
        super(id);
        StackPane pane = new StackPane();
        pane.setMaxHeight(50);
        text = new Text(name);
        text.setVisible(false);
        text.setManaged(false);
        Rectangle view = new Rectangle(50, 50);
        view.setStroke(Color.LIGHTGREY);
        view.setFill(Color.LIGHTGREY);

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