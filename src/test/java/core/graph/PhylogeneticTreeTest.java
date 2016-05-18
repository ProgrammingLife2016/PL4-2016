package core.graph;

import application.fxobjects.graph.cell.PhylogeneticCell;
import core.Model;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by user on 18-5-2016.
 */
public class PhylogeneticTreeTest {

    PhylogeneticTree pt;

    @Before
    public void setUp() {
        pt = new PhylogeneticTree();
    }

    /**
     * Test the class constructor.
     */
    @Test
    public void testConstructor() {
        assertNotNull(pt);
    }
    /**
     * Test the getModel method.
     */
    @Test
    public void testGetModel() {
        Object model = pt.getModel();
        assertEquals(Model.class, model.getClass());
    }

    /**
     * Test the setup method.
     */
    @Test
    public void testSetup() {

    }

    /**
     * Test the endUpdate method.
     */
    @Test
    public void testEndUpdate() {

    }
}