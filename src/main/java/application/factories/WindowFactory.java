package application.factories;

import application.controllers.MainController;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Screen;
import javafx.stage.Stage;
import org.apache.commons.io.FilenameUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.Stack;
import java.util.stream.Collectors;

import static java.lang.String.format;

/**
 * WindowFactory class.
 *
 * @version 1.0
 * @since 25-04-2016
 */
public final class WindowFactory {
    static Rectangle2D screenSize;
    static Stage window;
    static MainController mainController;
    static Scene scene;

    /**
     * Private class constructor.
     */
    private WindowFactory() {
    }

    /**
     * Create method for windows.
     *
     * @param m parent of the window
     * @return the constructed window.
     */
    public static Stage createWindow(MainController m) {
        mainController = m;
        window = new Stage();
        scene = createScene(m.getRoot());

        screenSize = Screen.getPrimary().getVisualBounds();

        window.setWidth(screenSize.getWidth());
        window.setHeight(screenSize.getHeight());
        window.setMaxHeight(screenSize.getHeight());
        window.setScene(scene);
        window.show();
        return window;
    }

    /**
     * Method to create the scene.
     *
     * @param parent parent object for the scene.
     * @return the constructed scene.
     */
    public static Scene createScene(Parent parent) {
        Scene scene = new Scene(parent);
        return scene;
    }

    /**
     * Method that creates a directoryChooser.
     *
     * @return the directoryChooser.
     */
    public static FileChooser createGraphChooser() {
        FileChooser directoryChooser = new FileChooser();
        directoryChooser.setTitle("Select Graph File");

        Stack<String> mostRecentDir = mainController.getMostRecentDir();

        if (!mostRecentDir.isEmpty()) {
            File initialFile = new File(mostRecentDir.get(0));
            if (initialFile != null && initialFile.isDirectory()) {
                directoryChooser.setInitialDirectory(initialFile);
            }
        }

        File selectedFile = directoryChooser.showOpenDialog(window);

        if (selectedFile != null) {
            mainController.addRecentGFA(selectedFile.getAbsolutePath());
            File parentDir = selectedFile.getParentFile();
            mainController.addRecentDir(parentDir.getAbsolutePath());

            createGFApopup(parentDir, selectedFile);
        }

        return directoryChooser;
    }


    /**
     * Method to create a pop-up when selecting a NWK File
     *
     * @param parentDir    the current Directory
     * @param selectedFile the selected File
     */
    @SuppressFBWarnings
    public static void createGFApopup(File parentDir, File selectedFile) {
        ArrayList<Text> candidates = new ArrayList<>();
        if (parentDir.isDirectory()) {
            for (File f : parentDir.listFiles()) {
                String ext = FilenameUtils.getExtension(f.getName());
                if (ext.equals("nwk")) {
                    Text t = new Text(f.getAbsolutePath());
                    candidates.add(t);
                }
            }
        }

        mainController.setBackground("/background_images/loading.png");
        if (!candidates.isEmpty()) {
            showPopup(candidates, selectedFile, "NWK");
        } else {
            mainController.getGraphController().getGraph().getNodeMapFromFile(selectedFile.toString());
            mainController.initGraph();
        }
    }

    /**
     * Method that creates a directoryChooser.
     *
     * @return the directoryChooser
     */
    public static FileChooser createTreeChooser() {
        FileChooser directoryChooser = new FileChooser();
        directoryChooser.setTitle("Select Tree File");

        Stack<String> mostRecentDir = mainController.getMostRecentDir();

        if (!mostRecentDir.isEmpty()) {
            File initialFile = new File(mostRecentDir.get(0));
            if (initialFile != null && initialFile.isDirectory()) {
                directoryChooser.setInitialDirectory(initialFile);
            }
        }

        File selectedFile = directoryChooser.showOpenDialog(window);

        if (selectedFile != null) {
            File parentDir = selectedFile.getParentFile();

            mainController.addRecentDir(parentDir.getAbsolutePath());

            createNWKpopup(parentDir, selectedFile);
        }
        return directoryChooser;
    }

    /**
     * Method the create a pop-up when choosing a GFA File
     *
     * @param parentDir    the current Directory we're at
     * @param selectedFile the selected File
     */
    @SuppressFBWarnings
    public static void createNWKpopup(File parentDir, File selectedFile) {
        ArrayList<Text> candidates = new ArrayList<>();
        if (parentDir.isDirectory()) {
            for (File f : parentDir.listFiles()) {
                String ext = FilenameUtils.getExtension(f.getName());
                if (ext.equals("gfa")) {
                    Text t = new Text(f.getAbsolutePath());
                    candidates.add(t);
                }
            }
        }

        mainController.setBackground("/background_images/loading.png");
        if (!candidates.isEmpty()) {

            showPopup(candidates, selectedFile, "GFA");
        } else {
            mainController.addRecentNWK(selectedFile.getAbsolutePath());
            mainController.initTree(selectedFile.getAbsolutePath());
        }
    }

