package application.controllers;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;

import java.net.URL;
import java.util.*;

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
    private TextField textField;
    private Button searchButton;
    private Button deselectButton;

    /**
     * Constructor to create MainController based on abstract Controller.
     */
    public MainController() {
        super(new BorderPane());
        loadFXMLfile("/fxml/main.fxml");

        // Create the new GraphController
        graphController = new GraphController(this);

        currentView = graphController.getGraph().getLevelMaps().size() - 1;

        // Fill the graph
        fillGraph(null, new ArrayList<>());

        // Create the TreeController
        treeController = new TreeController(this);
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

    /**
     * Method to add items to the GUI
     */
    private void initGUI() {
        createZoomBoxAndLegend();
        createList();
        this.getRoot().setCenter(graphController.getRoot());
        graphController.takeSnapshot();
        graphController.initKeyHandler();

        setListItems();
        this.getRoot().setRight(listVBox);
    }

    /**
     * Method to fill the graph.
     *
     * @param ref             the reference string.
     * @param selectedGenomes the genomes to display.
     */
    public void fillGraph(Object ref, List<String> selectedGenomes) {
        // Apply the selected genomes
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
        graphController.getGraph().reset();
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

    /**
     * Method to create the Legend
     */
    private void createLegend() {
        LegendFactory legendFactory = new LegendFactory();
        legend = legendFactory.createLegend();
    }

    /**
     * Method to create the menu bar.
     */
    private void createMenu() {
        VBox vBox = new VBox();
        HBox hBox = new HBox();
        searchButton = new Button("Search Genome (In Tree)");
        deselectButton = new Button("Deselect All");

        textField = new TextField();
        searchButton.setOnAction(e -> {
            if (!textField.getText().isEmpty()) {
                treeController.applyCellHighlight(
                        treeController.getCellByName(
                                textField.textProperty().get().trim()));
                textField.setText("");
                fillTree();
            }
        });

        deselectButton.setOnAction(e -> {
            treeController.clearSelection();
            fillTree();

        });
        hBox.getChildren().addAll(textField, searchButton, deselectButton);

        MenuFactory menuFactory = new MenuFactory(this);
        menuBar = menuFactory.createMenu(menuBar);

        vBox.getChildren().addAll(menuBar, hBox);

        this.getRoot().setTop(vBox);
    }

    /**
     * Method to create the Info-list
     */
    private void createList() {
        listFactory = new ListFactory();
        listVBox = listFactory.createInfoList("");
        infoScroller = listFactory.getInfoScroller();
        list = listFactory.getList();

        list.setOnKeyPressed(graphController.getZoomController().getZoomBox().getKeyHandler());
        infoScroller.setOnKeyPressed(graphController.getZoomController()
                .getZoomBox().getKeyHandler());

        list.setOnMouseClicked(event -> {
            if (!(list.getSelectionModel().getSelectedItem() == null)) {
                graphController.getGraph().reset();
                getTextField().setText((String) list.getSelectionModel().getSelectedItem());

                fillGraph(list.getSelectionModel().getSelectedItem(), graphController.getGenomes());
                graphController.takeSnapshot();

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
     * Method to fill the phylogenetic tree.
     */
    public void fillTree() {
        screen = treeController.getRoot();
        this.getRoot().setCenter(screen);
        this.getRoot().setBottom(null);
        hideListVBox();
    }

    /**
     * Hide the info panel.
     */
    private void hideListVBox() {
        this.getRoot().getChildren().remove(listVBox);
    }

    /**
     * Method to add items to the Info-List
     */
    private void setListItems() {
        List<String> genomes = graphController.getGenomes();
        genomes.sort(Comparator.naturalOrder());
        list.setItems(FXCollections.observableArrayList(genomes));
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
     * Getter method for the ListFactory.
     *
     * @return the ListFactory.
     */
    public ListFactory getListFactory() {
        return listFactory;
    }

    /**
     * Method to set the currentView.
     *
     * @param currentView the current View.
     */
    public void setCurrentView(int currentView) {
        this.currentView = currentView;
    }

    /**
     * Getter for the textfiel.d
     *
     * @return the textfield.
     */
    public TextField getTextField() {
        return textField;
    }
}
