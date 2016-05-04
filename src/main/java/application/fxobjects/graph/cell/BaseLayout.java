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

    /**
     * Class constructor.
     *
     * @param graph  A given graph.
     * @param offset Offset to be added on execute() call.
     */
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

        boolean done = false;
        for (Cell cell : cells) {
            switch (cell.getType()) {
                case RECTANGLE:
                    currentX += offset;

                    currentY = 200;
                    cell.relocate(currentX, currentY);

                    cellCount = 1;
                    break;
                case TRIANGLE:
                    if (lastType == CellType.RECTANGLE)
                        currentX += (offset / 2);

                    currentY = -currentY + ((currentY / (currentY + 1) * 100));
                    currentX += offset;
                    cellCount++;

                    cell.relocate(currentX, currentY);
                    currentX -= (offset / 2);
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

    private void toCellWithDepth(Cell c, int depth, int downmoves) {
        //count leafs
        if (c.getCellChildren().isEmpty()) {
            count++;
        }
        if (depth > maxDepth)
            maxDepth = depth;
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

    }
}