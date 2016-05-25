package core;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

import java.util.*;

/**
 * Class responsible for the collapsing of nodes in the graph.
 * By collapsing nodes the size of the graph can be reduced.
 * Created by Niels Warnars on 2-5-2016.
 */
public final class GraphReducer {
    private GraphReducer() {
    }

    private static List<HashMap<Integer, Node>> levelMaps = new ArrayList<>();

    /**
     * Give all nodes a list of its parents.
     *
     * @param nodeMap A HashMap containing all nodes in the graph.
     */
    public static void determineParents(HashMap<Integer, Node> nodeMap) {
        for (int idx = 1; idx <= nodeMap.size(); idx++) {
            Node parent = nodeMap.get(idx);
            if (parent == null) {
                continue;
            }
            for (int childId : parent.getLinks()) {
                Node child = nodeMap.get(childId);
                if (child == null) {
                    continue;
                }

                if (!child.getParents().contains(parent.getId())) {
                    child.addParent(parent.getId());
                }
            }
        }
    }

    /**
     * Perform collapsing on multiple level of nodes.
     *
     * @param startMap An uncollapsed node map.
     * @return A list of node maps with a decreasing amount of nodes.
     */
    public static List<HashMap<Integer, Node>>
    createLevelMaps(HashMap<Integer, Node> startMap) {
        levelMaps.add(startMap);

        for (int i = 1;; i++) {
            HashMap<Integer, Node> levelMap = collapse(levelMaps.get(i - 1));
            levelMaps.add(levelMap);
            int previousMapSize = levelMaps.get(i - 1).size();
            int currentMapSize = levelMaps.get(i).size();

            System.out.println(
                    "[Previous Map] Index: " + (i - 1) + ", Map size: " + previousMapSize
                    + " - [Current Map]: Index " + i + ", Map size: " + currentMapSize);

            // Don't make any new zoom level if the number of nodes after reduction is only 2 less
            // than the number of nodes after previous reduction.
            if ((previousMapSize - currentMapSize) <= 2) {
                return levelMaps;
            }
        }

        //return levelMaps;
    }

    /**
     * Copy all values of a given node map.
     *
     * @param map A node map to be copied.
     * @return A copied node map.
     */
    @SuppressFBWarnings("WMI_WRONG_MAP_ITERATOR")
    private static HashMap<Integer, Node> copyNodeMap(HashMap<Integer, Node> map) {
        HashMap<Integer, Node> res = new HashMap<>();
        for (int i : map.keySet()) {
            Node n = map.get(i);
            Node newNode = new Node(n.getId(), n.getType(), n.getSequence(), n.getzIndex());
            newNode.setLinks(n.getLinks());
            newNode.setParents(n.getParents());
            newNode.setGenomes(n.getGenomes());
            newNode.setCollapseLevel(n.getCollapseLevel());

            res.put(i, newNode);
        }

        return res;
    }

    /**
     * Reduce the number of nodes in a graph by collapsing vertically and horizontally.
     *
     * @param map A HashMap containing all nodes in the graph.
     * @return A collapsed map.
     */
    public static HashMap<Integer, Node> collapse(HashMap<Integer, Node> map) {
        HashMap<Integer, Node> nodeMap = copyNodeMap(map);
        determineParents(nodeMap);

        for (int idx = 1; idx <= map.size(); idx++) {
            Node parent = nodeMap.get(idx);
            if (parent == null) {
                continue;
            }

            collapseBubble(nodeMap, parent);
            collapseIndel(nodeMap, parent);
            //collapseNodeSequence(nodeMap, parent);
        }

        return nodeMap;
    }

