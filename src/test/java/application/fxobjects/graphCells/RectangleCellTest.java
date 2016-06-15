package application.fxobjects.graphCells;

import core.typeEnums.CellType;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.scene.text.Text;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.*;

/**
 * Test class for the RectangleCell class.
 *
 * @author Niels Warnars
 */
public class RectangleCellTest {
    RectangleCell c;
    StackPane pane;

    /**
     * Set up the cell for testing.
     */
    @Before
    public void setUp() {
        pane = spy(new StackPane());
        c = new RectangleCell(1, 1, pane);
    }
    /**
     * Tests the getType method.
     */
    @Test
    public void testGetType() {
        assertEquals(CellType.RECTANGLE, c.getType());
    }

    /**
     * Tests the graphCell.focus method.
     */
    @Test
    public void testFocus() {
        c.focus();

        Shape shape = (Shape) pane.getChildren().get(0);
        assertEquals(Color.PURPLE, shape.getStroke());
        assertEquals(4, shape.getStrokeWidth(), 0.0001);
    }

    /**
     * Tests the graphCell.sideFocus method.
     */
    @Test
    public void testSideFocus() {
        c.sideFocus();

        Shape shape = (Shape) pane.getChildren().get(0);
        assertEquals(Color.MEDIUMPURPLE, shape.getStroke());
        assertEquals(4, shape.getStrokeWidth(), 0.0001);
    }

    /**
     * Tests the graphCell.originalFocus method.
     */
    @Test
    public void testOriginalFocus() {
        c.originalFocus();

        Shape shape = (Shape) pane.getChildren().get(0);
        assertEquals(Color.ORANGE, shape.getStroke());
        assertEquals(4, shape.getStrokeWidth(), 0.0001);
    }

    /**
     * Tests the setHighLight method.
     */
    @Test
    public void testSetHighLight() {
        Shape shape = (Shape) pane.getChildren().get(0);
        shape.setStrokeWidth(1);
        c.setHighLight();

        assertEquals(Color.YELLOW, shape.getStroke());
        assertEquals(4, shape.getStrokeWidth(), 0.0001);
    }

    /**
     * Tests the deselectHighLight method.
     */
    @Test
    public void testDeselectHighLight() {
        Shape shape = (Shape) pane.getChildren().get(0);
        c.setHighLight();
        c.deselectHighLight();

        assertEquals(Color.DODGERBLUE, shape.getStroke());
        assertEquals(1, shape.getStrokeWidth(), 0.0001);
    }

    /**
     * Tests the resetFocus method when the cell is not highlighted.
     */
    @Test
    public void testResetFocusNotHighlighted() {
        Shape shape = (Shape) pane.getChildren().get(0);
        c.setHighLight();
        c.deselectHighLight();
        c.resetFocus();

        assertEquals(Color.DODGERBLUE, shape.getStroke());
        assertEquals(1, shape.getStrokeWidth(), 0.0001);
    }

    /**
     * Tests the resetFocus method when the cell is highlighted.
     */
    @Test
    public void testResetFocusHighlighted() {
        Shape shape = (Shape) pane.getChildren().get(0);
        c.setHighLight();
        c.resetFocus();

        assertEquals(Color.YELLOW, shape.getStroke());
        assertEquals(4, shape.getStrokeWidth(), 0.0001);
    }
}