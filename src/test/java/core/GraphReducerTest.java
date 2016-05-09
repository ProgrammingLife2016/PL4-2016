package core;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;


/**
 * Test class for the GraphReducer class.
 * Created by Niels Warnars on 5-5-2016.
 */
public class GraphReducerTest {

    /**
     * Create a node map with a given amount of nodes.
     * @param numNodes  The number of nodes to create.
     * @return  A node map with a given amount of nodes.
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
        assertTrue(nodeMap.get(1).getParents().size() == 0);
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

        assertTrue(nodeMap.get(1).getParents().size() == 0);
        assertTrue(nodeMap.get(2).getParents().size() == 1);
        assertTrue(nodeMap.get(3).getParents().size() == 1);

        assertTrue(nodeMap.get(2).getParents().get(0) == 1);
        assertTrue(nodeMap.get(3).getParents().get(0) == 2);
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

        assertTrue(nodeMap.get(1).getParents().size() == 0);
        assertTrue(nodeMap.get(2).getParents().size() == 0);
        assertTrue(nodeMap.get(3).getParents().size() == 2);

        assertTrue(nodeMap.get(3).getParents().get(0) == 1);
        assertTrue(nodeMap.get(3).getParents().get(1) == 2);
    }


    /**
     * Test the collapsing of a Node sequence of four nodes.
     */
    @Test
    public void testCollapseNodeSequence() {
        HashMap<Integer, Node> nodeMap = createNodeMap(4);
        // 1-2-3-4
        nodeMap.get(1).addLink(2);
        nodeMap.get(2).addLink(3);
        nodeMap.get(3).addLink(4);

        GraphReducer.determineParents(nodeMap);
        assertTrue(GraphReducer.collapseNodeSequence(nodeMap, nodeMap.get(1)));

        // Check node 1
        assertEquals(nodeMap.get(1).getId(), nodeMap.get(1).getId());
        assertTrue(nodeMap.get(1).getLinks(nodeMap).size() == 1);
        assertTrue(nodeMap.get(1).getLinks(nodeMap).get(0) == nodeMap.get(3).getId());

        // Check node 2
        assertNull(nodeMap.get(2));

        // Check node 3
        assertEquals(nodeMap.get(3).getId(), nodeMap.get(3).getId());
        assertTrue(nodeMap.get(3).getParents(nodeMap).size() == 1);
        assertTrue(nodeMap.get(3).getParents(nodeMap).get(0) == nodeMap.get(1).getId());
    }

    /**
     * Test the collapseCheck method with too few nodes.
     */
    @Test
    public void collapseCheckTooFewNodes() {
        HashMap<Integer, Node> nodeMap = createNodeMap(1);
        nodeMap.get(1).addLink(2);

        assertFalse(GraphReducer.collapseCheck(nodeMap, nodeMap.get(1), nodeMap.get(1)));
    }

    /**
     * Test the collapseCheck method with too few nodes.
     */
    @Test
    public void collapseCheckTooManyGrandChildren() {
        HashMap<Integer, Node> nodeMap = createNodeMap(1);
        nodeMap.get(1).addLink(2);

        assertFalse(GraphReducer.collapseCheck(nodeMap, nodeMap.get(1), nodeMap.get(1)));
    }
    /**
     * Test the collapseCheck method with not a bubble.
     */
    @Test
    public void collapseCheckFalse() {
        HashMap<Integer, Node> nodeMap = createNodeMap(3);
        nodeMap.get(1).addLink(2);
        nodeMap.get(2).addLink(3);

        assertFalse(GraphReducer.collapseCheck(nodeMap, nodeMap.get(1), nodeMap.get(2)));
    }

    /**
     * Test the collapseCheck method with a bubble.
     */
    @Test
    public void testCollapseCheckTrue() {
        HashMap<Integer, Node> nodeMap = createNodeMap(4);
        nodeMap.get(1).addLink(2);
        nodeMap.get(1).addLink(3);
        nodeMap.get(2).addLink(4);
        nodeMap.get(3).addLink(4);

        assertTrue(GraphReducer.collapseCheck(nodeMap, nodeMap.get(1), nodeMap.get(2)));
        assertTrue(GraphReducer.collapseCheck(nodeMap, nodeMap.get(1), nodeMap.get(3)));
    }

    /**
     * Test the collapsing of a parent with four children.
     */
    @Test
    public void testCollapseNodeBubbles() {
        HashMap<Integer, Node> nodeMap = createNodeMap(6);

        nodeMap.get(1).setLinks(new ArrayList<>(Arrays.asList(2, 3, 4, 5)));
        nodeMap.get(2).setLinks(new ArrayList<>(Arrays.asList(6)));
        nodeMap.get(3).setLinks(new ArrayList<>(Arrays.asList(6)));
        nodeMap.get(4).setLinks(new ArrayList<>(Arrays.asList(6)));
        nodeMap.get(5).setLinks(new ArrayList<>(Arrays.asList(6)));

        GraphReducer.determineParents(nodeMap);

        assertTrue(nodeMap.get(1).getLinks(nodeMap).size() == 4);
        assertTrue(nodeMap.get(6).getParents(nodeMap).size() == 4);
        GraphReducer.collapseNodeBubbles(nodeMap, nodeMap.get(1));
        assertTrue(nodeMap.get(1).getLinks(nodeMap).size() == 1);
        assertTrue(nodeMap.get(1).getLinks(nodeMap).get(0) == nodeMap.get(5).getId());

        assertNull(nodeMap.get(2));
        assertNull(nodeMap.get(3));
        assertNull(nodeMap.get(4));

        assertTrue(nodeMap.get(5).getParents(nodeMap).size() == 1);
        assertTrue(nodeMap.get(5).getParents(nodeMap).get(0) == nodeMap.get(1).getId());
    }

    /**
     * Collapse a sequence of 100 nodes.
     */
    @Test
    public void testCollapse() {
        HashMap<Integer, Node> nodeMap = createNodeMap(100);

        for (int i = 1; i <= nodeMap.size(); i++) {
            nodeMap.get(i).setLinks(new ArrayList<>(Arrays.asList(i + 1)));
        }

        nodeMap = GraphReducer.collapse(nodeMap);
        assertTrue(nodeMap.values().size() == 51);
    }

    /**
     * Collapse a sequence of 100 nodes into multiple levels.
     */
    @Test
    public void testCreateLevelMaps() {
        HashMap<Integer, Node> nodeMap = createNodeMap(100);

        for (int i = 1; i <= nodeMap.size(); i++) {
            nodeMap.get(i).setLinks(new ArrayList<>(Arrays.asList(i + 1)));
        }

        List<HashMap<Integer, Node>> nodeMapList = GraphReducer.createLevelMaps(nodeMap);
        assertTrue(nodeMapList.size() > 1);

        for (int i = 2; i < nodeMapList.size(); i++) {
            assertTrue(nodeMapList.get(i).size() < 51);
        }
    }
}