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
    private int cellCount = 0;

    public BaseLayout(Graph graph, int offset) {
        this.currentX = 20;
        this.currentY = 200;
        this.lastType = null;
        this.offset = offset;
        this.graph = graph;
    }

    /**
     * This method aligns every node.
     */
    public void execute() {
        List<Cell> cells = graph.getModel().getAllCells();


        for (Cell cell : cells) {
            switch (cell.getType()) {
                case RECTANGLE:
                    if(lastType != CellType.TRIANGLE)
                        currentX += offset;
                    cell.relocate(currentX, 200);
                    currentY = 200;
                    cellCount = 0;
                case TRIANGLE:
                        if(cellCount%2==0) {
                            currentY += (cellCount+1)* offset;
                            currentY += offset * 2;
                        }
                    else {
                            currentY -= (cellCount+1) * offset;
                            currentY -= offset * 2;
                        }
                    currentX += offset;
                    cellCount++;

                    cell.relocate(currentX, currentY);
                        //currentY += offset * 2;
                default:
                    break;
            }
            lastType = cell.getType();
        }
    }
}