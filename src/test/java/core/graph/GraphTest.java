package core.graph;

import core.Model;
import core.Node;
import core.graph.cell.CellType;
import core.graph.cell.EdgeType;
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
        when(mockedModel.addCell(anyInt(), anyString(),
                anyInt(), any(CellType.class))).thenReturn(true);

        HashMap<Integer, Node> nodeMap = new HashMap<Integer, Node>();
        nodeMap.put(1, new Node(1, CellType.RECTANGLE, "", 1));
        nodeMap.put(2, new Node(2, CellType.BUBBLE, "", 2));
        nodeMap.put(3, new Node(3, CellType.INDEL, "", 3));
        nodeMap.put(4, new Node(4, CellType.COLLECTION, "", 4));
        nodeMap.put(5, new Node(5, CellType.COLLECTION, "", 5));

        nodeMap.get(1).setGenomes(new ArrayList<>(Arrays.asList("1")));
        nodeMap.get(2).setGenomes(new ArrayList<>(Arrays.asList("1")));
        nodeMap.get(3).setGenomes(new ArrayList<>(Arrays.asList("1")));
        nodeMap.get(4).setGenomes(new ArrayList<>(Arrays.asList("1")));
        nodeMap.get(5).setGenomes(new ArrayList<>(Arrays.asList("1")));

        g.setLevelMaps(new ArrayList<>(Arrays.asList(nodeMap, nodeMap)));
    }

    /**
     * Test for the class constructor.
     */
    @Test
    public void testConstructor() {
        assertNotNull(g);
    }

    /**
     * Test for the getModel method.
     */
    @Test
    public void testGetModel() {
        Object model = g.getModel();
        assertEquals(Model.class, model.getClass());
    }

    /**
     * Test for the getNodeMapFromFile method.
     */
    @Test
    public void testGetNodeMapFromFile() {
        HashMap<Integer, Node> nodeMap = g.getNodeMapFromFile();
        assertNotEquals(0, nodeMap.size());
    }

    /**
     * Test for the generateModel method.
     */
    @Test
    public void testGenerateModel() {
        List<String> genomes = new ArrayList<>(Arrays.asList("1", "2"));
        g.setCurrentGenomes(genomes);
        g.generateModel("", 1, mockedModel);
        verify(mockedModel, atLeast(1)).addCell(anyInt(), anyString(),
                anyInt(), any(CellType.class));
    }

    /**
     * Test for the addCell method.
     */
    @Test
    public void testAddCell() {
        g.addCell(g.getLevelMaps().get(0), mockedModel, 1, "", g.getLevelMaps().get(0).get(1),
                g.getLevelMaps().get(0).get(5));
        g.addCell(g.getLevelMaps().get(0), mockedModel, 2, "", g.getLevelMaps().get(0).get(2),
                g.getLevelMaps().get(0).get(5));
        g.addCell(g.getLevelMaps().get(0), mockedModel, 3, "", g.getLevelMaps().get(0).get(3),
                g.getLevelMaps().get(0).get(5));
        g.addCell(g.getLevelMaps().get(0), mockedModel, 4, "", g.getLevelMaps().get(0).get(4),
                g.getLevelMaps().get(0).get(5));

        g.setCurrentGenomes(new ArrayList<>(Arrays.asList("1", "2")));
        verify(mockedModel, atLeast(4)).addCell(anyInt(), anyString(),
                anyInt(), any(CellType.class));
        verify(mockedModel, atLeast(4)).addEdge(anyInt(), anyInt(),
                anyInt(), any(EdgeType.class));
    }

    /**
     * Test for the intersection method.
     */
    @Test
    public void testIntersection() {
        List<String> l1 = Arrays.asList("A", "B", "C", "D");
        List<String> l2 = Arrays.asList("B", "C");

        assertEquals(2, g.intersection(l1, l2));
    }

    /**
     * Test for the get/setGenomes method.
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

    /**
     * Test for the get/setCurrentGenomes method.
     */
    @Test
    public void testCurrentGenomes() {
        List<String> genomes = new ArrayList<>();
        genomes.add("1");
        genomes.add("2");

        g.setCurrentGenomes(genomes);
        assertEquals(2, g.getCurrentGenomes().size());
        assertEquals("1", g.getCurrentGenomes().get(0));
        assertEquals("2", g.getCurrentGenomes().get(1));
    }

    /**
     * Test for the reset and getCurrentInt methods.
     */
    @Test
    public void testCurrentInt() {
        g.reset();
        assertEquals(-1, g.getCurrentInt());
    }
}