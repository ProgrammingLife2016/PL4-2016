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
    private static final int BASE_X = 200;
    private static final int BASE_Y = 200;

    /**
     * Class constructor.
     * @param graph A given graph.
     * @param offset Offset to be added on execute() call.
     */
    public BaseLayout(Graph graph, int offset) {
        this.currentX = BASE_X;
        this.currentY = BASE_Y;
        this.lastType = null;
        this.offset = offset;
        this.graph = graph;
    }

    /**
     * This method aligns every node.
     */
    public void execute() {
        List<Cell> cells = graph.getModel().getAllCells();

        //boolean done = false;
        for (Cell cell : cells) {
            switch (cell.getType()) {
                case RECTANGLE:
                    if (lastType != CellType.TRIANGLE) {
                        currentX += offset;
                    }
                    cell.relocate(currentX, BASE_X);
                    currentY = BASE_Y;
                    cellCount = 0;
                    break;
                case TRIANGLE:
                    if (cellCount % 2 == 0) {
                        currentY += cellCount * offset;
                    } else {
                        currentY -= cellCount * offset;
                    }

                    if (currentY == BASE_Y) {
                        currentX += offset;
                    }

                    cellCount++;
                    cell.relocate(currentX, currentY);

                    // Don't draw triangles above rectangles
                    if (currentY != BASE_Y) {
                        currentX += offset;
                    }
                    break;
//                case PHYLOGENETIC:
//                    if(!done) {
//                        System.out.println("kachel");
//                        cell.relocate(100, 100);
//                        toCellWithDepth(cells.get(0), 0, 0);
//                        done = true;
//                        break;
//                    }
                default:
                    break;
            }
            lastType = cell.getType();
        }
    }
    private int maxDepth = 0;
    private int count = 0;

/*    private void toCellWithDepth(Cell c, int depth, int downmoves) {
        //count leafs
        if (c.getCellChildren().isEmpty()) {
            count++;
        }
        if (depth > maxDepth) {
            maxDepth = depth;
        }
        int childNumber = -1;
        for (Cell child : c.getCellChildren()) {
            childNumber++;
            toCellWithDepth(child, depth + 1, downmoves + childNumber);
        }
        //System.out.println(downmoves + " " + depth);

        if (c.getCellChildren().isEmpty()) {
            c.relocate(maxDepth * 50, count * 50);
        } else {
            c.relocate(50 + depth * 50, count * 50);
        }
    }*/
}