package core.parsers;

import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
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
     * Tests the readGFAFromFile method.
     */
    @Test
    public void testReadGFAFromFile() {
        try {
            HashMap<Integer, Node> map = p.readGFAFromFile("src/main/resources/TestFiles/TBTestFile.gfa");
            assertEquals(4, map.size());

            for (int i = 1; i <= 4; i++) {
                Node n = map.get(i);
                assertEquals(i, n.getId());
                assertEquals(Character.toString((char) ('A' + i - 1)), n.getSequence());
                assertEquals(i, n.getzIndex());
                assertEquals("GENOME_1", n.getGenomes().get(0));
                assertEquals(Integer.valueOf(i + 1), n.getLinks().get(0));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}