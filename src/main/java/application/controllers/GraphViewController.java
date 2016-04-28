package application.controllers;

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

    public void addGraphComponents() {
        Model model = graph.getModel();

        graph.beginUpdate();
        Parser parser = new Parser();
        nodeMap = parser.readGFA("src/main/resources/TB10.gfa");

        Node root = (nodeMap.get(1));
        model.addCell(root.getId(), root.getSequence(), CellType.RECTANGLE);

        for(int i = 1; i<=nodeMap.size();i++) {

            int numberOfLinks = nodeMap.get(i).getLinks().size();
            for(int j:nodeMap.get(i).getLinks()) {
                //Add next cell
                if(numberOfLinks==1) {
                    model.addCell(nodeMap.get(j).getId(), nodeMap.get(j).getSequence(), CellType.RECTANGLE);
                }
                else
                {
                    model.addCell(nodeMap.get(j).getId(), nodeMap.get(j).getSequence(), CellType.TRIANGLE);
                }
                //Add link from current cell to next cell
                model.addEdge(nodeMap.get(i).getId(), nodeMap.get(j).getId());
            }

        }

        //dfs(root,1,new boolean[nodeMap.size()],model);
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

    public Graph getGraph() {
        return graph;
    }



}
