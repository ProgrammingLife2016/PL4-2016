package application.fxobjects.graph.cell;

import core.graph.Graph;
import core.graph.cell.CellType;

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
    private double centerY;
    private double maxDistance;

    /**
     * Class constructor.
     * @param graph A given graph.
     * @param offset Offset to be added on execute() call.
     */
    public BaseLayout(Graph graph, int offset, double middle) {
        this.currentX = 20;
        this.currentY = 200;
        this.lastType = null;
        this.offset = offset;
        this.graph = graph;
        this.centerY = middle;
        this.maxDistance = middle;
    }

    /**
     * This method aligns every node.
     */
    public void execute() {
        List<Cell> cells = graph.getModel().getAllCells();

        for (Cell cell : cells) {
            switch (cell.getType()) {
                case RECTANGLE:
                    if (lastType != CellType.TRIANGLE) {
                        currentX += offset;
                    }
                    cell.relocate(currentX, centerY);
                    currentY = (int) centerY;
                    cellCount = 0;
                    break;
                case TRIANGLE:
                    if(cellCount%2==0) {
                        double increment = (cellCount+2)*(cellCount)* offset;
                        if (increment > maxDistance) {
                            double off = maxDistance - increment;
                            increment = increment - off - 200;
                            currentY += increment;
                        } else {
                            currentY += increment;
                        }

                        }
                    else {
                        double decrement = (cellCount) * (cellCount + 1) * offset;
                        if (decrement > maxDistance) {
                            double off = maxDistance - decrement;
                            decrement = decrement - off - 10;
                            currentY -= decrement;
                        } else {
                            currentY -= decrement;
                            //currentY -= offset * 2;
                        }
                    }

                    currentX += offset;
                    cellCount++;

                    cell.relocate(currentX, currentY);
                        //currentY += offset * 2;
                    break;
                default:
                    break;
            }
            lastType = cell.getType();
        }
    }
}