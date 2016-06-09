package application.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.*;
import org.apache.commons.io.FilenameUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.stream.Collectors;

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

        if (selectedFile != null) {
            mainController.addRecentGFA(selectedFile.toString());
        }

        File parentDir = selectedFile.getParentFile();

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

        if (!candidates.isEmpty()) {
            showGFApopup(candidates);
        } else {
            mainController.getGraphController().getGraph().getNodeMapFromFile(selectedFile.toString());
            mainController.initGraph();
        }


        return directoryChooser;
    }

    public static void showGFApopup(ArrayList<Text> candidates) {
        Stage tempStage = new Stage();

        ListView listView = new ListView();
        ObservableList<Text> list = FXCollections.observableArrayList();

        listView.setMinWidth(450);
        listView.setPrefWidth(450);
        listView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);

        Text text = new Text("Do you also want to load one of the following files? If not, press cancel");
        text.setWrappingWidth(listView.getPrefWidth());

        for (Text t : candidates) {
            t.setWrappingWidth(listView.getPrefWidth());
        }

        list.addAll(candidates.stream().collect(Collectors.toList()));
        listView.setItems(list);

        VBox vBox = new VBox();
        vBox.getChildren().addAll(text, listView);

        Scene tempScene = new Scene(vBox);

        tempStage.setScene(tempScene);
        tempStage.initModality(Modality.APPLICATION_MODAL);
        tempStage.setTitle("Load additional NWK file");
        
        tempStage.show();

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

        mainController.addRecentNWK(selectedFile.toString());

        mainController.initTree(selectedFile.getAbsolutePath());

        return directoryChooser;
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

        return directoryChooser;
    }

    /**
     * Creates the menu including a searchBar.
     */
    public static void createMenuWithSearch() {
        mainController.createMenu(true);
    }


}