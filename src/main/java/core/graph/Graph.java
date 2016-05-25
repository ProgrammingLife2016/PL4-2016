package core.graph;

import core.GraphReducer;
import core.Model;
import core.Node;
import core.Parser;
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
@SuppressWarnings({"PMD.UnusedPrivateField", "PMD.UnusedLocalVariable"})
public class Graph {

    private Boolean resetModel = true;

    private Model zoomIn;
    private Model current;
    private Model zoomOut;
    private int currentInt = -1;
    private Object currentRef = null;

    private List<String> genomes = new ArrayList<>();

    private List<String> genomesToDraw = new ArrayList<>();

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
//    @SuppressFBWarnings("OBL_UNSATISFIED_OBLIGATION_EXCEPTION_EDGE")
//    public HashMap<Integer, Node> getNodeMapFromFile() throws IOException {
//        Parser parser = new Parser();
//        InputStream inputStream = getClass().getResourceAsStream("/TB10.gfa");
//        HashMap<Integer, Node> startMap = parser.readGFA(inputStream);
//        inputStream.close();
//
//        return startMap;
//    }

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

        if (depth <= levelMaps.size() - 1 && depth >= 0) {

            System.out.println("Trying to draw:" + depth);
            //Normalize.
//            if (depth > levelMaps.size() - 1) {
//                depth = levelMaps.size() - 1;
//            } else if (depth < 0) {
//                depth = 0;
//            }

            //Reset the model and re'add the levelMaps, since we have another reference or depth.
            if (currentInt == -1) { //First time we are here.
                currentInt = depth;
                current.setLevelMaps(levelMaps);
                current = generateModel(ref, depth);
                System.out.println("todraw: " + genomesToDraw.toString());
                //LoadOneUp is only needed when we do not start on the top level.
                loadOneUp(ref, depth);
                loadOneDown(ref, depth);
            } else { //Second time. All models are loaded
                if (depth < currentInt) {
                    System.out.println("Zoom in");
                    zoomOut = current;
                    current = zoomIn;
                    loadOneDown(ref, depth);
                    currentInt = depth;
                } else if (depth > currentInt) {
                    System.out.println("Zoom out");
                    zoomIn = current;
                    current = zoomOut;
                    loadOneUp(ref, depth);
                    currentInt = depth;
                } else if (ref != currentRef){
                    System.out.println("Found a new ref");
                    current = generateModel(ref, depth);
                    System.out.println("todraw: " + genomesToDraw.toString());
                    //LoadOneUp is only needed when we do not start on the top level.
                    loadOneUp(ref, depth);
                    loadOneDown(ref, depth);
                }
            }
            System.out.println("CurrentInt: " + currentInt);

            // @TODO Switch method maken.
            // @TODO Check maken of we al kunnen switchen(Is de thread al klaar?)
        }
        return true;
    }

    private void loadOneUp(Object ref, int depth) {
        int finalDepth = depth;
        new Thread("Load one up") {
            public void run() {
                if (finalDepth + 1 <= levelMaps.size() - 1) {
                    zoomOut = new Model();
                    zoomOut = generateModel(ref, finalDepth + 1);
                    zoomOut.setLayout();
                    System.out.println("(THREAD): Done loading: " + (finalDepth + 1));
                } else {
                    System.out.println("(THREAD): Not loading map: " + (finalDepth + 1));
                }
            }
        }.run();
    }

    private void loadOneDown(Object ref, int depth) {
        int finalDepth = depth;
        new Thread("Load one down") {
            public void run() {
                if (finalDepth - 1 >= 0) {
                    zoomIn = new Model();
                    zoomIn = generateModel(ref, finalDepth - 1);
                    zoomIn.setLayout();
                    System.out.println("(THREAD): Done loading: " + (finalDepth - 1));

                } else {
                    System.out.println("(THREAD): Not loading map: " + (finalDepth - 1));
                }
            }
        }.run();
    }

    private Model generateModel(Object ref, int depth) {
        Model toret = new Model();
        toret.setLevelMaps(levelMaps);
        HashMap<Integer, Node> nodeMap = levelMaps.get(depth);
        Node root = nodeMap.get(1);

        if (ref == null) {
            ref = root.getGenomes().get(0);
        }

        if (genomesToDraw.size() > 0) {
            System.out.println("Only drawing selected");
            //We are now drawing only the selected items.
            // Only draw when the intersection > 0 (Node contains genome that we
            // want to draw.
            if (intersection(root.getGenomes(), genomesToDraw) > 0) {
                toret.addCell(root.getId(), root.getSequence(), CellType.TRIANGLE);
            }

            genomes = genomesToDraw;
            for (int i = 1; i <= nodeMap.size(); i++) {
                Node from = nodeMap.get(i);
                if (from == null) {
                    continue;
                }

                for (int j : from.getLinks(nodeMap)) {
                    Node to = nodeMap.get(j);
                    //Add next cell
                    if (intersection(to.getGenomes(), genomesToDraw) > 0) {
                        if (nodeMap.get(j).getGenomes().contains(ref)) {
                            toret.addCell(to.getId(), to.getSequence(), CellType.TRIANGLE);
                            toret.addEdge(from.getId(), to.getId(), intersection(from.getGenomes(),
                                    to.getGenomes()), EdgeType.GRAPH);
                        } else {
                            toret.addCell(to.getId(), to.getSequence(), CellType.TRIANGLE);
                            toret.addEdge(from.getId(), to.getId(), intersection(from.getGenomes(),
                                    to.getGenomes()), EdgeType.GRAPH);
                        }
                    }
                }
            }


        } else {

            if (root.getGenomes().contains(ref)) {
                toret.addCell(root.getId(), root.getSequence(), CellType.TRIANGLE);
            } else {
                toret.addCell(root.getId(), root.getSequence(), CellType.TRIANGLE);
            }

            genomes = new ArrayList<>();
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
                        toret.addCell(to.getId(), to.getSequence(), CellType.TRIANGLE);
                        toret.addEdge(from.getId(), to.getId(), intersection(from.getGenomes(),
                                to.getGenomes()), EdgeType.GRAPH);
                    } else {
                        toret.addCell(to.getId(), to.getSequence(), CellType.TRIANGLE);
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

    public int getCurrentInt() {
        return currentInt;
    }

    public void phyloSelection(List<String> s) {
        genomesToDraw = s;
        currentInt = -1;
//        try {
//            addGraphComponents(null,levelMaps.size()-1);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

    }
}