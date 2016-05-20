package application.controllers;

import core.graph.Graph;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.control.ScrollPane;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

/**
 * Created by Niek on 5/19/2016.
 */
public class GraphControllerTest {
    /**
     * Set-up the controller.
     */
    @Before
    public void setUp() {


    }

    /**
     * Clean-up.
     */
    @After
    public void tearDown() {

    }

    /*
    @Test
    public void testGraphControllerConstructor() {

        GraphController gc = new GraphController(g,ref);
        assert(gc.getGraph().equals(g));
        assert(gc.getRoot().getHbarPolicy().equals(ScrollPane.ScrollBarPolicy.ALWAYS));
        assert(gc.getRoot().getVbarPolicy().equals(ScrollPane.ScrollBarPolicy.NEVER));
    }
    */
}
