package application.fxobjects.graph.cell

import static core.graph.cell.CellType.RECTANGLE

/**
 * Created by Ties on 11-5-2016.
 */
class RectangleCellTest extends GroovyTestCase {
    RectangleCell cell;

    void setUp() {
        cell = new RectangleCell(1, "ATGC");
    }

    void testConstructor() {
        assertNotNull(cell)
    }

    void testGetType() {
        assertTrue("Should be Rectanglecell", cell.getType().equals(RECTANGLE))
    }

    void testGetText() {
        assertTrue("Should be ATGC", cell.getText().text.equals("ATGC"))
    }
}
