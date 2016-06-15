package core.filtering;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Filtering class test
 * 
 * @author Niels Warnars
 */
public class FilteringTest {

    /**
     * Tests the applyFilter method.
     */
    @Test
    public void testApplyFilter() {
        Filtering f = new Filtering();

        for (Filter filter : Filter.values()) {
            f.applyFilter(filter);
        }

        assertEquals(Filter.values().length, f.getFilters().size());
    }

    /**
     * Tests the removeFilter method.
     */
    @Test
    public void testRemoveFilter() {
        Filtering f = new Filtering();

        for (Filter filter : Filter.values()) {
            f.applyFilter(filter);
        }

        assertEquals(Filter.values().length, f.getFilters().size());

        for (Filter filter : Filter.values()) {
            f.removeFilter(filter);
        }

        assertEquals(0, f.getFilters().size());
    }

    /**
     * Tests the getSelectedGenomes method.
     */
    @Test
    public void getSelectedGenomes() {
        Filtering f = new Filtering();
        assertEquals(0, f.getSelectedGenomes().size());
    }

}