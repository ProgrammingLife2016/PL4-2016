package application.fxobjects.cell;

import javafx.scene.paint.Color;

/**
 * Utility class for color values.
 * Created by Arthur on 6/1/16.
 */
public final class LineageColor {
    public static final Color LIN0 = Color.web("000000");
    public static final Color LIN1 = Color.web("ed00c3");
    public static final Color LIN2 = Color.web("0000ff");
    public static final Color LIN3 = Color.web("500079");
    public static final Color LIN4 = Color.web("ff0000");
    public static final Color LIN5 = Color.web("4e2c00");
    public static final Color LIN6 = Color.web("69ca00");
    public static final Color LIN7 = Color.web("ff7e00");
    public static final Color LIN8 = Color.web("00ff9c");
    public static final Color LIN9 = Color.web("00ff9c");
    public static final Color LIN10 = Color.web("00ffff");

    public static final Color GLIN0 = Color.web("000000", 0.4);
    public static final Color GLIN1 = Color.web("ed00c3", 0.4);
    public static final Color GLIN2 = Color.web("0000ff", 0.4);
    public static final Color GLIN3 = Color.web("500079", 0.4);
    public static final Color GLIN4 = Color.web("ff0000", 0.4);
    public static final Color GLIN5 = Color.web("4e2c00", 0.4);
    public static final Color GLIN6 = Color.web("69ca00", 0.4);
    public static final Color GLIN7 = Color.web("ff7e00", 0.4);
    public static final Color GLIN8 = Color.web("00ff9c", 0.4);
    public static final Color GLIN9 = Color.web("00ff9c", 0.4);
    public static final Color GLIN10 = Color.web("00ffff", 0.4);

    public static final Color SLIN0 = Color.web("000000", 0.6);
    public static final Color SLIN1 = Color.web("ed00c3", 0.6);
    public static final Color SLIN2 = Color.web("0000ff", 0.6);
    public static final Color SLIN3 = Color.web("500079", 0.6);
    public static final Color SLIN4 = Color.web("ff0000", 0.6);
    public static final Color SLIN5 = Color.web("4e2c00", 0.6);
    public static final Color SLIN6 = Color.web("69ca00", 0.6);
    public static final Color SLIN7 = Color.web("ff7e00", 0.6);
    public static final Color SLIN8 = Color.web("00ff9c", 0.6);
    public static final Color SLIN9 = Color.web("00ff9c", 0.6);
    public static final Color SLIN10 = Color.web("00ffff", 0.6);

    /**
     * Constructor for the utility class.
     */
    private LineageColor() {
        throw new UnsupportedOperationException();
    }

    /**
     * Determines the color of the edges for the corresponding lineages in a highlighted situation.
     *
     * @param l the lineage code.
     * @return the color.
     */
    public static Color determineEdgeLinColor(int l) {
        switch (l) {
            case 0:
                return LIN0;
            case 1:
                return LIN1;
            case 2:
                return LIN2;
            case 3:
                return LIN3;
            case 4:
                return LIN4;
            case 5:
                return LIN5;
            case 6:
                return LIN6;
            case 7:
                return LIN7;
            case 8:
                return LIN8;
            case 9:
                return LIN9;
            case 10:
                return LIN10;
            default:
                break;
        }
        return LIN0;
    }

    /**
     * Determines the color of the edges for the corresponding
     * lineages in a non-highlighted situation.
     *
     * @param l the lineage code.
     * @return the color.
     */
    public static Color determineLeafLinColor(int l) {
        switch (l) {
            case 0:
                return GLIN0;
            case 1:
                return GLIN1;
            case 2:
                return GLIN2;
            case 3:
                return GLIN3;
            case 4:
                return GLIN4;
            case 5:
                return GLIN5;
            case 6:
                return GLIN6;
            case 7:
                return GLIN7;
            case 8:
                return GLIN8;
            case 9:
                return GLIN9;
            case 10:
                return GLIN10;
            default:
                break;
        }
        return SLIN0;
    }

    /**
     * Determines the color of the edges for the corresponding lineages in a highlighted situation.
     *
     * @param l the lineage code.
     * @return the color.
     */
    public static Color determineSelectedLeafLinColor(int l) {
        switch (l) {
            case 0:
                return SLIN0;
            case 1:
                return SLIN1;
            case 2:
                return SLIN2;
            case 3:
                return SLIN3;
            case 4:
                return SLIN4;
            case 5:
                return SLIN5;
            case 6:
                return SLIN6;
            case 7:
                return SLIN7;
            case 8:
                return SLIN8;
            case 9:
                return SLIN9;
            case 10:
                return SLIN10;
            default:
                break;
        }
        return SLIN0;
    }
}
