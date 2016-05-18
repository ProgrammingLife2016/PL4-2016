package core.graph;

import core.Model;
import core.Node;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.*;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;

/**
 * Created by user on 18-5-2016.
 */
public class GraphTest {
    private Graph g;

    /**
     * Initialize a new graph instance.
     */
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
     * Test the addGraphComponents method.
     */
    @Test
    public void testAddGraphComponents() {
        Model model = mock(Model.class);
        g.setModel(model);
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
        assertEquals("2", g.getGenomes().get(1));
    }
}