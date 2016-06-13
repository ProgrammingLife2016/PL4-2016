package core.graph;

import core.annotation.Annotation;
import core.genome.Genome;
import org.junit.Before;
import org.junit.Test;

import java.util.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;


/**
 * Test suite for the Node class.
 *
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
     * Tests whether links are added correctly.
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
     * Tests the addParent method with 1 parent node.
     */
    @Test
    public void testAddParent() {
        Node n1 = new Node(1, "A", 1);
        n1.addParent(21);

        assertEquals(n1.getParents().get(0), (Integer) 21);
    }


    /**
     * Tests the unionGenomes method with genome sets that overlap.
     */
    @Test
    public void testUnionGenomes() {
        Node n1 = new Node(1, "", 1);
        Node n2 = new Node(2, "", 2);

        n1.setGenomes(new ArrayList<>(Arrays.asList("1", "2", "3")));
        n2.setGenomes(new ArrayList<>(Arrays.asList("3", "4")));
        n1.unionGenomes(n2);

        assertEquals(4, n1.getGenomes().size());
        assertEquals(2, n2.getGenomes().size());

        assertEquals(new ArrayList<>(Arrays.asList("1", "2", "3", "4")), n1.getGenomes());
        assertEquals(new ArrayList<>(Arrays.asList("3", "4")), n2.getGenomes());
    }

    /**
     * Tests the addAllGenome with multiple genomes.
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
     * Tests the getLinks method with all links in the nodeMap.
     */
    @Test
    public void testGetLiveLinks() {
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
        assertEquals(liveNodes.get(0), n2.getId(), 0.0001);
        assertEquals(liveNodes.get(1), n3.getId(), 0.0001);
    }

    /**
     * Test get/setId methods.
     */
    @Test
    public void testId() {
        n.setId(42);
        assertEquals(42, n.getId());
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
        assertEquals("ATCGATCG", n.getSequence());

    }

    /**
     * Test the setCollapseLevel method.
     */
    @Test
    public void testCollapseLevelTest() {
        n.setCollapseLevel("42");
        assertEquals("42", n.getBubbleText());
    }

    /**
     * Tests the get/setzIndex method.
     */
    @Test
    public void testZIndex() {
        n.setzIndex(3);
        assertEquals(3, n.getzIndex());
    }

    /**
     * Test the setLinks method.
     */
    @Test
    public void testGetLinks() {
        ArrayList<Integer> al1 = new ArrayList<>();
        al1.add(0);
        assertEquals(al1, n.getLinks());
    }

    /**
     * Tests the getParents method with all parents in the nodeMap.
     */
    @Test
    public void testGetParents() {
        Node n1 = new Node(1, "A", 1);
        Node n2 = new Node(2, "B", 2);
        Node n3 = new Node(3, "C", 3);

        n1.addParent(n2.getId());
        n1.addParent(n3.getId());

        HashMap<Integer, Node> nodeMap = new HashMap<>();
        nodeMap.put(n1.getId(), n1);
        nodeMap.put(n3.getId(), n2);

        List<Integer> liveParents = n1.getParents(nodeMap);
        assertEquals(liveParents.get(0), n2.getId(), 0.0001);
    }

    /**
     * Tests the set/getGenomes methods.
     */
    @Test
    public void testGenomes() {
        n.setGenomes(Collections.singletonList("AA"));
        assertEquals("AA", n.getGenomes().get(0));
    }

    /**
     * Tests the getGenomesAsString method.
     */
    @Test
    public void testGetGenomesAsString() {
        n.setGenomes(Collections.singletonList("abc"));
        assertTrue(n.getGenomesAsString().contains("- abc"));
    }

    /**
     * Tests the set/getAnnotations methods.
     */
    @Test
    public void testAnnotations() {
        List<Annotation> annotations = new ArrayList<>();
        Annotation a = new Annotation();
        annotations.add(a);

        n.setAnnotations(annotations);
        assertEquals(a, n.getAnnotations().get(0));
    }

    /**
     * Tests the addAnnotation method.
     */
    @Test
    public void testAddAnnotation() {
        n.setAnnotations(new ArrayList<>());
        Annotation a = new Annotation();

        n.addAnnotation(a);
        assertEquals(a, n.getAnnotations().get(0));
    }


    /**
     * Tests the getAnnotationsAsString method
     */
    @Test
    public void testGetAnnotationsAsString() {
        Annotation a = new Annotation();
        a.setDisplayNameAttr("A");
        a.setIdAttr(42);
        a.addSpannedNode(new Node(1, "", 1));

        n.setAnnotations(new ArrayList<>(Arrays.asList(a)));
        assertTrue(n.getAnnotationsAsString().contains("- A (ID: 42), spanning nodes: 1"));
    }

    /**
     * Tests the equals method.
     */
    @Test
    public void testEquals() {
        Node n1 = new Node(1, "", 1);
        Node n2 = new Node(1, "", 1);

        assertTrue(n1.equals(n2));
    }

    /**
     * Tests the containsSameGenomes method.
     */
    @Test
    public void testContainsSameGenomes() {
        Node n1 = new Node(1, "", 1);
        Node n2 = new Node(1, "", 1);

        n1.setGenomes(new ArrayList<>(Arrays.asList("A")));
        n2.setGenomes(new ArrayList<>(Arrays.asList("A")));

        assertTrue(n1.containsSameGenomes(n2));
    }

    /**
     * Tests the set/getNucleotides methods.
     */
    @Test
    public void testNucleotides() {
        n.setNucleotides(42);
        assertEquals(42, n.getNucleotides());
    }

    /**
     * Tests the set/getPreviousLevelNodesIds methods.
     */
    @Test
    public void testPreviousLevelNodesIds() {
        ArrayList<Integer> list = new ArrayList<>();
        n.setPreviousLevelNodesIds(list);
        assertEquals(list, n.getPreviousLevelNodesIds());
    }

    /**
     * Tests the addPreviousLevelNodesId method.
     */
    @Test
    public void testAddPreviousLevelNodesId() {
        n.setPreviousLevelNodesIds(new ArrayList<>());

        n.addPreviousLevelNodesId(42);
        assertEquals(42, n.getPreviousLevelNodesIds().get(0), 0.0001);
    }

    /**
     * Tests the addPreviousLevelNodesIds method.
     */
    @Test
    public void testAddPreviousLevelNodesIds() {
        n.setPreviousLevelNodesIds(new ArrayList<>());

        ArrayList<Integer> ids = new ArrayList<>(Arrays.asList(0, 1));
        n.addPreviousLevelNodesIds(ids);
        assertTrue(n.getPreviousLevelNodesIds().containsAll(ids));
    }

    /**
     * Tests the set/getNextLevelNodeId methods.
     */
    @Test
    public void testNextLevelNodeId() {
        n.setNextLevelNodeId(42);
        assertEquals(42, n.getNextLevelNodeId());
    }
}