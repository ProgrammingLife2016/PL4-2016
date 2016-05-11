package application.controllers;

import application.TreeMain;
import application.fxobjects.graph.cell.*;
import core.graph.Graph;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Screen;
import java.net.URL;
import java.util.LinkedList;
import java.util.List;
import java.util.ResourceBundle;

import net.sourceforge.olduvai.treejuxtaposer.drawer.Tree;
import net.sourceforge.olduvai.treejuxtaposer.drawer.TreeNode;

/**
 * Created by Niek van der Laan on 5-9-2016
 */
public class TreeController extends Controller<ScrollPane> {
    private TreeMain tm;
    private MainController mainController;
    //private GraphMouseHandling graphMouseHandling;

    private javafx.geometry.Rectangle2D screenSize;

    public TreeController(MainController controller, TreeMain tm) {
        super(new ScrollPane());
        this.tm = tm;
        this.mainController = controller;
        //this.graphMouseHandling = new GraphMouseHandling(graph);
        this.screenSize = Screen.getPrimary().getVisualBounds();
        this.getRoot().setHbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);
        this.getRoot().setVbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);

        this.getRoot().addEventFilter(ScrollEvent.SCROLL, event -> {
            if (event.getDeltaY() != 0) {
                event.consume();
            }
        });

        init();
    }

    public TreeMain getTM() { return tm; }


    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    public void init() {
        System.out.println("init");
        AnchorPane root = new AnchorPane();

        root.getChildren().addAll(tm.getModel().getAddedCells());


        System.out.println(tm.getModel().getAllCells());
        CellLayout layout = new BaseLayout(tm.getModel(), 20, (int) (screenSize.getHeight() - 25) / 2);
        tm.endUpdate();
        layout.execute();

        this.getRoot().setContent(root);
    }
}
