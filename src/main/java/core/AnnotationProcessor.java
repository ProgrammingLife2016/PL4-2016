package core;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;

import static core.AnnotationParser.readCDSFilteredGFF;

/**
 * Class responsible
 * @author Niels Warnars
 */
public class AnnotationProcessor {

    private List<Annotation> annotations;
    private HashMap<Integer, Node> nodeMap;

    public AnnotationProcessor(HashMap<Integer, Node> nodeMap, String annotationsFile) throws IOException {
        this.nodeMap = filterAnnotationsInNodeMap(nodeMap);

        InputStream gffIS = getClass().getResourceAsStream(annotationsFile);
        annotations = readCDSFilteredGFF(gffIS);
    }

    /**
     * Matches reference nodes and annotations to each other.
     */
    public void matchNodesAndAnnotations() {
        System.out.println("Num CDS nodes: " + nodeMap.size());

        int startLoopIndex = 0;
        int nodeMapSize = determineNodeMapSize(nodeMap);
        for (Annotation a : annotations) {
            startLoopIndex = a.detNodesSpannedByAnnotation(startLoopIndex, nodeMap, nodeMapSize);

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
     * @param nodeMap A given map containing all nodes read from disk.
     * @return A hash map of nodes present in the reference.
     */
    private HashMap<Integer, Node> filterAnnotationsInNodeMap(HashMap<Integer, Node> nodeMap) {
        int numNodes = nodeMap.size();

        for (int i = 1; i <= numNodes; i++) {
            if (!nodeMap.get(i).getGenomes().contains("MT_H37RV_BRD_V5.ref")) {
                nodeMap.remove(i);
            }
        }

        return nodeMap;
    }
}
