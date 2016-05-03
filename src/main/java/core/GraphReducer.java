package core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

/**
 * Created by Niels Warnars on 2-5-2016.
 */
public class GraphReducer {


    public final static HashMap<Integer, Node> collapse(HashMap<Integer, Node> nodeMap) {
        for (int idx = 0; idx < nodeMap.size(); idx++) {

            Node parent = nodeMap.get(idx);
            if (parent != null) {
                List<Node> children = parent.getLiveLinks(nodeMap);

                int collapseLevels = 1;
                if (children.size() >= 4) {
                   collapseLevels = (int) Math.floor(children.size() / 2.0);
                }

                if (parent.getLiveLinks(nodeMap).size() >= 2) {
                    Node child1 = children.get(0);
                    Node child2 = children.get(children.size() - 1);

                    for (Node node : children) {

                    }
                    List<Node> grantChildren1 = new ArrayList<Node>();
                    List<Node> grantChildren2 = new ArrayList<Node>();

                }
            }

        }

        return nodeMap;
    }
}