package application.controllers;

import application.fxobjects.ZoomBox;
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
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.Screen;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
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

    private ListView list;
    private TextFlow infoList;
    private VBox listVBox;
    private Text id;
    private ScrollPane infoScroller;
    private int currentView = 9;
    private GraphController graphController;
    private TreeController treeController;
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
    }

    private void createInfoList(String info) {
        listVBox = new VBox();
        infoScroller = new ScrollPane();

        listVBox.setPrefWidth(248.0);
        listVBox.setMaxWidth(248.0);

        infoScroller.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        infoScroller.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        infoScroller.prefHeightProperty().bind(listVBox.heightProperty());
        infoScroller.prefWidth(screenSize.getWidth() / 5);

        if (info.isEmpty()) {
            createList();
        }

        createNodeInfo();

        infoScroller.setContent(infoList);
        listVBox.getChildren().addAll(list, infoScroller);
    }

    /**
     * Create a list on the right side of the screen with all genomes.
     */
    public void createList() {
        list = new ListView<>();
        list.setPlaceholder(new Label("No Genomes Loaded."));
        list.prefHeightProperty().bind(listVBox.heightProperty());
        list.prefWidthProperty().bind(listVBox.widthProperty());

        list.setOnKeyPressed(graphController.getZoomController().getZoomBox().getKeyHandler());
        infoScroller.setOnKeyPressed(graphController.getZoomController()
                .getZoomBox().getKeyHandler());

        list.setOnMouseClicked(event -> {
            if (!(list.getSelectionModel().getSelectedItem() == null)) {
                fillGraph(list.getSelectionModel().getSelectedItem(), new ArrayList<>());
            }
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
     * @param id desired info.
     */
    public void modifyNodeInfo(String id) {
        this.id.setText(id);
    }

    /**
     * Method to fill the graph.
     *
     * @param ref the reference string.
     */
    public void fillGraph(Object ref, List<String> selectedGenomes) {
        if (graphController == null) {
            Graph graph = null;
            try {
                graph = new Graph();
                graphController = new GraphController(graph, ref, this, currentView, selectedGenomes);
            } catch (IOException e) {
                e.printStackTrace();
            }

        } else {
            try {
                graphController.init(ref, currentView, selectedGenomes);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        screen = graphController.getRoot();
        this.getRoot().setCenter(screen);

        //!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
        //Image i = graphController.takeSnapshot();
//        Image i = graphController.getImage();
        graphController.getZoomController().createZoomBox();
        ZoomBox zoombox = graphController.getZoomController().getZoomBox();
//        zoombox.setFill(i);

        this.getRoot().setBottom(zoombox.getZoomBox());


        graphController.initKeyHandler();

        createInfoList("");

        List<String> genomes = graphController.getGenomes();
        genomes.sort(Comparator.naturalOrder());
        list.setItems(FXCollections.observableArrayList(genomes));

        showListVBox();
    }

    /**
     * If selections are made in the phylogenetic tree,
     * this method will visualize/highlight them specifically.
     *
     * @param s a List of selected strains.
     */
    public void soloStrainSelection(List<String> s) {
        //ToDo: add function to visualize only the selected strains.

        graphController.getGraph().setGenomes(new ArrayList<>());
        fillGraph(s.get(0), new ArrayList<>());
        System.out.println("Selected " + s.get(0 )+ "as a ref, drawing everything.");

        graphController.getZoomController().createZoomBox();
        StackPane zoombox = graphController.getZoomController().getZoomBox().getZoomBox();
        this.getRoot().setBottom(zoombox);

        graphController.initKeyHandler();

        createInfoList("");

        List<String> genomes = graphController.getGenomes();
        genomes.sort(Comparator.naturalOrder());
        list.setItems(FXCollections.observableArrayList(genomes));

        showListVBox();
    }

    /**
     * If selections are made in the phylogenetic tree,
     * this method will visualize/highlight them specifically.
     *
     * @param s a List of selected strains.
     */
    public void strainSelection(List<String> s) {
        //ToDo: add function to visualize only the selected strains.
        System.out.println("Show: " + s.toString());

        graphController.getGraph().phyloSelection(s);

        try {
            graphController.init(null, currentView, new ArrayList<>());
        } catch (IOException e) {
            e.printStackTrace();
        }

        screen = graphController.getRoot();

        this.getRoot().setCenter(screen);

//        try {
//            graphController.takeSnapshot();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

        graphController.getZoomController().createZoomBox();
        StackPane zoombox = graphController.getZoomController().getZoomBox().getZoomBox();
        this.getRoot().setBottom(zoombox);

        graphController.initKeyHandler();

        createInfoList("");

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
            treeController = new TreeController(pt, this,
                    this.getClass().getResourceAsStream("/metadata.xlsx"));
            screen = treeController.getRoot();
            this.getRoot().setCenter(screen);
            this.getRoot().setBottom(null);
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
        currentView = Math.max(0, currentView);

        currentView = Math.min(9, currentView);
        fillGraph(null, new ArrayList<>());

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

    /**
     * Getter method for the treeController.
     *
     * @return the treeController.
     */
    public TreeController getTreeController() {
        return treeController;
    }
    /**
     * Getter method for the MenuBar.
     *
     * @return the MenuBar.
     */
    public MenuBar getMenuBar() {
        return menuBar;
    }
}
