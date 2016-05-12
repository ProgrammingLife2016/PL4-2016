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

    public TreeLayout(Model model, int offset) {
        this.currentX = BASE_X;
        this.currentY = BASE_Y;
        this.offset = offset;
        this.model = model;
    }

    @Override
    public void execute() {
        final int leafX = model.getTree().getHeight() * 100;
        HashMap<Integer, Cell> drawnCells = new HashMap<Integer, Cell>();
        HashMap<Integer, Cell> undrawnCells = new HashMap<Integer, Cell>();

        for (Cell cell : model.getAllCells()) {
            undrawnCells.put(cell.getCellId(), cell);
        }


        for (int i = 0; i < model.getAllCells().size(); i++) {
            Cell cell = undrawnCells.get(i);
            int cellId = cell.getCellId();
            TreeNode nodeByCell = model.getTree().getNodeByKey(cellId);

            if (nodeByCell.isLeaf()) {
                currentY += offset;
                cell.relocate(leafX, currentY);
                undrawnCells.remove(cell.getCellId());
                drawnCells.put(cell.getCellId(), cell);
            }
        }

        for (TreeNode leave : model.getTree().nodes) {
            TreeNode parentNode = leave.parent();
            if (parentNode == null) { continue; }
            Cell parent = undrawnCells.get(parentNode.getKey());
            if (parent == null) { continue; }

            List<TreeNode> siblings = model.getTree().getLeaves(parentNode);
            if (siblings.size() == 2) {
                List<Cell> leavesAsCells = getLeavesAsCells(drawnCells, siblings);
                processParent(model, drawnCells, undrawnCells, parent, leavesAsCells.get(0), leavesAsCells.get(1));
            }
        }

        for (TreeNode leave : model.getTree().nodes) {
            TreeNode parentNode = leave.parent();
            if (parentNode == null) { continue; }
            Cell parent = undrawnCells.get(parentNode.getKey());
            if (parent == null) { continue; }

            List<TreeNode> siblings = model.getTree().getLeaves(parentNode);
            if (siblings.size() == 2) {
                List<Cell> leavesAsCells = getLeavesAsCells(drawnCells, siblings);
                processParent(model, drawnCells, undrawnCells, parent, leavesAsCells.get(0), leavesAsCells.get(1));
            }
        }




        System.out.println("Num drawn cells: " + drawnCells.size());
        System.out.println("Num undrawn cells: " + undrawnCells.size());
        System.out.println("Num nodes in tree: " + model.getTree().nodes.size());
    }

    private Cell getMostLeftCell(List<Cell> cells) {
        if (cells.size() == 0) { return null; }
        Cell res = cells.get(0);

        for (Cell cell : cells) {
            if (cell.getLayoutX() < res.getLayoutX()) {
                res = cell;
            }
        }
        return res;
    }

    private List<Cell> getLeavesAsCells(HashMap<Integer, Cell> cells, List<TreeNode> leaves) {
        List<Cell> leafCells = new ArrayList<Cell>();

        for (int i = 0; i < leaves.size(); i++) {
            Cell leaf = cells.get(leaves.get(i).getKey());
            if (leaf != null) {
                leafCells.add(leaf);
            }
        }

        return leafCells;
    }

    /**
     * Relocate the parent and add the edges from the parent to the two leaves.
     * @param model
     * @param drawnCells
     * @param undrawnCells
     * @param parent
     * @param leaf1
     * @param leaf2
     */
    private void processParent(Model model, HashMap<Integer, Cell> drawnCells,
                               HashMap<Integer, Cell> undrawnCells,
                               Cell parent, Cell leaf1, Cell leaf2) {

        int newX = (int)Math.min(leaf1.getLayoutX(), leaf2.getLayoutX()) - 50;
        int newY = ((int)(leaf1.getLayoutY() + leaf2.getLayoutY()) / 2);

        parent.relocate(newX, newY);

        model.addEdge(parent.getCellId(), leaf1.getCellId(), 1);
        model.addEdge(parent.getCellId(), leaf2.getCellId(), 1);

        undrawnCells.remove(parent.getCellId());
        drawnCells.put(parent.getCellId(), parent);
    }
}
