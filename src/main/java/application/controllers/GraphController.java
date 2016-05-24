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
import javax.swing.filechooser.FileSystemView;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import static application.controllers.WindowFactory.screenSize;

/**
 * Created by Daphne van Tetering on 4-5-2016.
 */
@SuppressWarnings("PMD.UnusedPrivateField")
public class GraphController extends Controller<ScrollPane> {
    private int maxWidth;
    private Graph graph;
    private ZoomController zoomController;
    private GraphMouseHandling graphMouseHandling;

    private String position;

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
        this.zoomController = new ZoomController(this);
        this.maxWidth = 0;
        this.graphMouseHandling = new GraphMouseHandling(m);
        this.getRoot().setHbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);
        this.getRoot().setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        this.position = "";

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

    /**
     * Getter method for the ZoomController
     * @return the ZoomController
     */
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

    }

    /**
     * Method to attach the keyHandler to the root of the Controller
     */
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
     * Getter method for the position of the generated
     * snapshot
     * @return the position of the snapshot
     */
    public String getPosition() {
        return position;
    }

    /**
     * Method take a snapshot of the current graph.
     *
     * @throws IOException Throw exception on write failure.
     */
    public void takeSnapshot() throws IOException {
        try {
            WritableImage image = new WritableImage(maxWidth + 50,
                    (int) screenSize.getHeight());
            WritableImage snapshot = this.getRoot().getContent().snapshot(
                    new SnapshotParameters(), image);

            String filePath = System.getProperty("user.home") + "/AppData/Local/Temp";
            File output = new File(filePath);
            File temp = File.createTempFile("snapshot", ".png", output);
            ImageIO.write(SwingFXUtils.fromFXImage(snapshot, null), "png", temp);

            position = temp.getName();
            temp.deleteOnExit();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
