package core.parsers;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import org.junit.Before;
import org.junit.Test;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;

import core.graph.Node;

import static org.junit.Assert.assertEquals;

/**
 * Test class for the Graph
 * @author Niels Warnars
 */
public class GraphParserTest {
    public GraphParser p;

    /**
     * Initialize the Parser before testing it.
     */
    @Before
    public void setUp() {
        p = new GraphParser();
    }

    /**
     * Test the readGFA method.
     */
    @Test
    public void testReadGFA() {
        HashMap<Integer, Node> map = new HashMap<>();

        try {
            InputStream is = new FileInputStream("src/main/resources/TestFiles/TBTestFile.gfa");
            map = p.readGFA(is);
            is.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        assertEquals(4, map.size());

        for (int i = 1; i <= 4; i++) {
            Node n = map.get(i);
            assertEquals(i, n.getId());
            assertEquals(Character.toString((char) ('A' + i - 1)), n.getSequence());
            assertEquals(i, n.getzIndex());
            assertEquals("GENOME_1", n.getGenomes().get(0));
            assertEquals(Integer.valueOf(i + 1), n.getLinks().get(0));
        }
    }

    /**
     * Tests the readGFAFromFile class.
     */
    @Test
    public void testReadGFAFromFile() {
        try {
            assertEquals(4, p.readGFAFromFile("src/main/resources/TestFiles/TBTestFile.gfa").size());
        } catch (IOException e) {
        }
    }


}