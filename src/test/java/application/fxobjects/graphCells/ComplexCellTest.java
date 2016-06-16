package application.fxobjects.graphCells;

import core.typeEnums.CellType;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Shape;
import javafx.scene.text.Text;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.spy;

/**
 * Test suite for the CollectionCell class.
 *
 */
public class ComplexCellTest {
    ComplexCell c;
    StackPane pane;

    /**
     * Set up the cell for testing.
     */
    @Before
    public void setUp() {
        pane = spy(new StackPane());
        Text text = spy(new Text());

        c = new ComplexCell(1, 1, pane, text);
    }

    /**
     * Tests the getType method.
     */
    @Test
    public void testGetType() {
        assertEquals(CellType.COMPLEX, c.getType());
    }

    /**
     * Tests the resetFocus method.
     */
    @Test
    public void testResetFocus() {
        Shape shape = (Shape) pane.getChildren().get(0);
        c.focus();

        assertEquals(Color.PURPLE, shape.getStroke());
        assertEquals(4, shape.getStrokeWidth(), 0.0001);

        c.resetFocus();

        assertEquals(Color.DARKRED, shape.getStroke());
        assertEquals(1, shape.getStrokeWidth(), 0.0001);
    }
}