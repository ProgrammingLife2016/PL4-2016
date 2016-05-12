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
        drawParents();

    }

    /**
     * Draw the parent of already drawn nodes and connect them using edges.
     */
    private void drawParents() {
        List<TreeNode> leaves = model.getTree().getLeaves(model.getTree().getRoot());
        for (TreeNode leave : leaves) {
            TreeNode parentNode = leave.parent();
            Cell parent = undrawnCells.get(parentNode.getKey());

            if (parentNode == null) { continue; }
            if (parent == null) { continue; }

            List<TreeNode> siblings = model.getTree().getLeaves(parentNode);
            List<Cell> leavesAsCells = TreeNodesToCells(siblings);

            if (siblings.size() == 2) {
                processParent(parent, leavesAsCells.get(0), leavesAsCells.get(1));
            }
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
     * Convert a list of Tree Nodes to a list of their corresponding Cells.
     * @param treeNodes
     * @return
     */
    private List<Cell> TreeNodesToCells(List<TreeNode> treeNodes) {
        List<Cell> cells = new ArrayList<Cell>();

        for (TreeNode treeNode : treeNodes) {
            int id = treeNode.getKey();
            Cell cell = model.getAllCells().get(id);
            cells.add(cell);
        }

        return cells;
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
