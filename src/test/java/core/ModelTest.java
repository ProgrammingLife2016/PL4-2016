package core;

import application.fxobjects.Cell;
import application.fxobjects.Edge;
import core.graph.Node;
import core.model.Model;
import core.typeEnums.CellType;
import core.typeEnums.EdgeType;
import net.sourceforge.olduvai.treejuxtaposer.drawer.Tree;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;

/**
 * Created by Ties on 11-5-2016.
 */
public class ModelTest {

    /**
     * Test the constructor.
     */
    @Test
    public void testConstructor() {
        Model m = new Model();
        assertNotNull(m);
    }

    /**
     * Test the clearSelection method by clearing a set model.
     */
    @Test
    public void testClear() {
        Model m = new Model();
        m.clear();

        assertEquals(0, m.getAddedCells().size());
        assertEquals(0, m.getAddedEdges().size());
        assertEquals(0, m.getAllCells().size());
        assertEquals(0, m.getAllEdges().size());
    }

    /**
     * Test for clearAddedList by clearing a set addedList.
     */
    @Test
    public void testClearAddedLists() {
        Model m = new Model();
        m.clearAddedLists();

        assertEquals(0, m.getAddedCells().size());
    }

    /**
     * Test the getTree method.
     */
    @Test
    public void testGetTree() {
        Model m = new Model();
        Tree tree = new Tree();

        m.setTree(tree);
        assertEquals(tree, m.getTree());
    }

    /**
     * Test the getAddedCells method.
     */
    @Test
    public void testGetAddedCells() {
        Model m = new Model();
        m.addCell(mock(Cell.class));

        assertEquals(1, m.getAddedCells().size());
    }

    /**
     * Test the getAllCells method.
     */
    @Test
    public void testGetAllCells() {
        Model m = new Model();
        m.addCell(mock(Cell.class));

        m.merge();
        assertEquals(1, m.getAllCells().size());
    }

    /**
     * Test the getAddedEdges method.
     */
    @Test
    public void testGetAddedEdges() {
        Model m = new Model();
        m.addCell(mock(Cell.class));
        m.merge();

        m.addEdge(1, 1, 1, EdgeType.GRAPH);

        List<Edge> edges = m.getAddedEdges();
        assertTrue(edges.size() == 0);
    }

    /**
     * Test the getAllEdges method.
     */
    @Test
    public void testGetAllEdges() {
        Model m = new Model();
        m.addCell(1, "", 1, CellType.RECTANGLE);
        m.addCell(2, "", 1, CellType.RECTANGLE);
        m.addCell(3, "", 1, CellType.RECTANGLE);

        m.addEdge(1, 2, 1, EdgeType.GRAPH);
        m.addEdge(2, 3, 1, EdgeType.GRAPH);

        m.merge();
        List<Edge> edges = m.getAllEdges();
        assertEquals(2, edges.size());
    }

    /**
     * Test the getLevelMaps method.
     */
    @Test
    public void testGetLevelMaps() {
        Model m = new Model();
        HashMap<Integer, Node> levelMap1 = new HashMap<>();
        HashMap<Integer, Node> levelMap2 = new HashMap<>();
        List<HashMap<Integer, Node>> levelMaps = new ArrayList<>();

        levelMaps.add(levelMap1);
        levelMaps.add(levelMap2);

        m.setLevelMaps(levelMaps);
        assertEquals(2, m.getLevelMaps().size());
    }

    /**
     * Test the addEdge method.
     */
    @Test
    public void testAddEdge() {
        Model m = new Model();
        m.addCell(mock(Cell.class));
        m.addEdge(1, 1, 1, EdgeType.GRAPH);

        List<Edge> edges = m.getAddedEdges();
        assertEquals(0, edges.size());
    }
}
