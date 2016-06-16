package application.fxobjects.treeCells;

import core.typeEnums.CellType;
import javafx.scene.layout.StackPane;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.spy;

/**
 * Test suite for the MiddleCell class.
 *
 */
public class MiddleCellTest {
    public MiddleCell c;

    /**
     * Sets up the middle cell.
     */
    @Before
    public void setUp() {
        StackPane pane = spy(new StackPane());
        c = new MiddleCell(1, pane);
    }

    /**
     * Tests the getType method.
     */
    @Test
    public void getType() {
        assertEquals(CellType.TREEMIDDLE, c.getType());
    }

}