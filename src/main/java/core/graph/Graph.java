package core.graph;

import core.*;
import core.graph.cell.CellType;
import core.graph.cell.EdgeType;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

import java.io.IOException;
import java.util.*;

/**
 * Class representing a graph.
 */
@SuppressWarnings({"PMD.UnusedPrivateField", "PMD.UnusedFormalParameter",
        "PMD.UnusedLocalVariable"})
public class Graph {

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
     * Reference annotations.
     */
    private List<Annotation> annotations;

    /**
     * Class constructor.
     */
    public Graph() {
        zoomIn = new Model();
        current = new Model();
        zoomOut = new Model();

        annotations = new ArrayList<>();
    }

    /**
     * Read a node map from a gfa file on disk.
     *
     * @return A node map read from file.
     */
    @SuppressFBWarnings("OBL_UNSATISFIED_OBLIGATION_EXCEPTION_EDGE")
    public HashMap<Integer, Node> getNodeMapFromFile(String s) {
        try {
            Parser parser = new Parser();

            startMap = parser.readGFAAsString(s);
            nodeIds = startMap.size();
            levelMaps = GraphReducer.createLevelMaps(startMap, 1);

        } catch (IOException e) {
            e.printStackTrace();
        }

        return startMap;
    }

    /**
     * Add the nodes and edges of the graph to the model.
     *
     * @param ref   the reference string.
     * @param depth the depth to draw.
     * @return Boolean used for testing purposes.
     */
    @SuppressFBWarnings("OBL_UNSATISFIED_OBLIGATION_EXCEPTION_EDGE")
    public Boolean addGraphComponents(Object ref, int depth) {
        currentRef = ref;
        if (depth <= levelMaps.size() - 1 && depth >= 0) {

            //Reset the model and re'add the levelMaps, since we have another reference or depth.
            if (currentInt == -1) { //First time we are here.
                currentInt = depth;
                current.setLevelMaps(levelMaps);
                //currentRef = ref;
                current = generateModel(ref, depth);

                //LoadOneUp is only needed when we do not start on the top level.
                loadOneUp(depth);
                loadOneDown(depth);
            } else { //Second time. All models are loaded
                if (depth < currentInt) {
                    zoomOut = current;
                    current = zoomIn;
                    loadOneDown(depth);
                    currentInt = depth;
                } else if (depth > currentInt) {
                    zoomIn = current;
                    current = zoomOut;
                    loadOneUp(depth);
                    currentInt = depth;
                } else if (ref != currentRef) {
                    currentRef = ref;
                    current = generateModel(ref, depth);

                    //LoadOneUp is only needed when we do not start on the top level.
                    loadOneUp(depth);
                    loadOneDown(depth);
                }
            }
        }

        currentInt = depth;
        return true;
    }