    /**
     * Collapse a parent and its grandchild horizontally.
     *
     * @param nodeMap A HashMap containing all nodes in the graph.
     * @param parent  A given parent node to be collapsed with its grandchild.
     * @return Whether the horizontal collapse action has succeeded.
     */
    public static Boolean collapseNodeSequence(HashMap<Integer, Node> nodeMap, Node parent) {
        Boolean res = false;
        // Links must be present from parent --> child
        if (parent == null) {
            return false;
        }

        List<Integer> childrenIds = parent.getLinks(nodeMap);
        if (childrenIds.size() != 1) {
            return false;
        }

        for (int idx = 0; idx < childrenIds.size(); idx++) {
            Node child = nodeMap.get(childrenIds.get(idx));

            // A child may only have one parent and grandchild
            if (child.getLinks(nodeMap).size() != 1) {
                return false;
            }
            if (child.getParents(nodeMap).size() != 1) {
                return false;
            }
            Node grandChild = nodeMap.get(child.getLinks(nodeMap).get(0));

            parent.setLinks(new ArrayList<>(Arrays.asList(grandChild.getId())));
            grandChild.setParents(new ArrayList<>(Arrays.asList(parent.getId())));
            nodeMap.remove(child.getId());
            res = true;
        }

        return res;
    }

    /**
     * Collapse a child1, if child2 is a grandchild of child1.
     *
     * @param nodeMap A HashMap containing all nodes in the graph.
     * @param parent  A given parent node to be collapsed with a number of its children.
     * @return Whether nodes have been collapsed.
     */
    public static Boolean
    collapseIndel(HashMap<Integer, Node> nodeMap, Node parent) {
        List<Integer> children = parent.getLinks(nodeMap);
        if (children.size() != 2) {
            return false;
        }

        Node child1 = nodeMap.get(children.get(0));
        Node child2 = nodeMap.get(children.get(1));

        List<Integer> child1ChildrenIds = child1.getLinks(nodeMap);
        List<Integer> child2ChildrenIds = child2.getLinks(nodeMap);

        int newCollapseLevel = indelCollapseLevelGain(parent, child1, child2);

        // Child 1 is inserted
        if (child1ChildrenIds.size() == 1 && child1ChildrenIds.contains(child2.getId())) {
            if (nodeMap.get(child1.getId()).getParents(nodeMap).size() != 1) {
                return false;
            } else if (nodeMap.get(child2.getId()).getParents(nodeMap).size() != 2) {
                return false;
            }

            parent.setLinks(new ArrayList<>(Arrays.asList(child1.getId())));
            child1.setCollapseLevel(newCollapseLevel);
            child1.setType(NodeType.INDEL);
            child1.setSequence("");
            return true;

        // Child 2 is inserted
        } else if (child2ChildrenIds.size() == 1 && child2ChildrenIds.contains(child1.getId())) {
            if (nodeMap.get(child2.getId()).getParents(nodeMap).size() != 1) {
                return false;
            } else if (nodeMap.get(child1.getId()).getParents(nodeMap).size() != 2) {
                return false;
            }

            parent.setLinks(new ArrayList<>(Arrays.asList(child2.getId())));
            child2.setCollapseLevel(newCollapseLevel);
            child2.setType(NodeType.INDEL);
            child2.setSequence("");
            return true;
        }

        return false;
    }

    /**
     * Collapse a parent and its grandchild horizontally.
     *
     * @param nodeMap A HashMap containing all nodes in the graph.
     * @param parent  A given parent node to be collapsed with a number of its children.
     * @return Whether nodes have been collapsed.
     */
    public static Boolean
    collapseBubble(HashMap<Integer, Node> nodeMap, Node parent) {
        if (!checkBubbleCollapsePreConditions(nodeMap, parent)) {
            return false;
        }

        List<Integer> childrenIds = parent.getLinks(nodeMap);
        Node grandChild = determineGrandChild(nodeMap, parent);
        int newCollapseLevel = bubbleCollapseLevelGain(nodeMap, parent);

        // Remove all nodes in the bubble except for the parent node and the first childnode
        for (int i = 0; i < childrenIds.size(); i++) {
            int childId = childrenIds.get(i);
            Node child = nodeMap.get(childId);

            //Use the first child to represent the bubble
            if (i == 0) {
                parent.setLinks(new ArrayList<>(Arrays.asList(childId)));
                child.setLinks(new ArrayList<>(Arrays.asList(grandChild.getId())));
                child.setType(NodeType.BUBBLE);
                child.setCollapseLevel(child.getCollapseLevel() + 1);
                child.setSequence("");
            }
            else if (child != null) {
                nodeMap.remove(childId);
            }
        }

        return true;
    }

