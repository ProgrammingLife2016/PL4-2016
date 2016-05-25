package core;

import application.fxobjects.cell.graph.BubbleCell;
import application.fxobjects.cell.graph.IndelCell;
import org.junit.Test;

import java.util.*;

import static core.GraphReducer.createLevelMaps;
import static org.junit.Assert.*;


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
     * Test the collapsing of a Node sequence of three nodes.
     */
    @Test
    public void testCollapseNodeSequence() {
        HashMap<Integer, Node> nodeMap = createNodeMap(3);
        nodeMap.get(1).addLink(2);
        nodeMap.get(2).addLink(3);

        GraphReducer.determineParents(nodeMap);
        assertTrue(GraphReducer.collapseNodeSequence(nodeMap, nodeMap.get(1)));

        assertTrue(nodeMap.get(1).getLinks(nodeMap).size() == 1);
        assertTrue(nodeMap.get(1).getLinks(nodeMap).get(0) == nodeMap.get(3).getId());

        assertNull(nodeMap.get(2));

        assertTrue(nodeMap.get(3).getParents(nodeMap).size() == 1);
        assertTrue(nodeMap.get(3).getParents(nodeMap).get(0) == nodeMap.get(1).getId());
    }

//    /**
//     * Test the collapsing of a parent with two children.
//     */
//    @Test
//    public void testCollapseBubble() {
//        HashMap<Integer, Node> nodeMap = createNodeMap(6);
//        nodeMap.get(1).setSequence("A");
//
//        nodeMap.get(1).setLinks(new ArrayList<>(Arrays.asList(2, 3)));
//        nodeMap.get(2).setLinks(new ArrayList<>(Arrays.asList(4)));
//        nodeMap.get(3).setLinks(new ArrayList<>(Arrays.asList(4)));
//        nodeMap.get(4).setLinks(new ArrayList<>(Arrays.asList(5, 6)));
//
//        // Collapse the bubble
//        GraphReducer.determineParents(nodeMap);
//        assertTrue(GraphReducer.collapseBubble(nodeMap, nodeMap.get(1)));
//
//        assertEquals(2, nodeMap.get(1).getLinks(nodeMap).size());
//        assertEquals(5, (int) nodeMap.get(1).getLinks(nodeMap).get(0));
//        assertEquals(6, (int) nodeMap.get(1).getLinks(nodeMap).get(1));
//        assertEquals(1, nodeMap.get(1).getCollapseLevel());
//        assertEquals(NodeType.BUBBLE, nodeMap.get(1).getType());
//        assertEquals("", nodeMap.get(1).getSequence());
//
//        assertNull(nodeMap.get(2));
//        assertNull(nodeMap.get(3));
//        assertNull(nodeMap.get(4));
//    }

    /**
     * Test that bubbles in which the grandchild has more parents than the parent has children
     * should not be collapsed.
     */
    @Test
    public void testBubbleShouldNotCollapse() {
        HashMap<Integer, Node> nodeMap = createNodeMap(6);

        nodeMap.get(1).setLinks(new ArrayList<>(Arrays.asList(2, 6)));
        nodeMap.get(2).setLinks(new ArrayList<>(Arrays.asList(3, 4)));
        nodeMap.get(3).setLinks(new ArrayList<>(Arrays.asList(5)));
        nodeMap.get(4).setLinks(new ArrayList<>(Arrays.asList(5)));
        nodeMap.get(6).setLinks(new ArrayList<>(Arrays.asList(5)));

        // Collapse the bubble
        GraphReducer.determineParents(nodeMap);
        assertFalse(GraphReducer.collapseBubble(nodeMap, nodeMap.get(2)));

        assertNotNull(nodeMap.get(3));
        assertNotNull(nodeMap.get(4));
        assertNotNull(nodeMap.get(5));
        assertEquals(1, nodeMap.get(6).getLinks(nodeMap).size());
    }

