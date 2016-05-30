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
        a = new Annotation("a", "b", "c", 1, 2, 3, "d", "e", "f", 4, "g", "h");
    }

    /**
     * Tests the getSeqid method.
     */
    @Test
    public void testGetSeqid() {
        assertEquals("a", a.getSeqid());
    }

    /**
     * Tests the getSource method.
     */
    @Test
    public void testGetSource() {
        assertEquals("b", a.getSource());
    }

    /**
     * Tests the getType method.
     */
    @Test
    public void testGetType() {
        assertEquals("c", a.getType());
    }

    /**
     * Tests the getStart method.
     */
    @Test
    public void testGetStart() {
        assertEquals(1, a.getStart());
    }

    /**
     * Tests the getEnd method.
     */
    @Test
    public void testGetEnd() {
        assertEquals(2, a.getEnd());
    }

    /**
     * Tests the getScore method.
     */
    @Test
    public void testGetScore() {
        assertEquals(3, a.getScore(), 0.0001);
    }

    /**
     * Tests the getStrand method.
     */
    @Test
    public void testGetStrand() {
        assertEquals("d", a.getStrand());
    }

    /**
     * Tests the getPhase method.
     */
    @Test
    public void testGetPhase() {
        assertEquals("e", a.getPhase());
    }

    /**
     * Tests the getCallhounClassAttr method.
     */
    @Test
    public void testGetCallhounClassAttr() {
        assertEquals("f", a.getCallhounClassAttr());
    }

    /**
     * Tests the getIdAttr method.
     */
    @Test
    public void testGetIdAttr() {
        assertEquals(4, a.getIdAttr(), 0.0001);
    }

    /**
     * Tests the getNameAttr method.
     */
    @Test
    public void testGetNameAttr() {
        assertEquals("g", a.getNameAttr());
    }

    /**
     * Tests the getDisplayNameAttr method.
     */
    @Test
    public void testGetDisplayNameAttr() {
        assertEquals("h", a.getDisplayNameAttr());
    }

}