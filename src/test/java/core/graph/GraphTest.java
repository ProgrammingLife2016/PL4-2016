package core.graph;

import core.Model;
import core.Node;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Created by user on 18-5-2016.
 */
public class GraphTest {
    private Graph g;

    @Before
    public void setUp() {
        g = new Graph();
    }

    /**
     * Test the class constructor.
     */
    @Test
    public void testConstructor() {
        assertNotNull(g);
    }

    /**
     * Test the getModel method.
     */
    @Test
    public void testGetModel() {
        Object model = g.getModel();
        assertEquals(Model.class, model.getClass());
    }

    /**
     * Test the getNodeMapFromFile method.
     */
    @Test
    public void testGetNodeMapFromFile() {
        try {
            HashMap<Integer, Node> nodeMap = g.getNodeMapFromFile();
            assertNotEquals(0, nodeMap.size());
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * Test the intersection method.
     */
    @Test
    public void testIntersection() {
        List<String> l1 = Arrays.asList("A", "B", "C", "D");
        List<String> l2 = Arrays.asList("B", "C");

        assertEquals(2, g.intersection(l1, l2));
    }

    /**
     * Test the getGenomes method.
     */
    @Test
    public void testGetGenomes() {
        List<String> genomes = new ArrayList<>();
        genomes.add("1");
        genomes.add("2");

        g.setGenomes(genomes);
        assertEquals(2, g.getGenomes().size());
        assertEquals("1", g.getGenomes().get(0));
        assertEquals("2", g.getGenomes().get(0));
    }
}