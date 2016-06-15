package application.fxobjects.layout;

import application.fxobjects.Cell;
import application.fxobjects.graphCells.RectangleCell;
import core.graph.Graph;
import core.model.Model;
import core.typeEnums.CellType;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;

/**
 * Test class for GraphLayout.java.
 *
 * @author Arthur Breurkes.
 * @since 04-05-2016
 * @version 1.0
 */
@SuppressWarnings({
       "checkstyle:magicnumber"
})
public class GraphLayoutTest {
    private GraphLayout graphLayout;

    @Mock
    public final Graph testGraph = mock(Graph.class);

    /**
     * Set up method for testing purposes.
     * @throws Exception possible Exception.
     */
    @Before
    public void setUp() throws Exception {
        graphLayout = spy(new GraphLayout(null, 0, 0));
    }

    /**
     * Test whether the constructor works correctly.
     * @throws Exception possible Exception.
     */
    @Test
    public void testConstructor() throws Exception {
        assertNotNull(graphLayout);
    }

    /**
     * Test whether getOffset() and setOffset() work correctly.
     * @throws Exception possible Exception.
     */
    @Test
    public void testGetAndSetOffset() throws Exception {
        graphLayout.setOffset(0);

        assertEquals(0, graphLayout.getOffset());
    }

    /**
     * Test whether getCurrentX() and setCurrentX() work correctly.
     * @throws Exception possible Exception.
     */
    @Test
    public void testGetAndSetCurrentX() throws Exception {
        graphLayout.setCurrentX(42);

        assertEquals(42, graphLayout.getCurrentX());
    }

    /**
     * Test whether getCurrentY() and setCurrentY() work correctly.
     * @throws Exception possible Exception.
     */
    @Test
    public void testGetAndSetCurrentY() throws Exception {
        graphLayout.setCurrentY(42);

        assertEquals(42, graphLayout.getCurrentY());
    }

    /**
     * Test whether getLastTyp() and setLastType() work correctly.
     * @throws Exception possible Exception.
     */
    @Test
    public void testGetAndSetLastType() throws Exception {
        graphLayout.setLastType(CellType.RECTANGLE);

        assertEquals(CellType.RECTANGLE, graphLayout.getLastType());
    }

    /**
     * Test whether getCellCount() and setCellCount() work correctly.
     * @throws Exception possible Exception.
     */
    @Test
    public void testGetAndSetCellCount() throws Exception {
        graphLayout.setCellCount(42);

        assertEquals(42, graphLayout.getCellCount());
    }

    /**
     * Tests whether the execute method actually relocates a cell.
     */
    @Test
    public void testExecute() {
        Cell cell = spy(new RectangleCell(1, 1));

        Model model = spy(new Model());
        model.addCell(cell);

        graphLayout = new GraphLayout(model, 20, 500);

        assertFalse(cell.isRelocated());
    }

    /**
     * Tests whether the breathFirstPlacing method actually relocates a cell.
     */
    @Test
    public void testBreadthFirstPlacing() {
        Cell celll = spy(new RectangleCell(1, 1));
        Cell cell2 = spy(new RectangleCell(2, 1));

        celll.addCellChild(cell2);

        Model model = spy(new Model());
        model.addCell(celll);

        graphLayout = new GraphLayout(model, 20, 500);

        assertFalse(cell2.isRelocated());
        graphLayout.breadthFirstPlacing(celll);
        assertTrue(cell2.isRelocated());
    }

}