package core;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Test suite for the Annotation class.
 *
 * @author Niels Warnars
 */
public class AnnotationTest {
    public Annotation a;

    /**
     * Sets up an annotation object.
     */
    @Before
    public void setUp() {
        a = new Annotation();
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

}