//    /**
//     * Test whether the counter for the number of lower collapses is working correctly
//     * when the bubble contains a collapsed bubble at a child of the parent in the bubble.
//     */
//    @Test
//    public void testBubbleCollapseLevel() {
//        List<NodeType> types = new ArrayList<>(Arrays.asList(NodeType.BUBBLE, NodeType.INDEL));
//
//        for (NodeType type : types) {
//            for (int i = 2; i <= 4; i++) {
//                HashMap<Integer, Node> nodeMap = createNodeMap(4);
//                nodeMap.get(2).setType(type);
//                nodeMap.get(i).setCollapseLevel(1);
//
//                nodeMap.get(1).setLinks(new ArrayList<>(Arrays.asList(2, 3)));
//                nodeMap.get(2).setLinks(new ArrayList<>(Arrays.asList(4)));
//                nodeMap.get(3).setLinks(new ArrayList<>(Arrays.asList(4)));
//
//                // Collapse the bubble
//                GraphReducer.determineParents(nodeMap);
//                assertTrue(GraphReducer.collapseBubble(nodeMap, nodeMap.get(1)));
//                assertEquals(2, nodeMap.get(1).getCollapseLevel());
//            }
//        }
//    }

//    /**
//     * Test whether the counter for the number of lower collapses is working correctly
//     * with chained bubbles.
//     */
//    @Test
//    public void testCollapseLevelChainedBubbles() {
//        HashMap<Integer, Node> nodeMap = createNodeMap(7);
//        nodeMap.get(1).setLinks(new ArrayList<>(Arrays.asList(2, 3)));
//        nodeMap.get(2).setLinks(new ArrayList<>(Arrays.asList(4)));
//        nodeMap.get(3).setLinks(new ArrayList<>(Arrays.asList(4)));
//
//        nodeMap.get(4).setLinks(new ArrayList<>(Arrays.asList(5, 6)));
//        nodeMap.get(5).setLinks(new ArrayList<>(Arrays.asList(7)));
//        nodeMap.get(6).setLinks(new ArrayList<>(Arrays.asList(7)));
//
//        for (int i = 1; i <= 2; i++) {
//            GraphReducer.determineParents(nodeMap);
//            assertTrue(GraphReducer.collapseBubble(nodeMap, nodeMap.get(1)));
//            assertEquals(i, nodeMap.get(1).getCollapseLevel());
//        }
//    }
//
//    /**
//     * Test whether the counter for the number of lower collapses is working correctly
//     * with chained indels.
//     */
//    @Test
//    public void testCollapseLevelChainedIndels() {
//        HashMap<Integer, Node> nodeMap = createNodeMap(5);
//        nodeMap.get(1).setLinks(new ArrayList<>(Arrays.asList(2, 3)));
//        nodeMap.get(2).setLinks(new ArrayList<>(Arrays.asList(3)));
//
//        nodeMap.get(3).setLinks(new ArrayList<>(Arrays.asList(4, 5)));
//        nodeMap.get(4).setLinks(new ArrayList<>(Arrays.asList(5)));
//
//
//        for (int i = 1; i <= 2; i++) {
//            GraphReducer.determineParents(nodeMap);
//            assertTrue(GraphReducer.collapseIndel(nodeMap, nodeMap.get(1)));
//            assertEquals(i, nodeMap.get(1).getCollapseLevel());
//        }
//    }

