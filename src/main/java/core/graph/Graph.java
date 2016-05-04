package core.graph;

import core.Model;
import core.graph.cell.CellType;
import core.Node;
import core.Parser;

import java.util.HashMap;

/**
 * Class representing a graph.
 */
public class Graph {

    private Model model;
    private HashMap<Integer, Node> nodeMap;

    /**
     * Class constructor.
     */
    public Graph() {
        this.model = new Model();

    }

    /**
     * Get the model of the Graph.
     * @return The model of the graph.
     */
    public Model getModel() {
        return model;
    }


    public void addGraphComponents() {
        Parser parser = new Parser();
        nodeMap = parser.readGFA("src/main/resources/TB10.gfa");

        Node root = (nodeMap.get(1));
        model.addCell(root.getId(), root.getSequence(), CellType.RECTANGLE);

        for(int i = 1; i<=nodeMap.size();i++) {
            int numberOfLinks = nodeMap.get(i).getLinks().size();
            for(int j:nodeMap.get(i).getLinks()) {
                //Add next cell
                if(numberOfLinks==1) {
                    model.addCell(nodeMap.get(j).getId(), nodeMap.get(j).getSequence(), CellType.RECTANGLE);
                }
                else {
                    model.addCell(nodeMap.get(j).getId(), nodeMap.get(j).getSequence(), CellType.TRIANGLE);
                }
                //Add link from current cell to next cell
                model.addEdge(nodeMap.get(i).getId(), nodeMap.get(j).getId());
            }
        }
    }

    public void endUpdate() {
        // every cell must have a parent, if it doesn't, then the graphParent is
        // the parent
        getModel().attachOrphansToGraphParent(model.getAddedCells());

        // remove reference to graphParent
        getModel().disconnectFromGraphParent(model.getRemovedCells());

        // merge added & removed cells with all cells
        getModel().merge();

    }

}