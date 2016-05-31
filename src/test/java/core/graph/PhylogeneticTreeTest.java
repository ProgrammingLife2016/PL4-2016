package core.graph;

import core.Model;
import core.graph.cell.CellType;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import net.sourceforge.olduvai.treejuxtaposer.drawer.Tree;
import org.junit.*;
import static org.junit.Assert.*;
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
        Tree tree = pt.getTreeFromFile();

        when(model.addCell(anyInt(), anyString(), any(CellType.class))).thenReturn(true);
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

    /**
     * Test for the getTreeFromFile method.
     */
    @Test
    public void testGetTreeFromFile() {
        Tree tree = pt.getTreeFromFile();
        assertNotEquals(0, tree.getLeafCount());
    }

    /**
     * Test for the setup method.
     */
    @Test
    public void testSetup() {
        Tree tree = pt.getTreeFromFile();
        pt.setup(tree);

        verify(model, atLeast(1)).addCell(anyInt(), anyString(), any(CellType.class));
    }
}