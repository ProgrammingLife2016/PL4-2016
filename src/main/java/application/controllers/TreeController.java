package application.controllers;

import application.TreeItem;
import application.fxobjects.graph.cell.BaseLayout;
import application.fxobjects.graph.cell.Cell;
import application.fxobjects.graph.cell.CellLayout;
import application.fxobjects.graph.cell.PhylogeneticCell;
import application.fxobjects.phylogeny.TreeLayout;
import javafx.event.EventHandler;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.AnchorPane;
import core.graph.Graph;
import javafx.stage.Screen;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by Niek van der Laan on 5-9-2016
 */
public class TreeController extends Controller<ScrollPane> {
    private TreeItem root;
    private MainController mainController;
    //private GraphMouseHandling graphMouseHandling;

    private javafx.geometry.Rectangle2D screenSize;

    public TreeController(MainController controller, TreeItem root) {
        super(new ScrollPane());
        this.root = root;
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

    public TreeItem getRootNode() { return root; }


    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    public void init() {
        System.out.println("init");
        AnchorPane root = new AnchorPane();

        root.getChildren().add(new PhylogeneticCell(1, "test"));
        CellLayout layout = new TreeLayout(this.root, 20,(int) (screenSize.getHeight()-25)/2);
        layout.execute();
        this.getRoot().setContent(root);
    }
}
