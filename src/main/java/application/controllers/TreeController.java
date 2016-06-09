package application.controllers;

import application.fxobjects.cell.*;
import application.fxobjects.cell.layout.*;
import application.fxobjects.cell.tree.LeafCell;
import core.Filter;
import core.MetaData;
import core.graph.PhylogeneticTree;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;

import java.net.URL;
import java.util.*;

import static application.fxobjects.cell.LineageColor.*;
import static core.MetaData.META_DATA;

/**
 * Class responsible for setting up the scroll pane containing the phylogenetic tree.
 */
public class TreeController extends Controller<ScrollPane> {
    private PhylogeneticTree pt;
    private List<Edge> collectedEdges;
    private List<Cell> selectedStrains;
    private List<Cell> collectedStrains;
    private TreeMouseHandling treeMouseHandling;
    private AnchorPane root;

    /**
     * Class constructor.
     *
     * @param m      MainController.
     * @param string The name of the tree.
     */
    public TreeController(MainController m, String string) {
        super(new ScrollPane());
        this.pt = new PhylogeneticTree(string);
        this.selectedStrains = new ArrayList<>();
        this.collectedStrains = new ArrayList<>();
        this.treeMouseHandling = new TreeMouseHandling(m);

        this.getRoot().setVbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);
        init();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
    }

    /**
     * Add cells from the model to the gui.
     */
    public void init() {
        root = new AnchorPane();

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
     * Add a single strain to selection.
     *
     * @param cell the strain to add.
     */
    public void selectStrain(Cell cell) {
        selectedStrains.add(cell);
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
    @SuppressWarnings("checkstyle:linelength")
    public void applyCellHighlight(Cell cell) {
        if (cell instanceof LeafCell) {
            String name = ((LeafCell) cell).getName();
            List<Cell> parentList = new ArrayList<>();
            parentList.add(cell);
            collectedStrains.clear();
            collectedStrains.add(cell);

            if (name.contains("TKK")) {
                applyColorUpwards(parentList, determineEdgeLinColor(META_DATA.get(name).getLineage()), 4.0);
                applyColorOnCell(cell, determineSelectedLeafLinColor(META_DATA.get(name).getLineage()));
            } else if (name.contains("G")) {
                applyColorUpwards(parentList, determineEdgeLinColor(4), 4.0);
                applyColorOnCell(cell, determineSelectedLeafLinColor(4));
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
        if (cell instanceof LeafCell) {
            String name = ((LeafCell) cell).getName();
            List<Cell> parentList = new ArrayList<>();
            parentList.add(cell);
            collectedStrains.clear();
            collectedStrains.add(cell);

            if (name.contains("TKK")) {
                applyColorUpwards(parentList, Color.BLACK, 1.0);
                applyColorOnCell(cell, determineLeafLinColor(META_DATA.get(name).getLineage()));
            } else if (name.contains("G")) {
                applyColorUpwards(parentList, Color.BLACK, 1.0);
                applyColorOnCell(cell, determineLeafLinColor(4));
            } else {
                applyColorUpwards(parentList, Color.BLACK, 1.0);
            }
            applyColorUpwards(parentList, Color.BLACK, 1.0);
        }
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

        applyColorOnCells();
        applyColorOnEdges(determineEdgeLinColor(getCommonLineage()), 4.0);
    }

    /**
     * Reverts the highlight in the phylogenetic tree on losing hover over an Edge.
     *
     * @param edge the Edge which is no longer being hovered over.
     */
    public void revertEdgeHighlight(Edge edge) {
        collectedEdges = new ArrayList<>();
        collectedStrains.clear();
        collectEdges(edge);

        revertColorOnCells();
        applyColorOnEdges(Color.BLACK, 1.0);
    }

    /**
     * Apply a certain color and stroke to all cells being hovered over.
     */
    @SuppressWarnings("checkstyle:linelength")
    private void applyColorOnCells() {
        collectedStrains.forEach(s -> {
                    LeafCell c = (LeafCell) s;
                    if (c.getName().contains("TKK")) {
                        c.setBackground(
                                new Background(
                                        new BackgroundFill(
                                                determineSelectedLeafLinColor(
                                                        META_DATA.get(
                                                                c.getName()).getLineage()
                                                ), null, null
                                        )
                                )
                        );
                    } else if (c.getName().contains("G")) {
                        c.setBackground(
                                new Background(
                                        new BackgroundFill(determineSelectedLeafLinColor(4), null, null)
                                )
                        );
                    }
                }
        );
    }

    /**
     * Revert a certain color and stroke to all cells being hovered over.
     */
    @SuppressWarnings("checkstyle:linelength")
    private void revertColorOnCells() {
        collectedStrains.forEach(s -> {
            LeafCell c = (LeafCell) s;
            if (c.getName().contains("TKK")) {
                c.setBackground(
                        new Background(
                                new BackgroundFill(
                                        determineLeafLinColor(
                                                META_DATA.get(
                                                        c.getName()).getLineage()
                                        ), null, null
                                )
                        )
                );
            } else if (c.getName().contains("G")) {
                c.setBackground(
                        new Background(
                                new BackgroundFill(determineLeafLinColor(4), null, null)
                        )
                );
            }
        });
    }

    /**
     * Apply a certain color and stroke to the cell being hovered over.
     *
     * @param e the given Cell.
     * @param c the given Color.
     */
    private void applyColorOnCell(Cell e, Color c) {
        e.setBackground(new Background(new BackgroundFill(c, null, null)));
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
     * Apply a certain color and stroke to the edges in the list.
     *
     * @param c the given Color.
     * @param s the given stroke.
     */
    private void applyColorOnEdges(Color c, double s) {
        collectedEdges.forEach(e -> {
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
                if (MetaData.META_DATA.containsKey(((LeafCell) c).getName())
                        && MetaData.META_DATA.get(((LeafCell) c).getName()).getLineage() == i) {
                    tempCount++;
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

        return genomes;
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

    /**
     * Method that selects Cells by its name.
     *
     * @param name the name to search for.
     * @return A matching cell.
     */
    public Cell getCellByName(String name) {
        for (Object c : root.getChildren()) {
            if (c instanceof LeafCell && ((LeafCell) c).getName().contains(name)) {
                return (Cell) c;
            }
        }

        return null;
    }

    /**
     * Method to clear all selected genomes.
     */
    public void clearSelection() {
        selectedStrains.forEach(this::revertCellHighlight);
        selectedStrains.clear();
        modifyGraphOptions();

    }

    /**
     * Processes a lineage
     *
     * @param lineage The integer value of a lineage
     * @param state   true/false state
     */
    private void processLineage(int lineage, boolean state) {
        META_DATA.values().forEach(s -> {
            if (s.getLineage() == lineage && state) {
                Cell cell = getCellByName(s.getName());
                selectedStrains.add(cell);
                applyCellHighlight(cell);
            } else if (s.getLineage() == lineage) {
                Cell cell = getCellByName(s.getName());
                selectedStrains.remove(cell);
                revertCellHighlight(cell);
            }
        });
    }

    /**
     * Filters the phylogenetic tree.
     *
     * @param f     A given filter
     * @param state true/false state
     */
    public void filterPhyloLineage(Filter f, boolean state) {
        switch (f) {
            case LIN1:
                processLineage(1, state);
                break;
            case LIN2:
                processLineage(2, state);
                break;
            case LIN3:
                processLineage(3, state);
                break;
            case LIN4:
                processLineage(4, state);
                break;
            case LIN5:
                processLineage(5, state);
                break;
            case LIN6:
                processLineage(6, state);
                break;
            case LIN7:
                processLineage(7, state);
                break;
            case LIN8:
                processLineage(8, state);
                break;
            case LIN9:
                processLineage(9, state);
                break;
            case LIN10:
                processLineage(10, state);
                break;
            default:
                break;
        }
        modifyGraphOptions();
    }

    public ArrayList<String> selectAll() {
        ArrayList<String> toret = new ArrayList<>();

        for (Object c : root.getChildren()) {
            if (c instanceof LeafCell) {
                selectedStrains.add(((LeafCell) c));
                applyCellHighlight((Cell) c);
            }
        }
        modifyGraphOptions();


        return toret;
    }
}
