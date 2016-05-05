package application.controllers;

import javafx.scene.Group;
import javafx.scene.control.ScrollPane;

/**
 * Created by Daphne van Tetering on 3-5-2016.
 */
public class ZoomFactory {

    /**
     * zoom factory oonstructor method.
     */
    public ZoomFactory() {

    }

    /**
     * Create a new zoomer object.
     * @param s The scrollpane to be a added to the zoomer object.
     * @param g The group to be a added to the zoomer object.
     * @return  A zoomer object.
     */
    public static Zoomer createZoomer(ScrollPane s, Group g) {
        Zoomer z = new Zoomer(s, g);
        z.addListeners();
        return z;
    }

}
