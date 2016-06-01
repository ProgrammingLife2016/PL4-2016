package core.genome;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Test suite for the Genome class.
 *
 * @author Niels Warnars
 */
public class GenomeTest {
    Genome g;

    /**
     * Set up a new Genome instance.
     */
    @Before
    public void setUp() {
        g = new Genome();
    }

    /**
     * Test case for the get/setAge methods.
     */
    @Test
    public void testSetAge() {
        g.setAge(42);
        assertEquals(42, g.getAge());
    }

    /**
     * Test case for the get/setSex methods.
     */
    @Test
    public void testSetSex() {
        g.setSex("test");
        assertEquals("test", g.getSex());
    }

    /**
     * Test case for the get/setHiv methods.
     */
    @Test
    public void testSetHivBool() {
        g.setHiv(true);
        assertTrue(g.isHiv());
    }

    /**
     * Test case for the get/setHiv methods.
     */
    @Test
    public void testSetHivString() {
        g.setHiv("Positive");
        assertTrue(g.isHiv());

        g.setHiv("Negative");
        assertFalse(g.isHiv());
    }

    /**
     * Test case for the get/setCohort methods.
     */
    @Test
    public void testSetCohort() {
        g.setCohort("test");
        assertEquals("test", g.getCohort());
    }

    /**
     * Test case for the get/setStudyDistrict methods.
     */
    @Test
    public void testSetStudyDistrict() {
        g.setStudyDistrict("test");
        assertEquals("test", g.getStudyDistrict());
    }

    /**
     * Test case for the get/setSpecimenType methods.
     */
    @Test
    public void testSetSpecimenType() {
        g.setSpecimenType("test");
        assertEquals("test", g.getSpecimenType());
    }

    /**
     * Test case for the get/setSmearStatus methods.
     */
    @Test
    public void testSetSmearStatus() {
        g.setSmearStatus("test");
        assertEquals("test", g.getSmearStatus());
    }

    /**
     * Test case for the get/setIsolation methods.
     */
    @Test
    public void testSetIsolation() {
        g.setIsolation("test");
        assertEquals("test", g.getIsolation());
    }

    /**
     * Test case for the get/setPhenoDST methods.
     */
    @Test
    public void testSetPhenoDST() {
        g.setPhenoDST("test");
        assertEquals("test", g.getPhenoDST());
    }

    /**
     * Test case for the get/setCapreomycin methods.
     */
    @Test
    public void testSetCapreomycin() {
        g.setCapreomycin("test");
        assertEquals("test", g.getCapreomycin());
    }

    /**
     * Test case for the get/setEthambutol methods.
     */
    @Test
    public void testSetEthambutol() {
        g.setEthambutol("test");
        assertEquals("test", g.getEthambutol());
    }

    /**
     * Test case for the get/setEthionamide methods.
     */
    @Test
    public void testSetEthionamide() {
        g.setEthionamide("test");
        assertEquals("test", g.getEthionamide());
    }

    /**
     * Test case for the get/setIsoniazid methods.
     */
    @Test
    public void testSetIsoniazid() {
        g.setIsoniazid("test");
        assertEquals("test", g.getIsoniazid());
    }

    /**
     * Test case for the get/setKanamycin methods.
     */
    @Test
    public void testSetKanamycin() {
        g.setKanamycin("test");
        assertEquals("test", g.getKanamycin());
    }

    /**
     * Test case for the get/setPyrazinamide methods.
     */
    @Test
    public void testSetPyrazinamide() {
        g.setPyrazinamide("test");
        assertEquals("test", g.getPyrazinamide());
    }

    /**
     * Test case for the get/setOfloxacin methods.
     */
    @Test
    public void testSetOfloxacin() {
        g.setOfloxacin("test");
        assertEquals("test", g.getOfloxacin());
    }

    /**
     * Test case for the get/setRifampin methods.
     */
    @Test
    public void testSetRifampin() {
        g.setRifampin("test");
        assertEquals("test", g.getRifampin());
    }

    /**
     * Test case for the get/setStreptomycin methods.
     */
    @Test
    public void testSetStreptomycin() {
        g.setStreptomycin("test");
        assertEquals("test", g.getStreptomycin());
    }

    /**
     * Test case for the get/setSpoligotype methods.
     */
    @Test
    public void testSetSpoligotype() {
        g.setSpoligotype("test");
        assertEquals("test", g.getSpoligotype());
    }

    /**
     * Test case for the get/setGenoDST methods.
     */
    @Test
    public void testSetGenoDST() {
        g.setGenoDST("test");
        assertEquals("test", g.getGenoDST());
    }

    /**
     * Test case for the get/setLineage methods.
     */
    @Test
    public void testSetLineage() {
        g.setLineage(42);
        assertEquals(42, g.getLineage());
    }

    /**
     * Test case for the get/setTf methods.
     */
    @Test
    public void testSetTf() {
        g.setTf("test");
        assertEquals("test", g.getTf());
    }

}