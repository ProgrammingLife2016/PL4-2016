package application.controllers;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;

import java.io.IOException;
import java.io.InputStream;

/**
 * Controller class, used when creating other controllers.
 *
 * @param <T> FXML-root.
 * @version 1.0
 * @since 25-04-2016
 */
public abstract class Controller<T extends Parent> implements Initializable {
    private T root;
    private SimpleBooleanProperty visible = new SimpleBooleanProperty(true);

    /**
     * Constructor: generate a Controller.
     *
     * @param root parameter set as root through the Controller.
     */
    public Controller(final T root) {
        this.root = root;
        root.visibleProperty().bind(visible);
    }

    /**
     * Method to load FXML files.
     *
     * @param filePath location of the FXML file to be loaded.
     */
    @SuppressFBWarnings({"OBL_UNSATISFIED_OBLIGATION", "UI_INHERITANCE_UNSAFE_GETRESOURCE"})
    public final void loadFXMLfile(String filePath) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(filePath));
            InputStream stream = this.getClass().getResourceAsStream(filePath);

            loader.setRoot(root);
            loader.setController(this);
            loader.load(stream);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Method to return the root.
     *
     * @return root the classes' root.
     */
    public T getRoot() {
        return root;
    }

    /**
     * Method to set the root.
     *
     * @param root new Root to be set.
     */
    public void setRoot(T root) {
        this.root = root;
    }

}