package core.annotation;

import core.graph.Node;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * Test suite for the Annotation Parser class.
 *
 */
public class AnnotationProcessorTest {

    public List<Annotation> annotations;
    public HashMap<Integer, Node> nodeMap;
    public AnnotationProcessor ap;

    /**
     * Sets up annotation and node data.
     */
    @Before
    public void setUp() {
        Node n1 = new Node(1, "abc", 1);
        Node n2 = new Node(2, "def", 4);
        Node n3 = new Node(2, "xyz", 20);

        n1.setGenomes(Collections.singletonList("test.ref"));

        nodeMap = new HashMap<>();
        nodeMap.put(1, n1);
        nodeMap.put(2, n2);
        nodeMap.put(3, n3);

        Annotation a = new Annotation();
        a.setSeqid("test");
        a.setStart(2);
        a.setEnd(5);

        annotations = new ArrayList<>();
        annotations.add(a);
        ap = new AnnotationProcessor(annotations);
    }

    /**
     * tests the filterAnnotationsInNodeMap method.
     */
    @Test
    public void testFilterAnnotationsInNodeMap() {
        HashMap<Integer, Node> map = new HashMap<>();

        Node n1 = new Node(1, "", 1);
        Node n2 = new Node(2, "", 2);

        n1.setGenomes(Collections.singletonList("test.ref"));

        map.put(1, n1);
        map.put(2, n2);

        HashMap<Integer, Node> res = ap.filterAnnotationsInNodeMap(map);
        assertEquals(1, res.size());
        assertEquals(n1, res.get(1));
    }

    /**
     * Tests the findAnnotation method.
     *
     * @throws AnnotationProcessor.TooManyAnnotationsFoundException throw exception on too many
     * matching annotations.
     * @throws AnnotationProcessor.NoAnnotationsFoundException throw exception on no annotations found.
     */
    @Test
    public void testFindAnnotation() throws AnnotationProcessor.TooManyAnnotationsFoundException,
            AnnotationProcessor.NoAnnotationsFoundException {
        List<Annotation> annotationList = new ArrayList<>();
        Annotation a1 = new Annotation();
        Annotation a2 = new Annotation();

        a1.setDisplayNameAttr("test1");
        a2.setDisplayNameAttr("test2");

        annotationList.add(a1);
        annotationList.add(a2);

        for (Annotation ann : annotationList) {
            System.out.println(ann.getDisplayNameAttr());
        }

        assertEquals(a2, AnnotationProcessor.findAnnotation(annotationList, "test2"));
    }

    /**
     * Tests the findAnnotation method with too many matching annotations.
     *
     * @throws AnnotationProcessor.TooManyAnnotationsFoundException throw exception on too many
     * matching annotations.
     */
    @Test(expected = AnnotationProcessor.TooManyAnnotationsFoundException.class)
    public void testFindAnnotationMultipleMatches()
            throws AnnotationProcessor.TooManyAnnotationsFoundException {
        List<Annotation> annotationList = new ArrayList<>();
        Annotation a1 = new Annotation();
        Annotation a2 = new Annotation();

        a1.setDisplayNameAttr("test");
        a2.setDisplayNameAttr("test");

        annotationList.add(a1);
        annotationList.add(a2);

        try {
            AnnotationProcessor.findAnnotation(annotationList, "test");
        } catch (AnnotationProcessor.NoAnnotationsFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * Tests the findAnnotation method with no matching annotations.
     *
     * @throws AnnotationProcessor.TooManyAnnotationsFoundException throw exception on too many
     * matching annotations.
     * @throws AnnotationProcessor.NoAnnotationsFoundException throw exception on no matching annotations.
     */
    @Test(expected = AnnotationProcessor.NoAnnotationsFoundException.class)
    public void testFindAnnotationNoMatch() throws AnnotationProcessor.TooManyAnnotationsFoundException,
            AnnotationProcessor.NoAnnotationsFoundException {
        List<Annotation> annotationList = new ArrayList<>();
        Annotation a1 = new Annotation();
        Annotation a2 = new Annotation();

        a1.setDisplayNameAttr("test1");
        a2.setDisplayNameAttr("test2");

        annotationList.add(a1);
        annotationList.add(a2);

        AnnotationProcessor.findAnnotation(annotationList, "abc");
    }

    /**
     * Tests the getAnnotations method.
     */
    @Test
    public void testGetAnnotations() {
        assertEquals(annotations, ap.getAnnotations());
    }

}