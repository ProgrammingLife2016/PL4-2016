package core;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import org.junit.Test;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * Created by user on 18-5-2016.
 */
public class AnnotationParserTest {

    /**
     * Test the readGFA method.
     *
     * @throws FileNotFoundException Throw exception on read error.
     */
    @SuppressFBWarnings("OBL_UNSATISFIED_OBLIGATION_EXCEPTION_EDGE")
    @Test
    public void testReadGFF() throws FileNotFoundException {
        List<Annotation> annotations = new ArrayList<Annotation>();
        InputStream is = getClass().getResourceAsStream("/TestFile.gff");

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
        assertEquals(4, a.getIdAttr(), 0.0001);
        assertEquals("g", a.getNameAttr());
        assertEquals("h", a.getDisplayNameAttr());
    }

}