package core
import application.fxobjects.graph.cell.Cell
import application.fxobjects.graph.cell.RectangleCell

import static org.mockito.Mockito.mock
/**
 * Created by Ties on 11-5-2016.
 */
class ModelTest extends GroovyTestCase {

    void testConstructor() {
        Model m = new Model()
        assertNotNull(m)
    }

    void testClear() {
        Model m = new Model()
        m.clear()

        assertEquals(0, m.getAddedCells().size())
        assertEquals(0, m.getAddedEdges().size())
        assertEquals(0, m.getAllCells().size())
        assertEquals(0, m.getAllEdges().size())
        assertEquals(0, m.getRemovedCells().size())
        assertEquals(0, m.getRemovedEdges().size())
    }

    void testClearAddedLists() {

        Model m = new Model()
        m.clearAddedLists()

        assertEquals(0, m.getAddedCells().size())
    }

    void testGetAddedCells() {
        Model m = new Model()
        m.clear()
        Cell c = mock(Cell.class);
        m.addCell(c)

        assertEquals("[" + c.toString() + "]", m.getAddedCells().toString())
    }

    void testGetRemovedCells() {
        Model m = new Model()
        m.clear()
        Cell c = mock(Cell.class);
        m.addCell(c);
        assertEquals("[]", m.getRemovedCells().toString())

    }

    void testGetAllCells() {
        Model m = new Model()
        m.clear()
        Cell c = mock(Cell.class);
        m.addCell(c);
        m.merge();
        assertEquals("[" + c.toString() + "]", m.getAllCells().toString())
    }

    void testGetAddedEdges() {
        Model m = new Model()

        Cell c1 = new RectangleCell(3, "A")
        Cell c2 = new RectangleCell(4, "B");

        m.addCell(c1);
        m.addCell(c2);

        m.merge();

        m.addEdge(3, 4, 1);

        assertTrue("[(3,4)]".equals("" + m.getAddedEdges()));

    }
}
