package core.annotation;

import core.graph.Node;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Class representing a gene annotation according to the specification on
 * http://www.sequenceontology.org/gff3.shtml and differences in provided GFF data.
 *
 * @author Niels Warnars
 */
@SuppressFBWarnings("URF_UNREAD_FIELD")
@SuppressWarnings("PMD.UnusedPrivateField")
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
    private long idAttr;
    private String nameAttr;
    private String displayNameAttr;

    private List<Node> spannedNodes;
    private int offsetInFirstSpannedNode;
    private int offsetInLastSpannedNode;

    /**
     * Sets up a gene annotation.
     */
    public Annotation() {
        seqid = "";
        source = "";
        type = "";
        start = -1;
        end = -1;
        score = -1;
        strand = "";
        phase = "";
        callhounClassAttr = "";
        idAttr = -1;
        nameAttr = "";
        displayNameAttr = "";

        spannedNodes = new ArrayList<>();
        offsetInFirstSpannedNode = -1;
        offsetInLastSpannedNode = -1;
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
     * Sets the score.
     * @param score The score.
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
    public long getIdAttr() {
        return idAttr;
    }

    /**
     * Sets the idAttr.
     * @param idAttr The idAttr.
     */
    public void setIdAttr(long idAttr) {
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
     * Adds a new spanned by the annotation.
     * @param node A new spanned by the annotation.
     */
    public void addSpannedNode(Node node) {
        spannedNodes.add(node);
    }


    @SuppressFBWarnings("EQ_COMPARETO_USE_OBJECT_EQUALS")
    @Override
    public int compareTo(Annotation o) {
        if (this.getStart() >= o.getStart()) {
            return 1;
        } else if (this.getStart() < o.getStart()) {
            return -1;
        }

        return 0;
    }

    /**
     * Determines the nodes spanned by the annotation.
     *
     * @param startLoopIdx The index to start the enumeration at.
     * @param nodeMap A hash map of nodes in the reference.
     * @param nodeMapSize The original size of the node map.
     * @return the new startLoopIndex.
     */
    public int detNodesSpannedByAnnotation(int startLoopIdx, HashMap<Integer, Node> nodeMap,
                                           int nodeMapSize) {
        Boolean nodesMustBeAdded = false;
        for (int idx = startLoopIdx; idx < nodeMapSize; idx++) {
            Node n = nodeMap.get(idx);
            if (n == null) {
                continue;
            }

            int nLower = n.getzIndex();
            int nUpper = n.getzIndex() + n.getSequence().length();

            if (nLower <= start && nUpper >= start) {
                offsetInFirstSpannedNode = start - n.getzIndex();
                nodesMustBeAdded = true;
            }

            if (nodesMustBeAdded) {
                addSpannedNode(n);
            }

            if (nLower <= end && nUpper >= end) {
                offsetInLastSpannedNode = end - n.getzIndex();
                return n.getId();
            }
        }

        return -1;
    }
}
