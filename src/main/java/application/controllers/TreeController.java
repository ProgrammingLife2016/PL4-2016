package application.controllers;

import application.fxobjects.graph.cell.Cell;
import application.fxobjects.graph.cell.CellLayout;
import core.graph.TreeMain;
import application.fxobjects.phylogeny.TreeLayout;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Screen;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by Niek van der Laan on 5-9-2016
 */
public class TreeController extends Controller<ScrollPane> {
    private TreeMain tm;
    private GraphMouseHandling graphMouseHandling;

    private javafx.geometry.Rectangle2D screenSize;

    /**
     * Class constructor.
     * @param tm
     */
    public TreeController(TreeMain tm) {
        super(new ScrollPane());
        this.tm = tm;
        this.graphMouseHandling = new GraphMouseHandling();
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

    /**
     * Add cells from the model to the gui.
     */
    public void init() {
        AnchorPane root = new AnchorPane();
        root.getChildren().addAll(tm.getModel().getAddedCells());

        for (Cell cell : tm.getModel().getAddedCells()) {
            graphMouseHandling.setMouseHandling(cell);
        }

        CellLayout layout = new TreeLayout(tm.getModel(), 50);
        tm.endUpdate();
        layout.execute();

        root.getChildren().addAll(tm.getModel().getAddedEdges());
        this.getRoot().setContent(root);
    }
}
