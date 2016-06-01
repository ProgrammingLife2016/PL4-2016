package application.fxobjects.cell;

import core.graph.cell.CellType;
import javafx.scene.Node;
import javafx.scene.layout.Pane;

import java.util.ArrayList;
import java.util.List;

/**
 * Abstract cell class representing a node in the gui.
 */
public abstract class Cell extends Pane {
    private int cellId;
    private boolean relocated = false;
    List<Cell> children = new ArrayList<>();
    List<Cell> parents = new ArrayList<>();

    Node view;

    /**
     * Class constructor.
     *
     * @param cellId The ID of the cell.
     */
    public Cell(int cellId) {
        this.cellId = cellId;
    }

    /**
     * Add a cell as a child cell.
     *
     * @param cell A cell to be added as a child cell.
     */
    public void addCellChild(Cell cell) {
        children.add(cell);
    }

    /**
     * Return all child cells.
     *
     * @return A list of child cells.
     */
    public List<Cell> getCellChildren() {
        return children;
    }

    /**
     * Add a cell as a parent cell.
     *
     * @param cell A cell to be added as a parent cell.
     */
    public void addCellParent(Cell cell) {
        parents.add(cell);
    }

    /**
     * Return all parent cells.
     *
     * @return A list of parent cells.
     */
    public List<Cell> getCellParents() {
        return parents;
    }

    /**
     * Remove a given child cell.
     *
     * @param cell A given child cell to be removed.
     */
    public void removeCellChild(Cell cell) {
        children.remove(cell);
    }

    /**
     * Set the view of the cell and its children.
     *
     * @param view A given view node.
     */
    public void setView(Node view) {
        this.view = view;
        getChildren().add(view);

    }

    /**
     * Return the view of the cell.
     *
     * @return The view of the cell.
     */
    public Node getView() {
        return this.view;
    }

    /**
     * Return the id of the cell.
     *
     * @return The cell ID.
     */
    public int getCellId() {
        return cellId;
    }

    /**
     * Return the type of the Cell.
     *
     * @return The type of the Cell.
     */
    public abstract CellType getType();

    /**
     * Return whether a cell has been relocated.
     *
     * @return Whether a cell has been relocated.
     */
    public boolean isRelocated() {
        return relocated;
    }

    /**
     * Set the relocation status of a cell.
     *
     * @param relocated The relocation status of a cell.
     */
    public void setRelocated(boolean relocated) {
        this.relocated = relocated;
    }

    /**
     * toString method.
     *
     * @return this to string.
     */
    public String toString() {
        return cellId + "";
    }

    /**
     * Abstract method.
     */
    public void focus() { }

    /**
     * Abstract method.
     */
    public void resetFocus() { }

}