    /**
     * Determine the grand child of a parent node.
     *
     * @param nodeMap A HashMap containing all nodes in the graph.
     * @param parent  A given parent node to be collapsed with a number of its children.
     * @return The grand child of a parent node.
     */
    private static Node determineGrandChild(HashMap<Integer, Node> nodeMap, Node parent) {
        List<Integer> children = parent.getLinks(nodeMap);

        Node child0 = nodeMap.get(children.get(0));
        int grandChildId = child0.getLinks(nodeMap).get(0);
        return nodeMap.get(grandChildId);
    }

    /**
     * Check whether a bubble has the right shape to be collapsed.
     *
     * @param nodeMap A HashMap containing all nodes in the graph.
     * @param parent  A given parent node to be collapsed with a number of its children.
     * @return Whether a bubble has the right shape to be collapsed.
     */
    private static Boolean
    checkBubbleCollapsePreConditions(HashMap<Integer, Node> nodeMap, Node parent) {
        List<Integer> children = parent.getLinks(nodeMap);

        if (children.size() <= 1) {
            return false;
        }

        // Check whether all children only have one child
        for (int i = 0; i < children.size(); i++) {
            if (nodeMap.get(children.get(i)).getLinks(nodeMap).size() != 1) {
                return false;
            }
        }

        // Check whether the grandchild is only connected to the parent
        for (int i = 0; i < children.size() - 1; i++) {
            Integer grandChild1 = nodeMap.get(children.get(i)).getLinks(nodeMap).get(0);
            Integer grandChild2 = nodeMap.get(children.get(i + 1)).getLinks(nodeMap).get(0);
            if (!grandChild1.equals(grandChild2)) {
                return false;
            }
        }

        Node grandChild = determineGrandChild(nodeMap, parent);

        // Check whether the parent and grand child only have links between each other.
        if (grandChild.getParents(nodeMap).size() != children.size()) {
            return false;
        }

        // Check whether the children only have one parent.
        for (int i = 0; i < children.size(); i++) {
            if (nodeMap.get(children.get(i)).getParents(nodeMap).size() != 1) {
                return false;
            }
        }

        return true;
    }

    /**
     * Determine the Indel collapse level increase.
     *
     * @param parent The parent node.
     * @param child1 Child 1 of the parent.
     * @param child2 Child 2 of the parent.
     * @return The Indel collapse level increase.
     */
    private static int indelCollapseLevelGain(Node parent, Node child1, Node child2) {
        List<Node> nodesInIndel = new ArrayList<>();
        nodesInIndel.add(parent);
        nodesInIndel.add(child1);
        nodesInIndel.add(child2);

        return newCollapseLevel(nodesInIndel);
    }

    /**
     * Determine the bubble collapse level increase.
     *
     * @param nodeMap A HashMap containing all nodes in the graph.
     * @param parent  A given parent node to be collapsed with a number of its children.
     * @return The bubble collapse level increase.
     */
    private static int bubbleCollapseLevelGain(HashMap<Integer, Node> nodeMap, Node parent) {
        List<Node> nodesInBubble = new ArrayList<>();
        nodesInBubble.add(parent);
        nodesInBubble.add(determineGrandChild(nodeMap, parent));

        for (int childId : parent.getLinks(nodeMap)) {
            nodesInBubble.add(nodeMap.get(childId));
        }

        return newCollapseLevel(nodesInBubble);
    }

    /**
     * Determine the new collapse level of a list of nodes.
     *
     * @param nodes List of nodes.
     * @return The new collapse level.
     */
    private static int newCollapseLevel(List<Node> nodes) {
        int res = 0;
        for (Node n : nodes) {
            if (res < n.getCollapseLevel()) {
                res = n.getCollapseLevel();
            }
        }

        return res + 1;
    }
}