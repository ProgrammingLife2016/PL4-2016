package core;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;

import static core.AnnotationParser.readCDSFilteredGFF;

/**
 * Class responsible for processing the annotation data.
 *
 * @author Niels Warnars
 */
public class AnnotationProcessor {

    private String reference;
    private List<Annotation> annotations;
    private HashMap<Integer, Node> filteredNodeMap;

    public AnnotationProcessor(HashMap<Integer, Node> nodeMap, String annotationsFile, String reference) throws IOException {
        this.reference = reference;
        this.filteredNodeMap = filterAnnotationsInNodeMap(nodeMap);

        InputStream gffIS = getClass().getResourceAsStream(annotationsFile);
        annotations = readCDSFilteredGFF(gffIS);
    }

    /**
     * Matches reference nodes and annotations to each other.
     */
    public void matchNodesAndAnnotations() {
        int startLoopIndex = 0;
        int nodeMapSize = determineNodeMapSize(filteredNodeMap);
        for (Annotation a : annotations) {
            startLoopIndex = a.detNodesSpannedByAnnotation(startLoopIndex, filteredNodeMap, nodeMapSize);

            for (Node n : a.getSpannedNodes()) {
                n.addAnnotation(a);
            }
        }
    }



    /**
     * Determines the index of the last key in a given node map.
     *
     * @param nodeMap A given node map.
     * @return The index of the last key in the node map.
     */
    private int determineNodeMapSize(HashMap<Integer, Node> nodeMap) {
        int lastKey = -1;
        for (int key : nodeMap.keySet()) {
            if (key > lastKey) {
                lastKey = key;
            }
        }

        return lastKey;
    }



    /**
     * Filters out all nodes in a node map that do not belong to the reference.
     *
     * @return A hash map of nodes present in the reference.
     */
    private HashMap<Integer, Node> filterAnnotationsInNodeMap(HashMap<Integer, Node> baseNodeMap) {
        HashMap<Integer, Node> nodeMap = new HashMap<>();

        for (Node n : baseNodeMap.values()) {
            if (n.getGenomes().contains(reference)) {
                nodeMap.put(n.getId(), n);
            }
        }

        return nodeMap;
    }

    /**
     * Gets the list of annotations.
     *
     * @return The list of annotations.
     */
    public List<Annotation> getAnnotations() {
        return annotations;
    }
}
