package application.fxobjects.graph.cell;

import core.Model;
import core.graph.cell.CellType;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

import java.util.List;

/**
 * Class to create a proper layout fitting the corresponding data.
 * @since 27-04-2016
 * @version 1.0
 */
@SuppressWarnings({"PMD.UnusedPrivateField", "PMD.UnusedPrivateMethod"})
public class BaseLayout extends CellLayout {
    private int offset;
    private Model model;
    private int currentX;
    private int currentY;
    private CellType lastType;
    private int cellCount = 0;
    private int centerY;
    private double maxDistance;

    private static final int BASE_X = 200;
    private static final int BASE_Y = 200;

    /**
     * Class constructor.
     *
     * @param model  A given model.
     * @param offset Offset to be added on execute() call.
     * @param middle The center Y coordinate of the model.
     */
    @SuppressFBWarnings("URF_UNREAD_FIELD")
    public BaseLayout(Model model, int offset, int middle) {
        this.currentX = BASE_X;
        this.currentY = BASE_Y;
        this.lastType = null;
        this.offset = offset;
        this.model = model;
        this.centerY = middle;
        System.out.println(centerY + "   center Y");
        this.maxDistance = middle;
    }

    /**
     * Method to align all nodes properly.
     */
    public void execute() {
        for (Cell cell : model.getAllCells()) {
            switch (cell.getType()) {
                case RECTANGLE:
                    currentX += offset;

                    currentY = centerY;
                    cell.relocate(currentX, currentY);

                    cellCount = 1;
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
                    cell.setRelocated(true);

                    // Don't draw triangles above rectangles
                    if (currentY != BASE_Y) {
                        currentX += offset;
                    }
                    break;
                default:
                    System.out.println("default");
                    break;
            }
            lastType = cell.getType();
        }
    }


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
}