package application.controllers;

import javafx.geometry.Orientation;
import javafx.scene.control.ScrollBar;

/**
 * Created by Daphne van Tetering on 5-5-2016.
 */
public class ScrollFactory {
    private MainController mainController;

    public ScrollFactory(MainController controller) {
        this.mainController = controller;
    }

    public static ScrollBar createHorizontalScrollBar() {
        ScrollBar horizontalScrollbar = new ScrollBar();
        horizontalScrollbar.setOrientation(Orientation.HORIZONTAL);

        return horizontalScrollbar;
    }

    public static ScrollBar createVerticalScrollBar() {
        ScrollBar verticalScrollBar = new ScrollBar();
        verticalScrollBar.setOrientation(Orientation.VERTICAL);

        return verticalScrollBar;
    }
}
