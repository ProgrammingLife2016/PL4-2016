package core.graph;

import application.fxobjects.Cell;
import application.fxobjects.Edge;
import core.annotation.Annotation;
import core.genome.Genome;
import core.model.Model;
import core.parsers.GraphParser;
import core.typeEnums.CellType;
import core.typeEnums.EdgeType;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import javafx.geometry.Rectangle2D;
import javafx.stage.Screen;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;


/**
 * Class representing a graph.
 */
public class Graph {

    private Rectangle2D screenSize;
    private Boolean debugScreenShouldBeInitialized;

    private Model zoomIn;
    private Model current;
    private Model zoomOut;

    private int currentInt = -1;
    private ArrayList<String> currentRef = new ArrayList<>();
    private ArrayList<String> allGenomes = new ArrayList<>();


    private boolean filtering;

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
        debugScreenShouldBeInitialized = true;

        zoomIn = new Model();
        current = new Model();
        zoomOut = new Model();

        annotations = new ArrayList<>();
    }

    /**
     * Read a node map from a gfa file on disk.
     *
     * @param path The file path of the GFA file.
     * @return A node map read from file.
     */
    public HashMap<Integer, Node> getNodeMapFromFile(String path) {
        try {
            startMap = new GraphParser().readGFAFromFile(path);
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
    public Boolean addGraphComponents(ArrayList<String> ref, int depth) {
        currentRef = ref;
        if (depth <= levelMaps.size() - 1 && depth >= 0) {

            //Reset the model and re'add the levelMaps, since we have another reference or depth.
            if (currentInt == -1) { //First time we are here.
                currentInt = depth;
                current.setLevelMaps(levelMaps);
                current = generateModel(ref, depth);

                //LoadOneUp is only needed when we do not start on the top level.
                loadBoth(depth);
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
                    loadBoth(depth);
                }
            }
        }
        currentInt = depth;
        return true;
    }

    public boolean changeLevelMaps(List<String> selectedGenomes) {
        if (!(intersection(selectedGenomes, currentGenomes) == currentGenomes.size() && currentGenomes.size() == selectedGenomes.size()) ) {
            levelMaps = GraphReducer.createLevelMaps(startMap, 1, selectedGenomes);
            currentGenomes = selectedGenomes;
            return true;
        }
        return false;
    }

    /**
     * Load both.
     * @param depth depth
     */
    private void loadBoth(int depth) {
        loadOneUp(depth);
        loadOneDown(depth);
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
    public Model generateModel(ArrayList<String> ref, int depth) {
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
    public Model generateModel(ArrayList<String> ref, int depth, Model toret) {
        //Apply the levelMaps and annotations
        toret.setLevelMaps(levelMaps);
        toret.setAnnotations(annotations);

        //Select the level to draw from
        HashMap<Integer, Node> nodeMap = levelMaps.get(depth);

        //Root Node
        Node root = nodeMap.get(1);
        allGenomes.addAll(root.getGenomes());
        if (filtering) { //Draw selected references
            // We are now drawing only the selected items.
            generateModelWithSelectedGenomes(nodeMap, root, toret, ref);
        } else { // Draw all nodes.
            //Create a new genome list.
            toret.addCell(root.getId(), root.getSequence(),
                    root.getNucleotides(), root.getType());
            genomes = new ArrayList<>();
            genomes.addAll(root.getGenomes());

            List<Integer> sortedNodeIds = topologicalSort(nodeMap);
            for (int nodeId : sortedNodeIds) {
                Node node = nodeMap.get(nodeId);
                if (node == null) {
                    continue;
                }
                node.getGenomes().stream().filter(s -> !genomes.contains(s)).
                        forEach(genomes::add);
                addCell(nodeMap, ref, toret, node);
            }
        }
        if (debugScreenShouldBeInitialized) {
            toret.setLayout();
        }
        return toret;
    }

    public ArrayList<String> findAllGenomes() {
        ArrayList<String> allGenomes = new ArrayList<>();
        for (int nodeId : startMap.keySet()) {
            Node node = startMap.get(nodeId);
            node.getGenomes().stream().filter(s -> !allGenomes.contains(s)).
                    forEach(allGenomes::add);
        }
        this.allGenomes = allGenomes;
        return allGenomes;
    }

    /**
     * Method to add a cell, that corresponds to a given node, to a model.
     *
     * @param nodeMap the map the node resides in
     * @param ref the reference strain
     * @param toret the model to add the cell to
     * @param node the node for which we need to add a cell
     */
    public void addCell(HashMap<Integer, Node> nodeMap, ArrayList<String> ref, Model toret, Node node) {
        //Add next cell
        CellType type = node.getType();

        if (type == CellType.RECTANGLE) {
            toret.addCell(node.getId(), node.getSequence(), node.getNucleotides(),
                    CellType.RECTANGLE);
        } else if (type == CellType.BUBBLE) {
            toret.addCell(node.getId(),
                    node.getBubbleText(),
                    node.getNucleotides(), CellType.BUBBLE);
        } else if (type == CellType.INDEL) {
            toret.addCell(node.getId(),
                    String.valueOf(node.getCollapseLevel()), node.getNucleotides(),
                    CellType.INDEL);
        } else if (type == CellType.COLLECTION) {
            toret.addCell(node.getId(), String.valueOf(node.getCollapseLevel()), node.getNucleotides(),
                    CellType.COLLECTION);
        } else if (type == CellType.COMPLEX) {
            toret.addCell(node.getId(), String.valueOf(node.getCollapseLevel()), node.getNucleotides(),
                    CellType.COMPLEX);
        }

        addEdgesToCell(node, nodeMap, toret, ref);
    }

    /**
     * Method to topologically sort the nodes in a given node map.
     * Puts the IDs of the topologically sorted nodes in a list.
     *
     * @param nodeMap the node map to sort topologically
     * @return the list of topologically sorted IDs
     */
    @SuppressFBWarnings
    public List<Integer> topologicalSort(HashMap<Integer, Node> nodeMap) {
        //Copy the nodeMap to not make any changes to our original map.
        HashMap<Integer, Node> copyNodeMap = GraphReducer.copyNodeMap(nodeMap);
        //nodesWithoutParent <-List of all nodes with no incoming edges
        LinkedList<Node> nodesWithoutParent = new LinkedList<>();
        for (int key : nodeMap.keySet()) {
            Node node = nodeMap.get(key);
            //Check whether the node has no parents
            if (node.getParents().size() == 0) {
                //Add the node to the list of nodes without parent
                nodesWithoutParent.addLast(node);
            }
        }
        return topologicalSort(nodesWithoutParent, copyNodeMap);
    }

    /**
     * Traverses the graph and adds all Node IDs horizontally
     * @param nodesWithoutParent list of starting nodes
     * @param nodeMap a copy of the nodemap the nodes reside in
     * @return
     */
    private List<Integer> topologicalSort(LinkedList<Node> nodesWithoutParent, HashMap<Integer, Node> nodeMap) {
        //Empty list to put all node IDs in topologically sorted order.
        List<Integer> sortedNodeIds = new ArrayList<>();
        //while still nodes without parent unvisited
        while (!nodesWithoutParent.isEmpty()) {
            //remove a node n from the list
            Node n = nodesWithoutParent.getFirst();
            nodesWithoutParent.remove(n);

            //insert ID of n into the sorted list
            sortedNodeIds.add(n.getId());

            //for each node m with an edge e from n to m do
            for (int childId : n.getLinks()) {
                Node m = nodeMap.get(childId);
                m.removeParent(n.getId()); //Remove edge from m

                //if m has no other incoming edges then insert m into the linked list.
                if (m.getParents().size() == 0) {
                    nodesWithoutParent.addLast(m);
                }
            }
        }
        //return the sorted list
        return sortedNodeIds;
    }


    /**
     * Draws the selected genomes.
     *
     * @param nodeMap map of nodes.
     * @param root    Root of the graph.
     * @param toret   A given model.
     * @param ref     Reference object.
     */
    private void generateModelWithSelectedGenomes(HashMap<Integer, Node> nodeMap, Node root,
                                                  Model toret, ArrayList<String> ref) {
        if (intersection(root.getGenomes(), currentGenomes) > 0) {
            toret.addCell(root.getId(), root.getSequence(),
                    root.getNucleotides(), CellType.RECTANGLE);
        }

        // In this case we know that the genomes in the graph are only this ones.
        genomes = currentGenomes;

        // Only draw when the intersection > 0 (Node contains genome that we
        // want to draw.
        List<Integer> sortedNodeIds = topologicalSort(nodeMap);
        for (int nodeId : sortedNodeIds) {
            Node from = nodeMap.get(nodeId);
            if (from == null) {
                continue;
            }
            if (intersection(from.getGenomes(), currentGenomes) > 0) {
                for (int j : from.getLinks(nodeMap)) {
                    Node to = nodeMap.get(j);
                    if (intersection(to.getGenomes(), currentGenomes) > 0) {
                        //Add next cell
                        addCell(nodeMap, ref, toret, from);
                    }
                }
            }
        }
    }

    /**
     * Method to add Edges to a cell.
     * @param to the target node
     * @param nodeMap the node map both nodes reside in
     * @param toret the model to which the edges are added
     * @param ref the reference strain
     */
    public void addEdgesToCell(Node to, HashMap<Integer, Node> nodeMap, Model toret,
                                ArrayList<String> ref) {

        for (int parentId : to.getParents()) {
            Node from = nodeMap.get(parentId);
            int maxEdgeWidth = 10;
            int width = (int) Math.round(maxEdgeWidth
                    * ((double) intersection(intersectingStrings(from.getGenomes(), genomes),
                    intersectingStrings(to.getGenomes(), genomes))
                    / (double) Math.max(genomes.size(), 10))) + 1;

            ifStatement:
            if (intersection(from.getGenomes(), ref) > 0 && intersection(to.getGenomes(), ref) > 0) {
                boolean edgePlaced = false;
                for (int child : from.getLinks()) {
                    if (intersectionInt(nodeMap.get(child).getLinks(), from.getLinks()) > 0
                            && intersection(nodeMap.get(child).getGenomes(), ref) > 0 && ref.size() < 2) {
                        toret.addEdge(from.getId(), to.getId(), width, EdgeType.GRAPH);
                        if (intersection(nodeMap.get(child).getGenomes(), ref) > 0) {
                            toret.addEdge(from.getId(), child, width, EdgeType.GRAPH_REF);
                        }
                        edgePlaced = true;
                    }
                }
                if (edgePlaced) {
                    break ifStatement;
                }
                toret.addEdge(from.getId(), to.getId(), width, EdgeType.GRAPH_REF);

            } else {
                toret.addEdge(from.getId(), to.getId(), width, EdgeType.GRAPH);
            }
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
     * Method to get number of intersecting ints in a list.
     *
     * @param l1 first list.
     * @param l2 second list.
     * @return number of intersecting elements.
     */
    public int intersectionInt(List<Integer> l1, List<Integer> l2) {
        int i = 0;
        for (Integer s : l1) {
            if (l2.contains(s)) {
                i++;
            }
        }
        return i;
    }

    /**
     * Method that returns a list of strings present in both lists.
     *
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
    public ArrayList<String> getCurrentRef() {
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

        zoomIn.setAnnotations(annotations);
        current.setAnnotations(annotations);
        zoomOut.setAnnotations(annotations);
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

    /**
     * Method to return a model with all nodes within the view.
     *
     * @param min left side of the view.
     * @return the model.
     */
    public Model getModelAllInView(int min) {
        Model m = new Model();
        screenSize = Screen.getPrimary().getVisualBounds();
        int startTile = Math.max(((int) ((min - (min % screenSize.getWidth())) / screenSize.getWidth()) - 1), 0);
        for (int i = startTile; i < startTile + 3; i++) {
            if (getModel().getCellTile(i) != null) {
                for (Cell c : getModel().getCellTile(i)) {
                    m.addCell(c);
                    for (Edge e : c.getEdges()) {
                        m.addEdge(e);
                    }
                }
            }
        }

        return addFirstAndLast(m);
    }

    /**
     * Adds the leftmost and rightmost cell to the Model.
     *
     * @param m the Model to add cells to.
     * @return the new Model.
     */
    private Model addFirstAndLast(Model m) {
        if (current.getRightMost() != null) {
            if (!(m.getAllCells().contains(getModel().getLeftMost()))) {
                m.addCell(getModel().getLeftMost());
            }

            if (!m.getAllCells().contains(getModel().getRightMost())) {
                m.addCell(getModel().getRightMost());
            }
        }
        return m;
    }

    /**
     * Reduce the list of selected genomes to genomes available in the loaded graph.
     *
     * @param genomes selected genomes.
     * @param filtering whether filters are applied.
     * @return the reduced list of genomes.
     */
    public List<String> reduceGenomes(List<Genome> genomes, boolean filtering) {
        this.filtering = filtering;
        List<String> selectedGenomeNames = genomes.stream().map(Genome::getName)
                .collect(Collectors.toList());
        return selectedGenomeNames.stream().filter(
                this.allGenomes::contains).collect(Collectors.toList());
    }

    /**
     * Gets whether screen elements should be initialized.
     *
     * @return Whether screen elements should be initialized.
     */
    public Boolean getDebugScreenShouldBeInitialized() {
        return debugScreenShouldBeInitialized;
    }

    /**
     * Sets whether screen elements should be initialized.
     *
     * @param debugScreenShouldBeInitialized Whether screen elements should be initialized.
     */
    public void setDebugScreenShouldBeInitialized(Boolean debugScreenShouldBeInitialized) {
        this.debugScreenShouldBeInitialized = debugScreenShouldBeInitialized;
    }

}