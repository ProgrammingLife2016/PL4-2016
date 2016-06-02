package application.controllers;

import application.fxobjects.cell.Cell;
import application.fxobjects.cell.Edge;
import application.fxobjects.cell.LineageColor;
import application.fxobjects.cell.layout.CellLayout;
import application.fxobjects.cell.layout.TreeLayout;
import application.fxobjects.cell.tree.LeafCell;
import core.MetaData;
import core.graph.PhylogeneticTree;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.paint.Color;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

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
     * @param m MainController.
     */
    public TreeController(MainController m) {
        super(new ScrollPane());
        this.pt = new PhylogeneticTree();
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
                applyColorUpwards(parentList, LIN4, 4.0);
                applyColorOnCell(cell, SLIN4);
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
                applyColorOnCell(cell, GLIN4);
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
                                                                (c.getName())).getLineage()
                                                ), null, null
                                        )
                                )
                        );
                    } else if (c.getName().contains("G")) {
                        c.setBackground(
                                new Background(
                                        new BackgroundFill(LineageColor.SLIN4, null, null
                                        )
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
                                                        (c.getName())).getLineage()
                                        ), null, null
                                )
                        )
                );
            } else if (c.getName().contains("G")) {
                c.setBackground(
                        new Background(
                                new BackgroundFill(LineageColor.GLIN4, null, null
                                )
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
        selectedStrains.clear();
        modifyGraphOptions();

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

    public void getCellByName(String name) {
        double max = 0;
        double cLoc = 0;
        for (Object c : root.getChildren()
                ) {
            if (c instanceof LeafCell) {
                if (((LeafCell) c).getName().contains(name)) {
                    selectedStrains.add((LeafCell) c);
                    System.out.println("Found it");
                    cLoc = ((LeafCell) c).getLayoutY();
                }
                if (((LeafCell) c).getLayoutX() > max) {
                    max = ((LeafCell) c).getLayoutY();
                }
            }
        }
        colorSelectedStrains();
        modifyGraphOptions();
        getRoot().setVvalue(cLoc/max);
    }

    public void clearSelection() {
        selectedStrains.forEach(this::revertCellHighlight);
        selectedStrains.clear();
        modifyGraphOptions();

    }
}
