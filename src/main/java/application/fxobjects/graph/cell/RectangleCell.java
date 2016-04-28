package application.fxobjects.graph.cell;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import application.fxobjects.graph.cell.Cell;

public class RectangleCell extends Cell {
    private final CellType type = CellType.RECTANGLE;

    public RectangleCell( String id) {
        super( id);

        Rectangle view = new Rectangle( 50,50);

        view.setStroke(Color.DODGERBLUE);
        view.setFill(Color.DODGERBLUE);

        setView( view);

    }

    public CellType getType() {
        return type;
    }
}