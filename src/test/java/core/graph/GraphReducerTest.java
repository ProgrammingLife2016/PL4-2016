package core.graph;

import core.typeEnums.CellType;
import org.junit.Test;

import java.util.*;

import static org.junit.Assert.*;


/**
 * Test class for the GraphReducer class.
 */
public class GraphReducerTest {

    /**
     * Create a node map with a given amount of nodes.
     *
     * @param numNodes The number of nodes to create.
     * @return A node map with a given amount of nodes.
     */
    private HashMap<Integer, Node> createNodeMap(int numNodes) {
        HashMap<Integer, Node> nodeMap = new HashMap<>();

        for (int i = 1; i <= numNodes; i++) {
            nodeMap.put(i, new Node(i, "", i));
        }

        return nodeMap;
    }

    /**
     * Test the determineParents method without any node having a parent.
     */
    @Test
    public void testDetermineParentsEmpty() {
        HashMap<Integer, Node> nodeMap = new HashMap<>();
        Node n1 = new Node(1, "A", 1);
        nodeMap.put(1, n1);

        GraphReducer.determineParents(nodeMap);
        assertEquals(0, nodeMap.get(1).getParents().size());
    }

    /**
     * Test the determineParents method with singular linked nodes.
     */
    @Test
    public void testDetermineParentsLinkedParents() {
        HashMap<Integer, Node> nodeMap = new HashMap<>();
        Node n1 = new Node(1, "A", 1);
        Node n2 = new Node(2, "B", 2);
        Node n3 = new Node(3, "C", 3);

        n1.addLink(2);
        n2.addLink(3);

        nodeMap.put(1, n1);
        nodeMap.put(2, n2);
        nodeMap.put(3, n3);

        GraphReducer.determineParents(nodeMap);

        assertEquals(0, nodeMap.get(1).getParents().size());
        assertEquals(1, nodeMap.get(2).getParents().size());
        assertEquals(1, nodeMap.get(3).getParents().size());

        assertEquals(1, (int) nodeMap.get(2).getParents().get(0));
        assertEquals(2, (int) nodeMap.get(3).getParents().get(0));
    }

    /**
     * Test the determineParents method.
     */
    @Test
    public void testDetermineParentsDoubleParent() {
        HashMap<Integer, Node> nodeMap = new HashMap<>();
        Node n1 = new Node(1, "A", 1);
        Node n2 = new Node(2, "B", 2);
        Node n3 = new Node(3, "C", 3);

        n1.addLink(3);
        n2.addLink(3);

        nodeMap.put(1, n1);
        nodeMap.put(2, n2);
        nodeMap.put(3, n3);

        GraphReducer.determineParents(nodeMap);

        assertEquals(0, nodeMap.get(1).getParents().size());
        assertEquals(0, nodeMap.get(2).getParents().size());
        assertEquals(2, nodeMap.get(3).getParents().size());

        assertEquals(1, (int) nodeMap.get(3).getParents().get(0));
        assertEquals(2, (int) nodeMap.get(3).getParents().get(1));
    }

    /**
     * Test whether the genomes that are in the deletion edge
     * are added to the indel node when collapsed.
     */
    @Test
    public void testGenomesInIndel() {
        HashMap<Integer, Node> nodeMap = createNodeMap(5);
        nodeMap.get(1).setLinks(new ArrayList<>(Arrays.asList(3, 4, 5)));
        nodeMap.get(2).setLinks(new ArrayList<>(Arrays.asList(3)));
        nodeMap.get(3).setLinks(new ArrayList<>(Arrays.asList(5)));
        nodeMap.get(4).setLinks(new ArrayList<>(Arrays.asList(5)));

        nodeMap.get(1).setGenomes(new ArrayList<>(Arrays.asList("A", "B", "C", "D", "E")));
        nodeMap.get(2).setGenomes(new ArrayList<>(Arrays.asList("W", "X", "Y", "Z")));
        nodeMap.get(3).setGenomes(new ArrayList<>(Arrays.asList("A", "W", "X", "Y", "Z")));
        nodeMap.get(4).setGenomes(
                new ArrayList<>(Arrays.asList("B", "C", "D")));
        nodeMap.get(5).setGenomes(
                new ArrayList<>(Arrays.asList("A", "B", "C", "D", "E", "W", "X", "Y", "Z")));


        GraphReducer.determineParents(nodeMap);
        GraphReducer.collapseIndel(nodeMap, nodeMap.get(1));

        assertEquals(nodeMap.get(5).getGenomes(),
                new ArrayList<>(Arrays.asList("A", "B", "C", "D", "E", "W", "X", "Y", "Z")));
        assertEquals(nodeMap.get(3).getGenomes(),
                new ArrayList<>(Arrays.asList("A", "W", "X", "Y", "Z")));
        assertEquals(nodeMap.get(1).getLinks(), new ArrayList<>(Arrays.asList(3, 4)));
        assertEquals(nodeMap.get(4).getGenomes(),
                new ArrayList<>(Arrays.asList("B", "C", "D", "E")));
    }

