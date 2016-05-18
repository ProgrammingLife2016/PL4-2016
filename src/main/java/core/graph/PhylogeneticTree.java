package core.graph;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

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
     * Get the model of the Tree.
     * @return The model of the Tree.
     */
    public Model getModel() {
        return model;
    }

    /**
     * Add TreeNodes to the model.
     * @throws IOException  Throw exception on read failure.
     */
    @SuppressFBWarnings({"I18N", "NP_DEREFERENCE_OF_READLINE_VALUE"})
    public void setup() throws IOException {
        model.setTree(getTreeFromFile());
        for (TreeNode leaf : model.getTree().nodes) {
            model.addCell(leaf.getKey(), leaf.getName(), CellType.PHYLOGENETIC);
        }
    }

    /**
     * Set-up the tree model from a Newick data file.
     * @return  A Newick tree.
     * @throws IOException  Throw exception on read failure.
     */
    public Tree getTreeFromFile() throws FileNotFoundException {
        File f = new File("src/main/resources/340tree.rooted.TKK.nwk");
        BufferedReader r = new BufferedReader(new FileReader(f));
        TreeParser tp = new TreeParser(r);

        return tp.tokenize("340tree.rooted.TKK");
    }
}
