package application.fxobjects.graph.cell;

import javafx.scene.control.Tooltip;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class RectangleCell extends Cell {
    private final CellType type = CellType.RECTANGLE;

    public RectangleCell(int id, String seq) {
        super(id);
        Rectangle view = new Rectangle(50,50);

        view.setStroke(Color.DODGERBLUE);
        view.setFill(Color.DODGERBLUE);

        Tooltip tip = new Tooltip();

        tip.setText(seq);
        Tooltip.install(view, tip);

        setView(view);
    }

    public CellType getType() {
        return type;
    }
}