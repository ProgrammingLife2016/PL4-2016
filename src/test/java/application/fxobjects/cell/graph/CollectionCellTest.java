package application.fxobjects.cell.graph;

import core.graph.cell.CellType;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.spy;

/**
 * Test suite for the CollectionCell class.
 *
 * @author Niels Warnars
 */
public class CollectionCellTest {
    CollectionCell c;

    /**
     * Set up the cell for testing.
     */
    @Before
    public void setUp() {
        StackPane pane = spy(new StackPane());
        Text text = spy(new Text());

        c = new CollectionCell(1, pane, text);
    }

    /**
     * Tests the getType method.
     */
    @Test
    public void testGetType() {
        assertEquals(CellType.COLLECTION, c.getType());
    }

}