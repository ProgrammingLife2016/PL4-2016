package core;

import core.genome.Genome;
import core.parsers.MetaDataParser;
import org.junit.Test;

import java.io.InputStream;
import java.util.TreeMap;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

/**
 * Test for the MetaData class.
 *
 * @author Niels Warnars
 */
public class MetaDataParserTest {

    /**
     * Tests the parse method.
     */
    @Test
    public void testParse() {
        InputStream is = getClass().getResourceAsStream("/TestFiles/metadataTestFile.xlsx");
        TreeMap<String, Genome> tm = MetaDataParser.parse(is);

        assertEquals(1, tm.size());
        Genome g = tm.get("TKKa");

        assertEquals(1, g.getAge());
        assertEquals("c", g.getSex());
        assertEquals(true, g.isHiv());
        assertEquals("e", g.getCohort());
        assertEquals("g", g.getStudyDistrict());
        assertEquals("h", g.getSpecimenType());
        assertEquals("j", g.getSmearStatus());
        assertEquals("k", g.getIsolation());
        assertEquals("l", g.getPhenoDST());
        assertEquals("m", g.getCapreomycin());
        assertEquals("n", g.getEthambutol());
        assertEquals("o", g.getEthionamide());
        assertEquals("p", g.getIsoniazid());
        assertEquals("q", g.getKanamycin());
        assertEquals("s", g.getPyrazinamide());
        assertEquals("t", g.getOfloxacin());
        assertEquals("u", g.getRifampin());
        assertEquals("v", g.getStreptomycin());
        assertEquals("w", g.getSpoligotype());
        assertEquals("y", g.getGenoDST());
        assertEquals(4, g.getLineage());
        assertFalse(g.isTf());
    }

    /**
     * Tests the detLineage method.
     */
    @Test
    public void testDetLineage() {
        assertEquals(0, MetaDataParser.detLineage("unknown"));
        assertEquals(8, MetaDataParser.detLineage("LIN animal"));
        assertEquals(9, MetaDataParser.detLineage("LIN B"));

        assertEquals(10, MetaDataParser.detLineage("LIN CANETTII"));
        assertEquals(1, MetaDataParser.detLineage("LIN 1"));
    }

}