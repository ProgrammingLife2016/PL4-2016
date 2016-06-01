package core;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Annotation data POC.
 */
public class Poc {
    public static void main(String[] args) throws IOException {
        HashMap<Integer, Node> nodeMap = getNodeMap("src\\main\\resources\\TB10.gfa");
        List<Annotation> annotations = getAnnotations("src\\main\\resources\\decorationV5_20130412.gff");

        int startLoopIndex = 0;
        for (Annotation a : annotations) {
            startLoopIndex = a.detNodesSpannedByAnnotation(startLoopIndex, nodeMap);

            for (Node n : a.getSpannedNodes()) {
                System.out.println("Annotation ID: " + a.getStart()
                        + ", Node: " + n.getId());
            }
            //for (Node n : a.getSpannedNodes()) {
            //    n.setAnnotation(a);
            //}
        }
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




    /**
     * Gets a list of CDS filtered and sorted annotations from disk.
     *
     * @param path The path to the gff file.
     * @return A filtered and sorted list of annotations.
     * @throws IOException Throw an exception on read failure.
     */
    private static List<Annotation> getAnnotations(String path) throws IOException {
        InputStream is = new FileInputStream(path);

        List<Annotation> annotations = AnnotationParser.readGFF(is).stream()
                .filter(a -> a.getType().equals("CDS")).collect(Collectors.toList());

        Collections.sort(annotations);
        return annotations;
    }
}
