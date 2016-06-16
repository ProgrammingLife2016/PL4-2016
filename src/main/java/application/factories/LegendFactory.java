package application.factories;

import application.fxobjects.Edge;
import application.fxobjects.graphCells.*;
import application.fxobjects.treeCells.MiddleCell;
import core.typeEnums.EdgeType;
import javafx.geometry.Insets;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;


/**
 * LegendFactory Class
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
        grid.setVgap(10);
        grid.setPadding(new Insets(10, 10, 10, 10));

        Text title = new Text("Legend");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 18));
        grid.add(title, 0, 0);

        grid.add(new RectangleCell(0, 1), 0, 1);
        grid.add(new BubbleCell(0, 1, "N"), 0, 2);
        grid.add(new IndelCell(0, 1, "N"), 0, 3);
        grid.add(new CollectionCell(0, 1, "N"), 0, 4);
        grid.add(new ComplexCell(0, 1, "N"), 0, 5);


        MiddleCell mc1 = new MiddleCell(0);
        MiddleCell mc2 = new MiddleCell(0);
        Edge e = new Edge(mc1, mc2, 8, EdgeType.GRAPH);
        e.getLine().getStrokeDashArray().addAll(3d, 17d);
        e.getLine().setOpacity(0.2d);
        grid.add(mc1, 0, 3);
        grid.add(e, 0, 4);
        grid.add(mc2, 1, 3);

        fillGrid(grid);

        HBox legend = new HBox();
        legend.getChildren().addAll(grid);
        return legend;
    }

    /**
     * Add info to the legend
     * @param grid the current grid-structure of the legend
     */
    private void fillGrid(GridPane grid) {
        grid.add(new Text("-    Basic Node"), 2, 1);
        grid.add(new Text("-    Bubble Node"), 2, 2);
        grid.add(new Text("-    Indel Node"), 2, 3);
        grid.add(new Text("-    Collection Node"), 2, 4);
        grid.add(new Text("-    Complex Node"), 2, 5);
        grid.add(new Text("-     Dashed edge"), 2, 6);
        grid.add(new Text(""), 3, 1);
        grid.add(new Text("  -    Contains N other nodes"), 3, 2);
        grid.add(new Text("  -    Contains N other nodes"), 3, 3);
        grid.add(new Text("  -    Contains N horizontally collapsed nodes"), 3, 4);
        grid.add(new Text("  -    Contains N nodes that are structured in a more complex manner"), 3, 5);
        grid.add(new Text("  -    A longer Edge typically crossing over other edges. "
                + "Functionally identical to a normal Edge."), 3, 6);
    }

}