    /**
     * Test whether the genomes that are in the deletion edge
     * are added to the indel node when collapsed.
     */
    @Test
    public void testGenomesInIndel2() {
        HashMap<Integer, Node> nodeMap = createNodeMap(6);
        nodeMap.get(1).setLinks(new ArrayList<>(Arrays.asList(2, 3, 4)));
        nodeMap.get(2).setLinks(new ArrayList<>(Arrays.asList(4)));
        nodeMap.get(3).setLinks(new ArrayList<>(Arrays.asList(6)));
        nodeMap.get(4).setLinks(new ArrayList<>(Arrays.asList(5)));
        nodeMap.get(5).setLinks(new ArrayList<>(Arrays.asList(6)));


        nodeMap.get(1).setGenomes(new ArrayList<>(Arrays.asList("A", "B", "C", "D", "E")));
        nodeMap.get(2).setGenomes(new ArrayList<>(Arrays.asList("B", "C")));
        nodeMap.get(3).setGenomes(new ArrayList<>(Arrays.asList("D", "E")));
        nodeMap.get(4).setGenomes(new ArrayList<>(Arrays.asList("A", "B", "C")));
        nodeMap.get(5).setGenomes(new ArrayList<>(Arrays.asList("A", "B", "C")));
        nodeMap.get(6).setGenomes(new ArrayList<>(Arrays.asList("A", "B", "C", "D", "E")));


        GraphReducer.determineParents(nodeMap);
        GraphReducer.collapseIndel(nodeMap, nodeMap.get(1));

        assertEquals(nodeMap.get(1).getGenomes(),
                new ArrayList<>(Arrays.asList("A", "B", "C", "D", "E")));
        assertEquals(nodeMap.get(6).getGenomes(),
                new ArrayList<>(Arrays.asList("A", "B", "C", "D", "E")));
        assertEquals(nodeMap.get(1).getLinks(),
                new ArrayList<>(Arrays.asList(2, 3)));
        assertEquals(nodeMap.get(2).getType(), CellType.INDEL);
        assertEquals(nodeMap.get(2).getGenomes(),
                new ArrayList<>(Arrays.asList("A", "B", "C")));
        assertEquals(nodeMap.get(4).getGenomes(),
                new ArrayList<>(Arrays.asList("A", "B", "C")));
    }

    /**
     * Test the collapsing of a triangle of nodes.
     */
    @Test
    public void testCollapseIndel() {
        HashMap<Integer, Node> nodeMap = createNodeMap(5);
        nodeMap.get(1).setSequence("A");

        nodeMap.get(1).setLinks(new ArrayList<>(Arrays.asList(2, 3)));
        nodeMap.get(2).setLinks(new ArrayList<>(Arrays.asList(3)));
        nodeMap.get(3).setLinks(new ArrayList<>(Arrays.asList(4, 5)));

        // Collapse the indel
        GraphReducer.determineParents(nodeMap);
        assertTrue(GraphReducer.collapseIndel(nodeMap, nodeMap.get(1)));

        assertEquals(1, nodeMap.get(1).getLinks(nodeMap).size());
        assertEquals(2, (int) nodeMap.get(1).getLinks(nodeMap).get(0));
        assertEquals(3, (int) nodeMap.get(2).getLinks(nodeMap).get(0));
        assertEquals(2, nodeMap.get(3).getLinks(nodeMap).size());

        assertEquals(1, nodeMap.get(1).getCollapseLevel());
        assertEquals(CellType.INDEL, nodeMap.get(2).getType());
        assertEquals("", nodeMap.get(2).getSequence());
    }

    /**
     * Test that indels in which the child has more than one child itself should not collapse.
     */
    @Test
    public void testIndelShouldNotCollapse() {
        HashMap<Integer, Node> nodeMap = createNodeMap(5);

        nodeMap.get(1).setLinks(new ArrayList<>(Arrays.asList(2, 3)));
        nodeMap.get(2).setLinks(new ArrayList<>(Arrays.asList(3, 4)));
        nodeMap.get(3).setLinks(new ArrayList<>(Arrays.asList(5)));

        // Collapse the bubble
        GraphReducer.determineParents(nodeMap);
        GraphReducer.collapseIndel(nodeMap, nodeMap.get(1));

        assertNotNull(nodeMap.get(2));
        assertNotNull(nodeMap.get(3));
        assertEquals(1, nodeMap.get(4).getParents(nodeMap).size());
        assertEquals(1, nodeMap.get(5).getParents(nodeMap).size());
    }

}