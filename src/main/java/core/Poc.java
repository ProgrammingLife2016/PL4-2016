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
        HashMap<Integer, Node> nodeMap = getNodeMap("src\\main\\resources\\TB10.gfa");
        InputStream is = new FileInputStream("src\\main\\resources\\decorationV5_20130412.gff");
        List<Annotation> annotations = readCDSFilteredGFF(is);

        System.out.println("Num CDS nodes: " + nodeMap.size());

        long startTime = System.currentTimeMillis();

        int startLoopIndex = 0;
        for (Annotation a : annotations) {
            startLoopIndex = a.detNodesSpannedByAnnotation(startLoopIndex, nodeMap);

            if (startLoopIndex == -1) break;
            System.out.println(a.getSpannedNodes().size());

            //for (Node n : a.getSpannedNodes()) {
            //    n.setAnnotation(a);
            //}
        }

        long stopTime = System.currentTimeMillis();
        System.out.println("Elapsed time: " + (stopTime - startTime) + " ms");
    }





    /**
     * Gets a hash map of nodes from disk.
     *
     * @param path The path to the gfa file.
     * @return A hash map of nodes.
     * @throws IOException Throw an exception on read failure.
     */
    private static HashMap<Integer, Node> getNodeMap(String path) throws IOException {
        HashMap<Integer, Node> nodeMap = new Parser().readGFA(new FileInputStream(path));
        int numNodes = nodeMap.size();

        for (int i = 1; i <= numNodes; i++) {
            if (!nodeMap.get(i).getGenomes().contains("MT_H37RV_BRD_V5.ref")) {
                nodeMap.remove(i);
            }
        }

        return nodeMap;
    }
}
