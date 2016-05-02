package application.controllers;

import application.fxobjects.ZoomBox;
import application.fxobjects.graph.Graph;
import application.fxobjects.graph.Model;
import application.fxobjects.graph.cell.BaseLayout;
import application.fxobjects.graph.cell.CellLayout;
import application.fxobjects.graph.cell.CellType;
import core.GraphReducer;
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

import java.net.URL;
import java.util.HashMap;
import java.util.ResourceBundle;

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

        for (int i = 0; i < 10; i++) {
            System.out.println(nodeMap.size());
            GraphReducer.collapse(nodeMap);
            System.out.println("--------------------");
        }

        Node root = (nodeMap.get(1));
        model.addCell(root.getId(), root.getSequence(), CellType.RECTANGLE);

        for (int idx = 1; idx <= nodeMap.size(); idx++) {
            Node node = nodeMap.get(idx);
            if (node != null) {
                int numberOfLinks = node.getLinks().size();

                for (int linkId : node.getLinks()) {
                    Node child = nodeMap.get(linkId);

                    if (child != null) {
                        //Add next cell
                        if (numberOfLinks == 1) {
                            model.addCell(child.getId(), child.getSequence(), CellType.RECTANGLE);
                        } else {
                            model.addCell(child.getId(), child.getSequence(), CellType.TRIANGLE);
                        }
                        //Add link from current cell to next cell
                        model.addEdge(node.getId(), child.getId());
                    }
                }
            }
        }

        graph.endUpdate();
    }

    /**
     * A simple Depth Frist implementation to display every Node in our Graph.
     * @param n Current Node.
     * @param ni Integer to find current Node in map.
     * @param marked Keep track of Nodes that are already added in case of loops.
     * @param m The model to add the Nodes to.
     */
    private void dfs(Node n,int ni,boolean[] marked, Model m){
        if(n == null && ni>0) return;
        marked[ni-1] = true;

        //for every child
        for(int i: n.getLinks())
        {
            Node next = nodeMap.get(i);
            //if childs state is not visited then recurse

            if(!marked[i - 1])
            {
                m.addCell(next.getId(), next.getSequence(),CellType.RECTANGLE);
                m.addEdge(n.getId(), next.getId());
                dfs(next, i, marked, m);
                marked[i-1] =true;
            }
            else
            {
                m.addEdge(n.getId(), next.getId());
            }
        }
    }

    /**
     * Getter for the graph.
     * @return the graph.
     */
    public Graph getGraph() {
        return graph;
    }



}
