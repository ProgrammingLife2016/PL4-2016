package core.phylogeneticTree;

import core.model.Model;
import core.typeEnums.CellType;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import net.sourceforge.olduvai.treejuxtaposer.TreeParser;
import net.sourceforge.olduvai.treejuxtaposer.drawer.Tree;
import net.sourceforge.olduvai.treejuxtaposer.drawer.TreeNode;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.nio.file.Paths;

/**
 * Class representing a phylogenetic tree.
 */
public class PhylogeneticTree {

    private Model model;

    /**
     * Class constructor.
     *
     * @param s The name of the tree.
     */
    public PhylogeneticTree(String s) {
        this.model = new Model();
        Tree tree = getTreeFromFile(s);

        this.setup(tree);
    }

    /**
     * Class constructor for testing purposes.
     *
     * @param model A given model.
     */
    public PhylogeneticTree(Model model) {
        this.model = model;
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
     * @param path The path to the newick file
     * @return A Newick tree.
     */
    @SuppressFBWarnings({"NP_NULL_PARAM_DEREF", "DM_DEFAULT_ENCODING", "NP_NULL_ON_SOME_PATH_FROM_RETURN_VALUE"})
    public Tree getTreeFromFile(String path) {
        FileInputStream fileInputStream = null;
        try {
            fileInputStream = new FileInputStream(path);
        } catch (FileNotFoundException e) {
            
        }

        BufferedReader r = new BufferedReader(new InputStreamReader(fileInputStream));
        TreeParser tp = new TreeParser(r);

        String f = Paths.get(path).getFileName().toString();
        return tp.tokenize(f);
    }

    /**
     * Add TreeNodes to the model.
     *
     * @param tree A Newick Tree read from disk.
     */
    public void setup(Tree tree) {
        model.setTree(tree);

        for (TreeNode node : tree.nodes) {
            if (node.isLeaf()) {
                model.addCell(node.getKey(), node.getName(), 0, CellType.TREELEAF);
            } else {
                model.addCell(node.getKey(), node.getName(), 0, CellType.TREEMIDDLE);
            }
        }
    }
}
