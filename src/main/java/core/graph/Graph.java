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
     * Add the nodes and edges of the graph to the model.
     *
     * @param ref the reference string.
     * @throws IOException Throw exception on read GFA read failure.
     */
    @SuppressFBWarnings("OBL_UNSATISFIED_OBLIGATION_EXCEPTION_EDGE")
    public Boolean addGraphComponents(Object ref) throws IOException {
        Parser parser = new Parser();
        InputStream inputStream = getClass().getResourceAsStream("/TB10.gfa");
        HashMap<Integer, Node> startMap = null;
        if (startMap == null) {
            startMap = parser.readGFA(inputStream);
        } else {
            System.out.println("We already read the file");
        }
        inputStream.close();

        List<HashMap<Integer, Node>> levelMaps = GraphReducer.createLevelMaps(startMap);
        model.setLevelMaps(levelMaps);



        //Reset the model, since we have another reference.
        model = new Model();
        HashMap<Integer, Node> nodeMap = levelMaps.get(levelMaps.size() - 1);

        Node root = nodeMap.get(1);

        if (root.getGenomes().contains(ref)) {
            model.addCell(root.getId(), root.getSequence(), CellType.RECTANGLE);
        } else {
            //This IS NOT the reference, this should be a TRIANGLE,
            //but out program does not like it to be one.
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

        return true;
    }

    /**
     * Method that updates the model.
     */
    public void endUpdate() {
        // every cell must have a parent, if it doesn't, then the graphParent is
        // the parent
        getModel().attachOrphansToGraphParent(model.getAddedCells());

        // remove reference to graphParent
        getModel().disconnectFromGraphParent(model.getRemovedCells());

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
    private int intersection(List<String> l1, List<String> l2) {
        int i = 0;
        for (String s : l1) {
            if (l2.contains(s)) {
                i++;
            }
        }
        return i;
    }

    /**
     * Unused at this point of time.
     */
    public void addPhylogeneticTree() {

        try {
            setup();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Implementing phylogenetic tree here.
     */
    //TreeItem current;

    /**
     * Setup method for the PHYLOGENETIC Tree.
     *
     * @throws IOException Throw exception on read failure.
     */
    @SuppressFBWarnings({"I18N", "NP_DEREFERENCE_OF_READLINE_VALUE"})
    void setup() throws IOException {
//        File f = new File("src/main/resources/340tree.rooted.TKK.nwk");
//        BufferedReader r = new BufferedReader(new FileReader(f));
//        String t = r.readLine();
//        r.close();
//
//        current = TreeParser.parse(t);
//
//        Model model = getModel();
//        int i = 1;
//
//        Queue<TreeItem> q = new LinkedList<>();
//        //ArrayList<Integer> done = new ArrayList<>();
//
//        System.out.println(current.getName());
//        q.add(current);
//        model.addCell(i, current.getName(), CellType.PHYLOGENETIC);
//        System.out.println("Cell added: " + i);
//
//        while (!q.isEmpty()) {
//            current = q.poll();
//            //From node
//            int j = i;
//
//            for (TreeItem child : current.getChildren()) {
//                model.addCell(++i, child.getName(), CellType.PHYLOGENETIC);
//                System.out.println("Cell added: " + i);
//                model.addEdge(j, i, 1);
//                System.out.println("Link added: " + j + ", " + i);
//                q.add(child);
//            }
//        }
        endUpdate();
    }

    /**
     * Getter method for the genomens.
     *
     * @return the genomes.
     */
    public List<String> getGenomes() {
        return genomes;
    }
}