    /**
     * Method to show the created NWK pop-up
     *
     * @param candidates   all candidates which can be loaded next
     * @param selectedFile the currently selected GFA File
     * @param type         The type
     */
    public static void showPopup(ArrayList<Text> candidates, File selectedFile, String type) {
        Stage tempStage = new Stage();
        ListView listView = new ListView();

        fillList(listView, candidates);
        handleTempStage(tempStage, type, listView);

        if (type.toUpperCase().equals("NWK")) {
            addGFAEventHandler(listView, selectedFile, tempStage);
        } else if (type.toUpperCase().equals("GFA")) {
            addNWKEventHandler(listView, selectedFile, tempStage);
        }
    }

    /**
     * Method to add the needed EventHandler to the List of Files
     *
     * @param listView        the List of Files
     * @param selectedGFAFile the Files which is selected
     * @param tempStage       the currently showed Stage
     */
    public static void addGFAEventHandler(ListView listView, File selectedGFAFile, Stage tempStage) {
        listView.setOnMouseClicked(event -> {
            Text file = (Text) listView.getSelectionModel().getSelectedItem();
            File nwk = new File(file.getText());

            tempStage.hide();

            if (!mainController.isMetaDataLoaded()) {
                createMetaDatapopup(selectedGFAFile, nwk);
            } else {
                mainController.addRecentGFA(selectedGFAFile.getAbsolutePath());
                mainController.getGraphController().getGraph().getNodeMapFromFile(selectedGFAFile.getAbsolutePath());
                mainController.initGraph();
                mainController.addRecentNWK(nwk.getAbsolutePath());
                mainController.initTree(nwk.getAbsolutePath());

                createMenuWithSearchWithoutAnnotation();
            }
        });
    }

    /**
     * Method to create the MetaData Pop-up
     *
     * @param gfaFile the earlier chosen GFA-File
     * @param nwkFile the earlier chosen NWK-File
     */
    public static void createMetaDatapopup(File gfaFile, File nwkFile) {
        ArrayList<Text> candidates = new ArrayList<>();
        File parentDir = gfaFile.getParentFile();
        if (parentDir.isDirectory()) {
            File[] fileList = parentDir.listFiles();
            if (fileList != null) {
                for (File f : fileList) {
                    String ext = FilenameUtils.getExtension(f.getName());
                    if (ext.equals("xlsx")) {
                        Text t = new Text(f.getAbsolutePath());
                        candidates.add(t);
                    }
                }
            }
        }

        File[] files = new File[4];
        files[0] = gfaFile;
        files[1] = nwkFile;

        mainController.setBackground("/background_images/loading.png");
        if (!candidates.isEmpty()) {
            showMetaAnnoPopup(candidates, files, "metaData");
        } else {
            mainController.getGraphController().getGraph().getNodeMapFromFile(gfaFile.toString());
            mainController.initGraph();
            mainController.addRecentNWK(nwkFile.getAbsolutePath());
            mainController.initTree(nwkFile.getAbsolutePath());
        }
    }

    /**
     * Method to create a PopUp when no Annotation Data is loaded
     *
     * @param files the currently chosen files
     */
    public static void createAnnotationPopup(File[] files) {
        ArrayList<Text> candidates = new ArrayList<>();
        File gfaFile = files[0];
        File parentDir = gfaFile.getParentFile();
        if (parentDir.isDirectory()) {
            File[] fileList = parentDir.listFiles();
            if (fileList != null) {
                for (File f : fileList) {
                    String ext = FilenameUtils.getExtension(f.getName());
                    if (ext.equals("gff")) {
                        Text t = new Text(f.getAbsolutePath());
                        candidates.add(t);
                    }
                }
            }
        }

        if (!candidates.isEmpty()) {
            showMetaAnnoPopup(candidates, files, "gff");
        } else {
            chooseCorrectFile(files);
        }

    }

    /**
     * Method to create and show the MetaData Pop-up
     *
     * @param candidates all candidates that can be opened
     * @param files      the list of selected Files
     * @param type       the type
     */
    public static void showMetaAnnoPopup(ArrayList<Text> candidates, File[] files, String type) {
        Stage tempStage = new Stage();
        ListView listView = new ListView();

        fillList(listView, candidates);
        handleTempStage(tempStage, type, listView);

        if (type.equals("metaData")) {
            addMetaDataEventHandler(listView, files, tempStage);
        }

        if (type.equals("gff")) {
            addGFFEventHandler(listView, files, tempStage);
        }
    }

