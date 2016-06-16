package application.controllers;

import application.factories.LegendFactory;
import application.factories.ListFactory;
import application.factories.MenuFactory;
import application.factories.WindowFactory;
import application.fxobjects.Cell;
import application.fxobjects.graphCells.RectangleCell;
import application.fxobjects.treeCells.LeafCell;
import core.annotation.Annotation;
import core.annotation.AnnotationProcessor;
import core.filtering.Filter;
import core.filtering.Filtering;
import core.graph.Node;
import core.parsers.AnnotationParser;
import core.parsers.MetaDataParser;
import core.typeEnums.CellType;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URISyntaxException;
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
    private TextField annotationTextField;
    private StackPane box;
    private int count;
    private int secondCount;
    private Filtering filtering;
    private boolean inGraph;
    private boolean metaDataLoaded;
    private boolean annotationsLoaded;

    private Button searchButton;
    private Button selectAllButton;
    private Button deselectSearchButton;
    private HBox hBox;

    private Stack<String> mostRecentGFF;
    private Stack<String> mostRecentGFA;
    private Stack<String> mostRecentNWK;

    /**
     * Constructor to create MainController based on abstract Controller.
     */
    public MainController() {
        super(new BorderPane());
        loadFXMLfile("/fxml/main.fxml");

        this.count = -1;
        this.secondCount = -1;
        this.mostRecentGFF = new Stack<>();
        this.mostRecentGFA = new Stack<>();
        this.mostRecentNWK = new Stack<>();
        this.filtering = new Filtering();
        this.metaDataLoaded = false;
        this.annotationsLoaded = false;

        checkMostRecent("/mostRecentGFA.txt", mostRecentGFA);
        checkMostRecent("/mostRecentGFF.txt", mostRecentGFF);
        checkMostRecent("/mostRecentNWK.txt", mostRecentNWK);

        createMenu(false, false);

        setBackground("/background_images/DART2N.png");

        // Create the new GraphController
        graphController = new GraphController(this);
    }

    /**
     * Initializes the graph.
     */
    public void initGraph() {
        initGUI();
        graphController.getGraph().findAllGenomes();
        // currentView = graphController.getGraph().getLevelMaps().size() - 1;

        // fillGraph(new ArrayList<>(), new ArrayList<>());

        //  graphController.getGraph().getModel().matchNodesAndAnnotations();
    }

    /**
     * Method to set the background of the MainScreen
     *
     * @param s URL of the image to be set
     */
    public void setBackground(String s) {
        ImageView imageView = new ImageView(s);
        imageView.fitWidthProperty().bind(this.getRoot().widthProperty());
        imageView.fitHeightProperty().bind(this.getRoot().heightProperty());
        this.getRoot().setCenter(imageView);
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
        List<Annotation> annotations = AnnotationParser.readGFFFromFile(path);

        setAnnotationsLoaded(true);

        graphController.getGraph().setAnnotations(annotations);
        graphController.getGraph().getModel().matchNodesAndAnnotations();

        MenuFactory.loadAnnotations.setDisable(true);
    }

    /**
     * Initializes the meta data in the MetaData class.
     *
     * @param path Path to the meta data file.
     */
    public void initMetadata(String path) {
        MetaDataParser.readMetadataFromFile(path);
        setMetaDataLoaded(true);
    }

    /**
     * Method to check whether the MetaData is loaded or not
     *
     * @return boolean
     */
    public boolean isMetaDataLoaded() {
        return metaDataLoaded;
    }

    /**
     * Method to set whether the MetaData is loaded or not
     *
     * @param x boolean
     */
    public void setMetaDataLoaded(boolean x) {
        this.metaDataLoaded = x;
    }

    /**
     * Method the check whether we have loaded annotation-data or not
     *
     * @return boolean
     */
    public boolean isAnnotationsLoaded() {
        return annotationsLoaded;
    }

    /**
     * Method to set whether the annotation-data is loaded or not
     *
     * @param x boolean indication whether annotation-data is loaded or not
     */
    public void setAnnotationsLoaded(boolean x) {
        this.annotationsLoaded = x;
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
    }

    /**
     * Method to check whether the file containing recently opened files is empty or not.
     *
     * @param fileName   The name of the most recent file.
     * @param mostRecent list of most recent files.
     */
    @SuppressFBWarnings
    public void checkMostRecent(String fileName, Stack<String> mostRecent) {
        try {
            File file = new File(MainController.class.getProtectionDomain()
                    .getCodeSource().getLocation().toURI().getPath());
            File current = new File(file.getParentFile() + fileName);

            if (current.exists()) {
                Scanner sc = new Scanner(current);
                while (sc.hasNextLine()) {
                    String string = sc.nextLine();
                    mostRecent.add(string);
                }

                sc.close();
            }
        } catch (IOException e1) {
            e1.printStackTrace();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

    /**
     * Write a recently chosen NWK file to the file
     *
     * @param fileName   The name of the most recent file.
     * @param mostRecent list of most recent files.
     */
    @SuppressFBWarnings
    public void writeMostRecent(String fileName, Stack<String> mostRecent) {
        try {
            File file = new File(MainController.class.getProtectionDomain()
                    .getCodeSource().getLocation().toURI().getPath());
            File current = new File(file.getParentFile() + fileName);

            current.createNewFile();

            PrintWriter writer = new PrintWriter(current, "UTF-8");

            for (int i = 0; i < mostRecent.size(); i++) {
                writer.println(mostRecent.get(i));
            }

            writer.close();
        } catch (IOException e1) {
            e1.printStackTrace();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

    /**
     * Get the list containing most recent GFF files
     *
     * @return the list
     */
    public Stack getMostRecentGFF() {
        return mostRecentGFF;
    }

    /**
     * Get the list containing most recent GFA files
     *
     * @return the list
     */
    public Stack getMostRecentGFA() {
        return mostRecentGFA;
    }

    /**
     * Get the list containing most recent NWK files
     *
     * @return the list
     */
    public Stack getMostRecentNWK() {
        return mostRecentNWK;
    }

    /**
     * Add a file to the recent opened GFF files
     *
     * @param s the file to be added
     */
    public void addRecentGFF(String s) {
        if (!mostRecentGFF.contains(s)) {
            mostRecentGFF.push(s);
            writeMostRecent("/mostRecentGFF.txt", mostRecentGFF);
        }
    }

    /**
     * Add a file to the recent opened GFA files
     *
     * @param s the file to be added
     */
    public void addRecentGFA(String s) {
        if (!mostRecentGFA.contains(s)) {
            mostRecentGFA.push(s);
            writeMostRecent("/mostRecentGFA.txt", mostRecentGFA);
        }

    }

    /**
     * Add a file to the recent opened NWK files
     *
     * @param s the file to be added
     */
    public void addRecentNWK(String s) {
        if (!mostRecentNWK.contains(s)) {
            mostRecentNWK.push(s);
            writeMostRecent("/mostRecentNWK.txt", mostRecentNWK);
        }

    }

    /**
     * Method to add items to the GUI
     */
    private void initGUI() {
        createZoomBoxAndLegend();

        MenuFactory.toggleViewMenu(false);
        MenuFactory.toggleFileMenu(true);
        MenuFactory.toggleMostRecent(true);
        MenuFactory.toggleFilters(false);

        this.getRoot().setCenter(graphController.getRoot());
        toggleSelectDeselect(true);

        if (secondCount == -1) {
            createList();
            setListItems();
            secondCount++;
        }

        this.getRoot().setRight(listVBox);
    }

    /**
     * Method to fill the graph.
     *
     * @param ref             the reference string.
     * @param selectedGenomes the genomes to display.
     */
    public void fillGraph(ArrayList<String> ref, List<String> selectedGenomes) {
        createMenu(true, true);
        inGraph = true;

        // Apply the selected genomes
        if (graphController.getGraph().changeLevelMaps(selectedGenomes)) {
            currentView = getGraphController().getGraph().getLevelMaps().size() - 1;
        }
        graphController.update(ref, currentView);
        graphController.getZoomBox().fillZoomBox(count == -1);

        count++;
    }

    /**
     * If selections are made in the phylogenetic tree,
     * this method will visualize/highlight them specifically.
     *
     * @param s a List of selected strains.
     */
    public void soloStrainSelection(List<String> s) {
        ArrayList<String> list2 = new ArrayList<>();
        list2.add(s.get(0));
        fillGraph(list2, new ArrayList<>());
        initGUI();
    }

    /**
     * If selections are made in the phylogenetic tree,
     * this method will visualize/highlight them specifically.
     *
     * @param s a List of selected strains.
     */
    public void strainSelection(ArrayList<String> ref, List<String> s) {
        graphController.getGraph().reset();
        fillGraph(ref, s);
        System.out.println("current view: " + currentView);

        setListItems();
        initGUI();
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
     * @param buttons The buttons.
     */
    private void setGenomeButtonActionListener(ArrayList<Button> buttons) {
        buttons.get(0).setOnAction(e -> {
            if (!genomeTextField.getText().isEmpty()
                    && treeController.getCellByName(
                    genomeTextField.textProperty().get().trim()) != null) {
                LeafCell cell = treeController.getCellByName(genomeTextField.textProperty().get().trim());
                treeController.applyCellHighlight(cell);
                treeController.selectStrain(cell);
                genomeTextField.setText("");

                if (inGraph) {
                    fillTree();
                }

                if (cell != null) {
                    treeController.getRoot().setVvalue(cell.getLayoutY() / treeController.getMaxY());
                }
            }
        });

        buttons.get(1).setOnAction(e -> {
            treeController.clearSelection();
            genomeTextField.setText("");
            fillTree();
        });

        buttons.get(2).setOnAction(e -> {
            treeController.selectAll();
            genomeTextField.setText("");
            fillTree();
        });
    }

    /**
     * Adds an action listener to the annotation highlight button.
     *
     * @param highlightButton          The annotation highlight button.
     * @param deselectAnnotationButton The annotation deselect button.
     */
    private void setAnnotationButtonsActionListener(Button highlightButton, Button deselectAnnotationButton) {
        highlightButton.setOnAction(e -> {
            if (!isAnnotationsLoaded()) {
                createAnnotationPopup();
            } else {

                if (currentView != 0) {
                    return;
                }
                if (!annotationTextField.getText().isEmpty()) {
                    initListenerProperties();
                }
            }
        });

        deselectAnnotationButton.setOnAction(e -> {
            deselectAllAnnotations();
            annotationTextField.setText("");
        });

    }

    /**
     * Method to specify what the Listener needs to do
     */
    public void initListenerProperties() {
        List<Annotation> annotations = graphController.getGraph().getModel().getAnnotations();
        try {
            Annotation newAnn = AnnotationProcessor.findAnnotation(annotations,
                    annotationTextField.getText());
            Map<Integer, Cell> cellMap = graphController.getGraph().getModel().getCellMap();
            if (newAnn == null || newAnn.getSpannedNodes() == null) {
                return;
            }
            // Deselect the previously highlighted annotation as only one should be highlighted at a time.
            deselectAllAnnotations();
            if (newAnn.getSpannedNodes().get(0) != null) {
                int i = newAnn.getSpannedNodes().get(0).getId();
                graphController.getRoot().setHvalue((cellMap.get(i)).getLayoutX()
                        / getGraphController().getGraph().getModel().getMaxWidth());
            }

            for (Node n : newAnn.getSpannedNodes()) {
                ((RectangleCell) cellMap.get(n.getId())).setHighLight();
            }
        } catch (AnnotationProcessor.TooManyAnnotationsFoundException e1) {
            System.out.println("[DEBUG] Found too many matching annotations");
        }
    }

    /**
     * Method to create a PopUp when no Annotation Data is loaded
     */
    public void createAnnotationPopup() {
        WindowFactory.createAnnotationChooser("Please load Annotation Data first");
    }

    /**
     * Deselects all annotations
     */
    private void deselectAllAnnotations() {
        if (currentView != 0) {
            return;
        }
        Map<Integer, Node> nodeMap = graphController.getGraph().getModel().getLevelMaps().get(0);
        Map<Integer, Cell> cellMap = graphController.getGraph().getModel().getCellMap();

        for (Node n : nodeMap.values()) {
            if (n.getType().equals(CellType.RECTANGLE)) {
                ((RectangleCell) cellMap.get(n.getId())).deselectHighLight();
            }
        }
    }

    /**
     * Method to create the menu bar.
     *
     * @param withSearch           Whether to add the search bar.
     * @param withAnnotationSearch Whether to add the annotation search box to the search bar.
     */
    public void createMenu(boolean withSearch, boolean withAnnotationSearch) {
        VBox vBox = new VBox();
        hBox = new HBox();
        genomeTextField = new TextField();
        hBox.getStylesheets().add("/css/main.css");

        searchButton = new Button("Search Genome (In Tree)");
        selectAllButton = new Button("Select all");
        deselectSearchButton = new Button("Deselect All");
        ArrayList<Button> buttons = new ArrayList<>();
        Collections.addAll(buttons, searchButton, deselectSearchButton, selectAllButton);
        setGenomeButtonActionListener(buttons);
        hBox.getChildren().addAll(genomeTextField, searchButton, selectAllButton, deselectSearchButton);

        // Dont add the annotation search box in the tree view
        if (withAnnotationSearch) {
            annotationTextField = new TextField();
            Button highlightButton = new Button("Highlight annotation");
            Button deselectAnnotationButton = new Button("Deselect annotation");
            setAnnotationButtonsActionListener(highlightButton, deselectAnnotationButton);
            hBox.getChildren().addAll(annotationTextField, highlightButton, deselectAnnotationButton);
        }

        if (withSearch) {
            vBox.getChildren().addAll(menuBar, hBox);
        } else {
            MenuFactory menuFactory = new MenuFactory(this);
            menuBar = menuFactory.createMenu(menuBar);
            MenuFactory.toggleViewMenu(true);
            MenuFactory.toggleFilters(true);
            vBox.getChildren().addAll(menuBar);
        }

        this.getRoot().setTop(vBox);
    }

    /**
     * Method to enable and disable the select and the select buttons
     *
     * @param x boolean indicating enabling or disabling
     */
    public void toggleSelectDeselect(boolean x) {
        selectAllButton.setDisable(x);
        deselectSearchButton.setDisable(x);

    }


    /**
     * Method to create the Info-list
     */
    public void createList() {
        listFactory = new ListFactory();
        listVBox = listFactory.createInfoList("");
        list = listFactory.getList();

        list.setOnMouseClicked(event -> listSelect());

        setListItems();

        listVBox.getStylesheets().add("/css/list.css");

        this.getRoot().setRight(listVBox);
    }

    /**
     * All strains selected to highlight.
     */
    private ArrayList<String> highlights = new ArrayList<>();

    /**
     * Method to perform action upon listItem selection
     */
    public void listSelect() {
        if (!(list.getSelectionModel().getSelectedItem() == null)) {

            highlights.clear();

            for (Object o : list.getSelectionModel().getSelectedItems()) {
                highlights.add((String) o);
            }

            //fillGraph(highlights, graphController.getGenomes());
            strainSelection(highlights, getTreeController().getSelectedGenomes());
            System.out.println("Select");
            if (getGraphController().getGraphMouseHandling().getPrevClick() != null) {
                graphController.focus(getGraphController()
                        .getGraphMouseHandling().getPrevClick().getCellId());
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
                graphController.getGraph().getCurrentGenomes());
    }

    /**
     * Method to fill the phylogenetic tree.
     */
    public void fillTree() {
        inGraph = false;
        createMenu(true, false);
        screen = treeController.getRoot();
        toggleSelectDeselect(false);

        this.getRoot().setCenter(screen);
        this.getRoot().setBottom(null);
    }

    /**
     * Method to add items to the Info-List
     */
    private void setListItems() {
        List<String> genomes = new ArrayList<>();
        if (filtering.isFiltering()) {
            genomes = graphController.getGraph().reduceGenomes(
                    filtering.getSelectedGenomes(), filtering.isFiltering());
        } else if (treeController != null && !treeController.getSelectedGenomes().isEmpty()) {
            genomes = graphController.getGraph().reduceGenomes(
                    treeController.getSelectedGenomes());
        }
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
     * Modify the filters applied to the tree.
     *
     * @param f     Filter type.
     * @param state true or false state.
     */
    public void modifyFilter(Filter f, boolean state) {
        treeController.getSelectedStrains().forEach(treeController::revertCellHighlight);

        if (state) {
            filtering.applyFilter(f);
        } else {
            filtering.removeFilter(f);
        }

        treeController.clearSelectedStrains();
        filtering.getSelectedGenomes().forEach(g ->
                        treeController.addSelectedStrain(treeController.getCellByName(g.getName()))
        );

        if (inGraph) {
            strainSelection(new ArrayList<>(), getLoadedGenomeNames());
            StringBuilder builder = new StringBuilder();
            appendFilterNames(builder);
            listFactory.modifyNodeInfo(builder.toString());
        }

        treeController.colorSelectedStrains();
    }

    /**
     * Return a list with in the graph loaded genome names.
     *
     * @return a list of loaded genome names.
     */
    public List<String> getLoadedGenomeNames() {
        return graphController.getGraph().reduceGenomes(filtering.getSelectedGenomes(), filtering.isFiltering());
    }

    /**
     * Getter for the Filtering class.
     *
     * @return the Filtering class.
     */
    public Filtering getFiltering() {
        return filtering;
    }

    /**
     * Check whether scene is in graph.
     *
     * @return true if in graph, false otherwise.
     */
    public boolean isInGraph() {
        return inGraph;
    }

    /**
     * Get the names of all applied filters.
     *
     * @param builder a builder to append to.
     */
    public void appendFilterNames(StringBuilder builder) {
        if (filtering.isFiltering()) {
            builder.append("Applied filters: ").append("\n");
            filtering.getFilters().forEach(f ->
                            builder.append(f.getFilterName()).append("\n")
            );
            builder.append("\n");
        }
    }
}
