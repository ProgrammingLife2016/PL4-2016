package core;

import application.fxobjects.cell.Cell;
import application.fxobjects.cell.Edge;
import application.fxobjects.cell.graph.BubbleCell;
import application.fxobjects.cell.graph.CollectionCell;
import application.fxobjects.cell.graph.IndelCell;
import application.fxobjects.cell.layout.GraphLayout;
import application.fxobjects.cell.tree.LeafCell;
import application.fxobjects.cell.graph.RectangleCell;

import application.fxobjects.cell.tree.MiddleCell;
import core.graph.cell.CellType;
import core.graph.cell.EdgeType;
import javafx.geometry.Rectangle2D;
import javafx.stage.Screen;
import net.sourceforge.olduvai.treejuxtaposer.drawer.Tree;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Model containing all nodes and edges.
 */
public class Model {

    private Cell graphParent;

    private List<Cell> allCells;
    private List<Cell> addedCells;

    private List<Edge> allEdges;
    private List<Edge> addedEdges;

    private Map<Integer, Cell> cellMap; // <id,cell>

    private List<HashMap<Integer, Node>> levelMaps;

    private List<Annotation> annotations;
    private Tree tree;
    private GraphLayout graphLayout;

    private Rectangle2D screenSize;

    /**
     * Class constructor.
     */
    public Model() {
        graphParent = new RectangleCell(1);
        graphLayout = new GraphLayout(null, 0, 0);

        // clear model, create lists
        clear();
    }

    /**
     * Remove all cells and edges.
     */
    public void clear() {
        allCells = new ArrayList<>();
        addedCells = new ArrayList<>();

        allEdges = new ArrayList<>();
        addedEdges = new ArrayList<>();

        cellMap = new HashMap<>();

        levelMaps = new ArrayList<>();

        annotations = new ArrayList<>();

        tree = new Tree();
    }

    /**
     * Remove all added cells and edges.
     */
    public void clearAddedLists() {
        addedCells.clear();
        addedEdges.clear();
    }

    /**
     * Method to get the layout of the graph.
     *
     * @return the layout
     */
    public GraphLayout getGraphLayout() {
        return graphLayout;
    }

    /**
     * Get the phylogenetic tree.
     *
     * @return The phylogenetic tree.
     */
    public Tree getTree() {
        return tree;
    }

    /**
     * Retrieves the size of the levelMaps list.
     *
     * @return the size of the levelMaps list.
     */
    public int getLevelMapsSize() {
        return levelMaps.size();
    }

    /**
     * Set the phylogenetic tree.
     *
     * @param tree The phylogenetic tree.
     */
    public void setTree(Tree tree) {
        this.tree = tree;
    }

    /**
     * Get a list of added cells.
     *
     * @return A list of added cells.
     */
    public List<Cell> getAddedCells() {
        return addedCells;
    }

    /**
     * Get a list of all cells.
     *
     * @return A list of all cells.
     */
    public List<Cell> getAllCells() {
        return allCells;
    }

    /**
     * Get a list of added edges.
     *
     * @return A list of all added edges.
     */
    public List<Edge> getAddedEdges() {
        return addedEdges;
    }

    /**
     * Get a list of all edges.
     *
     * @return A list of all edges.
     */
    public List<Edge> getAllEdges() {
        return allEdges;
    }

    /**
     * Get a list of annotations.
     *
     * @return A list of annotations.
     */
    public List<Annotation> getAnnotations() {
        return annotations;
    }

    /**
     * Set a list of annotations.
     *
     * @param annotations A list of annotations.
     */
    public void setAnnotations(List<Annotation> annotations) {
        this.annotations = annotations;
    }

