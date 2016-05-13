package core;

import core.graph.cell.CellType;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by Ties on 11-5-2016.
 */
public class ModelTest {

    /**
     * Test for constructor.
     */
    @Test
    public void testConstructor() {
        Model m = new Model();
        assertNotNull(m);
    }

    /**
     * Test for method clear in Model.
     */
    @Test
    public void testClear() {
        Model m = new Model();
        m.clear();

        assertEquals(0, m.getAddedCells().size());
        assertEquals(0, m.getAddedEdges().size());
        assertEquals(0, m.getAllCells().size());
        assertEquals(0, m.getAllEdges().size());
        assertEquals(0, m.getRemovedCells().size());
        assertEquals(0, m.getRemovedEdges().size());
    }

    /**
     * Test for method clearaddedlist in model.
     */
    @Test
    public void testClearAddedLists() {

        Model m = new Model();
        m.clearAddedLists();

        assertEquals(0, m.getAddedCells().size());
    }

    /**
     * Test for method getAddedCells in model.
     */
    @Test
    public void testGetAddedCells() {
        Model m = new Model();
        m.clear();
        m.addCell(3, "A", CellType.RECTANGLE);

        assertEquals("[" + 3 + "]", m.getAddedCells().toString());
    }

    /**
     * Test for Getter.
     */
    @Test
    public void testGetRemovedCells() {
        Model m = new Model();
        m.clear();

        m.addCell(3, "A", CellType.RECTANGLE);

        assertEquals("[]", m.getRemovedCells().toString());

    }

    /**
     * Test for getter.
     */
    @Test
    public void testGetAllCells() {
        Model m = new Model();
        m.clear();

        m.addCell(3, "A", CellType.RECTANGLE);
        m.merge();
        assertEquals("[" + "3" + "]", m.getAllCells().toString());
    }

    /**
     * Test for getter.
     */
    @Test
    public void testGetAddedEdges() {
        Model m = new Model();

        m.addCell(3, "A", CellType.RECTANGLE);
        m.addCell(4, "B", CellType.RECTANGLE);

        m.merge();

        m.addEdge(3, 4, 1);

        assertTrue("[(3,4)]".equals("" + m.getAddedEdges()));

    }
}
