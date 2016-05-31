package application.fxobjects.cell.tree;

import core.graph.cell.CellType;
import javafx.scene.layout.StackPane;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Test suite for the LeafCell class.
 *
 * @author Niels Warnars
 */
public class LeafCellTest {
    public LeafCell c;

    /**
     * Sets up the leaf cell.
     */
    @Before
    public void setUp() {
        StackPane pane = new StackPane();
        c = new LeafCell(1, "", pane);
    }

    /**
     * Tests the getType method.
     */
    @Test
    public void getType() {
        assertEquals(CellType.TREELEAF, c.getType());
    }

    /**
     * Tests the getName and setName methods.
     */
    @Test
    public void testName() {
        c.setName("test");
        assertEquals("test", c.getName());
    }

}