package core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

/**
 * Created by Niels Warnars on 2-5-2016.
 */
public class GraphReducer {

    private final static void determineParents(HashMap<Integer, Node> nodeMap) {
        for (int idx = 1; idx <= nodeMap.size(); idx++) {
            Node parent = nodeMap.get(idx);
            if (parent == null) { continue; }
            for (int childId : parent.getLinks()) {
                nodeMap.get(childId).addParent(parent.getId());
            }
        }
    }

    public final static void collapse(HashMap<Integer, Node> nodeMap) {
        determineParents(nodeMap);

        for (int idx = 1; idx <= nodeMap.size(); idx++) {
            Node parent = nodeMap.get(idx);
            if (parent == null) { continue; }

            List<Node> children = parent.getLiveLinks(nodeMap);
            // Only parents with two children should be evaluated.
            if (children.size() == 2) {
                for (Node child : children) {
                    if (recurse2(nodeMap, parent, child)) {
                        nodeMap.remove(child.getId());
                    }
                }
            }

//            if (children.size() == 3) {
//                for (Node child : children) {
//                    if (recurse(nodeMap, parent, child)) {
//                        nodeMap.remove(child.getId());
//                    }
//                }
//            }
        }
    }

    private final static Boolean recurse2(HashMap<Integer, Node> nodeMap, Node startNode, Node currentNode) {
        List<Node> startNodeChildren = startNode.getLiveLinks(nodeMap);
        List<Node> currentNodeChildren = currentNode.getLiveLinks(nodeMap);

        if (startNodeChildren.size() != 2) { return false; }
        if (currentNodeChildren.size() != 1) { return false; }
        if (startNodeChildren.contains(currentNode)) { return true; }

        Node currentNodeChild = nodeMap.get(currentNodeChildren.get(0).getId());
       // if (currentNodeChild == null) { return false; }

        return currentNodeChild.getGenomes().containsAll(startNode.getGenomes());
    }
}