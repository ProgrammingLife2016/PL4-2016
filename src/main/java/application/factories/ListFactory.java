package application.factories;

import javafx.geometry.Rectangle2D;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.SelectionMode;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.Screen;

/**
 * ListFactory Class
 */
public class ListFactory {
    private ListView list;
    private TextFlow infoList;
    private VBox listVBox;
    private Text id;
    private ScrollPane infoScroller;
    private Rectangle2D screenSize;

    /**
     * Constructor - Create a new ListFactory
     */
    public ListFactory() {
        this.screenSize = Screen.getPrimary().getVisualBounds();
    }

    /**
     * Method to create the GUI aspects of the InfoList
     *
     * @param info the info to be shown
     * @return the create InfoList
     */
    public VBox createInfoList(String info) {
        listVBox = new VBox();
        infoScroller = new ScrollPane();

        listVBox.setMinWidth(248.0);
        listVBox.setPrefWidth(248.0);
        listVBox.setMaxWidth(248.0);

        infoScroller.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        infoScroller.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        infoScroller.prefHeightProperty().bind(listVBox.heightProperty());
        infoScroller.prefWidth(screenSize.getWidth() / 5);

        if (info.isEmpty()) {
            createList();
        }

        createNodeInfo();

        infoScroller.setContent(infoList);
        listVBox.getChildren().addAll(list, infoScroller);

        return listVBox;
    }

    /**
     * Create a list on the right side of the screen with all genomes.
     */
    private void createList() {
        list = new ListView<>();
        list.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        list.setPlaceholder(new Label("No Genomes Loaded."));
        list.prefHeightProperty().bind(listVBox.heightProperty());
        list.prefWidthProperty().bind(listVBox.widthProperty());
    }


    /**
     * Create an info panel to show the information on a node.
     */
    private void createNodeInfo() {
        infoList = new TextFlow();
        infoList.prefHeightProperty().bind(infoScroller.heightProperty());
        infoList.prefWidthProperty().bind(infoScroller.widthProperty());

        id = new Text();
        id.setText("Select Node to view info");

        infoList.getChildren().addAll(id);
    }

    /**
     * Modify the information of the Node.
     *
     * @param id desired info.
     */
    public void modifyNodeInfo(String id) {
        this.id.setText(id);
        this.id.setWrappingWidth(infoScroller.widthProperty().doubleValue());
    }

    /**
     * Getter method for the list
     *
     * @return the list
     */
    public ListView getList() {
        return list;
    }

}
