package application.controllers;

import application.fxobjects.cell.Cell;
import application.fxobjects.cell.Edge;
import application.fxobjects.cell.layout.CellLayout;
import application.fxobjects.cell.layout.TreeLayout;
import application.fxobjects.cell.tree.LeafCell;
import core.MetaParser;
import core.graph.PhylogeneticTree;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;

import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.TreeMap;

/**
 * Class responsible for setting up the scroll pane containing the phylogenetic tree.
 * Created by Niek van der Laan on 5-9-2016
 */
public class TreeController extends Controller<ScrollPane> {
    private PhylogeneticTree pt;
    private List<Edge> collectedEdges;
    private List<Cell> selectedStrains;
    private List<Cell> collectedStrains;
    private MainController mainController;
    private TreeMap<String, Integer> metaData;
    private TreeMouseHandling treeMouseHandling;

    private static final Color LIN0 = Color.web("000000");
    private static final Color LIN1 = Color.web("ed00c3");
    private static final Color LIN2 = Color.web("0000ff");
    private static final Color LIN3 = Color.web("500079");
    private static final Color LIN4 = Color.web("ff0000");
    private static final Color LIN5 = Color.web("4e2c00");
    private static final Color LIN6 = Color.web("69ca00");
    private static final Color LIN7 = Color.web("ff7e00");
    private static final Color LIN8 = Color.web("00ff9c");
    private static final Color LIN9 = Color.web("00ff9c");
    private static final Color LIN10 = Color.web("00ffff");

    /**
     * Class constructor.
     *
     * @param pt A phylogenetic tree.
     * @param m  MainController.
     * @param s  InputStream for metaData.
     */
    public TreeController(PhylogeneticTree pt, MainController m, InputStream s) {
        super(new ScrollPane());
        this.pt = pt;
        this.mainController = m;
        this.metaData = MetaParser.parse(s);
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
            if (selectedStrains.contains(e)) {
                selectedStrains.remove(e);
            } else {
                selectedStrains.add(e);
            }
            modifyGraphOptions();
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
        if (cell instanceof LeafCell) {
            String temp = ((LeafCell) cell).getName();
            collectedStrains.clear();
            List<Cell> parentList = new ArrayList<>();
            parentList.add(cell);
            collectedStrains.add(cell);

            if (temp.contains("TKK")) {
                applyColorUpwards(parentList, determineLinColor(metaData.get(temp)), 4.0);
            } else {
                applyColorUpwards(parentList, Color.YELLOW, 4.0);
            }
        }
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
        collectedEdges = new ArrayList<>();
        collectedStrains.clear();
        collectEdges(edge);

        applyColorOnEdges(collectedEdges, determineLinColor(getCommonLineage()), 4.0);
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
            } else {
                collectedStrains.add(next);
            }
        }
    }

    /**
     * Apply a certain color and stroke to the edges in the list.
     *
     * @param l the given List of Edges.
     * @param c the given Color.
     * @param s the given stroke.
     */
    private void applyColorOnEdges(List<Edge> l, Color c, double s) {
        l.forEach(e -> {
            e.getLine().setStroke(c);
            e.getLine().setStrokeWidth(s);
        });
    }

    /**
     * Collect all edges that will be highlighted for selection
     */
    private void collectEdges(Edge edge) {
        collectedEdges.add(edge);
        collectEdgesUpwards(edge.getSource());
        collectEdgesDownwards(edge.getTarget());
    }

    /**
     * Collect all selection covered edges from a Cell.
     *
     * @param c a Cell.
     */
    private void collectEdgesUpwards(Cell c) {
        List<Cell> parentList = new ArrayList<>();
        parentList.add(c);

        while (!parentList.isEmpty()) {
            Cell next = parentList.remove(0);
            parentList.addAll(next.getCellParents());

            if (next.getCellId() != 0) {
                collectedEdges.add(pt.getModel().getEdgeFromChild(next));
            }
        }
    }

    /**
     * Collect all selection covered edges from a Cell.
     *
     * @param c a Cell.
     */
    private void collectEdgesDownwards(Cell c) {
        List<Cell> childList = new ArrayList<>();
        childList.add(c);

        while (!childList.isEmpty()) {
            Cell next = childList.remove(0);
            childList.addAll(next.getCellChildren());

            if (!(next instanceof LeafCell)) {
                collectedEdges.addAll(pt.getModel().getEdgeFromParent(next));
            } else {
                collectedStrains.add(next);
            }
        }
    }

    /**
     * Get the most common lineage of the collected strains.
     *
     * @return the common lineage identifier.
     */
    private int getCommonLineage() {
        int common = 0;
        int count = 0;
        for (int i = 0; i < 10; i++) {
            int tempCount = 0;
            for (Cell c : collectedStrains) {
                if (metaData.containsKey(((LeafCell) c).getName())) {
                    if (metaData.get(((LeafCell) c).getName()) == i) {
                        tempCount++;
                    }
                }
            }
            if (tempCount > count) {
                count = tempCount;
                common = i;
            }
        }

        return common;
    }

    /**
     * Getter method for the selected strains.
     *
     * @return a list with the selected strains.
     */
    public List<Cell> getSelectedStrains() {
        return selectedStrains;
    }

    /**
     * Getter method for the selected strains.
     *
     * @return a list with the selected strains.
     */
    public List<String> getSelectedGenomes() {
        List<String> genomes = new ArrayList<>();

        selectedStrains.forEach(s -> genomes.add(((LeafCell) s).getName()));
        selectedStrains.clear();
        modifyGraphOptions();

        return genomes;
    }

    /**
     * Determines the color of the edges for the corresponding lineages in a highlighted situation.
     *
     * @param l the lineage code.
     * @return the color.
     */
    private Color determineLinColor(int l) {
        switch (l) {
            case 0:
                return LIN0;
            case 1:
                return LIN1;
            case 2:
                return LIN2;
            case 3:
                return LIN3;
            case 4:
                return LIN4;
            case 5:
                return LIN5;
            case 6:
                return LIN6;
            case 7:
                return LIN7;
            case 8:
                return LIN8;
            case 9:
                return LIN9;
            case 10:
                return LIN10;
            default:
                break;
        }
        return LIN0;
    }

    /**
     * Modifies the option on the MenuBarItem that shows the graph with the selected strain(s).
     */
    private void modifyGraphOptions() {
        int s = selectedStrains.size();
        if (s == 0) {
            MenuFactory.showOnlyThisStrain.setDisable(true);
            MenuFactory.showSelectedStrains.setDisable(true);
        } else if (s == 1) {
            MenuFactory.showOnlyThisStrain.setDisable(false);
            MenuFactory.showSelectedStrains.setDisable(false);
        } else {
            MenuFactory.showOnlyThisStrain.setDisable(true);
            MenuFactory.showSelectedStrains.setDisable(false);
        }
    }
}
