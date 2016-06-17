package core.graph;

import core.typeEnums.CellType;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Stack;
import java.util.stream.Collectors;

/**
 * Class responsible for the collapsing of nodes in the graph.
 * By collapsing nodes the size of the graph can be reduced.
 */
public final class GraphReducer {

    /**
     * Class constructor
     */
    private GraphReducer() {

    }

    private static List<HashMap<Integer, Node>> levelMaps = new ArrayList<>();
    private static int startMapSize = 0;

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
     * @param minDelta The minimum reduction necessary to continue the reducing.
     * @param genomesInFilter the genomes in this filter.
     * @return A list of node maps with a decreasing amount of nodes.
     */
    public static List<HashMap<Integer, Node>> createLevelMaps(HashMap<Integer, Node> startMap,
                                                               int minDelta, List<String> genomesInFilter) {
        levelMaps = new ArrayList<>();
        startMapSize = startMap.size();
        System.out.println("startmap size:" + startMap.size());
        determineParents(startMap);
        HashMap<Integer, Node> filteredNodeMap = generateFilteredMap(startMap, genomesInFilter);
        determineParents(filteredNodeMap);
        levelMaps.add(filteredNodeMap);
        HashMap<Integer, Node> collapsedFilteredMap = collapseFirstMap(filteredNodeMap);
        determineParents(collapsedFilteredMap);
        levelMaps.add(collapsedFilteredMap);
        int reduceAmount = 1;

        for (int i = 2;; i++) {
            HashMap<Integer, Node> levelMap = collapse(levelMaps.get(i - 1), i - 1);
            int previousMapSize = levelMaps.get(i - 1).size();
            int currentMapSize = levelMap.size();

            if (levelMaps.size() == 10) {
                reduceZoomingLevels(reduceAmount);
                i -= reduceAmount;
            }

            // Don't make any new zoom level if the number of nodes after reduction is less
            // than the set minimum number of nodes after previous reduction.
            if ((previousMapSize - currentMapSize) <= minDelta) {
                levelMaps.set(i - 1, levelMap);

                traverseMaps(reduceAmount, i);

                return levelMaps;
            } else {
                levelMaps.add(levelMap);
            }
        }
    }

    /**
     * Generates a filtered map
     *
     * @param startMap the startMap.
     * @param genomesInFilter the genomes in the filter.
     * @return a filtered map.
     */
    public static HashMap<Integer, Node> generateFilteredMap(HashMap<Integer, Node> startMap,
                                                             List<String> genomesInFilter) {
        HashMap<Integer, Node> filteredNodeMap = new HashMap<>(copyNodeMap(startMap));
        for (int nodeId : startMap.keySet()) {
            Node node = filteredNodeMap.get(nodeId);
            if (node == null) {
                continue;
            }
            if (!intersects(node.getGenomes(), genomesInFilter)) {
                for (int parentId : node.getParents()) {
                    Node parent = filteredNodeMap.get(parentId);
                    parent.removeLink(nodeId);
                }
                for (int childId : node.getLinks()) {
                    Node child = filteredNodeMap.get(childId);
                    child.removeParent(nodeId);
                }
                node.setParents(new ArrayList<>());
                node.setLinks(new ArrayList<>());
                filteredNodeMap.remove(nodeId);
            }
        }
        return filteredNodeMap;
    }

    private static boolean intersects(List<String> l1, List<String> l2) {
        for (String s : l1) {
            if (l2.contains(s)) {
                return true;
            }
        }
        return false;
    }

    private static HashMap<Integer, Node> collapseFirstMap(HashMap<Integer, Node> nodeMap) {
        HashMap<Integer, Node> reducedMap = new HashMap<>(copyNodeMap(nodeMap));
        for (int idx = 1; idx < startMapSize; idx++) {
            Node parent = reducedMap.get(idx);
            if (parent == null) {
                continue;
            }
            boolean collapsed = true;
            while (collapsed) {
                 collapsed = collapseNodeSequence(reducedMap, parent, 0);
            }
        }

        System.out.println("reduced map size: " + reducedMap.size());
        return reducedMap;
    }

