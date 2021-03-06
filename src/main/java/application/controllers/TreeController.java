package application.controllers;

import application.fxobjects.Cell;
import application.fxobjects.Edge;
import application.fxobjects.layout.CellLayout;
import application.fxobjects.layout.TreeLayout;
import application.fxobjects.treeCells.LeafCell;
import application.mouseHandlers.TreeMouseHandling;
import core.genome.Genome;
import core.parsers.MetaDataParser;
import core.phylogeneticTree.PhylogeneticTree;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import static application.fxobjects.LineageColor.*;

/**
 * Class responsible for setting up the scroll pane containing the phylogenetic tree.
 */
@SuppressWarnings("PMD.UselessParentheses")
public class TreeController extends Controller<ScrollPane> {
    private PhylogeneticTree pt;
    private List<Edge> collectedEdges;
    private List<LeafCell> selectedStrains;
    private List<LeafCell> collectedStrains;
    private TreeMouseHandling treeMouseHandling;
    private AnchorPane root;
    private MainController mainController;

    private double maxY = -1;

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
        this.mainController = m;

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
        maxY = ((TreeLayout) layout).getMaxY();

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
        });
        mainController.setListItems();
    }

    /**
     * Add a single strain to selection.
     *
     * @param cell the strain to add.
     */
    public void selectStrain(LeafCell cell) {
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
    public void applyCellHighlight(LeafCell cell) {
        String name = cell.getName();
        if (!mainController.isInGraph()) {
            updateMetaInfo(cell);
        }
        List<Cell> parentList = new ArrayList<>();
        parentList.add(cell);
        collectedStrains.clear();
        collectedStrains.add(cell);


        if (name.contains("TKK") && MetaDataParser.getMetadata().get(name) != null) {
            applyColorUpwards(parentList,
                    determineEdgeLinColor(MetaDataParser.getMetadata().get(name).getLineage()), 4.0);
            applyColorOnCell(cell, determineSelectedLeafLinColor(MetaDataParser.getMetadata().get(name).getLineage()));
          
        } else if (name.contains("G")) {
            applyColorUpwards(parentList, determineEdgeLinColor(4), 4.0);
            applyColorOnCell(cell, determineSelectedLeafLinColor(4));

        } else {
            applyColorUpwards(parentList, Color.YELLOW, 4.0);
        }
    }

    /**
     * Reverts the highlight in the phylogenetic tree on losing hover over a leafNode.
     *
     * @param cell the Cell which is no longer being hovered over.
     */
    public void revertCellHighlight(LeafCell cell) {
        String name = cell.getName();
        List<Cell> parentList = new ArrayList<>();
        parentList.add(cell);
        collectedStrains.clear();
        collectedStrains.add(cell);

        if (name.contains("TKK") && MetaDataParser.getMetadata().get(name) != null) {
            applyColorUpwards(parentList, Color.BLACK, 1.0);
            applyColorOnCell(cell, determineLeafLinColor(MetaDataParser.getMetadata().get(name).getLineage()));
        } else if (name.contains("G")) {
            applyColorUpwards(parentList, Color.BLACK, 1.0);
            applyColorOnCell(cell, determineLeafLinColor(4));
        } else {
            applyColorUpwards(parentList, Color.BLACK, 1.0);
        }
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
    private void applyColorOnCells() {
        collectedStrains.forEach(s -> {
                    LeafCell c = s;
                    if (c.getName().contains("TKK") && MetaDataParser.getMetadata().get(c.getName()) != null) {
                        c.setBackground(
                                new Background(
                                        new BackgroundFill(
                                                determineSelectedLeafLinColor(
                                                        MetaDataParser.getMetadata().get(
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
    private void revertColorOnCells() {
        collectedStrains.forEach(s -> {
            LeafCell c = s;
            if (c.getName().contains("TKK") && MetaDataParser.getMetadata().get(c.getName()) != null) {
                c.setBackground(
                        new Background(
                                new BackgroundFill(
                                        determineLeafLinColor(
                                                MetaDataParser.getMetadata().get(
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
                collectedStrains.add((LeafCell) next);
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
                if (MetaDataParser.getMetadata().containsKey(((LeafCell) c).getName())
                        && MetaDataParser.getMetadata().get(((LeafCell) c).getName()).getLineage() == i) {
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
    public List<LeafCell> getSelectedStrains() {
        return selectedStrains;
    }

    /**
     * Getter method for the selected strains.
     *
     * @return a list with the selected strains.
     */
    public List<String> getSelectedGenomes() {
        List<String> genomes = new ArrayList<>();

        selectedStrains.forEach(s -> genomes.add(s.getName()));

        return genomes;
    }

    /**
     * Method that selects Cells by its name.
     *
     * @param name the name to search for.
     * @return a Cell gotten by its name.
     */
    public LeafCell getCellByName(String name) {
        for (Object c : root.getChildren()) {
            if (c instanceof LeafCell && ((LeafCell) c).getName().contains(name)) {
                return (LeafCell) c;
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

    }

    /**
     * Method to select all genomes in the tree.
     */
    public void selectAll() {
        root.getChildren().stream().filter(c -> c instanceof LeafCell).forEach(c -> {
            if (!selectedStrains.contains(c) && ((LeafCell) c).getName().contains("TKK")) {
                selectedStrains.add(((LeafCell) c));
                applyCellHighlight((LeafCell) c);
            }
        });
    }

    /**
     * Put all info of a TreeCell in the infoBox
     *
     * @param cell the cell of which we want info
     */
    private void updateMetaInfo(LeafCell cell) {
        if (cell.getName().contains("TKK")) {
            StringBuilder builder = new StringBuilder();
            Genome genome = MetaDataParser.getMetadata().get(cell.getName());

            mainController.appendFilterNames(builder);
            builder.append(genome.getName()).append("\n");
            builder.append("Lineage: ").append(genome.getLineage()).append("\n");
            builder.append("Age: ").append(genome.getAge()).append("\n");
            builder.append("Sex: ").append(genome.getSex()).append("\n");
            builder.append("HIV: ").append(genome.isHiv()).append("\n");
            builder.append("Cohort: ").append(genome.getCohort()).append("\n");
            builder.append("Study district: ").append(genome.getStudyDistrict()).append("\n");
            builder.append("Specimen type: ").append(genome.getSpecimenType()).append("\n");
            builder.append("Microscopic smear: ").append(genome.getSmearStatus()).append("\n");
            builder.append("DNA Isolation: ").append(genome.getIsolation()).append("\n");
            builder.append("Phenotypic DST: ").append(genome.getPhenoDST()).append("\n");
            builder.append("Capreomycin: ").append(genome.getCapreomycin()).append("\n");
            builder.append("Ethambutol: ").append(genome.getEthambutol()).append("\n");
            builder.append("Ethionamide: ").append(genome.getEthionamide()).append("\n");
            builder.append("Isoniazid: ").append(genome.getIsoniazid()).append("\n");
            builder.append("Kanamycin: ").append(genome.getKanamycin()).append("\n");
            builder.append("Pyrazinamide: ").append(genome.getPyrazinamide()).append("\n");
            builder.append("Ofloxacin: ").append(genome.getOfloxacin()).append("\n");
            builder.append("Rifampin: ").append(genome.getRifampin()).append("\n");
            builder.append("Streptomycin: ").append(genome.getStreptomycin()).append("\n");
            builder.append("Spoligotype: ").append(genome.getSpoligotype()).append("\n");
            builder.append("Genotypic DST: ").append(genome.getGenoDST()).append("\n")
                    .append("Tugela Ferry: ").append(genome.isTf()).append("\n");

            mainController.getListFactory().modifyNodeInfo(builder.toString());
        }
    }

    /**
     * Add a strain to the selected strains list.
     *
     * @param cell the Cell to add.
     */
    public void addSelectedStrain(LeafCell cell) {
        selectedStrains.add(cell);
    }

    /**
     * Clear the list of selected strains.
     */
    public void clearSelectedStrains() {
        selectedStrains.clear();
    }

    /**
     * Getter method for the maximum Y value.
     *
     * @return the max Y value.
     */
    public double getMaxY() {
        return maxY;
    }
}
