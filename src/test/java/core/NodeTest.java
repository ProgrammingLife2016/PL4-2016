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
     * Test to test if Node's are added correctly.
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
     * Test the getLiveLinks method with all links in the nodeMap.
     */
    @Test
    public void testGetLiveLinksFull() {
        Node n1 = new Node(1, "A", 1);
        Node n2 = new Node(2, "B", 2);
        Node n3 = new Node(3, "C", 3);

        n1.addLink(n2.getId());
        n1.addLink(n3.getId());

        HashMap<Integer, Node> nodeMap = new HashMap<>();
        nodeMap.put(n1.getId(), n1);
        nodeMap.put(n2.getId(), n2);
        nodeMap.put(n3.getId(), n3);

        List<Node> liveNodes = n1.getLiveLinks(nodeMap);
        assertEquals(liveNodes.size(), 2);
        assertEquals(liveNodes.get(0), n2);
        assertEquals(liveNodes.get(1), n3);
    }

    /**
     * Test the getLiveParents method with only one parent in the nodeMap.
     */
    @Test
    public void testGetLiveLinksPartial() {
        Node n1 = new Node(1, "A", 1);
        Node n2 = new Node(2, "B", 2);
        Node n3 = new Node(3, "C", 3);

        n1.addParent(n2.getId());
        n1.addParent(n3.getId());

        HashMap<Integer, Node> nodeMap = new HashMap<>();
        nodeMap.put(n1.getId(), n1);
        nodeMap.put(n2.getId(), n2);

        List<Node> liveParents = n1.getLiveParents(nodeMap);
        assertEquals(liveParents.size(), 1);
        assertEquals(liveParents.get(0), n2);
    }

    /**
     * Test the getLiveParents method with all parents in the nodeMap.
     */
    @Test
    public void testGetLiveParentsFull() {
        Node n1 = new Node(1, "A", 1);
        Node n2 = new Node(2, "B", 2);
        Node n3 = new Node(3, "C", 3);

        n1.addParent(n2.getId());
        n1.addParent(n3.getId());

        HashMap<Integer, Node> nodeMap = new HashMap<>();
        nodeMap.put(n1.getId(), n1);
        nodeMap.put(n2.getId(), n2);
        nodeMap.put(n3.getId(), n3);

        List<Node> liveParents = n1.getLiveParents(nodeMap);
        assertEquals(liveParents.size(), 2);
        assertEquals(liveParents.get(0), n2);
        assertEquals(liveParents.get(1), n3);
    }

    /**
     * Test the getLiveLinks method with only one link in the nodeMap.
     */
    @Test
    public void testGetLiveParentsPartial() {
        Node n1 = new Node(1, "A", 1);
        Node n2 = new Node(2, "B", 2);
        Node n3 = new Node(3, "C", 3);

        n1.addLink(n2.getId());
        n1.addLink(n3.getId());

        HashMap<Integer, Node> nodeMap = new HashMap<>();
        nodeMap.put(n1.getId(), n1);
        nodeMap.put(n2.getId(), n2);

        List<Node> liveNodes = n1.getLiveLinks(nodeMap);
        assertEquals(liveNodes.size(), 1);
        assertEquals(liveNodes.get(0), n2);
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
     * Test GetId method.
     */
    @Test
    public void testGetId() {
        assertEquals(n.getId(), 0);
    }

    /**
     * Test the GetSequence method.
     */
    @Test
    public void testGetSequence() {
        assertEquals(n.getSequence(), "ATCG");
    }

    /**
     * Test the SetSequence method.
     */
    @Test
    public void testSetSequence() {
        n.setSequence("ATCGATCG");
        assertEquals(n.getSequence(), "ATCGATCG");

    }

    /**
     * Test the GetzIndex method.
     */
    @Test
    public void testGetzIndex() {
        assertEquals(n.getzIndex(), 0);
    }

    /**
     * Test the SetzIndex method.
     */
    @Test
    public void testSetzIndex() {
        n.setzIndex(3);
        assertEquals(n.getzIndex(), 3);
    }

    /**
     * Test the GetLinks method.
     */
    @Test
    public void testGetLinks() {
        ArrayList<Integer> al1 = new ArrayList<Integer>();
        al1.add(0);
        assertTrue(n.getLinks().equals(al1));
    }
}