    /**
     * Traverse the maps.
     *
     * @param reduceAmount amount to reduce
     * @param i i
     */
    private static void traverseMaps(int reduceAmount, int i) {
        int maxDepth = 10;
        for (int j = i; maxDepth < 500; j++) {
            if (levelMaps.size() == 10) {
                reduceZoomingLevels(reduceAmount);
                j -= reduceAmount;
            }
            HashMap<Integer, Node> levelMap2 = secondStageCollapse(levelMaps.get(j - 1), j - 1, maxDepth);
            int previousMapSize2 = levelMaps.get(j - 1).size();
            int currentMapSize2 = levelMap2.size();
            if (previousMapSize2 - currentMapSize2 == 0) {
                levelMaps.set(j - 1, levelMap2);
                maxDepth += 10;
                j--;
            } else {
                levelMaps.add(levelMap2);
                System.out.println(levelMap2.size());
            }
        }
    }

    /**
     * Reduced the number of zoomlevels.
     *
     * @param amountToRemove amount to remove
     */
    public static void reduceZoomingLevels(int amountToRemove) {
        for (int k = 0; k < amountToRemove; k++) {
            int smallestDifference = Integer.MAX_VALUE;
            int index = -1;
            for (int level = 3; level < levelMaps.size() - 2; level++) {
                int currentMapSize = levelMaps.get(level).size();
                int previousMapSize = levelMaps.get(level - 1).size();
                int difference = currentMapSize - previousMapSize;
                if (difference < smallestDifference) {
                    index = level;
                    smallestDifference = difference;
                }
            }
            levelMaps.remove(index);
        }
    }

    /**
     * Reduce the number of nodes in a graph by collapsing vertically and horizontally.
     *
     * @param map       A HashMap containing all nodes in the graph.
     * @param zoomLevel The current zoomLevel
     * @param maxDepth  The maximumDepth allowed for a complexNode
     * @return A collapsed map.
     */
    public static HashMap<Integer, Node> secondStageCollapse(HashMap<Integer, Node> map, int zoomLevel, int maxDepth) {
        HashMap<Integer, Node> nodeMap = new HashMap<>(copyNodeMap(map));
        determineParents(nodeMap);

        for (int idx = 1; idx < startMapSize; idx++) {
            Node parent = nodeMap.get(idx);
            if (parent == null) {
                continue;
            }
            collapseComplexPath(nodeMap, parent, zoomLevel, maxDepth);
            //If the nodemap is still bigger than a desired top view,
            //We can try and keep collapsing as we used to do.
            if (nodeMap.size() > 100) {
                collapseNodeSequence(nodeMap, parent, zoomLevel);
                collapseBubble(nodeMap, parent, zoomLevel);
                collapseIndel(nodeMap, parent);
            }
        }


        return nodeMap;
    }

    /**
     * Copy all values of a given node map.
     * ff
     *
     * @param map A node map to be copied.
     * @return A copied node map.
     */
    @SuppressFBWarnings("WMI_WRONG_MAP_ITERATOR")
    public static HashMap<Integer, Node> copyNodeMap(HashMap<Integer, Node> map) {
        HashMap<Integer, Node> res = new HashMap<>();
        for (int i : map.keySet()) {
            Node n = map.get(i);
            Node newNode = new Node(n.getId(), n.getType(), n.getSequence(), n.getzIndex());
            newNode.setLinks(new ArrayList<>(n.getLinks()));
            newNode.setParents(new ArrayList<>(n.getParents()));
            newNode.setGenomes(new ArrayList<>(n.getGenomes()));
            newNode.setPreviousLevelNodesIds(new ArrayList<>(n.getPreviousLevelNodesIds()));
            newNode.setCollapseLevel(String.valueOf(n.getCollapseLevel()));
            // Annotations should for now only be shown at the most zoomed in level.
            newNode.setAnnotations(new ArrayList<>());
            newNode.setNucleotides(n.getNucleotides());

            res.put(i, newNode);
        }

        return res;
    }

