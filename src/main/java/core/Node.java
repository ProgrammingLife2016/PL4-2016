package core;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Skullyhoofd on 25/04/2016.
 * A Node in the genome.
 */
@SuppressWarnings("checkstyle:javadocmethod")
public class Node {

    /**
     * Identifier.
     */
    private int id;

    /**
     * Actual nucleic acid sequence of the node.
     */
    private String sequence;

    /**
     * 'Depth' of the node in the genome.
     */
    private int zIndex;

    /**
     * List of ids of Nodes to which this one is connected.
     */
    private List<Integer> links;

    /**
     * List of ids of parent Nodes which have this node as child.
     */
    private List<Integer> parents;
    /**
     * List of genomes of which this node is a part.
     */
    private List<String> genomes;

    /**
     * Node constructor.
     * @param id - Node identifier.
     * @param seq - Actual nucleic acid sequence contents of the node.
     * @param z - The 'depth' of the node in the genome.
     */
    public Node(int id, String seq, int z) {
        this.id = id;
        this.sequence = seq;
        this.zIndex = z;
        this.links = new ArrayList<>();
        this.parents = new ArrayList<>();
        this.genomes = new ArrayList<>();
    }

    /**
     * Add a link to another node.
     * @param link - The other node to which this one is linked.
     */
    public void addLink(int link) {
        this.links.add(link);
    }

    /**
     * Add a parent node's Id which links this node.
     * @param parent - A parent node.
     */
    public void addParent(int parent) {
        this.parents.add(parent);
    }

    /**
     * Add a genome to the node.
     * @param s - The other node to which this one is linked.
     */
    public void addAllGenome(String[] s) {
        this.genomes.addAll(Arrays.asList(s));
    }

    /**
     * Gets all live parents by comparing the link list with all existing nodes.
     * @param nodeMap A HashMap of all existing nodes.
     * @return A list of parents which are not null.
     */
    public List<Node> getLiveParents(HashMap<Integer, Node> nodeMap) {
        List<Node> liveParents = new ArrayList<>();

        for (int id : parents) {
            Node parent = nodeMap.get(id);
            if (parent != null) {  liveParents.add(parent); }
        }

        return liveParents;
    }

    /**
     * Gets all live links by comparing the link list with all existing nodes.
     * @param nodeMap A HashMap of all existing nodes.
     * @return A list of links which are not null.
     */
    public List<Node> getLiveLinks(HashMap<Integer, Node> nodeMap) {
        List<Node> liveLinks = new ArrayList<>();

        for (int id : links) {
            Node child = nodeMap.get(id);
            if (child != null) { liveLinks.add(child); }
        }

        return liveLinks;
    }

    /**
     * toStrong method to represent Node as a string.
     *
     * @return - the string representation.
     */
    @Override
    public String toString() {
        return "Node{" +
                "id=" + id +
                ", sequence='" + sequence + '\'' +
                ", zIndex=" + zIndex +
                ", links=" + links +
                ", parents=" + parents +
                ", genomes=" + genomes +
                '}';
    }

    /** Getters & Setters. **/

    public int getId() {
        return id;
    }

    public String getSequence() {
        return sequence;
    }

    public void setSequence(String sequence) {
        this.sequence = sequence;
    }

    public int getzIndex() {
        return zIndex;
    }

    public void setzIndex(int zIndex) {
        this.zIndex = zIndex;
    }

    public List<Integer> getLinks() {
        return links;
    }

    public void setLinks(List<Integer> links) {
        this.links = links;
    }

    public List<Integer> getParents() {
        return parents;
    }

    public void setParents(List<Integer> parents) { this.parents = parents; }

    public List<String> getGenomes() {
        return genomes;
    }


}
