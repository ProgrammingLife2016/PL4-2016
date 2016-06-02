package core;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import static core.AnnotationParser.readCDSFilteredGFF;

/**
 * Class responsible for processing the annotation data.
 *
 * @author Niels Warnars
 */
public class AnnotationProcessor {

    private List<Annotation> annotations;
    private HashMap<Integer, Node> filteredNodeMap;

    /**
     * Initializes a new annotation parser.
     *
     * @param nodeMap A given hash map of nodes.
     * @param gffFile The name/path of the GFF file.
     * @throws IOException Throw exception on read failure.
     */
    public AnnotationProcessor(HashMap<Integer, Node> nodeMap, String gffFile) throws IOException {
        annotations = readCDSFilteredGFF(getClass().getResourceAsStream(gffFile));
        this.filteredNodeMap = filterAnnotationsInNodeMap(nodeMap);
    }

    /**
     * Matches reference nodes and annotations to each other.
     */
    public void matchNodesAndAnnotations() {
        int startLoopIndex = 0;
        int nodeMapSize = determineNodeMapSize(filteredNodeMap);
        for (Annotation a : annotations) {
            startLoopIndex = a.detNodesSpannedByAnnotation(
                    startLoopIndex, filteredNodeMap, nodeMapSize);

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
        String reference = "";
        HashMap<Integer, Node> nodeMap = new HashMap<>();

        if (annotations.size() > 0) {
            reference = annotations.get(0).getSeqid() + ".ref";
        }

        for (Node n : baseNodeMap.values()) {
            if (n.getGenomes().contains(reference)) {
                nodeMap.put(n.getId(), n);
            }
        }

        return nodeMap;
    }

    /**
     * Finds an annotation by a specified annotation id.
     *
     * @param id A specified annotation id.
     * @return The found annotation.
     */
    public Annotation findAnnotationByID(long id) {
        for (Annotation a : annotations) {
            if (a.getIdAttr() == id) {
                return a;
            }
        }

        return null;
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
