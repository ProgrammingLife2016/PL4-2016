package application.mouseHandlers;

import application.controllers.MainController;
import application.fxobjects.graphCells.RectangleCell;
import javafx.scene.Node;
import javafx.scene.layout.StackPane;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;

/**
 * Test class for the GraphMouseHandling class
 *
 */
public class GraphMouseHandlingTest {
    GraphMouseHandling handling;

    /**
     * Sets up a GraphMouseHandling instance.
     */
    @Before
    public void setUp() {
        handling = new GraphMouseHandling(mock(MainController.class));
    }

    /**
     * Tests the setMouseHandling method.
     */
    @Test
    public void testSetMouseHandling() {
        Node n = new StackPane();
        handling.setMouseHandling(n);

        assertNotNull(n.getOnMousePressed());
        assertNotNull(n.getOnMouseEntered());
        assertNotNull(n.getOnMouseDragged());
    }

    /**
     * Tests the get/setPrevClick methods.
     */
    @Test
    public void testGetPrevClick() {
        RectangleCell cell = new RectangleCell(1, 1, spy(new StackPane()));
        handling.setPrevClick(cell);

        assertEquals(cell, handling.getPrevClick());
    }

    /**
     * Tests the get/setFocusedNode methods.
     */
    @Test
    public void testGetFocusedNode() {
        core.graph.Node n = new core.graph.Node(1, "", 1);
        handling.setFocusedNode(n);

        assertEquals(n, handling.getFocusedNode());
    }

    /**
     * Tests the getOriginallyFocusedNode method.
     */
    @Test
    public void testGetOriginallyFocusedNode() {
        assertNull(handling.getOriginallyFocusedNode());
    }

    /**
     * Tests the getOriginalZoomLevel method.
     */
    @Test
    public void testGetOriginalZoomLevel() {
        assertEquals(0, handling.getOriginalZoomLevel());
    }

}