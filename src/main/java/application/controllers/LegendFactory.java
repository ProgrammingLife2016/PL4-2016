package application.controllers;

import application.fxobjects.cell.graph.BubbleCell;
import application.fxobjects.cell.graph.CollectionCell;
import application.fxobjects.cell.graph.IndelCell;
import application.fxobjects.cell.graph.RectangleCell;
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
        final VBox col1 = new VBox();
        col1.getChildren().add(new RectangleCell(0));
        col1.getChildren().add(new BubbleCell(0, "N"));
        col1.getChildren().add(new IndelCell(0, "N"));
        col1.getChildren().add(new CollectionCell(0, "N"));

        final VBox col2 = new VBox();
        col2.getChildren().add(new Text("  -  Basic Node"));
        col2.getChildren().add(new Text("  -  Bubble Node"));
        col2.getChildren().add(new Text("  -  Indel Node"));
        col2.getChildren().add(new Text("  -  Collection Node"));

        final VBox col3 = new VBox();
        col3.getChildren().add(new Text(""));
        col3.getChildren().add(new Text("  -  Contains N other nodes"));
        col3.getChildren().add(new Text("  -  Contains N other nodes"));
        col3.getChildren().add(new Text("  -  Contains N (horizontally collapsed) nodes"));

        HBox legend = new HBox();
        legend.getChildren().addAll(col1, col2, col3);
        return legend;
    }

}
