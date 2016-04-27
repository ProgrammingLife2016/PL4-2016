package application.fxobjects.graph.cell;

import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;

import application.fxobjects.graph.cell.Cell;

public class TriangleCell extends Cell {
    private final CellType type = CellType.TRIANGLE;

    public TriangleCell(String id) {
        super(id);

        double width = 50;
        double height = 50;

        Polygon view = new Polygon(width / 2, 0, width, height, 0, height);

        view.setStroke(Color.RED);
        view.setFill(Color.RED);

        setView(view);
    }

    public CellType getType() {
        return type;
    }
}