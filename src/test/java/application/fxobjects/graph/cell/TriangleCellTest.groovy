package application.fxobjects.graph.cell

import core.graph.cell.CellType

import static core.graph.cell.CellType.TRIANGLE

/**
 * Created by Ties on 11-5-2016.
 */
class TriangleCellTest extends GroovyTestCase {

    TriangleCell cell;

    void setUp() {
        cell = new TriangleCell(1,"ATGC");
    }

    void testConstructor() {
        assertNotNull(cell);
    }

    void testGetType() {
        assertTrue("Should be Triangle", cell.getType().equals(TRIANGLE))
    }

    void testGetText() {
        assertTrue("Should be ATGC", cell.getText().text.equals("ATGC"))
    }
}
