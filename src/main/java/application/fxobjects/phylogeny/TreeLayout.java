package application.fxobjects.phylogeny;

import application.fxobjects.graph.cell.Cell;
import application.fxobjects.graph.cell.CellLayout;
import application.fxobjects.graph.cell.PhylogeneticLeafCell;
import core.graph.cell.CellType;

import net.sourceforge.olduvai.treejuxtaposer.drawer.TreeNode;

/**
 * Created by Niek on 5/9/2016.
 */
public class TreeLayout extends CellLayout{

    private TreeNode root;
    private int offset;
    private int currentX;
    private int currentY;
    private CellType lastType;
    private int cellCount = 0;
    private int centerY;

    private int currentRow;
    private static final int baseX = 200;
    private static final int baseY = 200;

    public TreeLayout(TreeNode root, int offset, int middle) {
        this.currentX = baseX;
        this.currentY = baseY;
        this.lastType = null;
        this.offset = offset;
        this.root = root;
        this.centerY = middle;
        System.out.println(centerY + "   center Y");
    }

    @Override
    public void execute() {
        Cell testCell = new PhylogeneticLeafCell(1, "Tester");
        testCell.relocate(currentX, currentY);
    }
}
