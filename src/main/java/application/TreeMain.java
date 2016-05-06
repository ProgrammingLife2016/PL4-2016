package application;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

/**
 * Created by Niek on 4/25/2016.
 */
public class TreeMain {

    /**
     * Class constructor.
     */
    public TreeMain() {

    }
    TreeItem root;
    int currentDepth = 0;

    /**
     * Set-up the initialization of the tree.
     * @throws IOException Throw an exception on read failure.
     */
    void setup() throws IOException {
        File f = new File("src/main/resources/340tree.rooted.TKK.nwk");
        BufferedReader r = new BufferedReader(new FileReader(f));
        String t = r.readLine();
        root = TreeParser.parse(t);
        System.out.println(root.toString());
    }

    /**
     * Main class for initializing the tree parser as seperate program.
     * @param args Optional cli arguments.
     */
/*    public static void main(String args[]) {
        try {
            TreeMain tm = new TreeMain();
            tm.setup();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }*/
}
