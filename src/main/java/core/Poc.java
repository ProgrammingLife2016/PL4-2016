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
        InputStream is = new FileInputStream("src\\main\\resources\\TB10.gfa");
        HashMap<Integer, Node> nodeMap = new Parser().readGFA(is);

        AnnotationProcessor ap = new AnnotationProcessor(nodeMap, "/decorationV5_20130412.gff");
        ap.matchNodesAndAnnotations();
    }
}
