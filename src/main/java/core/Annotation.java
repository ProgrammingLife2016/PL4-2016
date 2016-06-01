package core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Class representing a gene annotation according to the specification on
 * http://www.sequenceontology.org/gff3.shtml and differences in provided GFF data.
 *
 * @author Niels Warnars
 */
public class Annotation implements Comparable<Annotation> {
    private String seqid;
    private String source;
    private String type;

    private int start;
    private int end;
    private float score;

    private String strand;
    private String phase;

    private String callhounClassAttr;
    private double idAttr;
    private String nameAttr;
    private String displayNameAttr;

    private List<Node> spannedNodes;
    private int OffsetInFirstSpannedNode;
    private int OffsetInLastSpannedNode;

    /**
     * Sets up a gene annotation.
     */
    public Annotation() {
        seqid = "";
        source = "";
        type = "";
        start = 0;
        end = 0;
        score = 0;
        strand = "";
        phase = "";
        callhounClassAttr = "";
        idAttr = 0;
        nameAttr = "";
        displayNameAttr = "";

        spannedNodes = new ArrayList<Node>();
    }

    /**
     * Gets the seqid.
     * @return The seqid.
     */
    public String getSeqid() {
        return seqid;
    }

    /**
     * Sets the seqid.
     * @param seqid The seqid.
     */
    public void setSeqid(String seqid) {
        this.seqid = seqid;
    }

    /**
     * Gets the source.
     * @return Sets the source.
     */
    public String getSource() {
        return source;
    }

    /**
     * Sets the source.
     * @param source The source.
     */
    public void setSource(String source) {
        this.source = source;
    }

    /**
     * Gets the type.
     * @return The type.
     */
    public String getType() {
        return type;
    }

    /**
     * Sets the type.
     * @param type The type.
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * Gets the start.
     * @return The start.
     */
    public int getStart() {
        return start;
    }

    /**
     * Sets the start.
     * @param start The start.
     */
    public void setStart(int start) {
        this.start = start;
    }

    /**
     * Gets the end.
     * @return The end.
     */
    public int getEnd() {
        return end;
    }

    /**
     * Sets the end.
     * @param end The end.
     */
    public void setEnd(int end) {
        this.end = end;
    }

    /**
     * Gets the score.
     * @return The score.
     */
    public float getScore() {
        return score;
    }

    /**
     * Sets
     * @param score
     */
    public void setScore(float score) {
        this.score = score;
    }

    /**
     * Gets the strand.
     * @return The strand.
     */
    public String getStrand() {
        return strand;
    }

    /**
     * Sets the strand.
     * @param strand The strand.
     */
    public void setStrand(String strand) {
        this.strand = strand;
    }

    /**
     * Gets the phase.
     * @return The phase.
     */
    public String getPhase() {
        return phase;
    }

    /**
     * Sets the phase.
     * @param phase The phase.
     */
    public void setPhase(String phase) {
        this.phase = phase;
    }

    /**
     * Gets the callhounClassAttr.
     * @return The callhounClassAttr.
     */
    public String getCallhounClassAttr() {
        return callhounClassAttr;
    }

    /**
     * Sets the callhounClassAttr.
     * @param callhounClassAttr The callhounClassAttr.
     */
    public void setCallhounClassAttr(String callhounClassAttr) {
        this.callhounClassAttr = callhounClassAttr;
    }

    /**
     * Gets the idAttr.
     * @return The idAttr.
     */
    public double getIdAttr() {
        return idAttr;
    }

    /**
     * Sets the idAttr.
     * @param idAttr The idAttr.
     */
    public void setIdAttr(double idAttr) {
        this.idAttr = idAttr;
    }

    /**
     * Gets the nameAttr.
     * @return The nameAttr.
     */
    public String getNameAttr() {
        return nameAttr;
    }

    /**
     * Sets the nameAttr.
     * @param nameAttr The nameAttr.
     */
    public void setNameAttr(String nameAttr) {
        this.nameAttr = nameAttr;
    }

