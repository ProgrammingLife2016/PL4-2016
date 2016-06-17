package application.fxobjects.layout;

import application.fxobjects.Cell;
import application.fxobjects.Edge;
import application.fxobjects.graphCells.GraphCell;
import core.model.Model;
import core.typeEnums.CellType;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import javafx.stage.Screen;

import java.util.ArrayList;
import java.util.List;

/**
 * Class to create a proper layout fitting the corresponding data.
 *
 * @version 1.0
 * @since 27-04-2016
 */
public class GraphLayout extends CellLayout {
    private int offset;
    private Model model;
    private int currentX;
    private int currentY;
    private CellType lastType;
    private int cellCount = 0;

    private double maxWidth;
    private double minWidth;
    private double maxHeight;

    private Cell leftMost;
    private Cell rightMost;

    private double tileWidth;

    /**
     * Class constructor.
     *
     * @param model  A given model.
     * @param offset Offset to be added on execute() call.
     * @param middle The center Y coordinate of the model.
     */
    @SuppressFBWarnings("URF_UNREAD_FIELD")
    public GraphLayout(Model model, int offset, int middle) {
        this.currentY = middle;
        this.lastType = null;
        this.offset = offset;
        this.model = model;
        this.maxWidth = 0;
        this.minWidth = Integer.MAX_VALUE;

        this.maxHeight = 0;
    }

    /**
     * Method to align all nodes properly.
     */
    public void execute() {
        tileWidth = Screen.getPrimary().getVisualBounds().getWidth();
        List<Cell> cells = model.getAddedCells();
        System.out.println("placing cells");
        for (Cell c : cells) {
            GraphCell cell = (GraphCell) c;
            if (!cell.isRelocatedX()) {
                placeCells(cell);
                List<Cell> childrenToDraw = new ArrayList<>(cell.getCellChildren());

                for (Cell child : cell.getCellChildren()) {
                    for (Cell childParent : child.getCellParents()) {
                        if (!childParent.isRelocatedX()) {
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
            int tile = (int) ((c.getLayoutX() - (c.getLayoutX() % tileWidth)) / tileWidth);
            model.addCellInTile(tile, c);

            if (cell.getLayoutY() > maxHeight) {
                maxHeight = cell.getLayoutY();
            }

            for (Edge e : cell.getEdges()) {
                if (e.getLength() > Screen.getPrimary().getBounds().getWidth()) {
                    model.addLongEdge(e);
                }
            }
        }
        System.out.println("finished placing cells");

    }

    /**
     * Place the cell in the graph
     * @param cell cell to be placed
     */
    private void placeCells(GraphCell cell) {
        currentX += offset;
        if (currentX > maxWidth) {
            maxWidth = currentX;
            rightMost = cell;
        }
        if (currentX < minWidth) {
            minWidth = currentX;
            leftMost = cell;
        }

        if (cell.isRelocatedY()) {
            cell.relocate(currentX - (cell.getCellShape().getLayoutBounds().getWidth() / 2), cell.getLayoutY());
        } else {
            cell.relocate(currentX - (cell.getCellShape().getLayoutBounds().getWidth() / 2),
                    currentY - (cell.getCellShape().getLayoutBounds().getWidth() / 2));
        }
        cell.setRelocatedX(true);
        cell.setRelocatedY(true);

        currentX += offset + cell.getCellShape().getLayoutBounds().getWidth() / 2;
    }



    /**
     * A method to place a Node's children and if need be, recursively their children.
     *
     * @param cell - The parent node.
     * @param childrenToDraw list of Cells that are ready to be drawn topologically.
     */
    private void breadthFirstPlacing(Cell cell, List<Cell> childrenToDraw) {
        int yOffset = 2 * offset; //y-offset between nodes on the same x-level
        int oddOffset = 0; //initial offset when there are an odd number of children
        int evenOffset = yOffset / 2; //offset for an even amount of children
        int modifier = -1; //alternate between above and below for the same x-level
        for (Cell c : cell.getCellChildren()) {
            GraphCell child = (GraphCell) c;
                if (cellCount % 2 == 0) {
                    double yCoord = currentY - evenOffset - (child.getCellShape().getLayoutBounds().getHeight() / 2);
                    if (child.isRelocatedY()) { yCoord = child.getLayoutY(); }
                    if (yCoord > maxHeight) { maxHeight = yCoord; }
                    child.relocate(currentX - (child.getCellShape().getLayoutBounds().getWidth() / 2), yCoord);
                    evenOffset = (yOffset / 2) * modifier;
                    modifier *= -1;
                    if (modifier > 0) { modifier++; }
                } else {
                    double yCoord = currentY + oddOffset - (child.getCellShape().getLayoutBounds().getHeight() / 2);
                    if (child.isRelocatedY()) { yCoord = child.getLayoutY(); }
                    if (yCoord > maxHeight) { maxHeight = yCoord; }
                    child.relocate(currentX - (child.getCellShape().getLayoutBounds().getWidth() / 2), yCoord);
                    oddOffset = yOffset * modifier;
                    modifier *= -1;
                    if (modifier < 0) { modifier--; }
                }
            if (childrenToDraw.contains(child)) {
                child.setRelocatedX(true);
            }
            child.setRelocatedY(true);
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
     * Get the max height
     * @return the max height
     */
    public double getMaxHeight() { return maxHeight; }
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