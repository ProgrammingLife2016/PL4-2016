package core;

import java.util.ArrayList;
import java.util.Arrays;
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
        this.links = new ArrayList<Integer>();
        this.genomes = new ArrayList<String>();
    }

    /**
     * Add a link to another node.
     * @param link - The other node to which this one is linked.
     */
    public void addLink(int link) {
        this.links.add(link);
    }

    /**
     * Add a genome to the node.
     * @param s - The other node to which this one is linked.
     */
    public void addAllGenome(String[] s) {
        this.genomes.addAll(Arrays.asList(s));
    }

    /**
     * toStrong method to represent Node as a string.
     * @return - the string representation.
     */
    @Override
    public String toString() {
        return "Node{"
                + "id=" + id
                + ", sequence='" + sequence + '\''
                + ", zIndex=" + zIndex
                + ", links=" + links.toString()
                + ", genomes=" + genomes.toString()
                + '}';
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

    public List<String> getGenomes() {
        return genomes;
    }


}
