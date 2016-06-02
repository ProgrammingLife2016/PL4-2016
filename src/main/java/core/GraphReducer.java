package core;

import core.graph.cell.CellType;
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
            levelMaps.add(levelMap);
            int previousMapSize = levelMaps.get(i - 1).size();
            int currentMapSize = levelMaps.get(i).size();

            // Don't make any new zoom level if the number of nodes after reduction is only 2 less
            // than the number of nodes after previous reduction.
            if ((previousMapSize - currentMapSize) <= minDelta) {
                return levelMaps;
            }
        }

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
            newNode.setCollapseLevel(n.getCollapseLevel());
            newNode.setNucleotides(n.getNucleotides());

            res.put(i, newNode);
        }

        return res;
    }

    /**
     * Reduce the number of nodes in a graph by collapsing vertically and horizontally.
     *
     * @param map A HashMap containing all nodes in the graph.
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

            collapseBubble(nodeMap, parent);
            collapseIndel(nodeMap, parent);
            if (zoomLevel > 0) {
                boolean collapsed = true;
                int collapseCount = 0;
                while (collapsed && collapseCount < 6) {
                    collapsed = collapseNodeSequence(nodeMap, parent);
                    collapseCount++;
                }
            }
        }

        return nodeMap;
    }

    /**
     * Collapse a parent and its child horizontally.
     *
     * @param nodeMap A HashMap containing all nodes in the graph.
     * @param parent  A given parent node to be collapsed with its child.
     * @return Whether the horizontal collapse action has succeeded.
     */
    public static Boolean collapseNodeSequence(HashMap<Integer, Node> nodeMap, Node parent) {
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

        // Add up both collapse levels and add it to the parent
        int totalCollapseLevel = parent.getCollapseLevel() + child.getCollapseLevel();
        parent.setType(CellType.COLLECTION);
        parent.setSequence("");
        parent.setCollapseLevel(totalCollapseLevel);
        parent.setNucleotides(parent.getNucleotides() + child.getNucleotides());

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
     * @param parent the parent of the inDel to collapse
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
                    for (int grandchildParentId : grandChild.getParents()) {

                        if (grandchildParentId != child.getId()
                                && grandchildParentId != parent.getId()) {
                            Node other = nodeMap.get(grandchildParentId);
                            for (String genome : other.getGenomes()) {
                                if (allGenomes.contains(genome)) {
                                    allGenomes.remove(genome);
                                }
                            }
                        }
                    }
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
     * @param nodeMap the nodeMap were currently at
     * @param parent the parent of the to collapse bubble
     * @return Whether nodes have been collapsed.
     */
    public static Boolean collapseBubble(HashMap<Integer, Node> nodeMap, Node parent) {
        List<Integer> children = parent.getLinks(nodeMap);
        List<Node> bubbleChildren = new ArrayList<>();
        if (children.size() <= 1) {
            return false;
        }

        // Add all children that have one child and parent to the list of potential bubble nodes.
        for (int i = 0; i < children.size(); i++) {
            Node child = nodeMap.get(children.get(i));
            if (child.getLinks(nodeMap).size() == 1 && child.getParents(nodeMap).size() == 1) {
                bubbleChildren.add(child);
            }
        }

        for (Node child : bubbleChildren) {
            Node grandChild = nodeMap.get(child.getLinks(nodeMap).get(0));
            List<Node> bubble = new ArrayList<Node>();
            bubble.add(child);
            for (Node otherChild : bubbleChildren) {
                if (!otherChild.equals(child)
                        && grandChild.equals(nodeMap.get(otherChild.getLinks(nodeMap).get(0)))) {
                    bubble.add(otherChild);
                }
            }
            if (bubble.size() > 1) {
                for (Node bubbleChild : bubble) {
                    if (!bubbleChild.equals(child)) {
                        child.unionGenomes(bubbleChild);
                        child.setNucleotides(child.getNucleotides() + bubbleChild.getNucleotides());
                        parent.removeLink(bubbleChild.getId());
                        grandChild.removeParent(bubbleChild.getId());
                        nodeMap.remove(bubbleChild.getId());
                    }
                }
                child.setType(CellType.BUBBLE);
                child.setCollapseLevel(bubble.size());
                child.setSequence("");
                return true;
            }
        }
        return false;
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
