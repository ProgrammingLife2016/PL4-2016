package core.graph;

import core.Model;
import core.graph.cell.CellType;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.*;

import static org.junit.Assert.*;
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.*;

/**
 * Created by user on 18-5-2016.
 */
@RunWith(MockitoJUnitRunner.class)
public class GraphTest {
    private Graph g;

    private Model mockedModel;

    /**
     * Initialize a new graph instance.
     */
    @Before
    public void setUp() {
        g = new Graph();
        mockedModel = mock(Model.class);

        when(mockedModel.addCell(anyInt(), anyString(), any(CellType.class))).thenReturn(true);
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
     * Test the getNodeMapFromFile method.
     */
    @Test
    public void testGetNodeMapFromFile() {
//        try {
        //HashMap<Integer, Node> nodeMap = g.getNodeMapFromFile();
//            assertNotEquals(0, nodeMap.size());
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

    }

    /**
     * Test the addGraphComponents method to see whether edges and cells are actually
     * added to the model.
     * @throws IOException Throw exception on file read failure.
     */
//    @Test
//    public void testAddGraphComponents() throws IOException {
//        g.setModel(mockedModel);
//        g.setresetModel(false);
//
//        assertEquals(mockedModel, g.getModel());
//
//        g.addGraphComponents(null);
//        verify(mockedModel, atLeast(1)).addCell(anyInt(), anyString(), any(CellType.class));
//        verify(mockedModel, atLeast(1)).addEdge(anyInt(), anyInt(), anyInt());
//    }

    /**
     * Test the intersection method.
     */
    @Test
    public void testIntersection() {
        List<String> l1 = Arrays.asList("A", "B", "C", "D");
        List<String> l2 = Arrays.asList("B", "C");

        assertEquals(2, g.intersection(l1, l2));
    }

    /**
     * Test the getGenomes method.
     */
    @Test
    public void testGetGenomes() {
        List<String> genomes = new ArrayList<>();
        genomes.add("1");
        genomes.add("2");

        g.setGenomes(genomes);
        assertEquals(2, g.getGenomes().size());
        assertEquals("1", g.getGenomes().get(0));
        assertEquals("2", g.getGenomes().get(1));
    }
}