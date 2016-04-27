package application.fxobjects.graph.cell;

import application.fxobjects.graph.Graph;

import java.util.List;

/**
 * Created by Arthur on 4/27/16.
 */
public class BaseLayout extends CellLayout {
    private int offset;
    private Graph graph;
    private int currentX;
    private int currentY;
    private CellType lastType;

    public BaseLayout(Graph graph, int offset) {
        this.graph = graph;
        this.currentX = 20;
        this.currentY = 200;
        this.lastType = null;
        this.offset = offset;
    }

    public void execute() {
        List<Cell> cells = graph.getModel().getAllCells();

        for (Cell cell : cells) {
            switch (cell.getType()) {
                case RECTANGLE:
                    if(lastType == CellType.TRIANGLE)
                        currentX += offset;
                    cell.relocate(currentX, 200);
                    currentY = 200;
                case TRIANGLE:
                    if(lastType == CellType.RECTANGLE) {
                        currentX += offset;
                        currentY += offset;
                    }
                    else
                        currentY -= offset * 2;
                    cell.relocate(currentX, currentY);
                default:
                    break;
            }
            lastType = cell.getType();
        }
    }
}