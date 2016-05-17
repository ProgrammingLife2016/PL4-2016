package application.controllers;

import core.graph.Graph;
import core.graph.PhylogeneticTree;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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
import java.util.ResourceBundle;

/**
 * MainController for GUI.
 */
public class MainController extends Controller<BorderPane> {

    @FXML
    ScrollPane screen;
    @FXML
    MenuBar menuBar;
    @FXML
    FlowPane pane;
    @FXML
    ListView list;
    @FXML
    TextFlow infoList;
    @FXML
    VBox listVBox;


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
        createInfoList();
        //createInfoList();

//        this.getRoot().getStylesheets().add("application/css/main.css");
//        this.getRoot().getStyleClass().add("root");
    }

//    private void createPane() {
//        pane = new FlowPane();
//        pane.getChildren();
//    }
//
//    private void createInfoList() {
//        infoList = new ListView<String>();
//        ObservableList<String> items = FXCollections.observableArrayList(
//                "Select a Node to show info");
//        list.setOnMouseClicked(event ->
//                System.out.println(list.getSelectionModel().getSelectedItem()));
//        list.setItems(items);
//        this.getRoot().setRight(list);
//    }

    /**
     * On the right side of the screen, create a VBox showing:
     *  - A list with all genome strains.
     *  - A box with info on a selected node.
     */
    private void createInfoList() {
        listVBox = new VBox();

        createList();
        createNodeInfo();

        listVBox.getChildren().addAll(list, infoList);
    }

    /**
     * Create a list on the right side of the screen with all genomes.
     */
    private void createList() {
        list = new ListView<>();
        list.setPlaceholder(new Label("No Genomes Loaded."));
        list.setOnMouseClicked(event -> {
            if(!(list.getSelectionModel().getSelectedItem()==(null))) {
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

        //ToDo: add more Text for extra info.
        Text seq = new Text();
        seq.setText("Test");

        infoList.getChildren().addAll(seq);
    }

    /**
     * Method to fill the graph.
     *
     * @param ref the reference string.
     */
    public void fillGraph(Object ref) {
        Graph graph = new Graph();
        GraphController graphController = new GraphController(graph, ref);
        screen = graphController.getRoot();
        this.getRoot().setCenter(screen);
        list.setItems(FXCollections.observableArrayList(graphController.getGenomes()));
        showListVBox();
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
     */
    public void switchScene() {
        fillGraph(null);
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
}
