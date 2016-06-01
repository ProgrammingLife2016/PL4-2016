package core;

import core.genome.Genome;
import org.junit.Test;

import java.io.InputStream;
import java.util.TreeMap;

import static org.junit.Assert.assertEquals;

/**
 * Test for the MetaData class.
 *
 * @author Niels Warnars
 */
public class MetaDataTest {

    /**
     * Tests the parse method.
     */
    @Test
    public void testParse() {
        InputStream is = getClass().getResourceAsStream("/metadataTestFile.xlsx");
        TreeMap<String, Genome> tm = MetaData.parse(is);

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
        assertEquals("aa", g.getTf());
    }

    /**
     * Tests the detLineage method.
     */
    @Test
    public void testDetLineage() {
        assertEquals(0, MetaData.detLineage("unknown"));
        assertEquals(8, MetaData.detLineage("LIN animal"));
        assertEquals(9, MetaData.detLineage("LIN B"));

        assertEquals(10, MetaData.detLineage("LIN CANETTII"));
        assertEquals(1, MetaData.detLineage("LIN 1"));
    }

}