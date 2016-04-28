package core;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;

/**
 * Created by Skullyhoofd on 25/04/2016.
 */
public class NodeTest {

    Node n;


    /**
     * SetUp used to test the Node Class.
     */
    @Before
    public void setUp() throws Exception {
        n = new Node(0,"ATCG",0);
        n.addLink(0);
    }

    /**
     * Test to test if Node's are added correctly.
     * @throws Exception
     */
    @Test
    public void testAddLink() throws Exception {
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
     * @throws Exception
     */
    @Test
    public void testGetId() throws Exception {
        assertEquals(n.getId(),0);
    }

    /**
     * Test the GetSequence method.
     * @throws Exception
     */
    @Test
    public void testGetSequence() throws Exception {
        assertEquals(n.getSequence(),"ATCG");
    }

    /**
     * Test the SetSequence method.
     * @throws Exception
     */
    @Test
    public void testSetSequence() throws Exception {
        n.setSequence("ATCGATCG");
        assertEquals(n.getSequence(), "ATCGATCG");

    }

    /**
     * Test the GetzIndex method.
     * @throws Exception
     */
    @Test
    public void testGetzIndex() throws Exception {
        assertEquals(n.getzIndex(), 0);
    }

    /**
     * Test the SetzIndex method.
     * @throws Exception
     */
    @Test
    public void testSetzIndex() throws Exception {
        n.setzIndex(3);
        assertEquals(n.getzIndex(), 3);
    }

    /**
     * Test the GetLinks method.
     * @throws Exception
     */
    @Test
    public void testGetLinks() throws Exception {
        ArrayList<Integer> al1 = new ArrayList<Integer>();
        al1.add(0);
        assertTrue(n.getLinks().equals(al1));
    }
}