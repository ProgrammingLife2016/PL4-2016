package application.fxobjects.graph.cell;

import core.graph.Graph;
import core.graph.cell.CellType;
import java.util.List;

/**
 * Class to create a proper layout fitting the corresponding data.
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
    private int centerY;
    private double maxDistance;

    private static final int baseX = 200;
    private static final int baseY = 200;

    /**
     * Class constructor.
     *
     * @param graph  A given graph.
     * @param offset Offset to be added on execute() call.
     */

    public BaseLayout(Graph graph, int offset, int middle) {
        this.currentX = baseX;
        this.currentY = baseY;
        this.lastType = null;
        this.offset = offset;
        this.graph = graph;
        this.centerY = middle;
        System.out.println(centerY + "   center Y");
        this.maxDistance = middle;
    }

    /**
     * Method to align all nodes properly.
     */
    public void execute() {
        List<Cell> cells = graph.getModel().getAllCells();
        boolean done = false;
        for (Cell cell : cells) {
            switch (cell.getType()) {
                case RECTANGLE:
                    currentX += offset;

                    currentY = centerY;
                    cell.relocate(currentX, currentY);

                    cellCount = 1;
                    break;
//                case TRIANGLE:
//                    if (lastType == CellType.RECTANGLE) {
//                        currentX += (offset / 2);
//                        currentY = centerY - 100;
//
//                    } else {
//                        currentX += offset;
//                        currentY = centerY - (500/cellCount);
//                    }
//
//
//                    cellCount++;
//                    //currentX -= (offset / 2);
//                    if (lastType != CellType.TRIANGLE) {
//                        currentX += offset;
//                    }
//                    cell.relocate(currentX, baseY);
//                    currentY = baseY;
//                    cellCount = 0;
//                    break;
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
//                case PHYLOGENETIC:
//                    if(!done) {
//                        System.out.println("kachel");
//                        cell.relocate(100, 100);
//                        toCellWithDepth(cells.get(0), 0, 0);
//                        done = true;
//                        break;
//                    }
                default:
                    System.out.println("default");
                    break;
            }
            lastType = cell.getType();
        }
    }

    private int maxDepth = 0;
    private int count = 0;

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