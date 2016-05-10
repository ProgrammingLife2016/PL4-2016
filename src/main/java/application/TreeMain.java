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
     * Constructor method of this class.
     */
    public TreeMain() {

    }

    TreeItem root;
    int currentDepth = 0;

    /**
     * The setup method for this class.
     *
     * @throws 'IOException'
     */
    void setup() throws IOException {
        File f = new File("src/main/resources/340tree.rooted.TKK.nwk");
        BufferedReader r = new BufferedReader(new FileReader(f));
        String t = r.readLine();
        root = TreeParser.parse(t);
        System.out.println(root.toString());
    }

    /**
     * Main of this class.
     *
     * @param args args.
     */
    public static void main(String args[]) {
        try {
            TreeMain tm = new TreeMain();
            tm.setup();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
