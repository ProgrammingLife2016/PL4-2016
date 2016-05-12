package application.controllers;

import application.fxobjects.graph.cell.Cell;
import application.fxobjects.graph.cell.CellLayout;
import core.graph.PhylogeneticTree;
import application.fxobjects.phylogeny.TreeLayout;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.AnchorPane;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Class responsible for settig up the scroll pane containing the phylogenetic tree.
 * Created by Niek van der Laan on 5-9-2016
 */
public class TreeController extends Controller<ScrollPane> {
    private PhylogeneticTree pt;
    private GraphMouseHandling graphMouseHandling;

    /**
     * Class constructor.
     * @param pt    A phylogenetic tree.
     */
    public TreeController(PhylogeneticTree pt) {
        super(new ScrollPane());
        this.pt = pt;
        this.graphMouseHandling = new GraphMouseHandling();
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
     * @return The phylogenetic tree.
     */
    public PhylogeneticTree getPT() { return pt; }


    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    /**
     * Add cells from the model to the gui.
     */
    public void init() {
        AnchorPane root = new AnchorPane();
        root.getChildren().addAll(pt.getModel().getAddedCells());

        for (Cell cell : pt.getModel().getAddedCells()) {
            graphMouseHandling.setMouseHandling(cell);
        }

        CellLayout layout = new TreeLayout(pt.getModel(), 50);
        pt.endUpdate();
        layout.execute();

        root.getChildren().addAll(pt.getModel().getAddedEdges());
        this.getRoot().setContent(root);
    }
}
