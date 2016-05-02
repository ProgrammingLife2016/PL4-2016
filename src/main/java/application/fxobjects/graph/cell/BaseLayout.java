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
    private static final int baseX = 200;
    private static final int baseY = 200;
    /**
     * Class constructor.
     * @param graph A given graph.
     * @param offset Offset to be added on execute() call.
     */
    public BaseLayout(Graph graph, int offset) {
        this.currentX = baseX;
        this.currentY = baseY;
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
                    if (lastType != CellType.TRIANGLE) {
                        currentX += offset;
                    }
                    cell.relocate(currentX, baseY);
                    currentY = baseY;
                    cellCount = 0;
                    break;
                case TRIANGLE:
                    if (cellCount % 2 == 0) {
                        currentY += cellCount * offset;
                    } else {
                        currentY -= cellCount * offset;
                    }

                    if (currentY == baseY) {
                        currentX += offset;
                    }

                    cellCount++;
                    cell.relocate(currentX, currentY);

                    // Don't draw triangles above rectangles
                    if (currentY != baseY) {
                        currentX += offset;
                    }
                    break;
                default:
                    break;
            }
            lastType = cell.getType();
        }
    }
}