package core.graph;

import application.fxobjects.graph.cell.PhylogeneticCell;
import core.Model;
import net.sourceforge.olduvai.treejuxtaposer.drawer.Tree;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

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
     * Test the getTreeFromFile method.
     */
    @Test
    public void testGetTreeFromFile() {
        try {
            Tree tree = pt.getTreeFromFile();
            assertNotEquals(0, tree.getLeafCount());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}