package application.controllers;

import javafx.scene.Group;
import javafx.scene.control.ScrollPane;

/**
 * Created by Daphne van Tetering on 3-5-2016.
 */
public class ZoomFactory{

    public ZoomFactory() {


    }

    public static Zoomer createZoomer(ScrollPane s, Group g) {
        Zoomer z = new Zoomer(s, g);
        z.addListeners();
        return z;
    }

}
