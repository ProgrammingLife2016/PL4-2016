package application.controllers;

import application.fxobjects.cell.graph.BubbleCell;
import application.fxobjects.cell.graph.CollectionCell;
import application.fxobjects.cell.graph.IndelCell;
import application.fxobjects.cell.graph.RectangleCell;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;


/**
 * Created by Daphne van Tetering on 30-5-2016.
 */
public class LegendFactory {

    /**
     * Constructor - Create a new LegendFactory
     */
    public LegendFactory() {
    }

    /**
     * Create a legend info panel that shows the meaning of different types of cells.
     *
     * @return A legend panel.
     */
    public HBox createLegend() {
        final GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(10, 10, 10, 10));

        grid.add(new RectangleCell(0), 0, 0);
        grid.add(new BubbleCell(0, "N"), 0, 1);
        grid.add(new IndelCell(0, "N"), 0, 2);
        grid.add(new CollectionCell(0, "N"), 0, 3);

        grid.add(new Text("-    Basic Node"), 1, 0);
        grid.add(new Text("-    Bubble Node"), 1, 1);
        grid.add(new Text("-    Indel Node"), 1, 2);
        grid.add(new Text("-    Collection Node"), 1, 3);

        grid.add(new Text(""), 2, 0);
        grid.add(new Text("  -    Contains N other nodes"), 2, 1);
        grid.add(new Text("  -    Contains N other nodes"), 2, 2);
        grid.add(new Text("  -    Contains N (horizontally collapsed) nodes"), 2, 3);

        HBox legend = new HBox();
        legend.getChildren().addAll(grid);
        return legend;
    }

}
