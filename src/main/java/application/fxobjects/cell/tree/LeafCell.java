package application.fxobjects.cell.tree;

import application.fxobjects.cell.Cell;
import core.MetaData;
import core.graph.cell.CellType;
import javafx.scene.control.Label;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;

import static application.fxobjects.cell.LineageColor.*;

/**
 * Class representing a Phylogenetic leave node.
 */
public class LeafCell extends Cell {
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

        if (name.contains("TKK") && MetaData.getMetadata().get(name) != null) {
            this.setBackground(
                    new Background(
                            new BackgroundFill(
                                    determineLeafLinColor(
                                            MetaData.getMetadata().get(name).getLineage()
                                    ), null, null
                            )
                    )
            );
        } else if (name.contains("G")) {
            this.setBackground(
                    new Background(new BackgroundFill(determineLeafLinColor(4), null, null)));
        } else {
            this.setBackground(new Background(new BackgroundFill(Color.LIGHTGRAY, null, null)));
        }
        setView(pane);

        type = CellType.TREELEAF;
    }

    /**
     * Getter method for the name of the strain.
     *
     * @return the name of the strain.
     */
    public String getName() {
        return name;
    }
}