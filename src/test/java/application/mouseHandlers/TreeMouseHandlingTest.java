package application.mouseHandlers;

import application.controllers.MainController;
import javafx.scene.Node;
import javafx.scene.layout.StackPane;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

/**
 * Test class for the TreeMouseHandling class.
 *
 * @author Niels Warnars
 */
public class TreeMouseHandlingTest {
    public TreeMouseHandling handling;

    /**
     * Sets up a TreeMouseHandling instance.
     */
    @Before
    public void setUp() {
        handling = new TreeMouseHandling(mock(MainController.class));
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
        assertNotNull(n.getOnMouseExited());
    }

}