    /**
     * Reduce the number of nodes in a graph by collapsing vertically and horizontally.
     *
     * @param map       A HashMap containing all nodes in the graph.
     * @param zoomLevel The current zoomLevel
     * @return A collapsed map.
     */
    public static HashMap<Integer, Node> collapse(HashMap<Integer, Node> map, int zoomLevel) {
        HashMap<Integer, Node> nodeMap = new HashMap<>(copyNodeMap(map));
        determineParents(nodeMap);

        for (int idx = 1; idx < startMapSize; idx++) {
            Node parent = nodeMap.get(idx);
            if (parent == null) {
                continue;
            }
            if (zoomLevel > 0) {
                boolean collapsed = true;
                int collapseCount = 0;
                while (collapsed && collapseCount < 6) {
                    collapsed = collapseNodeSequence(nodeMap, parent, zoomLevel);
                }
            }
            collapseBubble(nodeMap, parent, zoomLevel);
            collapseIndel(nodeMap, parent);
        }

        return nodeMap;
    }

    /**
     * Method to collapse the more complex paths. This is done through
     * traversing the nodes in BFS manner, while also making sure every path
     * leads to the same final node.
     *
     * @param nodeMap       A HashMap containing all nodes in the graph.
     * @param parent        A given parent node to be collapsed with its child.
     * @param zoomLevel     the zoom level of the previous levelMap.
     * @param maxComplexity The maximum allowed number of nodes that should be collapsed.
     * @return Whether the complex collapse action has succeeded.
     */
    public static Boolean collapseComplexPath(HashMap<Integer, Node> nodeMap, Node parent,
                                              int zoomLevel, int maxComplexity) {
        // Links must be present from parent --> child

        if (parent == null) { return false; }

        ArrayList<Node> collapsingNodes = new ArrayList<>();
        Node targetNode = findComplexPath(parent, collapsingNodes, nodeMap, maxComplexity);

        if (!collapsingNodes.isEmpty() && targetNode != null) {
            Node complexNode = collapsingNodes.get(0);
            complexNode.setType(CellType.COMPLEX);
            for (Node collapseNode : collapsingNodes) {
                if (!collapseNode.equals(complexNode)) {
                    collapseNodeIntoParent(complexNode, collapseNode, zoomLevel);
                    for (int parentId : collapseNode.getParents()) {
                        Node collapseParent = nodeMap.get(parentId);
                        if (collapseParent != null) {
                            collapseParent.removeLink(collapseNode.getId());
                            collapseParent.addLink(complexNode.getId());
                            complexNode.addParent(parentId);
                        }
                    }
                    parent.removeLink(collapseNode.getId());
                    targetNode.removeParent(collapseNode.getId());
                    nodeMap.remove(collapseNode.getId());
                }
            }
            complexNode.setLinks(new ArrayList<>());
            complexNode.addLink(targetNode.getId());
            complexNode.setParents(new ArrayList<>());
            complexNode.addParent(parent.getId());
            targetNode.addParent(complexNode.getId());
            return true;
        }
        return false;
    }

    /**
     * Finds a path of nodes to be collapsed to a complex node.
     *
     * @param parent the node at the start
     * @param collapsingNodes list all nodes in the path
     * @param nodeMap the node map the nodes reside in
     * @param maxComplexity the maximum allowed number of nodes to be collapsed
     * @return
     */
    private static Node findComplexPath(Node parent, ArrayList<Node> collapsingNodes,
                                        HashMap<Integer, Node> nodeMap, int maxComplexity) {
        int pathComplexity = 0;
        boolean foundTarget = false;
        Node targetNode = null;
        Stack<Node> nonVisitedNodes = pushChildrenToStack(parent, nodeMap);
        while (!nonVisitedNodes.isEmpty() && pathComplexity < maxComplexity) {
            Node sourceNode = nonVisitedNodes.pop();
            if (sourceNode == null) { continue; }
            for (int parentId : sourceNode.getParents()) {
                Node collapseParent = nodeMap.get(parentId);
                for (String genome : collapseParent.getGenomes()) {
                    if (!parent.getGenomes().contains(genome)) {
                        return null;
                    }
                }
            }
            pathComplexity++;
            if (foundTarget) {
                if (!sourceNode.equals(targetNode)) {
                    collapsingNodes.add(sourceNode);
                    for (int childId : sourceNode.getLinks(nodeMap)) {
                        nonVisitedNodes.push(nodeMap.get(childId));
                    }
                }
            } else {
                if (sourceNode.containsSameGenomes(parent)) {
                    targetNode = sourceNode;
                    foundTarget = true;
                } else {
                    collapsingNodes.add(sourceNode);
                    for (int childId : sourceNode.getLinks(nodeMap)) {
                        nonVisitedNodes.push(nodeMap.get(childId));
                    }
                }
            }
        }
        if (nonVisitedNodes.isEmpty()) { return targetNode; } else { return null; }
    }


