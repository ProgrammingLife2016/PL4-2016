package core.graph;

import core.*;
import core.graph.cell.CellType;

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

    private Model model;

    private List<String> genomes = new ArrayList<>();


    /**
     * Class constructor.
     */
    public Graph() {
        this.model = new Model();
    }

    /**
     * Read a node map from a gfa file on disk.
     * @return  A node map read from file.
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
     * @param ref the reference string.
     * @param depth the depth to draw.
     * @return Boolean used for testing purposes.
     * @throws IOException Throw exception on read GFA read failure.
     */
    @SuppressFBWarnings("OBL_UNSATISFIED_OBLIGATION_EXCEPTION_EDGE")
    public Boolean addGraphComponents(Object ref, int depth)
            throws IOException {
        Parser parser = new Parser();
        InputStream inputStream = getClass().getResourceAsStream("/TB10.gfa");
        HashMap<Integer, Node> startMap = parser.readGFA(inputStream);
        inputStream.close();

        List<HashMap<Integer, Node>> levelMaps = GraphReducer.createLevelMaps(startMap);

        //Reset the model, since we have another reference.
        model = new Model();
        model.setLevelMaps(levelMaps);

        if (depth > levelMaps.size() - 1) {
            depth = levelMaps.size() - 1;
        } else if (depth < 0) {
            depth = 0;
        }
        HashMap<Integer, Node> nodeMap = levelMaps.get(depth);
        System.out.println("Loading map: " + depth);

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
            if (from == null) {
                continue;
            }

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

        return true;
    }

    /**
     * Method that updates the model.
     */

    public void endUpdate() {
        // every cell must have a parent, if it doesn't, then the graphParent is the parent.
        model.attachOrphansToGraphParent(model.getAddedCells());

        // merge added & removed cells with all cells
        model.merge();
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
        return model;
    }

    /**
     * Set the model of the Graph.
     *
     * @param model The model of the graph.
     */
    public void setModel(Model model) {
        this.model = model;
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
     * @param genomes the genomes.
     */
    public void setGenomes(List<String> genomes) {
        this.genomes = genomes;
    }
}