package application.fxobjects.phylogeny;

import application.fxobjects.graph.cell.Cell;
import application.fxobjects.graph.cell.CellLayout;
import application.fxobjects.graph.cell.Edge;
import application.fxobjects.graph.cell.PhylogeneticCell;
import core.Model;
import core.graph.cell.CellType;

import net.sourceforge.olduvai.treejuxtaposer.drawer.TreeNode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Niek on 5/9/2016.
 */
public class TreeLayout extends CellLayout{

    private int offset;
    private Model model;
    private int currentX;
    private int currentY;
    private int cellCount = 0;

    private static final int BASE_X = 0;
    private static final int BASE_Y = 0;

    private HashMap<Integer, Cell> drawnCells;
    private HashMap<Integer, Cell> undrawnCells;

    public TreeLayout(Model model, int offset) {
        this.currentX = BASE_X;
        this.currentY = BASE_Y;
        this.offset = offset;
        this.model = model;

        drawnCells = new HashMap<Integer, Cell>();
        undrawnCells = new HashMap<Integer, Cell>();
    }

    @Override
    public void execute() {
        int count = 0;

        // Initially all nodes are undrawn
        for (Cell cell : model.getAllCells()) {
            undrawnCells.put(cell.getCellId(), cell);
        }

        // draw all leaves to the right of the screen.
        drawLeaves();

        // Draw parent nodes and edges to the parent nodes
        while (undrawnCells.size() != 0) {
            drawParents();
        }

        System.out.println("Num cells: " + model.getAllCells().size());
        System.out.println("Num drawn cells: " + drawnCells.size());
        System.out.println("Num undrawn cells: " + undrawnCells.size());

    }

    /**
     * Draw the parent of already drawn nodes and connect them using edges.
     */
    private void drawParents() {
        HashMap<Integer, Cell> internalUndrawnCells = (HashMap<Integer, Cell>) undrawnCells.clone();

        for (Cell parentCell : internalUndrawnCells.values()) {
            TreeNode parentNode = model.getTree().getNodeByKey(parentCell.getCellId());

            if (parentNode == null) { continue; }
            if (parentCell == null) { continue; }
            if (parentNode.numberChildren() != 2) { continue; }

            TreeNode childNode1 = parentNode.getChild(0);
            TreeNode childNode2 = parentNode.getChild(1);

            Cell childCell1 = TreeNodeToCell(childNode1);
            Cell childCell2 = TreeNodeToCell(childNode2);

            if (!drawnCells.containsValue(childCell1)) { continue; }
            if (!drawnCells.containsValue(childCell2)) { continue; }

            processParent(parentCell, childCell1, childCell2);
        }
    }

    /**
     * relocate all leaves to the right of the screen
     */
    private void drawLeaves() {
        final int leafX = model.getTree().getHeight() * 100;

        for (int i = 0; i < model.getAllCells().size(); i++) {
            Cell cell = undrawnCells.get(i);
            int cellId = cell.getCellId();
            TreeNode nodeByCell = model.getTree().getNodeByKey(cellId);

            if (nodeByCell.isLeaf()) {
                relocateCell(cell, leafX, currentY += offset);
            }
        }
    }

    /**
     * Convert a TreeNode to its corresponding Cell.
     * @param treeNode
     * @return
     */
    private Cell TreeNodeToCell(TreeNode treeNode) {
        int id = treeNode.getKey();
        return model.getAllCells().get(id);
    }


    /**
     * Relocate the parent and add the edges from the parent to the two leaves.
     * @param parent
     * @param child1
     * @param child2
     */
    private void processParent(Cell parent, Cell child1, Cell child2) {

        int newX = (int)Math.min(child1.getLayoutX(), child2.getLayoutX()) - 50;
        int newY = ((int)(child1.getLayoutY() + child2.getLayoutY()) / 2);

        model.addEdge(parent.getCellId(), child1.getCellId(), 1);
        model.addEdge(parent.getCellId(), child2.getCellId(), 1);

        relocateCell(parent, newX, newY);
    }

    /**
     * Relocate a cell and mark it as 'drawn'.
     * @param cell
     * @param x
     * @param y
     */
    private void relocateCell(Cell cell, int x, int y) {

        cell.relocate(x, y);
        undrawnCells.remove(cell.getCellId());
        drawnCells.put(cell.getCellId(), cell);
    }
}
