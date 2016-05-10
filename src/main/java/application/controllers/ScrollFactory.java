package application.controllers;

import javafx.geometry.Orientation;
import javafx.scene.control.ScrollBar;

/**
 * Created by Daphne van Tetering on 5-5-2016.
 */
public class ScrollFactory {
    private MainController mainController;

    /**
     * Constructor method.
     *
     * @param controller the controller to use.
     */
    public ScrollFactory(MainController controller) {
        this.mainController = controller;
    }

    /**
     * Creates a horizontal scroll bar.
     *
     * @return the scroll bar.
     */
    public static ScrollBar createHorizontalScrollBar() {
        ScrollBar horizontalScrollbar = new ScrollBar();
        horizontalScrollbar.setOrientation(Orientation.HORIZONTAL);

        return horizontalScrollbar;
    }

    /**
     * Creates a vertical scroll bar.
     *
     * @return the scroll bar.
     */
    public static ScrollBar createVerticalScrollBar() {
        ScrollBar verticalScrollBar = new ScrollBar();
        verticalScrollBar.setOrientation(Orientation.VERTICAL);

        return verticalScrollBar;
    }
}
