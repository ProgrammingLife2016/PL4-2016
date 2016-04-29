package application.fxobjects.graph.cell;


import java.util.List;
import java.util.Random;

import application.fxobjects.graph.Graph;

/**
 * Class representing the lay-out of a random cell
 */
public class RandomCellLayout extends CellLayout {

    Graph graph;

    Random rnd = new Random();

    /**
     * Class constructor.
     * @param graph A given graph.
     */
    public RandomCellLayout(Graph graph) {

        this.graph = graph;

    }

    /**
     * Place all given cells at a random location.
     */
    public void execute() {

        List<Cell> cells = graph.getModel().getAllCells();

        for (Cell cell : cells) {

            double x = rnd.nextDouble() * 500;
            double y = rnd.nextDouble() * 500;

            cell.relocate(x, y);

        }

    }

}