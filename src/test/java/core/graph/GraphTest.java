package core.graph;

import core.Model;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by user on 18-5-2016.
 */
public class GraphTest {
    Graph g;

    @Before
    public void setUp() {
        g = new Graph();
    }

    /**
     * Test the class constructor.
     */
    @Test
    public void testConstructor() {
        assertNotNull(g);
    }

    /**
     * Test the getModel method.
     */
    @Test
    public void testGetModel() {
        Object model = g.getModel();
        assertEquals(Model.class, model.getClass());
    }

    /**
     * Test the addGraphComponents method.
     */
    @Test
    public void testAddGraphComponents() {

    }

    /**
     * Test the endUpdate method.
     */
    @Test
    public void testEndUpdate() {

    }

    /**
     * Test the addPhylogeneticTree method
     */
    @Test
    public void testAddPhylogeneticTree() {

    }

    /**
     * Test the setup method.
     */
    @Test
    public void testSetup() {

    }

    /**
     * Test the getGenomes method.
     */
    @Test
    public void testGetGenomes() {

    }
}