    /**
     * Pushes all children of a node onto a stack and returns it
     * @param parent the parent node
     * @param nodeMap the node map the node resides in
     * @return the stack
     */
    private static Stack<Node> pushChildrenToStack(Node parent, HashMap<Integer, Node> nodeMap) {
        Stack<Node> nonVisitedNodes = new Stack<>();
        for (int parentChild : parent.getLinks()) {
            nonVisitedNodes.push(nodeMap.get(parentChild));
        }
        return nonVisitedNodes;
    }

    /**
     * Collapse a parent and its child horizontally.
     *
     * @param nodeMap   A HashMap containing all nodes in the graph.
     * @param parent    A given parent node to be collapsed with its child.
     * @param zoomLevel the zoom level of the previous levelMap
     * @return Whether the horizontal collapse action has succeeded.
     */
    public static Boolean collapseNodeSequence(HashMap<Integer, Node> nodeMap, Node parent, int zoomLevel) {
        // Links must be present from parent --> child
        if (parent == null) { return false; }
        List<Integer> childrenIds = parent.getLinks(nodeMap);

        //Parent may only have one child.
        if (childrenIds.size() != 1) { return false; }

        //Retrieve the single child of the node.
        Node child = nodeMap.get(childrenIds.get(0));

        // The child may only have one parent and child
        if (child.getLinks(nodeMap).size() != 1 || child.getParents(nodeMap).size() != 1) { return false; }

        parent.setType(CellType.COLLECTION);
        parent.setSequence("");
        collapseNodeIntoParent(parent, child, zoomLevel);

        // Retrieve the single grandchild of the node.
        Node grandChild = nodeMap.get(child.getLinks(nodeMap).get(0));

        // Add edge from node to grandchild.
        parent.addLink(grandChild.getId());
        parent.removeLink(child.getId());

        //remove edge from child to grandchild.
        child.removeLink(grandChild.getId());

        //add node as new parent of the grandchild and remove the child as parent.
        grandChild.addParent(parent.getId());
        grandChild.removeParent(child.getId());

        //Add genomes of the child to the parent.
        parent.unionGenomes(child);

        //Remove the child node that is collapsed.
        nodeMap.remove(child.getId());

        return true;
    }

