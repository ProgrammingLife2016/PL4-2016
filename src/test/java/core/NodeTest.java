package core;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;


/**
 * Created by Skullyhoofd on 25/04/2016.
 */
public class NodeTest {

    Node n;


    /**
     * SetUp used to test the Node Class.
     */
    @Before
    public void setUp() {
        n = new Node(0, "ATCG", 0);
        n.addLink(0);
    }

    /**
     * Test whether links are added correctly.
     */
    @Test
    public void testAddLink() {
        ArrayList<Integer> al1 = new ArrayList<Integer>();
        al1.add(0);
        al1.add(1);
        al1.add(4);
        n.addLink(1);
        n.addLink(4);
        assertTrue(n.getLinks().equals(al1));
    }

    /**
     * Test the addParent method with 1 parent node.
     */
    @Test
    public void testAddParentPartial() {
        Node n1 = new Node(1, "A", 1);
        n1.addParent(21);

        assertEquals(n1.getParents().size(), 1);
        assertEquals(n1.getParents().get(0), (Integer) 21);
    }

    /**
     * Test the addParent method with multiple parent nodes.
     */
    @Test
    public void testAddParentFull() {
        Node n1 = new Node(1, "A", 1);
        n1.addParent(21);
        n1.addParent(42);

        assertEquals(n1.getParents().size(), 2);
        assertEquals(n1.getParents().get(0), (Integer) 21);
        assertEquals(n1.getParents().get(1), (Integer) 42);
    }

    /**
     * Test the addAllGenome with multiple genomes.
     */
    @Test
    public void testAddAllGenome() {
        String[] genomes = {"1", "2"};
        n.addAllGenome(genomes);

        List<String> expectedGenomes = new ArrayList<String>();
        expectedGenomes.add(genomes[0]);
        expectedGenomes.add(genomes[1]);

        assertEquals(expectedGenomes, n.getGenomes());
    }

    /**
     * Test the getLinks method with all links in the nodeMap.
     */
    @Test
    public void testGetLinksFull() {
        Node n1 = new Node(1, "A", 1);
        Node n2 = new Node(2, "B", 2);
        Node n3 = new Node(3, "C", 3);

        n1.addLink(n2.getId());
        n1.addLink(n3.getId());

        HashMap<Integer, Node> nodeMap = new HashMap<>();
        nodeMap.put(n1.getId(), n1);
        nodeMap.put(n2.getId(), n2);
        nodeMap.put(n3.getId(), n3);

        List<Integer> liveNodes = n1.getLinks(nodeMap);
        assertEquals(liveNodes.size(), 2);
        assertTrue(liveNodes.get(0) == n2.getId());
        assertTrue(liveNodes.get(1) == n3.getId());
    }

    /**
     * Test the getParents method with only one parent in the nodeMap.
     */
    @Test
    public void testGetLinksPartial() {
        Node n1 = new Node(1, "A", 1);
        Node n2 = new Node(2, "B", 2);
        Node n3 = new Node(3, "C", 3);

        n1.addParent(n2.getId());
        n1.addParent(n3.getId());

        HashMap<Integer, Node> nodeMap = new HashMap<>();
        nodeMap.put(n1.getId(), n1);
        nodeMap.put(n2.getId(), n2);

        List<Integer> liveParents = n1.getParents(nodeMap);
        assertEquals(liveParents.size(), 1);
        assertTrue(liveParents.get(0) == n2.getId());
    }

    /**
     * Test the getParents method with all parents in the nodeMap.
     */
    @Test
    public void testGetParentsFull() {
        Node n1 = new Node(1, "A", 1);
        Node n2 = new Node(2, "B", 2);
        Node n3 = new Node(3, "C", 3);

        n1.addParent(n2.getId());
        n1.addParent(n3.getId());

        HashMap<Integer, Node> nodeMap = new HashMap<>();
        nodeMap.put(n1.getId(), n1);
        nodeMap.put(n3.getId(), n2);

        List<Integer> liveParents = n1.getParents(nodeMap);
        assertEquals(liveParents.size(), 1);
        assertTrue(liveParents.get(0) == n2.getId());
    }

    /**
     * Test the getLinks method with only one link in the nodeMap.
     */
    @Test
    public void testGetParentsPartial() {
        Node n1 = new Node(1, "A", 1);
        Node n2 = new Node(2, "B", 2);
        Node n3 = new Node(3, "C", 3);

        n1.addLink(n2.getId());
        n1.addLink(n3.getId());

        HashMap<Integer, Node> nodeMap = new HashMap<>();
        nodeMap.put(n1.getId(), n1);
        nodeMap.put(n2.getId(), n2);

        List<Integer> liveNodes = n1.getLinks(nodeMap);
        assertEquals(liveNodes.size(), 1);
        assertTrue(liveNodes.get(0) == n2.getId());
    }

    /**
     * Test getId method.
     */
    @Test
    public void testGetId() {
        assertEquals(n.getId(), 0);
    }

    /**
     * Test the getSequence method.
     */
    @Test
    public void testGetSequence() {
        assertEquals(n.getSequence(), "ATCG");
    }

    /**
     * Test the setSequence method.
     */
    @Test
    public void testSetSequence() {
        n.setSequence("ATCGATCG");
        assertEquals(n.getSequence(), "ATCGATCG");

    }

    /**
     * Test the setCollapseLevel method.
     */
    @Test
    public void testSetCollapseLevel() {
        n.setCollapseLevel(42);
        assertEquals(n.getCollapseLevel(), 42);
    }

    /**
     * Test the setzIndex method.
     */
    @Test
    public void testGetzIndex() {
        assertEquals(n.getzIndex(), 0);
    }

    /**
     * Test the setzIndex method.
     */
    @Test
    public void testSetzIndex() {
        n.setzIndex(3);
        assertEquals(n.getzIndex(), 3);
    }

    /**
     * Test the setLinks method.
     */
    @Test
    public void testGetLinks() {
        ArrayList<Integer> al1 = new ArrayList<Integer>();
        al1.add(0);
        assertTrue(n.getLinks().equals(al1));
    }
}