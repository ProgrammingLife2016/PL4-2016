package core.graph;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;

import core.Model;
import core.graph.cell.CellType;
import net.sourceforge.olduvai.treejuxtaposer.TreeParser;
import net.sourceforge.olduvai.treejuxtaposer.drawer.Tree;
import net.sourceforge.olduvai.treejuxtaposer.drawer.TreeNode;

/**
 * Created by Niek on 4/25/2016.
 */
public class TreeMain {

    private Tree tree;
    private Model model;

    /**
     * Class constructor.
     */
    public TreeMain() {
        this.model = new Model();
    }

    /**
     * Get the model of the Tree.
     * @return The model of the Tree.
     */
    public Model getModel() {
        return model;
    }

    public void setup() throws IOException {
        File f = new File("src/main/resources/340tree.rooted.TKK.nwk");
        BufferedReader r = new BufferedReader(new FileReader(f));
        TreeParser tp = new TreeParser(r);

        model.setTree(tp.tokenize("340tree.rooted.TKK"));
        for (TreeNode leaf : model.getTree().nodes) {
            model.addCell(leaf.getKey(), leaf.getName(), CellType.PHYLOGENETIC);
        }
    }

    /**
     * Method that updates the model.
     */
    public void endUpdate() {
        model.merge();
    }
}
