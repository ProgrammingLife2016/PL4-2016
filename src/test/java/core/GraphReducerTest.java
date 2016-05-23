package core;

import org.junit.Test;

import java.util.Arrays;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
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

    /**
     * Test the genome stacking of a collapse of a three-Node sequence.
     */
    @Test
    public void testCollapseNodeSequenceGenomeStacking() {
        HashMap<Integer, Node> nodeMap = createNodeMap(3);
        nodeMap.get(1).addLink(2);
        nodeMap.get(2).addLink(3);

        String[] genome1 = {"a"};
        String[] genome2 = {"b"};
        String[] genome3 = {"c"};

        nodeMap.get(1).addAllGenome(genome1);
        nodeMap.get(2).addAllGenome(genome2);
        nodeMap.get(3).addAllGenome(genome3);

        GraphReducer.determineParents(nodeMap);
        assertTrue(GraphReducer.collapseNodeSequence(nodeMap, nodeMap.get(1)));

        assertTrue(nodeMap.get(1).getGenomes().size() == 2);
        assertTrue(nodeMap.get(1).getGenomes().contains("a"));
        assertTrue(nodeMap.get(1).getGenomes().contains("b"));

        assertTrue(nodeMap.get(3).getGenomes().size() == 2);
        assertTrue(nodeMap.get(3).getGenomes().contains("b"));
        assertTrue(nodeMap.get(3).getGenomes().contains("c"));
    }

    /**
     * Test the collapsing of a parent with two children.
     */
    @Test
    public void testCollapseSymmetricalNodeBubble() {
        HashMap<Integer, Node> nodeMap = createNodeMap(4);

        nodeMap.get(1).setLinks(new ArrayList<>(Arrays.asList(2, 3)));
        nodeMap.get(2).setLinks(new ArrayList<>(Arrays.asList(4)));
        nodeMap.get(3).setLinks(new ArrayList<>(Arrays.asList(4)));

        GraphReducer.determineParents(nodeMap);
        assertTrue(GraphReducer.collapseSymmetricalNodeBubble(nodeMap, nodeMap.get(1)));

        assertTrue(nodeMap.get(1).getLinks(nodeMap).size() == 1);
        assertTrue(nodeMap.get(1).getLinks(nodeMap).get(0) == nodeMap.get(2).getId());

        assertNull(nodeMap.get(3));

        assertTrue(nodeMap.get(2).getParents(nodeMap).size() == 1);
        assertTrue(nodeMap.get(2).getParents(nodeMap).get(0) == nodeMap.get(1).getId());
    }

    /**
     * Test the genome stacking of a collapse of a four-Node bubble.
     */
    @Test
    public void testCollapseSymmetricalNodeBubbleGenomeStacking() {
        HashMap<Integer, Node> nodeMap = createNodeMap(4);

        nodeMap.get(1).setLinks(new ArrayList<>(Arrays.asList(2, 3)));
        nodeMap.get(2).setLinks(new ArrayList<>(Arrays.asList(4)));
        nodeMap.get(3).setLinks(new ArrayList<>(Arrays.asList(4)));

        String[] genome2 = {"b"};
        String[] genome3 = {"c"};

        nodeMap.get(2).addAllGenome(genome2);
        nodeMap.get(3).addAllGenome(genome3);

        GraphReducer.determineParents(nodeMap);
        GraphReducer.collapseSymmetricalNodeBubble(nodeMap, nodeMap.get(1));

        assertTrue(nodeMap.get(2).getGenomes().size() == 2);
        assertTrue(nodeMap.get(2).getGenomes().contains("b"));
        assertTrue(nodeMap.get(2).getGenomes().contains("c"));
    }

    /**
     * Test the collapsing of a triangle of nodes.
     */
    @Test
    public void testCollapseAsymmetricalNodeBubble() {
        HashMap<Integer, Node> nodeMap = createNodeMap(3);
        nodeMap.get(1).setLinks(new ArrayList<>(Arrays.asList(2, 3)));
        nodeMap.get(2).setLinks(new ArrayList<>(Arrays.asList(3)));

        GraphReducer.determineParents(nodeMap);
        assertTrue(GraphReducer.collapseAsymmetricalNodeBubble(nodeMap, nodeMap.get(1)));

        assertTrue(nodeMap.get(1).getLinks(nodeMap).size() == 1);
        assertTrue(nodeMap.get(1).getLinks(nodeMap).get(0) == nodeMap.get(3).getId());

        assertNull(nodeMap.get(2));

        assertTrue(nodeMap.get(3).getParents(nodeMap).size() == 1);
        assertTrue(nodeMap.get(3).getParents(nodeMap).get(0) == nodeMap.get(1).getId());
    }

    /**
     * Test the genome stacking of a node-triangle.
     */
    @Test
    public void testCollapseAsymmetricalNodeBubbleGenomeStacking() {
        HashMap<Integer, Node> nodeMap = createNodeMap(3);
        nodeMap.get(1).setLinks(new ArrayList<>(Arrays.asList(2, 3)));
        nodeMap.get(2).setLinks(new ArrayList<>(Arrays.asList(3)));

        String[] genome1 = {"a"};
        String[] genome2 = {"b"};
        String[] genome3 = {"c"};

        nodeMap.get(1).addAllGenome(genome1);
        nodeMap.get(2).addAllGenome(genome2);
        nodeMap.get(3).addAllGenome(genome3);

        GraphReducer.determineParents(nodeMap);
        assertTrue(GraphReducer.collapseAsymmetricalNodeBubble(nodeMap, nodeMap.get(1)));

        assertTrue(nodeMap.get(3).getGenomes().size() == 2);
        assertTrue(nodeMap.get(3).getGenomes().contains("b"));
        assertTrue(nodeMap.get(3).getGenomes().contains("c"));
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
//    @Test
//    public void testCreateLevelMaps() {
//        HashMap<Integer, Node> nodeMap = createNodeMap(100);
//
//        for (int i = 1; i <= nodeMap.size(); i++) {
//            nodeMap.get(i).setLinks(new ArrayList<>(Arrays.asList(i + 1)));
//        }
//
//        List<HashMap<Integer, Node>> nodeMapList = GraphReducer.createLevelMaps(nodeMap);
//        assertTrue(nodeMapList.size() > 1);
//
//        for (int i = 2; i < nodeMapList.size(); i++) {
//            assertTrue(nodeMapList.get(i).size() < 51);
//        }
//    }

    /**
     * Test the addGenomes method with data lists without any overlap.
     */
    @Test
    public void testAddGenomesNoOverlap() {
        HashMap<Integer, Node> nodeMap = createNodeMap(2);
        String[] genomes1 = {"a"};
        String[] genomes2 = {"b"};

        nodeMap.get(1).addAllGenome(genomes1);
        nodeMap.get(2).addAllGenome(genomes2);

        GraphReducer.addGenomes(nodeMap.get(1), nodeMap.get(2));
        List<String> node1Genomes = nodeMap.get(1).getGenomes();
        assertTrue(node1Genomes.size() == 2);
        assertTrue(node1Genomes.contains("a"));
        assertTrue(node1Genomes.contains("b"));
    }

    /**
     * Test the addGenomes method with overlapping data lists.
     */
    @Test
    public void testAddGenomesWithOverlap() {
        HashMap<Integer, Node> nodeMap = createNodeMap(2);
        String[] genomes1 = {"a", "b"};
        String[] genomes2 = {"b", "c"};

        nodeMap.get(1).addAllGenome(genomes1);
        nodeMap.get(2).addAllGenome(genomes2);

        GraphReducer.addGenomes(nodeMap.get(1), nodeMap.get(2));
        List<String> node1Genomes = nodeMap.get(1).getGenomes();
        assertTrue(node1Genomes.size() == 3);
        assertTrue(node1Genomes.contains("a"));
        assertTrue(node1Genomes.contains("b"));
        assertTrue(node1Genomes.contains("c"));
    }
}