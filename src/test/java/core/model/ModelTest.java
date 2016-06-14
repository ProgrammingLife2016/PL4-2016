package core.model;

import application.fxobjects.Cell;
import application.fxobjects.Edge;
import application.fxobjects.graphCells.RectangleCell;
import application.fxobjects.layout.GraphLayout;
import core.annotation.Annotation;
import core.graph.Node;
import core.typeEnums.CellType;
import core.typeEnums.EdgeType;
import net.sourceforge.olduvai.treejuxtaposer.drawer.Tree;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

/**
 * Test class for the model class
 *
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
     * Tests the get/setGraphLayout methods.
     */
    @Test
    public void testGraphLayout() {
        Model m = new Model();
        GraphLayout gl = new GraphLayout(m, 1, 1);
        m.setGraphLayout(gl);

        assertEquals(gl, m.getGraphLayout());
    }

    /**
     * Tests the getTree method.
     */
    @Test
    public void testGetTree() {
        Model m = new Model();
        Tree tree = new Tree();

        m.setTree(tree);
        assertEquals(tree, m.getTree());
    }

    /**
     * Tests the getLevelMapsSize method.
     */
    @Test
    public void testGetLevelMapsSize() {
        Model m = new Model();
        assertEquals(0, m.getLevelMapsSize());
    }
    /**
     * Tests the getAddedCells method.
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
     * Tests the getAddedEdges method.
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
     * Tests the getAllEdges method.
     */
    @Test
    public void testGetAllEdges() {
        Model m = new Model();
        m.addCell(1, "", 1, CellType.RECTANGLE);
        m.addCell(2, "", 2, CellType.RECTANGLE);
        m.addCell(3, "", 3, CellType.RECTANGLE);

        m.addEdge(1, 2, 1, EdgeType.GRAPH);
        m.addEdge(2, 3, 1, EdgeType.GRAPH);

        m.merge();
        List<Edge> edges = m.getAllEdges();
        assertEquals(2, edges.size());
    }

    /**
     * Tests the getCellMap method.
     */
    @Test
    public void testGetCellMap() {
        Model m = new Model();
        m.addCell(1, "", 1, CellType.RECTANGLE);

        assertEquals(1, m.getCellMap().size());
    }

    /**
     * Tests the set/getAnnotations methods.
     */
    @Test
    public void testAnnotations() {
        Model m = new Model();
        Annotation a = new Annotation();
        a.setSeqid("test");

        m.setAnnotations(Collections.singletonList(a));
        assertEquals("test", m.getAnnotations().get(0).getSeqid());
    }

    /**
     * Tests the addCell method.
     */
    @Test
    public void testAddCell2() {
        Model m = new Model();
        Cell cell = mock(Cell.class);

        assertTrue(m.addCell(cell));
        assertFalse(m.addCell(cell));
    }

    /**
     * Tests the getLevelMaps method.
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
     * Tests the addEdge method.
     */
    @Test
    public void testAddEdge() {
        Model m = new Model();
        m.addCell(1, "", 1, CellType.RECTANGLE);
        m.addCell(2, "", 2, CellType.RECTANGLE);

        m.addEdge(1, 2, 1, EdgeType.GRAPH);

        List<Edge> edges = m.getAddedEdges();
        assertEquals(1, edges.size());
    }

    /**
     * Tests the getEdgeFromChild method.
     */
    @Test
    public void testGetEdgeFromChild() {
        Model m = new Model();
        RectangleCell c1 = spy(new RectangleCell(1, 1));
        RectangleCell c2 = spy(new RectangleCell(2, 2));
        RectangleCell c3 = spy(new RectangleCell(3, 3));

        m.addCell(c1);
        m.addCell(c2);
        m.addCell(c3);

        Edge e1 = new Edge(c1, c2, 1, EdgeType.GRAPH);
        Edge e2 = new Edge(c2, c3, 1, EdgeType.GRAPH);

        m.addEdge(e1);
        m.addEdge(e2);

        m.merge();
        assertNull(m.getEdgeFromChild(c2));
        assertEquals(0, m.getEdgeFromParent(c2).size());
    }

    /**
     * Tests the getMaxWidth method.
     */
    @Test
    public void testGetMaxWidth() {
        GraphLayout gl = mock(GraphLayout.class);
        when(gl.getMaxWidth()).thenReturn(new Double(42));

        Model m = new Model();
        m.setGraphLayout(gl);
        assertEquals(42, m.getMaxWidth(), 0.0001);
    }

    /**
     * Tests the addEdge method.
     */
    @Test
    public void testAddEdge2() {
        Model m = new Model();
        Edge e = mock(Edge.class);

        m.addEdge(e);
        assertEquals(e, m.getAddedEdges().get(0));
    }

    /**
     * Tests the getLeftMost method.
     */
    @Test
    public void testGetRightMost() {
        Cell c = mock(Cell.class);
        GraphLayout gl = mock(GraphLayout.class);
        when(gl.getRightMost()).thenReturn(c);

        Model m = new Model();
        m.setGraphLayout(gl);
        assertEquals(c, m.getRightMost());
    }

    /**
     * Tests the getLeftMost method.
     */
    @Test
    public void testGetLeftMost() {
        Cell c = mock(Cell.class);
        GraphLayout gl = mock(GraphLayout.class);
        when(gl.getLeftMost()).thenReturn(c);

        Model m = new Model();
        m.setGraphLayout(gl);
        assertEquals(c, m.getLeftMost());
    }
}
