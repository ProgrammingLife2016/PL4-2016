package application.controllers;

import application.fxobjects.cell.Cell;
import application.fxobjects.cell.Edge;
import application.fxobjects.cell.layout.CellLayout;
import application.fxobjects.cell.layout.TreeLayout;
import application.fxobjects.cell.tree.LeafCell;
import core.graph.PhylogeneticTree;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

/**
 * Class responsible for setting up the scroll pane containing the phylogenetic tree.
 * Created by Niek van der Laan on 5-9-2016
 */
public class TreeController extends Controller<ScrollPane> {
    private PhylogeneticTree pt;
    private List<Cell> selectedStrains;
    private List<Cell> collectedStrains;
    private TreeMouseHandling treeMouseHandling;

    /**
     * Class constructor.
     *
     * @param pt A phylogenetic tree.
     */
    public TreeController(PhylogeneticTree pt, MainController m) {
        super(new ScrollPane());
        this.pt = pt;
        this.selectedStrains = new ArrayList<>();
        this.collectedStrains = new ArrayList<>();
        this.treeMouseHandling = new TreeMouseHandling(m);
        this.getRoot().setHbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);
        this.getRoot().setVbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);

        this.getRoot().addEventFilter(ScrollEvent.SCROLL, event -> {
            if (event.getDeltaY() != 0) {
                event.consume();
            }
        });

        init();
    }

    /**
     * Get the phylogenetic tree.
     *
     * @return The phylogenetic tree.
     */
    public PhylogeneticTree getPT() {
        return pt;
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    /**
     * Add cells from the model to the gui.
     */
    public void init() {
        AnchorPane root = new AnchorPane();
        CellLayout layout = new TreeLayout(pt.getModel(), 30);
        layout.execute();

        List<Cell> nodeList = pt.getModel().getAddedCells();
        List<Edge> edgeList = pt.getModel().getAddedEdges();

        nodeList.forEach(treeMouseHandling::setMouseHandling);
        edgeList.forEach(treeMouseHandling::setMouseHandling);

        // Add all cells and edges to the anchor pane
        root.getChildren().addAll(pt.getModel().getAddedCells());
        root.getChildren().addAll(pt.getModel().getAddedEdges());
        this.getRoot().setContent(root);
    }

    /**
     * Selects strains to keep them highlighted.
     */
    public void selectStrains() {
        collectedStrains.forEach(e -> {
            if (selectedStrains.contains(e)) selectedStrains.remove(e);
            else selectedStrains.add(e);
        });
    }

    /**
     * Colors the selected strains after un-hover.
     */
    public void colorSelectedStrains() {
        selectedStrains.forEach(this::applyCellHighlight);
    }

    /**
     * Applies the highlight in the phylogenetic tree on hovering over a leafNode.
     *
     * @param cell the Cell being hovered over.
     */
    public void applyCellHighlight(Cell cell) {
        List<Cell> parentList = new ArrayList<>();
        parentList.add(cell);
        collectedStrains.clear();
        collectedStrains.add(cell);

        applyColorUpwards(parentList, Color.RED, 4.0);
    }

    /**
     * Reverts the highlight in the phylogenetic tree on losing hover over a leafNode.
     *
     * @param cell the Cell which is no longer being hovered over.
     */
    public void revertCellHighlight(Cell cell) {
        List<Cell> parentList = new ArrayList<>();
        parentList.add(cell);
        collectedStrains.clear();
        collectedStrains.add(cell);

        applyColorUpwards(parentList, Color.BLACK, 1.0);
    }

    /**
     * Applies the highlight in the phylogenetic tree on hovering over an Edge.
     *
     * @param edge the Edge being hovered over.
     */
    public void applyEdgeHighlight(Edge edge) {
        List<Cell> parentList = new ArrayList<>();
        List<Cell> childList = new ArrayList<>();
        parentList.add(edge.getSource());
        childList.add(edge.getTarget());
        collectedStrains.clear();

        applyColorOnSelf(edge, Color.RED, 4.0);
        applyColorUpwards(parentList, Color.RED, 4.0);
        applyColorDownwards(childList, Color.RED, 4.0);
    }

    /**
     * Reverts the highlight in the phylogenetic tree on losing hover over an Edge.
     *
     * @param edge the Edge which is no longer being hovered over.
     */
    public void revertEdgeHighlight(Edge edge) {
        List<Cell> parentList = new ArrayList<>();
        List<Cell> childList = new ArrayList<>();
        parentList.add(edge.getSource());
        childList.add(edge.getTarget());
        collectedStrains.clear();

        applyColorOnSelf(edge, Color.BLACK, 1.0);
        applyColorUpwards(parentList, Color.BLACK, 1.0);
        applyColorDownwards(childList, Color.BLACK, 1.0);
    }

    /**
     * Apply a certain color and stroke to the edge being hovered over.
     *
     * @param e the given Edge.
     * @param c the given Color.
     * @param s the given stroke.
     */
    private void applyColorOnSelf(Edge e, Color c, double s) {
        e.getLine().setStroke(c);
        e.getLine().setStrokeWidth(s);
    }

    /**
     * Apply a certain color and stroke to the edges upwards from the node in the list.
     *
     * @param l the given List of Edges.
     * @param c the given Color.
     * @param s the given stroke.
     */
    private void applyColorUpwards(List<Cell> l, Color c, double s) {
        while (!l.isEmpty()) {
            Cell next = l.remove(0);
            l.addAll(next.getCellParents());

            if (next.getCellId() != 0) {
                Edge e = pt.getModel().getEdgeFromChild(next);
                e.getLine().setStroke(c);
                e.getLine().setStrokeWidth(s);
            }
        }
    }

    /**
     * Apply a certain color and stroke to the edges downwards from the node in the list.
     *
     * @param l the given List of Edges.
     * @param c the given Color.
     * @param s the given stroke.
     */
    private void applyColorDownwards(List<Cell> l, Color c, double s) {
        while (!l.isEmpty()) {
            Cell next = l.remove(0);
            l.addAll(next.getCellChildren());

            if (!(next instanceof LeafCell)) {
                List<Edge> edges = pt.getModel().getEdgeFromParent(next);
                edges.forEach(e -> {
                    e.getLine().setStroke(c);
                    e.getLine().setStrokeWidth(s);
                });
            }
            else collectedStrains.add(next);
        }
    }
}
