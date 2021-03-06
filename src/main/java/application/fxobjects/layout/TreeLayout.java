package application.fxobjects.layout;

import application.fxobjects.Cell;
import application.fxobjects.treeCells.MiddleCell;
import core.model.Model;
import core.typeEnums.EdgeType;
import javafx.geometry.Rectangle2D;
import javafx.stage.Screen;
import net.sourceforge.olduvai.treejuxtaposer.drawer.TreeNode;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

/**
 * This class is responsible for properly drawing the phylogenetic tree.
 */
public class TreeLayout extends CellLayout {

    private int offset;
    private Model model;
    private int currentY;

    private HashMap<Integer, Cell> drawnCells;
    private HashMap<Integer, Cell> undrawnCells;

    private double maxY = 0;

    /**
     * Class constructor.
     *
     * @param model  A given model.
     * @param offset Offset to be added on execute() call.
     */
    public TreeLayout(Model model, int offset) {
        this.model = model;
        this.offset = offset;

        drawnCells = new HashMap<>();
        undrawnCells = new HashMap<>();
    }

    /**
     * Controls the drawing of all nodes in the phylogenetic tree.
     */
    @Override
    public void execute() {
        // Initialize all nodes as undrawn
        for (Cell cell : model.getAddedCells()) {
            undrawnCells.put(cell.getCellId(), cell);
        }

        // draw all leaves to the right of the screen.
        drawLeaves();

        // Draw parent nodes and edges to the parent nodes
        while (undrawnCells.size() != 0) {
            drawParents();
        }
    }

    /**
     * relocate all leaves to the right of the screen
     */
    private void drawLeaves() {
        Rectangle2D bounds = Screen.getPrimary().getVisualBounds();
        final double leafX = bounds.getWidth() * 0.74;

        for (int i = 0; i < model.getAddedCells().size(); i++) {
            Cell cell = undrawnCells.get(i);
            int cellId = cell.getCellId();
            TreeNode nodeByCell = model.getTree().getNodeByKey(cellId);

            if (nodeByCell.isLeaf()) {
                currentY += offset;
                relocateCell(cell, leafX, currentY);
            }

        }
    }

    /**
     * Convert a TreeNode to its corresponding Cell.
     *
     * @param treeNode A TreeNode to get its Cell from.
     * @return The Cell matching the given TreeNode.
     */
    private Cell treeNodeToCell(TreeNode treeNode) {
        int id = treeNode.getKey();
        return model.getAddedCells().get(id);
    }

    /**
     * Draw the parent of already drawn nodes and connect them using edges.
     */
    private void drawParents() {
        HashMap<Integer, Cell> internalUndrawnCells = (HashMap<Integer, Cell>) undrawnCells.clone();

        for (Cell parentCell : internalUndrawnCells.values()) {
            TreeNode parentNode = model.getTree().getNodeByKey(parentCell.getCellId());

            if (parentNode == null) {
                continue;
            }
            if (parentNode.numberChildren() != 2) {
                continue;
            }

            Cell childCell1 = treeNodeToCell(parentNode.getChild(0));
            Cell childCell2 = treeNodeToCell(parentNode.getChild(1));

            if (!drawnCells.containsValue(childCell1)) {
                continue;
            }
            if (!drawnCells.containsValue(childCell2)) {
                continue;
            }

            processParent(parentCell, childCell1, childCell2);
        }
    }

    /**
     * Relocate the parent and add the edges from the parent to the two leaves.
     *
     * @param parent A given parent cell.
     * @param child1 Child cell 1 of the parent.
     * @param child2 Child cell 2 of the parent.
     */
    private void processParent(Cell parent, Cell child1, Cell child2) {
        int parentX = (int) Math.min(child1.getLayoutX(), child2.getLayoutX()) - offset;
        int parentY = (int) (child1.getLayoutY() + child2.getLayoutY()) / 2;

        relocateCell(parent, parentX, parentY);
        ArrayList<Cell> cells = new ArrayList<>();
        Collections.addAll(cells, parent, child1, child2);
        drawPerpendicularEdges(cells);
    }


    /**
     * Draw perpendicular lines between a parent node and its children by inserting
     * two invisible helper middle cells.
     *
     * @param cells parent and its children.
     */
    private void drawPerpendicularEdges(ArrayList<Cell> cells) {
        Cell upperCorner = new MiddleCell(model.getAddedCells().size());
        Cell lowerCorner = new MiddleCell(model.getAddedCells().size() + 1);

        model.addCell(upperCorner);
        model.addCell(lowerCorner);

        model.addEdge(cells.get(0).getCellId(), upperCorner.getCellId(), 1, EdgeType.TREE);
        model.addEdge(upperCorner.getCellId(), cells.get(1).getCellId(), 1, EdgeType.TREE);
        model.addEdge(cells.get(0).getCellId(), lowerCorner.getCellId(), 1, EdgeType.TREE);
        model.addEdge(lowerCorner.getCellId(), cells.get(2).getCellId(), 1, EdgeType.TREE);

        relocateCell(upperCorner, (int) cells.get(0).getLayoutX(), (int) cells.get(1).getLayoutY());
        relocateCell(lowerCorner, (int) cells.get(0).getLayoutX(), (int) cells.get(2).getLayoutY());
    }

    /**
     * Relocate a cell and mark it as 'drawn'.
     *
     * @param cell The cell to relocate.
     * @param x    The new X coordinate of the cell.
     * @param y    The new Y coordinate of the cell.
     */
    private void relocateCell(Cell cell, double x, int y) {
        cell.relocate(x, y);
        if (y > maxY) {
            maxY = y;
        }
        undrawnCells.remove(cell.getCellId());
        drawnCells.put(cell.getCellId(), cell);
    }

    /**
     * Getter method for the max Y value.
     * @return the max Y value.
     */
    public double getMaxY() {
        return maxY;
    }
}
