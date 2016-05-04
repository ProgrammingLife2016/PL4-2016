package application.fxobjects.graph.cell;

import application.fxobjects.graph.Graph;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertEquals;

/**
 * Test class for BaseLayout.java.
 *
 * @author Arthur Breurkes.
 * @since 04-05-2016
 * @version 1.0
 */
@SuppressWarnings({
       "checkstyle:magicnumber"
})
public class BaseLayoutTest {
    private Graph testGraph;
    private BaseLayout baseLayout;

    /**
     * Set up method for testing purposes.
     * @throws Exception possible Exception.
     */
    @Before
    public void setUp() throws Exception {
        testGraph = new Graph();
        baseLayout = new BaseLayout(null, 0);
    }

    /**
     * Test whether the constructor works correctly.
     * @throws Exception possible Exception.
     */
    @Test
    public void testConstructor() throws Exception {
        assertNotNull(baseLayout);
    }

    /**
     * Test whether getOffset() and setOffset() work correctly.
     * @throws Exception possible Exception.
     */
    @Test
    public void testGetAndSetOffset() throws Exception {
        baseLayout.setOffset(0);

        assertEquals(0, baseLayout.getOffset());
    }

    /**
     * Test whether getGraph() and setGraph() work correctly.
     * @throws Exception possible Exception.
     */
    @Test
    public void testGetAndSetGraph() throws Exception {
        baseLayout.setGraph(testGraph);

        assertEquals(testGraph, baseLayout.getGraph());
    }

    /**
     * Test whether getCurrentX() and setCurrentX() work correctly.
     * @throws Exception possible Exception.
     */
    @Test
    public void testGetAndSetCurrentX() throws Exception {
        baseLayout.setCurrentX(42);

        assertEquals(42, baseLayout.getCurrentX());
    }

    /**
     * Test whether getCurrentY() and setCurrentY() work correctly.
     * @throws Exception possible Exception.
     */
    @Test
    public void testGetAndSetCurrentY() throws Exception {
        baseLayout.setCurrentY(42);

        assertEquals(42, baseLayout.getCurrentY());
    }

    /**
     * Test whether getLastTyp() and setLastType() work correctly.
     * @throws Exception possible Exception.
     */
    @Test
    public void testGetAndSetLastType() throws Exception {
        baseLayout.setLastType(CellType.RECTANGLE);

        assertEquals(CellType.RECTANGLE, baseLayout.getLastType());
    }

    /**
     * Test whether getCellCount() and setCellCount() work correctly.
     * @throws Exception possible Exception.
     */
    @Test
    public void testGetAndSetCellCount() throws Exception {
        baseLayout.setCellCount(42);

        assertEquals(42, baseLayout.getCellCount());
    }

    /**
     * Test whether getMaxDepth() and setMaxDepth() work correctly.
     * @throws Exception possible Exception.
     */
    @Test
    public void testGetAndSetMaxDepth() throws Exception {
        baseLayout.setMaxDepth(42);

        assertEquals(42, baseLayout.getMaxDepth());
    }

    /**
     * Test whether getCount() and setCount() work correctly.
     * @throws Exception possible Exception.
     */
    @Test
    public void testGetAndSetCount() throws Exception {
        baseLayout.setCount(42);

        assertEquals(42, baseLayout.getCount());
    }
}