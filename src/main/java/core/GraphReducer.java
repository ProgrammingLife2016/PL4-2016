package core;

import java.util.HashMap;
import java.util.List;
import java.util.Objects;

/**
 * Created by user on 2-5-2016.
 */
public class GraphReducer {


    public final static HashMap<Integer, Node> collapse(HashMap<Integer, Node> nodeMap) {
        for (int idx = 0; idx < nodeMap.size(); idx++) {

            Node node = nodeMap.get(idx);
            if (node != null) {
                if (node.getLinks().size() == 2) {
                System.out.println("OK");
                    Integer childId1 = node.getLinks().get(0);
                    Integer childId2 = node.getLinks().get(1);

                    Node child1 = nodeMap.get(childId1);
                    Node child2 = nodeMap.get(childId2);

                    if (child1.getLinks().size() == 1 && child2.getLinks().size() == 1) {
                        if (Objects.equals(child1.getLinks().get(0), child2.getLinks().get(0))) {

                            nodeMap.remove(childId2);
                            node.getLinks().remove(1);
                        }
                    }
                }
            }

        }

        return nodeMap;
    }
}