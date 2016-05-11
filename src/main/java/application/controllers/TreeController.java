package application.controllers;

import application.fxobjects.graph.cell.Cell;
import application.fxobjects.graph.cell.PhylogeneticCell;
import application.fxobjects.graph.cell.RectangleCell;
import core.graph.Graph;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Screen;
import java.net.URL;
import java.util.LinkedList;
import java.util.ResourceBundle;

import net.sourceforge.olduvai.treejuxtaposer.drawer.Tree;
import net.sourceforge.olduvai.treejuxtaposer.drawer.TreeNode;

/**
 * Created by Niek van der Laan on 5-9-2016
 */
public class TreeController extends Controller<ScrollPane> {
    private Tree tree;
    private MainController mainController;
    //private GraphMouseHandling graphMouseHandling;

    private javafx.geometry.Rectangle2D screenSize;

    public TreeController(MainController controller, Tree tree) {
        super(new ScrollPane());
        this.tree = tree;
        this.mainController = controller;
        //this.graphMouseHandling = new GraphMouseHandling(graph);
        this.screenSize = Screen.getPrimary().getVisualBounds();
        this.getRoot().setHbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);
        this.getRoot().setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);

        this.getRoot().addEventFilter(ScrollEvent.SCROLL, event -> {
            if (event.getDeltaY() != 0) {
                event.consume();
            }
        });

        init();
    }

    public Tree getTree() { return tree; }


    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    public void init() {
        System.out.println("init");
        AnchorPane root = new AnchorPane();
        LinkedList<TreeNode> leaves = tree.getLeaves(tree.getRoot());

        for (TreeNode leaf : leaves) {
            Cell cell = new RectangleCell(leaf.getKey(), leaf.getName());
            cell.relocate(200, 200);
            root.getChildren().add(cell);
        }

        this.getRoot().setContent(root);
    }
}
