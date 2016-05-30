package application.controllers;

import application.fxobjects.cell.graph.BubbleCell;
import application.fxobjects.cell.graph.CollectionCell;
import application.fxobjects.cell.graph.IndelCell;
import application.fxobjects.cell.graph.RectangleCell;
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

    private HBox legend;
    private VBox listVBox;
    private ListView list;
    private ScrollPane infoScroller;
    private int currentView;
    private ListFactory listFactory;

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
        createMenu();
    }

    private void initGUI() {
        createZoomBoxAndLegend();
        createList();
        this.getRoot().setCenter(graphController.getRoot());
        graphController.takeSnapshot();
        graphController.initKeyHandler();

        setListItems();
        this.getRoot().setRight(listVBox);
    }

    private void setListItems() {
        List<String> genomes = graphController.getGenomes();
        genomes.sort(Comparator.naturalOrder());
        list.setItems(FXCollections.observableArrayList(genomes));
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
        graphController.getGraph().setCurrentGenomes(selectedGenomes);

        graphController.update(ref, currentView);

        initGUI();

    }

    /**
     * If selections are made in the phylogenetic tree,
     * this method will visualize/highlight them specifically.
     *
     * @param s a List of selected strains.
     */
    public void soloStrainSelection(List<String> s) {
        fillGraph(s.get(0), new ArrayList<>());
        initGUI();
    }

    /**
     * If selections are made in the phylogenetic tree,
     * this method will visualize/highlight them specifically.
     *
     * @param s a List of selected strains.
     */
    public void strainSelection(List<String> s) {
        fillGraph(null, s);

        initGUI();
        setListItems();
    }

    /**
     * Create the HBox containing the zoom box and legend.
     */
    private void createZoomBoxAndLegend() {
        HBox hbox = new HBox();

        graphController.getZoomController().createZoomBox();
        StackPane zoombox = graphController.getZoomController().getZoomBox().getZoomBox();
        graphController.initKeyHandler();

        createLegend();
        legend.setAlignment(Pos.CENTER_RIGHT);
        hbox.setAlignment(Pos.CENTER);

        hbox.getChildren().addAll(zoombox, legend);
        this.getRoot().setBottom(hbox);
    }

    private void createLegend() {
        LegendFactory legendFactory = new LegendFactory(this);
        legend = legendFactory.createLegend();
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
    private void createMenu() {
        MenuFactory menuFactory = new MenuFactory(this);
        menuBar = menuFactory.createMenu(menuBar);
        this.getRoot().setTop(menuBar);
    }

    private void createList() {
        listFactory = new ListFactory(this);
        listVBox = listFactory.createInfoList("");
        infoScroller = listFactory.getInfoScroller();
        list = listFactory.getList();

        list.setOnKeyPressed(graphController.getZoomController().getZoomBox().getKeyHandler());
        infoScroller.setOnKeyPressed(graphController.getZoomController()
                .getZoomBox().getKeyHandler());

        list.setOnMouseClicked(event -> {
            if (!(list.getSelectionModel().getSelectedItem() == null)) {
                fillGraph(list.getSelectionModel().getSelectedItem(), new ArrayList<>());
            }
        });

        this.getRoot().setRight(listVBox);
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

    public ListFactory getListFactory() {
        return listFactory;
    }
}
