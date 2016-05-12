package core.graph

/**
 * Created by Ties on 11-5-2016.
 */
class GraphTest extends GroovyTestCase {

    Graph gh = new Graph()

    /**
     * Setup for this test.
     */
    void setup() {
        gh.addGraphComponents();
    }

    /**
     * Test is onject is not null.
     */
    void testConstructor() {
        assertNotNull(gh)
    }

    /**
     * Since we dont know which file we read, we can only assert NOT null.
     */
    void testGetModel() {
        assertNotNull(gh.getModel())
    }

    /**
     * Method will return true is it successfully loads a file.
     */
    void testAddGraphComponents() {
        //assertTrue(gh.addGraphComponents());
    }

    void testEndUpdate() {

    }

    void testAddPhylogeneticTree() {

    }

    void testSetup() {

    }

    void testIntersection() {
        List l1 = new ArrayList();
        List l2 = new ArrayList();

        l1.add("a");
        l2.add("a");
        l1.add("b");
        l2.add("c");

        //assertEquals(1,g.intersection(l1,l2));
    }
}
