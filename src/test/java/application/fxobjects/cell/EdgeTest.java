package application.fxobjects.cell;

import application.fxobjects.Cell;
import application.fxobjects.Edge;
import application.fxobjects.graphCells.RectangleCell;
import core.typeEnums.EdgeType;
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
 * @version 1.0
 * @since 04-05-2016
 */
public class EdgeTest {
    private Edge edge;

    @Mock
    public final Cell testCell = mock(RectangleCell.class);
    @Mock
    public final Line testLine = mock(Line.class);

    /**
     * Set up method for testing purposes.
     */
    @Before
    public void setUp() {
        edge = new Edge(new RectangleCell(0, 1), new RectangleCell(1, 1), 10, EdgeType.GRAPH);
    }

    /**
     * Test whether the constructor works correctly.
     *
     */
    @Test
    public void testConstructor() {
        assertNotNull(edge);
    }

    /**
     * Test whether getSource() and setSource() work correctly.
     */
    @Test
    public void testGetAndSetSource() {
        edge.setSource(testCell);

        assertEquals(testCell, edge.getSource());
    }

    /**
     * Test whether getTarget() and setTarget() work correctly.
     */
    @Test
    public void testGetAndSetTarget() {
        edge.setTarget(testCell);

        assertEquals(testCell, edge.getTarget());
    }

    /**
     * Test whether getLine() and setLine() work correcly.
     */
    @Test
    public void testGetAndSetLine() {
        edge.setLine(testLine);

        assertEquals(testLine, edge.getLine());
    }
}