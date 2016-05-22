package core.graph;

import java.io.*;

import core.Model;
import core.graph.cell.CellType;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import net.sourceforge.olduvai.treejuxtaposer.TreeParser;
import net.sourceforge.olduvai.treejuxtaposer.drawer.Tree;
import net.sourceforge.olduvai.treejuxtaposer.drawer.TreeNode;

/**
 * Class representing a phylogenetic tree.
 * Created by Niek on 4/25/2016.
 */
public class PhylogeneticTree {

    private Model model;

    /**
     * Class constructor.
     */
    public PhylogeneticTree() {
        this.model = new Model();
    }

    /**
     * Get the model of the PhylogeneticTree.
     *
     * @return The model of the PhylogeneticTree.
     */
    public Model getModel() {
        return model;
    }

    /**
     * Set the model of the PhylogeneticTree.
     *
     * @param model The model of the PhylogeneticTree.
     */
    public void setModel(Model model) {
        this.model = model;
    }

    /**
     * Set-up the tree model from a Newick data file.
     *
     * @return A Newick tree.
     * @throws IOException Throw exception on read failure.
     */
    @SuppressFBWarnings({"I18N", "NP_DEREFERENCE_OF_READLINE_VALUE"})
    public Tree getTreeFromFile() throws IOException {
        InputStream stream = this.getClass().getResourceAsStream("/340tree.rooted.TKK.nwk");
        BufferedReader r = new BufferedReader(new InputStreamReader(stream));
        TreeParser tp = new TreeParser(r);

        return tp.tokenize("340tree.rooted.TKK");
    }

    /**
     * Add TreeNodes to the model.
     *
     * @throws IOException Throw exception on read failure.
     */
    public void setup() throws IOException {
        Tree tree = getTreeFromFile();
        model.setTree(tree);

        for (TreeNode node : tree.nodes) {
            if (node.isLeaf()) {
                model.addCell(node.getKey(), node.getName(), CellType.TREELEAF);
            } else {
                model.addCell(node.getKey(), node.getName(), CellType.TREEMIDDLE);
            }
        }
    }

    /**
     * Method that updates the model.
     */
    public void endUpdate() {
        model.merge();
    }
}
