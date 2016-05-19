package application.controllers;

import core.graph.Graph;
import core.graph.PhylogeneticTree;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.geometry.Rectangle2D;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuBar;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.Screen;

import java.io.IOException;
import java.net.URL;
import java.util.Comparator;
import java.util.List;
import java.util.ResourceBundle;

/**
 * MainController for GUI.
 */
public class MainController extends Controller<BorderPane> {

    @FXML
    private ScrollPane screen;
    @FXML
    private MenuBar menuBar;
    @FXML
    ListView list;
    @FXML
    TextFlow infoList;
    @FXML
    VBox listVBox;
    @FXML
    Text id;
    @FXML
    ScrollPane infoScroller;

    private int currentView = 0;
    private GraphController graphController;


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
        createInfoList("");
    }

    /**
     * On the right side of the screen, create a VBox showing:
     * - A list with all genome strains.
     * - A box with info on a selected node.
     */
    private void createInfoList(String info) {
        listVBox = new VBox();
        infoScroller = new ScrollPane();

        infoScroller.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        infoScroller.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        infoScroller.prefHeightProperty().bind(listVBox.heightProperty());
        infoScroller.prefWidth(screenSize.getWidth() / 5);

        if (info == "") {
            createList();
        }

        createNodeInfo();

        infoScroller.setContent(infoList);
        listVBox.getChildren().addAll(list, infoScroller);

    }

    /**
     * Create a list on the right side of the screen with all genomes.
     */
    private void createList() {
        list = new ListView<>();
        list.setPlaceholder(new Label("No Genomes Loaded."));
        list.prefHeightProperty().bind(listVBox.heightProperty());
        list.setOnMouseClicked(event -> {
            if (!(list.getSelectionModel().getSelectedItem() == (null))) {
                fillGraph(list.getSelectionModel().getSelectedItem());
            }
            System.out.println(list.getSelectionModel().getSelectedItem());
        });
    }

    /**
     * Create an info panel to show the information on a node.
     */
    private void createNodeInfo() {
        infoList = new TextFlow();
        infoList.prefHeightProperty().bind(infoScroller.heightProperty());
        infoList.prefWidthProperty().bind(infoScroller.widthProperty());

        id = new Text();
        id.setText("Select Node to view info");

        infoList.getChildren().addAll(id);
    }

    /**
     * Modify the information of the Node.
     *
     * @param iD desired info.
     */
    public void modifyNodeInfo(String iD) {
        id.setText(iD);
    }

    /**
     * Method to fill the graph.
     *
     * @param ref the reference string.
     */
    public void fillGraph(Object ref) {
        Graph graph = new Graph();
        graphController = new GraphController(graph, ref, this, currentView);
        screen = graphController.getRoot();

        this.getRoot().setCenter(screen);

        List<String> genomes = graphController.getGenomes();
        genomes.sort(Comparator.naturalOrder());
        list.setItems(FXCollections.observableArrayList(genomes));

        showListVBox();
    }

    /**
     * Method to fill the phylogenetic tree.
     */
    public void fillTree() {
        try {
            PhylogeneticTree pt = new PhylogeneticTree();
            pt.setup();
            TreeController treeController = new TreeController(pt, this);
            screen = treeController.getRoot();
            this.getRoot().setCenter(screen);
        } catch (IOException e) {
            e.printStackTrace();
        }
        hideListVBox();
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
     *
     * @param delta the diff in view to apply.
     */
    public void switchScene(int delta) {
        currentView += delta;

    }

    /**
     * Switches the scene to the phylogenetic tree view.
     */
    public void switchTreeScene() {
        fillTree();
    }

    /**
     * Show the info panel.
     */
    private void showListVBox() {
        this.getRoot().setRight(listVBox);
    }

    /**
     * Hide the info panel.
     */
    private void hideListVBox() {
        this.getRoot().getChildren().remove(listVBox);
    }

    /**
     * Getter method for the graphController.
     *
     * @return the graphController.
     */
    public GraphController getGraphController() {
        return graphController;
    }
}
