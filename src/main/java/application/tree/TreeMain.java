package application.tree;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

import java.io.IOException;

/**
 * Created by Niek on 4/25/2016.
 */
public class TreeMain {

    /**
     * Main of this class.
     */
    public static void main() {
        try {
            TreeMain tm = new TreeMain();
            tm.setup();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //TreeItem root;
    //int currentDepth = 0;

    /**
     * The setup method for this class.
     *
     * @throws IOException Throws exception on read failure.
     */
    @SuppressFBWarnings({"I18N", "NP_DEREFERENCE_OF_READLINE_VALUE", "OS_OPEN_STREAM"})
    void setup() throws IOException {
        //File f = new File("src/main/resources/340tree.rooted.TKK.nwk");
        //BufferedReader r = new BufferedReader(new FileReader(f));
        //String t = r.readLine();
        //root = TreeParser.parse(t);
        //System.out.println(root.toString());
    }
}
