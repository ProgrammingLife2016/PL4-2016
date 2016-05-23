package application.controllers;

import application.fxobjects.cell.Cell;
import application.fxobjects.cell.Edge;
import application.fxobjects.cell.layout.CellLayout;
import core.graph.PhylogeneticTree;
import application.fxobjects.cell.layout.TreeLayout;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.AnchorPane;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

/**
 * Class responsible for setting up the scroll pane containing the phylogenetic tree.
 * Created by Niek van der Laan on 5-9-2016
 */
public class TreeController extends Controller<ScrollPane> {
    private PhylogeneticTree pt;
    private TreeMouseHandling treeMouseHandling;

    /**
     * Class constructor.
     *
     * @param pt A phylogenetic tree.
     */
    public TreeController(PhylogeneticTree pt, MainController m) {
        super(new ScrollPane());
        this.pt = pt;
        this.treeMouseHandling = new TreeMouseHandling(m);
        this.getRoot().setHbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);
        this.getRoot().setVbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);

        this.getRoot().addEventFilter(ScrollEvent.SCROLL, event -> {
            if (event.getDeltaY() != 0) {
                event.consume();
            }
        });

        init();
    }

    /**
     * Get the phylogenetic tree.
     *
     * @return The phylogenetic tree.
     */
    public PhylogeneticTree getPT() {
        return pt;
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    /**
     * Add cells from the model to the gui.
     */
    public void init() {
        AnchorPane root = new AnchorPane();
        CellLayout layout = new TreeLayout(pt.getModel(), 30);
        layout.execute();

        List<Cell> nodeList = pt.getModel().getAddedCells();
        List<Edge> edgeList = pt.getModel().getAddedEdges();

        nodeList.forEach(treeMouseHandling::setMouseHandling);
        edgeList.forEach(treeMouseHandling::setMouseHandling);

        // Add all cells and edges to the anchor pane
        root.getChildren().addAll(pt.getModel().getAddedCells());
        root.getChildren().addAll(pt.getModel().getAddedEdges());
        this.getRoot().setContent(root);
    }
}
