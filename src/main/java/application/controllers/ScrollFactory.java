package application.controllers;

import javafx.geometry.Orientation;
import javafx.scene.control.ScrollBar;

/**
 * Created by Daphne van Tetering on 5-5-2016.
 */
public class ScrollFactory {
    private MainController mainController;

    /**
     * Create a new ScrollFactory.
     * @param controller    The main controller.
     */
    public ScrollFactory(MainController controller) {
        this.mainController = controller;
    }

    /**
     * Create a horizontal scrollbar.
     * @return  A horizontal scrollbar.
     */
    public static ScrollBar createHorizontalScrollBar() {
        ScrollBar horizontalScrollbar = new ScrollBar();
        horizontalScrollbar.setOrientation(Orientation.HORIZONTAL);

        return horizontalScrollbar;
    }

    /**
     * Create a vertical scrollbar.
     * @return A vertical scrollbar.
     */
    public static ScrollBar createVerticalScrollBar() {
        ScrollBar verticalScrollBar = new ScrollBar();
        verticalScrollBar.setOrientation(Orientation.VERTICAL);

        return verticalScrollBar;
    }
}
