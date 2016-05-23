package application.controllers;

import application.fxobjects.cell.Cell;
import application.fxobjects.cell.Edge;
import application.fxobjects.cell.layout.CellLayout;
import application.fxobjects.cell.tree.LeafCell;
import core.graph.PhylogeneticTree;
import application.fxobjects.cell.layout.TreeLayout;
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
    private TreeMouseHandling treeMouseHandling;

    /**
     * Class constructor.
     *
     * @param pt A phylogenetic tree.
     */
    public TreeController(PhylogeneticTree pt, MainController m) {
        super(new ScrollPane());
        this.pt = pt;
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
     * Applies the highlight in the phylogenetic tree on hovering over a leafNode.
     * @param cell the Cell being hovered over.
     */
    public void applyCellHighlight(Cell cell) {
        List<Cell> parentList = new ArrayList<>();
        parentList.add(cell);

        while(!parentList.isEmpty()) {
            Cell next = parentList.remove(0);
            parentList.addAll(next.getCellParents());

            if (next.getCellId() != 0) {
                Edge edge = pt.getModel().getEdgeFromChild(next);
                edge.getLine().setStroke(Color.RED);
                edge.getLine().setStrokeWidth(4.0);
            }
        }
    }

    /**
     * Reverts the highlight in the phylogenetic tree on losing hover over a leafNode.
     * @param cell the Cell which is no longer being hovered over.
     */
    public void revertCellHighlight(Cell cell) {
        List<Cell> childList = new ArrayList<>();
        childList.add(cell);

        while(!childList.isEmpty()) {
            Cell next = childList.remove(0);
            childList.addAll(next.getCellParents());

            if(next.getCellId() != 0) {
                Edge edge = pt.getModel().getEdgeFromChild(next);
                edge.getLine().setStroke(Color.BLACK);
                edge.getLine().setStrokeWidth(1.0);
            }
        }
    }

    /**
     * Applies the highlight in the phylogenetic tree on hovering over an Edge.
     * @param edge the Edge being hovered over.
     */
    public void applyEdgeHighlight(Edge edge) {
        List<Cell> parentList = new ArrayList<>();
        parentList.add(edge.getTarget());
        edge.getLine().setStroke(Color.RED);
        edge.getLine().setStrokeWidth(4.0);


        while(!parentList.isEmpty()) {
            Cell next = parentList.remove(0);
            parentList.addAll(next.getCellChildren());

            if(!(next instanceof LeafCell)) {
                List<Edge> edges = pt.getModel().getEdgeFromParent(next);
                edges.forEach(e -> {
                    e.getLine().setStroke(Color.RED);
                    e.getLine().setStrokeWidth(4.0);
                });
            }
        }
    }

    /**
     * Reverts the highlight in the phylogenetic tree on losing hover over an Edge.
     * @param edge the Edge which is no longer being hovered over.
     */
    public void revertEdgeHighlight(Edge edge) {
        List<Cell> childList = new ArrayList<>();
        childList.add(edge.getTarget());
        edge.getLine().setStroke(Color.BLACK);
        edge.getLine().setStrokeWidth(1.0);

        while(!childList.isEmpty()) {
            Cell next = childList.remove(0);
            childList.addAll(next.getCellChildren());

            if(!(next instanceof LeafCell)) {
                List<Edge> edges = pt.getModel().getEdgeFromParent(next);
                edges.forEach(e -> {
                    e.getLine().setStroke(Color.BLACK);
                    e.getLine().setStrokeWidth(1.0);
                });
            }
        }
    }
}
