package application.controllers;

import application.fxobjects.cell.Edge;
import application.fxobjects.cell.graph.BubbleCell;
import application.fxobjects.cell.graph.CollectionCell;
import application.fxobjects.cell.graph.IndelCell;
import application.fxobjects.cell.graph.RectangleCell;
import application.fxobjects.cell.tree.MiddleCell;
import core.graph.cell.EdgeType;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;


/**
 * Class responsible for creating a new AnnotationSearchBoxFactory.
 *
 * @author Niels Warnars
 */
public class AnnotationSearchBoxFactory {

    /**
     * Constructor - Create a new AnnotationSearchBoxFactory.
     */
    public AnnotationSearchBoxFactory() {
    }


    /**
     * Create an annotation search box that allows to search on the id of an annotation.
     * On a search action the nodes in the annotation will be highlighted.
     *
     * @return A search box.
     */
    public GridPane createSearchBox() {
        final GridPane grid = new GridPane();
        grid.setPadding(new Insets(10, 10, 10, 10));
        grid.setHgap(10);

        Text title = new Text("Highlight an annotation on its ID");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 18));
        grid.add(title, 0, 0);

        final TextField box = new TextField();
        box.setPrefColumnCount(10);
        grid.add(box, 0, 1);

        Button search = new Button("Search");
        grid.add(search, 1, 1);

        search.setOnAction(e -> {
            if ((box.getText() != null && !box.getText().isEmpty())) {
                System.out.println("Input: " + box.getText());
            } else {
                System.out.println("No input");
            }
        });

        return grid;
    }

}
