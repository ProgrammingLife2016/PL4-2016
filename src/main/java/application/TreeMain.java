package application;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import net.sourceforge.olduvai.treejuxtaposer.TreeParser;
import net.sourceforge.olduvai.treejuxtaposer.drawer.Tree;
import net.sourceforge.olduvai.treejuxtaposer.drawer.TreeNode;

/**
 * Created by Niek on 4/25/2016.
 */
public class TreeMain {

    private Tree tree;

    public TreeMain() {
    }

    public void setup() throws IOException {
        File f = new File("src/main/resources/340tree.rooted.TKK.nwk");
        BufferedReader r = new BufferedReader(new FileReader(f));
        TreeParser tp = new TreeParser(r);

        tree = tp.tokenize("340tree.rooted.TKK");
        System.out.println(tree.toString());
    }

    public Tree getTree() {
        return tree;
    }
}
