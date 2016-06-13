package application.fxobjects;

import javafx.scene.paint.Color;

/**
 * Utility class for color values.
 * Created by Arthur on 6/1/16.
 */
public final class LineageColor {

    private static final String[] COLOR_STRINGS
            = new String[]{"000000", "ed00c3", "0000ff", "500079",
            "ff0000", "4e2c00", "69ca00", "ff7e00", "00ff9c", "00ff9c", "00ffff"};

    /**
     * Constructor for the utility class.
     */
    private LineageColor() {
    }

    /**
     * Determines the color of the edges for the corresponding lineages in a highlighted situation.
     *
     * @param l the lineage code.
     * @return the color.
     */
    public static Color determineEdgeLinColor(int l) {
        if (l < COLOR_STRINGS.length) {
            return Color.web(COLOR_STRINGS[l]);
        } else {
            return Color.web(COLOR_STRINGS[0]);
        }
    }

    /**
     * Determines the color of the edges for the corresponding
     * lineages in a non-highlighted situation.
     *
     * @param l the lineage code.
     * @return the color.
     */
    public static Color determineLeafLinColor(int l) {
        if (l < COLOR_STRINGS.length) {
            return Color.web(COLOR_STRINGS[l], 0.4);
        } else {
            return Color.web(COLOR_STRINGS[0], 0.4);
        }
    }

    /**
     * Determines the color of the edges for the corresponding lineages in a highlighted situation.
     *
     * @param l the lineage code.
     * @return the color.
     */
    public static Color determineSelectedLeafLinColor(int l) {
        if (l < COLOR_STRINGS.length) {
            return Color.web(COLOR_STRINGS[l], 0.6);
        } else {
            return Color.web(COLOR_STRINGS[0], 0.6);
        }
    }
}
