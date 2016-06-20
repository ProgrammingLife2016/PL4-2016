package core.annotation;

import core.graph.Node;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;

import static org.junit.Assert.assertEquals;

/**
 * Test suite for the Annotation class.
 *
 */
public class AnnotationTest {
    public Annotation a;

    /**
     * Sets up an annotation object.
     */
    @Before
    public void setUp() {
        a = new Annotation();
        a.setStart(1);
    }

    /**
     * Tests the getSeqid method.
     */
    @Test
    public void testGetSeqid() {
        a.setSeqid("a");
        assertEquals("a", a.getSeqid());
    }

    /**
     * Tests the getSource method.
     */
    @Test
    public void testGetSource() {
        a.setSource("b");
        assertEquals("b", a.getSource());
    }

    /**
     * Tests the getType method.
     */
    @Test
    public void testGetType() {
        a.setType("c");
        assertEquals("c", a.getType());
    }

    /**
     * Tests the getStart method.
     */
    @Test
    public void testGetStart() {
        a.setStart(1);
        assertEquals(1, a.getStart());
    }

    /**
     * Tests the getEnd method.
     */
    @Test
    public void testGetEnd() {
        a.setEnd(2);
        assertEquals(2, a.getEnd());
    }

    /**
     * Tests the getScore method.
     */
    @Test
    public void testGetScore() {
        a.setScore(3);
        assertEquals(3, a.getScore(), 0.0001);
    }

    /**
     * Tests the getStrand method.
     */
    @Test
    public void testGetStrand() {
        a.setStrand("d");
        assertEquals("d", a.getStrand());
    }

    /**
     * Tests the getPhase method.
     */
    @Test
    public void testGetPhase() {
        a.setPhase("e");
        assertEquals("e", a.getPhase());
    }

    /**
     * Tests the getCallhounClassAttr method.
     */
    @Test
    public void testGetCallhounClassAttr() {
        a.setCallhounClassAttr("f");
        assertEquals("f", a.getCallhounClassAttr());
    }

    /**
     * Tests the getIdAttr method.
     */
    @Test
    public void testGetIdAttr() {
        a.setIdAttr(4);
        assertEquals(4, a.getIdAttr(), 0.0001);
    }

    /**
     * Tests the getNameAttr method.
     */
    @Test
    public void testGetNameAttr() {
        a.setNameAttr("g");
        assertEquals("g", a.getNameAttr());
    }

    /**
     * Tests the getDisplayNameAttr method.
     */
    @Test
    public void testGetDisplayNameAttr() {
        a.setDisplayNameAttr("h");
        assertEquals("h", a.getDisplayNameAttr());
    }

    /**
     * Tests the getSpannedNodes method.
     */
    @Test
    public void testGetSpannedNodes() {
        assertEquals(0, a.getSpannedNodes().size());

        a.addSpannedNode(new Node(1, "", 1));
        a.addSpannedNode(new Node(2, "", 2));

        assertEquals(2, a.getSpannedNodes().size());
    }

    /**
     * Tests the compareTo method.
     */
    @Test
    public void testCompareTo() {
        Annotation b = new Annotation();

        b.setStart(0);
        assertEquals(1, a.compareTo(b));

        b.setStart(1);
        assertEquals(1, a.compareTo(b));

        b.setStart(2);
        assertEquals(-1, a.compareTo(b));
    }

    /**
     * Tests the detNodesSpannedByAnnotation method.
     */
    @Test
    public void testDetNodesSpannedByAnnotation() {
        Node n1 = new Node(1, "abc", 1);
        Node n2 = new Node(2, "def", 4);
        Node n3 = new Node(2, "xyz", 20);

        HashMap<Integer, Node> nodeMap = new HashMap<>();
        nodeMap.put(0, n1);
        nodeMap.put(1, n2);
        nodeMap.put(2, n3);
        nodeMap.put(3, null);

        Annotation ann = new Annotation();
        ann.setStart(2);
        ann.setEnd(5);

        assertEquals(0, ann.getSpannedNodes().size());
        ann.detNodesSpannedByAnnotation(nodeMap);
        assertEquals(2, ann.getSpannedNodes().size());
    }
}