//package core;
//
//import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
//import org.junit.Before;
//import org.junit.Test;
//
//import java.io.FileInputStream;
//import java.io.IOException;
//import java.io.InputStream;
//import java.util.HashMap;
//
//import static org.junit.Assert.*;
//
///**
// * Created by user on 18-5-2016.
// */
//public class ParserTest {
//    public Parser p;
//
//    /**
//     * Initialize the Parser before testing it.
//     */
//    @Before
//    public void setUp() {
//        p = new Parser();
//    }
//
//    /**
//     * Test the constructor.
//     */
//    @Test
//    public void testConstructor() {
//        assertNotNull(p);
//    }
//
//    /**
//     * Test the readGFA method.
//     */
//    @SuppressFBWarnings({"DLS_DEAD_LOCAL_STORE",
//            "OBL_UNSATISFIED_OBLIGATION_EXCEPTION_EDGE"})
//    @Test
//    public void testReadGFA() {
//        HashMap<Integer, Node> map = new HashMap<>();
//
//        try {
//            InputStream is = new FileInputStream("src\\main\\resources\\TestFiles\\TBTestFile.gfa");
//            map = p.readGFA(is);
//            is.close();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//        assertEquals(4, map.size());
//
//        for (int i = 1; i <= 4; i++) {
//            Node n = map.get(i);
//            assertEquals(i, n.getId());
//            assertEquals(Character.toString((char) ('A' + i - 1)), n.getSequence());
//            assertEquals(i, n.getzIndex());
//            assertEquals("GENOME_1", n.getGenomes().get(0));
//            assertEquals(Integer.valueOf(i + 1), n.getLinks().get(0));
//        }
//    }
//
//}