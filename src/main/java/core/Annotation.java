package core;

/**
 * Class representing a gene annotation according to the specification on
 * http://www.sequenceontology.org/gff3.shtml and differences in provided GFF data.
 *
 * @author Niels Warnars
 */
public class Annotation {
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

    /**
     * Sets up a gene annotation.
     *
     * @param seqid The ID of the current feature.
     * @param source The generator of this feature.
     * @param type The type of feature.
     * @param start The start coordinate of the feature.
     * @param end The end coordinate of the feature.
     * @param score The score of the feature.
     * @param strand The strand of the feature.
     * @param phase The phase of the feature.
     * @param callhounClassAttr The type of data.
     * @param idAttr The ID of the feature.
     * @param nameAttr The name of the feature.
     * @param displayNameAttr The display name for the feature.
     */
    public Annotation(String seqid, String source, String type, int start, int end, float score,
                      String strand, String phase, String callhounClassAttr, double idAttr,
                      String nameAttr, String displayNameAttr) {

        this.seqid = seqid;
        this.source = source;
        this.type = type;
        this.start = start;
        this.end = end;
        this.score = score;
        this.strand = strand;
        this.phase = phase;
        this.callhounClassAttr = callhounClassAttr;
        this.idAttr = idAttr;
        this.nameAttr = nameAttr;
        this.displayNameAttr = displayNameAttr;
    }

    /**
     * Gets the seqid.
     *
     * @return The seqid.
     */
    public String getSeqid() {
        return seqid;
    }

    /**
     * Gets the source.
     *
     * @return The source.
     */
    public String getSource() {
        return source;
    }

    /**
     * Gets the type.
     *
     * @return The type.
     */
    public String getType() {
        return type;
    }

    /**
     * Gets the start coordinate.
     *
     * @return The start coordinate.
     */
    public int getStart() {
        return start;
    }

    /**
     * Gets the end coordinate.
     *
     * @return The end coordinate.
     */
    public int getEnd() {
        return end;
    }

    /**
     * Gets the score of the feature.
     *
     * @return The score of the feature.
     */
    public float getScore() {
        return score;
    }

    /**
     * Gets the strand.
     *
     * @return The strand.
     */
    public String getStrand() {
        return strand;
    }

    /**
     * Gets the phase.
     *
     * @return The phase.
     */
    public String getPhase() {
        return phase;
    }

    /**
     * Gets the callhounClass attribute.
     *
     * @return The callhounClass attribute.
     */
    public String getCallhounClassAttr() {
        return callhounClassAttr;
    }

    /**
     * Gets the ID attribute.
     *
     * @return The ID attribute.
     */
    public double getIdAttr() {
        return idAttr;
    }

    /**
     * Gets the Name attribute.
     *
     * @return The Name attribute.
     */
    public String getNameAttr() {
        return nameAttr;
    }

    /**
     * Gets the displayName attribute.
     *
     * @return The displayName attribute.
     */
    public String getDisplayNameAttr() {
        return displayNameAttr;
    }
    
}
