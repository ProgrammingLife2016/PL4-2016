package application.fxobjects.graph.cell;

import core.graph.Graph;
import core.graph.cell.CellType;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;

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
    private BaseLayout baseLayout;

    @Mock
    public final Graph testGraph = mock(Graph.class);

    /**
     * Set up method for testing purposes.
     * @throws Exception possible Exception.
     */
    @Before
    public void setUp() throws Exception {
        baseLayout = new BaseLayout(null, 0, 0);
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

}