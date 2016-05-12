package application.controllers;

import core.graph.PhylogeneticTree;
import core.graph.Graph;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import javafx.fxml.FXML;
import javafx.geometry.Rectangle2D;
import javafx.scene.control.MenuBar;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Screen;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * MainController for GUI.
 */
public class MainController extends Controller<BorderPane> {

    @FXML
    ScrollPane screen;
    @FXML
    MenuBar menuBar;

    Rectangle2D screenSize;
    /**
     * Constructor to create MainController based on abstract Controller.
     */
    public MainController() {
        super(new BorderPane());
        loadFXMLfile("/fxml/main.fxml");
    }

    /**
     * Initialize method for the controller.
     *
     * @param location  location for relative paths.
     * @param resources resources to localize the root object.
     */
    @SuppressFBWarnings("URF_UNREAD_FIELD")
    public final void initialize(URL location, ResourceBundle resources) {
        screenSize = Screen.getPrimary().getVisualBounds();
        createMenu();

//        this.getRoot().getStylesheets().add("application/css/main.css");
//        this.getRoot().getStyleClass().add("root");
    }

    /**
     * Method to fill the graph.
     */
    public void fillGraph() {
        Graph graph = new Graph();
        GraphController graphController = new GraphController(graph);
        screen = graphController.getRoot();
        this.getRoot().setCenter(screen);
    }

    /**
     * Method to fill the phylogenetic tree.
     */
    public void fillTree() {
        try {
            PhylogeneticTree pt = new PhylogeneticTree();
            pt.setup();
            TreeController treeController = new TreeController(pt);
            screen = treeController.getRoot();
            this.getRoot().setCenter(screen);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Method to create the menu bar.
     */
    public void createMenu() {
        MenuFactory menuFactory = new MenuFactory(this);
        menuBar = menuFactory.createMenu(menuBar);
        this.getRoot().setTop(menuBar);
    }

    /**
     * Switches the scene to the graph view.
     */
    public void switchScene() {
        fillGraph();
    }

    /**
     * Switches the scene to the phylogenetic tree view.
     */
    public void switchTreeScene() {
        fillTree();
    }

}
