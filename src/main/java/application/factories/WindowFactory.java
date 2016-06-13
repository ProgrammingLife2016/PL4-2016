package application.factories;

import application.controllers.MainController;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
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

        File selectedFile = directoryChooser.showOpenDialog(window);
        mainController.addRecentGFA(selectedFile.getAbsolutePath());

        File parentDir = selectedFile.getParentFile();
        createGFApopup(parentDir, selectedFile);

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
     * Method to add the needed EventHandler to the List of Files
     *
     * @param listView     the List of Files
     * @param selectedFile the Files which is selected
     * @param tempStage    the currently showed Stage
     */
    public static void addGFAEventHandler(ListView listView, File selectedFile, Stage tempStage) {
        listView.setOnMouseClicked(event -> {
            Text file = (Text) listView.getSelectionModel().getSelectedItem();
            File f = new File(file.getText());

            tempStage.hide();
            mainController.addRecentNWK(f.getAbsolutePath());
            mainController.initTree(f.getAbsolutePath());
            mainController.addRecentGFA(selectedFile.getAbsolutePath());
            mainController.getGraphController().getGraph().getNodeMapFromFile(selectedFile.getAbsolutePath());
            mainController.initGraph();
            createMenuWithSearch();

        });
    }

    /**
     * Method that creates a directoryChooser.
     *
     * @return the directoryChooser
     */
    public static FileChooser createTreeChooser() {
        FileChooser directoryChooser = new FileChooser();
        directoryChooser.setTitle("Select Tree File");

        File selectedFile = directoryChooser.showOpenDialog(window);
        File parentDir = selectedFile.getParentFile();
        createNWKpopup(parentDir, selectedFile);

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
        ObservableList<Text> list = FXCollections.observableArrayList();

        listView.getStylesheets().add("/css/popup.css");

        listView.setMinWidth(450);
        listView.setPrefWidth(450);
        listView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);

        Text text = new Text("Do you also want to load one of the following files? If not, exit.");
        text.setWrappingWidth(listView.getPrefWidth());
        text.getStyleClass().add("title");

        for (Text t : candidates) {
            t.setWrappingWidth(listView.getPrefWidth());
            t.getStyleClass().add("text");
        }

        list.addAll(candidates.stream().collect(Collectors.toList()));
        listView.setItems(list);

        VBox vBox = new VBox();
        vBox.getChildren().addAll(text, listView);

        vBox.getStylesheets().add("/css/popup.css");
        vBox.getStyleClass().add("vBox");

        Scene tempScene = new Scene(vBox);

        tempStage.setScene(tempScene);
        tempStage.initModality(Modality.APPLICATION_MODAL);
        tempStage.setTitle(format("Load additional %s file", type));

        tempStage.setResizable(false);
        tempStage.show();

        if (type.toUpperCase().equals("NWK")) {
            addGFAEventHandler(listView, selectedFile, tempStage);
        } else if (type.toUpperCase().equals("GFA")) {
            addNWKEventHandler(listView, selectedFile, tempStage);
        }
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
            String f = file.getText();

            tempStage.hide();
            mainController.getGraphController().getGraph().getNodeMapFromFile(f);
            mainController.initGraph();
            mainController.addRecentNWK(selectedFile.getAbsolutePath());
            mainController.initTree(selectedFile.getAbsolutePath());
            mainController.addRecentGFA(f);
            createMenuWithSearch();

        });
    }


    /**
     * Method that creates a directoryChooser.
     *
     * @return the directoryChooser
     */
    public static FileChooser createAnnotationChooser() {
        FileChooser directoryChooser = new FileChooser();
        directoryChooser.setTitle("Select Annotation File");

        File selectedFile = directoryChooser.showOpenDialog(window);
        mainController.initAnnotations(selectedFile.getAbsolutePath());
        mainController.addRecentGFF(selectedFile.getAbsolutePath());

        return directoryChooser;
    }

    /**
     * Method that creates a directoryChooser.
     *
     * @return the directoryChooser
     */
    public static FileChooser createMetadataChooser() {
        FileChooser directoryChooser = new FileChooser();
        directoryChooser.setTitle("Select Metadata File");

        File selectedFile = directoryChooser.showOpenDialog(window);
        mainController.initMetadata(selectedFile.getAbsolutePath());
        mainController.addRecentMetadata(selectedFile.getAbsolutePath());

        return directoryChooser;
    }

    /**
     * Creates the menu including a searchBar.
     */
    public static void createMenuWithSearch() {
        mainController.createMenu(true);
    }


}