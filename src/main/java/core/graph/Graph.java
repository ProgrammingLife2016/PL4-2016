package core.graph;

import core.*;
import core.graph.cell.CellType;

import core.graph.cell.EdgeType;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

import java.io.IOException;
import java.io.InputStream;

import java.util.*;

/**
 * Class representing a graph.
 */
@SuppressWarnings({"PMD.UnusedPrivateField", "PMD.UnusedLocalVariable"})
public class Graph {

    private Boolean resetModel = true;

    private Model zoomIn;
    private Model current;
    private Model zoomOut;

    private List<String> genomes = new ArrayList<>();

    private HashMap<Integer, Node> startMap;

    private List<HashMap<Integer, Node>> levelMaps;


    /**
     * Class constructor.
     */
    public Graph() throws IOException {
        zoomIn = new Model();
        current = new Model();
        zoomOut = new Model();

        Parser parser = new Parser();
        InputStream inputStream = getClass().getResourceAsStream("/TB10.gfa");
        try {
            startMap = parser.readGFA(inputStream);
            levelMaps = GraphReducer.createLevelMaps(startMap);
            inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Read a node map from a gfa file on disk.
     *
     * @return A node map read from file.
     * @throws IOException Throw exception on read GFA read failure.
     */
    @SuppressFBWarnings("OBL_UNSATISFIED_OBLIGATION_EXCEPTION_EDGE")
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
     * @param ref   the reference string.
     * @param depth the depth to draw.
     * @return Boolean used for testing purposes.
     * @throws IOException Throw exception on read GFA read failure.
     */
    @SuppressFBWarnings("OBL_UNSATISFIED_OBLIGATION_EXCEPTION_EDGE")
    public Boolean addGraphComponents(Object ref, int depth)
            throws IOException {
        //Reset the model and re'add the levelMaps, since we have another reference.
        current = new Model();
        current.setLevelMaps(levelMaps);
        //Generate the model

        if (depth > levelMaps.size() - 1) {
            depth = levelMaps.size() - 1;
        } else if (depth < 0) {
            depth = 0;
        }
        current = generateModel(ref, depth);

        return true;
    }

    private Model generateModel(Object ref, int depth) {
        HashMap<Integer, Node> nodeMap = levelMaps.get(depth);
        Node root = nodeMap.get(1);

        if (ref == null) {
            ref = root.getGenomes().get(0);
        }

        if (root.getGenomes().contains(ref)) {
            current.addCell(root.getId(), root.getSequence(), CellType.RECTANGLE);
        } else {
            current.addCell(root.getId(), root.getSequence(), CellType.TRIANGLE);
        }

        genomes.addAll(root.getGenomes());

        for (int i = 1; i <= nodeMap.size(); i++) {
            Node from = nodeMap.get(i);
            if (from == null) {
                continue;
            }

            for (int j : from.getLinks(nodeMap)) {
                Node to = nodeMap.get(j);
                to.getGenomes().stream().filter(s -> !genomes.contains(s)).forEach(genomes::add);
                //Add next cell
                if (nodeMap.get(j).getGenomes().contains(ref)) {
                    current.addCell(to.getId(), to.getSequence(), CellType.RECTANGLE);
                } else {
                    current.addCell(to.getId(), to.getSequence(), CellType.TRIANGLE);
                }

                //Add link from current cell to next cell
                current.addEdge(from.getId(), to.getId(), intersection(from.getGenomes(),
                        to.getGenomes()), EdgeType.GRAPH);
            }
        }

        current.setLayout();
        return current;
    }

    /**
     * Method that updates the model.
     */

    public void endUpdate() {
        // every cell must have a parent, if it doesn't, then the graphParent is the parent.
        current.attachOrphansToGraphParent(current.getAddedCells());

        // merge added & removed cells with all cells
        current.merge();
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
     * Set whether the model should be reset in the addGraphComponents method.
     * This option is only used for testing purposes to allow for mocks.
     *
     * @param resetModel whether the model should be reset in the addGraphComponents method.
     */
    public void setresetModel(Boolean resetModel) {
        this.resetModel = resetModel;
    }

    /**
     * Get the model of the Graph.
     *
     * @return The model of the graph.
     */
    public Model getModel() {
        return current;
    }

    /**
     * Set the model of the Graph.
     *
     * @param model The model of the graph.
     */
    public void setModel(Model model) {
        this.current = model;
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
     * Setter method for the genomes.
     *
     * @param genomes the genomes.
     */
    public void setGenomes(List<String> genomes) {
        this.genomes = genomes;
    }
}