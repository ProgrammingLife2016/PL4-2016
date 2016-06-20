package core.annotation;

import core.graph.Node;

import java.util.HashMap;
import java.util.List;

/**
 * Class responsible for processing the annotation data.
 *
 */
public class AnnotationProcessor {

    private List<Annotation> annotations;
    private HashMap<Integer, Node> filteredNodeMap;

    /**
     * Initializes a new annotation parser.
     *
     * @param nodeMap A given hash map of nodes.
     * @param annotations List of annotations.
     */
    public AnnotationProcessor(HashMap<Integer, Node> nodeMap, List<Annotation> annotations) {
        this.annotations = annotations;
        this.filteredNodeMap = filterAnnotationsInNodeMap(nodeMap);
    }

    /**
     * Matches reference nodes and annotations to each other.
     */
    public void matchNodesAndAnnotations() {
        int startLoopIndex = 0;
        for (Annotation a : annotations) {
            startLoopIndex = a.detNodesSpannedByAnnotation(
                    startLoopIndex, filteredNodeMap);

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
    public int determineNodeMapSize(HashMap<Integer, Node> nodeMap) {
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
     * @param baseNodeMap a base node map.
     * @return A hash map of nodes present in the reference.
     */
    public HashMap<Integer, Node> filterAnnotationsInNodeMap(HashMap<Integer, Node> baseNodeMap) {
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
     * @param annotations A list of annotations.
     * @param str A specified part of the display name to search on.
     * @return The found annotation.
     * @throws TooManyAnnotationsFoundException Throws exception on too many matching annotations.
     */
    public static Annotation findAnnotation(List<Annotation> annotations, String str)
            throws TooManyAnnotationsFoundException {
        int counter = 0;
        Annotation matchingAnnotation = null;

        for (Annotation a : annotations) {
            String[] displayNameArr = a.getDisplayNameAttr().split(" ");

            // Example input: DNA polymerase III DnaN
            if (str.contains(" ") && a.getDisplayNameAttr().toLowerCase().contains(str.toLowerCase())) {
                System.out.println("Contains space");
                counter++;
                matchingAnnotation = a;
            } else {
                // Example input: 0002
                if (displayNameArr[0].contains(str)) {
                    counter++;
                    matchingAnnotation = a;
                }

                // Example input: DnaN or dnaN
                if (displayNameArr[displayNameArr.length - 1].toLowerCase().equals(str.toLowerCase())) {
                    counter++;
                    matchingAnnotation = a;
                }
            }
        }

        if (counter == 1) {
            return matchingAnnotation;
        } else if (counter > 1) {
            throw new TooManyAnnotationsFoundException();
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

    /**
     * Exception for too many elements in a data structure.
     */
    public static class TooManyAnnotationsFoundException extends Exception {

        /**
         * Exception class constructor.
         */
        public TooManyAnnotationsFoundException() {
        }
    }
}
