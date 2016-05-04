package application.fxobjects.graph.cell;

import javafx.scene.shape.Line;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;

/**
 * Test class for Edge.java.
 *
 * @author Arthur Breurkes.
 * @since 04-05-2016
 * @version 1.0
 */
public class EdgeTest {
    private Edge edge;

    @Mock
    public final Cell testCell = mock(RectangleCell.class);
    @Mock
    public final Line testLine = mock(Line.class);

    /**
     * Set up method for testing purposes.
     * @throws Exception possible Exception.
     */
    @Before
    public void setUp() throws Exception {
        edge = new Edge(new RectangleCell(0, ""), new RectangleCell(1, ""));
    }

    /**
     * Test whether the constructor works correctly.
     * @throws Exception possible Exception.
     */
    @Test
    public void testConstructor() throws Exception {
        assertNotNull(edge);
    }

    /**
     * Test whether getSource() and setSource() work correctly.
     * @throws Exception possible Exception.
     */
    @Test
    public void testGetAndSetSource() throws Exception {
        edge.setSource(testCell);

        assertEquals(testCell, edge.getSource());
    }

    /**
     * Test whether getTarget() and setTarget() work correctly.
     * @throws Exception possible Exception.
     */
    @Test
    public void testGetAndSetTarget() throws Exception {
        edge.setTarget(testCell);

        assertEquals(testCell, edge.getTarget());
    }

    /**
     * Test whether getLine() and setLine() work correcly.
     * @throws Exception possible Exception.
     */
    @Test
    public void testGetAndSetLine() throws Exception {
        edge.setLine(testLine);

        assertEquals(testLine, edge.getLine());
    }
}