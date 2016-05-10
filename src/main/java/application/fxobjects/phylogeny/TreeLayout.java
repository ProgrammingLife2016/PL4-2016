package application.fxobjects.phylogeny;

import application.TreeItem;
import application.fxobjects.graph.cell.Cell;
import application.fxobjects.graph.cell.CellLayout;
import application.fxobjects.graph.cell.PhylogeneticCell;
import core.graph.cell.CellType;

/**
 * Created by Niek on 5/9/2016.
 */
public class TreeLayout extends CellLayout{

    private TreeItem root;
    private int offset;
    private int currentX;
    private int currentY;
    private CellType lastType;
    private int cellCount = 0;
    private int centerY;

    private int currentRow;
    private static final int baseX = 200;
    private static final int baseY = 200;

    public TreeLayout(TreeItem root, int offset, int middle) {
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
        Cell testCell = new PhylogeneticCell(1, "Tester");
        testCell.relocate(currentX, currentY);
    }
}