    /**
     * Method to Zoom out
     *
     * @param depth Depth to be loaded
     */
    private void loadOneUp(int depth) {
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
     * @param depth Depth to be loaded
     */
    private void loadOneDown(int depth) {
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
    public Model generateModel(Object ref, int depth) {
        return generateModel(ref, depth, new Model());
    }

    /**
     * Method to generate a new model
     *
     * @param ref   reference object
     * @param depth the depth of the model
     * @param toret a given model
     * @return the new model
     */
    public Model generateModel(Object ref, int depth, Model toret) {
        //Apply the levelMaps and annotations
        toret.setLevelMaps(levelMaps);
        toret.setAnnotations(annotations);
        System.out.println("Num annotations: " + toret.getAnnotations().size());

        //Select the level to draw from
        HashMap<Integer, Node> nodeMap = levelMaps.get(depth);

        //Root Node
        Node root = nodeMap.get(1);
        if (currentGenomes.size() > 0) { //Draw selected references
            // We are now drawing only the selected items.
            generateModelWithSelectedGenomes(nodeMap, root, toret, ref);
        } else { // Draw all nodes.
            //Create a new genome list.
            toret.addCell(root.getId(), root.getSequence(),
                    root.getNucleotides(), CellType.RECTANGLE);
            genomes = new ArrayList<>();
            genomes.addAll(root.getGenomes());
            for (int i = 1; i < nodeIds; i++) {
                Node from = nodeMap.get(i);
                if (from == null) { continue; }
                for (int j : from.getLinks(nodeMap)) {
                    Node to = nodeMap.get(j);
                    to.getGenomes().stream().filter(s -> !genomes.contains(s)).
                            forEach(genomes::add);
                    //Add next cell
                    addCell(nodeMap, toret, j, ref, to, from);
                }
            }
        }

        toret.setLayout();
        return toret;
    }


    /**
     * Draws the selected genomes.
     *
     * @param nodeMap map of nodes.
     * @param root Root of the graph.
     * @param toret A given model.
     * @param ref Reference object.
     */
    private void generateModelWithSelectedGenomes(HashMap<Integer, Node> nodeMap, Node root,
                                                 Model toret, Object ref) {
        if (intersection(root.getGenomes(), currentGenomes) > 0) {
            toret.addCell(root.getId(), root.getSequence(),
                    root.getNucleotides(), CellType.RECTANGLE); }

        // In this case we know that the genomes in the graph are only this ones.
        genomes = currentGenomes;

        // Only draw when the intersection > 0 (Node contains genome that we
        // want to draw.
        for (int i = 1; i < nodeIds; i++) {
            Node from = nodeMap.get(i);
            if (from == null) { continue; }
            if (intersection(from.getGenomes(), currentGenomes) > 0) {
                for (int j : from.getLinks(nodeMap)) {
                    Node to = nodeMap.get(j);
                    if (intersection(to.getGenomes(), currentGenomes) > 0) {
                        //Add next cell
                        addCell(nodeMap, toret, j, ref, to, from);
                    }
                }
            }
        }
    }
    /**
     * Method to add a new Cell to the graph
     *
     * @param nodeMap the current NodeMap we are reading from
     * @param toret   the Model the Cell is added to
     * @param j       the number of the Cell
     * @param ref     the current Reference strain
     * @param to      cell we are going to
     * @param from    cell we are coming from
     */
    public void addCell(HashMap<Integer, Node> nodeMap, Model toret, int j,
                        Object ref, Node to, Node from) {
        //Add next cell
        int maxEdgeWidth = 10;
        CellType type = nodeMap.get(j).getType();

        if (type == CellType.RECTANGLE) {
            toret.addCell(to.getId(), to.getSequence(), to.getNucleotides(),
                    CellType.RECTANGLE);
        } else if (type == CellType.BUBBLE) {
            toret.addCell(to.getId(),
                    Integer.toString(to.getCollapseLevel()),
                    to.getNucleotides(), CellType.BUBBLE);
        } else if (type == CellType.INDEL) {
            toret.addCell(to.getId(),
                    Integer.toString(to.getCollapseLevel()), to.getNucleotides(),
                    CellType.INDEL);
        } else if (type == CellType.COLLECTION) {
            toret.addCell(to.getId(), Integer.toString(to.getCollapseLevel()), to.getNucleotides(),
                    CellType.COLLECTION);
        }

        if (to.getGenomes().contains(ref) && from.getGenomes().contains(ref)) {
            int width = (int) Math.round(maxEdgeWidth
                    * ((double) intersection(intersectingStrings(from.getGenomes(), genomes),
                    intersectingStrings(to.getGenomes(), genomes))
                    / (double) Math.max(genomes.size(), 10))) + 1;
            toret.addEdge(from.getId(), to.getId(), width, EdgeType.GRAPH_REF);
        } else {
            int width = (int) Math.round(maxEdgeWidth
                    * ((double) intersection(intersectingStrings(from.getGenomes(), genomes),
                    intersectingStrings(to.getGenomes(), genomes))
                    / (double) Math.max(genomes.size(), 10))) + 1;
            toret.addEdge(from.getId(), to.getId(), width, EdgeType.GRAPH);
        }
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
     * Method that returns a list of strings present in both lists.
     * @param l1 first list
     * @param l2 second list
     * @return result
     */
    public List<String> intersectingStrings(List<String> l1, List<String> l2) {
        ArrayList<String> res = new ArrayList<String>();
        for (String s : l1) {
            if (l2.contains(s)) {
                res.add(s);
            }
        }
        return res;
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
     * Getter method for the genomes.
     *
     * @return the genomes.
     */
    public List<String> getCurrentGenomes() {
        return this.currentGenomes;
    }

    /**
     * Setter method for the genomes.
     *
     * @param genomes the genomes.
     */
    public void setCurrentGenomes(List<String> genomes) {
        this.currentGenomes = genomes;
    }

    /**
     * Get the current level.
     *
     * @return the current level.
     */
    public int getCurrentInt() {
        return currentInt;
    }

    /**
     * Get the current highlighted strain.
     *
     * @return the current highlighted strain.
     */
    public Object getCurrentRef() {
        return currentRef;
    }

    /**
     * Get the levelMaps.
     *
     * @return the levelMaps.
     */
    public List<HashMap<Integer, Node>> getLevelMaps() {
        return levelMaps;
    }

    /**
     * Set the levelMaps.
     *
     * @param levelMaps the levelMaps.
     */
    public void setLevelMaps(List<HashMap<Integer, Node>> levelMaps) {
        this.levelMaps = levelMaps;
    }

    /**
     * Get the annotations.
     *
     * @return The annotations.
     */
    public List<Annotation> getAnnotations() {
        return annotations;
    }

    /**
     * Sets the annotations.
     *
     * @param annotations The annotations
     */
    public void setAnnotations(List<Annotation> annotations) {
        this.annotations = annotations;
    }

    /**
     * Method to reset the current view.
     */
    public void reset() {
        this.currentInt = -1;
    }

    /**
     * Method to get the MaxWidth.
     *
     * @return the MaxWidth.
     */
    public double getMaxWidth() {
        return current.getMaxWidth();
    }
}