package application.controllers;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;

/**
 * Created by Daphne van Tetering on 4-5-2016.
 */
public class MenuFactory {
    private static MenuItem loadPhylogeneticTree, loadGenome, resetView, shortcuts,
            showPhylogeneticTree, showGenomeSequence, test;
    private static MainController mainController;

    /**
     * Constructor method for this class.
     *
     * @param controller the mainController to use.
     */
    public MenuFactory(MainController controller) {
        mainController = controller;
    }

    /**
     * Method that creates a Menu.
     *
     * @param bar a MenuBar.
     * @return the completed MenuBar.
     */
    public MenuBar createMenu(MenuBar bar) {
        Menu fileMenu = initFileMenu();
        Menu viewMenu = initViewMenu();
        Menu helpMenu = initHelpMenu();

        bar.getMenus().addAll(fileMenu, viewMenu, helpMenu);
        return bar;

    }


    private Menu initHelpMenu() {
        shortcuts = initMenuItem("Shortcuts", new KeyCodeCombination(KeyCode.TAB), null);

        Menu helpMenu = initMenu("Help", shortcuts);
        return helpMenu;

    }

    private Menu initViewMenu() {
        showGenomeSequence = initMenuItem("Pick reference Genome Sequence", null,event ->  mainController.fillGraph(null));
        showPhylogeneticTree = initMenuItem("Show Phylogenetic Tree", null, null);
        resetView = initMenuItem("Reset", null, null);
        test = initMenuItem("Test", null, event -> mainController.switchScene());

        Menu viewMenu = initMenu("View", test, showGenomeSequence, showPhylogeneticTree, resetView);
        return viewMenu;

    }

    private Menu initFileMenu() {
        loadGenome = initMenuItem("Load Genome Sequence",
                new KeyCodeCombination(KeyCode.C, KeyCodeCombination.CONTROL_DOWN),
                t -> WindowFactory.createDirectoryChooser());
        loadPhylogeneticTree = initMenuItem("Load Phylogenetic Tree",
                new KeyCodeCombination(KeyCode.O, KeyCodeCombination.CONTROL_DOWN),
                null);

        Menu fileMenu = initMenu("File", loadGenome, loadPhylogeneticTree);
        return fileMenu;

    }

    private Menu initMenu(String title, final MenuItem... items) {
        Menu newMenu = new Menu(title);
        newMenu.getItems().addAll(items);
        return newMenu;
    }

    private MenuItem initMenuItem(String title, KeyCombination combination,
                                         EventHandler<ActionEvent> handler) {
        MenuItem newItem = new MenuItem(title);
        newItem.setAccelerator(combination);
        newItem.setOnAction(handler);
        return newItem;
    }
}
