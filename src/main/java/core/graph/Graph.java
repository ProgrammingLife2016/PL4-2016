package core.graph;

import core.*;
import core.graph.cell.CellType;
import core.graph.cell.EdgeType;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Class representing a graph.
 */
@SuppressWarnings({"PMD.UnusedPrivateField", "PMD.UnusedFormalParameter",
        "PMD.UnusedLocalVariable"})
public class Graph {

    private Boolean resetModel = true;

    private Model zoomIn;
    private Model current;
    private Model zoomOut;
    private int currentInt = -1;
    private Object currentRef = null;
    private int nodeIds;

    /**
     * All the genomes that are in this graph.
     */
    private List<String> genomes = new ArrayList<>();

    /**
     * The genomes we want to draw.
     */
    private List<String> currentGenomes = new ArrayList<>();

    /**
     * Startmap.
     */
    private HashMap<Integer, Node> startMap;

    /**
     * All the (collapsed) maps.
     */
    private List<HashMap<Integer, Node>> levelMaps;


    /**
     * Class constructor.
     *
     * @throws IOException Throw exception
     */
    public Graph() throws IOException {
        zoomIn = new Model();
        current = new Model();
        zoomOut = new Model();

        try {
            startMap = getNodeMapFromFile();
            nodeIds = startMap.size();
            levelMaps = GraphReducer.createLevelMaps(startMap, 1);
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
     * @param selectedGenomes the genomes to display
     * @return Boolean used for testing purposes.
     * @throws IOException Throw exception on read GFA read failure.
     */
    @SuppressFBWarnings("OBL_UNSATISFIED_OBLIGATION_EXCEPTION_EDGE")
    public Boolean addGraphComponents(Object ref, int depth, List<String> selectedGenomes)
            throws IOException {
        if (depth <= levelMaps.size() - 1 && depth >= 0) {

            //Reset the model and re'add the levelMaps, since we have another reference or depth.
            if (currentInt == -1) { //First time we are here.
                currentInt = depth;
                current.setLevelMaps(levelMaps);
                current = generateModel(ref, depth);

                //LoadOneUp is only needed when we do not start on the top level.
                loadOneUp(depth, selectedGenomes);
                loadOneDown(depth, selectedGenomes);
            } else { //Second time. All models are loaded
                if (depth < currentInt) {
                    zoomOut = current;
                    current = zoomIn;
                    loadOneDown(depth, selectedGenomes);
                    currentInt = depth;
                } else if (depth > currentInt) {
                    zoomIn = current;
                    current = zoomOut;
                    loadOneUp(depth, selectedGenomes);
                    currentInt = depth;
                } else if (ref != currentRef) {
                    currentRef = ref;
                    current = generateModel(ref, depth);

                    //LoadOneUp is only needed when we do not start on the top level.
                    loadOneUp(depth, selectedGenomes);
                    loadOneDown(depth, selectedGenomes);
                }
            }
        }

        currentInt = depth;
        return true;
    }

    /**
     * Method to Zoom out
     *
     * @param depth           Depth to be loaded
     * @param selectedGenomes Genomes to display
     */
    private void loadOneUp(int depth, List<String> selectedGenomes) {
        int finalDepth = depth;
        new Thread("Load one up") {
            public void start() {
                if (finalDepth + 1 <= levelMaps.size() - 1) {
                    zoomOut = generateModel(currentRef, finalDepth + 1);
                }
            }
        }.start();
    }

    /**
     * Method to Zoom in
     *
     * @param depth           Depth to be loaded
     * @param selectedGenomes Genomes to display
     */
    private void loadOneDown(int depth, List<String> selectedGenomes) {
        int finalDepth = depth;
        new Thread("Load one down") {
            public void start() {
                if (finalDepth - 1 >= 0) {
                    zoomIn = generateModel(currentRef, finalDepth - 1);

                }
            }
        }.start();
    }

    /**
     * Method to generate a new model
     *
     * @param ref   reference object
     * @param depth the depth of the model
     * @return the new model
     */
    private Model generateModel(Object ref, int depth) {
        //Create a new Model to return
        Model toret = new Model();
        //Apply the levelMaps
        toret.setLevelMaps(levelMaps);
        //Select the level to draw from
        HashMap<Integer, Node> nodeMap = levelMaps.get(depth);
        //Root Node
        Node root = nodeMap.get(1);

        if (currentGenomes.size() > 0) { //Draw selected references
            System.out.println("Only drawing selected");
            //We are now drawing only the selected items.
            // Only draw when the intersection > 0 (Node contains genome that we
            // want to draw.
            if (intersection(root.getGenomes(), currentGenomes) > 0) {
                toret.addCell(root.getId(), root.getSequence(), CellType.RECTANGLE);
            }
            // In this case we know that the genomes in the graph are only this ones.
            genomes = currentGenomes;

            //current.clearCellMap();
            for (int i = 1; i < nodeIds; i++) {
                Node from = nodeMap.get(i);
                if (from == null) {
                    continue;
                }
                if (intersection(from.getGenomes(), currentGenomes) > 0) {
                    for (int j : from.getLinks(nodeMap)) {
                        Node to = nodeMap.get(j);
                        if (intersection(to.getGenomes(), currentGenomes) > 0) {
                            //Add next cell
                            NodeType type = nodeMap.get(j).getType();

                            if (type == NodeType.BASE) {
                                toret.addCell(to.getId(), to.getSequence(),
                                        CellType.RECTANGLE);

                            } else if (type == NodeType.BUBBLE) {
                                toret.addCell(to.getId(),
                                        Integer.toString(to.getCollapseLevel()),
                                        CellType.BUBBLE);
                            } else if (type == NodeType.INDEL) {
                                toret.addCell(to.getId(),
                                        Integer.toString(to.getCollapseLevel()),
                                        CellType.INDEL);
                            } else if (type == NodeType.COLLECTION) {
                                toret.addCell(to.getId(), Integer.toString(to.getCollapseLevel()),
                                        CellType.COLLECTION);
                            }

                            if (to.getGenomes().contains(ref) && from.getGenomes().contains(ref)) {
                                toret.addEdge(from.getId(), to.getId(),
                                        intersection(from.getGenomes(),
                                        to.getGenomes()), EdgeType.GRAPH_REF);
                            } else {
                                toret.addEdge(from.getId(), to.getId(),
                                        intersection(from.getGenomes(),
                                        to.getGenomes()), EdgeType.GRAPH);
                            }
                        }
                    }
                }
            }
        } else { // Draw all nodes.
            //Create a new genome list.
            toret.addCell(root.getId(), root.getSequence(), CellType.RECTANGLE);
            genomes = new ArrayList<>();
            genomes.addAll(root.getGenomes());

            for (int i = 1; i < nodeIds; i++) {
                Node from = nodeMap.get(i);
                if (from == null) {
                    continue;
                }

                for (int j : from.getLinks(nodeMap)) {
                    Node to = nodeMap.get(j);

                    to.getGenomes().stream().filter(s -> !genomes.contains(s)).
                            forEach(genomes::add);
                    //Add next cell
                    NodeType type = nodeMap.get(j).getType();

                    if (type == NodeType.BASE) {
                        toret.addCell(to.getId(), Integer.toString(to.getCollapseLevel()),
                                CellType.RECTANGLE);
                    } else if (type == NodeType.BUBBLE) {
                        toret.addCell(to.getId(), Integer.toString(to.getCollapseLevel()),
                                CellType.BUBBLE);
                    } else if (type == NodeType.INDEL) {
                        toret.addCell(to.getId(), Integer.toString(to.getCollapseLevel()),
                                CellType.INDEL);
                    } else if (type == NodeType.COLLECTION) {
                        toret.addCell(to.getId(), Integer.toString(to.getCollapseLevel()),
                                CellType.COLLECTION);
                    }

                    if (to.getGenomes().contains(ref) && from.getGenomes().contains(ref)) {
                        //current.addCell(to.getId(), to.getSequence(), CellType.RECTANGLE);
                        toret.addEdge(from.getId(), to.getId(), intersection(from.getGenomes(),
                                to.getGenomes()), EdgeType.GRAPH_REF);
                    } else {
                        toret.addEdge(from.getId(), to.getId(), intersection(from.getGenomes(),
                                to.getGenomes()), EdgeType.GRAPH);
                    }
                }
            }
        }

        toret.setLayout();
        return toret;
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
     * Getter method for the genomes.
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

    /**
     * Get the current level
     * @return the current level
     */
    public int getCurrentInt() {
        return currentInt;
    }

    /**
     * Indicate which strains are selected in the phylogenetic tree
     * @param s the selected strains
     */
    public void phyloSelection(List<String> s) {
        currentGenomes = s;
        currentInt = -1;
        currentRef = null;
    }

    /**
     * Get the current highlighted strain
     * @return the current highlighted strain
     */
    public Object getCurrentRef() {
        return currentRef;
    }

    /**
     * Get the levelMaps
     * @return the levelMaps
     */
    public List<HashMap<Integer, Node>> getLevelMaps() {
        return levelMaps;
    }
}