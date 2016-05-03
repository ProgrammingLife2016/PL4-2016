package application;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

/**
 * Created by Niek on 4/25/2016.
 */
public class TreeMain {


    public TreeMain() {

    }
    TreeItem root;
    int current_depth = 0;

    void setup() throws IOException {
        File f = new File("src/main/resources/340tree.rooted.TKK.nwk");
        BufferedReader r = new BufferedReader(new FileReader(f));
        String t = r.readLine();
        root = TreeParser.parse(t);
        System.out.println(root.toString());
    }

    public static void main(String args[]) {
        try {
            TreeMain tm = new TreeMain();
            tm.setup();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
