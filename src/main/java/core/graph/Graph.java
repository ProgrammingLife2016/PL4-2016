package core.graph;

import core.GraphReducer;
import core.Model;
import core.graph.cell.CellType;
import core.Node;
import core.Parser;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

import java.io.IOException;
import java.io.InputStream;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Class representing a graph.
 */
@SuppressWarnings({"PMD.UnusedPrivateField", "PMD.UnusedLocalVariable"})
public class Graph {

    private Model model;

    private List<String> genomes = new ArrayList<>();

    /**
     * Class constructor.
     */
    public Graph() {
        this.model = new Model();
    }

    /**
     * Get the model of the Graph.
     *
     * @return The model of the graph.
     */
    public Model getModel() {
        return model;
    }

    /**
     * Read a node map from a gfa file on disk.
     * @return  A node map read from file.
     * @throws IOException Throw exception on read GFA read failure.
     */
    public HashMap<Integer, Node> getNodeMapFromFile() throws IOException {
        Parser parser = new Parser();
        InputStream inputStream = getClass().getResourceAsStream("/TB10.gfa");
        HashMap<Integer, Node> startMap = parser.readGFA(inputStream);
        inputStream.close();

        return startMap;
    }

    /**
     * Add the nodes and edges of the graph to the model.
     *
     * @param ref the reference string.
     * @throws IOException Throw exception on read GFA read failure.
     */
    @SuppressFBWarnings("OBL_UNSATISFIED_OBLIGATION_EXCEPTION_EDGE")
    public void addGraphComponents(Object ref) throws IOException {
        List<HashMap<Integer, Node>> levelMaps = GraphReducer.createLevelMaps(getNodeMapFromFile());
        model.setLevelMaps(levelMaps);

        //Reset the model, since we have another reference.
        model = new Model();
        HashMap<Integer, Node> nodeMap = levelMaps.get(0);

        Node root = nodeMap.get(1);

        if (root.getGenomes().contains(ref)) {
            model.addCell(root.getId(), root.getSequence(), CellType.RECTANGLE);
        } else {
            model.addCell(root.getId(), root.getSequence(), CellType.TRIANGLE);
        }

        genomes.addAll(root.getGenomes());

        if (ref == null) {
            ref = root.getGenomes().get(0);
        }

        for (int i = 1; i <= nodeMap.size(); i++) {
            Node from = nodeMap.get(i);
            if (from == null) { continue; }

            for (int j : from.getLinks(nodeMap)) {
                Node to = nodeMap.get(j);
                to.getGenomes().stream().filter(s -> !genomes.contains(s)).forEach(genomes::add);
                //Add next cell
                if (nodeMap.get(j).getGenomes().contains(ref)) {
                    model.addCell(to.getId(), to.getSequence(), CellType.RECTANGLE);
                } else {
                    model.addCell(to.getId(), to.getSequence(), CellType.TRIANGLE);
                }

                //Add link from current cell to next cell
                model.addEdge(from.getId(), to.getId(), intersection(from.getGenomes(),
                        to.getGenomes()));
            }
        }
    }

    /**
     * Method that updates the model.
     */
    public void endUpdate() {
        // every cell must have a parent, if it doesn't, then the graphParent is
        // the parent
        getModel().attachOrphansToGraphParent(model.getAddedCells());

        // merge added & removed cells with all cells
        getModel().merge();
    }

    /**
     * Method that returns the amount of element that exist in both lists.
     *
     * @param l1 The first list.
     * @param l2 The second list.
     * @return Number of element that exist in both lists.
     */
    public int intersection(List<String> l1, List<String> l2) {
        int i = 0;
        for (String s : l1) {
            if (l2.contains(s)) {
                i++;
            }
        }
        return i;
    }

    /**
     * Getter method for the genomens.
     *
     * @return the genomes.
     */
    public List<String> getGenomes() {
        return genomes;
    }

    /**
     * Setter method for the genomens.
     *
     * @return the genomes.
     */
    public void setGenomes(List<String> genomes) {
        this.genomes = genomes;
    }
}