    /**
     * Gets the displayNameAttr.
     * @return The displayNameAttr.
     */
    public String getDisplayNameAttr() {
        return displayNameAttr;
    }

    /**
     * Sets the displayNameAttr.
     * @param displayNameAttr The displayNameAttr.
     */
    public void setDisplayNameAttr(String displayNameAttr) {
        this.displayNameAttr = displayNameAttr;
    }

    /**
     * Gets the nodes spanned by the annotation.
     * @return The nodes spanned by the annotation.
     */
    public List<Node> getSpannedNodes() {
        return spannedNodes;
    }

    /**
     * Sets the nodes spanned by the annotation.
     * @param coveredNodes The nodes spanned by the annotation.
     */
    public void setSpannedNodes(List<Node> coveredNodes) {
        this.spannedNodes = coveredNodes;
    }

    /**
     * Adds a new spanned by the annotation.
     * @param node A new spanned by the annotation.
     */
    public void addSpannedNode(Node node) {
        spannedNodes.add(node);
    }
    @Override
    public String toString() {
        return "Annotation{" +
                "seqid='" + seqid + '\'' +
                ", source='" + source + '\'' +
                ", type='" + type + '\'' +
                ", start=" + start +
                ", end=" + end +
                ", score=" + score +
                ", strand='" + strand + '\'' +
                ", phase='" + phase + '\'' +
                ", callhounClassAttr='" + callhounClassAttr + '\'' +
                ", idAttr=" + idAttr +
                ", nameAttr='" + nameAttr + '\'' +
                ", displayNameAttr='" + displayNameAttr + '\'' +
                '}';
    }

    @Override
    public int compareTo(Annotation o) {
        if (this.getStart() > o.getStart())
            return 1;
        if (this.getStart() < o.getStart())
            return -1;

        return 0;
    }

    /**
     * Determines the nodes spanned by the annotation.
     *
     * @param startLoopIdx The index to start the enumeration at.
     * @param nodeMap A hash map of nodes in the reference.
     * @return the new startLoopIndex.
     */
    public int detNodesSpannedByAnnotation(int startLoopIdx, HashMap<Integer, Node> nodeMap) {
        int startNodeId = findFirstSpannedNode(nodeMap, startLoopIdx, this.start);
        int endNodeId = findLastSpannedNode(nodeMap, startLoopIdx, this.end);

        for (int idx = startNodeId; idx < endNodeId; idx++) {
            Node n = nodeMap.get(idx);
            if (n == null) continue;

            addSpannedNode(n);
        }

        return endNodeId;
    }

    /**
     * Finds the node containing the DNA sequence at start of the annotation.
     *
     * @param nodeMap A given node map.
     * @param startLoopIdx The index to start the enumeration at.
     * @param start The start index of the annotation.
     * @return The id of the Node containing the DNA sequence at start of the annotation.
     */
    public int findFirstSpannedNode(HashMap<Integer, Node> nodeMap, int startLoopIdx, int start) {
        for (int idx = startLoopIdx; idx < nodeMap.keySet().size(); idx++) {
            Node n = nodeMap.get(idx);
            if (n == null) continue;

            int nLower = n.getzIndex();
            int nUpper = n.getzIndex() + n.getSequence().length();

            if (nLower <= start && nUpper >= start) {
                return idx;
            }
        }

        return 0;
    }

    /**
     * Finds the node containing the DNA sequence at end of the annotation.
     *
     * @param nodeMap A given node map.
     * @param startLoopIdx The index to start the enumeration at.
     * @param end The end index of the annotation.
     * @return The id of the Node containing the DNA sequence at end of the annotation.
     */
    public int findLastSpannedNode(HashMap<Integer, Node> nodeMap, int startLoopIdx, int end) {
        for (int idx = startLoopIdx; idx < nodeMap.keySet().size(); idx++) {
            Node n = nodeMap.get(idx);
            if (n == null) continue;

            int nLower = n.getzIndex();
            int nUpper = n.getzIndex() + n.getSequence().length();

            if (nLower <= end && nUpper >= end) {
                return idx;
            }
        }

        return 0;
    }

}
