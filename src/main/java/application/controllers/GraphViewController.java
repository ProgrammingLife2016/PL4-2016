package application.controllers;

import application.TreeItem;
import application.TreeMain;
import application.TreeParser;
import application.fxobjects.ZoomBox;
import application.fxobjects.graph.Graph;
import application.fxobjects.graph.Model;
import application.fxobjects.graph.cell.BaseLayout;
import application.fxobjects.graph.cell.CellLayout;
import application.fxobjects.graph.cell.CellType;
import core.Node;
import core.Parser;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.MenuBar;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
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
    private HashMap<Integer, Node> nodeMap;

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
        loadFXMLfile("/application/fxml/graphview.fxml");
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

        addGraphComponents();
        //addPhylogeneticTree();
        CellLayout layout = new BaseLayout(graph, 100);
        layout.execute();

        this.getRoot().getChildren().addAll(root);
    }

    /**
     * Method that adds all nodes to the Model.
     */
    public void addGraphComponents() {
        Model model = graph.getModel();

        graph.beginUpdate();
        Parser parser = new Parser();
        nodeMap = parser.readGFA("src/main/resources/TB10.gfa");

        Node root = (nodeMap.get(1));
        model.addCell(root.getId(), root.getSequence(), CellType.RECTANGLE);

        Object r = root.getGenomes().get(0);

        for (int i = 1; i <= nodeMap.size(); i++) {
            Collection<Integer> start = new ArrayList<>();
            int numberOfLinks = nodeMap.get(i).getLinks().size();
            for (int j : nodeMap.get(i).getLinks()) {
                //Add next cell
                if (nodeMap.get(j).getGenomes().contains(r)) {
                    model.addCell(nodeMap.get(j).getId(), nodeMap.get(j).getSequence(), CellType.RECTANGLE);
                } else {
                    model.addCell(nodeMap.get(j).getId(), nodeMap.get(j).getSequence(), CellType.TRIANGLE);
                }
                //Add link from current cell to next cell

                Collection<Integer> end = new ArrayList<>();

                model.addEdge(nodeMap.get(i).getId(), nodeMap.get(j).getId(), intersection(nodeMap.get(i).getGenomes(),nodeMap.get(j).getGenomes()));
            }

        }

        //dfs(root,1,new boolean[nodeMap.size()],model);
        graph.endUpdate();
    }

    private int intersection(List<String> l1, List<String> l2) {
        int i = 0;
        System.out.println(l1);
        System.out.println(l2);
        for (String s:l1) {
            if(l2.contains(s)) {
                i++;
            }
        }
        System.out.println(i);
        return i;

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
        model.addCell(i,current.getName(),CellType.PHYLOGENETIC);
        System.out.println("Cell added: " + i);

        while (!q.isEmpty()) {
            current = q.poll();
            //From node
            int j = i;

            for (TreeItem child : current.getChildren()) {
                model.addCell(++i, child.getName(), CellType.PHYLOGENETIC);
                System.out.println("Cell added: " + i);
                model.addEdge(j, i, 1);
                System.out.println("Link added: " + j +  ", "+ i);
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
