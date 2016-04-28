package application.fxobjects.graph;

import application.controllers.ZoomController;
import application.fxobjects.graph.cell.Cell;
import application.fxobjects.graph.cell.CellLayer;
import javafx.scene.Group;
import javafx.scene.layout.Pane;

public class Graph {

    private Model model;

    private Group canvas;

    private ZoomController zoomController;

    MouseHandling mouseHandler;

    /**
     * the pane wrapper is necessary or else the scrollpane would always align
     * the top-most and left-most child to the top and left eg when you drag the
     * top child down, the entire scrollpane would move down
     */
    CellLayer cellLayer;

    public Graph() {

        this.model = new Model();

        canvas = new Group();
        cellLayer = new CellLayer();

        canvas.getChildren().addAll(cellLayer);

        mouseHandler = new MouseHandling(this);

        zoomController = new ZoomController(canvas);

    }


    public Pane getCellLayer() {
        return this.cellLayer;
    }

    public ZoomController getZoomController() { return zoomController; }

    public Model getModel() {
        return model;
    }


    public void beginUpdate() {
    }

    public void endUpdate() {

        // add components to graph pane
        getCellLayer().getChildren().addAll(model.getAddedEdges());
        getCellLayer().getChildren().addAll(model.getAddedCells());

        // remove components from graph pane
        getCellLayer().getChildren().removeAll(model.getRemovedCells());
        getCellLayer().getChildren().removeAll(model.getRemovedEdges());

        // enable dragging of cells
        for (Cell cell : model.getAddedCells()) {
            mouseHandler.makeDraggable(cell);
        }

        // every cell must have a parent, if it doesn't, then the graphParent is
        // the parent
        getModel().attachOrphansToGraphParent(model.getAddedCells());

        // remove reference to graphParent
        getModel().disconnectFromGraphParent(model.getRemovedCells());

        // merge added & removed cells with all cells
        getModel().merge();

    }

    public double getScale() {
        return zoomController.getZoomPane().getScaleValue();
    }
}