package core;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

/**
 * Class responsible for the collapsing of nodes in the graph.
 * By collapsing nodes the size of the graph can be reduced.
 * Created by Niels Warnars on 2-5-2016.
 */
public final class GraphReducer {
    private GraphReducer() { }

    private static List<HashMap<Integer, Node>> levelMaps = new ArrayList<HashMap<Integer, Node>>();

    /**
     * Give all nodes a list of its parents.
     * @param nodeMap   A HashMap containing all nodes in the graph.
     */
    public static void determineParents(HashMap<Integer, Node> nodeMap) {
        for (int idx = 1; idx <= nodeMap.size(); idx++) {
            Node parent = nodeMap.get(idx);
            if (parent == null) { continue; }
            for (int childId : parent.getLinks()) {
                Node child = nodeMap.get(childId);
                if (child == null) { continue; }

                if (!child.getParents().contains(parent.getId())) {
                        child.addParent(parent.getId());
                }
            }
        }
    }

    /**
     * Perform collapsing on multiple level of nodes.
     * @param startMap  An uncollapsed node map.
     * @return  A list of node maps with a decreasing amount of nodes.
     */
    public static List<HashMap<Integer, Node>>
    createLevelMaps(HashMap<Integer, Node> startMap) {
        levelMaps.add(startMap);

        for (int i = 1;; i++) {
            HashMap<Integer, Node> levelMap = collapse(levelMaps.get(i - 1));
            levelMaps.add(levelMap);
            int previousMapSize = levelMaps.get(i - 1).size();
            int currentMapSize = levelMaps.get(i).size();

            // Don't make any new zoom level if the number of nodes after reduction is only 10 less
            // than the number of nodes after previous reduction.
            if ((previousMapSize - currentMapSize) < 10) {
                return levelMaps;
            }
        }
    }

    /**
     * Reduce the number of nodes in a graph by collapsing vertically and horizontally.
     * @param map   A HashMap containing all nodes in the graph.
     * @return	A collapsed map.
     */
    public static HashMap<Integer, Node> collapse(HashMap<Integer, Node> map) {
        HashMap<Integer, Node> nodeMap = (HashMap<Integer, Node>) map.clone();

        determineParents(nodeMap);

        for (int idx = 1; idx <= map.size(); idx++) {
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
     * @return  Whether the horizontal collapse action has succeeded.
     */
    public static Boolean collapseNodeSequence(HashMap<Integer, Node> nodeMap, Node parent) {
        if (parent == null) { return false; }
        if (parent.getLiveLinks(nodeMap).size() != 1) { return false; }
        Node child = parent.getLiveLinks(nodeMap).get(0);

        if (child.getLiveLinks(nodeMap).size() != 1) { return false; }
        if (child.getLiveParents(nodeMap).size() != 1) { return false; }
        Node grandChild = child.getLiveLinks(nodeMap).get(0);

        if (grandChild.getLiveParents(nodeMap).size() != 1) { return false; }
        if (grandChild.getLiveLinks(nodeMap).size() != 1) { return false; }

        nodeMap.remove(child.getId());
        parent.setLinks(new ArrayList<>(Arrays.asList(grandChild.getId())));
        grandChild.setParents(new ArrayList<>(Arrays.asList(parent.getId())));

        return true;
    }


    /**
     * Collapse a parent and its grandchild horizontally.
     * @param nodeMap   A HashMap containing all nodes in the graph.
     * @param parent    A given parent node to be collapsed with a number of its children.
     * @return  Whether nodes have been collapsed.
     */
    public static Boolean
    collapseNodeBubbles(HashMap<Integer, Node> nodeMap, Node parent) {
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
    public static Boolean
    collapseCheck(HashMap<Integer, Node> nodeMap, Node startNode, Node currentNode) {
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