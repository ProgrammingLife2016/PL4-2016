package core;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

import static core.AnnotationParser.readCDSFilteredGFF;

/**
 * Annotation data POC.
 */
public class Poc {
    public static void main(String[] args) throws IOException {
        HashMap<Integer, Node> nodeMap = new Parser().readGFA(new FileInputStream("src\\main\\resources\\TB10.gfa"));
        nodeMap = filterAnnotationsInNodeMap(nodeMap);

        InputStream is = new FileInputStream("src\\main\\resources\\decorationV5_20130412.gff");
        List<Annotation> annotations = readCDSFilteredGFF(is);

        matchNodesAndAnnotations(nodeMap, annotations);
    }

    /**
     * Matches reference nodes and annotations to each other.
     *
     * @param nodeMap A given node map.
     * @param annotations A list of annotations.
     */
    private static void matchNodesAndAnnotations(HashMap<Integer, Node> nodeMap, List<Annotation> annotations) {
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
    private static int determineNodeMapSize(HashMap<Integer, Node> nodeMap) {
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
    private static HashMap<Integer, Node> filterAnnotationsInNodeMap(HashMap<Integer, Node> nodeMap) {
        int numNodes = nodeMap.size();

        for (int i = 1; i <= numNodes; i++) {
            if (!nodeMap.get(i).getGenomes().contains("MT_H37RV_BRD_V5.ref")) {
                nodeMap.remove(i);
            }
        }

        return nodeMap;
    }
}
