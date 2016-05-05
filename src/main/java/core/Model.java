package core;

import application.fxobjects.graph.cell.Cell;
import core.graph.cell.CellType;
import application.fxobjects.graph.cell.Edge;
import application.fxobjects.graph.cell.RectangleCell;
import application.fxobjects.graph.cell.TriangleCell;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Model containing all nodes and edges.
 */
public class Model {

    Cell graphParent;

    List<Cell> allCells;
    List<Cell> addedCells;
    List<Cell> removedCells;

    List<Edge> allEdges;
    List<Edge> addedEdges;
    List<Edge> removedEdges;

    double size;
    Map<Integer, Cell> cellMap; // <id,cell>

    /**
     * Class constructor.
     */
    public Model() {
        graphParent = new RectangleCell(1, "");

       // clear model, create lists
        clear();
    }

    /**
     * Remove all cells and edges.
     */
    public void clear() {

        allCells = new ArrayList<>();
        addedCells = new ArrayList<>();
        removedCells = new ArrayList<>();

        allEdges = new ArrayList<>();
        addedEdges = new ArrayList<>();
        removedEdges = new ArrayList<>();

        cellMap = new HashMap<>(); // <id,cell>

    }

    /**
     * Remove all added cells and edges.
     */
    public void clearAddedLists() {
        addedCells.clear();
        addedEdges.clear();
    }

    /**
     * Get a list of added cells.
     * @return A list of added cells.
     */
    public List<Cell> getAddedCells() {
        return addedCells;
    }

    /**
     * Get a list of removed cells.
     * @return A list of removed cells.
     */
    public List<Cell> getRemovedCells() {
        return removedCells;
    }

    /**
     * Get a list of all cells.
     * @return A list of all cells.
     */
    public List<Cell> getAllCells() {
        return allCells;
    }

    /**
     * Get a list of added edges.
     * @return A list of all added edges.
     */
    public List<Edge> getAddedEdges() {
        return addedEdges;
    }

    /**
     * Get a list of removed edges.
     * @return A list of removed edges.
     */
    public List<Edge> getRemovedEdges() {
        return removedEdges;
    }

    /**
     * Get a list of all edges.
     * @return A list of all edges.
     */
    public List<Edge> getAllEdges() {
        return allEdges;
    }

    /**
     * Method to add a Cell (Node).
     * @param id the id, which represents the sequence.
     * @param seq The genome sequence of a cell.
     * @param type The type of cell.
     */
    public void addCell(int id, String seq, CellType type) {

        switch (type) {

            case RECTANGLE:
                RectangleCell rectangleCell = new RectangleCell(id, seq);
                addCell(rectangleCell);
                break;

            case TRIANGLE:
                TriangleCell circleCell = new TriangleCell(id, seq);
                addCell(circleCell);
                break;

            default:
                throw new UnsupportedOperationException("Unsupported type: " + type);
        }
    }

    /**
     * Method to add a Cell (Node).
     * @param cell The cell (Node) to add.
     */
    private void addCell(Cell cell) {

        if (!cellMap.containsKey(cell.getCellId())) {
            addedCells.add(cell);

            cellMap.put(cell.getCellId(), cell);
        }
    }

    /**
     * Method to add an Edge to the model.
     * @param sourceId From.
     * @param targetId To.
     */
    public void addEdge(int sourceId, int targetId) {

        Cell sourceCell = cellMap.get(sourceId);
        Cell targetCell = cellMap.get(targetId);

        Edge edge = new Edge(sourceCell, targetCell);

        addedEdges.add(edge);

    }

    /**
     * Attach all cells which don't have a parent to graphParent.
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
        allCells.removeAll(removedCells);

        addedCells.clear();
        removedCells.clear();

        // edges
        allEdges.addAll(addedEdges);
        allEdges.removeAll(removedEdges);

        addedEdges.clear();
        removedEdges.clear();

    }
}