package application.controllers;

import application.fxobjects.cell.graph.RectangleCell;
import core.*;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.image.ImageView;
import javafx.scene.control.*;
import javafx.scene.layout.*;

import java.io.FileNotFoundException;
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
    private int currentView;
    private ListFactory listFactory;
    private TextField genomeTextField;
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

        ImageView imageView = new ImageView("/DART2N.png");
        imageView.fitWidthProperty().bind(this.getRoot().widthProperty());
        imageView.fitHeightProperty().bind(this.getRoot().heightProperty());

        this.getRoot().setCenter(imageView);

        // Create the new GraphController
        graphController = new GraphController(this);
    }

    /**
     * Initializes the graph.
     */
    public void initGraph() {
        currentView = graphController.getGraph().getLevelMaps().size() - 1;
        fillGraph(null, new ArrayList<>());
        graphController.getGraph().getModel().matchNodesAndAnnotations();
    }

    /**
     * Initializes the tree.
     *
     * @param s Path to the tree file.
     */
    public void initTree(String s) {
        treeController = new TreeController(this, s);
        fillTree();
    }

    /**
     * Initializes the annotation data.
     *
     * @param path Path to the annotation data file.
     */
    public void initAnnotations(String path) {
        try {
            List<Annotation> annotations = AnnotationParser.readCDSFilteredGFF(path);
            graphController.getGraph().setAnnotations(annotations);
            graphController.getGraph().getModel().matchNodesAndAnnotations();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
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
        createMenu(false);
    }

    /**
     * Method to add items to the GUI
     */
    private void initGUI() {
        createZoomBoxAndLegend();

        this.getRoot().setCenter(graphController.getRoot());
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

        // Place the legend
        createLegend();
        legend.setAlignment(Pos.CENTER_RIGHT);

        // Place the zoom box
        box = graphController.getZoomBox().getZoomBox();

        hbox.setAlignment(Pos.CENTER);
        hbox.getChildren().addAll(box, legend);
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
     * Adds an action listener to the genome search and deselect buttons.
     *
     * @param searchButton The genome search button.
     * @param deselectButton The deselect button.
     */
   private void setSearchAndDeselectButtonActionListener(
           Button searchButton, Button deselectButton) {
       searchButton.setOnAction(e -> {
           if (!genomeTextField.getText().isEmpty()) {
               application.fxobjects.cell.Cell cell = treeController.getCellByName(
                       genomeTextField.textProperty().get().trim());
               treeController.applyCellHighlight(cell);
               treeController.selectStrain(cell);
               genomeTextField.setText("");
               fillTree();
           }
       });

       deselectButton.setOnAction(e -> {
           treeController.clearSelection();
           fillTree();
       });
   }

    /**
     * Adds an action listener to the annotation highlight button.
     *
     * @param annotationTextField The annotation search field.
     * @param highlightButton The annotation highlight button.
     */
    private void setHighlightButtonActionListener(
            TextField annotationTextField, Button highlightButton, Button deselectAnnotationButton) {
        highlightButton.setOnAction(e -> {
            if (currentView != 0) {
                return;
            }
            if (!annotationTextField.getText().isEmpty()) {
                List<Annotation> annotations
                        = graphController.getGraph().getModel().getAnnotations();

                try {
                    Annotation ann = AnnotationProcessor
                            .findAnnotation(annotations, annotationTextField.getText());

                    Map<Integer, application.fxobjects.cell.Cell> cellMap
                            = graphController.getGraph().getModel().getCellMap();

                    for (Node n : ann.getSpannedNodes()) {
                        ((RectangleCell) cellMap.get(n.getId())).setHighLight();
                    }
                } catch (AnnotationProcessor.TooManyAnnotationsFoundException e1) {
                    e1.printStackTrace();
                }
            }
        });

        highlightButton.setOnAction(e -> {

        });
    }

    /**
     * Method to create the menu bar.
     *
     * @param withSearch Which part of the menu to show.
     */
    public void createMenu(boolean withSearch) {
        VBox vBox = new VBox();
        HBox hBox = new HBox();
        genomeTextField = new TextField();

        Button searchButton = new Button("Search Genome (In Tree)");
        Button deselectSearchButton = new Button("Deselect All");

        TextField annotationTextField = new TextField();
        Button highlightButton = new Button("Highlight annotation");
        Button deselectAnnotationButton = new Button("Highlight annotation");

        setSearchAndDeselectButtonActionListener(searchButton, deselectSearchButton);
        setHighlightButtonActionListener(annotationTextField, highlightButton, deselectAnnotationButton);

        hBox.getChildren().addAll(genomeTextField, searchButton,
                deselectSearchButton, annotationTextField, highlightButton);

        if (withSearch) {
            vBox.getChildren().addAll(menuBar, hBox);
        } else {
            MenuFactory menuFactory = new MenuFactory(this);
            menuBar = menuFactory.createMenu(menuBar);
            vBox.getChildren().addAll(menuBar);
        }

        this.getRoot().setTop(vBox);
    }

    /**
     * Method to create the Info-list
     */
    public void createList() {
        listFactory = new ListFactory();
        listVBox = listFactory.createInfoList("");
        list = listFactory.getList();

        list.setOnMouseClicked(event -> listSelect());
        
        list.setOnMouseClicked(event -> {
            if (!(list.getSelectionModel().getSelectedItem() == null)) {
                graphController.getGraph().reset();
                getTextField().setText((String) list.getSelectionModel().getSelectedItem());

                fillGraph(list.getSelectionModel().getSelectedItem(), graphController.getGenomes());
                graphController.takeSnapshot();

            }
        });

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

    /**
     * Getter for the textfiel.d
     *
     * @return the textfield.
     */
    public TextField getTextField() {
        return genomeTextField;
    }
}
