package application.controllers;

import application.fxobjects.cell.graph.BubbleCell;
import application.fxobjects.cell.graph.CollectionCell;
import application.fxobjects.cell.graph.IndelCell;
import application.fxobjects.cell.graph.RectangleCell;
import core.graph.Graph;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuBar;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.Screen;

import java.net.URL;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.ResourceBundle;

/**
 * MainController for GUI.
 */
public class MainController extends Controller<BorderPane> {

    /**
     * The Controllers used in this Class.
     */
    private GraphController graphController;
    private TreeController treeController;

    /**
     * FXML Objects.
     */
    @FXML
    private ScrollPane screen;
    @FXML
    private MenuBar menuBar;
    private ListView list;
    private TextFlow infoList;
    private VBox listVBox;
    private Text id;
    private ScrollPane infoScroller;
    private int currentView;
    Rectangle2D screenSize;

    /**
     * Constructor to create MainController based on abstract Controller.
     */
    public MainController() {
        super(new BorderPane());
        loadFXMLfile("/fxml/main.fxml");

        /**
         * Create a new GraphController.
         */
        graphController = new GraphController(this);
        //@ToDo This shouldnt be here.
        currentView = graphController.getGraph().getLevelMaps().size() - 1;

        /**
         * Fll the graph.
         */
        fillGraph(null, new ArrayList<>());

        /**
         * Create a new TreeController.
         */
        treeController = new TreeController(this,
                this.getClass().getResourceAsStream("/metadata.xlsx"));
    }


    /**
     * Getter method for the current view level.
     *
     * @return the current view level.
     */
    public int getCurrentView() {
        return currentView;
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
                graphController.takeSnapshot();
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
     * Create a legend info panel that shows the meaning of different types of cells.
     *
     * @return A legend panel.
     */
    private Pane createLegend() {
        final VBox col1 = new VBox();
        col1.getChildren().add(new RectangleCell(0, ""));
        col1.getChildren().add(new BubbleCell(0, "N"));
        col1.getChildren().add(new IndelCell(0, "N"));
        col1.getChildren().add(new CollectionCell(0, "N"));

        final VBox col2 = new VBox();
        col2.getChildren().add(new Text("  -  Basic Node"));
        col2.getChildren().add(new Text("  -  Bubble Node"));
        col2.getChildren().add(new Text("  -  Indel Node"));
        col2.getChildren().add(new Text("  -  Collection Node"));

        final VBox col3 = new VBox();
        col3.getChildren().add(new Text(""));
        col3.getChildren().add(new Text("  -  Contains N other nodes"));
        col3.getChildren().add(new Text("  -  Contains N other nodes"));
        col3.getChildren().add(new Text("  -  Contains N (horizontally collapsed) nodes"));

        return new StackPane(new HBox(col1, col2, col3));
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
     * @param ref             the reference string.
     * @param selectedGenomes the genomes to display.
     */
    public void fillGraph(Object ref, List<String> selectedGenomes) {
        /**
         * Apply the new Selected Genomes.
         */
        graphController.getGraph().setGenomes(selectedGenomes);

        //@ToDo: Implement a new method that loads the graph correctly.
        // Maybe this is it.
        graphController.init(ref, currentView);

        screen = graphController.getRoot();
        this.getRoot().setCenter(screen);

        graphController.takeSnapshot();


        createZoomBoxAndLegend();

        //TODO: Please do not remove line below
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
        graphController.getGraph().setGenomes(new ArrayList<>());
        fillGraph(s.get(0), new ArrayList<>());

        createZoomBoxAndLegend();
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
        System.out.println(s.toString());
        graphController.getGraph().reset();
        fillGraph(null,s);


//        Graph graph = null;
//        if (graphController == null) {
//            graph = new Graph();
//        } else {
//            graph = graphController.getGraph();
//        }
//        graph.phyloSelection(s);
//        //@TODO No new GraphController should be made.
////        graphController = new GraphController(graph, graph.getCurrentRef(), this, currentView, s);

        screen = graphController.getRoot();
        this.getRoot().setCenter(screen);

        graphController.takeSnapshot();


        createZoomBoxAndLegend();
        graphController.initKeyHandler();

        createInfoList("");

        List<String> genomes = graphController.getGraph().getGenomes();
        genomes.sort(Comparator.naturalOrder());
        list.setItems(FXCollections.observableArrayList(genomes));

        showListVBox();
    }

    /**
     * Create the HBox containing the zoom box and legend.
     */
    private void createZoomBoxAndLegend() {
        graphController.getZoomController().createZoomBox();

        HBox hbox = new HBox();
        StackPane zoombox = graphController.getZoomController().getZoomBox().getZoomBox();
        StackPane legend = new StackPane(createLegend());
        legend.setAlignment(Pos.CENTER_RIGHT);
        hbox.setAlignment(Pos.CENTER);

        hbox.getChildren().addAll(zoombox, legend);
        this.getRoot().setBottom(hbox);
    }

    /**
     * Method to fill the phylogenetic tree.
     */
    public void fillTree() {
        screen = treeController.getRoot();
        this.getRoot().setCenter(screen);
        this.getRoot().setBottom(null);
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
        currentView = Math.min(graphController.getGraph().getLevelMaps().size() - 1, currentView);
        fillGraph(graphController.getGraph().getCurrentRef(),
                graphController.getGraph().getGenomes());
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