//    /**
//     * Test the collapsing of a triangle of nodes.
//     */
//    @Test
//    public void testCollapseIndel() {
//        HashMap<Integer, Node> nodeMap = createNodeMap(5);
//        nodeMap.get(1).setSequence("A");
//
//        nodeMap.get(1).setLinks(new ArrayList<>(Arrays.asList(2, 3)));
//        nodeMap.get(2).setLinks(new ArrayList<>(Arrays.asList(3)));
//        nodeMap.get(3).setLinks(new ArrayList<>(Arrays.asList(4, 5)));
//
//        // Collapse the indel
//        GraphReducer.determineParents(nodeMap);
//        assertTrue(GraphReducer.collapseIndel(nodeMap, nodeMap.get(1)));
//
//        assertTrue(nodeMap.get(1).getLinks(nodeMap).size() == 2);
//        assertTrue(nodeMap.get(1).getLinks(nodeMap).get(0) == 4);
//        assertTrue(nodeMap.get(1).getLinks(nodeMap).get(1) == 5);
//
//        assertEquals(1, nodeMap.get(1).getCollapseLevel());
//        assertEquals(NodeType.INDEL, nodeMap.get(1).getType());
//        assertEquals("", nodeMap.get(1).getSequence());
//
//        assertNull(nodeMap.get(2));
//        assertNull(nodeMap.get(3));
//    }

    /**
     * Test that indels in which the grandchild has more parents than the parent has children
     * should not be collapsed.
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


//    /**
//     * Test whether the counter for the number of lower collapses is working correctly.
//     */
//    @Test
//    public void testIndelCollapseLevel() {
//        List<NodeType> types = new ArrayList<>(Arrays.asList(NodeType.BUBBLE, NodeType.INDEL));
//
//        for (NodeType type : types) {
//            for (int i = 2; i <= 3; i++) {
//                HashMap<Integer, Node> nodeMap = createNodeMap(3);
//                nodeMap.get(i).setType(type);
//                nodeMap.get(i).setCollapseLevel(1);
//
//                nodeMap.get(1).setLinks(new ArrayList<>(Arrays.asList(2, 3)));
//                nodeMap.get(2).setLinks(new ArrayList<>(Arrays.asList(3)));
//
//                GraphReducer.determineParents(nodeMap);
//                assertTrue(GraphReducer.collapseIndel(nodeMap, nodeMap.get(1)));
//                assertEquals(2, nodeMap.get(1).getCollapseLevel());
//            }
//        }
//    }

//    /**
//     * Test the createLevelMaps method with superposed bubbles
//     */
//    @Test
//    public void testCreateLevelMapsWithSuperposedBubbles() {
//        HashMap<Integer, Node> nodeMap = createNodeMap(7);
//        nodeMap.get(1).setLinks(new ArrayList<>(Arrays.asList(2, 6)));
//        nodeMap.get(2).setLinks(new ArrayList<>(Arrays.asList(3, 4)));
//        nodeMap.get(3).setLinks(new ArrayList<>(Arrays.asList(5)));
//        nodeMap.get(4).setLinks(new ArrayList<>(Arrays.asList(5)));
//
//        nodeMap.get(5).setLinks(new ArrayList<>(Arrays.asList(7)));
//        nodeMap.get(6).setLinks(new ArrayList<>(Arrays.asList(7)));
//
//        List<HashMap<Integer, Node>> levelMaps = GraphReducer.createLevelMaps(nodeMap);
//
//        assertEquals(1, levelMaps.get(1).get(2).getCollapseLevel());
//        assertEquals(2, levelMaps.get(2).get(1).getCollapseLevel());
//
//        assertEquals(NodeType.BUBBLE, levelMaps.get(1).get(2).getType());
//        assertEquals(NodeType.BUBBLE, levelMaps.get(2).get(1).getType());
//
//        assertEquals(4, levelMaps.get(1).values().size());
//        assertEquals(1, levelMaps.get(2).values().size());
//    }

//    /**
//     * Test the createLevelMaps method with superposed bubbles
//     */
//    @Test
//    public void testCreateLevelMapsWithSuperposedIndels() {
//        HashMap<Integer, Node> nodeMap = createNodeMap(5);
//        nodeMap.get(1).setLinks(new ArrayList<>(Arrays.asList(2, 5)));
//        nodeMap.get(2).setLinks(new ArrayList<>(Arrays.asList(5)));
//
//        nodeMap.get(2).setLinks(new ArrayList<>(Arrays.asList(3, 4)));
//        nodeMap.get(3).setLinks(new ArrayList<>(Arrays.asList(4)));
//
//        List<HashMap<Integer, Node>> levelMaps = GraphReducer.createLevelMaps(nodeMap);
//
//        assertEquals(1, levelMaps.get(1).get(2).getCollapseLevel());
//        assertEquals(2, levelMaps.get(2).get(1).getCollapseLevel());
//
//        //assertEquals(NodeType.INDEL, levelMaps.get(1).get(2).getType());
//        //assertEquals(NodeType.INDEL, levelMaps.get(2).get(1).getType());
//
//        assertEquals(3, levelMaps.get(1).values().size());
//        assertEquals(1, levelMaps.get(2).values().size());
//    }
}