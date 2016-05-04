package core;

import java.util.*;

/**
 * Created by Niels Warnars on 2-5-2016.
 */
public class GraphReducer {
    private final static void determineParents(HashMap<Integer, Node> nodeMap) {
        for (int idx = 1; idx <= nodeMap.size(); idx++) {
            Node parent = nodeMap.get(idx);
            if (parent == null) { continue; }
            for (int childId : parent.getLinks()) {
                Node child = nodeMap.get(childId);

                if (child != null)
                    if (!child.getParents().contains(parent.getId())) {
                        child.addParent(parent.getId());
                    }
            }
        }
    }

    public final static void collapse(HashMap<Integer, Node> nodeMap, int collapseLevel) {
        determineParents(nodeMap);

        for (int idx = 1; idx <= nodeMap.size(); idx++) {
            Node parent = nodeMap.get(idx);
            if (parent == null) { continue; }

            Boolean collapsedNodes = collapseNodeBubbles(nodeMap, parent);
            if (!collapsedNodes && collapseLevel >= 1) {
                collapseNodeSequence(nodeMap, parent);
            }
        }
    }



    private final static void collapseNodeSequence(HashMap<Integer, Node> nodeMap, Node parent) {
        if (parent == null) { return; }
        if (parent.getLiveLinks(nodeMap).size() != 1) { return; }
        Node child = parent.getLiveLinks(nodeMap).get(0);

        if (child == null) { return; }
        if (child.getLiveLinks(nodeMap).size() != 1) { return; }
        if (child.getLiveParents(nodeMap).size() != 1) { return; }
        Node grandChild = child.getLiveLinks(nodeMap).get(0);

        if (grandChild == null) { return; }
        if (grandChild.getLiveLinks(nodeMap).size() != 1) { return; }
        List<Integer> newParentLinks = new ArrayList<>();
        newParentLinks.add(grandChild.getId());

        nodeMap.remove(child.getId());
        parent.setLinks(newParentLinks);
    }


    private final static Boolean collapseNodeBubbles(HashMap<Integer, Node> nodeMap, Node parent) {
        Boolean res = false;
        List<Node> children = parent.getLiveLinks(nodeMap);
        if (children.size() > 4) { return res; }
        for (Node child : children) {
            if (recurse(nodeMap, parent, child)) {
                nodeMap.remove(child.getId());
                if (!res) { res = true; }
            }
        }

        return res;
    }


    private final static Boolean recurse(HashMap<Integer, Node> nodeMap, Node startNode, Node currentNode) {
        List<Node> startNodeChildren = startNode.getLiveLinks(nodeMap);
        List<Node> currentNodeChildren = currentNode.getLiveLinks(nodeMap);

        if (startNodeChildren.size() < 2) { return false; }
        if (currentNodeChildren.size() != 1) { return false; }
        if (startNodeChildren.contains(currentNode)) { return true; }

        Node currentNodeChild = nodeMap.get(currentNodeChildren.get(0).getId());
        if (currentNodeChild == null) { return false; }

        return currentNodeChild.getGenomes().containsAll(startNode.getGenomes());
    }
}