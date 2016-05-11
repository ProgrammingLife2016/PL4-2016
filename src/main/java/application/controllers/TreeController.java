package application.controllers;

import application.fxobjects.graph.cell.CellLayout;
import application.fxobjects.graph.cell.PhylogeneticLeafCell;
import application.fxobjects.phylogeny.TreeLayout;
import javafx.scene.control.ScrollPane;
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
        this.getRoot().setVbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);

        /*
        this.getRoot().addEventFilter(ScrollEvent.SCROLL, event -> {
            if(event.getDeltaY() != 0) {
                event.consume();
            }
        });
        */

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
        root.getChildren().add(new PhylogeneticLeafCell(1, "test"));
        CellLayout layout = new TreeLayout(tree.getRoot(), 20, (int) (screenSize.getHeight()-25)/2);
        layout.execute();
        this.getRoot().setContent(root);
    }
}
