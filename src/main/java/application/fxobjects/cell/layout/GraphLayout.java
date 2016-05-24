package application.fxobjects.cell.layout;

import application.fxobjects.cell.Cell;
import core.Model;
import core.graph.cell.CellType;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

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
    private double maxDistance;
    private double maxWidth;

    private static final int BASE_X = 175;

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
        this.maxDistance = middle;
        this.maxWidth = 0;

    }

    /**
     * Method to align all nodes properly.
     */
    public void execute() {
        List<Cell> cells = model.getAllCells();

        for (Cell cell : cells) {
            if (!cell.isRelocated()) {
                currentX += offset;
                if (currentX > maxWidth) {
                    maxWidth = currentX;
                }
                currentY = centerY;
                cell.relocate(currentX, currentY);
                cell.setRelocated(true);

                currentX += offset;

                //only continue when there is more than 1 child
                cellCount = cell.getCellChildren().size();
                if (cellCount < 2) {
                    continue;
                }

                int yOffset = 3 * offset; //y-offset between nodes on the same x-level
                int oddChildOffset = 0; //initial offset when there are an odd number of children
                int evenChildOffset = (yOffset) / 2; //offset for an even amount of children
                int modifier = -1; //alternate between above and below for the same x-level

                for (Cell child : cell.getCellChildren()) {
                    if (cellCount % 2 == 0) {
                        child.relocate(currentX, currentY - evenChildOffset);
                        evenChildOffset = (yOffset / 2) * modifier;
                        child.setRelocated(true);

                        modifier *= -1;
                        if (modifier > 0 ){
                            modifier++;
                        }
                    } else {
                        child.relocate(currentX, currentY - oddChildOffset);
                        oddChildOffset = ((yOffset) * modifier);
                        child.setRelocated(true);

                        modifier *= -1;
                        if (modifier < 0) {
                            modifier--;
                        }
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

    public double getMaxWidth() { return maxWidth; }
}