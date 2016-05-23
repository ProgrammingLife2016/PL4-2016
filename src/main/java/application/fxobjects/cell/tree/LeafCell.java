package application.fxobjects.cell.tree;

import application.fxobjects.cell.Cell;
import core.graph.cell.CellType;
import javafx.scene.control.Label;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

/**
 * Class representing a Phylogenetic leave node.
 */
public class LeafCell extends Cell {
    private final CellType type = CellType.TREELEAF;
    private Text text;

    /**
     * Phylogenetic leave cell constructor.
     * @param id The ID of the cell.
     * @param name The name of the genome.
     */
    public LeafCell(int id, String name) {
        super(id);
        StackPane pane = new StackPane(new Label(name));
        pane.setBackground(new Background(new BackgroundFill(Color.LIGHTGREY, null, null)));

        setView(pane);
    }

    /**
     * Set the name of a sample.
     * @param text The name of a sample.
     */
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