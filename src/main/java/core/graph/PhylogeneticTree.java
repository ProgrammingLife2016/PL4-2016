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
     * @param s The name of the tree.
     * @return A Newick tree.
     */
    @SuppressFBWarnings({"I18N", "NP_DEREFERENCE_OF_READLINE_VALUE"})
    public Tree getTreeFromFile(String s) {
        FileInputStream fileInputStream = null;
        try {
            fileInputStream = new FileInputStream(s);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        BufferedReader r = new BufferedReader(new InputStreamReader(fileInputStream));
        TreeParser tp = new TreeParser(r);

        char[] x = s.toCharArray();
        String temp = "";
        for (int i = s.length(); i > 0; i--) {
            if (!(x[i - 1] == '\\'))  {
                temp += x[i - 1];
            } else {
                break;
            }
        }

        String f = new StringBuilder(temp).reverse().toString();
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

    /**
     * Method that updates the model.
     */
    public void endUpdate() {
        model.merge();
    }
}
