package core.phylogeneticTree;

import core.model.Model;
import core.typeEnums.CellType;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

/**
 * Test suite for the Phylogenetic Tree class.
 *
 * @author Niels Warnars
 */
@SuppressFBWarnings("DLS_DEAD_LOCAL_STORE")
public class PhylogeneticTreeTest {

    private PhylogeneticTree pt;
    private Model model;

    /**
     * Setup a new Phylogenetic Tree instance.
     */
    @Before
    public void setUp() {
        model = mock(Model.class);
        pt = new PhylogeneticTree(model);

        when(model.addCell(anyInt(), anyString(), anyInt(), any(CellType.class))).thenReturn(true);
    }

    /**
     * Test for the get/setModel methods.
     */
    @Test
    public void testGetModel() {
        pt.setModel(null);
        assertNull(pt.getModel());

        pt.setModel(model);
        assertEquals(model, pt.getModel());
    }
//
//    /**
//     * Test for the getTreeFromFile method.
//     */
//    @Test
//    public void testGetTreeFromFile() {
//        Tree tree = pt.getTreeFromFile();
//        assertNotEquals(0, tree.getLeafCount());
//    }

    /**
     * Test for the setup method.
     */
//    @Test
//    public void testSetup() {
//        Tree tree = pt.getTreeFromFile();
//        pt.setup(tree);
//
//        verify(model, atLeast(1)).addCell(anyInt(), anyString(), anyInt(), any(CellType.class));
//    }

}