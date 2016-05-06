package application.controllers;

import application.TreeItem;
import application.TreeParser;
import application.fxobjects.ZoomBox;
import application.fxobjects.graph.Graph;
import application.fxobjects.graph.Model;
import application.fxobjects.graph.cell.BaseLayout;
import application.fxobjects.graph.cell.CellLayout;
import application.fxobjects.graph.cell.CellType;
import core.GraphReducer;
import core.Node;
import core.Parser;
import javafx.fxml.FXML;
import javafx.scene.control.MenuBar;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;

import java.io.*;
import java.net.URL;
import java.util.*;

/**
 * Controller class, used when creating other controllers.
 *
 * @author Daphne van Tetering.
 * @version 1.0
 * @since 26-04-2016
 */
public class GraphViewController extends Controller<StackPane> {
    private Graph graph;

    private MenuController menuController;
    private HashMap<Integer, Node> fullNodeMap;

    private ZoomController zoomController;
    private ZoomBox zoomBox;

    @FXML
    StackPane screen;
    @FXML
    MenuBar menuBar;

    /**
     * Constructor: generate a Controller.
     */
    public GraphViewController() {
        super(new StackPane());
        loadFXMLfile("/fxml/graphview.fxml");
        this.graph = new Graph();

        zoomController = graph.getZoomController();
        zoomBox = zoomController.getZoomBox();
    }

    /**
     * Method to initialize.
     *
     * @param location
     * @param resources
     */
    public void initialize(URL location, ResourceBundle resources) {
        graph = new Graph();

        BorderPane root = graph.getZoomController().getPane();

        HBox hbox = new HBox();
        menuController = new MenuController(this, menuBar);

        hbox.getChildren().addAll(screen, menuBar);
        screen.getChildren().setAll();

        root.setTop(hbox);

        createLevelMaps();
        // For now only render the most collapsed map
        addGraphComponents(graph.getModel().getLevelMaps().size() - 1);
        //addPhylogeneticTree();
        CellLayout layout = new BaseLayout(graph, 100);
        layout.execute();

        this.getRoot().getChildren().addAll(root);
    }

    /**
     * Create the zoom level maps.
     */
    public void createLevelMaps() {
        Model model = graph.getModel();
        graph.beginUpdate();
        Parser parser = new Parser();

        HashMap<Integer, Node> startMap
                = parser.readGFA(this.getClass().getResourceAsStream("/TB10.gfa"));
        List<HashMap<Integer, Node>> levelMaps = GraphReducer.createLevelMaps(startMap);
        model.setLevelMaps(levelMaps);
    }
    /**
     * Method that adds all nodes to the Model.
     */
    public void addGraphComponents(int zoomLevel) {
        Model model = graph.getModel();
        HashMap<Integer, Node> nodeMap = model.getLevelMaps().get(zoomLevel);

        Node root = (nodeMap.get(1));
        model.addCell(root.getId(), root.getSequence(), CellType.RECTANGLE);

        for (int idx = 1; idx <= nodeMap.size(); idx++) {
            Node node = nodeMap.get(idx);
            if (node != null) {
                for (Node child : node.getLiveLinks(nodeMap)) {
                    //Add next cell
                    if (node.getLiveLinks(nodeMap).size() == 1) {
                        model.addCell(child.getId(), child.getSequence(), CellType.RECTANGLE);
                    } else {
                        model.addCell(child.getId(), child.getSequence(), CellType.TRIANGLE);
                    }
                    //Add link from current cell to next cell
                    model.addEdge(node.getId(), child.getId());
                }
            }
        }

        graph.endUpdate();
    }

    public void addPhylogeneticTree() {

        try {
            //TreeMain tm = new TreeMain();
            setup();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * Implementing phylogenetic tree here.
     */
    TreeItem current;
    int current_depth = 0;

    void setup() throws IOException {
        File f = new File("src/main/resources/340tree.rooted.TKK.nwk");
        BufferedReader r = new BufferedReader(new FileReader(f));
        String t = r.readLine();
        current = TreeParser.parse(t);

        Model model = graph.getModel();
        graph.beginUpdate();
        int i = 1;

        Queue<TreeItem> q = new LinkedList<>();
        ArrayList<Integer> done = new ArrayList<>();

        System.out.println((current.getName()));
        q.add(current);
        model.addCell(i, current.getName(), CellType.PHYLOGENETIC);
        System.out.println("Cell added: " + i);

        while (!q.isEmpty()) {
            current = q.poll();
            //From node
            int j = i;

            for (TreeItem child : current.getChildren()) {
                model.addCell(++i, child.getName(), CellType.PHYLOGENETIC);
                System.out.println("Cell added: " + i);
                model.addEdge(j, i);
                System.out.println("Link added: " + j + ", " + i);
                //System.out.println("Link added: " + j +  ", "+ i);
                q.add(child);
            }
            //done.add(i);
           // i++;
        }

        graph.endUpdate();
    }

    /**
     * Getter for the graph.
     *
     * @return the graph.
     */
    public Graph getGraph() {
        return graph;
    }


}
