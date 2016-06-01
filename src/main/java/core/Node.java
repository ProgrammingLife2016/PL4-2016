package core;

import core.graph.cell.CellType;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Skullyhoofd on 25/04/2016.
 * A Node in the genome.
 */
@SuppressWarnings("PMD.OverrideBothEqualsAndHashcode")
@SuppressFBWarnings("HE_EQUALS_USE_HASHCODE")
public class Node {

    /**
     * Identifier.
     */
    private int id;

    /**
     * The type of node.
     */
    private CellType type;

    /**
     * Actual nucleic acid sequence of the node.
     */
    private String sequence;

    /**
     * The number of underlying collapses.
     */
    private int collapseLevel;

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
     *
     * @param id  - Node identifier.
     * @param seq - Actual nucleic acid sequence contents of the node.
     * @param z   - The 'depth' of the node in the genome.
     */
    public Node(int id, String seq, int z) {
        this(id, CellType.RECTANGLE, seq, z);
    }


    /**
     * Node constructor.
     *
     * @param id   - Node identifier.
     * @param type - The type of node.
     * @param seq  - Actual nucleic acid sequence contents of the node.
     * @param z    - The 'depth' of the node in the genome.
     */
    public Node(int id, CellType type, String seq, int z) {
        this.id = id;
        this.type = type;
        this.sequence = seq;
        this.zIndex = z;
        this.links = new ArrayList<>();
        this.parents = new ArrayList<>();
        this.genomes = new ArrayList<>();

        this.collapseLevel = 1;
    }

    /**
     * Add a link to another node.
     *
     * @param link - The other node to which this one is linked.
     */
    public void addLink(int link) {
        this.links.add(link);
    }

    /***
     * Remove a link between two nodes
     * @param link the link to be removed
     */
    public void removeLink(int link) {
        this.links.remove(Integer.valueOf(link));
    }

    /**
     * Add a parent node's Id which links this node.
     *
     * @param parent - A parent node.
     */
    public void addParent(int parent) {
        this.parents.add(parent);
    }

    /**
     * Add a genome to the node
     *
     * @param node the node of which genomes should be unioned.
     */
    public void unionGenomes(Node node) {
        for (String otherGenome : node.getGenomes()) {
            if (!genomes.contains(otherGenome)) {
                genomes.add(otherGenome);
            }
        }
    }

    /**
     * Add a genome to the node.
     *
     * @param s - The other node to which this one is linked.
     */
    public void addAllGenome(String[] s) {
        this.genomes.addAll(Arrays.asList(s));
    }


    /**
     * Method to remove parent
     *
     * @param parentId parent to be removed
     */
    public void removeParent(Integer parentId) {
        parents.remove(parentId);
    }

    /**
     * Method to add parent
     *
     * @param parentId parent to be added
     */
    public void addParent(Integer parentId) {
        parents.add(parentId);
    }

    /**
     * Gets all live parents by comparing the link list with all existing nodes.
     *
     * @param nodeMap A HashMap of all existing nodes.
     * @return A list of parents which are not null.
     */
    public List<Integer> getParents(HashMap<Integer, Node> nodeMap) {
        List<Integer> liveParents = new ArrayList<>();

        for (int id : parents) {
            Node parent = nodeMap.get(id);
            if (parent != null) {
                liveParents.add(parent.getId());
            }
        }

        return liveParents;
    }

    /**
     * Gets all live links by comparing the link list with all existing nodes.
     *
     * @param nodeMap A HashMap of all existing nodes.
     * @return A list of links which are not null.
     */
    public List<Integer> getLinks(HashMap<Integer, Node> nodeMap) {
        List<Integer> liveLinks = new ArrayList<>();

        for (int id : links) {
            Node child = nodeMap.get(id);
            if (child != null) {
                liveLinks.add(child.getId());
            }
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
        return "Node{"
                + "id=" + id
                + ", sequence='" + sequence + '\''
                + ", zIndex=" + zIndex
                + ", links=" + links
                + ", parents=" + parents
                + ", genomes=" + genomes
                + '}';
    }

    /**
     * Getters & Setters.
     **/

    /**
     * Get the node id.
     *
     * @return The node id.
     */
    public int getId() {
        return id;
    }

    /**
     * Set the node id.
     *
     * @param id The node id.
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Get the node type.
     *
     * @return The node type.
     */
    public CellType getType() {
        return type;
    }

    /**
     * Set the node type.
     *
     * @param type The node type.
     */
    public void setType(CellType type) {
        this.type = type;
    }

    /**
     * Get the nucleotide sequence.
     *
     * @return The nucleotide sequence.
     */
    public String getSequence() {
        return sequence;
    }

    /**
     * Set the nucleotide sequence.
     *
     * @param sequence The nucleotide sequence.
     */
    public void setSequence(String sequence) {
        this.sequence = sequence;
    }

    /**
     * Get the collapse level.
     *
     * @return The collapse level.
     */
    public int getCollapseLevel() {
        return collapseLevel;
    }

    /**
     * Increment the collapse level.
     *
     * @param collapseLevel The number to be added to the collapse level.
     */
    public void setCollapseLevel(int collapseLevel) {
        this.collapseLevel = collapseLevel;
    }

    /**
     * Get the zIndex.
     *
     * @return The zIndex.
     */
    public int getzIndex() {
        return zIndex;
    }

    /**
     * Set the zIndex.
     *
     * @param zIndex The zIndex.
     */
    public void setzIndex(int zIndex) {
        this.zIndex = zIndex;
    }

    /**
     * Get the node's children.
     *
     * @return The node's children.
     */
    public List<Integer> getLinks() {
        return links;
    }

    /**
     * Set the node's children.
     *
     * @param links The node's children.
     */
    public void setLinks(List<Integer> links) {
        this.links = links;
    }

    /**
     * Get the node's parents.
     *
     * @return The node's parents.
     */
    public List<Integer> getParents() {
        return parents;
    }

    /**
     * Set the node's parents.
     *
     * @param parents The node's parents.
     */
    public void setParents(List<Integer> parents) {
        this.parents = parents;
    }

    /**
     * Returns the genomes as a list.
     *
     * @return the genomes.
     */
    public List<String> getGenomes() {
        return genomes;
    }

    /**
     * Returns the genomes as a string.
     *
     * @return the String.
     */
    public String getGenomesAsString() {
        StringBuilder sb = new StringBuilder();

        for (String s : getGenomes()) {
            sb.append(" - ");
            sb.append(s);
            sb.append('\n');
        }
        return sb.toString();
    }

    /**
     * Sets the genomes through the node.
     *
     * @param genomes The genomes through the node.
     */
    public void setGenomes(List<String> genomes) {
        this.genomes = genomes;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        return id == ((Node) o).id;

    }

}
