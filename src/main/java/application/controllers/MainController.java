package application.controllers;

import application.fxobjects.cell.graph.RectangleCell;
import core.*;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.image.ImageView;
import javafx.scene.control.*;
import javafx.scene.layout.*;

import java.io.*;
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
    private LinkedList<String> mostRecentGFF;
    private LinkedList<String> mostRecentMetadata;
    private LinkedList<String> mostRecentGFA;
    private LinkedList<String> mostRecentNWK;
    private String lastAnnotationSearch;

    /**
     * Constructor to create MainController based on abstract Controller.
     */
    public MainController() {
        super(new BorderPane());
        loadFXMLfile("/fxml/main.fxml");

        this.count = -1;
        this.secondCount = -1;
        this.mostRecentGFF = new LinkedList<>();
        this.mostRecentMetadata = new LinkedList<>();
        this.mostRecentGFA = new LinkedList<>();
        this.mostRecentNWK = new LinkedList<>();

        checkMostRecent("/mostRecentGFF.txt", mostRecentGFF);
        checkMostRecent("/mostRecentMetadata.txt", mostRecentMetadata);
        checkMostRecent("/mostRecentGFA.txt", mostRecentGFA);
        checkMostRecent("/mostRecentNWK.txt", mostRecentNWK);

        createMenu(false);

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

        fillGraph(new ArrayList<>(), new ArrayList<>());

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
        MetaData.readMetadataFromFile(path);
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
     * Method to check whether the file containing recently opened files is empty or not
     */
    public void checkMostRecent(String fileName, LinkedList<String> mostRecent) {
        try {
            String s = ClassLoader.getSystemClassLoader().getResource(".").getPath().replaceAll("%20", " ");
            File x = new File(s + fileName);
            Scanner sc = new Scanner(x);
            while (sc.hasNextLine()) {
                String string = sc.nextLine();
                mostRecent.addFirst(string);
            }

            /*
             * Als je dit leest, plaats dan een comment bij de PR.
             */
            sc.close();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
    }

    /**
     * Write a recently chosen NWK file to the file
     */
    public void writeMostRecent(String fileName, LinkedList<String> mostRecent) {
        try {
            String s = ClassLoader.getSystemClassLoader().getResource(".").getPath().replaceAll("%20", " ");
            File file = new File(s + fileName);
            file.createNewFile();

            PrintWriter writer = new PrintWriter(file, "UTF-8");

            for (int i = 0; i < mostRecent.size(); i++) {
                writer.println(mostRecent.get(i));
            }

            writer.close();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
    }

    /**
     * Get the list containing most recent GFF files
     *
     * @return the list
     */
    public LinkedList getMostRecentGFF() {
        return mostRecentGFF;
    }

    /**
     * Get the list containing most recent GFA files
     *
     * @return the list
     */
    public LinkedList getMostRecentMetadata() {
        return mostRecentMetadata;
    }

    /**
     * Get the list containing most recent GFA files
     *
     * @return the list
     */
    public LinkedList getMostRecentGFA() {
        return mostRecentGFA;
    }

    /**
     * Get the list containing most recent NWK files
     *
     * @return the list
     */
    public LinkedList getMostRecentNWK() {
        return mostRecentNWK;
    }

    /**
     * Add a file to the recent opened GFF files
     *
     * @param s the file to be added
     */
    public void addRecentGFF(String s) {
        mostRecentGFF.addFirst(s);
        writeMostRecent("/mostRecentGFF.txt", mostRecentGFF);
    }

    /**
     * Add a file to the recent opened Metadata files
     *
     * @param s the file to be added
     */
    public void addRecentMetadata(String s) {
        mostRecentMetadata.addFirst(s);
        writeMostRecent("/mostRecentMetadata.txt", mostRecentMetadata);
    }

    /**
     * Add a file to the recent opened GFA files
     *
     * @param s the file to be added
     */
    public void addRecentGFA(String s) {
        mostRecentGFA.addFirst(s);
        writeMostRecent("/mostRecentGFA.txt", mostRecentGFA);
    }

    /**
     * Add a file to the recent opened NWK files
     *
     * @param s the file to be added
     */
    public void addRecentNWK(String s) {
        mostRecentNWK.addFirst(s);
        writeMostRecent("/mostRecentNWK.txt", mostRecentNWK);
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
    public void fillGraph(ArrayList<String> ref, List<String> selectedGenomes) {
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
     * @param searchButton   The genome search button.
     * @param deselectButton The deselect button.
     * @param selectAllButton The select all button.
     */
    private void setSearchAndDeselectButtonActionListener(
            Button searchButton, Button deselectButton, Button selectAllButton) {
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

        selectAllButton.setOnAction(e -> {
            treeController.selectAll();
            fillTree();
        });
    }

    /**
     * Adds an action listener to the annotation highlight button.
     *
     * @param annotationTextField The annotation search field.
     * @param highlightButton     The annotation highlight button.
     */
    private void setHighlightButtonActionListener(TextField annotationTextField,
                                                  Button highlightButton,
                                                  Button deselectAnnotationButton) {
        highlightButton.setOnAction(e -> processAnnotationButtonPress(annotationTextField, e));

        deselectAnnotationButton.setOnAction(e -> processAnnotationButtonPress(annotationTextField, e));
    }

    /**
     * Performs the actions needed on Annotation highlight and deselect button presses.
     *
     * @param annotationTextField The annotation search box.
     * @param e                   The ActionEvent that has been triggered.
     */
    private void processAnnotationButtonPress(TextField annotationTextField, ActionEvent e) {
        if (currentView != 0) {
            return;
        }

        if (!annotationTextField.getText().isEmpty()) {
            List<Annotation> annotations = graphController.getGraph().getModel().getAnnotations();

            try {
                Annotation newAnnotation
                        = AnnotationProcessor.findAnnotation(annotations, annotationTextField.getText());
                Map<Integer, application.fxobjects.cell.Cell> cellMap
                        = graphController.getGraph().getModel().getCellMap();

                if (newAnnotation == null || newAnnotation.getSpannedNodes() == null) {
                    return;
                }

                if (e.getSource().toString().contains("Highlight")) {
                    deselectPreviousHighLight(cellMap, annotations);
                }

                for (Node n : newAnnotation.getSpannedNodes()) {
                    if (e.getSource().toString().contains("Highlight")) {
                        ((RectangleCell) cellMap.get(n.getId())).setHighLight();
                    } else if (e.getSource().toString().contains("Deselect")) {
                        ((RectangleCell) cellMap.get(n.getId())).deselectHighLight();
                    }
                }

            } catch (AnnotationProcessor.TooManyAnnotationsFoundException e1) {
                e1.printStackTrace();
            }

            lastAnnotationSearch = annotationTextField.getText();
        }
    }

    /**
     * Deselects the old annotation.
     *
     * @param cellMap     Map of cells.
     * @param annotations List of annotations.
     */
    private void deselectPreviousHighLight(Map<Integer, application.fxobjects.cell.Cell> cellMap,
                                           List<Annotation> annotations) {
        if (lastAnnotationSearch != null) {
            try {
                Annotation oldAnnotation = AnnotationProcessor.findAnnotation(annotations, lastAnnotationSearch);
                for (Node oldAnnotationNode : oldAnnotation.getSpannedNodes()) {
                    ((RectangleCell) cellMap.get(oldAnnotationNode.getId())).deselectHighLight();
                }
            } catch (AnnotationProcessor.TooManyAnnotationsFoundException e) {
                e.printStackTrace();
            }
        }
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
        Button selectAllButton = new Button("Select all");
        Button deselectSearchButton = new Button("Deselect All");

        TextField annotationTextField = new TextField();
        Button highlightButton = new Button("Highlight annotation");
        Button deselectAnnotationButton = new Button("Deselect annotation");

        setSearchAndDeselectButtonActionListener(searchButton, deselectSearchButton, selectAllButton);
        setHighlightButtonActionListener(annotationTextField, highlightButton, deselectAnnotationButton);

        hBox.getChildren().addAll(genomeTextField, searchButton, selectAllButton, deselectSearchButton,
                annotationTextField, highlightButton, deselectAnnotationButton);

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

        setListItems();
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
     * Getter for the textfield
     *
     * @return the textfield.
     */
    public TextField getTextField() {
        return genomeTextField;
    }
}