    /**
     * Collapse a complex insertion or deletion
     *
     * @param nodeMap the NodeMap were currently at
     * @param parent  the parent of the inDel to collapse
     * @return Whether nodes have been collapsed.
     */
    public static Boolean collapseIndel(HashMap<Integer, Node> nodeMap, Node parent) {
        List<Integer> children = parent.getLinks(nodeMap);

        if (children.size() < 2) { return false; }
        // Loop through all children of the parent.
        for (int i = 0; i < children.size(); i++) {
            Node child = nodeMap.get(children.get(i));
            //Find children that have one child and one parent
            if (child.getLinks(nodeMap).size() == 1 && child.getParents(nodeMap).size() == 1) {
                int grandChildId = child.getLinks().get(0);
                //Check whether the node has another child as its own child.
                if (children.contains(grandChildId)) {
                    Node grandChild = nodeMap.get(grandChildId);
                    //Find out which nodes should be in the inDel node
                    List<String> allGenomes = new ArrayList<>(grandChild.getGenomes());
                    grandChild.getParents().stream().filter(grandchildParentId -> grandchildParentId != child.getId()
                            && grandchildParentId != parent.getId()).forEach(grandchildParentId -> {
                        Node other = nodeMap.get(grandchildParentId);
                        other.getGenomes().stream().filter(genome ->
                                allGenomes.contains(genome)).forEach(allGenomes::remove);
                    });
                    //Remove link from parent to the grandChild.
                    parent.removeLink(grandChildId);
                    //Change parent of the grandchild to the child node
                    grandChild.removeParent(parent.getId());

                    //Make the inserted node an Indel node.
                    child.setType(CellType.INDEL);
                    child.setSequence("");
                    child.setGenomes(allGenomes);
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Collapse a complex bubble
     *
     * @param nodeMap   the nodeMap were currently at
     * @param parent    the parent of the to collapse bubble
     * @param zoomLevel the zoom level of the previous levelMap
     * @return Whether nodes have been collapsed.
     */
    public static Boolean collapseBubble(HashMap<Integer, Node> nodeMap, Node parent, int zoomLevel) {
        List<Node> bubbleChildren = createBubble(parent, nodeMap);
        if (bubbleChildren.size() > 1) {
            for (Node child : bubbleChildren) {
                Node grandChild = nodeMap.get(child.getLinks(nodeMap).get(0));
                List<Node> bubble = new ArrayList<>();
                bubble.add(child);
                bubble.addAll(bubbleChildren.stream().filter(
                        otherChild -> !otherChild.equals(child) && grandChild.equals(
                                nodeMap.get(otherChild.getLinks(nodeMap).get(0)))).collect(Collectors.toList()));
                if (bubble.size() > 1) {
                    StringBuffer buf = new StringBuffer();
                    for (Node bubbleChild : bubble) {
                        if (!bubbleChild.equals(child)) {
                            collapseNodeIntoParent(child, bubbleChild, zoomLevel);
                            parent.removeLink(bubbleChild.getId());
                            grandChild.removeParent(bubbleChild.getId());
                            nodeMap.remove(bubbleChild.getId());
                        }

                        buf.append(bubbleChild.getSequence() + "\n");
                    }
                    changeNodeToBubble(bubble.size(), child, buf);
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Creates a list of nodes that are bubble children of the parent node
     *
     * @param parent the parent node
     * @param nodeMap the nodemap the ndoes reside in
     * @return the list of bubble children
     */
    private static List<Node> createBubble(Node parent, HashMap<Integer, Node> nodeMap) {
        List<Integer> children = parent.getLinks(nodeMap);
        List<Node> bubbleChildren = new ArrayList<>();
        // Add all children that have one child and
        // parent to the list of potential bubble nodes.
        for (int i = 0; i < children.size(); i++) {
            Node child = nodeMap.get(children.get(i));
            if (child.getLinks(nodeMap).size() == 1 && child.getParents(nodeMap).size() == 1) {
                bubbleChildren.add(child);
            }
        }
        return bubbleChildren;
    }

    /**
     * Sets all parameters of a node according to those of a bubble node.
     * @param bubbleSize The size of the to be created bubble
     * @param child the node that will represent the node
     * @param buf The information to be drawn in the bubble
     */
    private static void changeNodeToBubble(int bubbleSize, Node child, StringBuffer buf) {
        child.setType(CellType.BUBBLE);
        if (bubbleSize < 6 && buf.length() < 20) {
            child.setCollapseLevel("\n" + buf.toString());
        } else {
            child.setCollapseLevel("Long Sequence");
        }
        child.setSequence("");
    }

    /**
     * General method to collapse a node into another node
     *
     * @param parent    The node that will be retained
     * @param child     The node that will be collapsed
     * @param zoomLevel The previous zoom level
     */
    public static void collapseNodeIntoParent(Node parent, Node child, int zoomLevel) {
        parent.unionGenomes(child);
        parent.setNucleotides(parent.getNucleotides() + child.getNucleotides());
        parent.addPreviousLevelNodesIds(new ArrayList<>(child.getPreviousLevelNodesIds()));
        parent.addPreviousLevelNodesId(child.getId());
        if (levelMaps.size() > 0) {
            levelMaps.get(zoomLevel).get(child.getId());
            levelMaps.get(zoomLevel).get(child.getId()).setNextLevelNodeId(parent.getId());
        }
    }

    /**
     * Set the start map size.
     *
     * @param startMapSize The start map sie.
     */
    public static void setStartMapSize(int startMapSize) {
        GraphReducer.startMapSize = startMapSize;
    }
}
