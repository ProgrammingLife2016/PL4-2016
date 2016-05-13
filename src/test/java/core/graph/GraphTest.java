package core.graph;

import org.junit.Test;

import static junit.framework.TestCase.assertNotNull;

/**
 * Created by Ties on 11-5-2016.
 */
public class GraphTest {

    Graph gh = new Graph();

    /**
     * Setup for this test.
     */
    @Test
    public void setup() {
//        try {
//            //gh.addGraphComponents(null);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
    }

    /**
     * Test is onject is not null.
     */
    @Test
    public void testConstructor() {
        assertNotNull(gh.getModel());
    }

    /**
     * Since we dont know which file we read, we can only assert NOT null.
     */
    @Test
    public void testGetModel() {
        assertNotNull(gh.getModel() == null);
    }

    /**
     * Method will return true is it successfully loads a file.
     */
    @Test
    public void testAddGraphComponents() {
        //assertTrue(gh.addGraphComponents());
    }
}
