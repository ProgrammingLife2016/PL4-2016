package core;

import java.util.Arrays;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

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

            // Don't make any new zoom level if the number of nodes after reduction is only 2 less
            // than the number of nodes after previous reduction.
            if ((previousMapSize - currentMapSize) <= 2) {
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
            if (parent == null) {
                continue;
            }
            collapseSymmetricalNodeBubble(nodeMap, parent);
            collapseAsymmetricalNodeBubble(nodeMap, parent);
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
        Boolean res = false;
        // Links must be present from parent --> child
        if (parent == null) { return false; }

        List<Integer> childrenIds = parent.getLinks(nodeMap);
        if (childrenIds.size() != 1) { return false; }

        for (int idx = 0; idx < childrenIds.size(); idx++) {
            Node child = nodeMap.get(childrenIds.get(idx));

            // A child may only have one parent and grandchild
            if (child.getLinks(nodeMap).size() != 1) { return false; }
            if (child.getParents(nodeMap).size() != 1) { return false; }
            Node grandChild = nodeMap.get(child.getLinks(nodeMap).get(0));

            addGenomes(parent, child);
            addGenomes(grandChild, child);

            parent.setLinks(new ArrayList<>(Arrays.asList(grandChild.getId())));
            grandChild.setParents(new ArrayList<>(Arrays.asList(parent.getId())));
            nodeMap.remove(child.getId());
            res = true;
        }

        return res;
    }

    /**
     * Collapse a child1, if child2 is a grandchild of child1.
     * @param nodeMap   A HashMap containing all nodes in the graph.
     * @param parent    A given parent node to be collapsed with a number of its children.
     * @return  Whether nodes have been collapsed.
     */
    public static Boolean
    collapseAsymmetricalNodeBubble(HashMap<Integer, Node> nodeMap, Node parent) {
        List<Integer> children = parent.getLinks(nodeMap);
        if (children.size() == 2) {
            int child1Id = children.get(0);
            int child2Id = children.get(1);

            List<Integer> child1ChildrenIds = nodeMap.get(child1Id).getLinks(nodeMap);
            List<Integer> child2ChildrenIds = nodeMap.get(child2Id).getLinks(nodeMap);

            Node child1 = nodeMap.get(child1Id);
            Node child2 = nodeMap.get(child2Id);

            if (child1ChildrenIds.contains(child2Id)) {
                addGenomes(child2, child1);
                nodeMap.remove(child1Id);
                return true;

            } else if (child2ChildrenIds.contains(child1Id)) {
                addGenomes(child1, child2);
                nodeMap.remove(child2Id);
                return true;
            }
        }

        return false;
    }


    /**
     * Collapse a parent and its grandchild horizontally.
     * @param nodeMap   A HashMap containing all nodes in the graph.
     * @param parent    A given parent node to be collapsed with a number of its children.
     * @return  Whether nodes have been collapsed.
     */
    public static Boolean
    collapseSymmetricalNodeBubble(HashMap<Integer, Node> nodeMap, Node parent) {
        List<Integer> children = parent.getLinks(nodeMap);
        if (children.size() <= 1) { return false; }

        // Check whether all children only have one child
        for (int i = 0; i < children.size(); i++) {
            if (nodeMap.get(children.get(i)).getLinks(nodeMap).size() != 1) { return false; }
        }

        // Check whether all grand children are the same
        for (int i = 0; i < children.size() - 1; i++) {
            Integer grandChild1 = nodeMap.get(children.get(i)).getLinks(nodeMap).get(0);
            Integer grandChild2 = nodeMap.get(children.get(i + 1)).getLinks(nodeMap).get(0);
            if (!grandChild1.equals(grandChild2)) { return false; }
        }

        Node child0 = nodeMap.get(children.get(0));
        // Remove redundant nodes in bubble
        for (int i = 1; i < children.size(); i++) {
            int childId = children.get(i);
            Node child = nodeMap.get(childId);

            if (child != null) {
                addGenomes(child0, child);
                nodeMap.remove(childId);
            }
        }

        return true;
    }

    /**
     * Adds a list of genomes to another list.
     * @param base  Base node.
     * @param toAdd Node of which its genome has to be added to the base node.
     */
    public static void addGenomes(Node base, Node toAdd) {
        Set<String> hs = new LinkedHashSet<String>(base.getGenomes());
        hs.addAll(toAdd.getGenomes());

        base.setGenomes(new ArrayList<String>(hs));
    }
}