package core.parsers;

import core.annotation.Annotation;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import org.junit.Test;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import static core.parsers.AnnotationParser.readGFFFromFile;
import static org.junit.Assert.assertEquals;

/**
 * Test class for the AnnotationParser class
 */
public class AnnotationParserTest {

    /**
     * Tests the readGFA method.
     *
     * @throws FileNotFoundException Throw exception on read error.
     */
    @SuppressFBWarnings("OBL_UNSATISFIED_OBLIGATION_EXCEPTION_EDGE")
    @Test
    public void testReadGFF() throws FileNotFoundException {
        List<Annotation> annotations = new ArrayList<>();
        InputStream is = getClass().getResourceAsStream("/TestFiles/TestFile.gff");

        try {
            annotations = AnnotationParser.readGFF(is);
            is.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        assertEquals(1, annotations.size());
        Annotation a = annotations.get(0);

        assertEquals("a", a.getSeqid());
        assertEquals("b", a.getSource());
        assertEquals("c", a.getType());
        assertEquals(1, a.getStart());
        assertEquals(2, a.getEnd());
        assertEquals(3, a.getScore(), 0.0001);
        assertEquals("d", a.getStrand());
        assertEquals("e", a.getPhase());
        assertEquals("f", a.getCallhounClassAttr());
        assertEquals(4, a.getIdAttr());
        assertEquals("g", a.getNameAttr());
        assertEquals("h", a.getDisplayNameAttr());
    }

    /**
     * Tests the readGFFFromFile method.
     */
    @Test
    public void testReadGFFFromFile() {
        List<Annotation> annotations = readGFFFromFile("src/main/resources/decorationV5_20130412.gff");
        assertEquals(4043, annotations.size());
    }

}