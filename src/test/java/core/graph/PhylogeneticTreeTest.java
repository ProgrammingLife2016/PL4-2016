package core.graph;
//
//import core.Model;
//import core.graph.cell.CellType;
//import org.junit.*;
//import static org.junit.Assert.*;
//import static org.mockito.Matchers.*;
//import static org.mockito.Mockito.*;

/**
 * Created by user on 18-5-2016.
 */
public class PhylogeneticTreeTest {

//    private PhylogeneticTree pt;
//
//    private Model mockedModel;
//
//    /**
//     * Setup a new Phylogenetic Tree instance.
//     */
//    @Before
//    public void setUp() {
//        pt = new PhylogeneticTree();
//        mockedModel = mock(Model.class);
//
//        when(mockedModel.addCell(anyInt(), anyString(), any(CellType.class))).thenReturn(true);
//    }
//
//    /**
//     * Test the class constructor.
//     */
//    @Test
//    public void testConstructor() {
//        assertNotNull(pt);
//    }
//
//    /**
//     * Test the getModel method.
//     */
//    @Test
//    public void testGetModel() {
//        Object model = pt.getModel();
//        assertEquals(Model.class, model.getClass());
//    }
//
//    /**
//     * Test the getTreeFromFile method.
//     */
//    @Test
//    public void testGetTreeFromFile() {
//        net.sourceforge.olduvai.treejuxtaposer.drawer.Tree tree = pt.getTreeFromFile();
//        assertNotEquals(0, tree.getLeafCount());
//    }
//
//    /**
//     * Test the setup method.
//     */
//    @Test
//    public void testSetup() {
//        pt.setModel(mockedModel);
//        pt.setup();
//        verify(mockedModel, atLeast(1)).addCell(anyInt(), anyString(), any(CellType.class));
//    }
}