    private static void fillList(ListView listView, ArrayList<Text> candidates) {
        ObservableList<Text> list = FXCollections.observableArrayList();

        listView.getStylesheets().add("/css/popup.css");

        listView.setMinWidth(450);
        listView.setPrefWidth(450);
        listView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);

        for (Text t : candidates) {
            t.setWrappingWidth(listView.getPrefWidth());
            t.getStyleClass().add("text");
        }

        list.addAll(candidates.stream().collect(Collectors.toList()));
        listView.setItems(list);
    }


    /**
     * Method to create the popup
     *
     * @param tempStage the Stage to show it on
     * @param type      the type of File we want to load
     * @param listView  the ListView to add the information to
     */
    private static void handleTempStage(Stage tempStage, String type, ListView listView) {
        Text text = new Text("Do you also want to load one of the following files? If not, exit.");
        text.setWrappingWidth(listView.getPrefWidth());
        text.getStyleClass().add("title");

        VBox vBox = new VBox();
        vBox.getStylesheets().add("/css/popup.css");
        vBox.getStyleClass().add("vBox");
        vBox.getChildren().addAll(text, listView);

        Scene tempScene = new Scene(vBox);

        tempStage.setScene(tempScene);
        tempStage.initModality(Modality.APPLICATION_MODAL);
        tempStage.setTitle(format("Load additional %s file", type));

        tempStage.setResizable(false);
        tempStage.show();
    }


    /**
     * Method to add the needed EventHandler to the List of Files
     *
     * @param listView     the List of Files
     * @param selectedFile the Files which is selected
     * @param tempStage    the currently showed Stage
     */
    public static void addNWKEventHandler(ListView listView, File selectedFile, Stage tempStage) {
        listView.setOnMouseClicked(event -> {
            Text file = (Text) listView.getSelectionModel().getSelectedItem();
            File nwk = new File(file.getText());
            tempStage.hide();

            if (!mainController.isMetaDataLoaded()) {
                createMetaDatapopup(selectedFile, nwk);
            } else {
                mainController.getGraphController().getGraph().getNodeMapFromFile(nwk.getAbsolutePath());
                mainController.initGraph();
                mainController.addRecentNWK(selectedFile.getAbsolutePath());
                mainController.initTree(selectedFile.getAbsolutePath());
                mainController.addRecentGFA(nwk.getAbsolutePath());
                createMenuWithSearchWithoutAnnotation();
            }
        });
    }

    /**
     * Method to add EventHandlers to the MetaData Pop-uo
     *
     * @param listView  the listView showing
     * @param files     the list of chosen Files
     * @param tempStage the Stage of the shown window
     */
    public static void addMetaDataEventHandler(ListView listView, File[] files, Stage tempStage) {
        listView.setOnMouseClicked(event -> {
            Text file = (Text) listView.getSelectionModel().getSelectedItem();
            File meta = new File(file.getText());

            tempStage.hide();

            files[2] = meta;

            createAnnotationPopup(files);
        });
    }

    /**
     * Method to add an Event Handler to the GFF (Annotation) Pop Up
     *
     * @param listView  the listView showing
     * @param files     the list of chosen Files
     * @param tempStage the Stage of the shown window
     */
    public static void addGFFEventHandler(ListView listView, File[] files, Stage tempStage) {
        listView.setOnMouseClicked(event -> {
            Text file = (Text) listView.getSelectionModel().getSelectedItem();
            File annotation = new File(file.getText());
            files[3] = annotation;

            tempStage.hide();

            if (files[0] != null && files[1] != null && files[2] != null) {
                chooseCorrectFile(files);
            }
        });

        tempStage.setOnCloseRequest(event -> chooseCorrectFile(files));
    }


    /**
     * Method to make sure we load the file with the right method
     *
     * @param files the files to load
     */
    public static void chooseCorrectFile(File[] files) {
        File gfaFile = files[0];
        File nwkFile = files[1];
        File metaFile = files[2];
        File annoFile = files[3];

        if (FilenameUtils.getExtension(gfaFile.getName()).equals("nwk")) {
            mainController.getGraphController().getGraph().getNodeMapFromFile(nwkFile.getAbsolutePath());
            mainController.initGraph();
            mainController.addRecentGFA(nwkFile.getAbsolutePath());

            if (annoFile != null) {
                mainController.initAnnotations(annoFile.getAbsolutePath());
            }

            mainController.initMetadata(metaFile.getAbsolutePath());
            mainController.initTree(gfaFile.getAbsolutePath());
            mainController.addRecentNWK(gfaFile.getAbsolutePath());

            createMenuWithSearchWithoutAnnotation();
        } else {
            mainController.getGraphController().getGraph().getNodeMapFromFile(gfaFile.getAbsolutePath());
            mainController.initGraph();
            mainController.addRecentGFA(gfaFile.getAbsolutePath());

            if (annoFile != null) {
                mainController.initAnnotations(annoFile.getAbsolutePath());
            }

            mainController.initMetadata(metaFile.getAbsolutePath());
            mainController.initTree(nwkFile.getAbsolutePath());
            mainController.addRecentNWK(nwkFile.getAbsolutePath());

            createMenuWithSearchWithoutAnnotation();
        }
    }

    /**
     * Method that creates a directoryChooser.
     *
     * @param s the title of the directoryChooser
     * @return the directoryChooser
     */
    public static FileChooser createAnnotationChooser(String s) {
        FileChooser directoryChooser = new FileChooser();
        directoryChooser.setTitle(s);

        Stack<String> mostRecentDir = mainController.getMostRecentDir();

        if (!mostRecentDir.isEmpty()) {
            File initialFile = new File(mostRecentDir.get(0));
            if (initialFile != null && initialFile.isDirectory()) {
                directoryChooser.setInitialDirectory(initialFile);
            }
        }

        File selectedFile = directoryChooser.showOpenDialog(window);
        if (selectedFile != null) {
            mainController.initAnnotations(selectedFile.getAbsolutePath());
            mainController.addRecentGFF(selectedFile.getAbsolutePath());
        }
        return directoryChooser;
    }

    /**
     * Method to create to alert
     * Shown when no strains are selected but we still want to see the graph
     */
    public static void createAlert() {
        final Stage dialog = new Stage();
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.setResizable(false);
        dialog.setTitle("Not enough strains selected");

        VBox content = new VBox();

        addAlertComponents(content, dialog, "Please select two or more strains first.");

        Scene dialogScene = new Scene(content, 250, 100);
        dialog.setScene(dialogScene);
        dialog.show();
    }

    /**
     * Shows a pop-up when no annotations have been found.
     */
    public static void createAnnNotFoundAlert() {
        final Stage dialog = new Stage();
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.setResizable(false);
        dialog.setTitle("Annotation not found");

        VBox content = new VBox();

        addAlertComponents(content, dialog, "Your search does not match any of\n the present annotations.");

        Scene dialogScene = new Scene(content, 250, 100);
        dialog.setScene(dialogScene);
        dialog.show();
    }

    /**
     * Shows a pop-up when no annotations have been found.
     */
    public static void createTooManyAnnAlert() {
        final Stage dialog = new Stage();
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.setResizable(false);
        dialog.setTitle("Too many matches found");

        VBox content = new VBox();

        addAlertComponents(content, dialog, "Annotation search resulted in\n too many matches.");

        Scene dialogScene = new Scene(content, 250, 100);
        dialog.setScene(dialogScene);
        dialog.show();
    }

    public static void createTooManyFilters() {
        final Stage dialog = new Stage();
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.setResizable(false);
        dialog.setTitle("Too many filters applied");

        VBox content = new VBox();

        addAlertComponents(content, dialog, "Too many filters applied,\n no results found.");

        Scene dialogScene = new Scene(content, 250, 100);
        dialog.setScene(dialogScene);
        dialog.show();
    }

    /**
     * Method to add the components to the alert pop up
     *
     * @param content   the content to be set
     * @param dialog    the dialog to add the content to
     * @param textToAdd The text to be displayed in the popup
     */
    public static void addAlertComponents(VBox content, Stage dialog, String textToAdd) {
        content.getStylesheets().add("/css/popup.css");
        content.getStyleClass().add("vBox");
        content.setAlignment(Pos.CENTER);
        content.setPadding(new Insets(10, 10, 10, 10));
        content.setSpacing(10);
        Text text = new Text(textToAdd);
        text.getStyleClass().add("text");

        Button ok = new Button();
        ok.getStyleClass().add("button");
        ok.setText("OK");
        ok.setOnMouseClicked(event -> dialog.hide());

        content.getChildren().addAll(text, ok);
    }

    /**
     * Creates the menu including a search bar with the annotations search box.
     */
    public static void createMenuWithSearch() {
        mainController.createMenu(true, true);
    }

    /**
     * Creates the menu including a search bar without the annotations search box.
     */
    public static void createMenuWithSearchWithoutAnnotation() {
        mainController.createMenu(true, false);
    }

}