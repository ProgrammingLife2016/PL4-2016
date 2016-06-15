package application.controllers;

import application.factories.LegendFactory;
import application.factories.ListFactory;
import application.factories.MenuFactory;
import application.fxobjects.Cell;
import application.fxobjects.graphCells.RectangleCell;
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

    private Button searchButton;
    private Button selectAllButton;
    private Button deselectSearchButton;
    private HBox hBox;

    private Stack<String> mostRecentGFF;
    private Stack<String> mostRecentMetadata;
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
        this.mostRecentMetadata = new Stack<>();
        this.mostRecentGFA = new Stack<>();
        this.mostRecentNWK = new Stack<>();
        this.filtering = new Filtering();

        checkMostRecent("/mostRecentGFA.txt", mostRecentGFA);
        checkMostRecent("/mostRecentGFF.txt", mostRecentGFF);
        checkMostRecent("/mostRecentMetadata.txt", mostRecentMetadata);
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
        currentView = graphController.getGraph().getLevelMaps().size() - 1;

        fillGraph(new ArrayList<>(), new ArrayList<>());

        graphController.getGraph().getModel().matchNodesAndAnnotations();
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

        graphController.getGraph().setAnnotations(annotations);
        graphController.getGraph().getModel().matchNodesAndAnnotations();
    }

    /**
     * Initializes the meta data in the MetaData class.
     *
     * @param path Path to the meta data file.
     */
    public void initMetadata(String path) {
        MetaDataParser.readMetadataFromFile(path);
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
    public Stack getMostRecentMetadata() {
        return mostRecentMetadata;
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
     * Add a file to the recent opened Metadata files
     *
     * @param s the file to be added
     */
    public void addRecentMetadata(String s) {
        if (!mostRecentMetadata.contains(s)) {
            mostRecentMetadata.push(s);
            writeMostRecent("/mostRecentMetadata.txt", mostRecentMetadata);
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
    public void strainSelection(List<String> s) {
        graphController.getGraph().reset();
        fillGraph(new ArrayList<>(), s);

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
     * @param searchButton    The genome search button.
     * @param deselectButton  The deselect button.
     * @param selectAllButton The select all button.
     */
    private void setGenomeButtonActionListener(Button searchButton, Button deselectButton, Button selectAllButton) {
        searchButton.setOnAction(e -> {
            if (!genomeTextField.getText().isEmpty()) {
                Cell cell = treeController.getCellByName(genomeTextField.textProperty().get().trim());
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

        deselectButton.setOnAction(e -> {
            treeController.clearSelection();
            genomeTextField.setText("");
            fillTree();
        });

        selectAllButton.setOnAction(e -> {
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
            if (currentView != 0) {
                return;
            }

            if (!annotationTextField.getText().isEmpty()) {
                List<Annotation> annotations = graphController.getGraph().getModel().getAnnotations();

                try {
                    Annotation newAnnotation
                            = AnnotationProcessor.findAnnotation(annotations, annotationTextField.getText());
                    Map<Integer, Cell> cellMap = graphController.getGraph().getModel().getCellMap();
                    if (newAnnotation == null || newAnnotation.getSpannedNodes() == null) {
                        return;
                    }

                    // Deselect the previously highlighted annotation as only one should be highlighted at a time.
                    deselectAllAnnotations();
                    for (Node n : newAnnotation.getSpannedNodes()) {
                        ((RectangleCell) cellMap.get(n.getId())).setHighLight();
                    }
                } catch (AnnotationProcessor.TooManyAnnotationsFoundException e1) {
                    System.out.println("[DEBUG] Found too many matching annotations");
                }

                annotationTextField.setText("");
            }
        });

        deselectAnnotationButton.setOnAction(e -> {
            deselectAllAnnotations();
            annotationTextField.setText("");
        });
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
     * @param withAnnotationSearch Whether to add the annotation seach box to the search bar.
     */
    public void createMenu(boolean withSearch, boolean withAnnotationSearch) {
        VBox vBox = new VBox();
        hBox = new HBox();
        genomeTextField = new TextField();


        hBox.getStylesheets().add("/css/main.css");

        searchButton = new Button("Search Genome (In Tree)");
        selectAllButton = new Button("Select all");
        deselectSearchButton = new Button("Deselect All");
        setGenomeButtonActionListener(searchButton, deselectSearchButton, selectAllButton);
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
            graphController.getGraph().reset();

            highlights.clear();

            for (Object o : list.getSelectionModel().getSelectedItems()) {
                highlights.add((String) o);
            }

            fillGraph(highlights, graphController.getGenomes());
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
                graphController.getGraph().getGenomes());
    }

    /**
     * Method to fill the phylogenetic tree.
     */
    public void fillTree() {
        inGraph = false;
        createMenu(true, false);
        screen = treeController.getRoot();
        this.getRoot().setCenter(screen);
        this.getRoot().setBottom(null);
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
     * Getter for the textfield
     *
     * @return the textfield.
     */
    public TextField getTextField() {
        return genomeTextField;
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
            strainSelection(getLoadedGenomeNames());
        }

        treeController.colorSelectedStrains();
        treeController.modifyGraphOptions();
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
}
