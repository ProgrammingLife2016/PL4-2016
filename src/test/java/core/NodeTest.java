package core;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

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