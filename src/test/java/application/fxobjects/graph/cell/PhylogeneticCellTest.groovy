package application.fxobjects.graph.cell

import static core.graph.cell.CellType.PHYLOGENETIC

/**
 * Created by Ties on 11-5-2016.
 */
class PhylogeneticCellTest extends GroovyTestCase {

    PhylogeneticCell cell;

    void setUp() {
        cell = new PhylogeneticCell(1, "ATGC");
    }

    void testConstructor() {
        assertNotNull(cell);
    }

    void testGetType() {
        assertTrue("Should be Phylogenetic", cell.getType().equals(PHYLOGENETIC))
    }

    void testGetText() {
        assertTrue("Should be ATGC", cell.getText().text.equals("ATGC"))
    }
}
