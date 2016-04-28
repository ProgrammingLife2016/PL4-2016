package application.controllers;

import application.fxobjects.ZoomBox;
import application.fxobjects.graph.Graph;
import application.fxobjects.graph.Model;
import application.fxobjects.graph.cell.BaseLayout;
import application.fxobjects.graph.cell.CellLayout;
import application.fxobjects.graph.cell.CellType;
import javafx.fxml.FXML;
import javafx.scene.control.MenuBar;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import javax.swing.border.Border;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * Controller class, used when creating other controllers.
 *
 * @author Daphne van Tetering.
 * @version 1.0
 * @since 26-04-2016
 */
public class GraphViewController extends Controller<StackPane> {
    private Graph graph;
    private Stage primaryStage;
    private MenuController menuController;

    @FXML
    StackPane screen;
    @FXML
    BorderPane mainPane;
    @FXML
    MenuBar menuBar;

    /**
     * Constructor: generate a Controller.
     */
    public GraphViewController(Stage primaryStage) {
        super(new StackPane());
        loadFXMLfile("/application/fxml/graphview.fxml");

        this.graph = new Graph();
        //this.primaryStage = primaryStage;
    }

    public void initialize(URL location, ResourceBundle resources) {
        BorderPane root = new BorderPane();



        graph = new Graph();

        root.setCenter(graph.getScrollPane());

        ZoomBox box = graph.getZoomBox();

        root.setRight(box.getZoomBox());

//        Scene scene = new Scene(root);
//        scene.getStylesheets().add(getClass().getResource("/application/css/graph.css").toExternalForm());
//
//        primaryStage.setScene(scene);
//        primaryStage.show();

        addGraphComponents();

        CellLayout layout = new BaseLayout(graph, 100);
        layout.execute();

        this.getRoot().getChildren().addAll(root);
    }

    private void addGraphComponents() {
        Model model = graph.getModel();

        graph.beginUpdate();

        model.addCell("Cell A", CellType.RECTANGLE);
        model.addCell("Cell AA", CellType.TRIANGLE);
        model.addCell("Cell AB", CellType.TRIANGLE);
        model.addCell("Cell B", CellType.RECTANGLE);

        model.addEdge("Cell A", "Cell AA");
        model.addEdge("Cell A", "Cell AB");
        model.addEdge("Cell AA", "Cell B");
        model.addEdge("Cell AB", "Cell B");

        graph.endUpdate();
    }

    public Graph getGraph() {
        return graph;
    }
}
