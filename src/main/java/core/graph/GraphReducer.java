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
 * Created by Niels Warnars on 2-5-2016.
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
     * @return A list of node maps with a decreasing amount of nodes.
     */
    public static List<HashMap<Integer, Node>>
    createLevelMaps(HashMap<Integer, Node> startMap, int minDelta) {
        levelMaps.add(startMap);
        startMapSize = startMap.size();

        for (int i = 1;; i++) {
            HashMap<Integer, Node> levelMap = collapse(levelMaps.get(i - 1), i - 1);
            int previousMapSize = levelMaps.get(i - 1).size();
            int currentMapSize = levelMap.size();

            // Don't make any new zoom level if the number of nodes after reduction is only 2 less
            // than the number of nodes after previous reduction.
            if ((previousMapSize - currentMapSize) <= minDelta) {
                levelMaps.set(i - 1, levelMap);
                int maxDepth = 20;
                for (int j = i; maxDepth < 1001; j++) {
                    HashMap<Integer, Node> levelMap2 = secondStageCollapse(levelMaps.get(j - 1), j - 1, maxDepth);
                    int previousMapSize2 = levelMaps.get(j - 1).size();
                    int currentMapSize2 = levelMap2.size();
                    if (previousMapSize2 - currentMapSize2 == 0) {
                        levelMaps.set(j - 1, levelMap2);
                        maxDepth += 5;
                        j--;
                    } else {
                        levelMaps.add(levelMap2);
                    }
                }
                return levelMaps;
            } else {
                levelMaps.add(levelMap);
            }
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
        HashMap<Integer, Node> nodeMap = copyNodeMap(map);
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
        HashMap<Integer, Node> nodeMap = copyNodeMap(map);
        determineParents(nodeMap);

        for (int idx = 1; idx < startMapSize; idx++) {
            Node parent = nodeMap.get(idx);
            if (parent == null) {
                continue;
            }
            collapseBubble(nodeMap, parent, zoomLevel);
            collapseIndel(nodeMap, parent);
            if (zoomLevel > 0) {
                boolean collapsed = true;
                int collapseCount = 0;
                while (collapsed && collapseCount < 6) {
                    collapsed = collapseNodeSequence(nodeMap, parent, zoomLevel);
                    collapseCount++;
                }
            }
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

        if (parent == null) {
            return false;
        }

        Stack<Node> nonVisitedNodes = new Stack();
        ArrayList<Node> collapsingNodes = new ArrayList<>();
        Node targetNode = null;
        for (int parentChild : parent.getLinks()) {
            nonVisitedNodes.push(nodeMap.get(parentChild));
        }

        int pathComplexity = 0;
        boolean foundTarget = false;
        while (!nonVisitedNodes.isEmpty() && pathComplexity < maxComplexity) {
            Node sourceNode = nonVisitedNodes.pop();
            if (sourceNode == null) {
                continue;
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

        if (nonVisitedNodes.isEmpty() && !collapsingNodes.isEmpty() && foundTarget) {
            Node complexNode = collapsingNodes.get(0);
            complexNode.setType(CellType.COMPLEX);
            for (Node collapseNode : collapsingNodes) {
                if (!collapseNode.equals(complexNode)) {
                    collapseNodeIntoParent(complexNode, collapseNode, zoomLevel);
                    parent.removeLink(collapseNode.getId());
                    targetNode.removeParent(collapseNode.getId());
                    nodeMap.remove(collapseNode.getId());
                }
            }
            complexNode.setLinks(new ArrayList<>());
            complexNode.addLink(targetNode.getId());
            return true;
        }

        return false;
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
        if (parent == null) {
            return false;
        }

        List<Integer> childrenIds = parent.getLinks(nodeMap);

        //Parent may only have one child.
        if (childrenIds.size() != 1) {
            return false;
        }


        //Retrieve the single child of the node.
        Node child = nodeMap.get(childrenIds.get(0));


        // The child may only have one parent and child
        if (child.getLinks(nodeMap).size() != 1) {
            return false;
        }

        if (child.getParents(nodeMap).size() != 1) {
            return false;
        }

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
    public static Boolean
    collapseIndel(HashMap<Integer, Node> nodeMap, Node parent) {
        List<Integer> children = parent.getLinks(nodeMap);

        if (children.size() < 2) {
            return false;
        }
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
                        other.getGenomes().stream().filter(
                                genome -> allGenomes.contains(genome)).forEach(allGenomes::remove);
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
        List<Integer> children = parent.getLinks(nodeMap);
        List<Node> bubbleChildren = new ArrayList<>();
        if (children.size() > 1) {
            // Add all children that have one child and
            // parent to the list of potential bubble nodes.
            for (int i = 0; i < children.size(); i++) {
                Node child = nodeMap.get(children.get(i));
                if (child.getLinks(nodeMap).size() == 1 && child.getParents(nodeMap).size() == 1) {
                    bubbleChildren.add(child);
                }
            }
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
                    child.setType(CellType.BUBBLE);


                    if (bubble.size() < 6 && buf.length() < 20) {
                        child.setCollapseLevel("\n" + buf.toString());
                    } else {
                        child.setCollapseLevel("Long Sequence");
                    }

                    child.setSequence("");
                    return true;
                }
            }
        }
        return false;
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

    /**
     * Set the level maps.
     *
     * @param levelMaps The level maps.
     */
    public static void setLevelMaps(List<HashMap<Integer, Node>> levelMaps) {
        GraphReducer.levelMaps = levelMaps;
    }
}
