package application.controllers;

import application.fxobjects.cell.Cell;
import application.fxobjects.cell.layout.GraphLayout;
import core.graph.Graph;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import javafx.embed.swing.SwingFXUtils;
import javafx.geometry.Rectangle2D;
import javafx.scene.SnapshotParameters;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.WritableImage;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Screen;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

/**
 * Created by Daphne van Tetering on 4-5-2016.
 */
@SuppressWarnings("PMD.UnusedPrivateField")
public class GraphController extends Controller<ScrollPane> {
    private Graph graph;
    private ZoomController zoomController;
    private GraphMouseHandling graphMouseHandling;

    /**
     * Constructor method for this class.
     *
     * @param g     the graph.
     * @param ref   the reference string.
     * @param m     the mainController.
     * @param depth the depth to draw.
     */
    @SuppressFBWarnings("URF_UNREAD_FIELD")
    public GraphController(Graph g, Object ref, MainController m, int depth) {
        super(new ScrollPane());
        this.graph = g;
        this.zoomController = new ZoomController();
        this.graphMouseHandling = new GraphMouseHandling(m);
        this.getRoot().setHbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);
        this.getRoot().setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);

        this.getRoot().setOnKeyPressed(zoomController.getZoomBox().getKeyHandler());

        this.getRoot().addEventFilter(ScrollEvent.SCROLL, event -> {
            if (event.getDeltaY() != 0) {
                event.consume();
            }
        });

        try {
            init(ref, depth);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Getter method for the graph.
     *
     * @return the graph.
     */
    public Graph getGraph() {
        return graph;
    }

    public ZoomController getZoomController() {
        return zoomController;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
    }

    /**
     * Init method for this class.
     *
     * @param ref   the reference string.
     * @param depth the depth to draw.
     * @throws IOException Throw exception on read GFA read failure.
     */
    public void init(Object ref, int depth) throws IOException {
        AnchorPane root = new AnchorPane();

        graph.addGraphComponents(ref, depth);

        // add components to graph pane
        root.getChildren().addAll(graph.getModel().getAddedEdges());
        root.getChildren().addAll(graph.getModel().getAddedCells());

        List<Cell> list = graph.getModel().getAddedCells();

        for (Cell c : list) {
            graphMouseHandling.setMouseHandling(c);
        }

        graph.endUpdate();


        this.getRoot().setContent(root);

        //takeSnapshot();
    }

    /**
     * Getter method for the genomes.
     *
     * @return the list of genomes.
     */
    public List<String> getGenomes() {
        return graph.getGenomes();
    }

    /**
     * Method take a snapshot of the current graph.
     * ---- not used because of a null pointer ---
     * @TODO: fix.
     *
     * @throws IOException Throw exception on write failure.
     */
    public void takeSnapshot() throws IOException {
        SnapshotParameters snapshotParameters = new SnapshotParameters();
        //WritableImage image = new WritableImage((int)maxWidth + 50, (int) screenSize.getHeight());
        WritableImage snapshot = this.getRoot().snapshot(
                snapshotParameters, new WritableImage(getGraph().getModel().getWidth() + 50, getGraph().getModel().getHeight()));

        File output = new File("snapshot.png");
        ImageIO.write(SwingFXUtils.fromFXImage(snapshot, null), "png", output);
    }


}
