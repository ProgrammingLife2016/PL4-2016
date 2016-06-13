package application.fxobjects.cell.layout;

import application.fxobjects.cell.Cell;
import application.fxobjects.cell.graph.GraphCell;
import core.Model;
import core.Node;
import core.graph.cell.CellType;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

/**
 * Class to create a proper layout fitting the corresponding data.
 *
 * @version 1.0
 * @since 27-04-2016
 */
@SuppressWarnings({"PMD.UnusedPrivateField", "PMD.UnusedPrivateMethod"})
public class GraphLayout extends CellLayout {
    private int offset;
    private Model model;
    private int currentX;
    private int currentY;
    private CellType lastType;
    private int cellCount = 0;
    private int centerY;

    private double maxWidth;

    private Cell leftMost;
    private Cell rightMost;

    private static final int BASE_X = 100;

    /**
     * Class constructor.
     *
     * @param model  A given model.
     * @param offset Offset to be added on execute() call.
     * @param middle The center Y coordinate of the model.
     */
    @SuppressFBWarnings("URF_UNREAD_FIELD")
    public GraphLayout(Model model, int offset, int middle) {
        this.currentX = BASE_X;
        this.currentY = middle;
        this.lastType = null;
        this.offset = offset;
        this.model = model;
        this.centerY = middle;
        this.maxWidth = 0;
    }

    /**
     * Method to align all nodes properly.
     */
    @SuppressWarnings("checkstyle:methodlength")
    public void execute() {
        int minWidth = Integer.MAX_VALUE;

        List<Cell> cells = model.getAddedCells();
        for (Cell c : cells) {
            GraphCell cell = (GraphCell) c;
            if (!cell.isRelocated()) {
                currentX += offset;
                if (currentX > maxWidth) {
                    maxWidth = currentX;
                    rightMost = cell;
                }
                if (currentX < minWidth) {
                    minWidth = currentX;
                    leftMost = cell;
                }
                currentY = centerY;

                cell.relocate(currentX - (cell.getCellShape().getLayoutBounds().getWidth() / 2),
                        currentY - (cell.getCellShape().getLayoutBounds().getHeight() / 2));
                cell.setRelocated(true);

                currentX += offset + cell.getCellShape().getLayoutBounds().getWidth() / 2;

                List<Cell> childrenToDraw = new ArrayList<>(cell.getCellChildren());
                for (Cell child : cell.getCellChildren()) {
                    for (Cell childParent : child.getCellParents()) {
                        if (!childParent.isRelocated()) {
                            childrenToDraw.remove(child);
                        }
                    }
                }
                //only continue when there is more than 1 child to draw
                cellCount = cell.getCellChildren().size();
                if (cellCount > 1) {
                    breadthFirstPlacing(cell, childrenToDraw);
                }

            }
        }
    }



    /**
     * A method to place a Node's children and if need be, recursively their children.
     *
     * @param cell - The parent node.
     */
    @SuppressWarnings("checkstyle:methodlength")
    public void breadthFirstPlacing(Cell cell, List<Cell> childrenToDraw) {
        int yOffset = 2 * offset; //y-offset between nodes on the same x-level
        int oddChildOffset = 0; //initial offset when there are an odd number of children
        int evenChildOffset = yOffset / 2; //offset for an even amount of children
        int modifier = -1; //alternate between above and below for the same x-level
        for (Cell c : cell.getCellChildren()) {
            GraphCell child = (GraphCell) c;
            if (!child.isRelocated() || child.getLayoutX() < cell.getLayoutX()) {
                if (childrenToDraw.size() % 2 == 0) {
                    child.relocate(currentX
                                    - (child.getCellShape().getLayoutBounds().getWidth() / 2),
                            currentY - evenChildOffset
                                    - (child.getCellShape().getLayoutBounds().getHeight() / 2));
                    evenChildOffset = (yOffset / 2) * modifier;
                    if (childrenToDraw.contains(child)) {
                        child.setRelocated(true);
                    }
                    modifier *= -1;
                    if (modifier > 0) {
                        modifier++;
                    }
                } else {
                    child.relocate(currentX
                                    - (child.getCellShape().getLayoutBounds().getWidth() / 2),
                            currentY + oddChildOffset
                                    - (child.getCellShape().getLayoutBounds().getHeight() / 2));
                    oddChildOffset = yOffset * modifier;
                    if (childrenToDraw.contains(child)) {
                        child.setRelocated(true);
                    }

                    modifier *= -1;
                    if (modifier < 0) {
                        modifier--;
                    }
                }
            }
        }
    }

    /**
     * Getter method for the offset.
     *
     * @return offset.
     */
    public int getOffset() {
        return offset;
    }

    /**
     * Setter method for the offset.
     *
     * @param offset value to set offset to.
     */
    public void setOffset(int offset) {
        this.offset = offset;
    }

    /**
     * Getter method for the current X value.
     *
     * @return currentX.
     */
    public int getCurrentX() {
        return currentX;
    }

    /**
     * Setter method for the current X value.
     *
     * @param currentX value to set currentX to.
     */
    public void setCurrentX(int currentX) {
        this.currentX = currentX;
    }

    /**
     * Getter method for the current Y value.
     *
     * @return currentY.
     */
    public int getCurrentY() {
        return currentY;
    }

    /**
     * Setter method for the current Y value.
     *
     * @param currentY value to set currentY to.
     */
    public void setCurrentY(int currentY) {
        this.currentY = currentY;
    }

    /**
     * Getter method for the last seen type of cell.
     *
     * @return the last seen CellType.
     */
    public CellType getLastType() {
        return lastType;
    }

    /**
     * Setter method for the last seen type of cell.
     *
     * @param lastType CellType to be set.
     */
    public void setLastType(CellType lastType) {
        this.lastType = lastType;
    }

    /**
     * Getter method for the cell count.
     *
     * @return cellCount.
     */
    public int getCellCount() {
        return cellCount;
    }

    /**
     * Setter method for the cell count.
     *
     * @param cellCount the value to set cellCount to.
     */
    public void setCellCount(int cellCount) {
        this.cellCount = cellCount;
    }

    /**
     * Get the max width
     *
     * @return the max width
     */
    public double getMaxWidth() {
        return maxWidth;
    }

    /**
     * getter for the leftmost cell.
     *
     * @return the leftmost cell.
     */
    public Cell getLeftMost() {
        return leftMost;
    }

    /**
     * getter for the rightmost cell.
     *
     * @return the rightmost cell.
     */
    public Cell getRightMost() {
        return rightMost;
    }
}