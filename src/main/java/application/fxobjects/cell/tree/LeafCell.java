package application.fxobjects.cell.tree;

import application.fxobjects.cell.Cell;
import core.graph.cell.CellType;
import javafx.scene.control.Label;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;

/**
 * Class representing a Phylogenetic leave node.
 */
public class LeafCell extends Cell {
    private final CellType type = CellType.TREELEAF;
    private String name;


    /**
     * Phylogenetic leave cell constructor.
     *
     * @param id   The ID of the cell.
     * @param name The name of the genome.
     */
    public LeafCell(int id, String name) {
        this(id, name, new StackPane(new Label(name)));
    }

    /**
     * Phylogenetic leave cell constructor.
     *
     * @param id   The ID of the cell.
     * @param name The name of the genome.
     * @param pane A given stack pane.
     */
    public LeafCell(int id, String name, StackPane pane) {
        super(id);
        this.name = name;

        pane.setBackground(new Background(new BackgroundFill(Color.LIGHTGREY, null, null)));
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
     * Getter method for the name of the strain.
     *
     * @return the name of the strain.
     */
    public String getName() {
        return name;
    }

    /**
     * Setter method for the name of the strain.
     *
     * @param name the name of the strain.
     */
    public void setName(String name) {
        this.name = name;
    }
}