package application.controllers;

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
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
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
    private Rectangle2D screenSize;
    private int maxWidth;
    private int maxHeight;

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
        this.maxWidth = 0;
        this.graphMouseHandling = new GraphMouseHandling(m);
        this.screenSize = Screen.getPrimary().getVisualBounds();
        this.maxHeight = (int) screenSize.getHeight();
        this.getRoot().setHbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);
        this.getRoot().setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);


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

    public ZoomController getZoomController() { return zoomController; }

    @Override
    public void initialize(URL location, ResourceBundle resources) { }

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

        graph.getModel().getAddedCells().forEach(graphMouseHandling::setMouseHandling);

        graph.endUpdate();
        GraphLayout layout = new GraphLayout(graph.getModel(), 20,
                (int) (screenSize.getHeight() - 25) / 2);
        layout.execute();
        maxWidth = (int) layout.getMaxWidth();
        this.getRoot().setContent(root);

    }

    public void initKeyHandler() {
        this.getRoot().setOnKeyPressed(zoomController.getZoomBox().getKeyHandler());
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
     * @throws IOException Throw exception on write failure.
     */
    public void takeSnapshot() throws IOException {
        WritableImage image = new WritableImage(maxWidth + 50, (int) screenSize.getHeight());
        WritableImage snapshot = this.getRoot().getContent().snapshot(new SnapshotParameters(), image);

        File output = new File("/snapshot.png");
        output.deleteOnExit();
        ImageIO.write(SwingFXUtils.fromFXImage(snapshot, null), "png", output);
    }
}
