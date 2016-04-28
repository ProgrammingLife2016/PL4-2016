package application.fxobjects.graph.cell;

import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import javafx.scene.text.Text;

/**
 * Class representing a Triangle shape.
 */
@SuppressWarnings("CheckStyle.JavadocMethod")
public class TriangleCell extends Cell {
    private final CellType type = CellType.TRIANGLE;
    private Text text;

    /**
     * Triangle cell constructor.
     * @param id The ID of a cell.
     * @param seq The genome sequence of a cell.
     */
    public TriangleCell(int id, String seq) {
        super(id);
        double width = 50;
        double height = 50;

        StackPane pane = new StackPane();
        pane.setMaxHeight(50);
        text = new Text(seq);
        text.setVisible(false);
        text.setManaged(false);

        Polygon view = new Polygon(width / 2, 0, width, height, 0, height);

        view.setStroke(Color.RED);
        view.setFill(Color.RED);

        pane.getChildren().addAll(view, text);

        setView(pane);
    }

    /**
     * Return the type of the Cell.
     * @return The type of the Cell.
     */
    public CellType getType() {
        return type;
    }

    /**
     * Return the Cell's text.
     * @return The Cell's text.
     */
    public Text getText() {
        return text;
    }
}