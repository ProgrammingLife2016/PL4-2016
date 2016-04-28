package application.fxobjects.graph.cell;

import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;

public class RectangleCell extends Cell {
    private final CellType type = CellType.RECTANGLE;
    private Text text;

    public RectangleCell(int id, String seq) {
        super(id);
        StackPane pane = new StackPane();
        pane.setMaxHeight(50);
        text = new Text(seq);
        text.setVisible(false);
        text.setManaged(false);
        Rectangle view = new Rectangle(50,50);
        view.setStroke(Color.DODGERBLUE);
        view.setFill(Color.DODGERBLUE);

        pane.getChildren().addAll(view, text);

        setView(pane);
    }

    public CellType getType() {
        return type;
    }

    public Text getText() {
        return text;
    }
}