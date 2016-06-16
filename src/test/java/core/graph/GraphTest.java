package core.graph;

import core.annotation.Annotation;
import core.model.Model;
import core.typeEnums.CellType;
import core.typeEnums.EdgeType;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

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
     *
     * Test for the generateModel method.
     */
    @Test
    public void testGenerateModel() {
        List<String> genomes = new ArrayList<>(Arrays.asList("1", "2"));
        g.setCurrentGenomes(genomes);
        g.generateModel(new ArrayList<>(), 1, mockedModel);
        verify(mockedModel, atLeast(0)).addCell(anyInt(), anyString(),
                anyInt(), any(CellType.class));
    }

    /**
     * Test for the addCell method.
     */
    @Test
    public void testAddCell() {
        HashMap<Integer, Node> nodeMap = g.getLevelMaps().get(0);
        Node node1 = nodeMap.get(1);
        Node node2 = nodeMap.get(2);
        Node node3 = nodeMap.get(3);
        Node node4 = nodeMap.get(4);
        g.addCell(nodeMap, new ArrayList<>(), mockedModel, node1);
        g.addCell(nodeMap, new ArrayList<>(), mockedModel, node2);
        g.addCell(nodeMap, new ArrayList<>(), mockedModel, node3);
        g.addCell(nodeMap, new ArrayList<>(), mockedModel, node4);
        verify(mockedModel, times(4)).addCell(anyInt(), anyString(),
                anyInt(), any(CellType.class));
    }

    /**
     * Test whether the edges are added to the model.
     */
    @Test
    public void testAddEdges() {
        HashMap<Integer, Node> nodeMap = g.getLevelMaps().get(0);
        Node node1 = nodeMap.get(1);
        Node node2 = nodeMap.get(2);
        Node node3 = nodeMap.get(3);
        node2.setParents(new ArrayList<>(Arrays.asList(1)));
        node3.setParents(new ArrayList<>(Arrays.asList(2)));
        g.addEdgesToCell(node2, nodeMap, mockedModel, new ArrayList<>());
        g.addEdgesToCell(node3, nodeMap, mockedModel, new ArrayList<>());
        verify(mockedModel, times(2)).addEdge(anyInt(), anyInt(),
                anyInt(), any(EdgeType.class));
    }

    /**
     * Tests whether edges are added to a model when a reference is selected.
     */
    @Test
    public void testAddEdgesWithSelectedGenomes() {
        HashMap<Integer, Node> nodeMap = g.getLevelMaps().get(0);
        Node node1 = nodeMap.get(1);
        Node node2 = nodeMap.get(2);
        Node node3 = nodeMap.get(3);
        node2.setParents(new ArrayList<>(Arrays.asList(1)));
        node3.setParents(new ArrayList<>(Arrays.asList(2)));
        g.setCurrentGenomes(new ArrayList<>(Arrays.asList("1")));
        node1.setGenomes(new ArrayList<>(Arrays.asList("1")));
        node2.setGenomes(new ArrayList<>(Arrays.asList("2")));
        node3.setGenomes(new ArrayList<>(Arrays.asList("1")));
        g.addEdgesToCell(node2, nodeMap, mockedModel, new ArrayList<>());
        g.addEdgesToCell(node3, nodeMap, mockedModel, new ArrayList<>());
        verify(mockedModel, times(2)).addEdge(anyInt(), anyInt(),
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
     * Tests the intersectingInt method.
     */
    @Test
    public void testIntersectingInt() {
        List<Integer> l1 = Arrays.asList(1, 2, 3);
        List<Integer> l2 = Arrays.asList(1, 2);

        assertEquals(2, g.intersectionInt(l1, l2));
    }

    /**
     * Tests the intersectingStrings method.
     */
    @Test
    public void testIntersectingStrings() {
        List<String> l1 = Arrays.asList("a", "b", "c");
        List<String> l2 = Arrays.asList("b");

        List<String> res = g.intersectingStrings(l1, l2);

        assertEquals(1, res.size());
        assertEquals("b", res.get(0));
    }

    /**
     * Test for the getModel method.
     */
    @Test
    public void testGetModel() {
        Graph g2 = new Graph();
        Model m = new Model();
        g2.setModel(m);

        assertEquals(m, g2.getModel());
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

    /**
     * Test the sorting of a typical bubble structure of nodes.
     */
    @Test
    public void testTopologicalSortBubble() {
        HashMap<Integer, Node> nodeMap = g.getLevelMaps().get(0);
        Node node1 = nodeMap.get(1);
        Node node2 = nodeMap.get(2);
        Node node3 = nodeMap.get(3);
        Node node4 = nodeMap.get(4);
        Node node5 = nodeMap.get(5);

        node1.setLinks(new ArrayList<>(Arrays.asList(2, 3)));
        node2.setLinks(new ArrayList<>(Arrays.asList(4)));
        node3.setLinks(new ArrayList<>(Arrays.asList(4)));
        node4.setLinks(new ArrayList<>(Arrays.asList(5)));

        node2.setParents(new ArrayList<>(Arrays.asList(1)));
        node3.setParents(new ArrayList<>(Arrays.asList(1)));
        node4.setParents(new ArrayList<>(Arrays.asList(2, 3)));
        node5.setParents(new ArrayList<>(Arrays.asList(4)));


        List<Integer> sortedNodeIds = g.topologicalSort(nodeMap);
        assertEquals(new ArrayList<>(Arrays.asList(1, 2, 3, 4, 5)), sortedNodeIds);
    }

    /**
     * Test the sorting of a typical indel structure of nodes.
     */
    @Test
    public void testTopologicalSortIndel() {
        HashMap<Integer, Node> nodeMap = g.getLevelMaps().get(0);
        Node node1 = nodeMap.get(1);
        Node node2 = nodeMap.get(2);
        Node node3 = nodeMap.get(3);
        Node node4 = nodeMap.get(4);
        Node node5 = nodeMap.get(5);

        node1.setLinks(new ArrayList<>(Arrays.asList(2)));
        node2.setLinks(new ArrayList<>(Arrays.asList(3, 4)));
        node3.setLinks(new ArrayList<>(Arrays.asList(4)));
        node4.setLinks(new ArrayList<>(Arrays.asList(5)));

        node2.setParents(new ArrayList<>(Arrays.asList(1)));
        node3.setParents(new ArrayList<>(Arrays.asList(2)));
        node4.setParents(new ArrayList<>(Arrays.asList(2, 3)));
        node5.setParents(new ArrayList<>(Arrays.asList(4)));


        List<Integer> sortedNodeIds = g.topologicalSort(nodeMap);
        assertEquals(new ArrayList<>(Arrays.asList(1, 2, 3, 4, 5)), sortedNodeIds);
    }

    /**
     * Test the sorting of a more complex structure of nodes.
     */
    @Test
    public void testTopologicalSortComplex() {
        HashMap<Integer, Node> nodeMap = g.getLevelMaps().get(0);
        Node node1 = nodeMap.get(1);
        Node node2 = nodeMap.get(2);
        Node node3 = nodeMap.get(3);
        Node node4 = nodeMap.get(4);
        Node node5 = nodeMap.get(5);

        node1.setLinks(new ArrayList<>(Arrays.asList(2, 3)));
        node2.setLinks(new ArrayList<>(Arrays.asList(5)));
        node3.setLinks(new ArrayList<>(Arrays.asList(2, 4, 5)));
        node4.setLinks(new ArrayList<>(Arrays.asList(5)));

        node2.setParents(new ArrayList<>(Arrays.asList(1, 3)));
        node3.setParents(new ArrayList<>(Arrays.asList(1)));
        node4.setParents(new ArrayList<>(Arrays.asList(3)));
        node5.setParents(new ArrayList<>(Arrays.asList(2, 3, 4)));


        List<Integer> sortedNodeIds = g.topologicalSort(nodeMap);
        assertEquals(new ArrayList<>(Arrays.asList(1, 3, 2, 4, 5)), sortedNodeIds);
    }
    /**
     * Tests the getCurrentRef method.
     */
    @Test
    public void testGetCurrentRef() {
        ArrayList<String> ref = new ArrayList<>();
        ref.add("a");
        ref.add("b");

        g.setDebugScreenShouldBeInitialized(false);
        g.addGraphComponents(ref, 1);

        assertEquals(ref, g.getCurrentRef());
    }
    /**
     * Tests the set/getAnnotations methods.
     */
    @Test
    public void testGetAnnotations() {
        List<Annotation> annotations = new ArrayList();
        g.setAnnotations(annotations);

        assertEquals(annotations, g.getAnnotations());
    }

    /**
     * Tests the getMaxWidth method.
     */
    @Test
    public void testGetMaxWidth() {
        Model m = mock(Model.class);
        when(m.getMaxWidth()).thenReturn(new Double(42));

        Graph g2 = new Graph();

        g2.setModel(m);
        assertEquals(42, g2.getMaxWidth(), 0.0001);
    }

//    /**
//     * Tests the getModelAllInView method.
//     */
//    @Test
//    public void testGetModelAllInView() {
//        Model m = g.getModel();
//        Model res = g.getModelAllInView(0);
//        assertNotEquals(m, res);
//    }

    /**
     * Tests the getDebugScreenShouldBeInitialized method.
     */
    @Test
    public void testGetDebugScreenShouldBeInitialized() {
        Graph g2 = new Graph();

        assertTrue(g2.getDebugScreenShouldBeInitialized());
        g2.setDebugScreenShouldBeInitialized(false);
        assertFalse(g2.getDebugScreenShouldBeInitialized());
    }
}