package application.controllers;

import application.fxobjects.cell.graph.RectangleCell;
import core.Annotation;
import core.AnnotationProcessor;
import core.Node;
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

    private GridPane annotationSearchBox;
    private HBox legend;
    private VBox listVBox;
    private ListView list;
    private int currentView;
    private ListFactory listFactory;
    private StackPane box;
    private int count;
    private int secondCount;
    private int selectedIndex;

    /**
     * Constructor to create MainController based on abstract Controller.
     */
    public MainController() {
        super(new BorderPane());
        loadFXMLfile("/fxml/main.fxml");

        this.count = -1;
        this.secondCount = -1;

        // Create the new GraphController
        graphController = new GraphController(this);

        currentView = graphController.getGraph().getLevelMaps().size() - 1;

        // Fill the graph
        fillGraph(null, new ArrayList<>());

        // Create the TreeController
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

    /**
     * Method to add items to the GUI
     */
    private void initGUI() {
        createZoomBoxAndLegend();
        if (secondCount == -1) {
            createList();
            setListItems();
            secondCount++;
        }

        this.getRoot().setCenter(graphController.getRoot());
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

        graphController.getZoomBox().fillZoomBox(count == -1);
        count++;
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

        // Place the annotationsearch box
        createAnnotationSearchBox();
        annotationSearchBox.setAlignment(Pos.CENTER_LEFT);

        // Place the legend
        createLegend();
        legend.setAlignment(Pos.CENTER_RIGHT);

        // Place the zoom box
        box = graphController.getZoomBox().getZoomBox();

        hbox.setAlignment(Pos.CENTER);
        hbox.getChildren().addAll(annotationSearchBox, box, legend);
        this.getRoot().setBottom(hbox);
    }

    /**
     * Method to create the annotation search box.
     */
    private void createAnnotationSearchBox() {
        annotationSearchBox = new AnnotationSearchBoxFactory().createSearchBox();

        TextField box = (TextField) annotationSearchBox.getChildren().get(2);
        Button search = (Button) annotationSearchBox.getChildren().get(3);

        search.setOnAction(e -> {
            if (box.getText() != null && !box.getText().isEmpty()) {
                long input = Long.parseLong(box.getText());

                List<Annotation> annotations
                        = graphController.getGraph().getModel().getAnnotations();
                Annotation ann = AnnotationProcessor.findAnnotationByID(
                        annotations, input);

                Map<Integer, application.fxobjects.cell.Cell> cellMap
                        = graphController.getGraph().getModel().getCellMap();

                for (Node n : ann.getSpannedNodes()) {
                    ((RectangleCell) cellMap.get(n.getId())).setHighLight();
                }
            }
        });
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
        MenuFactory menuFactory = new MenuFactory(this);
        menuBar = menuFactory.createMenu(menuBar);
        this.getRoot().setTop(menuBar);
    }

    /**
     * Method to create the Info-list
     */
    public void createList() {
        listFactory = new ListFactory();
        listVBox = listFactory.createInfoList("");
        list = listFactory.getList();

        list.setOnMouseClicked(event -> listSelect());

        list.setOnMouseReleased(event -> list.getFocusModel().focus(selectedIndex));

        setListItems();
        this.getRoot().setRight(listVBox);
    }


    /**
     * Method to perform action upon listItem selection
     */
    public void listSelect() {
        if (!(list.getSelectionModel().getSelectedItem() == null)) {
            selectedIndex = list.getSelectionModel().getSelectedIndex();
            graphController.getGraph().reset();
            fillGraph(list.getSelectionModel().getSelectedItem(), graphController.getGenomes());
            if (getGraphController().getGraphMouseHandling().getPrevClick() != null) {
                graphController.focus(getGraphController()
                        .getGraphMouseHandling().getPrevClick());
            }
        }

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
}
