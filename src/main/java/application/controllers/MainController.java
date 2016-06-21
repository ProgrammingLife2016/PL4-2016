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
import core.model.Model;
import core.parsers.AnnotationParser;
import core.parsers.MetaDataParser;
import core.typeEnums.CellType;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

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
    private Text annotationWarning;

    private StackPane box;
    private int secondCount;
    private Filtering filtering;
    private boolean inGraph;
    private boolean metaDataLoaded;
    private boolean annotationsLoaded;

    private boolean allowNucleotideLevel;
    private boolean showReferenceStrain;

    private Button searchButton, selectAllButton, deselectSearchButton, highlightButton, deselectAnnotationButton;

    private HBox hBox;

    private Stack<String> mostRecentGFF;
    private Stack<String> mostRecentGFA;
    private Stack<String> mostRecentNWK;
    private Stack<String> mostRecentDir;

    /**
     * Constructor to create MainController based on abstract Controller.
     */
    public MainController() {
        super(new BorderPane());
        loadFXMLfile("/fxml/main.fxml");

        this.secondCount = -1;
        this.mostRecentGFF = new Stack<>();
        this.mostRecentGFA = new Stack<>();
        this.mostRecentNWK = new Stack<>();
        this.mostRecentDir = new Stack<>();
        this.filtering = new Filtering();
        this.metaDataLoaded = false;
        this.annotationsLoaded = false;
        this.allowNucleotideLevel = false;
        this.showReferenceStrain = false;

        checkMostRecent("/mostRecentGFA.txt", mostRecentGFA);
        checkMostRecent("/mostRecentGFF.txt", mostRecentGFF);
        checkMostRecent("/mostRecentNWK.txt", mostRecentNWK);
        checkMostRecent("/mostRecentDir.txt", mostRecentDir);

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
        MenuFactory.toggleViewMenu(true);
        MenuFactory.toggleFileMenu(false);
        MenuFactory.toggleFilters(true);
        this.getRoot().setCenter(imageView);
    }

    /**
     * Method to get the Screen
     * @return the Screen
     */
    public ScrollPane getScreen() {
        return screen;
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
        setAnnotationsLoaded(true);
        List<Annotation> annotations = AnnotationParser.readGFFFromFile(path);
        graphController.getGraph().initAnnotations(annotations);
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
        } catch (IOException | URISyntaxException e1) {
            e1.printStackTrace();
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
        } catch (IOException | URISyntaxException e1) {
            e1.printStackTrace();
        }
    }

    /**
     * Get the list containing most recent GFF files
     *
     * @return the list
     */
    public Stack<String> getMostRecentGFF() {
        return mostRecentGFF;
    }

    /**
     * Get the list containing most recent GFA files
     *
     * @return the list
     */
    public Stack<String> getMostRecentGFA() {
        return mostRecentGFA;
    }

    /**
     * Get the list containing most recent NWK files
     *
     * @return the list
     */
    public Stack<String> getMostRecentNWK() {
        return mostRecentNWK;
    }

    /**
     * Get the list containing the most recenlty visited directory
     * @return the list
     */
    public Stack<String> getMostRecentDir() {
        return mostRecentDir;
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
     * Add a file to the recently opened directory
     * @param s the file to be added
     */
    public void addRecentDir(String s) {
        if (!mostRecentDir.contains(s)) {
            mostRecentDir.push(s);
            writeMostRecent("/mostRecentDir.txt", mostRecentDir);
        }
    }


    /**
     * Method to add items to the GUI
     */
    private void initGUI() {
        createZoomBoxAndLegend();

        WindowFactory.createMenuWithSearch();
        MenuFactory.toggleGraphViewMenu(true);
        toggleSelectDeselect(true);

        this.getRoot().setCenter(graphController.getRoot());


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
        boolean update = false;
        // Apply the selected genomes
        if (showReferenceStrain) {
            selectedGenomes.add("MT_H37RV_BRD_V5.ref");
        } else {
            selectedGenomes.remove("MT_H37RV_BRD_V5.ref");
        }
        if (graphController.getGraph().changeLevelMaps(selectedGenomes)) {
            currentView = getGraphController().getGraph().getLevelMaps().size() - 1;
            update = true;
        }

        graphController.update(ref, currentView);

        if (update) {
            graphController.getZoomBox().fillZoomBox(true);
        }
    }

    /**
     * Toggle whether the reference strain should be filtered or not
     */
    public void toggleShowReferenceStrain() {
        this.showReferenceStrain = !this.showReferenceStrain;
    }

    /**
     * If selections are made in the phylogenetic tree,
     * this method will visualize/highlight them specifically.
     *
     * @param ref the references to highlight.
     * @param s   a List of selected strains.
     */
    public void strainSelection(ArrayList<String> ref, List<String> s) {
        graphController.getGraph().reset();
        fillGraph(ref, s);
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
                    genomeTextField.textProperty().get().trim().toUpperCase()) != null) {
                LeafCell cell = treeController.getCellByName(
                        genomeTextField.textProperty().get().toUpperCase().trim());
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
     */
    private void setAnnotationButtonsActionListener() {
        highlightButton.setOnAction(e -> {
            if (isAnnotationsLoaded()) {
                String input = annotationTextField.getText();

                if (!input.isEmpty()) {
                    if (currentView > 0) {
                        allowNucleotideLevel = true;
                        switchScene(Integer.MIN_VALUE);
                    }

                    initListenerProperties();
                }
            }
        });

        deselectAnnotationButton.setOnAction(e -> {
            deselectAllAnnotations();
            annotationTextField.setText("");
            annotationWarning.setText("");
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

            // Deselect the previously highlighted annotation as only one should be highlighted at a time.
            deselectAllAnnotations();
            boolean foundAnnotation = false;
            if (newAnn.getSpannedNodes() != null && newAnn.getSpannedNodes().size() != 0) {
                for (Node node : newAnn.getSpannedNodes()) {
                    int id = node.getId();
                    Node nodeInMap = graphController.getGraph().getLevelMaps().get(0).get(id);
                    if (nodeInMap != null) {
                        graphController.slideToPercent((cellMap.get(id).getLayoutX() - (screen.getWidth() / 4))
                                / (graphController.getGraph().getModel().getMaxWidth() - 450));
                        foundAnnotation = true;
                        break;
                    }
                }
            }
            if (!foundAnnotation) {
                WindowFactory.createAnnNotFoundAlert();
            }

            for (Node n : newAnn.getSpannedNodes()) {
                ((RectangleCell) cellMap.get(n.getId())).setHighLight();
            }

            annotationWarning.setText("");
        } catch (AnnotationProcessor.TooManyAnnotationsFoundException e1) {
        }
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

        nodeMap.values().stream().filter(n -> n.getType().equals(CellType.RECTANGLE)).forEachOrdered(n -> {
            ((RectangleCell) cellMap.get(n.getId())).deselectHighLight();
        });
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
        addGenomeKeyHandler(genomeTextField);
        hBox.getStylesheets().add("/css/main.css");

        searchButton = new Button("Search Genome (In Tree)");
        selectAllButton = new Button("Select all");
        deselectSearchButton = new Button("Deselect All");
        ArrayList<Button> buttons = new ArrayList<>();
        Collections.addAll(buttons, searchButton, deselectSearchButton, selectAllButton);
        setGenomeButtonActionListener(buttons);
        hBox.getChildren().addAll(genomeTextField, searchButton, selectAllButton, deselectSearchButton);

        // Don't add the annotation search box in the tree view
        if (withAnnotationSearch) {
            annotationTextField = new TextField();
            addAnnotationKeyHandler(annotationTextField);
            highlightButton = new Button("Highlight annotation");
            deselectAnnotationButton = new Button("Deselect annotation");
            setAnnotationButtonsActionListener();
            hBox.getChildren().addAll(annotationTextField, highlightButton, deselectAnnotationButton);
        }

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
     * Method to add a Key Handler to the annotation TextField
     * @param textField the annotation TextField
     */
    public void addAnnotationKeyHandler(TextField textField) {
        textField.setOnKeyPressed(event -> {
            if (event.getCode().equals(KeyCode.ENTER)) {
                if (!annotationTextField.getText().isEmpty()) {
                    initListenerProperties();
                }
            }
        });
    }

    /**
     * Method to add a Key Handler to the genome TextField
     * @param textField the genome TextField
     */
    public void addGenomeKeyHandler(TextField textField) {
        textField.setOnKeyPressed(event -> {
            if (event.getCode().equals(KeyCode.ENTER)) {
                if (!genomeTextField.getText().isEmpty()
                        && treeController.getCellByName(
                        genomeTextField.textProperty().get().trim().toUpperCase()) != null) {
                    LeafCell cell = treeController.getCellByName(
                            genomeTextField.textProperty().get().toUpperCase().trim());
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

            }
        });
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
     * Method to enable the annotation searchBar
     * @param x boolean indicating enabling or disabling
     */
    public void toggleSearchBar(boolean x) {
        highlightButton.setDisable(x);
        deselectAnnotationButton.setDisable(x);
        annotationTextField.setDisable(x);
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

        int minLevel = 1;
        if (allowNucleotideLevel) {
            minLevel = 0;
        }
        currentView = Math.max(minLevel, currentView);
        currentView = Math.min(graphController.getGraph().getLevelMaps().size() - 1, currentView);
        fillGraph(graphController.getGraph().getCurrentRef(),
                graphController.getGraph().getCurrentGenomes());
        toggleSelectDeselect(true);
    }

    /**
     * Method to toggle whether or not the nucleotide level can be reached through scrolling.
     */
    public void toggleAllowNucleotideLevel() {
        this.allowNucleotideLevel = !this.allowNucleotideLevel;
    }

    /**
     * Method to fill the phylogenetic tree.
     */
    public void fillTree() {
        inGraph = false;
        screen = treeController.getRoot();

        if (isAnnotationsLoaded()) {
            MenuFactory.loadAnnotations.setDisable(true);
        }

        MenuFactory.toggleTreeViewMenu(true);

        this.getRoot().setCenter(screen);
        this.getRoot().setBottom(null);
    }

    /**
     * Method to add items to the Info-List
     */
    private void setListItems() {
        List<String> genomes;
        if (filtering.isFiltering()) {
            genomes = graphController.getGraph().reduceGenomeList(filtering.getSelectedGenomes());
        } else if (treeController != null && !treeController.getSelectedGenomes().isEmpty()) {
            genomes = graphController.getGraph().reduceGenomes(
                    treeController.getSelectedGenomes());
        } else {
            genomes = graphController.getGraph().getCurrentGenomes();
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
            if (filtering.isFiltering()) {
                strainSelection(new ArrayList<>(), getLoadedGenomeNames());
            } else {
                strainSelection(new ArrayList<>(), graphController.getGraph().getAllGenomes());
            }
        } else {
            setListItems();
        }

        StringBuilder builder = new StringBuilder();
        appendFilterNames(builder);
        listFactory.modifyNodeInfo(builder.toString());

        treeController.colorSelectedStrains();
    }

    /**
     * Return a list with in the graph loaded genome names.
     *
     * @return a list of loaded genome names.
     */
    public List<String> getLoadedGenomeNames() {
        return graphController.getGraph().reduceGenomeList(filtering.getSelectedGenomes());
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
