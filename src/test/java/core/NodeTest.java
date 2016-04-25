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

    @Before
    public void setUp() throws Exception {
        n = new Node(0,"ATCG",0);
        n.addLink(0);
    }

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


    @Test
    public void testGetId() throws Exception {
        assertEquals(n.getId(),0);
    }

    @Test
    public void testGetSequence() throws Exception {
        assertEquals(n.getSequence(),"ATCG");
    }

    @Test
    public void testSetSequence() throws Exception {
        n.setSequence("ATCGATCG");
        assertEquals(n.getSequence(), "ATCGATCG");

    }

    @Test
    public void testGetzIndex() throws Exception {
        assertEquals(n.getzIndex(), 0);
    }

    @Test
    public void testSetzIndex() throws Exception {
        n.setzIndex(3);
        assertEquals(n.getzIndex(), 3);
    }

    @Test
    public void testGetLinks() throws Exception {
        ArrayList<Integer> al1 = new ArrayList<Integer>();
        al1.add(0);
        assertTrue(n.getLinks().equals(al1));
    }
}