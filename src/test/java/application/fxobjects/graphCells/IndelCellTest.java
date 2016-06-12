package application.fxobjects.graphCells;

import core.typeEnums.CellType;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.spy;

/**
 * Test suite for the IndelCell class.
 *
 * @author Niels Warnars
 */
public class IndelCellTest {
    IndelCell c;

    /**
     * Set up the cell for testing.
     */
    @Before
    public void setUp() {
        StackPane pane = spy(new StackPane());
        Text text = spy(new Text());

        c = new IndelCell(1, 1, pane, text);
    }

    /**
     * Tests the getType method.
     */
    @Test
    public void testGetType() {
        assertEquals(CellType.INDEL, c.getType());
    }

}