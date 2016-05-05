package core;

import java.util.*;

/**
 * Created by Niels Warnars on 2-5-2016.
 */
public class GraphReducer {

    /**
     * Give all nodes a list of its parents.
     * @param nodeMap   A HashMap containing all nodes in the graph.
     */
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

    /**
     * Reduce the number of nodes in a graph by collapsing vertically and horizontally.
     * @param map   A HashMap containing all nodes in the graph.
     */
    public final static HashMap<Integer, Node> collapse(HashMap<Integer, Node> map) {
        HashMap<Integer, Node> nodeMap = (HashMap<Integer, Node>) map.clone();

        determineParents(nodeMap);

        for (int idx = 1; idx <= nodeMap.size(); idx++) {
            Node parent = nodeMap.get(idx);
            if (parent == null) { continue; }

            collapseNodeBubbles(nodeMap, parent);
            collapseNodeSequence(nodeMap, parent);
        }

        return nodeMap;
    }

    
    /**
     * Collapse a parent and its grandchild horizontally.
     * @param nodeMap   A HashMap containing all nodes in the graph.
     * @param parent    A given parent node to be collapsed with its grandchild.
     */
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
        if (grandChild.getLiveParents(nodeMap).size() != 1) { return; }
        List<Integer> newParentLinks = new ArrayList<>();
        newParentLinks.add(grandChild.getId());

        nodeMap.remove(child.getId());
        parent.setLinks(newParentLinks);
    }


    /**
     * Collapse a parent and its grandchild horizontally.
     * @param nodeMap   A HashMap containing all nodes in the graph.
     * @param parent    A given parent node to be collapsed with a number of its children.
     * @return  Whether nodes have been collapsed.
     */
    private final static Boolean collapseNodeBubbles(HashMap<Integer, Node> nodeMap, Node parent) {
        Boolean res = false;
        List<Node> children = parent.getLiveLinks(nodeMap);
        if (children.size() > 4) { return res; }
        for (Node child : children) {
            if (collapseCheck(nodeMap, parent, child)) {
                nodeMap.remove(child.getId());
                if (!res) { res = true; }
            }
        }

        return res;
    }


    /**
     * Check whether a node can be collapsed / removed.
     * @param nodeMap   A HashMap containing all nodes in the graph.
     * @param startNode    A parent node linked to a to be collapsed node.
     * @param currentNode   A node to be removed / collapsed.
     * @return  Whether currentNode can be removed / collapsed.
     */
    private final static Boolean collapseCheck(HashMap<Integer, Node> nodeMap, Node startNode, Node currentNode) {
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