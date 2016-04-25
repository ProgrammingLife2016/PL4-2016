package application;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

/**
 * Created by Niek on 4/25/2016.
 */
public class TreeMain {

    Tree treeoflife;
    int current_depth = 0;

    void setup() throws IOException {
        BufferedReader r = new BufferedReader(new FileReader(new File("340tree_rooted_TKK.nwk")));
        TreeParser tp = new TreeParser(r);
        treeoflife = tp.tokenize(1, "treeoflife", null);
        int tree_height = treeoflife.getHeight();
        System.out.println("largest tree height is: " + tree_height);
        recursive_print(0, 0);
    }

    void recursive_print (int currkey, int currdepth) {
        TreeNode currNode = treeoflife.getNodeByKey(currkey);
        int numChildren = currNode.numberChildren();
        for (int i = 0; i < numChildren; i++) {
            int childkey = currNode.getChild(i).key;
            TreeNode childnode = treeoflife.getNodeByKey(childkey);
            System.out.println("child name is: " + childnode.getName()
                    + " depth is: " + currdepth);
            recursive_print(childkey, currdepth+1);
        }
    }

    public void main(String args[]) {
        try {
            setup();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
