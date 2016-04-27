package application.fxobjects.graph.cell;


import java.util.List;
import java.util.Random;

import application.fxobjects.graph.Graph;
import application.fxobjects.graph.cell.Cell;
import application.fxobjects.graph.cell.CellLayout;

public class RandomCellLayout extends CellLayout {

    Graph graph;

    Random rnd = new Random();

    public RandomCellLayout(Graph graph) {

        this.graph = graph;

    }

    public void execute() {

        List<Cell> cells = graph.getModel().getAllCells();

        for (Cell cell : cells) {

            double x = rnd.nextDouble() * 500;
            double y = rnd.nextDouble() * 500;

            cell.relocate(x, y);

        }

    }

}