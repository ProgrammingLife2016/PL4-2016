package core;

import java.io.*;
import java.util.*;

/**
 * Annotation data POC.
 */
public class Poc {
    public static void main(String[] args) throws IOException {
        String reference = "MT_H37RV_BRD_V5.ref";
        InputStream is = new FileInputStream("src\\main\\resources\\TB10.gfa");
        HashMap<Integer, Node> nodeMap = new Parser().readGFA(is);

        AnnotationProcessor ap = new AnnotationProcessor(nodeMap, "/decorationV5_20130412.gff", reference);
        ap.matchNodesAndAnnotations();

        System.out.println("POC - NodeMap size: " + nodeMap.size());

        int counter = 0;
        for (Node n : nodeMap.values()) {
            if (n.getGenomes().contains(reference)) {
                counter++;
            }
        }

        System.out.println("POC - Num reference nodes: " + counter);
    }
}
