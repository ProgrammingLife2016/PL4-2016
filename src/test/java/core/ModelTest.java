package core;

import application.fxobjects.graph.cell.Cell;
import application.fxobjects.graph.cell.Edge;
import core.graph.cell.CellType;
import net.sourceforge.olduvai.treejuxtaposer.drawer.Tree;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

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
     * Test the clear method by clearing a set model.
     */
    @Test
    public void testClear() {
        Model m = new Model();
        m.addCell(1, "A", CellType.RECTANGLE);
        m.addEdge(1, 2, 1);

        m.merge();
        m.addCell(2, "B", CellType.RECTANGLE);
        m.addEdge(2, 3, 1);

        assertEquals(1, m.getAddedCells().size());
        assertEquals(1, m.getAddedEdges().size());
        assertEquals(1, m.getAllCells().size());
        assertEquals(1, m.getAllEdges().size());

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
        m.clear();
        m.addCell(3, "A", CellType.RECTANGLE);

        assertEquals(1, m.getAddedCells().size());
        assertEquals(3, m.getAddedCells().get(0));
    }

    /**
     * Test the getAllCells method.
     */
    @Test
    public void testGetAllCells() {
        Model m = new Model();
        m.clear();

        m.addCell(3, "A", CellType.RECTANGLE);
        m.merge();
        assertEquals(1, m.getAllCells().size());
        assertEquals(3, m.getAddedCells().get(0).getCellId());
    }

    /**
     * Test the getAddedEdges method.
     */
    @Test
    public void testGetAddedEdges() {
        Model m = new Model();
        m.addCell(3, "A", CellType.RECTANGLE);
        m.addCell(4, "B", CellType.RECTANGLE);

        m.merge();
        m.addEdge(3, 4, 1);

        List<Edge> edges = m.getAddedEdges();
        assertTrue(edges.size() == 1);
        assertEquals(3, edges.get(0).getSource().getCellId());
        assertEquals(4, edges.get(0).getTarget().getCellId());
    }

    /**
     * Test the getAllEdges method.
     */
    @Test
    public void testGetAllEdges() {
        Model m = new Model();
        m.addCell(1, "", CellType.RECTANGLE);
        m.addCell(2, "", CellType.RECTANGLE);
        m.addCell(3, "", CellType.RECTANGLE);

        m.addEdge(1, 2, 1);
        m.addEdge(2, 3, 1);

        m.merge();
        List<Edge> edges = m.getAllEdges();
        assertEquals(2, edges.size());
    }

    /**
     * Test the addCell method.
     */
    @Test
    public void testAddCell() {
        Model m = new Model();

        m.addCell(1, "A", CellType.RECTANGLE);
        m.addCell(2, "B", CellType.TRIANGLE);
        m.addCell(3, "C", CellType.PHYLOGENETIC);

        List<Cell> cells = m.getAddedCells();
        assertEquals(3, cells.size());

        // Test cell 1
        assertEquals(1, cells.get(0).getId());
        assertEquals("A", cells.get(0).getText());
        assertEquals(CellType.RECTANGLE, cells.get(0).getType());

        // Test cell 2
        assertEquals(2, cells.get(0).getId());
        assertEquals("B", cells.get(0).getText());
        assertEquals(CellType.TRIANGLE, cells.get(0).getType());

        // Test cell 3
        assertEquals(3, cells.get(0).getId());
        assertEquals("c", cells.get(0).getText());
        assertEquals(CellType.PHYLOGENETIC, cells.get(0).getType());
    }

    /**
     * Test the addLevelMap method.
     */
    @Test
    public void testAddLevelMap() {
        Model m = new Model();

        HashMap<Integer, Node> levelMap = new HashMap<>();
        m.addLevelMap(levelMap);
        assertEquals(1, m.getLevelMaps().size());
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
        m.addCell(1, "", CellType.RECTANGLE);
        m.addCell(2, "", CellType.RECTANGLE);
        m.addEdge(1, 2, 1);

        List<Edge> edges = m.getAddedEdges();
        assertEquals(1, edges.size());
        assertEquals(1, edges.get(0).getSource().getId());
        assertEquals(2, edges.get(0).getTarget().getId());
    }

    /**
     * Test the merge method.
     */
    @Test
    public void testMerge() {
        Model m = new Model();
        m.addCell(1, "", CellType.RECTANGLE);

        assertEquals(0, m.getAllCells().size());
        assertEquals(1, m.getAddedCells().size());

        m.merge();
        assertEquals(1, m.getAllCells().size());
        assertEquals(0, m.getAddedCells().size());
    }
}
