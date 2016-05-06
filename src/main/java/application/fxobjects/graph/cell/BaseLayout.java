package application.fxobjects.graph.cell;

import application.fxobjects.graph.Graph;

import java.util.List;

/**
 * Class to create a proper layout fitting the corresponding data.
 *
 * @author Arthur Breurkes.
 * @since 27-04-2016
 * @version 1.0
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

    private int maxDepth = 0;
    private int count = 0;

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
     * Method to align all nodes properly.
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

    //ToDo: relocate this method to phylogenetic class.
    /**
     * Layout method for phylogenetic tree.
     * @param c cell to start from.
     * @param depth allowed depth for traversal.
     * @param downmoves amount of down moves to go down.
     */
    /*private void toCellWithDepth(Cell c, int depth, int downmoves) {
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

    /**
     * Getter method for the offset.
     * @return offset.
     */
    public int getOffset() {
        return offset;
    }

    /**
     * Setter method for the offset.
     * @param offset value to set offset to.
     */
    public void setOffset(int offset) {
        this.offset = offset;
    }

    /**
     * Getter method for the Graph.
     * @return graph.
     */
    public Graph getGraph() {
        return graph;
    }

    /**
     * Setter method for the Graph.
     * @param graph Graph to be set.
     */
    public void setGraph(Graph graph) {
        this.graph = graph;
    }

    /**
     * Getter method for the current X value.
     * @return currentX.
     */
    public int getCurrentX() {
        return currentX;
    }

    /**
     * Setter method for the current X value.
     * @param currentX value to set currentX to.
     */
    public void setCurrentX(int currentX) {
        this.currentX = currentX;
    }

    /**
     * Getter method for the current Y value.
     * @return currentY.
     */
    public int getCurrentY() {
        return currentY;
    }

    /**
     * Setter method for the current Y value.
     * @param currentY value to set currentY to.
     */
    public void setCurrentY(int currentY) {
        this.currentY = currentY;
    }

    /**
     * Getter method for the last seen type of cell.
     * @return the last seen CellType.
     */
    public CellType getLastType() {
        return lastType;
    }

    /**
     * Setter method for the last seen type of cell.
     * @param lastType CellType to be set.
     */
    public void setLastType(CellType lastType) {
        this.lastType = lastType;
    }

    /**
     * Getter method for the cell count.
     * @return cellCount.
     */
    public int getCellCount() {
        return cellCount;
    }

    /**
     * Setter method for the cell count.
     * @param cellCount the value to set cellCount to.
     */
    public void setCellCount(int cellCount) {
        this.cellCount = cellCount;
    }

    /**
     * Getter method for the maximum allowed depth.
     * @return maxDepth.
     */
    public int getMaxDepth() {
        return maxDepth;
    }

    /**
     * Setter method for the maximum allowed depth.
     * @param maxDepth value to set maxDepth to.
     */
    public void setMaxDepth(int maxDepth) {
        this.maxDepth = maxDepth;
    }

    /**
     * Getter method for the count value.
     * @return count.
     */
    public int getCount() {
        return count;
    }

    /**
     * Setter method for the count value.
     * @param count value to set count to.
     */
    public void setCount(int count) {
        this.count = count;
    }
}