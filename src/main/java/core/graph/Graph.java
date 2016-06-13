package core.graph;

import core.annotation.Annotation;
import core.model.Model;
import core.parsers.GraphParser;
import core.typeEnums.CellType;
import core.typeEnums.EdgeType;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

import java.io.IOException;
import java.util.*;


/**
 * Class representing a graph.
 */
@SuppressWarnings({"PMD.UnusedPrivateField", "PMD.UnusedFormalParameter",
        "PMD.UnusedLocalVariable", "PMD.UselessParentheses"})
public class Graph {

    private Model zoomIn;
    private Model current;
    private Model zoomOut;
    private int currentInt = -1;
    private ArrayList<String> currentRef = new ArrayList<>();
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
     * @param path The file path of the GFA file.
     * @return A node map read from file.
     */
    @SuppressFBWarnings("OBL_UNSATISFIED_OBLIGATION_EXCEPTION_EDGE")
    public HashMap<Integer, Node> getNodeMapFromFile(String path) {
        try {
            GraphParser parser = new GraphParser();
            startMap = parser.readGFAFromFile(path);
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
    public Boolean addGraphComponents(ArrayList<String> ref, int depth) {
        currentRef = ref;
        if (depth <= levelMaps.size() - 1 && depth >= 0) {

            //Reset the model and re'add the levelMaps, since we have another reference or depth.
            if (currentInt == -1) { //First time we are here.
                currentInt = depth;
                current.setLevelMaps(levelMaps);
                //currentRef = ref;
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
                    System.out.println("!=");
                    current = generateModel(ref, depth);

                    //LoadOneUp is only needed when we do not start on the top level.
                    loadBoth(depth);
                }
            }
        }
        currentInt = depth;
        return true;
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
        if (currentRef.size() > 0) { //Draw selected references
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
                Node node= nodeMap.get(nodeId);
                if (node == null) {
                    continue;
                }
                node.getGenomes().stream().filter(s -> !genomes.contains(s)).
                        forEach(genomes::add);
                newAddCell(nodeMap, ref, toret, node);
            }
        }
        toret.setLayout();
        return toret;
    }

    public void newAddCell(HashMap<Integer, Node> nodeMap, ArrayList<String> ref, Model toret, Node node) {

        //Add next cell
        int maxEdgeWidth = 10;
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
        for (int parentId : node.getParents()) {
            Node parent = nodeMap.get(parentId);
            int width = (int) Math.round(maxEdgeWidth
                    * ((double) intersection(intersectingStrings(parent.getGenomes(), genomes),
                    intersectingStrings(node.getGenomes(), genomes))
                    / (double) Math.max(genomes.size(), 10))) + 1;
            toret.addEdge(parentId, node.getId(), width, EdgeType.GRAPH);
        }
    }

    public List<Integer> topologicalSort(HashMap<Integer, Node> nodeMap) {
        //S <- Set of all nodes with no incoming edges
        HashMap<Integer, Node> copyNodeMap = new HashMap<>();
        copyNodeMap = GraphReducer.copyNodeMap(nodeMap);
        HashSet<Node> S = new HashSet<>();
        List<Integer> L = new ArrayList<>();
        for (int key : nodeMap.keySet()) {
            Node node = nodeMap.get(key);
            //Check whether the node has no parents
            if (node.getParents().size() == 0) {
                //Add the node to the stack of nodes without parent
                S.add(node);
            }
        }

        //while S is non-empty do
        while (!S.isEmpty()) {
            //remove a node n from S
            Node n = S.iterator().next();

            S.remove(n);

            //insert n into L
            L.add(n.getId());

            //for each node m with an edge e from n to m do
            for (int childId : n.getLinks()) {
                //remove edge e from the graph
                Node m = copyNodeMap.get(childId);
                m.removeParent(n.getId()); //Remove edge from m

                //if m has no other incoming edges then insert m into S
                if (m.getParents().size() == 0) {
                    S.add(m);
                }
            }
        }

        return L;
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

        System.out.println("oops");
        // Only draw when the intersection > 0 (Node contains genome that we
        // want to draw.
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
                        newAddCell(nodeMap, ref, toret, from);
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
                        ArrayList<String> ref, Node to, Node from) {
        //Add next cell
        CellType type = nodeMap.get(j).getType();

        if (type == CellType.RECTANGLE) {
            toret.addCell(to.getId(), to.getSequence(), to.getNucleotides(), CellType.RECTANGLE);
        } else if (type == CellType.BUBBLE) {
            toret.addCell(to.getId(), to.getBubbleText(), to.getNucleotides(), CellType.BUBBLE);
        } else if (type == CellType.INDEL) {
            toret.addCell(to.getId(),
                    String.valueOf(to.getCollapseLevel()), to.getNucleotides(), CellType.INDEL);
        } else if (type == CellType.COLLECTION) {
            toret.addCell(to.getId(), String.valueOf(to.getCollapseLevel()), to.getNucleotides(),
                    CellType.COLLECTION);
        } else if (type == CellType.COMPLEX) {
            toret.addCell(to.getId(), String.valueOf(to.getCollapseLevel()), to.getNucleotides(),
                    CellType.COMPLEX);
        }

        addEdgesToCell(to, from, nodeMap, toret, ref);

    }

    private void addEdgesToCell(Node to, Node from, HashMap<Integer, Node> nodeMap, Model toret,
                                ArrayList<String> ref) {
        int maxEdgeWidth = 10;
        int width = (int) Math.round(maxEdgeWidth
                * ((double) intersection(intersectingStrings(from.getGenomes(), genomes),
                intersectingStrings(to.getGenomes(), genomes))
                / (double) Math.max(genomes.size(), 10))) + 1;

        ifStatement:
        if (intersection(from.getGenomes(), ref) > 0 && intersection(to.getGenomes(), ref) > 0) {
            boolean edgePlaced = false;
            for (int child : from.getLinks()) {
                if ((intersectionInt(nodeMap.get(child).getLinks(), from.getLinks()) > 0)
                        && intersection(nodeMap.get(child).getGenomes(), ref) > 0
                        && ref.size() < 2) {
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
     * @param max right side of the view.
     * @return the model.
     */
    public Model getModelAddedInView(int min, int max) {
        Model m = new Model();

        this.getModel().getAddedCells().stream().filter(c -> c.getLayoutX() > min && c.getLayoutX() <= max)
                .forEach(m::addCell);

        this.getModel().getAddedEdges().stream().filter(e -> !(
                (e.getSource().getLayoutX() < min && e.getTarget().getLayoutX() < min)
                        || (e.getSource().getLayoutX() > max && e.getTarget().getLayoutX() > max)))
                .forEach(m::addEdge);

        return addFirstAndLast(m);
    }

    /**
     * Method to return a model with all nodes within the view.
     *
     * @param min left side of the view.
     * @param max right side of the view.
     * @return the model.
     */
    public Model getModelAllInView(int min, int max) {
        Model m = new Model();

        this.getModel().getAllCells().stream().filter(c -> c.getLayoutX() > min && c.getLayoutX() <= max)
                .forEach(m::addCell);

        this.getModel().getAllEdges().stream().filter(e -> !(
                (e.getSource().getLayoutX() < min && e.getTarget().getLayoutX() < min)
                        || (e.getSource().getLayoutX() > max && e.getTarget().getLayoutX() > max))).forEach(m::addEdge);

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
}