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

            for (int childId : parent.getLinks()) {
                nodeMap.get(childId).addParent(parent.getId());
            }
        }
    }
    public final static void collapse(HashMap<Integer, Node> nodeMap) {
        determineParents(nodeMap);

        for (int idx = 1; idx <= nodeMap.size(); idx++) {
            Node parent = nodeMap.get(idx);
            System.out.println(parent);

            if (parent == null) { continue; }
            List<Node> children = parent.getLiveLinks(nodeMap);

            if (children.size() >= 2 && children.size() <= 2) {
                Node grandChild = null;

                for (Node node : children) {
                    List<Node> liveLinks = node.getLiveLinks(nodeMap);
                    List<Integer> parents = node.getParents();

                    if (liveLinks.size() != 1 || parents.size() != 1) { break; }

                    if (grandChild == null) {
                        grandChild = liveLinks.get(0);
                    } else if (liveLinks.get(0).getId() != grandChild.getId()) {
                        break;
                    }
                }
                //System.out.println("Removing node...");
                nodeMap.remove(children.get(1).getId());
            }
        }
    }
}