    /**
     * Method to add a Cell (Node).
     *
     * @param id   the id, which represents the sequence.
     * @param text The text of a cell.
     * @param type The type of cell.
     * @return True for testing purposes.
     */
    public Boolean addCell(int id, String text, CellType type) {
        switch (type) {
            case RECTANGLE:
                RectangleCell rectangleCell = new RectangleCell(id);
                addCell(rectangleCell);

                break;
            case BUBBLE:
                BubbleCell bubbleCell = new BubbleCell(id, text);
                addCell(bubbleCell);
                break;
            case INDEL:
                IndelCell indelCell = new IndelCell(id, text);
                addCell(indelCell);
                break;
            case COLLECTION:
                CollectionCell collectionCell = new CollectionCell(id, text);
                addCell(collectionCell);
                break;
            case TREELEAF:
                LeafCell leafCell = new LeafCell(id, text);
                addCell(leafCell);
                break;
            case TREEMIDDLE:
                MiddleCell middleCell = new MiddleCell(id);
                addCell(middleCell);
                break;
            default:
                throw new UnsupportedOperationException("Unsupported type: " + type);
        }

        return false;
    }

    /**
     * Method to add a Cell (Node).
     *
     * @param cell The cell (Node) to add.
     * @return True for testing purposes.
     */
    public Boolean addCell(Cell cell) {
        if (!cellMap.containsKey(cell.getCellId())) {
            addedCells.add(cell);
            cellMap.put(cell.getCellId(), cell);

            return true;
        }

        return false;
    }


    /**
     * Return a list of level maps.
     *
     * @return A list of level maps.
     */
    public List<HashMap<Integer, Node>> getLevelMaps() {
        return this.levelMaps;
    }

    /**
     * Set a list of level maps.
     *
     * @param levelMaps A list of level maps.
     */
    public void setLevelMaps(List<HashMap<Integer, Node>> levelMaps) {
        this.levelMaps = levelMaps;
    }

    /**
     * Method to add an Edge to the model.
     *
     * @param sourceId From.
     * @param targetId To.
     * @param width    The width of the edge.
     * @param type     The type of edge.
     * @return True for testing purposes.
     */
    public Boolean addEdge(int sourceId, int targetId, int width, EdgeType type) {
        Cell sourceCell = cellMap.get(sourceId);
        Cell targetCell = cellMap.get(targetId);

        if (sourceCell != null && targetCell != null) {
            Edge edge = new Edge(sourceCell, targetCell, width, type);
            addedEdges.add(edge);
        }

        return true;
    }

    /**
     * Attach all cells which don't have a parent to graphParent.
     *
     * @param cellList List of cells without a parent.
     */
    public void attachOrphansToGraphParent(List<Cell> cellList) {

        for (Cell cell : cellList) {
            if (cell.getCellParents().size() == 0) {
                graphParent.addCellChild(cell);
            }
        }

    }

    /**
     * Remove the graphParent reference if it is set
     *
     * @param cellList List of cells to be removed from the graph
     */
    public void disconnectFromGraphParent(List<Cell> cellList) {

        for (Cell cell : cellList) {
            graphParent.removeCellChild(cell);
        }
    }

    /**
     * Add and remove cells from the allCells list.
     */
    public void merge() {
        // cells
        allCells.addAll(addedCells);
        allCells.sort((o1, o2) -> {
            if (o1.getCellId() > o2.getCellId()) {
                return 1;
            }
            return -1;
        });
        addedCells.clear();

        // edges
        allEdges.addAll(addedEdges);
        addedEdges.clear();
    }

    /**
     * Set the screen properties.
     */
    public void setLayout() {
        this.screenSize = Screen.getPrimary().getVisualBounds();
        this.graphLayout = new GraphLayout(this, (int) ((screenSize.getWidth() - 288) / 40),
                (int) (screenSize.getHeight() - 150) / 2);

        graphLayout.execute();
    }

    /**
     * Get the incoming edge from a given child.
     *
     * @param c the child node.
     * @return the incoming edge.
     */
    public Edge getEdgeFromChild(Cell c) {
        return addedEdges.stream().filter(e ->
                e.getTarget().equals(c)).findFirst().orElse(null);
    }

    /**
     * Get the outgoing edge from a given Parent.
     *
     * @param p the parent node.
     * @return the incoming edge.
     */
    public List<Edge> getEdgeFromParent(Cell p) {
        return addedEdges.stream().filter(e ->
                e.getSource().equals(p)).collect(Collectors.toList());
    }

    /**
     * Getter for the maxWidth
     *
     * @return the maxWidth of the model.
     */
    public double getMaxWidth() {
        return graphLayout.getMaxWidth();
    }
}